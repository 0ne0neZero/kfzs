package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
/**
 * <p>
 * 收入合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude")
public class ContractIncomeConcludeE {

    /**
     * 合同id
     */
    @TableId(value = ID)
    private String id;


    /**
     * 中交合同id
     */
    private String fromid;

    /**
     * 中交合同主数据编码
     */
    private String conmaincode;

    /**
     * 中交合同类型业务码
     */
    private String bizCode;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 推送状态(0.失败；1.成功，2.待推送)
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer contractNature;

    /**
     * 合同业务分类Id
     */
    private Long categoryId;

    /**
     * 合同业务分类path
     */
    private String categoryPath;

    /**
     * 合同业务分类名称
     */
    private String categoryName;

    /**
     * 关联主合同Id
     */
    private String pid;

    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    private Integer contractType;

    /**
     * 无支付义务类是 1  否 0
     */
    private Integer noPaySign;

    /**
     * 甲方ID 收入类-取客户
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String partyAId;

    /**
     * 乙方ID 收入类-取供应商
     */
    private String partyBId;

    /**
     * 甲方名称
     */
    private String partyAName;

    /**
     * 乙方名称
     */
    private String partyBName;

    /**
     * 所属公司ID
     */
    private String orgId;

    /**
     * 所属部门ID
     */
    private String departId;

    /**
     * 所属部门名称
     */
    private String departName;

    /**
     * 所属公司名称
     */
    private String orgName;

    /**
     * 签约人名称
     */
    private String signPerson;

    /**
     * 签约人ID
     */
    private String signPersonId;

    /**
     * 签约日期
     */
    private LocalDate signDate;

    /**
     * 成本中心ID
     */
    private String costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 所属项目ID 来源 成本中心
     */
    private String communityId;

    /**
     * 所属项目名称 来源 成本中心
     */
    private String communityName;

    /**
     * 负责人ID
     */
    private String principalId;

    /**
     * 交易类型ID 1关联交易  2非关联交易
     */
    private String dealTypeId;

    /**
     * 交易类型 1关联交易  2非关联交易
     */
    private String dealType;

    /**
     * 用印类型  1合同专用章 2公司公章
     */
    private Integer sealType;

    /**
     * 用印类型  1合同专用章 2公司用章
     */
    private String sealTypeName;

    /**
     * 负责人名称
     */
    private String principalName;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 变更后合同履约金额
     */
    private BigDecimal changContractAmount;

    /**
     * 是否保证金 0 否 1 是
     */
    private Boolean bond;

    /**
     * 保证金额
     */
    private BigDecimal bondAmount;

    /**
     * 收款金额
     */
    private BigDecimal collectAmount;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 合同开始日期
     */
    private LocalDate gmtExpireStart;

    /**
     * 合同到期日期
     */
    private LocalDate gmtExpireEnd;

    /**
     * 是否引用范本  (0 否   1 是)
     */
    private Integer isTemp;

    /**
     * 范本ID
     */
    private String tempId;

    /**
     * 是否倒签 0 否  1 是
     */
    private Integer isBackDate;

    /**
     * 范本的filekey
     */
    private String tempFilekey;

    /**
     * 签约方式 0 新签 1 补充协议 2 续签 
     */
    private Integer signingMethod;

    /**
     * 合同预警状态 0正常 1 临期 2 已到期
     */
    private Integer warnState;

    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 
     */
    private Integer reviewStatus;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 租户id
     */
    private String tenantId;

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
    @TableLogic
    private Integer deleted;



    /**
     * 对方单位1-ID
     */
    private String oppositeOneId;

    /**
     *对方单位1-名称
     */
    private String oppositeOne;

    /**
     *对方单位2-ID
     */
    private String oppositeTwoId;

    /**
     *对方单位2-名称
     */
    private String oppositeTwo;

    /**
     *我方单位-ID
     */
    private String ourPartyId;

    /**
     *我方单位-名称
     */
    private String ourParty;

    /**
     *币种编码
     */
    private String currencyCode;

    /**
     *币种
     */
    private String currency;

    /**
     *汇率
     */
    private String exRate;

    /**
     *合同总额 原币含税
     */
    private BigDecimal contractAmountOriginalRate;

    /**
     *合同总额 原币不含税
     */
    private BigDecimal contractAmountOriginal;

    /**
     *合同总额 本币含税
     */
    private BigDecimal contractAmountLocalRate;

    /**
     *合同总额 本币不含税
     */
    private BigDecimal contractAmountLocal;

    /**
     *结算金额 原币含税
     */
    private BigDecimal settleAmountRate;

    /**
     *结算金额 原币不含税
     */
    private BigDecimal settleAmount;

    /**
     *备注
     */
    private String remark;

    /**
     *区域
     */
    private String region;

    /**
     *开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 是否为反审(0 否   1 是)
     */
    private Integer isBackReview;

    /**
     * 终止日期
     */
    private LocalDate endDate;

    /**
     * 分成比例
     */
    private BigDecimal companyRate;

    /**
     * 是否分成(0否1是)
     */
    private Integer ownerRate;

    /**
     * 合同服务类型
     */
    private Integer contractServeType;

    /**
     * 是否归档
     */
    private Integer archived;

    /**
     * 审核通过时间
     */
    private LocalDateTime approvalDate;

    /**
     * 合同推送时间
     */
    private LocalDateTime contractPushDate;
    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;
    //是否修正后编辑收款计划（1.是，0.否）
    private Integer isCorrectionAndPlan;

    public static final String FROMID = "fromid";

    public static final String OPPOSITE_ONE_ID = "oppositeOneId";

    public static final String OPPOSITE_ONE = "oppositeOne";

    public static final String OPPOSITE_TWO_ID = "oppositeTwoId";

    public static final String OPPOSITE_TWO = "oppositeTwo";

    public static final String OUR_PARTY_ID = "ourPartyId";

    public static final String OUR_PARTY = "ourParty";

    public static final String CURRENCY_CODE = "currencyCode";

    public static final String CURRENCY = "currency";

    public static final String EXRATE = "exRate";

    public static final String CONTRACT_AMOUNT_ORIGINAL_RATE = "contractAmountOriginalRate";

    public static final String CONTRACT_AMOUNT_ORIGINAL = "contractAmountOriginal";

    public static final String CONTRACT_AMOUNT_LOCAL_RATE = "contractAmountLocalRate";

    public static final String CONTRACT_AMOUNT_LOCAL = "contractAmountLocal";

    public static final String SETTLE_AMOUNT_RATE = "settleAmountRate";

    public static final String SETTLE_AMOUNT = "settleAmount";

    public static final String REMARK = "remark";

    public static final String INVOICE_AMOUNT = "invoiceAmount";

    public static final String IS_BACK_REVIEW = "isBackReview";

    public static final String END_DATE = "endDate";

    public static final String REGION = "region";



    /**
     * 中交合同编码
     */
    public static final String BIZCODE = "bizCode";


    /**
    * 合同id
    */
    public static final String ID = "id";

    /**
    * 合同名称
    */
    public static final String NAME = "name";

    /**
    * 合同编号
    */
    public static final String CONTRACT_NO = "contractNo";

    /**
    * 合同性质 1虚拟合同 0非虚拟合同
    */
    public static final String CONTRACT_NATURE = "contractNature";

    /**
    * 合同业务分类Id
    */
    public static final String CATEGORY_ID = "categoryId";

    /**
    * 合同业务分类名称
    */
    public static final String CATEGORY_NAME = "categoryName";

    /**
    * 关联主合同Id
    */
    public static final String PID = "pid";

    /**
    * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
    */
    public static final String CONTRACT_TYPE = "contractType";

    /**
    * 甲方ID 收入类-取客户
    */
    public static final String PARTY_AID = "partyAId";

    /**
    * 乙方ID 收入类-取供应商
    */
    public static final String PARTY_BID = "partyBId";

    /**
    * 甲方名称
    */
    public static final String PARTY_ANAME = "partyAName";

    /**
    * 乙方名称
    */
    public static final String PARTY_BNAME = "partyBName";

    /**
    * 所属公司ID
    */
    public static final String ORG_ID = "orgId";

    /**
    * 所属部门ID
    */
    public static final String DEPART_ID = "departId";

    /**
    * 所属部门名称
    */
    public static final String DEPART_NAME = "departName";

    /**
    * 所属公司名称
    */
    public static final String ORG_NAME = "orgName";

    /**
    * 签约人名称
    */
    public static final String SIGN_PERSON = "signPerson";

    /**
    * 签约人ID
    */
    public static final String SIGN_PERSON_ID = "signPersonId";

    /**
    * 签约日期
    */
    public static final String SIGN_DATE = "signDate";

    /**
    * 成本中心ID
    */
    public static final String COST_CENTER_ID = "costCenterId";

    /**
    * 成本中心名称
    */
    public static final String COST_CENTER_NAME = "costCenterName";

    /**
    * 所属项目ID 来源 成本中心
    */
    public static final String COMMUNITY_ID = "communityId";

    /**
    * 所属项目名称 来源 成本中心
    */
    public static final String COMMUNITY_NAME = "communityName";

    /**
    * 负责人ID
    */
    public static final String PRINCIPAL_ID = "principalId";

    /**
    * 交易类型ID 1关联交易  2非关联交易
    */
    public static final String DEAL_TYPE_ID = "dealTypeId";

    /**
    * 交易类型 1关联交易  2非关联交易
    */
    public static final String DEAL_TYPE = "dealType";

    /**
    * 用印类型  1合同专用章 2公司公章
    */
    public static final String SEAL_TYPE = "sealType";

    /**
    * 用印类型  1合同专用章 2公司用章
    */
    public static final String SEAL_TYPE_NAME = "sealTypeName";

    /**
    * 负责人名称
    */
    public static final String PRINCIPAL_NAME = "principalName";

    /**
    * 合同金额
    */
    public static final String CONTRACT_AMOUNT = "contractAmount";

    /**
    * 是否保证金 0 否 1 是
    */
    public static final String BOND = "bond";

    /**
    * 保证金额
    */
    public static final String BOND_AMOUNT = "bondAmount";

    /**
    * 付款金额
    */
    public static final String PAY_AMOUNT = "payAmount";

    /**
    * 合同开始日期
    */
    public static final String GMT_EXPIRE_START = "gmtExpireStart";

    /**
    * 合同到期日期
    */
    public static final String GMT_EXPIRE_END = "gmtExpireEnd";

    /**
    * 范本ID
    */
    public static final String TEMP_ID = "tempId";

    /**
    * 是否倒签 0 否  1 是
    */
    public static final String IS_BACK_DATE = "isBackDate";

    /**
    * 范本的filekey
    */
    public static final String TEMP_FILEKEY = "tempFilekey";

    /**
    * 签约方式 0 新签 1 补充协议 2 续签 
    */
    public static final String SIGNING_METHOD = "signingMethod";

    /**
    * 合同预警状态 0正常 1 临期 2 已到期
    */
    public static final String WARN_STATE = "warnState";

    /**
    * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 
    */
    public static final String REVIEW_STATUS = "reviewStatus";

    /**
    * 合同状态
    */
    public static final String STATUS = "status";

    /**
    * 租户id
    */
    public static final String TENANT_ID = "tenantId";

    /**
    * 创建人
    */
    public static final String CREATOR = "creator";

    /**
    * 创建人名称
    */
    public static final String CREATOR_NAME = "creatorName";

    /**
    * 创建时间
    */
    public static final String GMT_CREATE = "gmtCreate";

    /**
    * 操作人
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人名称
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
    * 操作时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
    * 是否删除  0 正常 1 删除
    */
    public static final String DELETED = "deleted";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    FROMID,
                                    BIZCODE,
                                    NAME,
                                    CONTRACT_NO,
                                    CONTRACT_NATURE,
                                    CATEGORY_ID,
                                    CATEGORY_NAME,
                                    PID,
                                    CONTRACT_TYPE,
                                    PARTY_AID,
                                    PARTY_BID,
                                    PARTY_ANAME,
                                    PARTY_BNAME,
                                    ORG_ID,
                                    DEPART_ID,
                                    DEPART_NAME,
                                    ORG_NAME,
                                    SIGN_PERSON,
                                    SIGN_PERSON_ID,
                                    SIGN_DATE,
                                    COST_CENTER_ID,
                                    COST_CENTER_NAME,
                                    COMMUNITY_ID,
                                    COMMUNITY_NAME,
                                    PRINCIPAL_ID,
                                    DEAL_TYPE_ID,
                                    DEAL_TYPE,
                                    SEAL_TYPE,
                                    SEAL_TYPE_NAME,
                                    PRINCIPAL_NAME,
                                    CONTRACT_AMOUNT,
                                    BOND,
                                    BOND_AMOUNT,
                                    PAY_AMOUNT,
                                    GMT_EXPIRE_START,
                                    GMT_EXPIRE_END,
                                    TEMP_ID,
                                    IS_BACK_DATE,
                                    TEMP_FILEKEY,
                                    SIGNING_METHOD,
                                    WARN_STATE,
                                    REVIEW_STATUS,
                                    STATUS,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
