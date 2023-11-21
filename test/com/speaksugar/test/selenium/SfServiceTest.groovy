package com.speaksugar.test.selenium

import com.speaksugar.jenkins.selenium.SfService
import com.speaksugar.jenkins.selenium.model.NodeDto
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import groovy.json.JsonOutput
import org.junit.Test

class SfServiceTest {

    @Test
    void createNodeLock() {
        SfService sfService = new SfService("http://10.32.57.28:4444")
        NodeLockReqDto nodeLockReqDto = [
                name: "Jasmine-webinar-test",
                list: [
                        [
                                "ip"   : "10.32.57.28",
                                "count": 1
                        ]
                ]
        ]
        NodeLockResDto nodeLockResDto = sfService.createNodeLock(nodeLockReqDto)
        println("nodeLockUuid: " + nodeLockResDto.uuid)
    }

    @Test
    void getNodeLock() {
        SfService sfService = new SfService("http://10.32.35.186:5555")
        List<NodeLockResDto> nodeLockResDtos = sfService.getNodeLock('Jasmine-webinar-test-1690245817665')
        println(JsonOutput.toJson(nodeLockResDtos))
    }

    @Test
    void deleteNodeLock() {
        String uuid = "jeffries.yu-169"
        SfService sfService = new SfService("http://10.74.1.239:5555")
        sfService.deleteNodeLock(uuid)
    }

    @Test
    void getNodeDtos() {
        SfService sfService = new SfService("http://127.0.0.1:5555")
        sfService.createNodeLock([
                name: 'fxxk',
                list: [[ip: '10.32.47.163', count: 1], [ip: '10.32.56.179', count: 1]]
        ] as NodeLockReqDto)
        List<NodeDto> nodeDtos = sfService.getNodeDtos([
                name: 'fxxk2',
                list: [[ ip: '10.32.47.163', count: 1], [ip: '10.32.56.179', count: 1], [ip: '10.32.36.79', count: 1]]
        ] as NodeLockReqDto)
        println(JsonOutput.toJson(nodeDtos))
    }

    @Test
    void isUpgradeable() {
        SfService sfService = new SfService("http://10.74.1.239:5555")
        println("isUpgradeable = ${sfService.isUpgradeable()}")
    }

    @Test
    void testContains() {
        println("In-Webinar-Test-In-Webinar-Chrome-Daily-Jasmine-1698026812733".contains("jeffries.yu-1"))
    }
}
