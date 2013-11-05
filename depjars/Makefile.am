depjardir = @datadir@/@PACKAGE@/jars
dist_depjar_DATA = $(JARS)


EXTRA_DIST = $(nodist_pkgdata_JAVA)
CONFIG_CLEAN_FILES = $(JARS)

AM_JAVACFLAGS= -cp $(top_builddir)/$(subdir)/$(JAVAROOT)

BUILDTMP=$(shell date +%s)

JARS := commons-net-3.1.jar commons-lang3-3.1.jar ftp4j-1.7.1.jar
BUILT_SOURCES := $(JARS)


%.jar: %-bin.zip
	unzip -oj $< '*/$@' && touch $@

commons-net-3.1-bin.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && untip -t $${TMPDIR-/tmp}/$(@F) > /dev/null || rm -f $${TMPDIR-/tmp}/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://archive.apache.org/dist/commons/net/binaries/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

commons-lang3-3.1-bin.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && untip -t $${TMPDIR-/tmp}/$(@F) > /dev/null || rm -f $${TMPDIR-/tmp}/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://archive.apache.org/dist/commons/lang/binaries/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


%.jar: %.zip
	unzip -oj $< '*/$@' && touch $@

ftp4j-1.7.1.zip:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://downloads.sourceforge.net/project/ftp4j/ftp4j/1.7.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


check-local: $(JARS)
	for i in $<; do jar tf $$i >/dev/null || exit 1; done

