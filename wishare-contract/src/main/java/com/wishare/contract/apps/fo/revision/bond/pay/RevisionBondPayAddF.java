package com.wishare.contract.apps.fo.revision.bond.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
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
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "保证金改版-缴纳类保证金新增请求参数AddF", description = "保证金改版-缴纳类保证金新增请求参数AddF")
public class RevisionBondPayAddF {

    /**
     * 保证金类型Code 不可为空
     */
    @ApiModelProperty(value = "保证金类型Code",required = true)
    @NotBlank(message = "保证金类型Code不可为空")
    @Length(message = "保证金类型Code不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
     * 客户ID 不可为空
     */
    @ApiModelProperty(value = "客户ID",required = true)
    @NotBlank(message = "客户ID不可为空")
    @Length(message = "客户ID不可超过 40 个字符",max = 40)
    private String customerId;
    /**
     * 合同ID
     */
    @ApiModelProperty("合同ID")
    @Length(message = "合同ID不可超过 40 个字符",max = 40)
    private String contractId;
    /**
     * 成本中心ID
     */
    @ApiModelProperty(value = "成本中心ID", required = true)
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
     * 所属项目ID
     */
    @ApiModelProperty("所属项目ID")
    @Length(message = "所属项目ID不可超过 40 个字符",max = 40)
    private String communityId;
    /**
     * 保证金总额 不可为空
     */
    @ApiModelProperty(value = "保证金总额",required = true)
    @Digits(integer = 18,fraction =2,message = "保证金总额不正确")
    @NotNull(message = "保证金总额不可为空")
    private BigDecimal bondAmount;
    /**
     * 计划付款日期
     */
    @ApiModelProperty(value = "计划付款日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedPayDate;
    /**
     * 计划付款金额 不可为空
     */
    @ApiModelProperty(value = "计划付款金额",required = true)
    @Digits(integer = 18,fraction =2,message = "计划付款金额不正确")
    @NotNull(message = "计划付款金额不可为空")
    private BigDecimal plannedPayAmount;
    /**
     * 所属部门ID
     */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 40 个字符",max = 40)
    private String orgId;
    /**
     * 负责人ID
     */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 40 个字符",max = 40)
    private String chargeManId;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
     * 附件凭证
     */
    @ApiModelProperty("附件凭证")
    private FileVo fileVo;
    /**
     * 是否为暂存
     */
    @ApiModelProperty("是否为暂存")
    private Boolean isStash;

}
