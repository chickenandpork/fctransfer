DIST_SUBDIRS =	depjars java tests-autotest
SUBDIRS =	depjars
if DO_JAVA
SUBDIRS +=	java
endif
SUBDIRS +=	tests-autotest

ACLOCAL_AMFLAGS = -I m4

# if missing mkinstalldirs install-sh trigger errors, ignore the result code on the "system (cp ...)" command in automake
dnl CONFIG_CLEAN_FILES = aclocal.m4 config.guess config.sub config.h config.h.in ltconfig ltmain.sh missing mkinstalldirs install-sh Doxyfile @PACKAGE@.spec @PACKAGE@.jar changes.atom
EXTRA_DIST = @PACKAGE@.spec



nodist_pkgdata_DATA = @PACKAGE@.jar
CONFIG_CLEAN_FILES = @PACKAGE@.jar

AM_JAVACFLAGS= -cp $(top_builddir)/java-pre/getopt.jar:$(top_builddir):.

BUILDTMP=$(shell date +%s)
BUILT_SOURCES =


@PACKAGE@.jar: depjars/commons-lang3-3.1.jar depjars/commons-net-3.1.jar depjars/ftp4j-1.7.1.jar java/@PACKAGE@.jar @GETOPTJAR@
	-rm -fr $(@D)/.workdir
	mkdir -p $(@D)/.workdir
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	rm -fr $(@D)/.workdir/META-INF/{maven,LICENSE.txt,MANIFEST.MF,{SUN_MICR,TESTKEY}.{RSA,SF}}
	echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.filexfer.WinchFactory"  >> $(@D)/.workdir/MANIFEST.MF
	(cd $(@D)/.workdir && jar cm MANIFEST.MF META-INF gnu it org) > $@
	rm -fr $(@D)/.workdir


if DO_DOXYGEN
doc: Doxyfile
	$(DOXYGEN)
endif

if DO_ASTYLE
formatting: java/FTP4J.java java/FileTransferWinch.java java/WinchFactory.java.in java/version.java.in
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

endif

if HAVE_RPMBUILD
rpm: dist-bzip2
	@RPMBUILD@ --define 'skipcheck 1' -ta $(PACKAGE)-$(VERSION).tar.bz2
endif
