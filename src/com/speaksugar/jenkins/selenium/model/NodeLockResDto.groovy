package com.speaksugar.jenkins.selenium.model

class NodeLockResDto {

    String uuid
    List<LockRes> list

    static class LockRes {
        String ip
        String name
        String os
        String arch
        String version
        Boolean hasAudio
        Boolean hasCamera
        String mark
        String user
        String password
    }

}
