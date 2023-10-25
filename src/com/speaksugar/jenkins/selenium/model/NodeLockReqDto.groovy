package com.speaksugar.jenkins.selenium.model

class NodeLockReqDto {

    String name
    String uuid
    List<LockReq> list

    static class LockReq {
        String ip
        String name
        String os
        String arch
        String version
        String mark
        Integer count
    }

}
