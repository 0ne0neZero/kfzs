package com.wishare.finance.domains.bill.service;

import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.vo.ApplyBatchDeductionV;
import com.wishare.finance.apps.model.bill.vo.BillCarryoverV;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillPageInfoV;
import com.wishare.finance.apps.model.bill.vo.BillSimpleInfoV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BatchSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.remote.enums.BillSettleStateEnum;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 账单领域服务
 *
 * @Author dxclay
 * @Date 2022/10/14
 * @Version 1.0
 */
@Slf4j
@Service
@Deprecated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillDomainServiceOld {

    private final BillFacade billFacade;

    /**
     * 获取账单详细信息
     *
     * @param billId 账单id
     * @param billType 账单类型
     * @return BillDetailMoreV
     */
    public BillDetailMoreV getAllDetail(Long billId, Integer billType,String supCpUnitId) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(billType);
        BillDetailMoreV allDetail = billFacade.getAllDetail(billId, billTypeEnum,supCpUnitId);
        if (Objects.nonNull(allDetail)) {
            List<BillCarryoverV> carryoverList = allDetail.getBillCarryoverDtos();
            if (CollectionUtils.isNotEmpty(carryoverList)) {
                //设置结转的目标账单
                if (CollectionUtils.isNotEmpty(carryoverList)) {
                    for (BillCarryoverV billCarryoverV : carryoverList) {
                        String targetBillNoStr = billCarryoverV.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillNo).filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                        if (StringUtils.isNotEmpty(targetBillNoStr)) {
                            billCarryoverV.setTargetBillNo(targetBillNoStr);
                        }
                    }
                }
            }
        }
        return allDetail;
    }

    /**
     * 反交账
     * @param billId
     * @param billType
     * @return
     */
    public boolean handReversal(Long billId, BillTypeEnum billType, String supCpUnitId){
        return billFacade.handReversal(billId, billType, supCpUnitId);
    }

    /**
     * 批量结算
     *
     * @param command
     * @param identityInfo
     * @return
     */
    public Boolean batchSettle(BatchSettleCommand command, IdentityInfo identityInfo) {
        //获取账单信息
        List<BillDetailMoreV> detailMoreVList = billFacade.getAlldetailList(command.getBillIds(), BillTypeEnum.valueOfByCode(command.getBillType()),command.getSupCpUnitId());
        if (CollectionUtils.isEmpty(detailMoreVList)) {
            throw BizException.throw400(ErrMsgEnum.BILL_NOT_FOUND.getErrMsg());
        }
        List<AddBillSettleF> addBillSettleRFList = command.generalBillSettleRf(detailMoreVList,identityInfo);
        return billFacade.batchSettle(addBillSettleRFList,BillTypeEnum.valueOfByCode(command.getBillType()));
    }

    public BillSimpleInfoV getBillSimpleInfoV(List<Long> billIds, Integer billType,
            String supCpUnitId) {
        return getBillSimpleInfoV(billIds, billType,supCpUnitId, null);
    }

    /**
     * 获取账单简约信息
     * 可开票金额 = （总应收金额-已开票金额）
     * 处理：统计【可开票金额】
     * @param billIds
     * @param billType
     * @return
     */
    public BillSimpleInfoV getBillSimpleInfoV(List<Long> billIds, Integer billType,
            String supCpUnitId, Integer invoiceType) {
        //账单list以及账单关联信息
        List<BillDetailMoreV> billInfos = billFacade.getAlldetailList(billIds, BillTypeEnum.valueOfByCode(billType),supCpUnitId);
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            // 中交预收账单不允许开发票只允许开收据，开票金额过滤
            if (Objects.nonNull(invoiceType)) {
                switch (InvoiceLineEnum.valueOfByCode(invoiceType)) {
                    case 纸质收据:
                    case 电子收据:
                    case 收据:
                        break;
                    default:
                        billInfos = billInfos.stream()
                                .filter(billInfo -> !BillTypeEnum.预收账单.equalsByCode(
                                Integer.parseInt(billInfo.getBillType()))).collect(
                                Collectors.toList());
                        break;
                }
            }
        }
        Long canInvoiceAmount = 0L;
        //单账单处理
        if (CollectionUtils.isNotEmpty(billInfos) && billInfos.size() == 1) {
            BillDetailMoreV billDetailMoreV = billInfos.get(0);
                canInvoiceAmount = billDetailMoreV.getCanInvoiceAmount();
        }else {
            //多账单处理
            for (BillDetailMoreV billInfo : billInfos) {
                canInvoiceAmount = canInvoiceAmount + billInfo.getCanInvoiceAmount();
            }
        }
        BillSimpleInfoV billSimpleInfoV = new BillSimpleInfoV();
        //可开票金额
        billSimpleInfoV.setCanInvoiceAmount(canInvoiceAmount);
        //账单数量
        billSimpleInfoV.setBillNum(billIds.size());
        billSimpleInfoV.setReceivableAmountSum(billInfos.stream().mapToLong(e-> e.getReceivableAmount() - e.getDiscountAmount()).sum());
        billSimpleInfoV.setActualPayAmountSum(billInfos.stream().mapToLong(BillDetailMoreV::getActualPayAmount).sum());
        billSimpleInfoV.setSupCpUnitId(billInfos.get(0).getSupCpUnitId());
        return billSimpleInfoV;
    }

    /**
     * 发起审核申请
     *
     * @param billApplyF 申请命令
     * @param billType 账单类型
     * @return Boolean
     */
    public Boolean apply(BillApplyF billApplyF, Integer billType) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(billType);
        return billFacade.apply(billApplyF, billTypeEnum);
    }

    /**
     * 账单批量申请
     *
     * @param command 批量申请命令
     * @param billType 账单类型
     * @return Boolean
     */
    public Boolean applyBatch(BillApplyBatchF command, Integer billType) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(billType);
        return billFacade.applyBatch(command, billTypeEnum);
    }

    /**
     * 分页查询账单
     *
     * @param queryF
     * @return
     */
    public PageV<BillPageInfoV> getPage(PageF<SearchF<?>> queryF) {
        int billType;
        List<Field> fields = queryF.getConditions().getFields();
        List<Field> billTypeField = fields.stream().filter(s -> "billType".equals(s.getName())).collect(Collectors.toList());
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        //账单类型
        if(CollectionUtils.isNotEmpty(billTypeField)){
            billType = (int) billTypeField.get(0).getValue();
            fields.removeAll(billTypeField);
        }else{
            billType = 0;
        }
        //无效账单还是正常账单
        if(CollectionUtils.isEmpty(billInvalidField) || (int)billInvalidField.get(0).getValue() == 0){
            //正常账单条件
            fields.add(new Field("b.state", BillStateEnum.正常.getCode(),1));
            fields.add(new Field("b.reversed", BillReverseStateEnum.已冲销.getCode(),2));
            fields.add(new Field("b.carried_state", BillCarryoverStateEnum.已结转.getCode(),2));
            fields.add(new Field("b.refund_state", BillRefundStateEnum.已退款.getCode(),2));
            fields.add(new Field("b.bill_label", BillLabelEnum.冲销标识.getCode(),11));
            fields.removeAll(billInvalidField);
        }

        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(billType);
        return billFacade.getPage(queryF, billTypeEnum);
    }

    /**
     * 账单批量申请
     *
     * @param command 批量申请命令
     * @param billType 账单类型
     * @return Boolean
     */
    public ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchF command, Integer billType) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(billType);
        return billFacade.applyBatchDeduction(command, billTypeEnum);
    }
}
