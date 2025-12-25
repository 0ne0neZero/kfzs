package com.wishare.contract.apps.fo.revision.income;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 收入合同订立信息拓展表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-09-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息拓展表原始请求参数", description = "收入合同订立信息拓展表原始请求参数，会跟着表重新生成")
public class ContractIncomeConcludeExpandRawF {

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
    /**
    * 履约进度
    */
    @ApiModelProperty("履约进度")
    @Length(message = "履约进度不可超过 30 个字符",max = 30)
    private String performschedule;
    /**
    * (担保信息)
    */
    @ApiModelProperty("(担保信息)")
    private String dbxx;
    /**
    * (租赁信息)
    */
    @ApiModelProperty("(租赁信息)")
    private String zlxx;
    /**
    * (保险信息)
    */
    @ApiModelProperty("(保险信息)")
    private String bxxx;
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

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"conlanguage\",\"conname\",\"issupplycontract\",\"oppositeconmaincode\",\"incomeexpendtype\",\"conperformcountry\",\"conperformprovinces\",\"conperformcity\",\"conperformxian\",\"disputesolutionway\",\"conmodelusecondition\",\"conundertakerid\",\"conundertaker\",\"conundertakercontact\",\"taxrate\",\"taxamt\",\"hsbgamt\",\"performschedule\",\"dbxx\",\"zlxx\",\"bxxx\",\"htdfxx\",\"tzxx\",\"fkdwxx\",\"skdwxx\",\"bzjxx\",\"spxx\",\"fjxx\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 收入合同订立信息拓展表id"
        + "contractId 合同id"
        + "conlanguage 合同语言"
        + "conname 合同名称"
        + "issupplycontract 是否补充合同"
        + "oppositeconmaincode 内部合同关联校验码"
        + "incomeexpendtype 收支类型"
        + "conperformcountry 合同履行地(国家)"
        + "conperformprovinces 合同履行地(省)"
        + "conperformcity 合同履行地(市)"
        + "conperformxian 合同履行地(县)"
        + "disputesolutionway 争议解决方式"
        + "conmodelusecondition 合同范本使用情况"
        + "conundertakerid 合同承办人id"
        + "conundertaker 合同承办人姓名"
        + "conundertakercontact 合同承办人联系方式"
        + "taxrate 税率"
        + "taxamt 税额"
        + "hsbgamt 合同变更后总金额（含税）"
        + "performschedule 履约进度"
        + "dbxx (担保信息)"
        + "zlxx (租赁信息)"
        + "bxxx (保险信息)"
        + "htdfxx (合同对方信息)"
        + "tzxx (特征信息)"
        + "fkdwxx (合同支付信息:支付方信息)"
        + "skdwxx (合同支付信息:收入方信息)"
        + "bzjxx (保证金信息)"
        + "spxx (审批信息)"
        + "fjxx (附件信息)"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "deleted 是否删除  0 正常 1 删除")
    private List<String> fields;


}
