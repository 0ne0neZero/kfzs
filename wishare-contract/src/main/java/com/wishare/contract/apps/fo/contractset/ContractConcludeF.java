package com.wishare.contract.apps.fo.contractset;


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
* 合同订立信息
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractConcludeF {

    @ApiModelProperty("合同id")
    private Long id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("公司id 来源 org_tenant.id")
    private String tenantId;
    @ApiModelProperty("项目id 来源 space_community.id")
    private String communityId;
    @ApiModelProperty("合同分类Id")
    private Long categoryId;
    @ApiModelProperty("父级id")
    private Long pid;
    @ApiModelProperty("合同性质 1 收入 2 支出")
    private Integer contractNature;
    @ApiModelProperty("是否虚拟合同 0 否 1 是")
    private Integer virtualContract;
    @ApiModelProperty("是否倒签合同 0 否 1 是")
    private Boolean backdatingContract;
    @ApiModelProperty("签约方式")
    private Integer signingMethod;
    @ApiModelProperty("合同状态 1 未履行 2 履行中 3 已到期 4 已终止")
    private Integer contractState;
    @ApiModelProperty("关联支出类合同")
    private String expenditureContract;
    @ApiModelProperty("是否引用范本 0 否 1 是")
    private Boolean referenceModel;
    @ApiModelProperty("关联收入类合同")
    private String incomeContract;
    @ApiModelProperty("是否框架合同 0 否 1 是")
    private Boolean frameworkContract;
    @ApiModelProperty("实际履约金额（本币含税）")
    private BigDecimal contractAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("关联招投标保证金账单id")
    private Long bidBondBillId;
    @ApiModelProperty("关联招投标保证金账单编号")
    private String bidBondBillNo;
    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;
    @ApiModelProperty("项目来源")
    private String projectSource;
    @ApiModelProperty("业态")
    private String businessType;
    @ApiModelProperty("物业模式")
    private String propertyModel;
}
