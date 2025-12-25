package com.wishare.contract.domains.vo.revision.pay;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author hhb
 * @describe
 * @date 2025/8/14 23:47
 */
@Getter
@Setter
public class ContractPayYjSubListV {
    //结算周期
    //private String splitMode;
    //结算周期
    //private String splitModeName;
    private String periodKey;
    //周期明细
    private String periodsDetail;
    //原合同-本期结算金额
    private BigDecimal bqSettlementAmount;
    //原合同-累计结算金额
    private BigDecimal totalSettlementAmount;
    //原合同-累计已审批结算金额
    //private BigDecimal totalApprovalSettlementAmount;
    //YJ后-本期结算金额
    private BigDecimal yjBqSettlementAmount;
    //YJ后-累计结算金额
    private BigDecimal yjTotalSettlementAmount;
    //YJ后-累计已审批结算金额
    //private BigDecimal yjTotalApprovalSettlementAmount;
    //差额-本期结算金额
    private BigDecimal ceBqSettlementAmount;
    //差额-累计结算金额
    private BigDecimal ceTotalSettlementAmount;
    //差额-累计已审批结算金额
    //private BigDecimal ceTotalApprovalSettlementAmount;
}
