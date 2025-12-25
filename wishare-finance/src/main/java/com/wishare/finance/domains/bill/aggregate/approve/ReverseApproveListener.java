package com.wishare.finance.domains.bill.aggregate.approve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.BillReverseA;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.service.BillDomainServiceImpl;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.REVERSE;


/**
 * @author xujian
 * @date 2022/11/6
 * @Description:
 */
public class ReverseApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    /**
     * 冲销后生成待审核，未结算的账单
     */
    private final String ReversedInitBill = "ReversedInitBill";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onAgree(B bill, BillApproveE billApprove) {
        IdempotentUtil.setIdempotent(String.valueOf(bill.getId()), REVERSE, 5, ErrorMessage.BILL_REVERSE_REPEAT);
        BillReverseA<B> billReverseA = new BillReverseA<B>(bill);
        ErrorAssertUtil.isTrueThrow402(bill.getMcReconcileState()!=ReconcileResultEnum.已核对.getCode(), ErrorMessage.BILL_APPLY_DEDUCTION_ERROR);
        ErrorAssertUtil.isTrueThrow402(bill.getReconcileState()!=ReconcileResultEnum.已核对.getCode(), ErrorMessage.BILL_APPLY_DEDUCTION_ERROR);
        billReverseA.reverse();
        // 如果存在结转其他账单的金额，金额自动回退原账单，且更新原账单的结转状态以及实收金额，
        if (bill.getType() != BillTypeEnum.预收账单.getCode()) {
            BillDomainServiceImpl billDomainService = Global.ac.getBean("billDomainService", BillDomainServiceImpl.class);
            billDomainService.reverseCarryoverBill(bill,null,null);
        }
        B reverseBill = billReverseA.getReverseBill();
        BillReverseRepository billReverseRepository = Global.ac.getBean(BillReverseRepository.class);
        BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(bill.getType()))
            .update(bill, new QueryWrapper<>().eq("id",bill.getId()).eq("sup_cp_unit_id", bill.getSupCpUnitId()));
        BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(bill.getType())).save(reverseBill);

//        GatherBill gatherBill = billReverseA.getGatherBill();
//        Global.ac.getBean(GatherBillRepository.class).save(gatherBill);
//        Global.ac.getBean(GatherDetailRepository.class).save(billReverseA.getGatherDetail());
        billReverseRepository.save(billReverseA);

        //冲销后生成已审核，未结算的应收账单
        if (billApprove.getExtField1().equals(ReversedInitBill)) {
            reversedInitBillNew(bill);
        }
        bill.offAccount();

        //原账单日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单冲销", true)))
                        .option(new ContentOption(new PlainTextDataItem("冲销原因：" + billApprove.getReason(), true))));

        //冲销单日志记录
        BizLog.normal(String.valueOf(reverseBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.创建,
                new Content().option(new ContentOption(new PlainTextDataItem("冲销生成", true))));

//        //冲销账单相反的账日志记录
//        BizLog.normal(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.创建,
//                new Content().option(new ContentOption(new PlainTextDataItem("冲销生成", true))));
    }

    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
    }

    /**
     * 冲销后生成已审核，未结算的应收账单
     *
     * @param bill
     */
    private void reversedInitBillNew(B bill) {
        Long billId = null;
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = Global.mapperFacade.map(bill, ReceivableBill.class);
            receivableBill.setId(null);
            receivableBill.setBillNo(null);
            receivableBill.setPayTime(null);
            receivableBill.setPayInfos(null);
            receivableBill.init();
            receivableBill.resetState();
            receivableBill.resetAmount(bill.getTotalAmount());
            receivableBill.resetOperatorInfo();
            receivableBill.setApprovedState(BillApproveStateEnum.已审核.getCode());//冲销后生成已审核，未结算的应收账单
            receivableBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
            ReceivableBillRepository receivableBillRepository = Global.ac.getBean(
                ReceivableBillRepository.class);
            receivableBill.setCostType();
            receivableBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(receivableBill.getRoomId()));
            receivableBillRepository.save(receivableBill);
            billId = receivableBill.getId();
        } else if (bill instanceof AdvanceBill) {
            AdvanceBill advanceBill = Global.mapperFacade.map(bill, AdvanceBill.class);
            advanceBill.setId(null);
            advanceBill.setBillNo(null);
            advanceBill.init();
            advanceBill.resetState();
            advanceBill.resetAmount(bill.getTotalAmount());
            advanceBill.resetOperatorInfo();
            advanceBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
            AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
            advanceBill.setCostType();
            advanceBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(advanceBill.getRoomId()));
            advanceBillRepository.save(advanceBill);
            billId = advanceBill.getId();
        } else if (bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill temporaryChargeBill = Global.mapperFacade.map(bill, TemporaryChargeBill.class);
            temporaryChargeBill.setId(null);
            temporaryChargeBill.setBillNo(null);
            temporaryChargeBill.setPayTime(null);
            temporaryChargeBill.setPayInfos(null);
            temporaryChargeBill.init();
            temporaryChargeBill.resetState();
            temporaryChargeBill.resetAmount(bill.getTotalAmount());
            temporaryChargeBill.resetOperatorInfo();
            temporaryChargeBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
            temporaryChargeBill.setApprovedState(BillApproveStateEnum.已审核.getCode());//冲销后生成已审核，未结算的应收账单
            TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(TemporaryChargeBillRepository.class);
            temporaryChargeBill.setCostType();
            temporaryChargeBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(temporaryChargeBill.getRoomId()));
            temporaryChargeBillRepository.save(temporaryChargeBill);
            billId = temporaryChargeBill.getId();
        }

        if (Objects.nonNull(billId)){
            //日志记录
            BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("冲销后生成待审核，未结算的账单", true))));
        }

    }


}
