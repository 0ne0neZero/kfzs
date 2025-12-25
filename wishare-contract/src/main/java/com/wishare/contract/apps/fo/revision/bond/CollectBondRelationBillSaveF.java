package com.wishare.contract.apps.fo.revision.bond;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
* <p>
* 收取保证金改版关联单据明细表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-07-27
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收取保证金改版关联单据明细表新增请求参数", description = "收取保证金改版关联单据明细表新增请求参数")
public class CollectBondRelationBillSaveF {

    /**
    * 保证金计划ID 不可为空
    */
    @ApiModelProperty(value = "保证金计划ID",required = true)
    @NotBlank(message = "保证金计划ID不可为空")
    @Length(message = "保证金计划ID不可超过 40 个字符",max = 40)
    private String bondId;
    /**
    * 编号 不可为空
    */
    @ApiModelProperty(value = "编号",required = true)
    @NotBlank(message = "编号不可为空")
    @Length(message = "编号不可超过 50 个字符",max = 50)
    private String code;
    /**
    * 业务类型编码（收款，收据，结转，退款） 不可为空
    */
    @ApiModelProperty(value = "业务类型编码（收款，收据，结转，退款）",required = true)
    @NotBlank(message = "业务类型编码（收款，收据，结转，退款）不可为空")
    @Length(message = "业务类型编码（收款，收据，结转，退款）不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
    * 业务类型名称（收款，收据，结转，退款）
    */
    @ApiModelProperty("业务类型名称（收款，收据，结转，退款）")
    @Length(message = "业务类型名称（收款，收据，结转，退款）不可超过 50 个字符",max = 50)
    private String type;
    /**
    * 金额（收款，收据，结转，退款） 不可为空
    */
    @ApiModelProperty(value = "金额（收款，收据，结转，退款）",required = true)
    @Digits(integer = 18,fraction =2,message = "金额（收款，收据，结转，退款）不正确")
    @NotNull(message = "金额（收款，收据，结转，退款）不可为空")
    private BigDecimal amount;
    /**
    * 交易方式编码（现金，微信，支付宝，网上转账等）
    */
    @ApiModelProperty("交易方式编码（现金，微信，支付宝，网上转账等）")
    @Length(message = "交易方式编码（现金，微信，支付宝，网上转账等）不可超过 40 个字符",max = 40)
    private String dealWayCode;
    /**
    * 交易方式名称（现金，微信，支付宝，网上转账等）
    */
    @ApiModelProperty("交易方式名称（现金，微信，支付宝，网上转账等）")
    @Length(message = "交易方式名称（现金，微信，支付宝，网上转账等）不可超过 50 个字符",max = 50)
    private String dealWay;
    /**
    * 业务费项ID
    */
    @ApiModelProperty("业务费项ID")
    @Length(message = "业务费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;
    /**
    * 业务费项名称
    */
    @ApiModelProperty("业务费项名称")
    @Length(message = "业务费项名称不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * （收款，收据，结转，退款）交易日期
    */
    @ApiModelProperty("（收款，收据，结转，退款）交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealDate;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
    * 原因
    */
    @ApiModelProperty("原因")
    @Length(message = "原因不可超过 255 个字符",max = 255)
    private String reason;
    /**
    * 剩余金额
    */
    @ApiModelProperty("剩余金额")
    @Digits(integer = 18,fraction =2,message = "剩余金额不正确")
    private BigDecimal residueAmount;
    /**
    * 审批流id
    */
    @ApiModelProperty("审批流id")
    private Long procId;
    /**
    * 状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成） 不可为空
    */
    @ApiModelProperty(value = "状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）",required = true)
    private Integer status;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

}
