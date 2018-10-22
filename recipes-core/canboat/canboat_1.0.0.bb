SUMMARY = "CANBOAT- A small but effective set of command-line utilities to work with CAN networks on BOATs."
SECTION = "base"
LICENSE = "GPLv3"

DEPENDS += "libxslt-native canboat-native"

LIC_FILES_CHKSUM = "file://GPL;md5=05507c6da2404b0e88fe5a152fd12540"

SRC_URI_append = " \
           file://0001-Non-root-user-in-Makefile.patch \
           file://0001-Define-ANALYZEREXEC.patch \
           file://0001-use-php-instead-of-php5.patch \
          "

SRC_URI = "https://github.com/canboat/canboat/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "6ee6162d30faa3b3f1ff068cc7a70a60"
SRC_URI[sha256sum] = "6bf1050a83a5d7eb8351547c10e7e2ae2e1811250d50a63880074f0c07ec672e"

PREFIX ?= "${root_prefix}"
#PREFIX_class-native = "${prefix}"

EXTRA_OEMAKE_append_class-target = " ANALYZEREXEC=analyzer "

do_install() {
   oe_runmake DESTDIR=${D} PREFIX=${root_prefix} EXEC_PREFIX=${exec_prefix} install

}

RDEPENDS_${PN}_append_class-target = " php-cli perl"

BBCLASSEXTEND = "native nativesdk"
