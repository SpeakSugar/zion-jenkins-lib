package com.speaksugar.test.cmdserver

import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.util.HttpUtil
import org.apache.http.client.utils.URIBuilder
import org.junit.Test

class CmdServerServiceTest {

    @Test
    void cmdsTest() {
        def result = HttpUtil.post("http://10.74.144.161:7777/cmd", [cmd: 'wmic process get processId,commandline | findstr /c:\"cmds\" | findstr /v findstr'])
        println(result)
    }

    @Test
    void cmdsTest2() {
        def result = HttpUtil.post("http://10.32.56.9:7777/cmd", [cmd: 'sudo rm -rf ~/Downloads/rc.pkg'])
        println(result)
    }

    @Test
    void getOs() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        String os = cmdServerService.getOs()
        println("os = ${os}")
    }

    @Test
    void getArch() {
        CmdServerService cmdServerService = new CmdServerService("http://10.74.144.88:7777")
        String arch = cmdServerService.getArch()
        println("arch = ${arch}")
    }

    @Test
    void installRcDT_mac_intel() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.47.184:7777")
        cmdServerService.installRcDT([
                mac_intel_url: "https://electron.fiji.gliprc.com/downloads-all/master/23.3.20/rc/7989-v23.3.20-noupdate-b14c99227/for-downloading/RingCentral-23.3.20-7989-noupdate-mac-x64.pkg"
        ] as RcDTReqDto)
    }

    @Test
    void installRcDT_mac_intel_partner() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.57.132:7777")
        cmdServerService.installRcDT([
                mac_intel_url: "https://electron.fiji.gliprc.com/downloads-all/stage/23.2.20/atos/7301-stage-23-2-20-noupdate-3236d120a/for-downloading/Unify%20Office-23.2.20-7301-noupdate-mac-x64.pkg"
        ] as RcDTReqDto)
    }

    @Test
    void installRcDT_win_intel() {
        CmdServerService cmdServerService = new CmdServerService("http://10.74.144.88:7777")
        cmdServerService.installRcDT([
                win_intel_url: "https://electron.fiji.gliprc.com/downloads-all/master/23.3.22/rc/8052-v23.3.22-noupdate-4c36dad70/for-downloading/RingCentral-23.3.22-8052-noupdate-win.msi"
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
    void killProcess() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.56.196:7777")
        // cmdServerService.killProcess("RingCentral.msi")
        cmdServerService.killProcess("RingCentral")
    }

    @Test
    void uninstallRcDT_win() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.35.220:7777")
        cmdServerService.uninstallRcDT("RingCentral")
    }

    @Test
    void cleanTmpFile() {
        CmdServerService cmdServerService = new CmdServerService("http://10.32.63.97:7777")
        cmdServerService.cleanTmpFile()
    }

    @Test
    void URIBuilder() {
        def builder = new URIBuilder("http://10.32.35.220:7777/os")
        println("uri = ${builder.toString()}")
    }
}
