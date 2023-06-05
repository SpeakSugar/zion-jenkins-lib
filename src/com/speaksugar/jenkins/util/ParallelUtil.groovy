package com.speaksugar.jenkins.util

import com.speaksugar.jenkins.exception.ThreadUtilException
import com.speaksugar.jenkins.global.GlobalVars

import java.util.concurrent.CopyOnWriteArrayList

class ParallelUtil {

    static <T> List<T> execute(List<Closure<T>> closures) {
        List<T> results = new CopyOnWriteArrayList<>()
        List<Exception> exceptions = new CopyOnWriteArrayList<>()
        Map<String, Closure> jobMap = [:]
        for (int i = 0; i < closures.size(); i++) {
            jobMap.put("job ${i}", {
                try {
                    results.add(closures.get(i)())
                } catch (Exception e) {
                    exceptions.add(e)
                }
            })
        }
        GlobalVars.jenkins.parallel(jobMap)
        if (exceptions.size() != 0) {
            String errMsg = ""
            for (Exception e : exceptions) {
                errMsg = "${errMsg}${e.message}\n"
            }
            throw new ThreadUtilException(errMsg)
        }
        return results
    }

}
