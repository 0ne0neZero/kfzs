package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/20 11:27
 * @descrption:
 */
@Data
@ApiModel(value = "红字确认单查询数据体")
public class RedConfirmSearchDataF {

    // 必填，长度20
    @ApiModelProperty(value = "机构税号")
    private String taxNo;

    // 必填，长度20
    @ApiModelProperty(value = "销售方统一社会信用代码/纳税人识别号/身份证件号码")
    private String sellerTaxNo;

    // 必填，长度32
    @ApiModelProperty(value = "红字确认单UUID")
    private String redConfirmUuid;
}
