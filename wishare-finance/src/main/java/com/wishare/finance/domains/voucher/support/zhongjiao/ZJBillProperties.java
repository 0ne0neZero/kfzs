package com.wishare.finance.domains.voucher.support.zhongjiao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = ZJBillProperties.PREFIX)
public class ZJBillProperties {

    public final static String PREFIX =  "zj.bill";

    private String url;

    // 资金计划编号默认值
    private String payPlanDefaultResult;
    // 期望结算方式默认值
    private String paymentMethodResult;
    // 往来单位银行信息默认值
    private String unitBankInfo;

}