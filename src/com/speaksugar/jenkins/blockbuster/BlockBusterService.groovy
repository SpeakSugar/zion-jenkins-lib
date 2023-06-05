package com.speaksugar.jenkins.blockbuster

import com.speaksugar.jenkins.blockbuster.model.CiReqDto
import com.speaksugar.jenkins.blockbuster.model.CiResDto
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.util.HttpUtil
import org.apache.http.client.utils.URIBuilder

class BlockBusterService {

    private String url
    private Object jenkins

    BlockBusterService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }

    void batchDeleteCi(String uri) {
        List<CiResDto> sub_ciResDtoList = this.getCis(uri, "1")
        sub_ciResDtoList.each { it -> this.deleteCi(it.id) }
        List<CiResDto> ciResDtoList = this.getCis(uri, "0")
        this.deleteCi(ciResDtoList.get(0).id)
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
