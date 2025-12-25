package com.wishare.finance.domains.voucher.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
* 业务单据明细表
* @author luzhonghe
* @TableName business_bill_detail
*/
@Getter
@Setter
@ApiModel(value = "业务单据明细表")
@TableName(value = TableNames.BUSINESS_BILL_DETAIL, autoResultMap = true)
public class BusinessBillDetail implements Serializable {

    /**
    * 单据详情id
    */
    @ApiModelProperty("单据详情id")
    private Long id;
    /**
    * 单据id
    */
    @ApiModelProperty("单据id")
    private Long billId;
    /**
    * 账簿编码
    */
    @ApiModelProperty("账簿编码")
    private String accountBookCode;
    /**
    * 账簿名称
    */
    @ApiModelProperty("账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "账套标识id")
    private String accountDetailId;

    @ApiModelProperty(value = "支付id")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<String> payIds;

    @ApiModelProperty("凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "记账人")
    private String bookkeeper;

    @ApiModelProperty(value = "模板序号，默认为1，从1开始递增")
    private Integer templateNum = 1;

    @Valid
    @ApiModelProperty(value = "费用明细")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessChargeDetailF> chargeDetails;

    @Valid
    @ApiModelProperty(value = "发票明细")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ReimbursementInvoiceF> invoices;

    @Valid
    @ApiModelProperty(value = "收款业务收款方信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessPayeeF> payees;

    @Valid
    @ApiModelProperty(value = "收款业务付款方信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessPayerF> payers;

    @ApiModelProperty(value = "支付方银行信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessBankPayInfoF> payBankInfos;

    @ApiModelProperty(value = "对公收款方信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessBankPublicF> publicPayees;

    @ApiModelProperty(value = "对私收款方信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private List<ProcessBankPrivateF> privatePayees;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

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
     * 创建时间 yyyy-MM-dd HH:mm:ss
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
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    public void init() {
        if (Objects.isNull(id)) {
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BUSINESS_BILL_DETAIL);
        }
    }

}
