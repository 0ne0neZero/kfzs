package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收票票据信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/22
 */
@Getter
@Setter
@ApiModel("收票发票信息")
public class InvoiceInfoF {

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty(value = "申请开票时间",required = true)
    @NotNull(message = "申请开票时间不能为空")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "开具发票时间",required = true)
    @NotNull(message = "开具发票时间不能为空")
    private LocalDateTime billingTime;

    @ApiModelProperty(value = "发票价税合计金额",required = true)
    @NotNull(message = "发票价税合计金额不能为空")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "发票抬头类型：1 个人 2 企业",required = true)
    @NotNull(message = "发票抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "发票代码",required = true)
    @NotBlank(message = "发票代码不能为空")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码",required = true)
    @NotBlank(message = "发票号码不能为空")
    private String invoiceNo;

    @ApiModelProperty(value = "外部系统发票地址, 诺诺或专业发票系统的发票地址")
    private String outInvoiceUrl;

    @ApiModelProperty(value = "开票员",required = true)
    @NotBlank(message = "开票员不能为空")
    private String clerk;

    @ApiModelProperty(value = "开票单元id, 开票的最小单元，应用系统自定义（如项目id）可用于后续业务操作",required = true)
    @NotBlank(message = "开票单元id不能为空")
    private String invRecUnitId;

    @ApiModelProperty(value = "开票单元名称",required = true)
    @NotBlank(message = "开票单元名称不能为空")
    private String invRecUnitName;

    @Valid
    @ApiModelProperty("收票明细信息")
    @NotNull(message = "收票明细不能为空")
    private List<InvoiceCollectionDetailF> invoiceDetails;

}
