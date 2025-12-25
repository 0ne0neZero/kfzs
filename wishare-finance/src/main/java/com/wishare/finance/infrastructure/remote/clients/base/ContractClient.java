package com.wishare.finance.infrastructure.remote.clients.base;


import com.wishare.finance.infrastructure.remote.vo.contract.*;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@OpenFeignClient(name = "wishare-contract", serverName = "合同系统", path = "/contract")
public interface ContractClient {

    @PostMapping(value = "/manage/contractIncome/getInfoListByIds",name = "应收账单合同信息查询")
    List<ContractV> getInfoListByIds(@RequestBody List<String> ids);

    @PostMapping(value = "/file/zjUpload",name = "中交影像上传",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ZJFileVo zjUpload(@RequestPart(value = "file",required = false)MultipartFile file , @RequestPart("billNo")String billNo);

    @PostMapping(value = "process/dealContractInvoice", name = "收票状态回调接口")
    Boolean dealContractInvoice(@RequestParam("id")  Long id, @RequestParam("state")Integer state);

    @GetMapping("/contractPaySettlementConclude/getDetailsById")
    ContractPaySettlementDetailsV getPayDetailsById(@RequestParam("id") String contractPlanId);

    @GetMapping("/contractIncomeSettlementConclude/getDetailsById")
    ContractIncomeSettlementDetailsV getIncomeDetailsById(@RequestParam("id") String contractPlanId);

    @PostMapping("/contractPayConclude/get")
    ContractPayConcludeV get(@Validated @RequestBody ContractPayConcludeF contractPayConcludeF);

    @PostMapping("/contractIncomeConclude/get")
    ContractIncomeConcludeV get(@Validated @RequestBody ContractIncomeConcludeF contractIncomeConcludeF);

    @PostMapping("/contractPayPlanConclude/getInnerInfoByContractIdsForPayApp")
    List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdOnPayApp(@RequestBody List<String> contractIds);

    @PostMapping("/contractPayPlanConclude/getInnerInfoByContractIds")
    List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdOnPay(@RequestBody List<String> contractIds);

    @PostMapping("/contractIncomePlanConclude/getInnerInfoByContractIds")
    List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdOnIncome(@RequestBody List<String> contractIds);

    @GetMapping("/contractPaySettlementConclude/invoice/getInfo")
    List<ContractSettlementInvoiceDetailF> getInvoice(@RequestParam("id") String id);

    @PostMapping(value = "/contractPayConcludeExpand/get", name = "获取支付合同扩展详细信息")
    ContractPayConcludeExpandV get(@Validated ContractPayConcludeExpandF contractPayConcludeExpandF);

    @PostMapping("/attachment/pageFront")
    PageV<AttachmentV> frontPage(@RequestBody PageF<SearchF<AttachmentE>> request);

    //获取所有合同（含补充协议）
    @PostMapping("/attachment/allContract/pageFront")
    PageV<AttachmentV> getAllFileList(@RequestBody PageF<SearchF<AttachmentE>> request);

    @PostMapping("/contractPaySettlementConclude/contractPaySettlement/updateStatus")
    Boolean updateStatus(@RequestBody ContractSettlementF contractSettlementF);

    @PostMapping("/paymentApplication/pass")
    void pass(@RequestBody FirstExamineMessageF message);
    @PostMapping("/paymentApplication/refuse")
    void refuse(@RequestBody FirstExamineMessageF message);

    @PostMapping(value = "/manage/contractBase/getContractPaymentList",name = "根据合同id批量获取收款信息")
    List<ContractPaymentVO> getContractPaymentList(@RequestBody List<String> ids);

    @PostMapping(value = "process/payNkBpmProcess", name = "支出合同NK结束BPM流程回调")
    Boolean payNkBpmProcess(@RequestParam("id")  String id, @RequestParam("status")Integer status);

    @PostMapping(value = "process/projectInitiationBpmProcess", name = "立项管理成本确认BPM流程回调")
    Boolean projectInitiationBpmProcess(@RequestParam("id")  String id, @RequestParam("status")Integer status);

    @PostMapping(value = "process/projectInitiationOrderForJDBpmProcess", name = "立项管理慧采下单BPM流程回调")
    Boolean projectInitiationOrderForJDBpmProcess(@RequestParam("id")  String id, @RequestParam("status")Integer status);

    @PostMapping(value = "process/incomeCorrectionBpmProcess", name = "收入合同修正BPM流程回调")
    Boolean incomeCorrectionBpmProcess(@RequestParam("id")  String id, @RequestParam("status")Integer status);
}