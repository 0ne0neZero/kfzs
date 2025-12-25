package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
public class ContractInfoV extends Tree<ContractInfoV,Long> {

//    @ApiModelProperty("合同id")
//    private Long id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("公司id 来源 org_tenant.id")
    private String tenantId;
    @ApiModelProperty("公司名称")
    private String tenantName;
    @ApiModelProperty(value = "项目id 来源 成本中心",required = true)
    private String communityId;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("合同编号")
    private String contractNo;
    //    @ApiModelProperty("父级id")
//    private Long pid;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String PartyBName;
    @ApiModelProperty("甲方Id")
    private Long partyAId;
    @ApiModelProperty("乙方Id")
    private Long PartyBId;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("合同性质 1 收入 2 支出 3 其他")
    private Integer contractNature;
    @ApiModelProperty("金额（含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("金额（不含税）")
    private BigDecimal amountTaxExcluded;
    @ApiModelProperty("合同预估金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 3 终止 4 终止中")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 0 未履行 1 履行中 2 已到期 3 已终止")
    private Integer contractState;
    @ApiModelProperty("合同业务时长月份数")
    private Integer dayNum;
    @ApiModelProperty("保证金额-原币")
    private BigDecimal bondAmount;
    @ApiModelProperty("保证金额-本币")
    private BigDecimal bondHomeCurrency;
    @ApiModelProperty("保证金余额")
    private BigDecimal bondBalance;
    @ApiModelProperty("计划收款金额")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("开收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("收付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("损益含税金额")
    private BigDecimal profitLocalCurrencyAmount;
    @ApiModelProperty("损益不含税金额")
    private BigDecimal profitTaxExcludedAmount;
    @ApiModelProperty("损益确认金额（收入确认）")
    private BigDecimal profitAmount;
    @ApiModelProperty("催收金额")
    private BigDecimal collectionAmount;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("税种税率id")
    private String taxRateIdPath;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
}
