package com.speaksugar.jenkins.cmdserver

import com.speaksugar.jenkins.cmdserver.constant.OS
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.util.HttpUtil
import org.apache.http.client.utils.URIBuilder

class CmdServerService {

    private String url
    private Object jenkins

    CmdServerService(String url) {
        this.url = url
        this.jenkins = GlobalVars.jenkins
    }

    String getOs() {
        return HttpUtil.get(new URIBuilder("${this.url}/os"))
    }

    String getArch() {
        return HttpUtil.get(new URIBuilder("${this.url}/arch"))
    }

    void installRcDT(RcDTReqDto rcDTReqDto) {
        String os = getOs()
        String arch = getArch()
        if (OS.MAC == os) {
            String appUrl = arch == "intel" ? rcDTReqDto.mac_intel_url : rcDTReqDto.mac_arm_url
            String download_cmd = "curl -s \"${appUrl}\" > ~/Downloads/rc.pkg"
            String install_cmd = "sudo installer -verbose -pkg ~/Downloads/rc.pkg -target /"
            String kill_cmd = "kill -9 \$(ps -ef | grep rc.pkg | awk '{print \$2}' | awk 'NR==1')"
            HttpUtil.post("${this.url}/cmd", [cmd: download_cmd, timeout: 300e3])
            HttpUtil.post("${this.url}/cmd", [cmd: install_cmd, timeout: 300e3])
            Thread.sleep(10000)
            HttpUtil.post("${this.url}/cmd", [cmd: kill_cmd])
        }
        if (OS.WIN == os) {
            // windows need uninstall app before
            // windows uninstall app no need appName
            uninstallRcDT(null)
            String appUrl = arch == "intel" ? rcDTReqDto.win_intel_url : rcDTReqDto.win_arm_url
            String download_cmd = "curl -s \"${appUrl}\" > %USERPROFILE%\\Downloads\\RingCentral.msi"
            String install_cmd = "msiexec /i \"%USERPROFILE%\\Downloads\\RingCentral.msi\""
            HttpUtil.post("${this.url}/cmd", [cmd: download_cmd, timeout: 300e3])
            HttpUtil.post("${this.url}/cmd", [cmd: install_cmd, timeout: 300e3])
        }

    }

    void uninstallRcDT(String appName) {
        String os = getOs()
        if (OS.MAC == os) {
            String cmd = "rm -rf \"${appName}.app\""
            HttpUtil.post("${this.url}/cmd", [cmd: cmd])
        }
        if (OS.WIN == os) {
            String cmd = "msiexec /x \"%USERPROFILE%\\Downloads\\RingCentral.msi\" /qr"
            try {
                HttpUtil.post("${this.url}/cmd", [cmd: cmd])
            } catch (Exception ignored) {
                // msiexec /x "%USERPROFILE%\Downloads\RingCentral.msi" /qr will cause e when app has uninstalled.
            }
        }
    }

}
