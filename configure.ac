dnl ***********************************************************************
dnl Process this file with autoconf to produce a configure script.
dnl ***********************************************************************
#AC_INIT(install-sh)
AC_INIT(fctransfer, 0.3)
AC_CONFIG_MACRO_DIR([m4])

AC_CANONICAL_HOST

AM_INIT_AUTOMAKE([foreign dist-bzip2])
AC_CONFIG_TESTDIR([tests-autotest])

dnl ***********************************************************************
dnl Checks for programs used during the build
dnl ***********************************************************************
dnl AC_PROG_CC
dnl AC_PROG_CXX
AC_PROG_INSTALL
AC_PROG_MAKE_SET

AC_SUBST(AUTOM4TE, autom4te)

SVN_CHECK_JDK([1.6],[1.6])
AM_CONDITIONAL(DO_JAVA, test "$JAVAC" != none)

dnl ***********************************************************************
dnl Checks for programs used during test, markup, diagrams. etc.  I'm a huge fan
dnl of automation, and if I can type a single command to make, check, package,
dnl and push a new release, HECK YEAH!
dnl 
dnl This was based on my work for the fctools project (nee vitools) so some may
dnl prune out in the near future
dnl ***********************************************************************
AC_PATH_PROGS(PATHAWK, [awk gawk nawk], [/bin/awk])
AC_PATH_PROGS(XMLLINT, [xmllint], [missing xmllint])
AC_ARG_WITH(xmllintcommentindent,
[AS_HELP_STRING([--with-xmllintcommentindent],[Set XML-comment indentation fudge such as xmllint-2.6.x/2.7.x differences; default=detect])],
[
    case "$withval" in
	"yes") WITH_XMLLINTINDENT="  " ;;
	"no")  WITH_XMLLINTINDENT=""  ;;
	*) case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	    206*) WITH_XMLLINTINDENT="" ;;
	    *) WITH_XMLLINTINDENT="  "  ;;
	    esac ;;
    esac
],
[
    case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	206*) WITH_XMLLINTINDENT="" ;;
	*) WITH_XMLLINTINDENT="  "  ;;
    esac
])
AC_SUBST([XMLLINTINDENT], ${WITH_XMLLINTINDENT})

AC_PATH_PROGS(XSLTPROC, [xsltproc], [missing xsltproc])
AC_PATH_PROGS(TIDY, [tidy], [missing tidy])
AC_PATH_PROGS(BZCAT, [bzcat], [missing bzcat])
AC_PATH_PROGS(SEVENZIP, [7za 7z keka7z], [missing 7za], $PATH:/Applications/Keka.app/Contents/Resources)
AC_PATH_PROGS(FILE, [file], [missing file])

CHECK_DOXYGEN(pkg/Doxyfile.in)

AC_PATH_PROGS(ASTYLE, [astyle], missing, $PATH:$HOME/bin)
AM_CONDITIONAL(DO_ASTYLE, test "x$ASTYLE" != "xmissing")
AC_PATH_PROGS(RPMBUILD, [rpmbuild], [missing rpmbuild])
AM_CONDITIONAL(HAVE_RPMBUILD, [test "x$RPMBUILD" != "xmissing rpmbuild"])


AC_PATH_PROGS(GIT, [git], [missing])
AC_MSG_CHECKING([version agreement with git tag])
BUILDID=$( (git describe; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 2)
CHKVER=$( (git describe; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 1)
if test "${CHKVER}" = "${VERSION}"
then 
  AC_MSG_RESULT([ok])
else
  AC_MSG_WARN([version mismatch])
fi
AC_SUBST(BUILDID)


AC_CHECK_PROG(GIT, [git], [git],[missing git])
AM_CONDITIONAL(DO_GITFORMATTING, test "$GIT" != "missing git" -a -f .git/config)
AC_PATH_PROGS(ECHO, [echo], [missing])
AC_PATH_PROGS(NC, [nc], [missing])
AC_PATH_PROGS(SED, [sed], [missing])
AC_PATH_PROGS(BASE64, [base64], [missing])
AM_CONDITIONAL(HAVE_BASE64, [test "x$BASE64" != "xmissing"])
AC_PATH_PROGS(N2A, [native2ascii])

AC_SUBST(yumrepos, [$sysconfdir/yum.repos.d])
AC_SUBST(faebase, [/home/services])
AC_SUBST(emaildest, [ftpupdates])
AC_SUBST(httpconfd, [/etc/httpd/conf.d])
AC_SUBST(localreposroot, [/var/ftp])
AC_SUBST(localreposversion, [CentOS5.6])

AC_ARG_ENABLE(autotest,
[AS_HELP_STRING([--enable-autotest],[Enable autotest testcases; breaks: make distcheck; default=disabled])],
[
    case "$enableval" in
	"yes") ENAB=yes ;;
	"no")  ENAB=no  ;;
	*) ENAB=no ;;
    esac
],
[
    ENAB=no
])
AM_CONDITIONAL(DO_AUTOTEST, test "$ENAB" = yes)


