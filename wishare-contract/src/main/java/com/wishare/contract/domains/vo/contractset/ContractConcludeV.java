package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
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
public class ContractConcludeV extends Tree<ContractConcludeV,Long> {

//    @ApiModelProperty("合同id")
//    private Long id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("公司id 来源 org_tenant.id")
    private String tenantId;
    @ApiModelProperty("公司名称")
    private String tenantName;
    @ApiModelProperty(value = "项目id 来源 成本中心",required = true)
    private String communityId;
    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty("所属部门ID")
    private String belongOrgId;

    @ApiModelProperty("所属部门名称")
    private String belongOrgName;

    @ApiModelProperty("合同编号")
    private String contractNo;
    //    @ApiModelProperty("父级id")
//    private Long pid;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String PartyBName;
    @ApiModelProperty("丙方名称")
    private String PartyCName;
    @ApiModelProperty("甲方Id")
    private Long partyAId;
    @ApiModelProperty("乙方Id")
    private Long PartyBId;
    @ApiModelProperty("丙方Id")
    private Long PartyCId;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("合同性质 1 收入 2 支出 3 其他")
    private Integer contractNature;
    @ApiModelProperty("金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("是否虚拟合同 false 否 true 是")
    private Boolean virtualContract;
    @ApiModelProperty("是否倒签合同 false 否 true 是")
    private Boolean backdatingContract;
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 3 终止 4 终止中")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止")
    private Integer contractState;
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    @ApiModelProperty("保证金额-原币")
    private BigDecimal bondAmount;
    @ApiModelProperty("保证金额-本币")
    private BigDecimal bondHomeCurrency;
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
    @ApiModelProperty("是否引用范本 false 否 true 是")
    private Boolean referenceModel;
    @ApiModelProperty("范本名称")
    private String tempName;
    @ApiModelProperty("关联收入类合同")
    private String incomeContract;
    @ApiModelProperty("合同文本")
    private String contractText;
    @ApiModelProperty(value = "合同文本名称",hidden = true)
    private String contractTextName;
    @ApiModelProperty("合同附件")
    private String contractEnclosure;
    @ApiModelProperty("其他说明文件")
    private String otherDocuments;
    @ApiModelProperty("审核状态 0 未提交 1 通过  2 审批中 3 已驳回")
    private Integer reviewStatus;
    @ApiModelProperty("审批流id")
    private Long procId;
    @ApiModelProperty("经办人ID")
    private String handlerId;
    @ApiModelProperty("经办人")
    private String handledBy;
    @ApiModelProperty("经办人所在部门")
    private String orgIds;
   /* @ApiModelProperty("经办人所在部门")
    private List<Long> orgIds;*/
    @ApiModelProperty("是否保证金 false 否 true 是")
    private Boolean bond;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("保证金类型")
    private Integer bondType;
    @ApiModelProperty("是否框架合同 false 否 true 是")
    private Boolean frameworkContract;
    @ApiModelProperty("保证金计划列表")
    private List<ContractBondPlanV> contractBondPlanV;
    @ApiModelProperty("收款计划列表")
    private List<ContractCollectionPlanV> contractCollectionPlanV;
    @ApiModelProperty("合同文本FileVo")
    private FileVo contractTextFileVo;
    @ApiModelProperty("合同附件FileVo")
    private List<FileVo> contractEnclosureFileVo;
    @ApiModelProperty("其他说明文件FileVo")
    private List<FileVo> otherDocumentsFileVo;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("实际履约金额（本币含税）")
    private BigDecimal contractAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("关联招投标保证金账单id")
    private Long bidBondBillId;
    @ApiModelProperty("关联招投标保证金账单编号")
    private String bidBondBillNo;
    @ApiModelProperty("项目来源")
    private String projectSource;
    @ApiModelProperty("业态")
    private String businessType;
    @ApiModelProperty("物业模式 0 包干制 1酬金制")
    private String propertyModel;
    @ApiModelProperty("物业费单价")
    private BigDecimal propertyFeePrice;
}
