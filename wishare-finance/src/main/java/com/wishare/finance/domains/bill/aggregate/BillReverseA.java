package com.wishare.finance.domains.bill.aggregate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.apps.model.reconciliation.vo.FlowClaimDetailV;
import com.wishare.finance.apps.service.reconciliation.FlowClaimAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description:
 */
@Getter
@Setter
public class BillReverseA<B extends Bill> extends BillReverseE {

    /**
     * 账单信息
     */
    public B bill;

    /**
     * 冲销创建的新账单
     */
    public B reverseBill;

    /**
     * 收款单
     */
    public GatherBill gatherBill;

    /**
     * 收款明细
     */
    public GatherDetail gatherDetail;

    public BillReverseA() {
    }

    public BillReverseA(B bill) {
        this.bill = bill;
    }

    /**
     * 冲销
     *
     * @return
     */
    public boolean reverse(){
        checkReverse();
        //1.冲销原账单
        bill.reverse();
        // 将账单关联的已收款单状态置为无效
        invalidGatherInfos();

        // 关联的账单减免记录删除
        BillAdjustRepository adjustRepository = Global.ac.getBean(BillAdjustRepository.class);
        adjustRepository.update(new UpdateWrapper<BillAdjustE>().eq("bill_id",bill.getId()).set("deleted",1));
        // 关联结转明细置为无效
        BillCarryoverRepository billCarryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        BillCarryoverDetailRepository billCarryoverDetailRepository = Global.ac.getBean(BillCarryoverDetailRepository.class);
        billCarryoverRepository.update(new UpdateWrapper<BillCarryoverE>().set("reversed",Const.State._1).eq("carried_bill_id",bill.getId()));
        billCarryoverDetailRepository.update(new UpdateWrapper<BillCarryoverDetailE>().set("reversed",Const.State._1).eq("carried_bill_id",bill.getId()));
        //2.生成新的冲销账单(追加标识字段)
        B b = Global.mapperFacade.map(bill, (Class<B>)bill.getClass());
        b.setId(null);
        b.setBillNo(null);
        b.init();
        b.resetState();
        b.approve(BillApproveStateEnum.已审核);
        b.resetAmount(-bill.getActualSettleAmount());
        reverseBill = b;
        reverseBill.setBillLabel(BillLabelEnum.冲销标识.getCode());
        reverseBill.setPayeeId(bill.getPayeeId());
        reverseBill.setPayeeName(bill.getPayeeName());
        reverseBill.setPayerId(bill.getPayerId());
        reverseBill.setPayerName(bill.getPayerName());
        reverseBill.setReversed(BillReverseStateEnum.未冲销.getCode());
        reverseBill.setSettleState(BillSettleStateEnum.已结算.getCode());
        reverseBill.setTotalAmount(bill.getTotalAmount());
        reverseBill.setReceivableAmount(bill.getReceivableAmount());
        reverseBill.setSettleAmount(-handleActualPayAmount(bill));
        reverseBill.setGmtCreate(LocalDateTime.now());
        reverseBill.setGmtModify(LocalDateTime.now());
        if (reverseBill instanceof ReceivableBill) {
            ((ReceivableBill)reverseBill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(reverseBill.getRoomId()));
        }else if (reverseBill instanceof TemporaryChargeBill) {
            ((TemporaryChargeBill)reverseBill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(reverseBill.getRoomId()));
        }else if (reverseBill instanceof AdvanceBill){
            ((AdvanceBill)reverseBill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(reverseBill.getRoomId()));
        }
//        //3.生成对应的收款单
//        gatherBill = generalGatherBill(bill.getActualSettleAmount());
//        //4.生成对应的收款明细
//        gatherDetail = generalGatherBillDetail(gatherBill,reverseBill);


        //4.构造冲销明细
        creatBillReverse();
        return true;
    }

