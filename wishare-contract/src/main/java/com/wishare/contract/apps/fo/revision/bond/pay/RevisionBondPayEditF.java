package com.wishare.contract.apps.fo.revision.bond.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/28  10:42
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-缴纳类保证金新增请求参数EditF", description = "保证金改版-缴纳类保证金新增请求参数EditF")
public class RevisionBondPayEditF {

    /**
     * id 不可为空
     */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 40 个字符",max = 40)
    private String id;
    /**
     * typeCode
     */
    @ApiModelProperty("保证金类型Code")
    @Length(message = "保证金类型Code不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
     * customerId
     */
    @ApiModelProperty("客户ID")
    @Length(message = "客户ID不可超过 40 个字符",max = 40)
    private String customerId;
    /**
     * contractId
     */
    @ApiModelProperty("合同ID")
    @Length(message = "合同ID不可超过 40 个字符",max = 40)
    private String contractId;
    /**
     * costCenterId
     */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
     * communityId
     */
    @ApiModelProperty("所属项目ID")
    @Length(message = "所属项目ID不可超过 40 个字符",max = 40)
    private String communityId;
    /**
     * bondAmount
     */
    @ApiModelProperty("保证金总额")
    @Digits(integer = 18,fraction =2,message = "保证金总额不正确")
    private BigDecimal bondAmount;
    /**
     * plannedPayDate
     */
    @ApiModelProperty("计划付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedPayDate;
    /**
     * plannedPayAmount
     */
    @ApiModelProperty("计划付款金额")
    @Digits(integer = 18,fraction =2,message = "计划付款金额不正确")
    private BigDecimal plannedPayAmount;
    /**
     * orgId
     */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 40 个字符",max = 40)
    private String orgId;
    /**
     * chargeManId
     */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 40 个字符",max = 40)
    private String chargeManId;
    /**
     * remark
     */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
     * 是否为暂存
     */
    @ApiModelProperty("是否为暂存")
    private Boolean isStash;

}
