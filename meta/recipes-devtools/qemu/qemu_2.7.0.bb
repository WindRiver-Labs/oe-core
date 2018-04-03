require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://no-valgrind.patch \
            file://pathlimit.patch \
            file://qemu-2.5.0-cflags.patch \
            file://0001-virtio-zero-vq-inuse-in-virtio_reset.patch \
            file://0002-fix-CVE-2016-7423.patch \
            file://0003-fix-CVE-2016-7908.patch \
            file://0004-fix-CVE-2016-7909.patch \
            file://04b33e21866412689f18b7ad6daf0a54d8f959a7.patch \
            file://0001-pci-assign-sync-MSI-MSI-X-cap-and-table-with-PCIDevi.patch \
            file://qemu-CVE-2016-6836.patch \
            file://qemu-CVE-2016-7155.patch \
            file://qemu-CVE-2016-7156.patch \
            file://qemu-CVE-2016-7157.patch \
            file://qemu-CVE-2016-7170.patch \
            file://qemu-CVE-2016-7421.patch \
            file://qemu-CVE-2016-7422.patch \
            file://qemu-CVE-2016-7466.patch \
            file://qemu-CVE-2016-7994.patch \
            file://qemu-CVE-2016-7995.patch \
            file://qemu-CVE-2016-9101.patch \
            file://qemu-CVE-2016-9102.patch \
            file://qemu-CVE-2016-9103.patch \
            file://qemu-CVE-2016-9104.patch \
            file://qemu-CVE-2016-9105.patch \
            file://qemu-CVE-2016-9106.patch \
            file://qemu-CVE-2016-9907.patch \
            file://qemu-CVE-2016-9908.patch \
            file://qemu-CVE-2016-9912.patch \
            file://qemu-CVE-2016-9911.patch \
            file://qemu-CVE-2016-9921.patch \
            file://qemu-CVE-2016-9776.patch \
            file://qemu-CVE-2016-9845.patch \
            file://qemu-CVE-2016-9846.patch \
            file://qemu-CVE-2016-9913.patch \
            file://qemu-CVE-2016-9914.patch \
            file://qemu-CVE-2016-9915.patch \
            file://qemu-CVE-2016-9916.patch \
            file://qemu-CVE-2016-7907.patch \
            file://qemu-CVE-2016-8577.patch \
            file://qemu-CVE-2016-8578.patch \
            file://qemu-CVE-2016-8667.patch \
            file://qemu-CVE-2016-8668.patch \
            file://qemu-CVE-2016-8669.patch \
            file://qemu-CVE-2016-8576.patch \
            file://qemu-CVE-2016-8909.patch \
            file://qemu-CVE-2016-8910.patch \
            file://qemu-CVE-2016-9381.patch \
            file://0001-i386-Add-support-for-SPEC_CTRL-MSR.patch \
            file://0002-x86-add-AVX512_4VNNIW-and-AVX512_4FMAPS-features.patch \
            file://0003-i386-Add-spec-ctrl-CPUID-bit.patch \
            file://0004-i386-Add-FEAT_8000_0008_EBX-CPUID-feature-word.patch \
            file://0005-i386-Add-new-IBRS-versions-of-Intel-CPU-models.patch \
            file://0006-target-i386-Add-more-Intel-AVX-512-instructions-supp.patch \
            file://0007-i386-Add-EPYC-IBPB-CPU-model.patch \
            file://0008-target-i386-Add-Intel-SHA_NI-instruction-support.patch \
"

SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "08d4d06d1cb598efecd796137f4844ab"
SRC_URI[sha256sum] = "326e739506ba690daf69fc17bd3913a6c313d9928d743bd8eddb82f403f81e53"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
