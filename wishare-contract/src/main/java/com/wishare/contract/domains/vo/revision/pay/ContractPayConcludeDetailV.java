package com.wishare.contract.domains.vo.revision.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.ContractQydwsF;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.domains.enums.revision.ServeTypeEnum;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  16:17
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息表视图对象detail", description = "支出合同订立信息表视图对象detail")
public class ContractPayConcludeDetailV {

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String id;
    /**
     * 合同详情页面传参ID
     */
    @ApiModelProperty("合同详情页面传参ID")
    private String detailTableId;

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
     *区域
     */
    private String region;
    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String name;
    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String contractNo;
    /**
     * 合同性质 1虚拟合同 0非虚拟合同
     */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    /**
     * 合同性质 1虚拟合同 0非虚拟合同
     */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private String contractNatureName;
    /**
     * 无支付义务类是 1  否 0
     */
    @ApiModelProperty("无支付义务类是 1  否 0")
    private Integer noPaySign;
    /**
     * 合同业务分类Id
     */
    @ApiModelProperty("合同业务分类Id")
    private Long categoryId;
    /**
     * 合同业务分类path
     */
    @ApiModelProperty("合同业务分类path")
    private String categoryPath;
    /**
     * 合同业务分类pathList
     */
    @ApiModelProperty("合同业务分类path")
    private List<Long> categoryPathList;
    /**
     * 合同业务分类名称
     */
    @ApiModelProperty("合同业务分类名称")
    private String categoryName;
    /**
     * 关联主合同Id
     */
    @ApiModelProperty("关联主合同Id")
    private String pid;
    /**
     * 关联主合同名称
     */
    @ApiModelProperty("关联主合同名称")
    private String pidName;
    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private String contractTypeName;
    /**
     * 甲方ID 支出类-取客户
     */
    @ApiModelProperty("甲方ID 支出类-取客户")
    private String partyAId;
    /**
     * 乙方ID 支出类-取供应商
     */
    @ApiModelProperty("乙方ID 支出类-取供应商")
    private String partyBId;
    /**
     * 甲方名称
     */
    @ApiModelProperty("甲方名称")
    private String partyAName;
    /**
     * 乙方名称
     */
    @ApiModelProperty("乙方名称")
    private String partyBName;
    /**
     * 所属公司ID
     */
    @ApiModelProperty("所属公司ID")
    private String orgId;
    /**
     * 所属部门ID
     */
    @ApiModelProperty("所属部门ID")
    private String departId;
    /**
     * 所属部门名称
     */
    @ApiModelProperty("所属部门名称")
    private String departName;
    /**
     * 所属公司名称
     */
    @ApiModelProperty("所属公司名称")
    private String orgName;
    /**
     * 签约人名称
     */
    @ApiModelProperty("签约人名称")
    private String signPerson;
    /**
     * 签约人ID
     */
    @ApiModelProperty("签约人ID")
    private String signPersonId;
    /**
     * 签约日期
     */
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate signDate;
    /**
     * 成本中心ID
     */
    @ApiModelProperty("成本中心ID")
    private String costCenterId;
    /**
     * 成本中心名称
     */
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    /**
     * 所属项目ID 来源 成本中心
     */
    @ApiModelProperty("所属项目ID 来源 成本中心")
    private String communityId;
    /**
     * 所属项目名称 来源 成本中心
     */
    @ApiModelProperty("所属项目名称 来源 成本中心")
    private String communityName;
    /**
     * 负责人ID
     */
    @ApiModelProperty("负责人ID")
    private String principalId;
    /**
     * 负责人名称
     */
    @ApiModelProperty("负责人名称")
    private String principalName;
    /**
     * 合同金额
     */
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    /**
     * 主合同金额
     */
    @ApiModelProperty("主合同金额")
    private BigDecimal mainContractAmount;
    /**
     * 主合同金额
     */
    @ApiModelProperty("主合同变更后金额")
    private BigDecimal mainContractBjAmount;
    /**
     * 变更后合同履约金额
     */
    @ApiModelProperty("变更后合同履约金额")
    private BigDecimal changContractAmount;
    /**
     * 合同履约金额拼接
     */
    @ApiModelProperty("合同履约金额拼接")
    private String changContractAmountPj;
    /**
     * 补充金额
     */
    @ApiModelProperty("补充金额")
    private BigDecimal supplyContractAmount;
    /**
     * 补充金额拼接
     */
    @ApiModelProperty("补充金额变更拼接")
    private String supplyContractChangeAmountPj;
    /**
     * 补充金额拼接
     */
    @ApiModelProperty("补充金额拼接")
    private String supplyContractAmountPj;
    /**
     * 是否保证金 0 否 1 是
     */
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    /**
     * 保证金额
     */
    @ApiModelProperty("保证金额")
    private BigDecimal bondAmount;
    /**
     * 付款金额
     */
    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;
    /**
     * 未付款金额
     */
    @ApiModelProperty("未付款金额")
    private BigDecimal unPayAmount;
    /**
     * 未收票金额
     */
    @ApiModelProperty("未收票金额")
    private BigDecimal unInvoiceAmount;
    /**
     * 扣款金额
     */
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    /**
     * 合同开始日期
     */
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    /**
     * 合同到期日期
     */
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    /**
     * 是否引用范本  (0 否   1 是)
     */
    @ApiModelProperty("是否引用范本  (0 否   1 是)")
    private Integer isTemp;
    /**
     * 范本ID
     */
    @ApiModelProperty("范本ID")
    private String tempId;
    /**
     * 是否倒签 0 否  1 是
     */
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    /**
     * 范本的filekey
     */
    @ApiModelProperty("范本的filekey")
    private String tempFilekey;
    /**
     * 签约方式 0 新签 1 补充协议 2 续签
     */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;
    /**
     * 签约方式 0 新签 1 补充协议 2 续签
     */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private String signingMethodName;
    /**
     * 合同预警状态 0正常 1 临期 2 已到期
     */
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    @ApiModelProperty("审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 ")
    private Integer reviewStatus;
    @ApiModelProperty("合同状态 0 待提交， 1 审批中， 2 已拒绝， 3 未开始， 4 执行中， 5 已到期， 6 已终止")
    private Integer status;
    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
     * 是否展示编辑按钮
     */
    @ApiModelProperty("是否展示编辑按钮")
    private Boolean showBtnEdit;
    /**
     * 是否展示变更按钮
     */
    @ApiModelProperty("是否展示变更按钮")
    private Boolean showBtnChange;
    /**
     * 是否展示续签按钮
     */
    @ApiModelProperty("是否展示续签按钮")
    private Boolean showBtnRenewal;
    /**
     * 是否展示终止按钮
     */
    @ApiModelProperty("是否展示终止按钮")
    private Boolean showBtnEnd;
    /**
     * 是否展示打印按钮
     */
    @ApiModelProperty("是否展示打印按钮")
    private Boolean showBtnPrint;
    /**
     * 是否展示删除按钮
     */
    @ApiModelProperty("是否展示删除按钮")
    private Boolean showBtnDelete;
    /**
     * 是否展示变更记录按钮
     */
    @ApiModelProperty("是否展示变更记录按钮")
    private Boolean showBtnChangeRecord;
    /**
     * 是否展示反审按钮
     */
    @ApiModelProperty("是否展示反审按钮")
    private Boolean showBtnBackReview;
    /**
     * 费项信息数据列
     */
    @ApiModelProperty("费项信息数据列")
    private String chargeItemShowList;
    /**
     * 税率信息数据列
     */
    @ApiModelProperty("税率信息数据列")
    private String taxRateShowList;

