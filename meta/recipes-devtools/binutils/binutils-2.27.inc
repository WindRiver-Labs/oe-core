LIC_FILES_CHKSUM="\
    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674\
    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6\
    file://gas/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    file://include/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://include/COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://libiberty/COPYING.LIB;md5=a916467b91076e631dd8edb7424769c7\
    file://bfd/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    "

def binutils_branch_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + "_" + pvsplit[1]

BINUPV = "${@binutils_branch_version(d)}"

UPSTREAM_CHECK_GITTAGREGEX = "binutils-(?P<pver>\d+_(\d_?)*)"

SRCREV = "5fe53ce37f9d9dd544ec8cc9e2863b68e1a61f4c"
SRC_URI = "\
     git://sourceware.org/git/binutils-gdb.git;branch=binutils-${BINUPV}-branch;protocol=git \
     file://0002-configure-widen-the-regexp-for-SH-architectures.patch \
     file://0003-Point-scripts-location-to-libdir.patch \
     file://0004-Only-generate-an-RPATH-entry-if-LD_RUN_PATH-is-not-e.patch \
     file://0005-Explicitly-link-with-libm-on-uclibc.patch \
     file://0006-Use-libtool-2.4.patch \
     file://0007-Add-the-armv5e-architecture-to-binutils.patch \
     file://0008-don-t-let-the-distro-compiler-point-to-the-wrong-ins.patch \
     file://0009-warn-for-uses-of-system-directories-when-cross-linki.patch \
     file://0010-Fix-rpath-in-libtool-when-sysroot-is-enabled.patch \
     file://0011-Change-default-emulation-for-mips64-linux.patch \
     file://0012-Add-support-for-Netlogic-XLP.patch \
     file://0013-fix-the-incorrect-assembling-for-ppc-wait-mnemonic.patch \
     file://0014-libtool-remove-rpath.patch \
     file://0015-binutils-mips-gas-pic-relax-linkonce.diff \
     file://0015-Refine-.cfi_sections-check-to-only-consider-compact-.patch \
     file://0016-Fix-seg-fault-in-ARM-linker-when-trying-to-parse-a-b.patch \
     file://0017-Fix-the-generation-of-alignment-frags-in-code-sectio.patch \
     file://0001-ppc-apuinfo-for-spe-parsed-incorrectly.patch \
     file://CVE-2017-6965.patch \
     file://CVE-2017-6966.patch \
     file://CVE-2017-6969.patch \
     file://CVE-2017-6969_2.patch \
     file://CVE-2017-7209.patch \
     file://CVE-2017-7210.patch \
     file://CVE-2017-7223.patch \
     file://CVE-2017-7225.patch \
     file://CVE-2017-7224.patch \
     file://CVE-2017-7226.patch \
     file://CVE-2017-7227.patch \
     file://CVE-2017-7299.patch \
     file://CVE-2017-7299_2.patch \
     file://CVE-2017-7300.patch \
     file://CVE-2017-7301.patch \
     file://CVE-2017-7302.patch \
     file://CVE-2017-7303.patch \
     file://CVE-2017-7614.patch \
     file://CVE-2017-8393.patch \
     file://CVE-2017-8394_1.patch \
     file://CVE-2017-8394_2.patch \
     file://CVE-2017-8395.patch \
     file://CVE-2017-8396_CVE-2017-8397.patch \
     file://CVE-2017-8398.patch \
     file://CVE-2017-8421.patch \
     file://CVE-2017-9038.patch \
     file://CVE-2017-9039.patch \
     file://CVE-2017-9040_CVE-2017-9042.patch \
     file://CVE-2017-9041.patch \
     file://CVE-2017-12451.patch \
     file://CVE-2017-12799.patch \
     file://CVE-2017-12448.patch \
     file://CVE-2017-9954.patch \
     file://CVE-2017-9748.patch \
     file://CVE-2017-9749.patch \
     file://CVE-2017-9742.patch \
     file://CVE-2017-9744.patch \
     file://CVE-2017-9747.patch \
     file://CVE-2017-9746.patch \
     file://CVE-2017-9750.patch \
     file://CVE-2017-9751.patch \
     file://CVE-2017-9756.patch \
     file://CVE-2017-9753-CVE-2017-9754.patch \
     file://CVE-2017-9755.patch \
     file://CVE-2017-9755-2.patch \
     file://CVE-2017-9752.patch \
     file://CVE-2017-9745.patch \
     file://0001-Fix-address-violation-parsing-a-corrupt-Alpha-VMS-bi.patch \
     file://CVE-2017-12449-CVE-2017-12455-CVE-2017-12457-CVE-2017-12458-CVE-2017-12459.patch \
     file://0001-Fix-address-violation-parsing-a-corrupt-IEEE-Alpha-b.patch \
     file://CVE-2017-12450-CVE-2017-12452-CVE-2017-12453-CVE-2017-12454-CVE-2017-12456.patch \
     file://0001-Add-bfd_get_file_size-to-get-archive-element-size.patch \
     file://0001-Change-bfd_get_size-bfd_get_file_size-to-ufile_ptr.patch \
     file://CVE-2017-9955.patch \
     file://CVE-2017-9955-2.patch \
     file://CVE-2017-9955-3.patch \
     file://CVE-2017-9955-4.patch \
     file://CVE-2017-9955-5.patch \
     file://CVE-2017-9955-6.patch \
     file://CVE-2017-9955-7.patch \
     file://CVE-2017-12967.patch \
     file://0001-Fix-seg-fault-reading-a-corrupt-ELF-binary.patch \
     file://CVE-2017-13710.patch \
     file://CVE-2017-14129.patch \
     file://CVE-2017-14130.patch \
     file://CVE-2017-14529.patch \
     file://CVE-2017-14930.patch \
     file://CVE-2017-14932.patch \
     file://CVE-2017-14934.patch \
     file://CVE-2017-14938.patch \
     file://CVE-2017-14128.patch \
     file://CVE-2017-14939.patch \
     file://CVE-2017-14940.patch \
     file://CVE-2017-15020.patch \
     file://CVE-2017-15021.patch \
     file://CVE-2017-15022.patch \
     file://CVE-2017-15024.patch \
     file://CVE-2017-15025.patch \
     file://CVE-2017-15225.patch \
     file://CVE-2017-15938.patch \
     file://CVE-2017-15939.patch \
     file://0001-Improve-objdump-s-behaviour-when-it-encounters-a-cor.patch \
     file://CVE-2017-16831.patch \
     file://0001-Fix-seg-fault-in-objdump-when-run-on-a-fuzzed-PE-bin.patch \
     file://CVE-2017-16832.patch \
     file://CVE-2017-16832-2.patch \
     file://CVE-2017-16826.patch \
     file://CVE-2017-16827.patch \
     file://0001-Fix-address-violation-when-reading-corrupt-DWARF-dat.patch \
     file://0001-PR22239-invalid-memory-read-in-display_debug_frames.patch \
     file://CVE-2017-16828.patch \
     file://CVE-2017-15996.patch \
     file://0001-readelf-Make-version-section-index-sum-unsigned.patch \
     file://0001-Fix-check-for-buffer-overflow-when-processing-versio.patch \
     file://0001-Fix-heap-buffer-address-violation-when-reading-versi.patch \
     file://CVE-2017-14333.patch \
     file://CVE-2017-17124.patch \
     file://CVE-2017-17123.patch \
     file://CVE-2017-17122.patch \
     file://CVE-2017-17080.patch \
     file://0001-Fix-calculation-of-synthetic-symbol-sizes-ppc64.patch \
     file://0001-nm-handling-of-synthetic-symbols.patch \
     file://CVE-2017-17125.patch \
     file://CVE-2017-17125-2.patch \
     file://CVE-2017-17126.patch \
     file://CVE-2017-17121.patch \
     file://CVE-2018-6323.patch \
     file://CVE-2018-6759.patch \
     file://CVE-2018-7208.patch \
     file://0001-PR22230-buffer-overflow-in-display_debug_macro.patch \
     file://0001-PR22232-NULL-pointer-dereference-in-load_specific_de.patch \
     file://CVE-2018-6543.patch \
     file://CVE-2018-7568.patch \
     file://CVE-2018-7569.patch \
     file://0001-PR22829-objcopy-strip-removes-PT_GNU_RELRO-from-lld-.patch \
     file://CVE-2018-7570.patch \
     file://CVE-2018-7642.patch \
     file://CVE-2018-7643.patch \
     file://0001-PR22047-Heap-out-of-bounds-read-in-parse_comp_unit.patch \
     file://CVE-2018-17358.patch \
     file://CVE-2018-17360.patch \
     file://CVE-2018-18309.patch \
     file://CVE-2018-18483.patch \
     file://CVE-2018-18605.patch \
     file://CVE-2018-18606.patch \
     file://CVE-2018-18607.patch \
"
S  = "${WORKDIR}/git"

do_configure_prepend () {
        rm -rf ${S}/gdb ${S}/libdecnumber ${S}/readline ${S}/sim
}
