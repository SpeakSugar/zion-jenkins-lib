package com.speaksugar.jenkins.blockbuster

import com.speaksugar.jenkins.global.GlobalVars

class BlockBusterService {

    private String url
    private Object jenkins

    public BlockBusterService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }


}
