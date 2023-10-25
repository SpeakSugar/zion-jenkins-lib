package com.speaksugar.test.wrapper

import org.junit.Test

class JupiterWrapperTest {

    @Test
    void arrayTest() {
        String eMsg = "[10.2.35.159:7777] fxxk, [10.2.35.153:7777] sxxt, [10.2.35.159:7777] fxxkxx"
        List<String> ips = eMsg.findAll(/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}/)
        println(ips.unique())
    }
}
