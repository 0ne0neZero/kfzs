package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/13 9:52
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("批量交账返回结果")
public class AccountHandBatchV {

    /**
     * 成功状态码 0全部成功 1部分成功 2全部失败
     */
    @ApiModelProperty("成功状态码 0全部成功 1部分成功 2全部失败")
    private Integer code;

    /**
     * 交账失败数量
     */
    @ApiModelProperty("交账失败数量")
    private Integer errSize;

    public AccountHandBatchV initResult(Integer errSize, Integer allSize) {
        if (errSize == 0) {
            this.code = 0;
        } else if (errSize < allSize) {
            this.code = 1;
        } else {
            this.code = 2;
        }
        this.errSize = errSize;
        return this;
    }
}
