package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.remote.vo.bpm.ProcessStartF;
import com.wishare.contract.apps.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author hhb
 * @describe
 * @date 2025/10/28 18:28
 */
@OpenFeignClient(name = "wishare-bpm", serverName = "流程中心服务", path = "/bpm")
public interface BpmClient {
    @ApiOperation(value = "通过流程模型ID（流程定义KEY）查询流程模型数据", notes = "通过流程模型ID（流程定义KEY）查询流程模型数据")
    @GetMapping("/process/model/{code}")
    WflowModelHistorysV getProcessModelByFormId(@PathVariable("code") @ApiParam("code") String code);

    @ApiOperation(value = "发起审批流程", notes = "发起审批流程")
    @PostMapping("/process/start/{defId}")
    String processStart(@PathVariable("defId") @ApiParam("defId") String defId, @Validated @RequestBody ProcessStartF processStartF);
}
