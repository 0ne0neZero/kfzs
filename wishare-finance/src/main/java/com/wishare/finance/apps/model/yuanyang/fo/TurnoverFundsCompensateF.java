package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 资金上缴 补偿信息
 */
@Getter
public class TurnoverFundsCompensateF {
    /**
     * 交易id
     */
    private String transactionNo;

    /**
     * 资金上缴流程入参
     */
    private TurnoverFundsReimbursementF turnoverFundsReimbursementF;
    public TurnoverFundsCompensateF setTurnoverFundsReimbursementF(TurnoverFundsReimbursementF turnoverFundsReimbursementF) {
        this.turnoverFundsReimbursementF = turnoverFundsReimbursementF;
        return this;
    }



    /**
     * 账簿信息
     */
    private List<AccountBookE> accountBooks;

    public TurnoverFundsCompensateF setAccountBook(List<AccountBookE> accountBooks) {
        this.accountBooks = accountBooks;
        return this;
    }



    /**
     * 法定单位信息
     */
    private List<OrgFinanceRv> orgFinances;

    public TurnoverFundsCompensateF setOrgFinance(List<OrgFinanceRv> orgFinances) {
        this.orgFinances = orgFinances;
        return this;
    }

    /**
     * 科目信息
     */
    private List<SubjectE> subjects;

    public TurnoverFundsCompensateF setSubjects(List<SubjectE> subjects) {
        this.subjects = subjects;
        return this;
    }

    /**
     * 费项信息
     */
    private Map<String, ChargeItemE> chargeItemMap;

    public TurnoverFundsCompensateF setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }


}
