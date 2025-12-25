package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@TableName("contract_conclude")
public class ContractConcludeE {

    /**
     * 合同id
     */
    private Long id;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 公司id 来源 org_tenant.id
     */
    private String tenantId;

    /**
     * 项目id 来源 成本中心
     */
    private String communityId;

    /**
     * 项目名称 来源 成本中心名称
     */
    private String communityName;

    /**
     * 所属部门ID
     */
    private String belongOrgId;

    /**
     * 所属部门名称
     */
    private String belongOrgName;

    /**
     * 所属部门组织路径
     */
    private String belongOrgPath;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * pid
     */
    private Long pid;

    /**
     * 甲方名称
     */
    private Long partyAId;

    /**
     * 乙方名称
     */
    private Long PartyBId;

    /**
     * 丙方名称
     */
    private Long PartyCId;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 币种
     */
    private String currency;

    /**
     * 汇率
     */
    private String exchangeRate;
    /**
     * 合同分类Id
     */
    private Long categoryId;
    /**
     * 原合同id(仅续约合同有值)
     */
    private Long originalContractId;

    /**
     * 合同性质 1 收入 2 支出
     */
    private Integer contractNature;

    /**
     * 税种税率id
     */
    private String taxRateIdPath;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 业务类型 1.工程施工类
     */
    private Integer natureType;

    /**
     * 是否自动生成损益（工程计提情况下）0 否 1 是
     */
    private Integer isNotProfit;

    /**
     * 本币金额（含税）
     */
    private BigDecimal amountTaxIncluded;

    /**
     * 本币金额（不含税）
     */
    private BigDecimal amountTaxExcluded;

    /**
     * 原币金额（含税）
     */
    private BigDecimal originalCurrency;

    /**
     * 合同预估金额
     */
    private BigDecimal estimatedAmount;

    /**
     * 合同生效日期
     */
    private LocalDate gmtExpireStart;

    /**
     * 合同结束日期
     */
    private LocalDate gmtExpireEnd;

    /**
     * 是否虚拟合同 0 否 1 是
     */
    private Boolean virtualContract;

    /**
     * 关联合同（当合同为虚拟合同时，该字段有值）
     */
    private Long relationContract;

    /**
     * 关联合同编号
     */
    private String relationContractNo;

    /**
     * 是否倒签合同 0 否 1 是
     */
    private Boolean backdatingContract;

    /**
     * 签约方式
     */
    private Integer signingMethod;

    /**
     * 合同状态 1 未履行 2 履行中 3 已到期 4 已终止
     */
    private Integer contractState;

    /**
     * 合同预警状态 0正常 1 临期 2 已到期
     */
    private Integer warnState;

    /**用印类型 1合同专用章 2公司公章
     */
    private Integer sealType;

    /**
     * 保证金额原币
     */
    private BigDecimal bondAmount;

    /**
     * 保证金额本币
     */
    private BigDecimal bondHomeCurrency;

    /**
     * 是否关联招投保证金（收入类为招标保证金，支出类为投标）
     */
    private Boolean bidBond;

    /**
     * 招投保证金名称
     */
    private String bidBondName;

    /**
     * 招投保证金金额
     */
    private BigDecimal bidBondAmount;

    /**
     * 关联支出类合同
     */
    private String expenditureContract;

    /**
     * 合同内容摘要
     */
    private String contractAbstract;

    /**
     * 是否引用范本 0 否 1 是
     */
    private Boolean referenceModel;

    /**
     * 范本id
     */
    private Long tempId;

    /**
     * 范本Filekey
     */
    private String tempFilekey;

    /**
     * 不使用范本原因
     */
    private String noTempReason;

    /**
     * 强制终止合同原因
     */
    private String reason;

    /**
     * 关联收入类合同
     */
    private String incomeContract;

    /**
     * 合同文本
     */
    private String contractText;

    /**
     * 合同文本名称
     */
    private String contractTextName;

    /**
     * 合同附件名称
     */
    private String enclosureName;

    /**
     * 其他说明文件名称
     */
    private String otherDocName;

    /**
     * 合同附件
     */
    private String contractEnclosure;

    /**
     * 其他说明文件
     */
    private String otherDocuments;

    /**
     * 审核状态 1 通过 2 未提交 3 审批中 4 已驳回
     */
    private Integer reviewStatus;

    /**
     * 审批流id
     */
    private Long procId;

    /**
     * 经办人ID
     */
    private String handlerId;
    /**
     * 经办人
     */
    private String handledBy;

    /**
     * 经办人所在部门
     */
    private String orgId;

    /**
     * 是否保证金 0 否 1 是
     */
    private Boolean bond;

    /**
     * 费项Id
     */
    private Long chargeItemId;

    /**
     * 保证金类型
     */
    private Integer bondType;

    /**
     * 是否框架合同 0 否 1 是
     */
    private Boolean frameworkContract;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    private Integer deleted;

    /**
     * 实际履约金额（本币含税）
     */
    private BigDecimal contractAmount;

    /**
     * 减免金额
     */
    private BigDecimal creditAmount;

    /**
     * 关联招投标保证金账单id
     */
    private Long bidBondBillId;

    /**
     * 关联招投标保证金账单编号
     */
    private String bidBondBillNo;
    /**
     * 项目来源
     */
    private String projectSource;

    /**
     * 业态
     */
    private String businessType;

    /**
     * 物业模式
     */
    private String propertyModel;

    /**
     * 物业费单价
     */
    private BigDecimal propertyFeePrice;

}
