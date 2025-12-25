package com.wishare.finance.infrastructure.remote.vo.contract;

import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title ContractReceivableBill
 * @date 2024.07.03  15:28
 * @description: 同步 枫行梦应收账单
 */
@Getter
@Setter
public class ContractReceivableBill {

    @ApiModelProperty("操作类型 1： 新增 2：变更 3: 废除")
    private Integer operator;

    @ApiModelProperty("合同编号")
    private String agreementNo;

    @ApiModelProperty("应收账单ID")
    private Long agreementBillId;

    @ApiModelProperty("项目 pj 码")
    private String projectCode;

    @ApiModelProperty("应收金额（含税）")
    private Long amount;

    @ApiModelProperty("费用名称")
    private String feeName;

    @ApiModelProperty("费用ID")
    private Long feeNameConfigInfoId;

    @ApiModelProperty("计费开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty("计费结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty("收入关系")
    private Integer incomeType;

    @ApiModelProperty("分成类型")
    private Integer percentType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    public ContractReceivableBill() {
    }

    public ContractReceivableBill(TemporaryChargeBill bill){
        this.agreementNo = bill.getContractNo();
        this.agreementBillId = bill.getId();
        this.amount = bill.getReceivableAmount();
        this.feeName = bill.getChargeItemName();
        this.feeNameConfigInfoId = bill.getChargeItemId();
        this.startDate = bill.getStartTime();
        this.endDate = bill.getEndTime();
        this.incomeType = 0;
        this.percentType = 0;
        this.taxRate = bill.getTaxRate();
    }

}
