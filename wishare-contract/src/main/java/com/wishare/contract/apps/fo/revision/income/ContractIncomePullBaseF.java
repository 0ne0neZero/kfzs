package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  10:39
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息表基础推送F", description = "收入合同订立信息表基础推送F")
public class ContractIncomePullBaseF {
    @ApiModelProperty("原系统合同id")
    private String fromid;

    @ApiModelProperty("合同中文名称")
    private String connamecn;

    @ApiModelProperty("合同语言")
    private String conlanguage;

    @ApiModelProperty("合同主数据编码")
    private String conmaincode;

    @ApiModelProperty("合同名称")
    private String conname;

    @ApiModelProperty("合同编码")
    private String concode;

    @ApiModelProperty("是否补充合同")
    private String issupplycontract;

    @ApiModelProperty("补充合同所属原合同主数据编码")
    private String supconmaincode;

    @ApiModelProperty("内部合同关联校验码")
    private String oppositeconmaincode;

    @ApiModelProperty("项目编码")
    private String projnumber;

    @ApiModelProperty("项目名称")
    private String projname;

    @ApiModelProperty("收支类型")
    private String incomeexpendtype;

    @ApiModelProperty("合同业务类型")
    private String businesstype;

    @ApiModelProperty("合同履行地(国家)")
    private String conperformcountry;

    @ApiModelProperty("合同履行地(省)")
    private String conperformprovinces;

    @ApiModelProperty("合同履行地(市)")
    private String conperformcity;

    @ApiModelProperty("合同履行地(县)")
    private String conperformxian;

    @ApiModelProperty("签约日期")
    private String signdate;

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

    @ApiModelProperty("合同概述")
    private String consummary;

    @ApiModelProperty("管理部门id")
    private String oid;

    @ApiModelProperty("我方签约单位id")
    private String partiesunitid;

    @ApiModelProperty("我方签约单位名称")
    private String partiesunitname;

    @ApiModelProperty("合同实际履约主体id")
    private String sjlydwid;

    @ApiModelProperty("合同实际履约主体名称")
    private String sjlydwname;

    @ApiModelProperty("合同状态")
    private String constatus;

    @ApiModelProperty("总金额币种/信托资金规模币种/贷款总额币种")
    private String currency;

    @ApiModelProperty("税率")
    private BigDecimal taxrate;

    @ApiModelProperty("税额")
    private BigDecimal taxamt;

    @ApiModelProperty("合同总金额(含税)")
    private BigDecimal hsamt;

    @ApiModelProperty("合同变更后总金额（含税）")
    private BigDecimal hsbgamt;

    @ApiModelProperty("管理部门名称")
    private String oname;

    @ApiModelProperty("履约进度")
    private String performschedule;

}
