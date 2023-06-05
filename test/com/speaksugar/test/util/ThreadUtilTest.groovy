package com.speaksugar.test.util

import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.util.ThreadUtil
import org.junit.Test

class ThreadUtilTest {

    @Test
    void parallelInstallDT() {
        List<Closure> closures = []
        CmdServerService cmdServerService = new CmdServerService("http://10.32.59.112:7777")
        Closure closure = {
            cmdServerService.installRcDT([
                    mac_intel_url: "https://electron.fiji.gliprc.com/downloads-all/stage/23.2.20/rc/7237-stage-23-2-20-noupdate-52d7d2d6a/for-downloading/RingCentral-23.2.20-7237-noupdate-mac-x64.pkg"
            ] as RcDTReqDto)
        }
        closures.add(closure)
        ThreadUtil.execute(closures)

    }

}
