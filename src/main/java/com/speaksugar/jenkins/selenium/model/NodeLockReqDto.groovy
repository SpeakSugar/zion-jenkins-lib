package com.speaksugar.jenkins.selenium.model

class NodeLockReqDto {

    String name
    List<LockReq> list

    static class LockReq {
        String ip
        String name
        String os
        String arch
        String mark
        Integer count
    }

}
