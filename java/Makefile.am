nodist_java_JAVA = FileTransferWinch.java WinchFactory.java FTP4J.java version.java ApacheCommonsNetSFTP.java sftp/Handler.java sftp/SFTPURLConnection.java
nodist_java_DATA = @PACKAGE@.jar
javadir = $(pkgdir)

EXTRA_DIST = $(nodist_java_JAVA) WinchFactory.java.in
CONFIG_CLEAN_FILES = @PACKAGE@.jar WinchFactory.java version.java

AM_JAVACFLAGS= @JAVAC_FLAGS@ -cp $(JAVAROOT):$(top_builddir)/depjars/commons-lang3-3.1.jar:$(top_builddir)/depjars/commons-net-3.1.jar:$(top_builddir)/depjars/ftp4j-1.7.1.jar:$(top_builddir)/depjars/slf4j-api-1.7.5.jar:$(top_builddir)/depjars/jzlib-1.1.2.jar:$(top_builddir)/depjars/bcpkix-jdk15on-151.jar:$(top_srcdir)/depjars/bcprov-jdk15on-151.jar:$(top_srcdir)/depjars/commons-net-ssh.jar:$(top_builddir)/depjars/log4j-1.2.17.jar:@GETOPTJAR@

BUILDTMP=$(shell date +%s)

BUILT_SOURCES = 

JAVAROOT = bld
BUILT_SOURCES += javaroot.stamp

javaroot.stamp: $(nodist_java_JAVA)
	mkdir -p $(JAVAROOT) && touch $@

@PACKAGE@.jar: classjava.stamp $(nodist_java_JAVA)
	awk -v BUILDTOP="bld" '/^package / {gsub (";$$",""); gsub ("\\.","/",$$2); SUBS[$$2]=$$2 } END { FIRST=0; for (n in SUBS) { if (0 == FIRST++) printf "jar cvf $@"; printf " -C %s %s",BUILDTOP,n }; printf "\n"; }' $^ | $(SHELL)
	touch @PACKAGE@.class

clean-local:
	CLASSES=`for i in $(nodist_java_JAVA); do awk -v FILE="$$i" '/^package / {gsub (";$$",""); gsub (".java",".class",FILE); gsub ("\\\\.","/",$$2); printf "%s/%s\n",$$2,FILE}' $(srcdir)/$$i; done`; (cd $(top_builddir)/ && rm -f $$CLASSES)
	rm -fr $(JAVAROOT) javaroot.stamp


