package com.speaksugar.test.wrapper

import org.junit.Test

class JupiterWrapperTest {

    @Test
    void arrayTest() {
        String eMsg = "[10.2.35.159:7777] fxxk, [10.2.35.153:7777] sxxt, [10.2.35.159:7777] fxxkxx"
        List<String> ips = eMsg.findAll(/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}/)
        println(ips.unique())
    }

    @Test
    void countTest() {
        Integer expectCount = 5
        Integer actualCount = 6
        Integer faultTolerant = 1
        Integer count = 0
        if (expectCount - actualCount <= faultTolerant) {
            expectCount >= actualCount ? (count = actualCount) : (count = expectCount)
        }
        println("count = ${count}")
    }
}
