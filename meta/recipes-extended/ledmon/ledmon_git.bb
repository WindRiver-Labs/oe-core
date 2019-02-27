SUMMARY = "Intel(R) Enclosure LED Utilities"

DESCRIPTION = "The utilities are designed primarily to be used on storage servers \
 utilizing MD devices (aka Linux Software RAID) for RAID arrays.\
"
HOMEPAGE = "https://github.com/intel/ledmon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
"

DEPENDS = " udev sg3-utils"

PARALLEL_MAKE = ""

SRC_URI = "git://github.com/intel/ledmon;branch=master"
SRCREV = "8a5d9526e5666e75625427b85a2586436651e89c"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

S = "${WORKDIR}/git"
EXTRA_OEMAKE = "CC='${CC}' LDFLAGS='${LDFLAGS}'"

do_compile() {
        oe_runmake SYSROOT="${STAGING_DIR_TARGET}"
}

do_install() {
	oe_runmake install DESTDIR=${D}
	cd ${S}/systemd
	oe_runmake install DESTDIR=${D}
}
