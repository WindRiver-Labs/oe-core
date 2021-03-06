SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecc0551bf54ad97f6b541720f84d6569"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz \
           file://0001-Include-linux-param.h-for-EXEC_PAGESIZE-definition.patch \
           file://watchdog-init.patch \
           file://watchdog-conf.patch \
           file://wd_keepalive.init \
           file://watchdog-ping.service \
           file://watchdog.service \
"

SRC_URI[md5sum] = "678c32f6f35a0492c9c1b76b4aa88828"
SRC_URI[sha256sum] = "ffdc865137ad5d8e53664bd22bad4de6ca136d1b4636720320cb52af0c18947c"

UPSTREAM_CHECK_URI = "http://sourceforge.net/projects/watchdog/files/watchdog/"
UPSTREAM_CHECK_REGEX = "/watchdog/(?P<pver>(\d+[\.\-_]*)+)/"

inherit autotools
inherit update-rc.d systemd

DEPENDS_append_libc-musl = " libtirpc "
CFLAGS_append_libc-musl = " -I${STAGING_INCDIR}/tirpc "
LDFLAGS_append_libc-musl = " -ltirpc "
EXTRA_OECONF_append_libc-musl = " --disable-nfs "

INITSCRIPT_PACKAGES = "${PN} ${PN}-keepalive"

INITSCRIPT_NAME_${PN} = "watchdog.sh"
INITSCRIPT_PARAMS_${PN} = "start 15 1 2 3 4 5 . stop 85 0 6 ."

INITSCRIPT_NAME_${PN}-keepalive = "wd_keepalive"
INITSCRIPT_PARAMS_${PN}-keepalive = "start 15 1 2 3 4 5 . stop 85 0 6 ."

SYSTEMD_SERVICE_${PN} = "watchdog.service"

PACKAGES =+ "${PN}-keepalive"

FILES_${PN}-keepalive = " \
    ${sysconfdir}/init.d/wd_keepalive \
    ${sbindir}/wd_keepalive \
"

RDEPENDS_${PN} += "${PN}-config ${PN}-keepalive"
RDEPENDS_${PN}-keepalive += "${PN}-config"

RRECOMMENDS_${PN} = "kernel-module-softdog"

do_install_append() {
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/watchdog*.service ${D}${systemd_unitdir}/system

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${sysconfdir}/modules-load.d
		echo "softdog" > ${D}${sysconfdir}/modules-load.d/softdog.conf
	fi
}

FILES_${PN} += "${systemd_unitdir}/system/*"
