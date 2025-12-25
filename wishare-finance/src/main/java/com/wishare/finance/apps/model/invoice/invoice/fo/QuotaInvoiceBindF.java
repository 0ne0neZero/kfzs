package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("根据查询条件获取账单信息")
public class QuotaInvoiceBindF {

    @ApiModelProperty(value = "账单id", required = true)
    private Long billId;

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n" +
            "     9：全电专票"+
            "     10：定额发票", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty(value = "发票收据主表ids", required = true)
    private List<Long> invoiceReceiptIds;

    @ApiModelProperty(value = "发票收据主表ids和对应开票金额", required = true)
    private List<InvoiceReceiptIdAndAmountF> invoiceReceiptIdAndAmount;

    @ApiModelProperty(value = "账单金额 单位元", required = true)
    @NotNull(message = "账单金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许最大值为1000000000")
    private Double totalAmount;

    @ApiModelProperty(value = "开票金额 单位元", required = true)
    @NotNull(message = "开票金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许最大值为1000000000")
    private Double totalInvoiceAmount;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @NotNull(message = "账单类型不能为空")
    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "抬头类型：1个人，2企业", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "收款方式")
    private String payChannel;

    @ApiModelProperty("开票备注信息")
    private String remark;

    @ApiModelProperty(value = "开票人")
    private String clerk;
    @Getter
    @Setter
    public static class InvoiceReceiptIdAndAmountF {
        private String invoiceId;
        private Double amount;
    }
}
