SUMMARY = "Library for obtaining the call-chain of a program"
DESCRIPTION = "a portable and efficient C programming interface (API) to determine the call-chain of a program"
HOMEPAGE = "http://www.nongnu.org/libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d80c8ed4062b8339b715f90fa68cc9f"
DEPENDS += "libatomic-ops"
DEPENDS:append:libc-musl = " libucontext"

SRC_URI = "http://download.savannah.nongnu.org/releases/libunwind/libunwind-${PV}.tar.gz \
           file://0003-x86-Stub-out-x86_local_resume.patch \
           file://0004-Fix-build-on-mips-musl.patch \
           file://0005-ppc32-Consider-ucontext-mismatches-between-glibc-and.patch \
           file://0006-Fix-for-X32.patch \
           file://0001-src-Gtrace-remove-unguarded-print-calls.patch \
           "
SRC_URI:append:libc-musl = " file://musl-header-conflict.patch"

SRC_URI[sha256sum] = "4a6aec666991fb45d0889c44aede8ad6eb108071c3554fcdff671f9c94794976"

inherit autotools multilib_header

COMPATIBLE_HOST:riscv32 = "null"

PACKAGECONFIG ??= ""
PACKAGECONFIG[lzma] = "--enable-minidebuginfo,--disable-minidebuginfo,xz"
PACKAGECONFIG[latexdocs] = "--enable-documentation, --disable-documentation, latex2man-native"

EXTRA_OECONF:arm = "--enable-debug-frame"
EXTRA_OECONF:armeb = "--enable-debug-frame"
EXTRA_OECONF:aarch64 = "--enable-debug-frame"

EXTRA_OECONF:append:libc-musl = " --disable-documentation --disable-tests --enable-static"

# http://errors.yoctoproject.org/Errors/Details/20487/
ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"

LDFLAGS += "-Wl,-z,relro,-z,now ${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', ' -fuse-ld=bfd ', '', d)}"

SECURITY_LDFLAGS:append:libc-musl = " -lssp_nonshared"
CACHED_CONFIGUREVARS:append:libc-musl = " LDFLAGS='${LDFLAGS} -lucontext'"

do_install:append () {
	oe_multilib_header libunwind.h
}

BBCLASSEXTEND = "native"