AC_ARG_WITH(ftptesturl,
[AS_HELP_STRING([--with-ftptesturl],[Enable testing of FTP by giving a test FTP URL; default=disable])],
[
    case "$withval" in
        "yes") WITH_FTPTESTURL="ftp://anonymous@ftp.smallfoot.org/incoming/" ;;
        "no")  WITH_FTPTESTURL=no  ;;
        *) WITH_FTPTESTURL="$withval" ;;
    esac
],
[
	WITH_FTPTESTURL=no ;
])
AC_SUBST([FTPTESTURL], ${WITH_FTPTESTURL})



AC_ARG_WITH(getoptplus,
[AS_HELP_STRING([--with-getoptplus],[Enable inclusion of an improved getopt .jar; default=search])],
[
    case "$withval" in
        "yes") WITH_GETOPTJAR=$((locate getopt.jar) | grep -v .workdir | head -1) ;;
        "no")  WITH_GETOPTJAR=no  ;;
        *) WITH_GETOPTJAR="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for getopt.jar])
    WITH_GETOPTJAR=$((locate getopt.jar) | grep -v .workdir | head -1) ;
AC_MSG_RESULT([${WITH_GETOPTJAR}])
])
AM_CONDITIONAL(DO_GETOPTJAR, test "$WITH_GETOPTJAR" != no)
AC_SUBST([GETOPTJAR], ${WITH_GETOPTJAR})



AC_CHECK_PROG(SVN, [svn], [svn],[missing svn])
AC_ARG_WITH(commons-net-ssh,
[AS_HELP_STRING([--with-commons-net-ssh],[Enable inclusion of a commons-net-ssh .jar; default=svn co and build])],
[
AC_MSG_CHECKING([for commons-net-ssh.jar])
    case "$withval" in
        "yes") WITH_COMMONSNETSSHJAR=$((locate commons-net-ssh.jar) | grep -v .workdir | head -1) ;;
        "no")  WITH_COMMONSNETSSHJAR=build  ;;
        *) WITH_COMMONSNETSSHJAR="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for commons-net-ssh.jar])
    WITH_COMMONSNETSSHJAR=$((locate commons-net-ssh.jar) | grep -v .workdir | head -1) ;
    if test -z "${WITH_COMMONSNETSSHJAR}" -o \! -f "${WITH_COMMONSNETSSHJAR}"; then WITH_COMMONSNETSSHJAR=build; fi
AC_MSG_RESULT([${WITH_COMMONSNETSSHJAR}])
])
AM_CONDITIONAL(BUILD_COMMONSNETSSHJAR, test "$WITH_COMMONSNETSSHJAR" = build)
AC_SUBST([COMMONSNETSSHJAR], ${WITH_COMMONSNETSSHJAR})



AC_ARG_WITH(sudopostmove,
[AS_HELP_STRING([--with-sudopostmove],[Enable sudopostmove of a built RPM; default=disabled])],
[
    case "$withval" in
        "no")  WITH_SUDOPOSTMOVE=no  ;;
        *) WITH_SUDOPOSTMOVE="$withval" ;;
    esac
],
[
    WITH_SUDOPOSTMOVE=no
])
AM_CONDITIONAL(DO_SUDOPOSTMOVE, test "$WITH_SUDOPOSTMOVE" != no)
AC_SUBST([SUDOPOSTMOVE], ${WITH_SUDOPOSTMOVE})



AC_CONFIG_FILES(Makefile
  convjars/Makefile 
  depjars/Makefile 
  java/Makefile java/WinchFactory.java
  java/version.java
  tests-autotest/Makefile tests-autotest/testsuite.at
)

if test "x$RPMBUILD" != "xmissing rpmbuild"
then
  AC_CONFIG_FILES(fctransfer.spec:pkg/rpm/fctransfer.spec.in)
fi

AC_OUTPUT
