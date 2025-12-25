package com.wishare.contract.apps.remote.clients;

import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/12/7/20:52
 */
@OpenFeignClient(name = "wishare-alert", serverName = "第三方调用中心", path = "/alert")
public interface AlertFeignClient {

    @PostMapping("/alert/scan//contract")
    @ApiOperation(value = "插入合同预警到定时扫描表", notes = "插入合同预警到定时扫描表")
    public ResponseEntity<Boolean> addContractAlertScan(@RequestParam(value = "id") @ApiParam(required = true, value = "合同id") Long id);

}
