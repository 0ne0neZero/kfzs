package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表特征信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractTzxxF {
    @ApiModelProperty("支付币种")
    private String paymentcurrency;


    @ApiModelProperty("汇率确定方式")
    private String ratemethod;

    @ApiModelProperty("汇率确定方式翻译")
    private String ratemethodname;


    @ApiModelProperty("价款方式")
    private String paymethod;


    @ApiModelProperty("价款方式翻译")
    private String paymethodname;


    @ApiModelProperty("发票类型")
    private String invoicetype;

    @ApiModelProperty("发票类型翻译")
    private String invoicetypename;


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

    @ApiModelProperty("最终结算审核方式翻译")
    private String finishverifymethodname;


    @ApiModelProperty("进度款支付比例")
    private String progressratio;


    @ApiModelProperty("进度款付款条件")
    private String progressrule;


    @ApiModelProperty("完工验收支付比例")
    private String finishratio;


    @ApiModelProperty("完工验收付款条件")
    private String finishrule;


    @ApiModelProperty("付款方式")
    private String paymentmethod;

    @ApiModelProperty("付款方式翻译")
    private String paymentmethodname;


    @ApiModelProperty("工程尾款应支付日期")
    private String gcwkyzfdate;


    @ApiModelProperty("是否有担保")
    private String issfydb;


    @ApiModelProperty("采购方式")
    private String purchasemethod;

    @ApiModelProperty("采购方式翻译")
    private String purchasemethodname;


    @ApiModelProperty("是否来源于框架协议")
    private String isagreement;


    @ApiModelProperty("缺陷责任期")
    private String defectperiod;


    @ApiModelProperty("退场日期")
    private String exitdate;


    @ApiModelProperty("险种")
    private String safekind;

    @ApiModelProperty("险种翻译")
    private String safekindname;



    private String paymentcurrencyname;


}
