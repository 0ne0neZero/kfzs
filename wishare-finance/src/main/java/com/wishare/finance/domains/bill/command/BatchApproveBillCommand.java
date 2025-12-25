package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class BatchApproveBillCommand extends BatchCommand{

    /**
     * 审核状态
     */
    private BillApproveStateEnum approveState;

    /*
     *       KEY:收款单ID
     *       VALUE:[
     *           KEY: 收款明细ID
     *           VALUE: 明细退款金额
     *             ]
     */
    @ApiModelProperty("收款明细退款详细包装参数")
    private Map<Long, Map<Long, BigDecimal>> gatherMap;

    /**
     * 申请驳回理由
     */
    private String rejectReason;

    public BatchApproveBillCommand() {
    }

    public BatchApproveBillCommand(PageF<SearchF<?>> query, List<Long> billIds, BillApproveStateEnum approveState) {
        setQuery(query);
        setBillIds(billIds);
        this.approveState = approveState;
    }

}
