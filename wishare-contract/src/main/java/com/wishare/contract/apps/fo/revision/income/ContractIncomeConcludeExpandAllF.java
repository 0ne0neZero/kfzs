package com.wishare.contract.apps.fo.revision.income;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
* 收入合同订立信息拓展表
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息拓展表请求参数", description = "收入合同订立信息拓展表")
public class ContractIncomeConcludeExpandAllF {

    /**
    * id
    */
    @ApiModelProperty("收入合同订立信息拓展表id")
    private Long id;
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
    /**
     * zlxx
     */
    @ApiModelProperty("(租赁信息)")
    private ContractZlxxF zlxx;
    /**
    * tzxx
    */
    @ApiModelProperty("(特征信息)")
    private ContractTzxxF tzxx;
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
    * fjxx
    */
    @ApiModelProperty("(附件信息)")
    private List<ContractFjxxF> fjxx;


}
