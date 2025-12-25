package com.wishare.contract.apps.fo.revision.income;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.apps.fo.revision.pay.ContractQydwsF;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.domains.enums.revision.ServeTypeEnum;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  10:48
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息表新增参数EditF", description = "收入合同订立信息表新增参数EditF")
public class ContractIncomeEditF {

    @ApiModelProperty("中交合同类型业务码")
    private String bizCode;

    /**
     * id 不可为空
     */
    @ApiModelProperty(value = "合同id",required = true)
    @NotBlank(message = "主键合同id不可为空")
    @Length(message = "合同id不可超过 50 个字符",max = 50)
    private String id;
    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 40 个字符",max = 40)
    private String contractNo;
    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;
    /**
     * 合同性质 1虚拟合同 0非虚拟合同
     */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    /**
     * noPaySign
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
    private List<String> categoryPathList;
    /**
     * 合同业务分类名称
     */
    @ApiModelProperty("合同业务分类名称")
    @Length(message = "合同业务分类名称不可超过 50 个字符",max = 50)
    private String categoryName;
    /**
     * 关联主合同Id
     */
    @ApiModelProperty("关联主合同Id")
    @Length(message = "关联主合同Id不可超过 50 个字符",max = 50)
    private String pid;
    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    /**
     * 甲方ID 收入类-取客户
     */
    @ApiModelProperty("甲方ID 收入类-取客户")
    @Length(message = "甲方ID 收入类-取客户不可超过 50 个字符",max = 50)
    private String partyAId;
    /**
     * 乙方ID 收入类-取供应商
     */
    @ApiModelProperty("乙方ID 收入类-取供应商")
    @Length(message = "乙方ID 收入类-取供应商不可超过 50 个字符",max = 50)
    private String partyBId;
    /**
     * 甲方名称
     */
    @ApiModelProperty("甲方名称")
    @Length(message = "甲方名称不可超过 50 个字符",max = 50)
    private String partyAName;
    /**
     * 乙方名称
     */
    @ApiModelProperty("乙方名称")
    @Length(message = "乙方名称不可超过 50 个字符",max = 50)
    private String partyBName;
    /**
     * 所属公司ID
     */
    @ApiModelProperty("所属公司ID")
    @Length(message = "所属公司ID不可超过 50 个字符",max = 50)
    private String orgId;
    /**
     * 所属部门ID
     */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 50 个字符",max = 50)
    private String departId;
    /**
     * 所属部门名称
     */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String departName;
    /**
     * 所属公司名称
     */
    @ApiModelProperty("所属公司名称")
    @Length(message = "所属公司名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
     * 签约人名称
     */
    @ApiModelProperty("签约人名称")
    @Length(message = "签约人名称不可超过 50 个字符",max = 50)
    private String signPerson;
    /**
     * 签约人ID
     */
    @ApiModelProperty("签约人ID")
    @Length(message = "签约人ID不可超过 50 个字符",max = 50)
    private String signPersonId;
    /**
     * 签约日期
     */
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate signDate;
    /**
     * 成本中心ID
     */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
     * 成本中心名称
     */
    @ApiModelProperty("成本中心名称")
    @Length(message = "成本中心名称不可超过 50 个字符",max = 50)
    private String costCenterName;
    /**
     * 所属项目ID 来源 成本中心
     */
    @ApiModelProperty("所属项目ID 来源 成本中心")
    @Length(message = "所属项目ID 来源 成本中心不可超过 40 个字符",max = 40)
    private String communityId;
    /**
     * 所属项目名称 来源 成本中心
     */
    @ApiModelProperty("所属项目名称 来源 成本中心")
    @Length(message = "所属项目名称 来源 成本中心不可超过 255 个字符",max = 255)
    private String communityName;
    /**
     * 负责人ID
     */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 50 个字符",max = 50)
    private String principalId;
    /**
     * 交易类型ID 1关联交易  2非关联交易
     */
    @ApiModelProperty("交易类型ID 1关联交易  2非关联交易")
    @Length(message = "交易类型ID 1关联交易  2非关联交易不可超过 40 个字符",max = 40)
    private String dealTypeId;
    /**
     * 用印类型  1合同专用章 2公司公章
     */
    @ApiModelProperty("用印类型  1合同专用章 2公司公章")
    private Integer sealType;
    /**
     * 负责人名称
     */
    @ApiModelProperty("负责人名称")
    @Length(message = "负责人名称不可超过 50 个字符",max = 50)
    private String principalName;
    /**
     * 合同金额
     */
    @ApiModelProperty("合同金额")
    @Digits(integer = 10,fraction =2,message = "合同金额不正确")
    private BigDecimal contractAmount;
    /**
     * 是否保证金 0 否 1 是
     */
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    /**
     * 保证金额
     */
    @ApiModelProperty("保证金额")
    @Digits(integer = 10,fraction =2,message = "保证金额不正确")
    private BigDecimal bondAmount;
    /**
     * 合同开始日期
     */
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    /**
     * 合同到期日期
     */
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
     * 范本的fileVo
     */
    @ApiModelProperty("范本的fileVo")
    private FileVo tempVo;
    /**
     * 是否倒签 0 否  1 是
     */
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    /**
     * 范本的filekey
     */
    @ApiModelProperty("范本的filekey")
    @Length(message = "范本的filekey不可超过 255 个字符",max = 255)
    private String tempFilekey;
    /**
     * 是否为暂存
     */
    @ApiModelProperty("是否为暂存")
    private Boolean isStash;



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
    @ApiModelProperty("开票金额")
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


    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;

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
    /**
     * conmanagetype
     */
    @ApiModelProperty("合同管理类别")
    @Length(message = "合同管理类别不可超过 30 个字符",max = 30)
    private String conmanagetype;
    /**
     * conincrementype
     */
    @ApiModelProperty("增值业务类型")
    @Length(message = "增值业务类型不可超过 30 个字符",max = 30)
    private String conincrementype;
    /**
     * fmzjtzskd
     */
    @ApiModelProperty("是否满足集团招商刻度0是1否")
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
    private String incomeexpendtype = "01";
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

    @ApiModelProperty("签约单位信息")
    private List<ContractQydwsF> qydws;

    @ApiModelProperty("合同对方信息")

    private List<ContractDfxxF> htdfxx;
    /**
     * fkdwxx
     */
    @ApiModelProperty("(合同支付信息:支付方信息)")
    private List<ContractZffxxF> fkdwxx;
    /**
     * skdwxx
     */
    @ApiModelProperty("(合同支付信息:收入方信息)")
    private List<ContractSrfxxF> skdwxx;
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


    @ApiModelProperty("发票类型")
    private List<String> invoicetype;


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

    @ApiModelProperty("是否启动WPS编辑")
    private Boolean isEditWps;

    @ApiModelProperty("履约主体json格式")
    private List<OrgInfoRv> sjlydwFs;

    @ApiModelProperty("履约主体id")
    private String sjlydwid;

    @ApiModelProperty("履约主体翻译")
    private String sjlydwidname;

    @ApiModelProperty("合同履行地(省)中文")
    private String conperformprovincesname;


    @ApiModelProperty("合同履行地(市)中文")
    private String conperformcityname;

    @ApiModelProperty("合同履行地(县)中文")
    private String conperformxianname;

    @ApiModelProperty("险种")
    private List<String> safekind;

    @ApiModelProperty("是否枫行梦")
    private String isfxm;

    @ApiModelProperty("分成比例")
    private BigDecimal companyRate;

    @ApiModelProperty("是否分成")
    private Integer ownerRate;

    @ApiModelProperty("清单")
    private List<ContractIncomeFundUpdateF> contractIncomeFundVList;

    @ApiModelProperty("合同服务类型")
    private ServeTypeEnum serveType;

    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;

    @ApiModelProperty("修正原因")
    private String correctionReason;
    @ApiModelProperty("修正附件信息")
    private List<ContractFjxxF> correctionFile;
}
