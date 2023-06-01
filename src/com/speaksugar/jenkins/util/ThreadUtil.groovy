package com.speaksugar.jenkins.util

import com.speaksugar.jenkins.exception.ThreadUtilException

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadUtil {

    static <T> List<T> execute(List<Closure<T>> closures) {
        ExecutorService threadPool = Executors.newFixedThreadPool(closures.size())
        List<T> results = new CopyOnWriteArrayList<>()
        List<Exception> exceptions = new CopyOnWriteArrayList<>()
        for (int i = 0; i < closures.size(); i++) {
            Closure cb= closures.get(i)
            threadPool.submit({
                try {
                    results.add(cb())
                } catch (Exception e) {
                    exceptions.add(e)
                }
            })
        }
        threadPool.shutdown()
        threadPool.awaitTermination(30, TimeUnit.MINUTES)
        println("exceptions.size = ${exceptions.size()}")
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
