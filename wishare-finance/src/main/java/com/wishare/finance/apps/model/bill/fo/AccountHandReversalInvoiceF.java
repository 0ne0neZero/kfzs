package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/14 9:48
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("票据红冲-少收金额")
public class AccountHandReversalInvoiceF {

    @ApiModelProperty("账单id")
    @NotNull(message = "账单id不存在")
    @DecimalMin(value = "1", message = "账单id必须为正整数")
    private Long id;

    @ApiModelProperty("账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;
}
