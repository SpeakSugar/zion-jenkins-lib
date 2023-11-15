package com.speaksugar.test.cmdserver.model

import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import org.junit.Test

class RcDTReqDtoTest {

    @Test
    void rcDTReqDtoTest() {
        RcDTReqDto rcDTReqDto = [
                mac_arm_url  : "",
                mac_intel_url: "",
                win_arm_url  : "",
                win_intel_url: ""

        ]
        println("rcDTReqDto.need_download = ${rcDTReqDto.need_download}")
    }
}
