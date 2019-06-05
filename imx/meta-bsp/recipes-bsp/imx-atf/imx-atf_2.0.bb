FILESEXTRAPATHS_prepend := "${BSPDIR}/sources/meta-freescale/recipes-bsp/${BPN}/${BPN}:"

require recipes-bsp/imx-atf/imx-atf_1.5.0.bb

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

# Upstream this:
#PV .= "+git${SRCPV}"
# So we don't have to do this:
PV = "2.0+git${SRCPV}"

SRCBRANCH = "imx_2.0.y"
ATF_SRC ?= "git://source.codeaurora.org/external/imx/imx-atf.git;protocol=https"
SRC_URI = "${ATF_SRC};branch=${SRCBRANCH} \
           file://0001-Allow-BUILD_STRING-to-be-set-in-.revision-file.patch \
"
SRCREV = "9951e98e99872c2b78d1668d29c54f99aa64d3ee"

PLATFORM_mx8qxp  = "imx8qx"

BUILD_OPTEE = "${@bb.utils.contains('COMBINED_FEATURES', 'optee', 'true', 'false', d)}"

do_compile_append() {
    if [ "${BUILD_OPTEE}" = "true" ]; then
        oe_runmake clean BUILD_BASE=build-optee
        oe_runmake BUILD_BASE=build-optee SPD=opteed bl31
    fi
}

do_deploy_append () {
    if [ "${BUILD_OPTEE}" = "true" ]; then
        install -m 0644 ${S}/build-optee/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/${BOOT_TOOLS}/bl31-${PLATFORM}.bin-optee
    fi
}
