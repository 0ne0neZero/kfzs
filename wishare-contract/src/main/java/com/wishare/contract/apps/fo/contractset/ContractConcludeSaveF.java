package com.wishare.contract.apps.fo.contractset;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 合同订立信息表 保存请求参数
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractConcludeSaveF {

    @ApiModelProperty(value = "合同名称",required = true)
    @NotNull(message = "合同名称不能为空！")
    private String name;
    @ApiModelProperty(value = "公司id 来源 org_tenant.id",required = true)
    private String tenantId;
    @ApiModelProperty(value = "项目id 来源 成本中心",required = true)
    private String communityId;
    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty(value = "所属部门ID", required = true)
    @NotBlank(message = "所属部门ID不能为空！")
    private String belongOrgId;
    @ApiModelProperty("所属部门名称")
    private String belongOrgName;
    @ApiModelProperty("所属部门组织路径")
    private String belongOrgPath;
    @ApiModelProperty(value = "合同编号")
    private String contractNo;
    @ApiModelProperty("父级id")
    private Long pid = 0L;
    @ApiModelProperty(value = "甲方名称",required = true)
    @NotNull(message = "甲方名称不能为空！")
    private Long partyAId;
    @ApiModelProperty(value = "乙方名称",required = true)
    @NotNull(message = "乙方名称不能为空！")
    private Long PartyBId;
    @ApiModelProperty("丙方名称")
    private Long PartyCId;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("发票类型")
    private String invoiceType;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("汇率")
    private String exchangeRate;
    @ApiModelProperty("合同性质 1 收入 2 支出 3 其他")
    private Integer contractNature;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("原币金额（含税）")
    private BigDecimal originalCurrency;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("是否虚拟合同 false 否 true 是")
    private Boolean virtualContract;
    @ApiModelProperty("关联合同（当合同为虚拟合同时，该字段有值）")
    private Long relationContract;
    @ApiModelProperty("关联合同编号")
    private String relationContractNo;
    @ApiModelProperty("是否倒签合同 false 否 true 是")
    private Boolean backdatingContract;
    @ApiModelProperty(value = "签约方式 0 新签 1 补充协议 2 续签 3 终止 4 终止中",required = true)
    @NotNull(message = "签约方式不能为空！")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止")
    private Integer contractState;
    @ApiModelProperty("合同用印 1合同专用章 2公司公章")
    private Integer sealType;
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
    @ApiModelProperty("不使用范本原因")
    private String noTempReason;
    @ApiModelProperty("范本id")
    private Long tempId;
    @ApiModelProperty("关联收入类合同")
    private String incomeContract;
    @ApiModelProperty("合同文本")
    private String contractText;
    @ApiModelProperty("范本Filekey")
    private String tempFilekey;
    @ApiModelProperty("合同附件")
    private String contractEnclosure;
    @ApiModelProperty("合同文本FileVo")
    private FileVo contractTextFileVo;
    @ApiModelProperty("合同附件FileVo")
    private List<FileVo> contractEnclosureFileVo;
    @ApiModelProperty(value = "合同文本名称",hidden = true)
    private String contractTextName;
    @ApiModelProperty(value = "合同附件名称",hidden = true)
    private String enclosureName;
    @ApiModelProperty(value = "其他说明文件名称",hidden = true)
    private String otherDocName;
    @ApiModelProperty("其他说明文件")
    private String otherDocuments;
    @ApiModelProperty("其他说明文件FileVo")
    private List<FileVo> otherDocumentsFileVo;
    @ApiModelProperty("审核状态 0 未提交 1 通过  2 审批中 3 已驳回")
    private Integer reviewStatus;
    @ApiModelProperty("审批流id")
    private Long procId;
    @ApiModelProperty("经办人ID")
    private String handlerId;
    @ApiModelProperty("经办人")
    private String handledBy;
    @ApiModelProperty(value = "经办人所在部门",required = true)
    @NotNull(message = "经办人所在部门不能为空！")
    private String orgId;
    @ApiModelProperty("是否保证金 false 否 true 是")
    private Boolean bond;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("保证金类型0 收取类 1缴纳类")
    private Integer bondType;
    @ApiModelProperty("是否框架合同 false 否 true 是")
    private Boolean frameworkContract;
    @ApiModelProperty("收款计划")
    private List<ContractCollectionPlanSaveF> collectionPlanSaveFList;
    @ApiModelProperty("保证金计划")
    private List<ContractBondPlanSaveF> bondPlanSaveFList;
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
    @ApiModelProperty("税种税率id")
    private String taxRateIdPath;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    @ApiModelProperty("业务类型 1.工程施工类")
    private Integer natureType;
    @ApiModelProperty("删除状态 0 否 1 是")
    private Integer deleted;
    @ApiModelProperty("空间资源列表")
    private List<ContractSpaceResourcesSaveF> contractSpaceResourcesF;
    @ApiModelProperty("项目来源")
    private String projectSource;
    @ApiModelProperty("业态")
    private String businessType;
    @ApiModelProperty("物业模式 0 包干制 1酬金制")
    private String propertyModel;
    @ApiModelProperty("物业费单价")
    private BigDecimal propertyFeePrice;
}
