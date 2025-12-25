package com.wishare.contract.apps.remote.fo.procreate;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程创建请求数据实体
 *
 * @author zhangfuyu
 * @Date 2023/6/12/10:55
 */
@Data
@ApiModel(value = "流程创建请求数据实体")
public class ProcessCreateBusinessInfoF {

    @ApiModelProperty(value = "表单唯一id")
    private String formdataid;

    @ApiModelProperty(value = "业务系统唯一id  uuid")
    private String SJID;

    @ApiModelProperty(value = "合同名称")
    private String CONNAME;

    @ApiModelProperty(value = "合同中文名称")
    private String CONNAMECN;

    @ApiModelProperty(value = "合同编码")
    private String CONCODE;

    @ApiModelProperty(value = "合同业务类型")
    private String BUSINESSTYPE;

    @ApiModelProperty(value = "管理部门名称")
    private String ONAME;

    @ApiModelProperty(value = "是否补充合同")
    private String ISSUPPLYCONTRACT;

    @ApiModelProperty(value = "补充合同所属原合同主数据编码")
    private String SUPCONMAINCODE;

    @ApiModelProperty(value = "内部合同关联校验码")
    private String OPPOSITECONMAINCODE;

    @ApiModelProperty(value = "项目名称")
    private String PROJNAME;

    @ApiModelProperty(value = "合同履行地(国家)")
    private String CONPERFORMCOUNTRY;

    @ApiModelProperty(value = "合同语言")
    private String CONLANGUAGE;

    @ApiModelProperty(value = "签约日期")
    private String SIGNDATE;

    @ApiModelProperty(value = "争议解决方式")
    private String DISPUTESOLUTIONWAY;

    @ApiModelProperty(value = "合同范本使用情况")
    private String CONMODELUSECONDITION;

    @ApiModelProperty(value = "总金额币种")
    private String CURRENCY;

    @ApiModelProperty(value = "税率")
    private String TAXRATE;

    @ApiModelProperty(value = "税额")
    private String TAXAMT;

    @ApiModelProperty(value = "合同总金额(含税)")
    private String HSAMT;

    @ApiModelProperty(value = "合同实际履约主体名称")
    private String SJLYDWNAME;

    @ApiModelProperty(value = "合同承办人姓名")
    private String CONUNDERTAKER;

    @ApiModelProperty(value = "合同概述")
    private String CONSUMMARY;

    @ApiModelProperty(value = "我方签约单位名称")
    private String PARTIESUNITNAME;

    @ApiModelProperty(value = "价款方式")
    private String PAYMETHOD;

    @ApiModelProperty(value = "是否存在预付款")
    private String ISSFYYFK;

    @ApiModelProperty(value = "预付款支付条件")
    private String PAYRULE;

    @ApiModelProperty(value = "支付币种")
    private String PAYMENTCURRENCY;

    @ApiModelProperty(value = "汇率确定方式")
    private String RATEMETHOD;

    @ApiModelProperty(value = "发票类型")
    private String INVOICETYPE;

    @ApiModelProperty(value = "预付款比例")
    private String PAYRATIO;

    @ApiModelProperty(value = "预付款金额")
    private String PAYAMT;

    @ApiModelProperty(value = "采购方式")
    private String PURCHASEMETHOD;

    @ApiModelProperty(value = "是否来源于框架协议")
    private String ISAGREEMENT;

    @ApiModelProperty(value = "合同约定开工日期")
    private String STARTDATE;

    @ApiModelProperty(value = "合同约定完工日期")
    private String ENDDATE;

    @ApiModelProperty(value = "工期")
    private String PERIOD;

    @ApiModelProperty(value = "缺陷责任期")
    private String DEFECTPERIOD;

    @ApiModelProperty(value = "退场日期")
    private String EXITDATE;

    @ApiModelProperty(value = "进度款支付比例")
    private String PROGRESSRATIO;

    @ApiModelProperty(value = "进度款付款条件")
    private String PROGRESSRULE;

    @ApiModelProperty(value = "完工验收支付比例")
    private String FINISHRATIO;

    @ApiModelProperty(value = "付款方式")
    private String PAYMENTMETHOD;

    @ApiModelProperty(value = "完工验收付款条件")
    private String FINISHRULE;

    @ApiModelProperty(value = "是否有担保")
    private String ISSFYDB;

    @ApiModelProperty(value = "变更后金额")
    private String HSBGAMT;

    @ApiModelProperty(value = "业务数据")
    private ProcessHtdfxxF[] HTDFXX;

    @ApiModelProperty(value = "业务数据")
    private ProcessFkdwxxF[] FKDWXX;

    @ApiModelProperty(value = "业务数据")
    private ProcessSkdwxxF[] SKDWXX;

    @ApiModelProperty(value = "业务数据")
    private ProcessDbxxF[] DBXX;

}
