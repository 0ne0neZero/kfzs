package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 现金流量
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Setter
@Getter
@Accessors(chain = true)
public class CashFlowOBV {

    @ApiModelProperty(value = "现金流量类型： 1流入，2流出")
    private Integer type;

    @ApiModelProperty(value = "现金流量编码")
    private String code;

    @ApiModelProperty(value = "现金流量名称")
    private String name;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "本位币金额（单位：分）")
    private Long money;

}
