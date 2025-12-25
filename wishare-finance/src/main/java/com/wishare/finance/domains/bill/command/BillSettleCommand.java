package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账单结算明细
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/22
 */
@Getter
@Setter
@ApiModel("支付结算明细")
public class BillSettleCommand {

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CHEQUE: 支票，COMPLEX: 组合支付，OTHER: 其他）")
    @NotNull(message = "结算渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    @NotNull(message = "结算方式不能为空")
    private Integer payWay;

    @ApiModelProperty(value = "收费对象类型")
    private Integer payerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    @ApiModelProperty(value = "收款方id")
    private String payeeId;

    @ApiModelProperty(value = "收款方名称")
    private String payeeName;

    @ApiModelProperty(value = "收款方手机号")
    private String payeePhone;

    @ApiModelProperty(value = "付款方id")
    private String payerId;

    @ApiModelProperty(value = "付款方名称")
    private String payerName;

    @ApiModelProperty(value = "付款方手机号")
    private String payerPhone;

    @ApiModelProperty(value = "减免说明列表")
    @Valid
    private List<DiscountOBV> discounts;

    @ApiModelProperty(value = "支付总金额 （区间为[1, 1000000000]）", required = true)
    @NotNull(message = "支付金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    private Long payAmount;

    @ApiModelProperty(value = "支付时间 格式：yyyy-MM-dd HH:mm:ss", required = true)
    @NotNull(message = "支付时间不能为空")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "渠道交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "支付结算详情， 有多个填多个，存在于组合支付的情况")
    private List<BillSettleDetailCommand> details;

    @ApiModelProperty("支付来源：0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 10-亿家生活app，11-亿管家app，12-亿家生活公众号，13-智能pos机")
    private Integer paySource;

    @ApiModelProperty(value = "支付渠道商户号")
    private String mchNo;

    @ApiModelProperty(value = "支付渠道设备号")
    private String deviceNo;

    @ApiModelProperty("银行流水号")
    private String bankFlowNo;


    /**
     * 构建收款单
     * @param bill
     * @return
     */
    public GatherBill buildGatherBill(Bill bill){
        GatherBill gatherBill = new GatherBill();
        gatherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        gatherBill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        gatherBill.setStatutoryBodyId(bill.getStatutoryBodyId());
        gatherBill.setStatutoryBodyName(bill.getStatutoryBodyName());
        gatherBill.setSbAccountId(bill.getSbAccountId());
        gatherBill.setStartTime(bill.getStartTime());
        gatherBill.setEndTime(bill.getEndTime());
        gatherBill.setPayTime(getPayTime());
        gatherBill.setPayChannel(getPayChannel());
        gatherBill.setPayWay(getPayWay());
        gatherBill.setDescription("应收账单入账收款");
        gatherBill.setTotalAmount(getPayAmount());
        gatherBill.setPayeeId(bill.getPayeeId());
        gatherBill.setPayeeName(bill.getPayeeName());
        gatherBill.setPayerId(bill.getPayerId());
        gatherBill.setPayerName(bill.getPayerName());
        gatherBill.setSysSource(bill.getSysSource());
        gatherBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        gatherBill.setTradeNo(getTradeNo());
        gatherBill.setSupCpUnitId(bill.getCommunityId());
        gatherBill.setSupCpUnitName(bill.getCommunityName());
        List<DiscountOBV> discounts = getDiscounts();
        if (CollectionUtils.isNotEmpty(discounts)){
            gatherBill.setDiscounts(getDiscounts());
            gatherBill.setDiscountAmount(discounts.stream().map(DiscountOBV::getAmount).collect(Collectors.summingLong(Long::longValue)));
        }
        return gatherBill;
    }

    /**
     * 构建收款详情
     * @param bill
     * @param gatherBill
     * @return
     */
    public List<GatherDetail> buildGatherDetails(Bill bill, GatherBill gatherBill){
        List<GatherDetail> gatherDetails = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(details)){
            for (BillSettleDetailCommand command : details) {
                GatherDetail detail = new GatherDetail();
                detail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_DETAIL));
                detail.setGatherType(GatherDetail.getGatherType(bill).getCode());
                detail.setGatherBillId(gatherBill.getId());
                detail.setGatherBillNo(gatherBill.getBillNo());
                if(BillTypeEnum.应收账单.equalsByCode(bill.getType()) || BillTypeEnum.临时收费账单.equalsByCode(bill.getType())){
                    detail.setRecBillId(bill.getId());
                    detail.setRecBillNo(bill.getBillNo());
                }
                detail.setCostCenterId(bill.getCostCenterId() );
                detail.setCostCenterName(bill.getCostCenterName());
                detail.setChargeItemId(bill.getChargeItemId());
                detail.setChargeItemName(bill.getChargeItemName());
                detail.setSupCpUnitId(bill.getCommunityId());
                detail.setSupCpUnitName(bill.getCommunityName());
                detail.setCpUnitId(bill.getRoomId());
                detail.setCpUnitName(bill.getRoomName());
                detail.setPayChannel(getPayChannel());
                detail.setPayWay(getPayWay());
                detail.setRecPayAmount(command.getPayAmount());
                detail.setPayAmount(command.getPayAmount());
                detail.setPayerType(getPayWay());
                detail.setPayTime(getPayTime());
                detail.setChargeStartTime(command.getChargeStartTime());
                detail.setChargeEndTime(command.getChargeEndTime());
                detail.setPayerId(gatherBill.getPayerId());
                detail.setPayerName(gatherBill.getPayerName());
                detail.setPayeeId(gatherBill.getPayeeId());
                detail.setPayeeName(gatherBill.getPayeeName());
                detail.setPayerPhone(getPayerPhone());
                detail.setPayeePhone(getPayeePhone());
                detail.setOutPayNo(command.getTradeNo());
                gatherDetails.add(detail);
            }
        }
        return gatherDetails;
    }
    
    
}
