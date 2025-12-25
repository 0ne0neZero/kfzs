package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/5/13
 */
@Getter
@Setter
@ApiModel(value="账单推凭状态")
public class BillPostedStatusV {

    @ApiModelProperty(value = "是否已推凭")
    private Boolean isPosted;

}
