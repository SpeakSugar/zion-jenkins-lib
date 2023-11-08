package com.speaksugar.jenkins.util

import com.speaksugar.jenkins.global.GlobalVars

class LogUtil {

    static info(String msg) {
        if (GlobalVars.jenkins != null) {
            GlobalVars.jenkins.echo(msg)
        } else {
            println(msg)
        }
    }

}
