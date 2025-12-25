package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.Bill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 所有账单分页列表(用于业主端查询账单)
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("所有账单分页列表")
public class AllBillPageDto extends Bill {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    /**
     * {@linkplain BillTypeEnum}
     */
    @ApiModelProperty("账单类型")
    private Integer billType;
    @ApiModelProperty("无效状态")
    private String invalidState;

    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积
     */
    private BigDecimal chargingArea;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    @ApiModelProperty("发生时间")
    private LocalDateTime payTime;

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;
    public String getPayChannelName(){
        if (this.payChannel!=null){
            return SettleChannelEnum.valueNameOfByCode(payChannel);
        }
        return "";
    }

    public String getInvalidState() {
        if(Objects.nonNull(getState()) && BillStateEnum.作废.getCode() == getState()){
            this.invalidState = "已作废";
        }else if(Objects.nonNull(getCarriedState()) && BillCarryoverStateEnum.已结转.getCode() == getCarriedState()){
            this.invalidState = "已结转";
        }else if(Objects.nonNull(getReversed()) && BillReverseStateEnum.已冲销.getCode() == getReversed()){
            this.invalidState = "已冲销";
        }else if(Objects.nonNull(getRefundState()) && BillRefundStateEnum.已退款.getCode() == getRefundState()){
            this.invalidState = "已退款";
        }else{
            return null;
        }
        return invalidState;
    }

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;
}
