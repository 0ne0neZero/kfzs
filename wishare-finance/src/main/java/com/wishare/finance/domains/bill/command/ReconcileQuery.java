package com.wishare.finance.domains.bill.command;

import com.wishare.starter.beans.PageF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 对账查询信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/23
 */
@Getter
@Setter
@ApiModel("对账查询信息")
public class ReconcileQuery extends PageF {

    @ApiModelProperty(value = "对账维度规则")
    private ReconcileDimensionRuleQuery dimensionRuleQuery;

    @ApiModelProperty(value = "对账前置条件")
    private List<ReconcilePreconditionsQuery> preconditionsQueries;

    @ApiModelProperty(value = "对账账单匹配条件")
    private ReconciliationBillQuery reconciliationBillQuery;

    @ApiModelProperty(value = "对账模式: 0账票流水对账，1商户清分对账")
    private Integer reconcileMode;

    @ApiModelProperty(value = "认领ID类型：1:蓝票;2:红票;")
    private List<Long> invoiceIdList;

    @ApiModelProperty(value = "认领ID类型：收款单id")
    private List<Long> gatherBillIdList;

    @ApiModelProperty(value = "认领ID类型：退款单id")
    private List<Long> payBillIdList;
}
