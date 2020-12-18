#
# Copyright (C) 2019 Konsulko Group
#

SUMMARY = "Full-featured set of base utils"
DESCRIPTION = "Package group bringing in packages needed to provide much of the base utils type functionality found in busybox"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

VIRTUAL-RUNTIME_vim ?= "vim-tiny"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = "\
    base-passwd \
    bash \
    bind-utils \
    bzip2 \
    coreutils \
    cpio \
    ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "", "debianutils-run-parts", d)} \
    dhcpcd \
    ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "", "kea", d)} \
    diffutils \
    ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "", "dpkg-start-stop", d)} \
    e2fsprogs \
    ed \
    file \
    findutils \
    gawk \
    grep \
    gzip \
    ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "", "ifupdown", d)} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'inetutils', d)} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'inetutils-ping', d)} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'inetutils-telnet', d)} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'inetutils-tftp', d)} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'inetutils-traceroute', d)} \
    iproute2 \
    ${@bb.utils.contains("MACHINE_FEATURES", "keyboard", "kbd", "", d)} \
    kmod \
    less \
    ncurses-tools \
    net-tools \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'parted', d)} \
    patch \
    procps \
    psmisc \
    sed \
    shadow-base \
    tar \
    time \
    unzip \
    util-linux \
    ${VIRTUAL-RUNTIME_vim} \
    ${@bb.utils.contains('INCOMPATIBLE_LICENSE', 'GPLv3', '', 'wget', d)} \
    which \
    xz \
    "
