package com.wishare.contract.apps.fo.revision.bond;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 收取保证金改版关联单据明细表 分页请求参数
* </p>
*
* @author chenglong
* @since 2023-07-27
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收取保证金改版关联单据明细表分页请求参数", description = "收取保证金改版关联单据明细表")
public class CollectBondRelationBillPageF {

    /**
    * bondId
    */
    @ApiModelProperty("保证金计划ID")
    @Length(message = "保证金计划ID不可超过 40 个字符",max = 40)
    private String bondId;
    /**
    * code
    */
    @ApiModelProperty("编号")
    @Length(message = "编号不可超过 50 个字符",max = 50)
    private String code;
    /**
    * typeCode
    */
    @ApiModelProperty("业务类型编码（收款，收据，结转，退款）")
    @Length(message = "业务类型编码（收款，收据，结转，退款）不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
    * type
    */
    @ApiModelProperty("业务类型名称（收款，收据，结转，退款）")
    @Length(message = "业务类型名称（收款，收据，结转，退款）不可超过 50 个字符",max = 50)
    private String type;
    /**
    * amount
    */
    @ApiModelProperty("金额（收款，收据，结转，退款）")
    @Digits(integer = 18,fraction =2,message = "金额（收款，收据，结转，退款）不正确")
    private BigDecimal amount;
    /**
    * dealWayCode
    */
    @ApiModelProperty("交易方式编码（现金，微信，支付宝，网上转账等）")
    @Length(message = "交易方式编码（现金，微信，支付宝，网上转账等）不可超过 40 个字符",max = 40)
    private String dealWayCode;
    /**
    * dealWay
    */
    @ApiModelProperty("交易方式名称（现金，微信，支付宝，网上转账等）")
    @Length(message = "交易方式名称（现金，微信，支付宝，网上转账等）不可超过 50 个字符",max = 50)
    private String dealWay;
    /**
    * chargeItemId
    */
    @ApiModelProperty("业务费项ID")
    @Length(message = "业务费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;
    /**
    * chargeItem
    */
    @ApiModelProperty("业务费项名称")
    @Length(message = "业务费项名称不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * dealDate
    */
    @ApiModelProperty("（收款，收据，结转，退款）交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealDate;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
    * reason
    */
    @ApiModelProperty("原因")
    @Length(message = "原因不可超过 255 个字符",max = 255)
    private String reason;
    /**
    * residueAmount
    */
    @ApiModelProperty("剩余金额")
    @Digits(integer = 18,fraction =2,message = "剩余金额不正确")
    private BigDecimal residueAmount;
    /**
    * procId
    */
    @ApiModelProperty("审批流id")
    private Long procId;
    /**
    * status
    */
    @ApiModelProperty("状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）")
    private Integer status;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人")
    @Length(message = "创建人不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人名称")
    @Length(message = "创建人名称不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * operator
    */
    @ApiModelProperty("操作人")
    @Length(message = "操作人不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人名称")
    @Length(message = "操作人名称不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"bondId\",\"code\",\"typeCode\",\"type\",\"amount\",\"dealWayCode\",\"dealWay\",\"chargeItemId\",\"chargeItem\",\"dealDate\",\"remark\",\"reason\",\"residueAmount\",\"procId\",\"status\",\"deleted\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\"]"
        + "id 主键ID"
        + "bondId 保证金计划ID"
        + "code 编号"
        + "typeCode 业务类型编码（收款，收据，结转，退款）"
        + "type 业务类型名称（收款，收据，结转，退款）"
        + "amount 金额（收款，收据，结转，退款）"
        + "dealWayCode 交易方式编码（现金，微信，支付宝，网上转账等）"
        + "dealWay 交易方式名称（现金，微信，支付宝，网上转账等）"
        + "chargeItemId 业务费项ID"
        + "chargeItem 业务费项名称"
        + "dealDate （收款，收据，结转，退款）交易日期"
        + "remark 备注"
        + "reason 原因"
        + "residueAmount 剩余金额"
        + "procId 审批流id"
        + "status 状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）"
        + "deleted 是否删除  0 正常 1 删除"
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间")
    private List<String> fields;


}
