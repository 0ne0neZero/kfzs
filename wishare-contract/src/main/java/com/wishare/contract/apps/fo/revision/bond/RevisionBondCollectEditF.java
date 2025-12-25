package com.wishare.contract.apps.fo.revision.bond;

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
 * @since： 2023/7/26  15:51
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-收取类保证金编辑请求参数EditF", description = "保证金改版-收取类保证金编辑请求参数EditF")
public class RevisionBondCollectEditF {

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
    @ApiModelProperty(value = "保证金类型Code", required = true)
    @Length(message = "保证金类型Code不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
     * supplierId
     */
    @ApiModelProperty(value = "供应商ID", required = true)
    @Length(message = "供应商ID不可超过 40 个字符",max = 40)
    private String supplierId;
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
    @ApiModelProperty(value = "保证金总额", required = true)
    @Digits(integer = 18,fraction =2,message = "保证金总额不正确")
    private BigDecimal bondAmount;
    /**
     * plannedCollectionDate
     */
    @ApiModelProperty(value = "计划收款日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionDate;
    /**
     * plannedCollectionAmount
     */
    @ApiModelProperty(value = "计划收款金额", required = true)
    @Digits(integer = 18,fraction =2,message = "计划收款金额不正确")
    private BigDecimal plannedCollectionAmount;
    /**
     * orgId
     */
    @ApiModelProperty(value = "所属部门ID", required = true)
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
