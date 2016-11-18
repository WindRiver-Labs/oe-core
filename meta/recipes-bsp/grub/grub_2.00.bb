require grub2.inc

RDEPENDS_${PN} = "diffutils freetype ${PN}-common"
RDEPENDS_${PN}-common = "${PN}-editenv"

PR = "r1"

EXTRA_OECONF = "--with-platform=pc --disable-grub-mkfont --program-prefix="" \
               --enable-liblzma=no --enable-device-mapper=no --enable-libzfs=no"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'largefile', '--enable-largefile', '--disable-largefile', d)}"

PACKAGES =+ "${PN}-editenv ${PN}-common"

FILES_${PN}-editenv = "${bindir}/grub-editenv"
FILES_${PN}-common = " \
    ${bindir} \
    ${sysconfdir} \
    ${sbindir} \
    ${datadir}/grub \
"

INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"
