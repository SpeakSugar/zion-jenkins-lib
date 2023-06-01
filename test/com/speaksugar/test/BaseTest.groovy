package com.speaksugar.test

import com.speaksugar.jenkins.util.ThreadUtil
import org.junit.Test

class BaseTest {

    @Test
    void nullTest() {
        String fxxk = null
        println("fxxk = ${fxxk}")
    }

    @Test
    void arrayTest() {
        def arr = ["a1", "a2", "a3"]
        println(arr.size())
        for (String a : arr) {
            println(a)
        }
    }

    @Test
    void curryTest() {
        def cb = { throw new Exception('fxxk e') }
        cb.curry()
    }

    @Test
    void threadTest() {

        List<Object> results = ThreadUtil.execute([{ println('fxuk') }, {
            println('fxxxxk')
            throw new Exception('fxxk e')
        }, { println('fxqk') }, { println('fxak') }, { println('fxwk') }])
        println("result size = ${results.size()}")
        println("result value = ${results[0]}")
    }

    @Test
    void threadTest2() {
        List<Exception> exceptions = ThreadUtil.execute([{
                                                             println('fxuk')
                                                             throw new Exception('fxxk e')
                                                         }])
        println("exceptions size = ${exceptions.size()}")
    }
}
