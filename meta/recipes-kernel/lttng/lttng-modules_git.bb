require lttng-modules_2.12.6.bb

LIC_FILES_CHKSUM = "file://LICENSE;md5=0464cff101a009c403cd2ed65d01d4c4"
DEFAULT_PREFERENCE = "-1"
SRC_URI = "git://git.lttng.org/lttng-modules;branch=master \
           file://2.12-Makefile-Do-not-fail-if-CONFIG_TRACEPOINTS-is-not-en.patch \
           file://2.12-BUILD_RUNTIME_BUG_ON-vs-gcc7.patch \
           file://0001-wip-fix-block-add-a-disk_uevent-helper-v5.12.patch \
           file://0001-fix-mm-tracing-kfree-event-name-mismatching-with-pro.patch \
           "
SRCREV = "484ec2179e14ae9272a7ad3d1820c837136fd144"
PV = "2.12.5+git${SRCPV}"
S = "${WORKDIR}/git"
