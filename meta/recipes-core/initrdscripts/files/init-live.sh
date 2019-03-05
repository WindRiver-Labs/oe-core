#!/bin/sh

PATH=/sbin:/bin:/usr/sbin:/usr/bin

ROOT_MOUNT="/rootfs"
ROOT_IMAGE="rootfs.img"
MOUNT="/bin/mount"
UMOUNT="/bin/umount"
ISOLINUX=""

ROOT_DISK=""

# Copied from initramfs-framework. The core of this script probably should be
# turned into initramfs-framework modules to reduce duplication.
udev_daemon() {
	OPTIONS="/sbin/udev/udevd /sbin/udevd /lib/udev/udevd /lib/systemd/systemd-udevd"

	for o in $OPTIONS; do
		if [ -x "$o" ]; then
			echo $o
			return 0
		fi
	done

	return 1
}

_UDEV_DAEMON=`udev_daemon`

early_setup() {
    mkdir -p /proc
    mkdir -p /sys
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    mount -t devtmpfs none /dev

    # support modular kernel
    modprobe isofs 2> /dev/null

    mkdir -p /run
    mkdir -p /var/run

    $_UDEV_DAEMON --daemon
    udevadm trigger --action=add
}

# Active LV manually to make it can be mounted automatically.
lvm_setup() {
    # wait 1 second to ensure /dev/md* available
    sleep 1
    lvm pvscan --cache --activate ay
    udevadm trigger --action=add

}

read_args() {
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                if [ ! "${optarg#UUID}" = "${optarg}" ]; then
                    ROOT_DEVICE=$(blkid -U ${optarg:5})
		 else
                    ROOT_DEVICE=$optarg
                fi ;;
            rootimage=*)
                ROOT_IMAGE=$optarg ;;
            rootfstype=*)
                modprobe $optarg 2> /dev/null ;;
            LABEL=*)
                label=$optarg ;;
            video=*)
                video_mode=$arg ;;
            vga=*)
                vga_mode=$arg ;;
            console=*)
                if [ -z "${console_params}" ]; then
                    console_params=$arg
                else
                    console_params="$console_params $arg"
                fi ;;
            debugshell*)
                if [ -z "$optarg" ]; then
                        shelltimeout=30
                else
                        shelltimeout=$optarg
                fi 
        esac
    done
}

