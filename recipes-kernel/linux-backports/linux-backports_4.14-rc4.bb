# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)


DESCRIPTION = "Linux Backports"
HOMEPAGE = "https://backports.wiki.kernel.org"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
SRC_URI = "http://www.kernel.org/pub/linux/kernel/projects/backports/stable/v4.14-rc4/backports-4.14-rc4-1.tar.bz2 \
          "
S = "${WORKDIR}/backports-4.14-rc4-1"
inherit backports_module
EXTRA_OEMAKE = "KLIB_BUILD=${STAGING_KERNEL_DIR} KLIB=${D} DESTDIR=${D}"

do_configure_prepend() {
	CC=${BUILD_CC} oe_runmake -C kconf conf
}
do_configure_append() {
	oe_runmake
	sed -i "s#@./scripts/update-initramfs## " Makefile
	sed -i "s#@./scripts/update-initramfs $(KLIB)## " Makefile.real
	sed -i "s#@./scripts/check_depmod.sh## " Makefile.real
	sed -i "s#@/sbin/depmod -a## " Makefile.real
}
FILES_${PN} += "${nonarch_base_libdir}/udev \
                ${sysconfdir}/udev \
               "
DEPENDS += "virtual/kernel"
inherit module-base kernel-backports_module-split
addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[deptask] = "do_populate_sysroot"
backports_module_do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   AR="${KERNEL_AR}" \
		   ${MAKE_TARGETS}
}
backports_module_do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
	           KERNEL_SRC=${STAGING_KERNEL_DIR} \
	           CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
	           modules_install
}
EXPORT_FUNCTIONS do_compile do_install
PACKAGES += "kernel-backports-modules"
DESCRIPTION_kernel-backports-modules = "Backports Kernel modules meta package"
FILES_kernel-backports-modules = ""
ALLOW_EMPTY_kernel-backports-modules = "1"


