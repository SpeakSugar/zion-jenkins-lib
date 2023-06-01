package com.speaksugar.jenkins.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadUtil {

    static <T> List<T> execute(List<Closure<T>> closures) {
        ExecutorService threadPool = Executors.newFixedThreadPool(closures.size())
        List<T> results = []
        List<Exception> exceptions = []
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
        if (exceptions.size() != 0) {

        }
        return results
    }

}