    /**
     * 清单累计金额
     */
    @ApiModelProperty("清单累计金额")
    private BigDecimal fundAmount;

    /**
     * 清单累计金额拼接
     */
    @ApiModelProperty("清单累计金额拼接")
    private String fundAmountPj;


    @ApiModelProperty("对方单位1-ID")
    private String oppositeOneId;
    @ApiModelProperty("对方单位1-名称")
    private String oppositeOne;
    @ApiModelProperty("对方单位2-ID")
    private String oppositeTwoId;
    @ApiModelProperty("对方单位2-名称")
    private String oppositeTwo;
    @ApiModelProperty("我方单位-ID")
    private String ourPartyId;
    @ApiModelProperty("我方单位-名称")
    private String ourParty;
    @ApiModelProperty("币种编码")
    private String currencyCode;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("汇率")
    private String exRate;
    @ApiModelProperty("合同总额 原币含税")
    private BigDecimal contractAmountOriginalRate;
    @ApiModelProperty("合同总额 原币不含税")
    private BigDecimal contractAmountOriginal;
    @ApiModelProperty("合同总额 本币含税")
    private BigDecimal contractAmountLocalRate;
    @ApiModelProperty("合同总额 本币不含税")
    private BigDecimal contractAmountLocal;
    @ApiModelProperty("结算金额 原币含税")
    private BigDecimal settleAmountRate;
    @ApiModelProperty("结算金额 原币不含税")
    private BigDecimal settleAmount;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("是否为反审(0 否   1 是)")
    private Integer isBackReview;
    /**
     * 终止日期
     */
    @ApiModelProperty("终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    // -- 以下为拓展信息 0926版本对接中交合同新字段

    @ApiModelProperty("履约进度")
    private String performschedule;
    /**
     * contractId
     */
    @ApiModelProperty("合同id")
    @Length(message = "合同id不可超过 50 个字符",max = 50)
    private String contractId;
    /**
     * conlanguage
     */
    @ApiModelProperty("合同语言")
    @Length(message = "合同语言不可超过 30 个字符",max = 30)
    private String conlanguage;
    private String conlanguagename;

    /**
     * conmanagetype
     */
    @ApiModelProperty("合同管理类别")
    @Length(message = "合同管理类别不可超过 30 个字符",max = 30)
    private String conmanagetype;
    private String conmanagetypename;

    /**
     * fwlb
     */
    @ApiModelProperty("服务类型")
    @Length(message = "服务类型不可超过 30 个字符",max = 30)
    private String fwlb;
    private String fwlbname;

    /**
     * conincrementype
     */
    @ApiModelProperty("增值业务类型")
    @Length(message = "增值业务类型不可超过 30 个字符",max = 30)
    private String conincrementype;
    private String conincrementypename;

    @ApiModelProperty("是否满足集团招商刻度")
    private Integer fmzjtzskd;

    /**
     * conname
     */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 450 个字符",max = 450)
    private String conname;
    /**
     * issupplycontract
     */
    @ApiModelProperty("是否补充合同")
    @Length(message = "是否补充合同不可超过 30 个字符",max = 30)
    private String issupplycontract;
    /**
     * oppositeconmaincode
     */
    @ApiModelProperty("内部合同关联校验码")
    @Length(message = "内部合同关联校验码不可超过 100 个字符",max = 100)
    private String oppositeconmaincode;
    /**
     * incomeexpendtype
     */
    @ApiModelProperty("收支类型")
    @Length(message = "收支类型不可超过 30 个字符",max = 30)
    private String incomeexpendtype;
    /**
     * conperformcountry
     */
    @ApiModelProperty("合同履行地(国家)")
    @Length(message = "合同履行地(国家)不可超过 30 个字符",max = 30)
    private String conperformcountry;
    /**
     * conperformprovinces
     */
    @ApiModelProperty("合同履行地(省)")
    @Length(message = "合同履行地(省)不可超过 30 个字符",max = 30)
    private String conperformprovinces;
    /**
     * conperformcity
     */
    @ApiModelProperty("合同履行地(市)")
    @Length(message = "合同履行地(市)不可超过 30 个字符",max = 30)
    private String conperformcity;
    /**
     * conperformxian
     */
    @ApiModelProperty("合同履行地(县)")
    @Length(message = "合同履行地(县)不可超过 30 个字符",max = 30)
    private String conperformxian;
    /**
     * disputesolutionway
     */
    @ApiModelProperty("争议解决方式")
    @Length(message = "争议解决方式不可超过 30 个字符",max = 30)
    private String disputesolutionway;
    /**
     * conmodelusecondition
     */
    @ApiModelProperty("合同范本使用情况")
    @Length(message = "合同范本使用情况不可超过 30 个字符",max = 30)
    private String conmodelusecondition;
    /**
     * conundertakerid
     */
    @ApiModelProperty("合同承办人id")
    @Length(message = "合同承办人id不可超过 30 个字符",max = 30)
    private String conundertakerid;
    /**
     * conundertaker
     */
    @ApiModelProperty("合同承办人姓名")
    @Length(message = "合同承办人姓名不可超过 100 个字符",max = 100)
    private String conundertaker;
    /**
     * conundertakercontact
     */
    @ApiModelProperty("合同承办人联系方式")
    @Length(message = "合同承办人联系方式不可超过 100 个字符",max = 100)
    private String conundertakercontact;
    /**
     * taxrate
     */
    @ApiModelProperty("税率")
    @Digits(integer = 20,fraction =2,message = "税率不正确")
    private BigDecimal taxrate;
    /**
     * taxamt
     */
    @ApiModelProperty("税额")
    @Digits(integer = 20,fraction =2,message = "税额不正确")
    private BigDecimal taxamt;
    /**
     * hsbgamt
     */
    @ApiModelProperty("合同变更后总金额（含税）")
    @Digits(integer = 20,fraction =2,message = "合同变更后总金额（含税）不正确")
    private BigDecimal hsbgamt;

    @ApiModelProperty("合同对方信息")

    private List<ContractDfxxF> htdfxx;
    /**
     * fkdwxx
     */
    @ApiModelProperty("(合同支付信息:支付方信息)")
    private List<ContractZffxxF> fkdwxx;
    @ApiModelProperty("支出方名称str")
    private String zcfstr;
    @ApiModelProperty("应付合同金额str")
    private String yfhtjestr;
    @ApiModelProperty("实际付款人str")
    private String sjfkrstr;

    /**
     * skdwxx
     */
    @ApiModelProperty("(合同支付信息:收入方信息)")
    private List<ContractSrfxxF> skdwxx;
    @ApiModelProperty("收入方名称str")
    private String srfstr;
    @ApiModelProperty("收入方账户类型str")
    private String zhlxstr;
    @ApiModelProperty("收入方账户-账户名称str")
    private String zhmcstr;
    @ApiModelProperty("收入方账户-账号str")
    private String zhstr;
    @ApiModelProperty("开户行str")
    private String khhstr;
    @ApiModelProperty("账户所在国家str")
    private String zhszgj;
    @ApiModelProperty("swiftcodestr")
    private String swiftcodestr;
    @ApiModelProperty("应收合同金额str")
    private String yshtjestr;
    @ApiModelProperty("实际收款人str")
    private String sjskrstr;

    @ApiModelProperty("签约单位信息")
    private List<ContractQydwsF> qydws;

    /**
     * bzjxx
     */
    @ApiModelProperty("(保证金信息)")
    private List<ContractBzjxxF> bzjxx;

    /**
     * spxx
     */
    @ApiModelProperty("(审批信息)")
    private List<ContractSpxxF> spxx;

    /**
     * bxxx
     */
    @ApiModelProperty("(保险信息)")
    private List<ContractBxxxF> bxxx;

    /**
     * dbxx
     */
    @ApiModelProperty("(担保信息)")
    private List<ContractDbxxF> dbxx;


    /**
     * fjxx
     */
    @ApiModelProperty("(附件信息)")
    private List<ContractFjxxF> fjxx;

    /**
     * pdffjxx
     */
    @ApiModelProperty("(PDF附件信息)")
    private List<ContractFjxxF> pdffjxx;


    //  以下为zlxx("(租赁信息)") 后需封装成对象
    @ApiModelProperty("租赁期限")
    private String rentperiod;

    @ApiModelProperty("租赁期限翻译")
    private String rentperiodname;

    @ApiModelProperty("免租期")
    private String rentfreeperiod;

    @ApiModelProperty("租赁开始日期")
    private String rentstartdate;

    @ApiModelProperty("租赁结束日期")
    private String rentenddate;


    //  以下为tzxx("(特征信息)") 后需封装成对象
    @ApiModelProperty("支付币种")
    private String paymentcurrency;


    @ApiModelProperty("汇率确定方式")
    private String ratemethod;


    @ApiModelProperty("价款方式")
    private String paymethod;


    @ApiModelProperty("价款方式翻译")
    private String paymethodname;


    @ApiModelProperty("发票类型")
    private List<String> invoicetype;

    @ApiModelProperty("发票类型名称")
    private String invoicetypename;


    @ApiModelProperty("合同价款支付条件")
    private String contractpaymentterms;


    @ApiModelProperty("是否存在预付款")
    private String issfyyfk;


    @ApiModelProperty("预付款支付条件")
    private String payrule;


    @ApiModelProperty("是否有保证金")
    private String issfybzj;


    @ApiModelProperty("预付款比例")
    private String payratio;


    @ApiModelProperty("预付款金额")
    private String payamt;


    @ApiModelProperty("合同约定开工日期")
    private String startdate;


    @ApiModelProperty("合同约定完工日期")
    private String enddate;


    @ApiModelProperty("工期")
    private String period;


    @ApiModelProperty("是否联合体")
    private String iscoalition;


    @ApiModelProperty("本单位承担合同额（含税）")
    private String bdwcdhtehsamt;


    @ApiModelProperty("本单位承担合同额适用税率")
    private String bdwcdbfhtetax;


    @ApiModelProperty("本单位承担合同额适用税额")
    private String bdwcdbfhtetaxamt;


    @ApiModelProperty("变更后本单位承担合同额（含税）")
    private String bghbdwcdhtehsamt;


    @ApiModelProperty("变更后本单位承担合同额适用税率")
    private String bghbdwcdbfhtetax;


    @ApiModelProperty("变更后本单位承担合同额适用税额")
    private String bghbdwcdbfhtetaxamt;


    @ApiModelProperty("材料是否可以调差")
    private String ismatadjustment;


    @ApiModelProperty("最终结算审核方式")
    private String finishverifymethod;


    @ApiModelProperty("进度款支付比例")
    private String progressratio;


    @ApiModelProperty("进度款付款条件")
    private String progressrule;


    @ApiModelProperty("完工验收支付比例")
    private String finishratio;


    @ApiModelProperty("完工验收付款条件")
    private String finishrule;


    @ApiModelProperty("付款方式/房款付款方式")
    private String paymentmethod;


    @ApiModelProperty("工程尾款应支付日期")
    private String gcwkyzfdate;


    @ApiModelProperty("是否有担保")
    private String issfydb;


    @ApiModelProperty("采购方式")
    private String purchasemethod;


    @ApiModelProperty("是否来源于框架协议")
    private String isagreement;


    @ApiModelProperty("缺陷责任期")
    private String defectperiod;


    @ApiModelProperty("退场日期")
    private String exitdate;


    @ApiModelProperty("险种")
    private List<String> safekind;

    /**
     * 合同状态
     */
    @ApiModelProperty("合同状态 新")
    private String statusname;

    @ApiModelProperty("履约进度翻译")
    private String performschedulename;

    @ApiModelProperty("收支类型翻译")
    private String incomeexpendtypename;

    @ApiModelProperty("争议解决方式翻译")
    private String disputesolutionwayname;


    @ApiModelProperty("合同范本使用情况翻译")
    private String conmodeluseconditionname;

    @ApiModelProperty("付款方式/房款付款方式翻译")
    private String paymentmethodname;

    @ApiModelProperty("(保证金类型拼接)")
    private String bzjlxstr;
    @ApiModelProperty("(保证金比例拼接)")
    private String bzjblstr;
    @ApiModelProperty("(保证金金额拼接)")
    private String bzjjestr;
    @ApiModelProperty("(保证金退还条件拼接)")
    private String bzjthtjstr;

    @ApiModelProperty("险种名称拼接")
    private String xzmcstr;
    @ApiModelProperty("保险金额拼接")
    private String bxjestr;
    @ApiModelProperty("免赔额拼接")
    private String mpestr;

    @ApiModelProperty("担保类别翻译拼接")
    private String dblbstr;
    @ApiModelProperty("担保形式翻译拼接")
    private String dbxsstr;
    @ApiModelProperty("担保比例拼接")
    private String dbblstr;
    @ApiModelProperty("担保金额拼接")
    private String dbjestr;
    @ApiModelProperty("退还条件拼接")
    private String thtjstr;


    private String conperformcountryname;
    private String conperformprovincesname;
    private String conperformcityname;
    private String conperformxianname;


    @ApiModelProperty("险种翻译")
    private String safekindname;

    @ApiModelProperty("采购方式翻译")
    private String purchasemethodname;


    @ApiModelProperty("最终结算审核方式翻译")
    private String finishverifymethodname;


    @ApiModelProperty("汇率确定方式翻译")
    private String ratemethodname;

    @ApiModelProperty("支付币种翻译")
    private String paymentcurrencyname;

    private String sjlydwid;

    private String sjlydwidname;

    private String sjlydw;


    @ApiModelProperty("履约主体json格式")
    private List<OrgInfoRv> sjlydwFs;

    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;


    @ApiModelProperty("清单信息")
    private List<ContractPayFundV> contractPayFundVList;

    @ApiModelProperty("合同服务类型")
    private ServeTypeEnum serveType;

    @ApiModelProperty("是否是推送场景")
    private Boolean isPush;

    //Nk状态（0.未开启；1.已开启;2.已关闭；3.关闭中）
    private Integer nkStatus;
    //是否HY（0.否；1.是）
    private Integer isHy;
    //hy关联合同ID
    private String hyContractId;
    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;
}
