package com.wishare.finance.domains.voucher.support.fangyuan;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = FangYuanBillProperties.PREFIX)
public class FangYuanBillProperties {

    public final static String PREFIX =  "fang-yuan.bill";

    private String FYUrl;
    private String FYSendUrl;
    private String appid;
    private String AppSecret;

}