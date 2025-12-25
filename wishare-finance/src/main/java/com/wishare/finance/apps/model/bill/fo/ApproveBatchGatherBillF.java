package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title ApproveBatchGatherBillF
 * @date 2023.12.06  15:18
 * @description 批量审核收款单信息
 */
@Getter
@Setter
@ApiModel("批量审核收款单信息")
public class ApproveBatchGatherBillF extends ApproveBatchBillF{

    /*
     *       KEY:收款单ID
     *       VALUE:[
     *           KEY: 收款明细ID
     *           VALUE: 明细退款金额
     *             ]
     */
    @ApiModelProperty("收款明细退款详细包装参数")
    private Map<Long, Map<Long, BigDecimal>> gatherMap;
}
