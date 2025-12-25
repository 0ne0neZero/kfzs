package com.wishare.finance.domains.bill.dto;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("结算详情")
public class SettleDetailDto {

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("房号id")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("账单来源")
    private String source;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额")
    private Long deductibleAmount;

    @ApiModelProperty("实际收减免金额")
    private Long discountAmount;

    @ApiModelProperty("违约金金额")
    private Long overdueAmount;

    @ApiModelProperty("结算金额")
    private Long settleAmount;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("结转金额")
    private Long carriedAmount;

    @ApiModelProperty("应缴总金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty("是否挂账：0未挂账，1已挂账")
    private Integer onAccount;

    @ApiModelProperty("收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    @ApiModelProperty("付款方id (结算的收费对象为账单的付款方)")
    private String payerId;

    @ApiModelProperty("付款方名称")
    private String payerName;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("结算账单ids列表")
    private List<Long> billIds;

    @ApiModelProperty("账单结算记录列表")
    private List<BillSettleDto> billSettleDtoList;

    /**
     * 构建结算单详情
     *
     * @param receivableBillAList
     * @param gatherDetailList
     * @return
     */
    public SettleDetailDto generalDetail(List<ReceivableBill> receivableBillAList, List<GatherDetail> gatherDetailList) {
        SettleDetailDto settleDetailDto = new SettleDetailDto();
        Long totalAmount = 0L;
        Long receivableAmount = 0L;
        Long overdueAmount = 0L;
        Long deductibleAmount = 0L;
        Long discountAmount = 0L;
        Long settleAmount = 0L;
        Long refundAmount = 0L;
        Long carriedAmount = 0L;
        Long actualUnpayAmount = 0L;
        List<Long> billIds = Lists.newArrayList();
        for (ReceivableBill receivableBill : receivableBillAList) {
            settleDetailDto = Global.mapperFacade.map(receivableBill, SettleDetailDto.class);
            settleDetailDto.setSource(receivableBill.getSource());
            if (receivableBillAList.size() <= 1) {
                settleDetailDto.setStartTime(receivableBill.getStartTime());
                settleDetailDto.setEndTime(receivableBill.getEndTime());
            }
            settleDetailDto.setPayerId(receivableBill.getPayerId());
            settleDetailDto.setPayerName(receivableBill.getPayerName());
            settleDetailDto.setSettleState(receivableBill.getSettleState());
            billIds.add(receivableBill.getId());
            totalAmount = totalAmount + receivableBill.getTotalAmount();
            receivableAmount = receivableAmount + receivableBill.getReceivableAmount();
            overdueAmount = overdueAmount + receivableBill.getOverdueAmount();
            deductibleAmount = deductibleAmount + receivableBill.getDeductibleAmount();
            discountAmount = discountAmount + receivableBill.getDiscountAmount();
            settleAmount = settleAmount + receivableBill.getSettleAmount();
            refundAmount = refundAmount + receivableBill.getRefundAmount();
            actualUnpayAmount = actualUnpayAmount + receivableBill.getActualUnpayAmount();
            carriedAmount = carriedAmount + receivableBill.getCarriedAmount();
        }
        settleDetailDto.setDiscountAmount(discountAmount);
        settleDetailDto.setTotalAmount(totalAmount);
        settleDetailDto.setReceivableAmount(receivableAmount);
        settleDetailDto.setOverdueAmount(overdueAmount);
        settleDetailDto.setDeductibleAmount(deductibleAmount);
        settleDetailDto.setSettleAmount(settleAmount);
        settleDetailDto.setRefundAmount(refundAmount);
        settleDetailDto.setActualUnpayAmount(actualUnpayAmount);
        settleDetailDto.setCarriedAmount(carriedAmount);
        settleDetailDto.setBillIds(billIds);
        settleDetailDto.setBillSettleDtoList(handleBillSettleDtoList(gatherDetailList));
        return settleDetailDto;
    }

    private List<BillSettleDto> handleBillSettleDtoList(List<GatherDetail> gatherDetailList) {
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
            billSettleDtoList.add(billSettleDto);
        });
        return billSettleDtoList;
    }

    /**
     * 构建临时收费结算单详情
     *
     * @param temporaryChargeBillAList
     * @param gatherDetailList
     * @return
     */
    public SettleDetailDto temporarySettleDetail(List<TemporaryChargeBill> temporaryChargeBillAList, List<GatherDetail> gatherDetailList) {
        SettleDetailDto settleDetailDto = new SettleDetailDto();
        Long totalAmount = 0L;
        Long receivableAmount = 0L;
        Long overdueAmount = 0L;
        Long deductibleAmount = 0L;
        Long discountAmount = 0L;
        Long settleAmount = 0L;
        Long refundAmount = 0L;
        Long actualUnpayAmount = 0L;
        List<Long> billIds = Lists.newArrayList();
        for (TemporaryChargeBill temporaryChargeBill : temporaryChargeBillAList) {
            settleDetailDto = Global.mapperFacade.map(temporaryChargeBill, SettleDetailDto.class);
            settleDetailDto.setSource(temporaryChargeBill.getSource());
            settleDetailDto.setSettleState(temporaryChargeBill.getSettleState());
            billIds.add(temporaryChargeBill.getId());
            totalAmount = totalAmount + temporaryChargeBill.getTotalAmount();
            receivableAmount = receivableAmount + temporaryChargeBill.getReceivableAmount();
            overdueAmount = overdueAmount + temporaryChargeBill.getOverdueAmount();
            deductibleAmount = deductibleAmount + temporaryChargeBill.getDeductibleAmount();
            discountAmount = discountAmount + temporaryChargeBill.getDiscountAmount();
            settleAmount = settleAmount + temporaryChargeBill.getSettleAmount();
            refundAmount = refundAmount + temporaryChargeBill.getRefundAmount();
            actualUnpayAmount = actualUnpayAmount + temporaryChargeBill.getActualUnpayAmount();
        }
        settleDetailDto.setDiscountAmount(discountAmount);
        settleDetailDto.setTotalAmount(totalAmount);
        settleDetailDto.setReceivableAmount(receivableAmount);
        settleDetailDto.setOverdueAmount(overdueAmount);
        settleDetailDto.setDeductibleAmount(deductibleAmount);
        settleDetailDto.setSettleAmount(settleAmount);
        settleDetailDto.setRefundAmount(refundAmount);
        settleDetailDto.setActualUnpayAmount(actualUnpayAmount);
        settleDetailDto.setBillIds(billIds);
        settleDetailDto.setBillSettleDtoList(handleBillSettleDtoList(gatherDetailList));
        return settleDetailDto;
    }
}
