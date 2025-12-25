package com.wishare.finance.domains.bill.aggregate.data;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @see com.wishare.finance.domains.bill.aggregate.data.EnvData
 */
@Component
public class EnvData {

    public static String config;
    @Value("${env.config:huixiang}")
    public void setConfig(String config) {
        EnvData.config = config;
    }

    public static Boolean msgFlag;
    @Value("${env.msgFlag:false}")
    public void setMsgFlag(Boolean msgFlag) {
        EnvData.msgFlag = msgFlag;
    }

}
