package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.bill.*;
import com.wishare.contract.apps.fo.revision.pay.settlement.*;
import com.wishare.contract.apps.service.revision.pay.ContractPaySettlementConcludeAppService;
import com.wishare.contract.domains.service.revision.pay.ContractPaySettlementConcludeService;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPaySettlementFileInfoV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPaySettlementPageV2;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同结算单表"})
@RequestMapping("/contractPaySettlementConclude")
public class ContractPaySettlementConcludeApi {

    private final ContractPaySettlementConcludeAppService contractPaySettlementConcludeAppService;

    private final ContractPaySettlementConcludeService contractPaySettlementConcludeService;


    @ApiOperation(value = "根据结算单id对应的主合同和补充协议合同id", notes = "根据结算单id对应的主合同和补充协议合同id")
    @GetMapping("/getAllContractId")
    public List<String> getAllContractId(@RequestParam("id") String id){
        return contractPaySettlementConcludeService.getAllContractId(id);
    }

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractPaySettlementDetailsV.class)
    @GetMapping("/getDetailsById")
    public ContractPaySettlementDetailsV getDetailsById(@RequestParam("id") String contractPlanId) {
        return contractPaySettlementConcludeAppService.getDetailsById(contractPlanId);
    }

    @ApiOperation(value = "根据合同id获取计量周期(原结算周期)周期下拉列表", notes = "根据合同id获取计量周期(原结算周期)下拉列表", response = ContractPayPlanPeriodV.class)
    @GetMapping("/getPlanPeriod")
    public ContractPayPlanPeriodV getPlanPeriod(@Validated @NotBlank(message = "合同id不能为空")
                                                    @RequestParam("contractId")  String contractId) {
        return contractPaySettlementConcludeAppService.getPlanPeriod(contractId);
    }

    @ApiOperation(value = "更新结算单的步骤", notes = "更新结算单的步骤", response = String.class)
    @PostMapping("/step/update")
    public String updateSettlementStep(@Validated @RequestBody ContractPaySettlementStepF stepF){
        return contractPaySettlementConcludeAppService.updateSettlementStep(stepF.getSettlementId(),stepF.getStep());
    }

    @ApiOperation(value = "获取结算单可用的成本预估计划列表", notes = "获取结算单可用的成本预估计划列表")
    @PostMapping("/getPlanList")
    public List<ContractPayPlanForSettlementV> getPlanList(@Validated @RequestBody ContractPayPlanListF contractPayPlanListF){
        return contractPaySettlementConcludeService.getPlanList(contractPayPlanListF);
    }


    @ApiOperation(value = "支出合同结算单分页列表")
    @PostMapping("/page")
    public PageV<ContractPaySettlementConcludeV> page(@RequestBody PageF<SearchF<ContractPaySettlementConcludePageF>> request) {
        return contractPaySettlementConcludeAppService.page(request);
    }

    @ApiOperation(value = "支出合同结算单分页列表")
    @PostMapping("/pageInfo")
    public PageV<ContractPaySettlementConcludeInfoV> pageInfo(@RequestBody PageF<SearchF<ContractPaySettlementConcludePageF>> request) {
        return contractPaySettlementConcludeAppService.pageInfo(request);
    }

    @ApiOperation(value = "支出合同结算单分页列表V2")
    @PostMapping("/v2/page")
    public PageV<ContractPaySettlementPageV2> pageV2(@RequestBody PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeAppService.pageV2(request);
    }

    @ApiOperation(value = "查询支出合同结算明细分页列表")
    @PostMapping("/v3/exportDetailsPage")
    public PageV<ContractPaySettlementPageV2> exportDetailsPage(@RequestBody PageF<SearchF<?>> req) {
        return contractPaySettlementConcludeAppService.exportDetailsPage(req);
    }

    @ApiOperation(value = "支出合同结算单-附件信息列表")
    @GetMapping("/v2/file/info")
    public ContractPaySettlementFileInfoV fileInfo(@RequestParam(value = "settlementId") String settlementId) {
        return contractPaySettlementConcludeAppService.fileInfo(settlementId);
    }

    @ApiOperation(value = "支出合同结算单分页列表V2")
    @PostMapping("/v2/page/mock")
    public PageV<ContractPaySettlementPageV2> pageMockV2(@RequestBody PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeAppService.pageMockV2(request);
    }

    @ApiOperation(value = "编辑列表", notes = "下拉列表，默认数量20", response = ContractPaySettlementConcludeEditV.class)
    @PostMapping("/list")
    public ContractPaySettlementConcludeEditV list(@Validated @RequestBody ContractPaySettlementConcludeListF contractPayPlanConcludeListF){
        return contractPaySettlementConcludeAppService.list(contractPayPlanConcludeListF);
    }

    @ApiOperation(value = "合同台账金额统计", response = ContractPaySettlementConcludeSumV.class)
    @PostMapping("/accountAmountSum")
    public ContractPaySettlementConcludeSumV accountAmountSum(@RequestBody PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeAppService.accountAmountSum(request);
    }

    @ApiOperation(value = "新增支出合同结算单", notes = "新增支出合同结算单")
    @PostMapping("/save")
    public String save(@Validated @RequestBody ContractPaySettlementAddF addF){
        return contractPaySettlementConcludeAppService.save(addF);
    }

    @ApiOperation(value = "更新支出合同结算单", notes = "更新支出合同结算单")
    @PostMapping("/update")
    public String update(@Validated @RequestBody ContractPaySettlementConcludeUpdateF contractPaySettlementConcludeUpdateF){
        contractPaySettlementConcludeAppService.update(contractPaySettlementConcludeUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "支出合同结算单删除", notes = "支出合同结算单删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPaySettlementConcludeAppService.removeById(id);
    }

    @ApiOperation(value = "支出合同结算单提交", notes = "支出合同结算单提交")
    @PostMapping("/sumbitId")
    public ContractPayProcessV sumbitId(@RequestParam("id") String id){
        return contractPaySettlementConcludeAppService.sumbitId(id);
    }

    @ApiOperation(value = "支出合同结算单反审核", notes = "支出合同结算单反审核")
    @PostMapping("/returnId")
    public String returnId(@RequestParam("id") String id){
        contractPaySettlementConcludeAppService.returnId(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "支出合同结算单收票", notes = "支出合同结算单收票")
    @PostMapping("/invoice")
    public String invoice(@RequestBody ContractSettlementsBillF contractSettlementsBillF){
        return contractPaySettlementConcludeAppService.invoice(contractSettlementsBillF);
    }

    @ApiOperation(value = "支出合同结算单收付款", notes = "支出合同结算单收付款")
    @PostMapping("/setFund")
    public String setFund(@RequestBody ContractSettlementsFundF contractSettlementsFundF){
        return contractPaySettlementConcludeAppService.setFund(contractSettlementsFundF);
    }

    @ApiOperation(value = "合同详情页分页查询关联票据数据", notes = "合同详情页分页查询关联票据数据")
    @PostMapping("/pageForContractDetailBill")
    public PageV<ContractPayBillV> pageForContractDetailBill(@RequestBody PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        return contractPaySettlementConcludeService.pageForContractDetailBill(form);
    }

    @ApiOperation(value = "泛微审批意见获取接口", response = OpinionApprovalV.class)
    @PostMapping("/opinionApproval")
    public OpinionApprovalV opinionApproval(@RequestParam("id") String id) {
        return contractPaySettlementConcludeService.opinionApproval(id);
    }

    @ApiOperation(value = "结算单打印信息", response = ContractPaySettlementInfoV.class)
    @PostMapping("/getSettleInfo")
    public ContractPaySettlementInfoV getSettleInfo(@RequestParam("id") String id) {
        return contractPaySettlementConcludeService.getSettleInfoV2(id);
    }

    @ApiOperation(value = "扣款明细分页查询")
    @PostMapping("/contractSettdeductionDetailPage")
    public PageV<ContractSettdeductionDetailV> contractSettdeductionDetailPage(@RequestBody PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeAppService.contractSettdeductionDetailPage(request);
    }

    @ApiOperation(value = "财务结算-结算信息回显")
    @GetMapping("/settlementSimpleInfo")
    public ContractPaySettlementSimpleInfoV settlementSimpleInfo(@RequestParam("id") String id) {
        return contractPaySettlementConcludeService.settlementSimpleInfo(id);
    }

    @ApiOperation(value = "保存财务结算")
    @PostMapping("/invoice/save")
    public Boolean invoiceSave(@RequestBody ContractSettlementInvoiceSaveF contractSettlementInvoiceSaveF) {
        return contractPaySettlementConcludeAppService.invoiceSave(contractSettlementInvoiceSaveF);
    }

    @ApiOperation(value = "查询财务结算-发票明细")
    @GetMapping("/invoice/getInfo")
    public List<ContractSettlementInvoiceDetailF> getInvoice(@RequestParam("id") String id) {
        return contractPaySettlementConcludeAppService.getInvoice(id);
    }

    @ApiOperation(value = "根据结算单名称查询审批完成但未发起支付的结算单")
    @PostMapping("/listSettleByCondition")
    public List<ContractPaySettlementF> listSettleByCondition(@RequestBody PaySettlementQueryF queryF) {
        return contractPaySettlementConcludeAppService.listSettleByCondition(queryF);
    }

    @PostMapping("/contractPaySettlement/updateStatus")
    Boolean updateStatus(@RequestBody ContractSettlementStatusF contractSettlementStatusF){
        return contractPaySettlementConcludeService.updateStatus(contractSettlementStatusF);
    }

    @ApiOperation(value = "根据结算单ID获取财务结算中其他附件信息")
    @GetMapping("/invoice/getFinanceOtherDetail")
    public ContractSettlementInvoiceOtherFileF getFinanceOtherDetail(@RequestParam("id") String id){
        return contractPaySettlementConcludeService.getFinanceOtherDetail(id);
    }

    @ApiOperation(value = "刷数据3")
    @PostMapping("/refresh3")
    public boolean refresh3(@RequestBody PageF<SearchF<?>> req) {
        return contractPaySettlementConcludeAppService.refresh3(req);
    }

    @ApiOperation(value = "刷数据4")
    @PostMapping("/refresh4")
    public boolean refresh4(@RequestBody PageF<SearchF<?>> req) {
        return contractPaySettlementConcludeAppService.refresh4(req);
    }

    @ApiOperation(value = "根据id删除结算审批")
    @GetMapping("/deletedPaySettlement")
    public Boolean deletedPaySettlement(@RequestParam("id") String id) {
        return contractPaySettlementConcludeService.deletedPaySettlementById( id);
    }
}
