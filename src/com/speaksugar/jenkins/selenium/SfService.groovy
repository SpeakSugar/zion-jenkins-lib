package com.speaksugar.jenkins.selenium

import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.model.NodeDto
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

    List<NodeDto> getNodeDtos(NodeLockReqDto nodeLockReqDto) {
        return HttpUtil.post("${this.url}/nodeDtos", nodeLockReqDto) as List<NodeDto>
    }

    NodeLockResDto createNodeLock(NodeLockReqDto nodeLockReqDto) {
        return HttpUtil.post("${this.url}/nodeLock", nodeLockReqDto) as NodeLockResDto
    }

    NodeLockResDto updateNodeLock(NodeLockReqDto nodeLockReqDto) {
        return HttpUtil.post("${this.url}/updateNodeLock", nodeLockReqDto) as NodeLockResDto
    }

    List<NodeLockResDto> getNodeLock(String uuid) {
        if (uuid == null) {
            return HttpUtil.get(new URIBuilder("${this.url}/nodeLock")) as List<NodeLockResDto>
        } else {
            return HttpUtil.get(new URIBuilder("${this.url}/nodeLock/${uuid}")) as List<NodeLockResDto>
        }
    }

    String deleteNodeLock(String uuid) {
        List<NodeLockResDto> nodeLockResDtos = this.getNodeLock(null)
        for (NodeLockResDto nodeLockResDto : nodeLockResDtos) {
            if (nodeLockResDto.uuid.contains(uuid)) {
                HttpUtil.delete(new URIBuilder("${this.url}/nodeLock/${nodeLockResDto.uuid}"))
            }
        }
        return "Delete Success"
    }

}
