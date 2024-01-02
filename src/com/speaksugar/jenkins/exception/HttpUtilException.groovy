package com.speaksugar.jenkins.exception

class HttpUtilException extends RuntimeException {

    HttpUtilException(String msg) {
        super(msg);
    }

    HttpUtilException(String msg, Exception e) {
        super(msg, e);
    }

}
