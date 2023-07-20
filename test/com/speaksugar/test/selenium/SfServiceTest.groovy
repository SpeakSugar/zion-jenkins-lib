package com.speaksugar.test.selenium

import com.speaksugar.jenkins.selenium.SfService
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import org.junit.Test

class SfServiceTest {

    @Test
    void createNodeLock() {
        SfService sfService = new SfService("http://10.32.57.28:4444")
        NodeLockReqDto nodeLockReqDto = [
                name: "Jasmine-webinar-test",
                list: [
                        [
                                "ip": "10.32.57.28",
                                "count": 1
                        ]
                ]
        ]
        NodeLockResDto nodeLockResDto = sfService.createNodeLock(nodeLockReqDto)
        println("nodeLockUuid: " + nodeLockResDto.uuid)
    }

    @Test
    void deleteNodeLock() {
        String uuid = "Jasmine-webinar-test-xxx"
        SfService sfService = new SfService("http://10.32.57.28:4444")
        sfService.deleteNodeLock(uuid)
    }
}
