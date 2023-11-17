package com.speaksugar.jenkins.layer

import com.speaksugar.jenkins.global.GlobalVars

class LogLayer {

    static info(String msg) {
        if (GlobalVars.jenkins != null) {
            GlobalVars.jenkins.echo(msg)
        } else {
            println(msg)
        }
    }

}
