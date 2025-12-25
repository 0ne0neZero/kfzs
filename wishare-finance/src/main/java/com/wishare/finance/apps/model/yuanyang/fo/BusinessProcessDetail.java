package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Getter
@Setter
@ApiModel("BPM单据明细")
public class BusinessProcessDetail {
    
    /**
     * 账簿编码
     */
    @NotBlank(message="账簿编码不能为空")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "账簿编码", required = true)
    private String accountBookCode;
    /**
     * 账簿名称
     */
    @NotBlank(message="账簿名称不能为空")
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty(value = "账簿名称", required = true)
    private String accountBookName;
    /**
     * 成本中心编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("成本中心编码")
    private String costCenterCode;
    /**
     * 成本中心名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
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
     * 人员编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("人员编码")
    private String staffCode;
    /**
     * 人员名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("人员名称")
    private String staffName;
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
    /**
     * 原币金额（单位：分）
     */
    @NotNull(message="原币金额（单位：分）不能为空")
    @ApiModelProperty(value = "原币金额（单位：分）", required = true)
    private Long amount;
    /**
     * 费项编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("费项编码")
    private String chargeItemCode;
    /**
     * 费项名称
     */
    @Length(max= 100,message="费项名称长度不能超过100")
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    /**
     * 本币金额（单位：分）
     */
    @NotNull(message="本币金额（单位：分）不能为空")
    @ApiModelProperty(value = "本币金额（单位：分）", required = true)
    private Long localAmount;
    /**
     * 本币不含税金额（单位：分）
     */
    @NotNull(message="本币不含税金额（单位：分）不能为空")
    @ApiModelProperty(value = "本币不含税金额（单位：分）", required = true)
    private Long excTaxLocalAmount;
    /**
     * 币种
     */
    @Length(max= 20,message="币种长度不能超过20")
    @ApiModelProperty(value = "币种,默认CNY", required = true)
    private String currency = "CNY";
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
     * 税额（单位：分）
     */
    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;
    /**
     * 付款账号
     */
    @Length(max= 32,message="付款账号长度不能超过32")
    @ApiModelProperty("付款账号")
    private String payerAccount;
    /**
     * 付款联行号
     */
    @Length(max= 32,message="付款联行号长度不能超过32")
    @ApiModelProperty("付款联行号")
    private String payerCnapsCode;
    /**
     * 收款账号
     */
    @Length(max= 32,message="收款账号长度不能超过32")
    @ApiModelProperty("收款账号")
    private String payeeAccount;
    /**
     * 收款联行号
     */
    @Length(max= 32,message="收款联行号长度不能超过32")
    @ApiModelProperty("收款联行号")
    private String payeeCnapsCode;
    /**
     * 收付类型 1收款， 2付款
     */
    @NotNull(message="收付类型不能为空")
    @ApiModelProperty(value = "收付类型 1收款， 2付款", required = true)
    private Integer payType;
    /**
     * 支付方式
     */
    @NotBlank(message="支付方式不能为空")
    @Length(max= 20,message="支付方式长度不能超过20")
    @ApiModelProperty(value = "支付方式", required = true)
    private String payMethod;
    /**
     * 单据描述
     */
    @Length(max= 20,message="单据描述长度不能超过20")
    @ApiModelProperty("单据描述")
    private String description;

}
