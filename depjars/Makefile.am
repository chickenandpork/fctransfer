depjardir = @datadir@/@PACKAGE@/jars
dist_depjar_DATA = $(JARS)


EXTRA_DIST = $(nodist_pkgdata_JAVA)
CONFIG_CLEAN_FILES = $(JARS) commons-lang3-3.1-bin.zip commons-net-3.1-bin.zip commons-net-3.3-bin.zip ftp4j-1.7.1.zip jzlib-1.1.2.zip log4j-1.2.17.zip slf4j-1.7.5.zip


AM_JAVACFLAGS= -cp $(top_builddir)/$(subdir)/$(JAVAROOT)

BUILDTMP=$(shell date +%s)

JARS = commons-net-3.1.jar commons-lang3-3.1.jar ftp4j-1.7.1.jar

%.jar: %-bin.zip
	unzip -oj $< '*/$@' && touch $@

commons-net-3.1-bin.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && unzip -t $${TMPDIR-/tmp}/$(@F) > /dev/null || rm -f $${TMPDIR-/tmp}/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://archive.apache.org/dist/commons/net/binaries/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

commons-lang3-3.1-bin.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && unzip -t $${TMPDIR-/tmp}/$(@F) > /dev/null || rm -f $${TMPDIR-/tmp}/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://archive.apache.org/dist/commons/lang/binaries/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


%.jar: %.zip
	unzip -oj $< '*/$(@F)' && touch $@

ftp4j-1.7.1.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://downloads.sourceforge.net/project/ftp4j/ftp4j/1.7.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


JARS += slf4j-simple-1.7.5.jar slf4j-api-1.7.5.jar log4j-1.2.17.jar

slf4j-1.7.5.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://www.slf4j.org/dist/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

slf4j-api-1.7.5.jar slf4j-ext-1.7.5-sources.jar slf4j-simple-1.7.5.jar: slf4j-1.7.5.zip
	unzip -oj $< '*/$(@F)' && touch $@

log4j-1.2.17.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://mirror.reverse.net/pub/apache/logging/log4j/1.2.17/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


JARS += jzlib-1.1.2.jar

#dnl com.jcraft.jzlib BSD
jzlib-1.1.2.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ https://github.com/ymnk/jzlib/archive/1.1.2.zip
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

jzlib-1.1.2.jar: jzlib-1.1.2.zip
	test \! -d $$(basename $(@F) .jar) || rm -r $$(basename $(@F) .jar)
	unzip -o $<
	test -d $$(basename $(@F) .jar)-bld || mkdir -p $$(basename $(@F) .jar)-bld
	$(JAVAC) -d $$(basename $(@F) .jar)-bld $$(find $$(basename $(@F) .jar) -name *.java)
	(cd $$(basename $(@F) .jar)-bld && jar cf ../$(@F) com)

JARS += bcprov-jdk15on-151.jar bcpkix-jdk15on-151.jar bcpg-jdk15on-151.jar

#dnl org.bouncycastle BSD-like requires copyright notice
# http://www.bouncycastle.org/download/bcprov-jdk15on-151.jar
# http://polydistortion.net/bc/download/bcprov-jdk15on-151.jar
# http://www.bouncycastle.org/download/bcpkix-jdk15on-151.jar
bcprov-jdk15on-151.jar bcpkix-jdk15on-151.jar bcpg-jdk15on-151.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://www.bouncycastle.org/download/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@



JARS		+= commons-net-ssh.jar

if BUILD_COMMONSNETSSHJAR
#dnl Apache License-2.0
commons-net-ssh.jar: slf4j-api-1.7.5.jar jzlib-1.1.2.jar bcpkix-jdk15on-151.jar bcprov-jdk15on-151.jar bcpg-jdk15on-151.jar
	test -d commons-net-ssh-read-only || svn co http://commons-net-ssh.googlecode.com/svn/ commons-net-ssh-read-only
	test -d $$(basename $(@F) .jar) || mkdir -p $$(basename $(@F) .jar)
	$(JAVAC) -cp slf4j-api-1.7.5.jar:jzlib-1.1.2.jar:bcpkix-jdk15on-151.jar:bcprov-jdk15on-151.jar -d $$(basename $(@F) .jar) $$(find commons-net-ssh-read-only/src/main/java/org -name *.java)
	(cd $$(basename $(@F) .jar) && jar cf ../$(@F) org)
else
commons-net-ssh.jar: @COMMONSNETSSHJAR@
	cp $< $@
endif

BUILT_SOURCES := $(JARS)

check-local: $(JARS)
	for i in $<; do jar tf $$i >/dev/null || exit 1; done

clean-local:
	rm -fr jzlib-1.1.2-bld jzlib-1.1.2


