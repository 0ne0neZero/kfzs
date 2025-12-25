package com.wishare.contract.domains.dto.contractset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

/**
* <p>
* 合同订立信息表
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_conclude请求对象", description = "合同订立信息表")
public class ContractConcludeD {

    @ApiModelProperty("合同id")
    private Long id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("公司id 来源 org_tenant.id")
    private String tenantId;
    @ApiModelProperty("项目id 来源 space_community.id")
    private String communityId;

    @ApiModelProperty("所属部门ID")
    private String belongOrgId;

    @ApiModelProperty("所属部门名称")
    private String belongOrgName;

    @ApiModelProperty("所属部门组织路径")
    private String belongOrgPath;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String PartyBName;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("合同性质 1 收入 2 支出")
    private Integer contractNature;
    @ApiModelProperty("金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("金额（不含税）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtExpireStart;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtExpireEnd;
    @ApiModelProperty("是否虚拟合同 0 否 1 是")
    private Boolean virtualContract;
    @ApiModelProperty("是否倒签合同 0 否 1 是")
    private Boolean backdatingContract;
    @ApiModelProperty("签约方式")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 1 未履行 2 履行中 3 已到期 4 已终止")
    private Integer contractState;
    @ApiModelProperty("保证金额")
    private Boolean bondAmount;
    @ApiModelProperty("是否关联招投保证金（收入类为招标保证金，支出类为投标）")
    private Boolean bidBond;
    @ApiModelProperty("招投保证金名称")
    private String bidBondName;
    @ApiModelProperty("招投保证金金额")
    private BigDecimal bidBondAmount;
    @ApiModelProperty("关联支出类合同")
    private String expenditureContract;
    @ApiModelProperty("合同内容摘要")
    private String contractAbstract;
    @ApiModelProperty("是否引用范本 0 否 1 是")
    private Boolean referenceModel;
    @ApiModelProperty("范本名称")
    private String tempName;
    @ApiModelProperty("关联收入类合同")
    private String incomeContract;
    @ApiModelProperty("合同文本")
    private String contractText;
    @ApiModelProperty("合同附件")
    private String contractEnclosure;
    @ApiModelProperty("其他说明文件")
    private String otherDocuments;
    @ApiModelProperty("审核状态 1 通过 2 未提交 3 审批中 4 已驳回")
    private Integer reviewStatus;
    @ApiModelProperty("审批流id")
    private Long procId;
    @ApiModelProperty("经办人")
    private String handledBy;
    @ApiModelProperty("经办人所在部门")
    private Long orgId;
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("保证金类型")
    private Integer bondType;
    @ApiModelProperty("是否框架合同 0 否 1 是")
    private Boolean frameworkContract;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;
    @ApiModelProperty("实际履约金额（本币含税）")
    private BigDecimal contractAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("关联招投标保证金账单id")
    private Long bidBondBillId;
    @ApiModelProperty("关联招投标保证金账单编号")
    private String bidBondBillNo;
    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;
    @ApiModelProperty("项目来源")
    private String projectSource;
    @ApiModelProperty("业态")
    private String businessType;
    @ApiModelProperty("物业模式 0 包干制 1酬金制")
    private String propertyModel;
    @ApiModelProperty("物业费单价")
    private BigDecimal propertyFeePrice;
}
