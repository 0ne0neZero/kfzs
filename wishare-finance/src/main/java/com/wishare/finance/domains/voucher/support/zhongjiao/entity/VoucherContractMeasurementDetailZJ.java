package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@TableName(value = TableNames.VOUCHER_CONTRACT_MEASURE_DETAIL_ZJ, autoResultMap = true)
public class VoucherContractMeasurementDetailZJ extends BaseEntity {
    @ApiModelProperty(value = "计量明细id")
    private Long id;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("费项Id")
    private String chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("合同清单项目")
    private String contractItem;

    @ApiModelProperty("含税金额(单位：分)")
    private Long taxIncludedAmount;

    @ApiModelProperty("不含税金额(单位：分)")
    private Long taxExcludedAmount;

    @ApiModelProperty("合同发票主表id")
    private Long contractInvoiceId;

    @ApiModelProperty("报账单主表Id")
    private Long voucherBillId;
}