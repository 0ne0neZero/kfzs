package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 单个凭证推凭信息
 * @author: pgq
 * @since: 2022/11/1 17:10
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("单个凭证推凭信息")
public class VoucherInference {

    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private Long billId;

    /**
     * 账单类型
     */
    @ApiModelProperty("账单类型")
    private Integer billType;

    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private Integer actionType;
}
