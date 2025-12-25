package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 诺税通saas红字确认单查询入参
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/13
 */
@Getter
@Setter
public class ElectronInvoiceRedQueryF {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 操作方身份： 0销方 1购方
     */
    private String identity;

    /**
     * 红字确认单申请号
     */
    private String billId;

    /**
     * 企业税号
     */
    private String taxnum;

}
