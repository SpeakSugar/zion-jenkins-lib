package com.speaksugar.test.blockbuster

import com.speaksugar.jenkins.blockbuster.BlockBusterService
import com.speaksugar.jenkins.blockbuster.model.CiReqDto
import com.speaksugar.jenkins.blockbuster.model.CiResDto
import org.junit.Test

class BlockBusterServiceTest {

    @Test
    void getCis() {
        BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
        List<CiResDto> ciResDtoList = blockBusterService.getCis("/jeffries/xxx/aaa/OASIS")
        println("ciResDtoList length = ${ciResDtoList.size()}")
    }

    @Test
    void addCi() {
        BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
        CiReqDto ciReqDto = [
                uri     : "/jeffries/xxx/aaa/OASIS",
                data    : [
                        system  : "mac",
                        host    : "10.32.35.207",
                        port    : 22,
                        username: "rcadmin",
                        password: "123123",
                        hasAudio: true
                ],
                maxLease: 1
        ]
        CiResDto ciResDto = blockBusterService.addCi(ciReqDto)
        println(ciResDto.name)
    }

    @Test
    void batchDeleteCi() {
        BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
        blockBusterService.batchDeleteCi("/jeffries/xxx/aaa")
    }

    @Test
    void deleteCi() {
        String id = "a90f05f9-6bef-4042-b168-d613548a4ce4"
        BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
        blockBusterService.deleteCi(id)
    }
}
