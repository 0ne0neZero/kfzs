package com.wishare.finance.infrastructure.remote.vo.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author scx
 * @date 2024/6/24
 * @Description:
 */
@Getter
@Setter
@ApiModel("通过收款单id查询发票")
public class ValidReverseGatherBillRV {

    @ApiModelProperty("收款单id")
    private Long gatherBillId;

    @ApiModelProperty("发票id")
    private List<Long> invoiceIds;
}
