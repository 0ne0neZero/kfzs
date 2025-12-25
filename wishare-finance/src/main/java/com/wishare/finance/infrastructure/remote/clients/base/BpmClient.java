package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProcessProgressV;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProgressNode;
import com.wishare.finance.infrastructure.remote.vo.bpm.SendMessageReqVO;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@OpenFeignClient(name = "wishare-bpm", serverName = "流程中心服务", path = "/bpm")
public interface BpmClient {

    @ApiOperation(value = "发起审批流程", notes = "发起审批流程")
    @PostMapping("/process/start/{defId}")
    String processStart(@PathVariable("defId") @ApiParam("defId") String defId, @Validated @RequestBody ProcessStartF processStartF);

    @ApiOperation(value = "通过流程模型ID（流程定义KEY）查询流程模型数据", notes = "通过流程模型ID（流程定义KEY）查询流程模型数据")
    @GetMapping("/process/model/{code}")
    WflowModelHistorysV getProcessModelByFormId(@PathVariable("code") @ApiParam("code") String code);

    @ApiOperation(value = "查询流程表单数据及审批的进度步骤")
    @GetMapping("/process/progress/{instanceId}/{nodeId}")
    ProcessProgressV getProcessFormAndInstanceProgress(@PathVariable("instanceId") String instanceId,
                                                       @PathVariable(required = false, value = "nodeId") String nodeId);

    @ApiOperation(value = "查询流程节点信息")
    @GetMapping("/process/progressInfo/{instanceId}")
    List<ProgressNode> getInstanceProgressInfo(@PathVariable String instanceId);


    @ApiOperation(value = "发起信息至工作台", notes = "发起信息至工作台")
    @PostMapping("/process/sendMessage")
    String sendMessage(@RequestBody SendMessageReqVO processStartF);

}
