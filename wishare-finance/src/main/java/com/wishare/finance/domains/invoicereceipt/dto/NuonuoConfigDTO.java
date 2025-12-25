package com.wishare.finance.domains.invoicereceipt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
public class NuonuoConfigDTO {

    /**
     * appKey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 税号
     */
    private String taxnum;

    /**
     * 临时域名
     */
    private String tempHost;
}
