package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
/**
 * @author dp
 * @date 2024/01/16
 * @Description:
 */
@Getter
@Setter
@ApiModel("查询收款单开票金额入参")
public class GatherInvoiceF {

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单明细id")
    private Long gatherDetailId;

    @ApiModelProperty(value = "收款单类型 0-收款单， 1-收款明细")
    private Integer gatherBillType;

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

    @ApiParam("上级收费单元ID")
    private String supCpUnitId;

}