    /**
     * 将账单名下的收款单和明细进行删除
     */
    private void invalidGatherInfos() {
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bill.getId(), bill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().eq("rec_bill_no",bill.getBillNo()).set("available",1)
                    .eq("sup_cp_unit_id",bill.getSupCpUnitId()));
            List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).distinct().collect(Collectors.toList());
            List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds, bill.getSupCpUnitId());
            for (GatherBill gatherBill1 : gatherBillList) {
                List<GatherDetail> gatherDetail1s = gatherDetailRepository.getByGatherBillId(gatherBill1.getId(), gatherBill1.getSupCpUnitId());
                if (CollectionUtils.isEmpty(gatherDetail1s)){
                    gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",gatherBill1.getId()).set("reversed",1)
                            .eq("sup_cp_unit_id",gatherBill1.getSupCpUnitId()));
                }
            }
        }
    }


    /**
     * 获取实际缴费金额
     *
     * @param bill
     * @return
     */
    private Long handleActualPayAmount(B bill) {
        return bill.getSettleAmount() - bill.getRefundAmount() - bill.getCarriedAmount();
    }

    /**
     * 构建收款单明细
     *
     * @return
     */
    private GatherDetail generalGatherBillDetail(GatherBill gatherBill, B reverseBill) {
        GatherDetail detail = new GatherDetail();
        detail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_DETAIL));
        detail.setGatherType(GatherDetail.getGatherType(bill).getCode());
        detail.setGatherBillId(gatherBill.getId());
        detail.setGatherBillNo(gatherBill.getBillNo());
        detail.setRecBillId(reverseBill.getId());
        detail.setRecBillNo(reverseBill.getBillNo());
        detail.setCostCenterId(bill.getCostCenterId() );
        detail.setCostCenterName(bill.getCostCenterName());
        detail.setChargeItemId(bill.getChargeItemId());
        detail.setChargeItemName(bill.getChargeItemName());
        detail.setSupCpUnitId(bill.getCommunityId());
        detail.setSupCpUnitName(bill.getCommunityName());
        detail.setCpUnitId(bill.getRoomId());
        detail.setCpUnitName(bill.getRoomName());
        detail.setPayChannel(SettleChannelEnum.其他.getCode());
        detail.setPayWay(SettleWayEnum.线上.getCode());
        detail.setRecPayAmount(bill.getSettleAmount());
        detail.setPayAmount(bill.getSettleAmount());
        detail.setPayerType(bill.getPayerType());
        detail.setPayerId(bill.getPayerId());
        detail.setPayerName(bill.getPayerName());
        detail.setPayeeId(bill.getPayeeId());
        detail.setPayeeName(bill.getPayeeName());
        detail.setPayTime(LocalDateTime.now());

        return detail;
    }

    /**
     * 构建收款单
     *
     * @return
     */
    private GatherBill generalGatherBill(Long actualSettleAmount) {
        GatherBill gatherBill = Global.mapperFacade.map(this.getReverseBill(), GatherBill.class);
        gatherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        gatherBill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        gatherBill.setPayChannel(SettleChannelEnum.其他.getCode());
        gatherBill.setPayWay(SettleWayEnum.线上.getCode());
        gatherBill.setCreator(reverseBill.getCreator());
        gatherBill.setCreatorName(reverseBill.getCreatorName());
        gatherBill.setGmtCreate(LocalDateTime.now());
        gatherBill.setOperator(reverseBill.getOperator());
        gatherBill.setOperatorName(reverseBill.getOperatorName());
        gatherBill.setGmtModify(LocalDateTime.now());
        gatherBill.setTotalAmount(actualSettleAmount);
        gatherBill.setPayTime(LocalDateTime.now());
        if (bill instanceof ReceivableBill) {
            gatherBill.setStartTime(((ReceivableBill) bill).getStartTime());
            gatherBill.setEndTime(((ReceivableBill) bill).getEndTime());
        }
        return gatherBill;
    }

    /**
     * 构造重新明细
     */
    private void creatBillReverse() {
        this.setBillId(bill.getId());
        this.setBillType(bill.getType());
        this.setTotalAmount(bill.getActualSettleAmount());
        this.setCommunityId(bill.getCommunityId());
        this.setReverseBillId(reverseBill.getId());
        this.setReverseAmount(reverseBill.getActualSettleAmount());
    }


    /**
     * 校检冲销
     */
    private void checkReverse() {
        // 针对已审核、已结算(部分结算)的非线上支付的账单进行冲销
        ErrorAssertUtil.isTrueThrow402(bill.getApprovedState() == BillApproveStateEnum.已审核.getCode(), ErrorMessage.BILL_IS_OPERATING, BillApproveStateEnum.已审核.getValue());
        // 已开票或 已结算 或部分结算  的账单才可冲销
        if (bill.getSettleState().equals(BillSettleStateEnum.未结算.getCode()) && !bill.getInvoiceState().equals(InvoiceStateEnum.已开票.getCode())){
            throw BizException.throw400("已开票或已结算或部分结算的账单才可冲销");
        }
        // 存在已认领流水的收款单的账单不可冲销
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getListByRecBillId(bill.getId(), bill.getSupCpUnitId());
        if (CollectionUtils.isEmpty(detailList)){return;}
        List<Long> gatherIds = detailList.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
        List<FlowClaimDetailV> recGatherIdFlowClaimRecord = Global.ac.getBean(FlowClaimAppService.class).getRecGatherIdFlowClaimRecord(gatherIds,bill.getSupCpUnitId());
        Assert.validate(() -> CollectionUtils.isEmpty(recGatherIdFlowClaimRecord), ()->BizException.throw400("无法冲销，账单下存在收款单已认领流水。请您先解除认领"));
    }

}
