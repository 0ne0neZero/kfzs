package com.wishare.finance.apps.model.invoice.invoicebook.vo;

import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceBookStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Getter
@Setter
@ApiModel("票本反参")
public class InvoiceBookV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("票本编号")
    private String invoiceBookNumber;

    @ApiModelProperty("票据类型:1增值税普通发票，2增值税专用发票，3纸质收据")
    private Integer type;

    @ApiModelProperty("购入组织id")
    private Long buyOrgId;

    @ApiModelProperty("购入组织名称")
    private String buyOrgName;

    @ApiModelProperty("购入数量")
    private Long buyQuantity;

    @ApiModelProperty("购入日期")
    private LocalDate buyDate;

    @ApiModelProperty("票面金额（单位:分）")
    private Long denomination;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("票本起始号码")
    private String invoiceStartNumber;

    @ApiModelProperty("票本结束号码")
    private String invoiceEndNumber;

    @ApiModelProperty("票本状态：1.未领用 2.部分领用 3.全部领用")
    private Integer state;

    @ApiModelProperty("票本状态描述：1.未领用 2.部分领用 3.全部领用")
    private String stateStr;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;

    public String getStateStr() {
        InvoiceBookStateEnum invoiceBookStateEnum = InvoiceBookStateEnum.valueOfByCode(this.state);
        return invoiceBookStateEnum.getDes();
    }
}
