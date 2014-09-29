nodist_pkgdata_DATA = @PACKAGE@.jar

CONFIG_CLEAN_FILES = @PACKAGE@.jar

BUILDTMP=$(shell date +%s)

JAVAROOT = bld


#depjars/bcprov-jdk15on-151.jar is signed and needs to be present in the classpath declaration for now

@PACKAGE@.jar: ../java/@PACKAGE@.jar @GETOPTJAR@ ../depjars/bcpkix-jdk15on-151.jar ../depjars/bcprov-jdk15on-151.jar ../depjars/bcpg-jdk15on-151.jar ../depjars/commons-lang3-3.1.jar ../depjars/commons-net-3.1.jar ../depjars/commons-net-ssh.jar ../depjars/ftp4j-1.7.1.jar ../depjars/jzlib-1.1.2.jar ../depjars/slf4j-api-1.7.5.jar ../depjars/slf4j-simple-1.7.5.jar
	-rm -fr $(@D)/.workdir
	mkdir -p $(@D)/.workdir
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	rm -fr $(@D)/.workdir/META-INF/{maven,LICENSE.txt,MANIFEST.MF,{SUN_MICR,TESTKEY,BCKEY}.{DSA,RSA,SF}}
	echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.filexfer.WinchFactory"  >> $(@D)/.workdir/MANIFEST.MF
	find $(@D)/.workdir '(' -name \*.java -o -name COPYING.LIB -o -name \*.html -o -name README -o -name Makefile -o -name ChangeLog -o -name GetoptDemo.class -o -name LANGUAGES -o -name \*.xml ')' -exec rm {} \;
	rm -fr $(@D)/.workdir/org/bouncycastle/jcajce/provider/asymmetric
	(cd $(@D)/.workdir && jar cm MANIFEST.MF META-INF gnu it org) > $@
	rm -fr $(@D)/.workdir

