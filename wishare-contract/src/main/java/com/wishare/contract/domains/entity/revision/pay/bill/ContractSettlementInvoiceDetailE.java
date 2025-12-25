package com.wishare.contract.domains.entity.revision.pay.bill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_settlement_invoice_detail")
public class ContractSettlementInvoiceDetailE {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 结算单ID
     */
    private String settlementId;

    /**
     * 票据类型
     */
    private String invoiceType;

    /**
     * 发票号码
     */
    private String invoiceNum;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 收票含税金额
     */
    private BigDecimal invoiceTaxAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 可抵扣金额
     */
    private BigDecimal deductionAmount;

    /**
     * 开票时间
     */
    private LocalDate invoiceDate;

    /**
     * 验证状态
     */
    private Integer verifyStatus;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    @ApiModelProperty("购方名称")
    private String inname;
    @ApiModelProperty("购方识别号")
    private String gfsbh;
    @ApiModelProperty("销方名称")
    private String outname;
    @ApiModelProperty("销方识别号")
    private String xfsbh;
}
