package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ChargeTicketRuleQueryTypeV {

    @ApiModelProperty("允许开票类型集合")
    private List<Integer> allowTypeList;

    @ApiModelProperty("默认开票类型")
    private Byte defaultType;

    /** 中交 增值税电子发票|电子收据|全电普票*/
    @ApiModelProperty("缴费强制开票 0禁用 1启用 默认禁用")
    private Byte isForce = 0;

    /** 中交 增值税电子发票|电子收据|全电普票*/
    @ApiModelProperty("缴费强制开票确认 0禁用 1启用 默认禁用")
    private Integer forceConfirm;

    @ApiModelProperty("是否允许缴费前开票 0否 1是")
    private Integer canInvoiceBeforePay;

    @ApiModelProperty("抬头模式设置 0编辑模式 1选择模式 默认0")
    private Integer titleMode;

    @ApiModelProperty("默认开票抬头 0业主 1房号 2房号-业主 3产权人")
    private Byte defaultTicketTitle;

    @ApiModelProperty("开票类型对应法定单位信息")
    private List<ChargeTicketRuleInvoiceV> invoiceVList;

    @ApiModelProperty("是否免税开票（0否1是）")
    private Byte freeTicketFlag;

    @ApiModelProperty("开票单位名称")
    private String nameCn;

    @ApiModelProperty("票据模板ID")
    private Long receiptTemplateId;

    @ApiModelProperty("票据模板名称")
    private String receiptTemplateName;

    @ApiModelProperty("可开票金额")
    private BigDecimal canInvoiceAmount;
}
