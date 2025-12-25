package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/1/4
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单金额相关扩展")
public class BillAmountExtendDto {

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;
}
