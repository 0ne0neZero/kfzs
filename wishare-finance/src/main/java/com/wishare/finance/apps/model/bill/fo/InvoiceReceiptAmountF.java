package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2023/2/17
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取账单的开票金额入参")
public class InvoiceReceiptAmountF {

    @ApiModelProperty(value = "账单ids",required = true)
    @NotNull(message = "账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;
}
