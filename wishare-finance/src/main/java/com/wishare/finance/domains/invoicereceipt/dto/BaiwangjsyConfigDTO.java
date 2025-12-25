package com.wishare.finance.domains.invoicereceipt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/10/30 11:57
 * @Description:
 */
@Getter
@Setter
public class BaiwangjsyConfigDTO {

    /**
     * appId
     */
    private String appId;

    /**
     * secret
     */
    private String secret;

    /**
     * 签名方式(0HmacSHA256,1MD5,3不签名)
     */
    private String signType;

    /**
     * 临时域名
     */
    private String tempHost;

}
