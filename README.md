# Zion-Jenkins-Lib

## Introduction

Jenkins 共享库.

## Requirement
```
jdk 17 或以上
```

## Usage

```xml
<dependency>
    <groupId>com.speaksugar.jenkins</groupId>
    <artifactId>zion-jenkins-lib</artifactId>
    <version>0.0.7-SNAPSHOT</version>
</dependency>
```

```groovy
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.SfService
import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.blockbuster.BlockBusterService
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import com.speaksugar.jenkins.blockbuster.model.CiReqDto
import com.speaksugar.jenkins.blockbuster.model.CiResDto
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.util.ParallelUtil
import org.jenkinsci.plugins.workflow.libs.Library

@Library('zion-jenkins-lib') _

NodeLockResDto nodeLockResDto = null
// example: sf_hub_url = 'http://127.0.0.1:5555'
SfService sfService = new SfService("${sf_hub_url}")

try {
    // 申请资源锁
    NodeLockReqDto nodeLockReqDto = [
            name: 'xxx', // 必填, 最好和 job 名称相关
            list: [
                    [
                            ip   : "xxx.xxx.xxx.xxx", // 选填, 申请的机器 ip 
                            name : "SDET-XMN-RCXME0874", // 选填, 申请的机器 name
                            arch : "intel", // 选填, intel or arm
                            os   : 'win', // 选填, win or mac
                            mark : "DT", // 选填, DT or webinar, 填了此值后优先选择带有此 mark 的机器, 不填则最后才选
                            count: 5 // 必填, 申请的机器数量
                    ],
                    [
                            // 可以有多个对象, 便于一次申请多个类型的机器
                    ]
            ]
    ]
    nodeLockResDto = sfService.createNodeLock(nodeLockReqDto)

    // 更新 blockbuster api 
    BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
    blockBusterService.batchDeleteCi('xxx/xxx/xxx') // 删除这个 uri 和 uri 下所有子项
    for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
        blockBusterService.addCi([
                // 需要把 lockRes 转成 ciReqDto 对象
        ])
    }

    GlobalVars.jenkins = this // 非常重要的赋值
    // 安装 Jupiter Desktop
    List<Closure> install_closures = []
    for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
        // 注意: 由于闭包的特性, 这行代码必须写在闭包外
        CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
        install_closures.add({
            RcDTReqDto rcDTReqDto = [
                    mac_arm_url  : "",
                    mac_intel_url: "",
                    win_arm_url  : "",
                    win_intel_url: ""

            ]
            String appName = "RingCentral"
            cmdServerService.installRcDT(rcDTReqDto, appName) // appName 可以不传, 默认为"RingCentral", 当安装其他 brand 时, 需要指定值
        })
    }
    ParallelUtil.execute(install_closures)

    // 执行 e2e 框架, 输出报告等...

} catch (Exception e) {
    println(e)
} finally {
    // kill Chrome, RingCentral 相关进程
    List<Closure> kill_closures = []
    for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
        CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
        kill_closures.add({
            cmdServerService.killProcess('Chrome')
            cmdServerService.killProcess("${AppName}")
        })
    }
    ParallelUtil.execute(kill_closures)

    // 删除临时文件目录
    if (nodeLockResDto != null) {
        try {
            for (NodeLockResDto.LockRes lockRes : nodeLockResDto.list) {
                CmdServerService cmdServerService = new CmdServerService("http://${lockRes.ip}:7777")
                cmdServerService.cleanTmpFile()
            }
        } catch (e) {
            println("cleanTmpFile failed")
        }
    }
    // job abort 或者发生异常时, 释放资源锁
    if (nodeLockResDto != null) {
        try {
            sfService.deleteNodeLock(nodeLockResDto.uuid)
        } catch (e) {
            println("deleteNodeLock ${nodeLockResDto.uuid} failed")
        }
    }
}
```