boot_live_root() {
    # Watches the udev event queue, and exits if all current events are handled
    udevadm settle --timeout=3 --quiet
    # Kills the current udev running processes, which survived after
    # device node creation events were handled, to avoid unexpected behavior
    killall -9 "${_UDEV_DAEMON##*/}" 2>/dev/null

    # Allow for identification of the real root even after boot
    mkdir -p  ${ROOT_MOUNT}/media/realroot
    mount -n --move "/run/media/${ROOT_DISK}" ${ROOT_MOUNT}/media/realroot

    # Move the mount points of some filesystems over to
    # the corresponding directories under the real root filesystem.
    for dir in `awk '/\/dev.* \/run\/media/{print $2}' /proc/mounts`; do
        mkdir -p  ${ROOT_MOUNT}/media/${dir##*/}
        mount -n --move $dir ${ROOT_MOUNT}/media/${dir##*/}
    done
    mount -n --move /proc ${ROOT_MOUNT}/proc
    mount -n --move /sys ${ROOT_MOUNT}/sys
    mount -n --move /dev ${ROOT_MOUNT}/dev

    cd $ROOT_MOUNT

    # busybox switch_root supports -c option
    exec switch_root -c /dev/console $ROOT_MOUNT /sbin/init $CMDLINE ||
        fatal "Couldn't switch_root, dropping to shell"
}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}

boot_disk_root() {
    mkdir $ROOT_MOUNT
    if [ "$ROOT_DEVICE" == "/dev/ram0" ]; then
        mount -n --move /run/media/$ROOTFS_DEVICE $ROOT_MOUNT
    else
        umount $ROOT_DEVICE 2>/dev/null
        mount  $ROOT_DEVICE $ROOT_MOUNT
    fi

    if touch $ROOT_MOUNT/bin 2>/dev/null; then
	# The root image is read-write, directly boot it up.
	boot_live_root
    fi

}

found_disk_rootfs() {
    # if rootfs is specified like root=/dev/mapper/wrl-root
    # we use the specified rootfs, if not we search all mounted disk
    if [ "$ROOT_DEVICE" == "/dev/ram0" ]; then
       for j in `ls /run/media 2>/dev/null`; do
	  if [ -d /run/media/$j/bin ]; then
             ROOTFS_DEVICE=$j
             found="disk"
             break
          fi
       done
    else
       found="disk"
    fi

    for k in `ls /run/media 2>/dev/null`; do
	  if [ -f /run/media/$k/initrd ]; then
             ROOT_DISK="$k"
             break
          fi
    done
}

early_setup

[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

if ! lvscan |grep -i -w "inactive" &>/dev/null;then
    lvm_setup
fi

read_args

echo "Waiting for removable media..."
C=0
while true
do
  for i in `ls /run/media 2>/dev/null`; do
      if [ -f /run/media/$i/$ROOT_IMAGE ] ; then
		found="img"
		ROOT_DISK="$i"
		break
      elif [ -f /run/media/$i/isolinux/$ROOT_IMAGE ]; then
		found="img"
		ISOLINUX="isolinux"
		ROOT_DISK="$i"
		break	
      fi
  done

  if [ "$found" = "img" ]; then
      break;
  else
      found_disk_rootfs
      if [ "$found" = "disk" ]; then
          break;
      fi
  fi
  # don't wait for more than $shelltimeout seconds, if it's set
  if [ -n "$shelltimeout" ]; then
      echo -n " " $(( $shelltimeout - $C ))
      if [ $C -ge $shelltimeout ]; then
           echo "..."
	   echo "Mounted filesystems"
           mount | grep media
           echo "Available block devices"
           cat /proc/partitions
           fatal "Cannot find $ROOT_IMAGE file in /run/media/* , dropping to a shell "
      fi
      C=$(( C + 1 ))
  fi
  sleep 1
done

# Try to mount the root image read-write and then boot it up.
# This function distinguishes between a read-only image and a read-write image.
# In the former case (typically an iso), it tries to make a union mount if possible.
# In the latter case, the root image could be mounted and then directly booted up.
mount_and_boot() {
    mkdir $ROOT_MOUNT
    mknod /dev/loop0 b 7 0 2>/dev/null

    if ! mount -o rw,loop,noatime,nodiratime /run/media/$ROOT_DISK/$ISOLINUX/$ROOT_IMAGE $ROOT_MOUNT ; then
	fatal "Could not mount rootfs image"
    fi

    if touch $ROOT_MOUNT/bin 2>/dev/null; then
	# The root image is read-write, directly boot it up.
	boot_live_root
    fi

    # determine which unification filesystem to use
    union_fs_type=""
    if grep -q -w "overlay" /proc/filesystems; then
	union_fs_type="overlay"
    elif grep -q -w "aufs" /proc/filesystems; then
	union_fs_type="aufs"
    else
	union_fs_type=""
    fi

    # make a union mount if possible
    case $union_fs_type in
	"overlay")
	    mkdir -p /rootfs.ro /rootfs.rw
	    if ! mount -n --move $ROOT_MOUNT /rootfs.ro; then
		rm -rf /rootfs.ro /rootfs.rw
		fatal "Could not move rootfs mount point"
	    else
		mount -t tmpfs -o rw,noatime,mode=755 tmpfs /rootfs.rw
		mkdir -p /rootfs.rw/upperdir /rootfs.rw/work
		mount -t overlay overlay -o "lowerdir=/rootfs.ro,upperdir=/rootfs.rw/upperdir,workdir=/rootfs.rw/work" $ROOT_MOUNT
		mkdir -p $ROOT_MOUNT/rootfs.ro $ROOT_MOUNT/rootfs.rw
		mount --move /rootfs.ro $ROOT_MOUNT/rootfs.ro
		mount --move /rootfs.rw $ROOT_MOUNT/rootfs.rw
	    fi
	    ;;
	"aufs")
	    mkdir -p /rootfs.ro /rootfs.rw
	    if ! mount -n --move $ROOT_MOUNT /rootfs.ro; then
		rm -rf /rootfs.ro /rootfs.rw
		fatal "Could not move rootfs mount point"
	    else
		mount -t tmpfs -o rw,noatime,mode=755 tmpfs /rootfs.rw
		mount -t aufs -o "dirs=/rootfs.rw=rw:/rootfs.ro=ro" aufs $ROOT_MOUNT
		mkdir -p $ROOT_MOUNT/rootfs.ro $ROOT_MOUNT/rootfs.rw
		mount --move /rootfs.ro $ROOT_MOUNT/rootfs.ro
		mount --move /rootfs.rw $ROOT_MOUNT/rootfs.rw
	    fi
	    ;;
	"")
	    mount -t tmpfs -o rw,noatime,mode=755 tmpfs $ROOT_MOUNT/media
	    ;;
    esac

    # boot the image
    boot_live_root
}

if [ "$label" != "boot" -a -f $label.sh ] ; then
	if [ -f /run/media/$i/$ISOLINUX/$ROOT_IMAGE ] ; then
	    ./$label.sh $i/$ISOLINUX $ROOT_IMAGE $video_mode $vga_mode $console_params
	else
	    fatal "Could not find $label script"
	fi

	# If we're getting here, we failed...
	fatal "Target $label failed"
fi

if [ "$found" = "img" ]; then
    mount_and_boot
elif [ "$found" = "disk" ]; then
    boot_disk_root
fi
