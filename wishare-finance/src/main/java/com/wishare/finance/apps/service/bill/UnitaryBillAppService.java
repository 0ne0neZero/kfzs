package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.BillPayF;
import com.wishare.finance.apps.model.bill.fo.DifferenceRefundF;
import com.wishare.finance.apps.model.bill.fo.InvoiceReverseF;
import com.wishare.finance.apps.model.bill.vo.BillTransactionV;
import com.wishare.finance.apps.service.bill.accounthand.AccountHandServiceFactory;
import com.wishare.finance.domains.bill.command.BillUnitaryEnterCommand;
import com.wishare.finance.domains.bill.command.RefundCommand;
import com.wishare.finance.domains.bill.command.UnitaryEnterBillCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTransactTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillUnitaryEnterResultDto;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.bill.service.AdvanceBillDomainService;
import com.wishare.finance.domains.bill.service.BillPaymentDomainService;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.bill.service.PayBillDomainService;
import com.wishare.finance.domains.bill.service.PayableBillDomainService;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.domains.bill.service.TemporaryChargeBillDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.event.EventTransactional;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一账单处理应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UnitaryBillAppService {

    private final PayBillDomainService payBillDomainService;
    private final GatherBillDomainService gatherBillDomainService;
    private final PayableBillDomainService payableBillDomainService;
    private final AdvanceBillDomainService advanceBillDomainService;
    private final ReceivableBillDomainService receivableBillDomainService;
    private final TemporaryChargeBillDomainService temporaryChargeBillDomainService;
    private final AccountHandServiceFactory accountHandServiceFactory;

    private final BillPaymentDomainService billPaymentDomainService;

    /**
     * 差额退款
     *
     * @param differenceRefundF 差额退款表单
     * @return 结果
     */
    public boolean refundDifference(DifferenceRefundF differenceRefundF) {
        BillTypeEnum billType = BillTypeEnum.valueOfByCode(differenceRefundF.getBillType());
        RefundCommand refundCommand = new RefundCommand();
        refundCommand.setBillId(differenceRefundF.getBillId());
        refundCommand.setRefunderId(ApiData.API.getUserId().get());
        refundCommand.setRefunderName(ApiData.API.getUserName().get());
        refundCommand.setRemark(differenceRefundF.getRefundReason());
        refundCommand.setRefundAmount(differenceRefundF.getRefundAmount());
        refundCommand.setSupCpUnitId(differenceRefundF.getSupCpUnitId());
        boolean res = false;
        switch (billType) {
            case 应收账单:
            case 临时收费账单:
                res = receivableBillDomainService.refund(refundCommand);
                break;
            case 预收账单:
                res = advanceBillDomainService.refund(refundCommand);
                break;
        }
        return res;
    }

    /**
     * 账单票据红冲
     *
     * @param invoiceReverseF 红冲表单
     * @return 结果
     */
    public Boolean reverseInvoice(InvoiceReverseF invoiceReverseF) {
        return accountHandServiceFactory.getAccountHandService(invoiceReverseF.getBillType()).invoiceReversal(invoiceReverseF.getBillId());
    }

    @Transactional
    public List<BillUnitaryEnterResultDto> enter(List<BillUnitaryEnterCommand> billUnitaryEnterFS, String supCpUnitId) {
        //将账单进行分组
        Map<Integer, List<BillUnitaryEnterCommand>> billGroup = billUnitaryEnterFS.stream().collect(Collectors.groupingBy(BillUnitaryEnterCommand::getBillType));
        //校验账单类型
        List<BillTypeEnum> billTypes = billGroup.keySet().stream().map(BillTypeEnum::valueOfByCode).collect(Collectors.toList());
        for (BillTypeEnum billType : billTypes) {
            switch (billType){
                case 预收账单:
                    return advanceBillDomainService.enterBatch(billGroup.get(billType.getCode()), supCpUnitId);
                case 应收账单:
                case 临时收费账单:
                    return receivableBillDomainService.enterBatch(billGroup.get(billType.getCode()), supCpUnitId);
                case 应付账单:
                    return payableBillDomainService.enterBatch(billGroup.get(billType.getCode()), supCpUnitId);
                case 付款单:
                    return payBillDomainService.enterBatch(billGroup.get(billType.getCode()), supCpUnitId);
                case 收款单:
                    return gatherBillDomainService.enterBatch(billGroup.get(billType.getCode()), supCpUnitId);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 统一付款
     *
     * @param billPayF
     * @return
     */
    @Transactional
    @EventTransactional
    public BillTransactionV pay(BillPayF billPayF) {
        SettleChannelEnum settleChannelEnum = SettleChannelEnum.valueOfByCode(billPayF.getPayChannel());
        ErrorAssertUtil.isTrueThrow403(SettleChannelEnum.招商银企直连 == settleChannelEnum, ErrorMessage.PAYMENT_CHANNEL_NOT_SUPPORT, settleChannelEnum.getCode());
        UnitaryEnterBillCommand billInfo = billPayF.getBillInfo();
        ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(billInfo.getStatutoryBodyId()) || StringUtils.isNotBlank(billInfo.getStatutoryBodyCode()), ErrorMessage.PAYMENT_SB_NOT_NULL);
        ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(billInfo.getCostCenterId()) || StringUtils.isNotBlank(billInfo.getCostCenterCode()), ErrorMessage.PAYMENT_COST_NOT_NULL);
        ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(billInfo.getChargeItemId()) || StringUtils.isNotBlank(billInfo.getChargeItemCode()), ErrorMessage.PAYMENT_CHARGE_ITEM_NOT_NULL);
        //支付信息
        BillTransactionV billTransactionV = new BillTransactionV();
        if (SettleWayEnum.线上.equalsByCode(billPayF.getPayWay()) && SettleChannelEnum.招商银企直连.equalsByCode(billPayF.getPayChannel())) {
            //发起支付
            TransactionOrder transactionOrder = Global.mapperFacade.map(billPayF, TransactionOrder.class);
            transactionOrder.setTransactionType(BillTransactTypeEnum.付款.getCode());
            //transactionOrder.setBillParam(JSON.toJSONString(billInfo));
            billPaymentDomainService.transact(transactionOrder);
            Global.mapperFacade.map(transactionOrder, billTransactionV);
        }else {
            billTransactionV.setBizTransactionNo(billPayF.getBizTransactionNo());
            billTransactionV.setErrCode("10001001");
            billTransactionV.setErrMsg("暂不支持支付方式=[" + billPayF.getPayWay() + "]且支付渠道[" + billPayF.getPayChannel() + "] 的交易");
        }
        return billTransactionV;
    }
}
