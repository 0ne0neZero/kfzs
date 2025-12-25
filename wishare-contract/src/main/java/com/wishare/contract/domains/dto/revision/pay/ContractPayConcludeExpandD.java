package com.wishare.contract.domains.dto.revision.pay;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

/**
* <p>
* 支出合同订立信息拓展表
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_pay_conclude_expand请求对象", description = "支出合同订立信息拓展表")
public class ContractPayConcludeExpandD {

    @ApiModelProperty("支出合同订立信息拓展表id")
    private Long id;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同语言")
    private String conlanguage;
    @ApiModelProperty("合同名称")
    private String conname;
    @ApiModelProperty("是否补充合同")
    private String issupplycontract;
    @ApiModelProperty("内部合同关联校验码")
    private String oppositeconmaincode;
    @ApiModelProperty("收支类型")
    private String incomeexpendtype;
    @ApiModelProperty("合同履行地(国家)")
    private String conperformcountry;
    @ApiModelProperty("合同履行地(省)")
    private String conperformprovinces;
    @ApiModelProperty("合同履行地(市)")
    private String conperformcity;
    @ApiModelProperty("合同履行地(县)")
    private String conperformxian;
    @ApiModelProperty("争议解决方式")
    private String disputesolutionway;
    @ApiModelProperty("合同范本使用情况")
    private String conmodelusecondition;
    @ApiModelProperty("合同承办人id")
    private String conundertakerid;
    @ApiModelProperty("合同承办人姓名")
    private String conundertaker;
    @ApiModelProperty("合同承办人联系方式")
    private String conundertakercontact;
    @ApiModelProperty("税率")
    private BigDecimal taxrate;
    @ApiModelProperty("税额")
    private BigDecimal taxamt;
    @ApiModelProperty("合同变更后总金额（含税）")
    private BigDecimal hsbgamt;
    @ApiModelProperty("(特征信息)")
    private String tzxx;
    @ApiModelProperty("(合同支付信息:支付方信息)")
    private String fkdwxx;
    @ApiModelProperty("(合同支付信息:收入方信息)")
    private String skdwxx;
    @ApiModelProperty("(保证金信息)")
    private String bzjxx;
    @ApiModelProperty("(审批信息)")
    private String spxx;
    @ApiModelProperty("(附件信息)")
    private String fjxx;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
