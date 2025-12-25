package com.wishare.contract.apis.revision.remind;

import com.wishare.contract.apps.fo.remind.ContractRemindMessageConfigF;
import com.wishare.contract.apps.service.revision.remind.ContractRemindMessageConfigService;
import com.wishare.contract.domains.vo.revision.remind.ContractRemindMessageConfigV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"收入合同订立信息表"})
@RequestMapping("/contractRemindMessage")
public class ContractRemindMessageConfigApi {

    @Autowired
    private ContractRemindMessageConfigService contractRemindMessageConfigService;

//    @Autowired
//    private ContractRemindMessageSendService contractRemindMessageSendService;

    @ApiOperation(value = "保存提醒/预警消息配置")
    @PostMapping("/save")
    public Boolean save(@RequestBody ContractRemindMessageConfigF param) {
        contractRemindMessageConfigService.save(param);
        return Boolean.TRUE;
    }

    @ApiOperation(value = "获取提醒/预警消息配置")
    @GetMapping("/detail")
    public ContractRemindMessageConfigV detail() {
        return contractRemindMessageConfigService.detail();
    }

//    @ApiOperation(value = "获取提醒/预警消息配置")
//    @GetMapping("/sendTest")
//    public void send() {
//        contractRemindMessageConfigService.send();
//    }
//
//    @ApiOperation(value = "获取提醒/预警消息配置")
//    @PostMapping("/sendTestZJ")
//    public void sendZJ(@RequestBody ContractAndPlanInfoV contractAndPlanInfoV) {
//        Set<String> ids = new HashSet<>();
//        ids.add("L20143704");
//        contractRemindMessageSendService.sendZJNoticeMessage(ids, contractAndPlanInfoV);
//    }

}
