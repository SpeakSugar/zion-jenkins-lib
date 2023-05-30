package com.speaksugar.jenkins.blockbuster

import com.speaksugar.jenkins.blockbuster.model.CiReqDto
import com.speaksugar.jenkins.blockbuster.model.CiResDto
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.util.HttpUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class BlockBusterService {

    private String url
    private Object jenkins

    BlockBusterService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }

    void batchDeleteCi(String uri) {
        List<CiResDto> ciResDtoList = this.getCis(uri, "1")
        ciResDtoList.each { it -> this.deleteCi(it.id) }
    }

    List<CiResDto> getCis(String uri = null, String scope = null, String filter = null) {
        URIBuilder uriBuilder = new URIBuilder("${this.url}/ci")
        if (uri != null) {
            uriBuilder.setParameter("uri", uri)
        }
        if (scope != null) {
            uriBuilder.setParameter("scope", scope)
        }
        if (filter != null) {
            uriBuilder.setParameter("filter", filter)
        }
        return HttpUtil.get(uriBuilder) as List<CiResDto>
    }

    CiResDto addCi(CiReqDto ciReqDto) {
        return HttpUtil.post("${url}/ci", ciReqDto) as CiResDto
    }

    String deleteCi(String id) {
        return HttpUtil.delete(new URIBuilder("${this.url}/ci/${id}"))
    }

}
