package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/13
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取可以开票金额入参")
public class CanInvoiceInfoF {

    @ApiModelProperty(value = "账单ids",required = true)
    @NotNull(message = "账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n")
    private Integer invoiceType;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单",required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;
}
