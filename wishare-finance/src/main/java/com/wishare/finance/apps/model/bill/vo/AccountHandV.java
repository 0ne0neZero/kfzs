package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/13 10:26
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("单个交账返回结果")
public class AccountHandV {

    @ApiModelProperty("结果 0交账成功 1交账异常-收款金额＜开票金额  2交账异常-收款金额>开票金额")
    private Integer code;

    public AccountHandV(Integer code) {
        this.code = code;
    }

    public AccountHandV() {
    }
}
