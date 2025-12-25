package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* @description:开票规则对应法定单位信息等
* @author: zhenghui
* @date: 2023/3/22 19:12
*/
@Getter
@Setter
@Accessors(chain = true)
public class ChargeTicketRuleInvoiceV {

    @ApiModelProperty("允许开票类型")
    private Integer allowTicketType;

    @ApiModelProperty("开票单位名称")
    private String nameCn;

    @ApiModelProperty("纳税人识别号")
    private String taxpayerNo;

    @ApiModelProperty("电话")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("开户行及账号")
    private String accountInfo;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "开票人")
    private String clerk;

}
