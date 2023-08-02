package com.speaksugar.jenkins.util

class RetryUtil {
    static retry(Closure<?> cb, int maxRetryTime = 10, int retryInterval = 5000) {
        def error
        def startTimeOfRetry = System.currentTimeMillis()

        for (int i = 0; i < maxRetryTime; i++) {
            if (System.currentTimeMillis() - startTimeOfRetry >= 10000) {
                println "is retrying, please wait patiently"
                startTimeOfRetry = System.currentTimeMillis()
            }
            try {
                return cb.call()
            } catch (Exception err) {
                error = err
                sleep(retryInterval)
            }
        }
        throw error
    }
}
