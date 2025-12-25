package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractDetailsV {

    @ApiModelProperty("合同id")
    private Long id;
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
    @ApiModelProperty("父级id")
    private Long pid;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String PartyBName;
    @ApiModelProperty("丙方名称")
    private String PartyCName;
    @ApiModelProperty("甲方Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partyAId;
    @ApiModelProperty("乙方Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partyBId;
    @ApiModelProperty("丙方Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partyCId;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("合同分类名称")
    private String categoryName;
    @ApiModelProperty("合同分类树形路径")
    private List<Long> categoryPath;
    @ApiModelProperty("合同性质 1 收入 2 支出 3 其他")
    private Integer contractNature;
    @ApiModelProperty("合同用印 1合同专用章 2公司公章")
    private Integer sealType;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("原币金额（含税）")
    private BigDecimal originalCurrency;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("汇率")
    private String exchangeRate;
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
    @ApiModelProperty("关联合同id（当合同为虚拟合同时，该字段有值）")
    private Long relationContract;
    @ApiModelProperty("关联合同编号")
    private String relationContractNo;
    @ApiModelProperty("关联合同名称（当合同为虚拟合同时，该字段有值）")
    private String relationContractName;
    @ApiModelProperty("是否倒签合同 false 否 true 是")
    private Boolean backdatingContract;
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 3 终止 4 终止中")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止")
    private Integer contractState;
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
    private List<ContractNameV> expenditureContractList;
    private String expenditureContract;
    @ApiModelProperty("合同内容摘要")
    private String contractAbstract;
    @ApiModelProperty("是否引用范本 false 否 true 是")
    private Boolean referenceModel;
    @ApiModelProperty("范本名称")
    private String tempName;
    @ApiModelProperty("范本Filekey")
    private String tempFilekey;
    @ApiModelProperty("不使用范本原因")
    private String noTempReason;
    @ApiModelProperty("强制终止合同原因")
    private String reason;
    @ApiModelProperty("范本id")
    private Long tempId;
    @ApiModelProperty("关联收入类合同")
    private List<ContractNameV> incomeContractList;
    private String incomeContract;
    @ApiModelProperty("合同文本")
    private String contractText;
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
    private String orgId;
    @ApiModelProperty("经办人所在部门")
    private List<Long> orgIds;
    @ApiModelProperty("是否保证金 false 否 true 是")
    private Boolean bond;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("发票类型")
    private String invoiceType;
    @ApiModelProperty("保证金类型0 收取类 1缴纳类")
    private Integer bondType;
    @ApiModelProperty("是否框架合同 false 否 true 是")
    private Boolean frameworkContract;
    @ApiModelProperty("保证金计划列表")
    private List<ContractBondPlanV> contractBondPlanV;
    @ApiModelProperty("收款计划列表")
    private List<ContractCollectionPlanV> collectionPlanVList;
    @ApiModelProperty("合同文本FileVo")
    private FileVo contractTextFileVo;
    @ApiModelProperty("合同附件FileVo")
    private List<FileVo> contractEnclosureFileVo;
    @ApiModelProperty("其他说明文件FileVo")
    private List<FileVo> otherDocumentsFileVo;
    @ApiModelProperty("损益计划列表")
    private List<ContractProfitLossPlanV> profitLossPlanList;
    @ApiModelProperty("实际履约金额（本币含税）")
    private BigDecimal contractAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("关联招投标保证金账单id")
    private Long bidBondBillId;
    @ApiModelProperty("关联招投标保证金账单编号")
    private String bidBondBillNo;
    @ApiModelProperty("原合同id(仅续约合同有值)")
    private Long originalContractId;
    @ApiModelProperty("原合同名称(仅续约合同有值)")
    private String originalContractName;
    @ApiModelProperty("已收/付款金额总和")
    private BigDecimal totalPaymentAmount;
    @ApiModelProperty("已开票/收票金额总和")
    private BigDecimal totalInvoiceAmount;
    @ApiModelProperty("收款明细列表")
    private List<CollectionDetailPlanV> collectionDetailPlanVList;
    @ApiModelProperty("付款明细列表")
    private List<ContractPaymentDetailV> contractPaymentDetailVList;
    @ApiModelProperty("开票明细列表")
    private List<InvoiceDetailPlanV> invoiceDetailPlanVList;
    @ApiModelProperty("收票明细列表")
    private List<ContractReceiveInvoiceDetailV> receiveInvoiceDetailVList;
    @ApiModelProperty("税种税率id")
    private String taxRateIdPath;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    @ApiModelProperty("业务类型 1.工程施工类")
    private Integer natureType;
    @ApiModelProperty("是否自动生成损益（工程计提情况下）0 否 1 是")
    private Integer isNotProfit = 1;
    @ApiModelProperty("创建人Id")
    private String creator;
    @ApiModelProperty("空间资源列表")
    private List<ContractSpaceResourcesV> spaceResourcesV;
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
