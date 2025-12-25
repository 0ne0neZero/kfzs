package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ApiModel("获取发票备注信息")
public class InvoiceRemarkF {

    @ApiModelProperty(value = "开票类型：1:蓝票;2:红票", required = true)
    @NotNull(message = "开票类型不能为空")
    private Integer invoiceType;

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n")
    private Integer type;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "账单ids", required = true)
    private List<Long> billIds;

    @ApiModelProperty("收款单ID")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单明细id集合")
    private List<Long> gatherDetailIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    private Integer billType;

}
