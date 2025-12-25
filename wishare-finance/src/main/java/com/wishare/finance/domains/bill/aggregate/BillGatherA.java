package com.wishare.finance.domains.bill.aggregate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.invoicereceipt.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@Slf4j
public class BillGatherA<B extends Bill> {

    /**
     * 收款单
     */
    private GatherBill gatherBill;

    /**
     * 收款详细信息
     */
    private List<BillGatherDetailA<B>> gatherDetailAS;

    public BillGatherA() {
    }

    public BillGatherA(GatherBill gatherBill, List<BillGatherDetailA<B>> gatherDetailAS) {
        this.gatherBill = gatherBill;
        this.gatherDetailAS = gatherDetailAS;
    }

    public List<B> getBillList() {
        if (CollectionUtils.isEmpty(this.gatherDetailAS)) {
            return Lists.newArrayList();
        }
      return this.gatherDetailAS.stream().map(BillGatherDetailA::getBill).collect(Collectors.toList());
    }

    public List<GatherDetail> getGatherDetails() {
        if (CollectionUtils.isEmpty(this.gatherDetailAS)) {
            return Lists.newArrayList();
        }
        return this.gatherDetailAS.stream().map(BillGatherDetailA::getGatherDetail).collect(Collectors.toList());
    }


    /**
     *
     * @param billList
     * @param commands
     */
    public BillGatherA(List<B> billList, List<AddBillSettleCommand> commands) {
        log.info("构造收款单实体类,账单数据：{}",JSON.toJSONString(billList));
        AddBillSettleCommand command = commands.get(0);
        B bill = billList.get(0);

        gatherBill = new GatherBill();
        gatherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_BILL));
        gatherBill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        gatherBill.setStatutoryBodyId(bill.getStatutoryBodyId());
        gatherBill.setStatutoryBodyName(bill.getStatutoryBodyName());
        gatherBill.setSbAccountId(bill.getSbAccountId());
        gatherBill.setRemark(command.getRemark());
        gatherBill.setBankFlowNo(command.getBankFlowNo());
        gatherBill.setSupCpUnitId(bill.getSupCpUnitId());
        gatherBill.setSupCpUnitName(bill.getSupCpUnitName());
        LocalDateTime minStartTime = null;
        LocalDateTime maxEndTime = null;

        if (bill instanceof ReceivableBill) {
            List<ReceivableBill> receivableBillList = Global.mapperFacade.mapAsList(billList, ReceivableBill.class);
            if (CollectionUtils.isNotEmpty(receivableBillList)) {
                maxEndTime =  receivableBillList.stream()
                        .map(ReceivableBill::getEndTime)
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo).orElse(null);
                minStartTime = receivableBillList.stream()
                        .map(ReceivableBill::getStartTime)
                        .filter(Objects::nonNull)
                        .min(LocalDateTime::compareTo).orElse(null);
            }
        }

        if (bill instanceof TemporaryChargeBill) {
            List<TemporaryChargeBill> temporaryBillList = Global.mapperFacade.mapAsList(billList, TemporaryChargeBill.class);
            if (CollectionUtils.isNotEmpty(temporaryBillList)) {
                maxEndTime =  temporaryBillList.stream()
                        .map(TemporaryChargeBill::getEndTime)
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo).orElse(null);
                minStartTime = temporaryBillList.stream()
                        .map(TemporaryChargeBill::getStartTime)
                        .filter(Objects::nonNull)
                        .min(LocalDateTime::compareTo).orElse(null);
            }
        }

        gatherBill.setStartTime(minStartTime);
        gatherBill.setEndTime(maxEndTime);
        gatherBill.setPayTime(Optional.ofNullable(command.getSettleTime()).orElse(LocalDateTime.now()));
        /** 结算渠道 */
        gatherBill.setPayChannel(this.handlePayChannel(commands));
        /** 结算方式(0线上，1线下) */
        gatherBill.setPayWay(command.getSettleWay());
        gatherBill.setDiscounts(handleDiscounts(commands));
        gatherBill.setDescription(handleDescription(commands));
        gatherBill.setTotalAmount(handleTotalAmount(commands));
        gatherBill.setPayeeId(command.getPayeeId());
        gatherBill.setPayeeName(command.getPayeeName());
        gatherBill.setPayerId(command.getPayerId());
        gatherBill.setPayerName(command.getPayerName());
        gatherBill.setSysSource(bill.getSysSource());
        gatherBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        gatherBill.setDiscountAmount(handleDiscountAmount(commands));
        gatherBill.setTradeNo(command.getTradeNo());
        gatherBill.setPaySource(command.getPaySource());

        this.getInstance(gatherBill,billList,commands);
    }

    /**
     * 获取收款单开票状态
     * @param gatherBill
     * @param billList
     */
    private void getInstance(GatherBill gatherBill ,List<B> billList, List<AddBillSettleCommand> commands){
        boolean allIdsNotEmpty = commands.stream()
                .allMatch(command -> command.getBillId() == null);
        if(allIdsNotEmpty){
            return;
        }
        List<Integer> invoiceStates = billList.stream().map(B::getInvoiceState).distinct().collect(Collectors.toList());
        log.info("获取收款单开票状态：{}", JSONObject.toJSONString(invoiceStates));
        Map<Long, List<B>> billMap = billList.stream().collect(Collectors.groupingBy(B::getId));
        if(invoiceStates.size() > 1 && (invoiceStates.contains(BillInvoiceStateEnum.部分开票.getCode())
                && invoiceStates.contains(BillInvoiceStateEnum.已开票.getCode()))){
            gatherBill.setInvoiceState(BillInvoiceStateEnum.部分开票.getCode());
            // 需要将已开票的收款明细进行累加
        }else if(invoiceStates.size() == 1 && invoiceStates.contains(BillInvoiceStateEnum.已开票.getCode())){
            gatherBill.setInvoiceState(BillInvoiceStateEnum.已开票.getCode());
        }else {
            gatherBill.setInvoiceState(BillInvoiceStateEnum.未开票.getCode());
        }
        long invoiceAmount = 0L;
        // 根据开票状态分别计算
        for (AddBillSettleCommand command : commands) {
            // 暂时跳出未开票
            if (BillInvoiceStateEnum.未开票.getCode().equals(gatherBill.getInvoiceState())){
                continue;
            }
            B bill = billMap.get(command.getBillId()).get(0);
            invoiceAmount += bill.getCurrentSettleAmount(command.getSettleAmount());
            log.info("构建收款单实体-getInstance,计算invoiceAmount,初始值0，计算后：{}",invoiceAmount);
        }
        gatherBill.setInvoiceAmount(invoiceAmount);
        log.info("构建收款单实体-getInstance,计算invoiceAmount,初始值0，最终取值：{}",invoiceAmount);
    }


    /**
     * 求和开票金额
     *
     * @param billList
     * @return
     */
    private Long handleInvoiceAmount(List<B> billList) {
        Long invoiceAmount = 0L;
        for (B bill: billList) {
            if (bill.getInvoiceAmount() != null) {
                invoiceAmount += bill.getInvoiceAmount();
            }
        }
        return invoiceAmount;
    }


    /**
     * 求和优惠金额
     *
     * @param commands
     * @return
     */
    private Long handleDiscountAmount(List<AddBillSettleCommand> commands) {
        Long discountAmountSum = 0L;
        for (AddBillSettleCommand command : commands) {
            if (command.getDiscountAmount() != null) {
                discountAmountSum = discountAmountSum + command.getDiscountAmount();
            }
        }
        return discountAmountSum;
    }


    /**
     * 计算账单金额
     *
     * @param commands
     * @return
     */
    private Long handleTotalAmount(List<AddBillSettleCommand> commands) {
        Long settleAmountSum = 0L;
        for (AddBillSettleCommand command : commands) {
            if (command.getSettleAmount() != null) {
                settleAmountSum = settleAmountSum + command.getSettleAmount();
            }
        }
        return settleAmountSum;
    }

    /**
     * 处理账单描述
     *
     * @param commands
     * @return
     */
    private String handleDescription(List<AddBillSettleCommand> commands) {
        List<String> remarkStrs = Lists.newArrayList();

        for (AddBillSettleCommand command : commands) {
            remarkStrs.add(command.getRemark());
        }
        remarkStrs = remarkStrs.stream().distinct().collect(Collectors.toList());
        return StringUtils.join(remarkStrs, ",");
    }

    /**
     * 处理减免信息
     *
     * @param commands
     * @return
     */
    private List<DiscountOBV> handleDiscounts(List<AddBillSettleCommand> commands) {
        List<DiscountOBV> discountOBVList = Lists.newArrayList();
        for (AddBillSettleCommand command : commands) {
            if (StringUtils.isNotBlank(command.getDiscounts())) {
                List<DiscountOBV> discountOBVS = JSON.parseArray(command.getDiscounts(), DiscountOBV.class);
                if (CollectionUtils.isNotEmpty(discountOBVS)) {
                    discountOBVList.addAll(discountOBVS);
                }
            }
        }
        return discountOBVList;
    }

    /**
     * 处理支付渠道
     *
     * @param commands
     * @return
     */
    private String handlePayChannel(List<AddBillSettleCommand> commands) {
        if (CollectionUtils.isNotEmpty(commands)) {
            List<String> settleChannels = commands.stream().map(AddBillSettleCommand::getSettleChannel).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(settleChannels) && settleChannels.size() > 1) {
                return SettleChannelEnum.组合支付.getCode();
            }
            return settleChannels.get(0);
        }
        return null;
    }
}
