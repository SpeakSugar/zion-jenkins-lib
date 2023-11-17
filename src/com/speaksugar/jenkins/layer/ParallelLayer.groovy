package com.speaksugar.jenkins.layer

import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.util.ParallelUtil
import com.speaksugar.jenkins.util.ThreadUtil

class ParallelLayer {

    static <T> List<T> execute(List<Closure<T>> closures) {
        if (GlobalVars.jenkins != null) {
            return ParallelUtil.execute(closures)
        } else {
            return ThreadUtil.execute(closures)
        }
    }
}
