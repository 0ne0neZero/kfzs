package com.wishare.contract.apis.contractset;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.fo.revision.SettBatchIdF;
import com.wishare.contract.apps.remote.fo.procreate.ProcessAdjustCallBackF;
import com.wishare.contract.apps.service.contractset.ContractProcessRecordService;
import com.wishare.owl.enhance.IOwlApiBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 中建流程创建回调
 *
 * @author long
 * @date 2023/7/18 16:55
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"第三方流程创建"})
@RequestMapping("/process")
public class ContractProcessRecordApi implements IOwlApiBase {
    private final ContractProcessRecordService contractProcessRecordService;

    /**
     * 合同审批完，中建流程创建回调，返回审批数据
     *
     * @param paCallBackF 合同流程创建回调数据
     * @param type 类型(1:合同订立支出 2:合同订立收入) 弃用外部入参，直接使用数据库查出来的类型
     * @return Boolean 是否调用成功
     */
    @PostMapping("/dealContractProcessRecord")
    @ApiOperation(value = "中建流程创建回调", notes = "中建流程创建回调")
    public Boolean dealContractProcessRecord(
            @RequestBody @ApiParam("合同流程创建回调数据")ProcessAdjustCallBackF paCallBackF,
            @RequestParam("type") @ApiParam("类型(1:合同订立支出 2:合同订立收入)") @NotNull(message = "合同类型不能为空") Integer type,
            @RequestParam(value = "skip", required = false) Integer skip) {
        log.info("【external中建流程创建回调参数】------> {}", JSON.toJSONString(paCallBackF));
        return contractProcessRecordService.dealContractProcess(paCallBackF, type, skip,Boolean.TRUE);
    }
    @PostMapping("/dealContractProcessRecordHand")
    @ApiOperation(value = "中建流程创建回调-手动调用，去除核销逻辑", notes = "中建流程创建回调，去除核销逻辑")
    public Boolean dealContractProcessRecordHand(
            @RequestBody @ApiParam("合同流程创建回调数据")ProcessAdjustCallBackF paCallBackF,
            @RequestParam("type") @ApiParam("类型(1:合同订立支出 2:合同订立收入)") @NotNull(message = "合同类型不能为空") Integer type,
            @RequestParam(value = "skip", required = false) Integer skip) {
        log.info("【external中建流程创建回调参数】------> {}", JSON.toJSONString(paCallBackF));
        return contractProcessRecordService.dealContractProcess(paCallBackF, type, skip,Boolean.FALSE);
    }

    @PostMapping("/batchHandlePayCostPlan")
    @ApiOperation("手动核销成本计划")
    public Boolean batchHandlePayCostPlan(@RequestBody SettBatchIdF settBatchIdF){
        return contractProcessRecordService.batchHandlePayCostPlan(settBatchIdF);
    }

    @PostMapping("/batchHandlePayIncomePlan")
    @ApiOperation("手动核销收入计划")
    public Boolean batchHandlePayIncomePlan(@RequestBody SettBatchIdF settBatchIdF){
        return contractProcessRecordService.batchHandlePayIncomePlan(settBatchIdF);
    }

    /**
     * 报账单审批完，创建回调
     * @return Boolean 是否调用成功
     */
    @PostMapping("/dealContractInvoice")
    @ApiOperation(value = "收票状态回调接口", notes = "收票状态回调接口")
    public Boolean dealContractInvoice(
            @RequestParam("id") @NotNull(message = "报账单id不能为空") Long id,
            @RequestParam("state") @ApiParam("审批状态(1:审批中 2:审批通过 3:审批拒绝)") @NotNull(message = "审批状态不能为空") Integer state) {
        log.info("【报账单回调参数ID】------> {}", id);
        return contractProcessRecordService.dealContractInvoice(id,state);
    }
    @PostMapping("/payNkBpmProcess")
    @ApiOperation(value = "支出合同NK结束BPM流程回调", notes = "支出合同NK结束BPM流程回调")
    public Boolean payNkBpmProcess(
            @RequestParam("id") @NotNull(message = "合同ID") String id,
            @RequestParam("status") @ApiParam("审批状态(2:审批通过 3:审批驳回)") @NotNull(message = "审批状态不能为空") Integer status) {
        log.info("【支出合同NK结束BPM流程回调】------> {}，{}", id, status);
        return contractProcessRecordService.payNkBpmProcess(id,status);
    }

    @PostMapping("/projectInitiationBpmProcess")
    @ApiOperation(value = "立项管理成本确认BPM流程回调", notes = "立项管理成本确认BPM流程回调")
    public Boolean projectInitiationBpmProcess(
            @RequestParam("id") @NotNull(message = "合同ID") String id,
            @RequestParam("status") @ApiParam("审批状态(2:审批通过 3:审批驳回)") @NotNull(message = "审批状态不能为空") Integer status) {
        log.info("【立项管理成本确认BPM流程回调】------> {}，{}", id, status);
        return contractProcessRecordService.projectInitiationBpmProcess(id,status);
    }

    @PostMapping("/projectInitiationOrderForJDBpmProcess")
    @ApiOperation(value = "立项管理慧采下单BPM流程回调", notes = "立项管理慧采下单BPM流程回调")
    public Boolean projectInitiationOrderForJDBpmProcess(
            @RequestParam("id") @NotNull(message = "合同ID") String id,
            @RequestParam("status") @ApiParam("审批状态(2:审批通过 3:审批驳回)") @NotNull(message = "审批状态不能为空") Integer status) {
        log.info("【立项管理慧采下单BPM流程回调】------> {}，{}", id, status);
        return contractProcessRecordService.projectInitiationOrderForJDBpmProcess(id,status);
    }

    @PostMapping("/incomeCorrectionBpmProcess")
    @ApiOperation(value = "收入合同修正BPM流程回调", notes = "收入合同修正BPM流程回调")
    public Boolean incomeCorrectionBpmProcess(
            @RequestParam("id") @NotNull(message = "合同ID") String id,
            @RequestParam("status") @ApiParam("审批状态(2:审批通过 3:审批驳回)") @NotNull(message = "审批状态不能为空") Integer status) {
        log.info("【收入合同修正BPM流程回调】------> {}，{}", id, status);
        return contractProcessRecordService.incomeCorrectionBpmProcess(id,status);
    }
}
