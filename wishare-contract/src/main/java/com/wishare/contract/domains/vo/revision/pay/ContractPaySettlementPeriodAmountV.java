package com.wishare.contract.domains.vo.revision.pay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hhb
 * @describe
 * @date 2025/12/5 18:01
 */
@Data
public class ContractPaySettlementPeriodAmountV {
    //合同ID
    private String contractId;
    //周期开始时间
    private Date startDate;
    //周期结束时间
    private Date endDate;
    //实际结算金额
    private BigDecimal actualSettlementAmount;
    //结算单ID
    private String settlementId;
}
