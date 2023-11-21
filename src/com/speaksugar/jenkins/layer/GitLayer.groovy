package com.speaksugar.jenkins.layer

import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.layer.model.CloneDto

class GitLayer {

    static clone(CloneDto cloneDto) {
        if (GlobalVars.jenkins != null) {
            GlobalVars.jenkins.sshagent(credentials: [cloneDto.credential]) {
                GlobalVars.jenkins.sh("git clone --single-branch --depth 1 --branch '${cloneDto.branch}' '${cloneDto.url}'")
            }
        }
    }
}
