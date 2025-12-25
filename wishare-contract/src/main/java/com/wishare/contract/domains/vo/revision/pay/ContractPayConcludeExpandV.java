package com.wishare.contract.domains.vo.revision.pay;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wishare.contract.apps.remote.vo.config.CfgExternalDeportData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 支出合同订立信息拓展表视图对象
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息拓展表视图对象", description = "支出合同订立信息拓展表视图对象")
public class ContractPayConcludeExpandV {

    /**
    * 支出合同订立信息拓展表id
    */
    @ApiModelProperty("支出合同订立信息拓展表id")
    private Long id;
    /**
    * 合同id
    */
    @ApiModelProperty("合同id")
    private String contractId;
    /**
    * 合同语言
    */
    @ApiModelProperty("合同语言")
    private String conlanguage;
    /**
     * 合同管理类别
     */
    @ApiModelProperty("合同管理类别")
    private String conmanagetype;
    /**
     * conincrementype
     */
    @ApiModelProperty("增值业务类型")
    @Length(message = "增值业务类型不可超过 30 个字符",max = 30)
    private String conincrementype;
    /**
     * fwlb
     */
    @ApiModelProperty("服务类型")
    @Length(message = "服务类型 30 个字符",max = 30)
    private String fwlb;
    /**
     * fmzjtzskd
     */
    @ApiModelProperty("是否满足集团招商刻度0是1否")
    private Integer fmzjtzskd;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String conname;
    /**
    * 是否补充合同
    */
    @ApiModelProperty("是否补充合同")
    private String issupplycontract;
    /**
    * 内部合同关联校验码
    */
    @ApiModelProperty("内部合同关联校验码")
    private String oppositeconmaincode;
    /**
    * 收支类型
    */
    @ApiModelProperty("收支类型")
    private String incomeexpendtype;
    /**
    * 合同履行地(国家)
    */
    @ApiModelProperty("合同履行地(国家)")
    private String conperformcountry;
    /**
    * 合同履行地(省)
    */
    @ApiModelProperty("合同履行地(省)")
    private String conperformprovinces;
    /**
    * 合同履行地(市)
    */
    @ApiModelProperty("合同履行地(市)")
    private String conperformcity;
    /**
    * 合同履行地(县)
    */
    @ApiModelProperty("合同履行地(县)")
    private String conperformxian;
    /**
    * 争议解决方式
    */
    @ApiModelProperty("争议解决方式")
    private String disputesolutionway;
    /**
    * 合同范本使用情况
    */
    @ApiModelProperty("合同范本使用情况")
    private String conmodelusecondition;
    /**
    * 合同承办人id
    */
    @ApiModelProperty("合同承办人id")
    private String conundertakerid;
    /**
    * 合同承办人姓名
    */
    @ApiModelProperty("合同承办人姓名")
    private String conundertaker;
    /**
    * 合同承办人联系方式
    */
    @ApiModelProperty("合同承办人联系方式")
    private String conundertakercontact;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private BigDecimal taxrate;
    /**
    * 税额
    */
    @ApiModelProperty("税额")
    private BigDecimal taxamt;
    /**
    * 合同变更后总金额（含税）
    */
    @ApiModelProperty("合同变更后总金额（含税）")
    private BigDecimal hsbgamt;

    @ApiModelProperty("履约进度")
    private String performschedule;
    /**
    * (特征信息)
    */
    @ApiModelProperty("(特征信息)")
    private String tzxx;
    /**
     * (签约单位)
     */
    @ApiModelProperty("(签约单位)")
    private String qydws;
    /**
     * (租赁信息)
     */
    @ApiModelProperty("(租赁信息)")
    private String zlxx;
    /**
     * (合同对方信息)
     */
    @ApiModelProperty("(合同对方信息)")
    private String htdfxx;
    /**
    * (合同支付信息:支付方信息)
    */
    @ApiModelProperty("(合同支付信息:支付方信息)")
    private String fkdwxx;
    /**
    * (合同支付信息:收入方信息)
    */
    @ApiModelProperty("(合同支付信息:收入方信息)")
    private String skdwxx;
    /**
    * (保证金信息)
    */
    @ApiModelProperty("(保证金信息)")
    private String bzjxx;
    /**
    * (审批信息)
    */
    @ApiModelProperty("(审批信息)")
    private String spxx;
    /**
    * (附件信息)
    */
    @ApiModelProperty("(附件信息)")
    private String fjxx;

    /**
     * (PDF附件信息)
     */
    @ApiModelProperty("(PDF附件信息)")
    private String pdffjxx;

    @ApiModelProperty("(保险信息)")
    private String bxxx;

    @ApiModelProperty("(担保信息)")
    private String dbxx;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

    private String sjlydw;

    private String sjlydwid;

    private String sjlydwidname;

    @ApiModelProperty("合同履行地(省)中文")
    private String conperformprovincesname;


    @ApiModelProperty("合同履行地(市)中文")
    private String conperformcityname;

    @ApiModelProperty("合同履行地(县)中文")
    private String conperformxianname;


    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;

    // "recipient": "17147850622409",
    // "recipientCode": "BP00634068",
    // "recipientName": "重庆市锦正园林绿化有限公司",

    // "nameOfReceivingAccount": "重庆市锦正园林绿化有限公司",
    // "account": "0000234963",
    // "accountCode": "BP00634068",
    // "bankAccountNumber": "236880116910001",
    // "openingBank": "招商银行股份有限公司重庆北部新区支行",

    @ApiModelProperty("实际收款人id")
    private String recipient;

    @ApiModelProperty("实际收款人主数据编码")
    private String recipientCode;

    @ApiModelProperty("实际收款人名称")
    private String recipientName;

    @ApiModelProperty("收款账户名称")
    private String nameOfReceivingAccount;

    @ApiModelProperty("收款账户id")
    private String account;

    @ApiModelProperty("收款账户主数据编码")
    private String accountCode;

    @ApiModelProperty("收款账户银行账号")
    private String bankAccountNumber;

    @ApiModelProperty("收款账户开户行")
    private String openingBank;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;

}
