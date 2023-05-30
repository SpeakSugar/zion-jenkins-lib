package com.speaksugar.jenkins.selenium

import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import com.speaksugar.jenkins.util.HttpUtil
import org.apache.http.client.utils.URIBuilder

class SfService {

    private String url
    private Object jenkins

    SfService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }

    NodeLockResDto createNodeLock(NodeLockReqDto nodeLockReqDto) {
        return HttpUtil.post("${this.url}/nodeLock", nodeLockReqDto) as NodeLockResDto
    }

    String deleteNodeLock(String uuid) {
        return HttpUtil.delete(new URIBuilder("${this.url}/nodeLock/${uuid}"))
    }

}
