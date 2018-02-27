# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Linux Backports"
HOMEPAGE = "https://backports.wiki.kernel.org"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/projects/backports/stable/v4.14-rc2/backports-4.14-rc2-1.tar.gz \
	   file://backports_config \
"
SRC_URI[md5sum] = "4f00003de9a55b7277e096dafe9a6c20"
SRC_URI[sha256sum] = "7e3afa1f9f77e10af9c0f21da9c14e2bf2b0034f111b7a8b38d7be9ea6751c01"

S = "${WORKDIR}/backports-4.14-rc2-1"

EXTRA_OEMAKE = "KLIB_BUILD=${STAGING_KERNEL_DIR} KLIB=${D} DESTDIR=${D}"

DEPENDS += "virtual/kernel"

inherit module-base

do_configure[depends] += "virtual/kernel:do_deploy"

do_configure_prepend() {
	cp ${STAGING_KERNEL_BUILDDIR}/.config ${STAGING_KERNEL_DIR}/.config
	cp ${WORKDIR}/backports_config ${S}/.config
	CC=${BUILD_CC} oe_runmake -C kconf conf
}

do_configure_append() {
	oe_runmake
	sed -i "s#@./scripts/update-initramfs## " Makefile
	sed -i "s#@./scripts/update-initramfs $(KLIB)## " Makefile.real
	sed -i "s#@./scripts/check_depmod.sh## " Makefile.real
	sed -i "s#@/sbin/depmod -a## " Makefile.real
}

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   AR="${KERNEL_AR}" \
		   ${MAKE_TARGETS}
}

do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
	           KERNEL_SRC=${STAGING_KERNEL_DIR} \
	           CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
	           modules_install
}

FILES_${PN} += "${nonarch_base_libdir}/udev \
                ${sysconfdir}/udev \
		${nonarch_base_libdir} \
               "
