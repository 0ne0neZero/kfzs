package com.wishare.finance.infrastructure.remote.clients.base;

import javax.validation.constraints.NotBlank;

import com.wishare.finance.domains.refund.*;
import com.wishare.finance.infrastructure.remote.vo.payment.DeductionBillF;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailV;
import com.wishare.finance.apps.model.bill.fo.ApproveReceivableBillF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ApproveTemporaryBillF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.RemissionManagementDTO;
import com.wishare.finance.apps.model.configure.chargeitem.vo.RemissionManagementDetailDTO;
import com.wishare.finance.infrastructure.remote.vo.charge.ApproveFilter;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;

@OpenFeignClient(name = "wishare-charge", serverName = "charge服务", path = "/charge")
public interface ChargeClient {

    @PostMapping(value = "/ticketRuleApi/update/receipt/template", name = "更新票据模板信息")
    void updateReceiptTemplate(@RequestParam("receiptTemplateId") Long receiptTemplateId, @RequestParam("receiptTemplateName") String receiptTemplateName);

    @GetMapping(value = "/ticketRuleApi/receipt/template/count", name = "获取票据模板的绑定规则数量")
    Long countByReceiptTemplateId(@RequestParam("receiptTemplateId") Long receiptTemplateId);

    @PostMapping("/callback/newCallBack")
    @ApiOperation(value = "重新接收回调信息api", notes = "重新接收回调信息api")
    boolean callBack(@Validated @RequestBody PaymentOrderDetailV orderDetailV,
                     @RequestParam("tenantId") @NotBlank(message = "租户ID不能为空") String tenantId);

    @GetMapping(value = "/approve/operatePassageRuleConfig", name = "获取报账单审批规则")
    ApproveFilter getApprovePushBillFilter(@RequestParam("supCpUnitId") String supCpUnitId, @RequestParam("operateType") Integer operateType);

    /**
     * @param comms k-communityId v-businessUnitId(业务单元id)
     * @return
     */
    @PostMapping("/chargeStandardBase/getBusinessUnitsByCommunityId")
    Map<String, List<Long>> getBusinessUnitsByCommunityId(@RequestBody Set<String> comms);

    @PostMapping("/tempDepositApi/deduction/deductionApplyList")
    @ApiOperation(value = "生成对应减免记录", notes = "生成对应减免记录")
    boolean deductionApplyList(@Validated @RequestBody List<DeductionBillF> deductionBillFList);

    @PostMapping("/refund/syncRefundManageStatus")
    void syncRefundManageStatus(@RequestBody RefundManageDTO refundManageDTO);

    @ApiOperation(value = "退款OA审批流回调")
    @PostMapping("/refund/refundManageCallBack")
    void refundManageCallBack(@RequestBody RefundManageDTO dto);

    @ApiOperation(value = "编辑业务支付申请单同步")
    @PostMapping("/refund/syncPaymentApplicationForm")
    void syncPaymentApplicationForm(@RequestBody RefundManageDetailDTO detailDTO);

    @ApiModelProperty(value = "删除退款管理")
    @PostMapping("/refund/removeRefundManage")
    void removeRefundManage(@RequestBody RefundManageDTO dto);



    /**
     * 单个应收账单审核
     */
    @PostMapping("/receivable/approve")
    Boolean approveChangeBatch(@RequestBody @Validated ApproveReceivableBillF approveReceivableBillF);

    @GetMapping("/receivable/getRemissionManagementDetail")
    List<RemissionManagementDetailDTO> getRemissionManagementDetail(@RequestParam String id);

    @PostMapping("/receivable/updateRemissionManage")
    boolean updateRemissionManage(@RequestBody RemissionManagementDTO dto);

    /**
     * 临时账单审核
     */
    @PostMapping("/tempDepositApi/approve")
    Boolean approveBatch(@Validated @RequestBody ApproveTemporaryBillF approveTemporaryBillF);

    @ApiModelProperty(value = "退款明细")
    @PostMapping("/refund/queryRefundManageDetail")
    List<RefundManagementDetailDTO> queryRefundManageDetail(@RequestBody RefundManagementDetailDTO detailDTO);

    @ApiModelProperty(value = "退款明细")
    @PostMapping("/ticketRuleApi/queryGatherDetailIds")
    ChargeTicketRuleQueryTypeV queryGatherDetailIds(@RequestBody ChargeTicketRuleTypeQueryF form);
}
