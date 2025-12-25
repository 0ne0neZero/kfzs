package com.wishare.finance.apis.pushbill;


import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.pushbill.fo.*;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.Mdm63Handler;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63CertainF;
import com.wishare.finance.apps.service.bill.PaymentApplicationFormService;
import com.wishare.finance.apps.service.pushbill.PushBillDxZJAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillFileZJService;
import com.wishare.finance.apps.service.remind.RemindMessageConfigServiceImpl;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderStatusResult;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusDel;
import com.wishare.finance.infrastructure.remote.vo.contract.FirstExamineMessageF;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = {"汇总单据中交-对下"})
@RestController
@RequestMapping("/voucherbillDxZJ")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDxZJApi implements ApiBase {

    private final PushBillDxZJAppService pushBillDxZJAppService;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final VoucherBillFileZJService voucherBillFileZJService;
    private final PaymentApplicationFormService paymentApplicationFormService;
    private final RemindMessageConfigServiceImpl remindMessageConfigService;
    private final Mdm63Handler mdm63Handler;


    @PostMapping("/page")
    @ApiOperation(value = "获取单据(分页)", notes = "获取单据(分页)")
    public PageV<VoucherBillZJV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form){
        return pushBillDxZJAppService.pageBySearch(form);
    }

    @PostMapping("/getMoney")
    @ApiOperation(value = "获取单据总金额", notes = "获取单据总金额")
    public VoucherBillZJMoneyV getMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return pushBillDxZJAppService.getMoney(form);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "对下-通过单据主表id查询单据信息", notes = "对下-通过单据主表id查询单据信息")
    public VoucherBillDxZJV getById (@Validated @RequestParam Long id) {
        return pushBillDxZJAppService.getById(id, false);
    }

    @PostMapping("/infer/QueryFinanceOrderDealResult")
    @ApiOperation(value = "主动查询推单状态接口", notes = "主动查询推单状态接口")
    public void queryFinanceOrderDealResult(@RequestBody OrderStatusBody orderStatusBody) {
        pushBillDxZJAppService.queryFinanceOrderDealResult(orderStatusBody);
    }

    @GetMapping("/autoFile")
    @ApiOperation(value = "合同报账单-计提 自动文件", notes = "合同报账单-计提 自动文件")
    public void autoFiles(@RequestParam(value = "voucherBillNo") String voucherBillNo, HttpServletResponse response){
        pushBillDxZJAppService.autoFile(voucherBillNo,response);
    }

    @PostMapping("/addFileInfo")
    @ApiOperation(value = "对下结算单-报账单新增影像信息", notes = "对下结算单-报账单新增影像信息")
    public boolean addFileInfo(@RequestBody @Validated UpLoadFileF upLoadFileF) {
        return voucherBillFileZJRepository.addFileInfo(upLoadFileF);
    }

    @GetMapping("/deleteFileInfo")
    @ApiOperation(value = "对下结算单-报账单删除影像信息", notes = "对下结算单-报账单删除影像信息")
    public boolean deleteFileInfo(@Param("id") Long id) {
        return voucherBillFileZJService.dxDeleteById(id);
    }

    @PostMapping("/infer/delPushOrder")
    @ApiOperation(value = "对下-中交删单接口", notes = "对下-中交删单接口")
    public OrderStatusResult delPushOrder(@RequestBody OrderStatusDel orderStatusDel) {
        return pushBillDxZJAppService.delOrderDealResult(orderStatusDel);
    }

    @PostMapping("/infer/batch/settlement")
    @ApiOperation(value = "批量同步-计提/实签", notes = "批量同步")
    public SyncBatchVoucherResultV syncBatchVoucherForSettlement(@RequestBody SyncBatchPushZJBillF syncBatchPushBillF) {
        log.info("合同报账单推送财务云入参：{}", JSONObject.toJSONString(syncBatchPushBillF));
        return pushBillDxZJAppService.syncBatchPushBillForSettlement(syncBatchPushBillF);
    }

    @PostMapping("/getPushCaiWuYunData")
    @ApiOperation(value = "根据报账单ID获取推送财务云数据")
    public String getPushCaiWuYunData (@RequestBody SyncBatchPushZJBillF syncBatchPushBillF) {
        return pushBillDxZJAppService.getPushCaiWuYunData(syncBatchPushBillF);
    }

    @PostMapping("/generate/settlement")
    @ApiOperation(value = "生成报账单-实签", notes = "生成报账单-实签")
    public void generateOnSettlement(@RequestBody VoucherBillGenerateOnContractSettlementF generateF) {
        pushBillDxZJAppService.generateOnSettlement(generateF);
    }


    @PostMapping("/paymentApplicationForm/list")
    @ApiOperation(value = "支付申请单-列表", notes = "支付申请单-列表")
    public PageV<PaymentApplicationFormZJV> list(@Validated @RequestBody PageF<SearchF<?>> form) {
        return paymentApplicationFormService.pageBySearch(form);
    }


    @PostMapping("/paymentApplicationForm/create")
    @ApiOperation(value = "支付申请单-创建/修改", notes = "支付申请单-创建/修改")
    public PaymentApplicationFormZJ createPaymentApplicationForm(@Validated @RequestBody PaymentApplicationAddFormF applicationAddFormF) {
        return paymentApplicationFormService.createPaymentApplicationForm(applicationAddFormF);
    }

    @PostMapping("/paymentApplicationForm/preGenerateDetail")
    @ApiOperation(value = "支付申请单-预生成款项明细和支付明细", notes = "支付申请单-预生成款项明细和支付明细")
    public PaymentApplicationFormDetailV preGenerateDetail(@Validated @RequestBody PreGenerateDetailF preGenerateDetailF) {
        return paymentApplicationFormService.preGenerateDetail(preGenerateDetailF);
    }


    @PostMapping("/paymentApplicationForm/detail")
    @ApiOperation(value = "支付申请单-详情", notes = "支付申请单-详情")
    public PaymentApplicationFormZJV detail(@RequestParam String payApplicationFormId) {
        return paymentApplicationFormService.detail(payApplicationFormId);
    }


    @PostMapping("/queryBZDetail")
    @ApiOperation(value = "支付申请单-报账明细", notes = "支付申请单-报账明细")
    public PageV<PaymentApplicationBZDetailV> queryBZDetail(@RequestBody PageF<SearchF<?>> form) {
        return paymentApplicationFormService.queryBZDetail(form);
    }

    @PostMapping("/queryKXDetails")
    @ApiOperation(value = "支付申请单-款项明细", notes = "支付申请单-款项明细")
    public PageV<PaymentApplicationKXDetailV> queryKXDetails(@RequestBody PageF<SearchF<?>> form) {
        return paymentApplicationFormService.queryKXDetails(form);
    }

    @PostMapping("/queryZFDetails")
    @ApiOperation(value = "支付申请单-支付明细", notes = "支付申请单-支付明细")
    public PageV<PaymentApplicationZFDetailV> queryZFDetails(@RequestBody PageF<SearchF<?>> form) {
        return paymentApplicationFormService.queryPaymentDetails(form);
    }

    @PostMapping("/paymentApplicationForm/approve")
    @ApiOperation(value = "支付申请单-审核 or 驳回", notes = "支付申请单-审核 or 驳回")
    public Boolean approve(@Validated @RequestBody ApproveOrRejectF approveOrRejectF) {
        paymentApplicationFormService.approve(approveOrRejectF);
        return true;
    }


    @PostMapping("/paymentApplicationForm/financialPreliminaryReview")
    @ApiOperation(value = "支付申请单-提交财务初审批", notes = "支付申请单-提交财务初审批")
    public Long financialPreliminaryReview(@Validated @RequestBody FinancialPreliminaryReviewF req) {
        return paymentApplicationFormService.financialPreliminaryReview(req);
    }

    @PostMapping("/paymentApplicationForm/initiateTheProcess")
    @ApiOperation(value = "支付申请单-发起流程", notes = "支付申请单-发起流程")
    public String initiateTheProcess(@RequestParam String payApplicationFormId) {
        return paymentApplicationFormService.initiateTheProcess(payApplicationFormId);
    }


    @PostMapping("/paymentApplicationForm/basic")
    @ApiOperation(value = "支付申请创建-基本信息", notes = "支付申请创建-基本信息")
    public PaymentApplicationBasicV queryBasic(@Validated @RequestBody AppBasicF appBasicF) {
        return paymentApplicationFormService.queryBasic(appBasicF);
    }

    @PostMapping("/sync/mock")
    public void syncMock(@RequestBody List<Mdm63CertainF> generateF) {
        mdm63Handler.sync(generateF);
    }

    @PostMapping("/sync2/mock")
    public void sync2Mock(@RequestParam("start") String start) {
        mdm63Handler.sync2(start);
    }

    @PostMapping("/sync3/mock")
    public void sync3Mock(@RequestParam("partnerId") String partnerId) {
        mdm63Handler.sync3(partnerId);
    }

    @PostMapping("/remind/mock")
    public void remindMock() {
        FirstExamineMessageF messageF = FirstExamineMessageF.builder().communityId("67ccf0a97607cb68f6461c598a447516").reason("测试原因").build();
        PaymentApplicationFormZJ paymentApplicationFormZJ = new PaymentApplicationFormZJ();
        paymentApplicationFormZJ.setCommunityId("67ccf0a97607cb68f6461c598a447516");
        paymentApplicationFormZJ.setTenantId("13554968497211");
        remindMessageConfigService.send(messageF, false, paymentApplicationFormZJ);
    }

    @PostMapping("/paymentApplicationForm/getPaymentByBizId")
    @ApiOperation(value = "根据工作台bizId获取支付申请单详情")
    public PaymentApplicationFormZJV getPaymentByBizId(@RequestParam String bizId) {
        return paymentApplicationFormService.getPaymentByBizId(bizId);
    }

    @ApiOperation(value = "更改对下实签来自合同数据", notes = "更改对下实签来自合同数据")
    @PostMapping("/updateBilDxZjFromContract")
    public void updateBilDxZjFromContract (
                          @RequestParam("settlementId")String settlementId,
                          @RequestParam("otherBusinessReasons") String otherBusinessReasons,
                          @RequestParam("externalDepartmentCode") String externalDepartmentCode,
                          @RequestParam("calculationMethod") Integer calculationMethod) {
        pushBillDxZJAppService.updateBilDxZjFromContract(settlementId, otherBusinessReasons, externalDepartmentCode, calculationMethod);
    }

    @GetMapping("/paymentApplicationForm/delete")
    @ApiOperation(value = "根据业务支付申请单ID删除对应业务支付申请数据")
    public Boolean deletePaymentById (@Validated @RequestParam Long id) {
        return paymentApplicationFormService.deletePaymentById(id);
    }

}
