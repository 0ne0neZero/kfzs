package com.wishare.finance.apis.pushbill;

import com.wishare.finance.apps.process.fo.ApprovalQueryF;
import com.wishare.finance.apps.process.fo.ProcessAdjustCallBackF;
import com.wishare.finance.apps.process.service.PaymentAppProcessService;
import com.wishare.finance.apps.process.vo.OpinionApprovalV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDxZJV;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.FinanceProcessRecordZJRepository;
import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"OA审批回调处理"})
@RestController
@RequestMapping("/process")
public class ProcessCallbackApi {

    @Autowired
    private FinanceProcessRecordZJRepository processRecordZJRepository;
    @Autowired
    private PaymentAppProcessService paymentAppProcessService;

    @PostMapping("/dealFinanceProcessRecord")
    @ApiOperation(value = "审批回调处理")
    public Boolean dealFinanceProcessRecord(@RequestBody ProcessAdjustCallBackF paCallBackF,
                                            @RequestParam("type") Integer type,
                                            @RequestParam(value = "skip", required = false) Integer skip) {
        return processRecordZJRepository.dealFinanceProcessRecord(paCallBackF, type, skip);
    }

    @PostMapping("/opinionApproval")
    @ApiOperation(value = "查询审批信息")
    public OpinionApprovalV opinionApproval(@RequestBody ApprovalQueryF approvalQueryF) {
        return processRecordZJRepository.opinionApproval(approvalQueryF);
    }


    @PostMapping("/opinionApproval/v2")
    @ApiOperation(value = "查询审批信息V2")
    public OpinionApprovalV2 opinionApprovalV2(@RequestParam("processId") String processId) {
        return processRecordZJRepository.opinionApprovalV2(processId);
    }

    @GetMapping("/refreshHtbzdByYwzfsq")
    @ApiOperation(value = "根据业务支付申请单号，生成合同报账单", notes = "根据业务支付申请单号，生成合同报账单")
    public String refreshHtbzdByYwzfsq (@Validated @RequestParam Long id) {
        return paymentAppProcessService.refreshHtbzdByYwzfsq(id);
    }
}
