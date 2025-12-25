package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/24
 */
@Getter
@Setter
@ApiModel("费用明细")
public class ProcessChargeDetailF {

    /**
     * 成本中心编码
     */
    @Length(max= 64,message="核算成本中心编码长度不能超过64")
    @ApiModelProperty("核算成本中心编码")
    private String costCenterCode;
    /**
     * 成本中心名称
     */
    @Length(max= 100,message="核算成本中心名称长度不能超过100")
    @ApiModelProperty("核算成本中心名称")
    private String costCenterName;

    /**
     * 部门编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("部门编码")
    private String orgCode;
    /**
     * 部门名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("部门名称")
    private String orgName;
    /**
     * 费项编码
     */
    @Length(max= 64,message="一级编码长度不能超过64")
    @ApiModelProperty("一级费项编码")
    private String firstChargeItemCode;
    /**
     * 费项名称
     */
    @Length(max= 100,message="一级费项名称长度不能超过100")
    @ApiModelProperty("一级费项名称")
    private String firstChargeItemName;
    /**
     * 费项编码
     */
    @Length(max= 64,message="二级编码长度不能超过64")
    @ApiModelProperty("二级费项编码")
    private String secondChargeItemCode;
    /**
     * 费项名称
     */
    @Length(max= 100,message="二级费项名称长度不能超过100")
    @ApiModelProperty("二级费项名称")
    private String secondChargeItemName;
    /**
     * 科目编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("科目编码")
    private String subjectCode;
    /**
     * 科目名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("科目名称")
    private String subjectName;
    /**
     * 业务类型编码
     */
    @Length(max= 64,message="业务类型编码长度不能超过64")
    @ApiModelProperty("业务类型编码")
    private String businessTypeCode;
    /**
     * 业务类型名称
     */
    @Length(max= 100,message="业务类型名称长度不能超过100")
    @ApiModelProperty("业务类型名称")
    private String businessTypeName;

    @ApiModelProperty("发票类型")
    private String invoiceType;
    /**
     * 税率编码
     */
    @Length(max= 64,message="税率编码长度不能超过64")
    @ApiModelProperty("税率编码")
    private String taxRateCode;
    /**
     * 税率
     */
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    /**
     * 原币金额（单位：分）
     */
    @NotNull(message="原币金额（单位：分）不能为空")
    @ApiModelProperty(value = "原币金额（单位：分）", required = true)
    private Long amount;
    /**
     * 本币不含税金额（单位：分）
     */
    @ApiModelProperty(value = "原币不含税金额（单位：分）", required = true)
    private Long excTaxAmount;
    /**
     * 税额（单位：分）
     */
    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

}
