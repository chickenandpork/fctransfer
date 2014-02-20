DIST_SUBDIRS =	depjars java tests-autotest
SUBDIRS =	depjars
if DO_JAVA
SUBDIRS +=	java
endif
SUBDIRS +=	tests-autotest

ACLOCAL_AMFLAGS = -I m4

# if missing mkinstalldirs install-sh trigger errors, ignore the result code on the "system (cp ...)" command in automake
dnl CONFIG_CLEAN_FILES = aclocal.m4 config.guess config.sub config.h config.h.in ltconfig ltmain.sh missing mkinstalldirs install-sh Doxyfile @PACKAGE@.spec @PACKAGE@.jar changes.atom
EXTRA_DIST = @PACKAGE@.spec gitdescribe



nodist_pkgdata_DATA = @PACKAGE@.jar
CONFIG_CLEAN_FILES = @PACKAGE@.jar

AM_JAVACFLAGS= -cp $(top_builddir)/java-pre/getopt.jar:$(top_builddir):.

BUILDTMP=$(shell date +%s)
BUILT_SOURCES =

#depjars/bcprov-jdk15on-149.jar is signed and needs to be present in the classpath declaration for now

@PACKAGE@.jar: java/@PACKAGE@.jar @GETOPTJAR@ depjars/bcpkix-jdk15on-149.jar depjars/bcprov-jdk15on-149.jar depjars/bcpg-jdk15on-149.jar depjars/commons-lang3-3.1.jar depjars/commons-net-3.1.jar depjars/commons-net-ssh.jar depjars/ftp4j-1.7.1.jar depjars/jzlib-1.1.2.jar depjars/slf4j-api-1.7.5.jar depjars/slf4j-simple-1.7.5.jar
	-rm -fr $(@D)/.workdir
	mkdir -p $(@D)/.workdir
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	rm -fr $(@D)/.workdir/META-INF/{maven,LICENSE.txt,MANIFEST.MF,{SUN_MICR,TESTKEY,BCKEY}.{DSA,RSA,SF}}
	echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.filexfer.WinchFactory"  >> $(@D)/.workdir/MANIFEST.MF
	rm -fr $(@D)/.workdir/org/bouncycastle/jcajce/provider/asymmetric
	(cd $(@D)/.workdir && jar cm MANIFEST.MF META-INF gnu it org) > $@
	echo rm -fr $(@D)/.workdir


if DO_DOXYGEN
doc: htdocs/README.dox Doxyfile
	$(DOXYGEN)
	@if test \! -d $(<D)/.git; then echo "subdir $(<D) not set as a gh-pages branch; see http://rickfoosusa.blogspot.com/2011/10/howto-use-doxygen-with-github.html"; git branch|grep gh-pages || echo git clone --branch gh-pages $$(git config --get remote.origin.url) $(<D); grep $(<D) .gitignore || echo "echo $(<D) > .gitignore"; echo "then delete everything in $(<D), commit, and push"; exit 1; fi
	@grep $(<D) .gitignore || { echo "echo $(<D) > .gitignore"; exit 1; }
	(cd $(<D) && git add -A)
	if test -n "$(DOXYMESSAGE)" ; then (cd $(<D) && git commit -a -m "$(DOXYMESSAGE)" && git push); fi

htdocs/README.dox: README.md Makefile
if DO_DOXYGEN_MD
	awk 'BEGIN { print "@file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } { printf "%s\n",$$0; } END {print "" }' $< > $@
else
	awk 'BEGIN { print "/** @file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } /@PACKAGE@$$/ {next} /^=+$$/ {next} { printf " * %s\n",$$0; } END {print " */" }' $< > $@
endif DO_DOXYGEN_MD_FALSE
endif


if DO_ASTYLE
formatting: java/FTP4J.java java/FileTransferWinch.java java/WinchFactory.java.in java/version.java.in java/ApacheCommonsNetSFTP.java
if DO_GITFORMATTING
	@git ls-files --other --exclude-standard --directory || { echo "###################### unignored local files: git add, git commit ?"; }
	@git diff --exit-code || { echo "###################### unstaged changes: git commit -a"; exit 1; }
	@git diff --cached --exit-code || { echo "###################### staged uncommitted changes: git commit"; exit 1; }
endif
	$(MAKE) check
	for i in $^; do $(ASTYLE) -A1 $$i; done
	$(MAKE) check
if DO_GITFORMATTING
	git commit -a -m 'formatting and whitespace'
endif DO_GITFORMATTING

endif DO_ASTYLE

if DO_GITFORMATTING
gitdescribe: .git/ORIG_HEAD
	git describe > $@
endif DO_GITFORMATTING


if HAVE_RPMBUILD
rpm: dist-bzip2
	@RPMBUILD@ --define 'skipcheck 1' -ta $(PACKAGE)-$(VERSION).tar.bz2
endif
