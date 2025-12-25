package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/5/13
 */
@Getter
@Setter
@ApiModel(value="查询账单是否推凭", description="查询账单是否推凭")
public class CheckPostedStatusF {

    @NotNull
    @ApiModelProperty(value = "账单id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "账单类型 1应收账单,2预收账单, 3临时收费账单, 4应付账单, 5退款账单, 6付款单, 7收款单")
    private Integer billType;

}
