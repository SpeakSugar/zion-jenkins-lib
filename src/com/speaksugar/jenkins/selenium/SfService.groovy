package com.speaksugar.jenkins.selenium

import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class SfService {

    private String url
    private Object jenkins

    SfService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }

    NodeLockResDto createNodeLock(NodeLockReqDto nodeLockReqDto) {
        CloseableHttpClient httpClient = HttpClients.createDefault()
        try {
            HttpPost httpPost = new HttpPost("${this.url}/nodeLock")
            httpPost.setEntity(new StringEntity(JsonOutput.toJson(nodeLockReqDto), ContentType.APPLICATION_JSON))
            HttpResponse response = httpClient.execute(httpPost)
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8")
            return new JsonSlurper().parseText(responseBody) as NodeLockResDto
        } finally {
            httpClient.close()
        }
    }

}
