package com.wishare.finance.domains.voucher.support.fangyuan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = TableNames.CHARGE_CUSTOMER, autoResultMap = true)
public class ChargeCustomer {
    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "费项编码")
    private String chargeItemCode;
    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;
    @ApiModelProperty(value = "客商")
    private String customer;
}
