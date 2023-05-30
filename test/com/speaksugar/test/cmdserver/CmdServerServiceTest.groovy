package com.speaksugar.test.cmdserver

import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import org.junit.Test

class CmdServerServiceTest {

    @Test
    void getOs() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        String os = cmdServerService.getOs()
        println("os = ${os}")
    }

    @Test
    void getArch() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        String arch = cmdServerService.getArch()
        println("arch = ${arch}")
    }

    @Test
    void installRcDT_mac_intel() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        cmdServerService.installRcDT([
                mac_intel_url: "https://electron.fiji.gliprc.com/downloads-all/stage/23.2.20/rc/7237-stage-23-2-20-noupdate-52d7d2d6a/for-downloading/RingCentral-23.2.20-7237-noupdate-mac-x64.pkg"
        ] as RcDTReqDto)
    }

    @Test
    void installRcDT_mac_intel_partner() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        cmdServerService.installRcDT([
                mac_intel_url: "https://electron.fiji.gliprc.com/downloads-all/stage/23.2.20/atos/7301-stage-23-2-20-noupdate-3236d120a/for-downloading/Unify%20Office-23.2.20-7301-noupdate-mac-x64.pkg"
        ] as RcDTReqDto)
    }

    @Test
    void installRcDT_win_arm() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.62.94:7777")
        cmdServerService.installRcDT([
                win_arm_url: "https://electron.fiji.gliprc.com/downloads-all/develop-branches/23.2.30/rc/7446-develop-noupdate-f22efe17a/for-downloading/RingCentral-23.2.30-7446-noupdate-win-arm64-arm64.msi"
        ] as RcDTReqDto)
    }

    @Test
    void uninstallRcDT_win() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.62.94:7777")
        cmdServerService.uninstallRcDT("RingCentral")
    }
}
