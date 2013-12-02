nodist_pkgdata_JAVA = FileTransferWinch.java WinchFactory.java FTP4J.java version.java ApacheCommonsNetSFTP.java sftp/Handler.java sftp/SFTPURLConnection.java
nodist_pkgdata_DATA = $(notdir $(subdir)).jar

EXTRA_DIST = $(nodist_pkgdata_JAVA) WinchFactory.java.in
CONFIG_CLEAN_FILES = $(notdir $(subdir)).jar

AM_JAVACFLAGS= -cp $(JAVAROOT):$(top_builddir)/depjars/commons-lang3-3.1.jar:$(top_builddir)/depjars/commons-net-3.1.jar:$(top_builddir)/depjars/ftp4j-1.7.1.jar:$(top_builddir)/depjars/slf4j-api-1.7.5.jar:$(top_builddir)/depjars/jzlib-1.1.2.jar:$(top_builddir)/depjars/bcpkix-jdk15on-149.jar:$(top_builddir)/depjars/bcprov-jdk15on-149.jar:$(top_builddir)/depjars/commons-net-ssh.jar:$(top_builddir)/depjars/log4j-1.2.17.jar:@GETOPTJAR@

BUILDTMP=$(shell date +%s)

BUILT_SOURCES = 

JAVAROOT = bld
BUILT_SOURCES += javaroot.stamp

javaroot.stamp: $(nodist_pkgdata_JAVA)
	mkdir -p $(JAVAROOT) && touch $@

@PACKAGE@.jar: classnodist_pkgdata.stamp
	awk -v BUILDTOP="bld" '/^package / {gsub (";$$",""); gsub ("\\.","/",$$2); SUBS[$$2]=$$2 } END { FIRST=0; for (n in SUBS) { if (0 == FIRST++) printf "jar cvf $@"; printf " -C %s %s",BUILDTOP,n }; printf "\n"; }' $(nodist_pkgdata_JAVA) | $(SHELL)
	touch $(notdir $(subdir)).class

clean-local:
	CLASSES=`for i in $(nodist_pkgdata_JAVA); do awk -v FILE="$$i" '/^package / {gsub (";$$",""); gsub (".java",".class",FILE); gsub ("\\\\.","/",$$2); printf "%s/%s\n",$$2,FILE}' $(srcdir)/$$i; done`; (cd $(top_builddir)/ && rm -f $$CLASSES)


$(notdir $(subdir)).jar: @PACKAGE@.jar @GETOPTJAR@ $(BUILT_SOURCES) 
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.filexfer.FileTransferWinch" >> $(@D)/.workdir/MANIFEST.MF
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do case $$i in javaroot.stamp) ;; *) (cd $(@D)/.workdir && jar xf $$i) ;; esac; done
	(cd $(@D)/.workdir && jar cm MANIFEST.MF org) > $@
	rm -fr $(@D)/.workdir



