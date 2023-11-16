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

### Selenium-Federation 资源整合
```groovy
import com.speaksugar.jenkins.global.GlobalVars
import com.speaksugar.jenkins.selenium.SfService
import com.speaksugar.jenkins.cmdserver.CmdServerService
import com.speaksugar.jenkins.blockbuster.BlockBusterService
import com.speaksugar.jenkins.selenium.model.NodeLockReqDto
import com.speaksugar.jenkins.selenium.model.NodeLockResDto
import com.speaksugar.jenkins.selenium.model.LockRes
import com.speaksugar.jenkins.blockbuster.model.CiReqDto
import com.speaksugar.jenkins.blockbuster.model.CiResDto
import com.speaksugar.jenkins.cmdserver.model.RcDTReqDto
import com.speaksugar.jenkins.util.ParallelUtil
import com.speaksugar.jenkins.wrapper.JupiterWrapper
import org.jenkinsci.plugins.workflow.libs.Library

@Library('zion-jenkins-lib') _

// 全局变量赋值
GlobalVars.jenkins = this // 非常重要的赋值
GlobalVars.SF_HUB_URL = "${sf_hub_url}" // sf hub 值, example: sf_hub_url = 'http://127.0.0.1:5555'
GlobalVars.JOB_NAME = "${job_name}" // jenkins job 名称

NodeLockResDto nodeLockResDto = null

try {

    SfService sfService = new SfService(GlobalVars.SF_HUB_URL)

    // 如果串行跑job时, 需要先释放 lock-by-issue 锁
    sfService.deleteNodeLock("lock-by-issue")
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
    
    // 0 为容错值, 当申请机器数大于等于(实际机器数 + 此值)时, 不会抛出异常
    nodeLockResDto = JupiterWrapper.createNodeLock(nodeLockReqDto, 0)

    // 重启 Node, 觉得有需要才加, 一般不加
    JupiterWrapper.restartNodes(nodeLockResDto)
    
    // 安装 Jupiter Desktop
    RcDTReqDto rcDTReqDto = [
            mac_arm_url  : "",
            mac_intel_url: "",
            win_arm_url  : "",
            win_intel_url: ""

    ]
    // 3 为容错值, 当安装失败机器数小于等于此值时, 不会抛出异常,
    // 失败机器从申请机器中移出且用 'lock-by-issue-${JOB_NAME}' 锁住
    // "RingCentral" 为 APP_NAME
    nodeLockResDto = JupiterWrapper.installElectron(rcDTReqDto, nodeLockResDto, 3, "RingCentral")
    
    // 更新 blockbuster api 
    BlockBusterService blockBusterService = new BlockBusterService("http://itop-xmn.lab.nordigy.ru:1389")
    blockBusterService.batchDeleteCi('xxx/xxx/xxx') // 删除这个 uri 和 uri 下所有子项
    for (LockRes lockRes : nodeLockResDto.list) {
        blockBusterService.addCi([
                // 需要把 lockRes 转成 ciReqDto 对象
        ])
    }

    // 执行 e2e 框架, 输出报告等...
    // 如何决定执行线程数? 根据 { nodeLockResDto.list.size() 大小 * 系数 } 得出, 由各个框架自己决定
    

} catch (Exception e) {
    println(e)
} finally {
    // kill Chrome, RingCentral 相关进程(可自定义)
    JupiterWrapper.killProcesses(nodeLockResDto, ["Chrome", "RingCentral"])

    // 删除临时文件目录
    JupiterWrapper.cleanTmpFile(nodeLockResDto)
    
    // job abort 或者发生异常时, 释放资源锁
    JupiterWrapper.deleteNodeLock(nodeLockResDto)
}
```
