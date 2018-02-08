FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

PACKAGECONFIG_append = " networkd resolved "

SRC_URI += "file://wired.network \
            file://wireless.network \
"

do_install_append() {
	install -d ${D}${sysconfdir}/systemd/network/
	install -m 0644 ${WORKDIR}/*.network ${D}${sysconfdir}/systemd/network/
}

FILES_${PN} += "{sysconfdir}/systemd/network/*"
