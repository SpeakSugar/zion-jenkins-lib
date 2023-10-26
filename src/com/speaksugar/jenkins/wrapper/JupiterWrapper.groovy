package com.speaksugar.jenkins.wrapper

import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.exception.ZionJenkinsException
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.SfService
import com.speaksugar.jenkins.selenium.model.NodeDto
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import com.speaksugar.jenkins.util.ParallelUtil

class JupiterWrapper {

    static NodeLockResDto createNodeLock(NodeLockReqDto nodeLockReqDto, Integer faultTolerant) {
        SfService sfService = new SfService(GlobalVars.SF_HUB_URL)
        List<NodeDto> nodeDtos = sfService.getNodeDtos(nodeLockReqDto)
        Integer expectCount = nodeLockReqDto.list.get(0).count
        Integer actualCount = nodeDtos.size()
        if (expectCount - actualCount <= faultTolerant) {
            nodeLockReqDto.list.get(0).count = actualCount
            return sfService.createNodeLock(nodeLockReqDto)
        } else {
            throw new ZionJenkinsException("[ZION-JENKINS-LIB] create node lock failed, expect count = ${expectCount}, actual count = ${actualCount}")
        }
    }

    // appName 可以不传, 默认为"RingCentral", 当安装其他 brand 时, 需要指定值
    static NodeLockResDto installElectron(RcDTReqDto rcDTReqDto,
                                          NodeLockResDto nodeLockResDto,
                                          Integer faultTolerant,
                                          String appName = "RingCentral") {
        List<Closure> install_closures = []
        for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
            CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
            install_closures.add({ cmdServerService.installRcDT(rcDTReqDto, appName) })
        }
        try {
            ParallelUtil.execute(install_closures)
            return nodeLockResDto
        } catch (Exception e) {
            println("[ZION-JENKINS-LIB] install electron err: ${e.message}")
            List<String> ips = e.message.findAll(/\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}/).unique()
            List<Object> excludeIps = new ArrayList<>()
            if (ips.size() > faultTolerant) {
                throw new ZionJenkinsException("[ZION-JENKINS-LIB] install electron failed, failed machines ${ips.size()} > ${faultTolerant}")
            }
            for (String ip : ips) {
                excludeIps.add([ip: ip, count: 1])
            }
            SfService sfService = new SfService(GlobalVars.SF_HUB_URL)
            sfService.updateNodeLock([
                    name: "lock-by-issue-${GlobalVars.JOB_NAME}",
                    uuid: nodeLockResDto.uuid,
                    list: excludeIps
            ] as NodeLockReqDto)
            return sfService.getNodeLock(nodeLockResDto.uuid).get(0)
        }
    }

    static killProcesses(NodeLockResDto nodeLockResDto, List<String> pNames) {
        if (nodeLockResDto == null) {
            return
        }
        List<Closure> kill_closures = []
        for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
            CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
            for (String pName : pNames) {
                String _pName = pName
                kill_closures.add({
                    cmdServerService.killProcess(_pName)
                })
            }
        }
        ParallelUtil.execute(kill_closures)
    }

    static cleanTmpFile(NodeLockResDto nodeLockResDto) {
        if (nodeLockResDto == null) {
            return
        }
        try {
            for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
                CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
                cmdServerService.cleanTmpFile()
            }
            println("[ZION-JENKINS-LIB] cleanTmpFile success")
        } catch (e) {
            println("[ZION-JENKINS-LIB] cleanTmpFile failed")
        }
    }

    static deleteNodeLock(NodeLockResDto nodeLockResDto) {
        if (nodeLockResDto == null) {
            return
        }
        try {
            SfService sfService = new SfService(GlobalVars.SF_HUB_URL)
            sfService.deleteNodeLock(nodeLockResDto.uuid)
            println("[ZION-JENKINS-LIB] deleteNodeLock ${nodeLockResDto.uuid} success")
        } catch (e) {
            println("[ZION-JENKINS-LIB] deleteNodeLock ${nodeLockResDto.uuid} failed")
        }
    }
}
