package com.wishare.contract.apps.fo.revision.pay;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
* <p>
* 支出合同订立信息拓展表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息拓展表新增请求参数", description = "支出合同订立信息拓展表新增请求参数")
public class ContractPayConcludeExpandSaveF {
    @ApiModelProperty("履约主体id")
    private String sjlydwid;

    @ApiModelProperty("履约主体翻译")
    private String sjlydwidname;

    @ApiModelProperty("履约主体json")
    private String sjlydw;
    /**
     * 合同id 不可为空
     */
    @ApiModelProperty(value = "合同id",required = true)
    @NotBlank(message = "合同id不可为空")
    @Length(message = "合同id不可超过 50 个字符",max = 50)
    private String contractId;
    /**
     * 合同语言
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
    @Length(message = "合同名称不可超过 450 个字符",max = 450)
    private String conname;
    /**
     * 是否补充合同
     */
    @ApiModelProperty("是否补充合同")
    @Length(message = "是否补充合同不可超过 30 个字符",max = 30)
    private String issupplycontract;
    /**
     * 内部合同关联校验码
     */
    @ApiModelProperty("内部合同关联校验码")
    @Length(message = "内部合同关联校验码不可超过 100 个字符",max = 100)
    private String oppositeconmaincode;
    /**
     * 收支类型
     */
    @ApiModelProperty("收支类型")
    @Length(message = "收支类型不可超过 30 个字符",max = 30)
    private String incomeexpendtype;
    /**
     * 合同履行地(国家)
     */
    @ApiModelProperty("合同履行地(国家)")
    @Length(message = "合同履行地(国家)不可超过 30 个字符",max = 30)
    private String conperformcountry;
    /**
     * 合同履行地(省)
     */
    @ApiModelProperty("合同履行地(省)")
    @Length(message = "合同履行地(省)不可超过 30 个字符",max = 30)
    private String conperformprovinces;
    /**
     * 合同履行地(市)
     */
    @ApiModelProperty("合同履行地(市)")
    @Length(message = "合同履行地(市)不可超过 30 个字符",max = 30)
    private String conperformcity;
    /**
     * 合同履行地(县)
     */
    @ApiModelProperty("合同履行地(县)")
    @Length(message = "合同履行地(县)不可超过 30 个字符",max = 30)
    private String conperformxian;
    /**
     * 争议解决方式
     */
    @ApiModelProperty("争议解决方式")
    @Length(message = "争议解决方式不可超过 30 个字符",max = 30)
    private String disputesolutionway;
    /**
     * 合同范本使用情况
     */
    @ApiModelProperty("合同范本使用情况")
    @Length(message = "合同范本使用情况不可超过 30 个字符",max = 30)
    private String conmodelusecondition;
    /**
     * 合同承办人id
     */
    @ApiModelProperty("合同承办人id")
    @Length(message = "合同承办人id不可超过 30 个字符",max = 30)
    private String conundertakerid;
    /**
     * 合同承办人姓名
     */
    @ApiModelProperty("合同承办人姓名")
    @Length(message = "合同承办人姓名不可超过 100 个字符",max = 100)
    private String conundertaker;
    /**
     * 合同承办人联系方式
     */
    @ApiModelProperty("合同承办人联系方式")
    @Length(message = "合同承办人联系方式不可超过 100 个字符",max = 100)
    private String conundertakercontact;
    /**
     * 税率
     */
    @ApiModelProperty("税率")
    @Digits(integer = 20,fraction =2,message = "税率不正确")
    private BigDecimal taxrate;
    /**
     * 税额
     */
    @ApiModelProperty("税额")
    @Digits(integer = 20,fraction =2,message = "税额不正确")
    private BigDecimal taxamt;
    /**
     * 合同变更后总金额（含税）
     */
    @ApiModelProperty("合同变更后总金额（含税）")
    @Digits(integer = 20,fraction =2,message = "合同变更后总金额（含税）不正确")
    private BigDecimal hsbgamt;

    @ApiModelProperty("履约进度")
    private String performschedule;
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
     * (特征信息)
     */
    @ApiModelProperty("(特征信息)")
    private String tzxx;
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
    /**
     * (保险信息)
     */
    @ApiModelProperty("(保险信息)")
    private String bxxx;
    /**
     * (担保信息)
     */
    @ApiModelProperty("(担保信息)")
    private String dbxx;


    @ApiModelProperty("是否有授权人")
    private String issqr;

    @ApiModelProperty("签约单位信息")
    private String qydws;

    @ApiModelProperty("授权人（多个授权人姓名和身份证号信息）")
    private String authorizedname;


    @ApiModelProperty("合同履行地(省)中文")
    private String conperformprovincesname;


    @ApiModelProperty("合同履行地(市)中文")
    private String conperformcityname;


    @ApiModelProperty("合同履行地(县)中文")
    private String conperformxianname;

}
