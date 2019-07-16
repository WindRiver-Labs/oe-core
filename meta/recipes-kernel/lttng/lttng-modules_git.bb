require lttng-modules_2.10.11.bb

LIC_FILES_CHKSUM = "file://LICENSE;md5=c4613d1f8a9587bd7b366191830364b3"
DEFAULT_PREFERENCE = "-1"
SRC_URI = "git://git.lttng.org/lttng-modules;branch=stable-2.10 \
           file://Makefile-Do-not-fail-if-CONFIG_TRACEPOINTS-is-not-en.patch \
           file://BUILD_RUNTIME_BUG_ON-vs-gcc7.patch \
           "
SRCREV = "624aca5d7507fbd11ea4a1a474c3aa1031bd9a31"
PV = "2.10.10+git${SRCPV}"
S = "${WORKDIR}/git"
