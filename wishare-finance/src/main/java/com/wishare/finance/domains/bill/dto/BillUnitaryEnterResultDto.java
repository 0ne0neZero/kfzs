package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单统一入账结果信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一入账结果信息")
public class BillUnitaryEnterResultDto {

    @ApiModelProperty(value = "收款单id", required = true)
    private Long id;

    @ApiModelProperty(value = "收款单编号", required = true)
    private Integer billNo;

    @ApiModelProperty(value = "账单类型 1:应收，2:预收，3:临时，4:应付，6:付款， 7:收款", required = true)
    private Integer billType;

}
