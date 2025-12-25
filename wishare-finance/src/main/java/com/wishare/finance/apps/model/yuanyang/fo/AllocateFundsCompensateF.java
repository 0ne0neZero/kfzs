package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import lombok.Getter;

import java.util.List;

/**
 * 资金下拨补偿信息
 */
@Getter
public class AllocateFundsCompensateF {
    /**
     * 交易id
     */
    private String transactionNo;

    public AllocateFundsCompensateF setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }

    /**
     * 资金下拨流程入参
     */
    private AllocateFundsReimbursementF allocateFundsReimbursementF;

    public AllocateFundsCompensateF setAllocateFundsReimbursementF(AllocateFundsReimbursementF allocateFundsReimbursementF) {
        this.allocateFundsReimbursementF = allocateFundsReimbursementF;
        return this;
    }

    /**
     * 账簿信息
     */
    private AccountBookE accountBook;

    public AllocateFundsCompensateF setAccountBook(AccountBookE accountBook) {
        this.accountBook = accountBook;
        return this;
    }

    /**
     * 法定单位信息
     */
    private OrgFinanceRv orgFinanceRv;

    public AllocateFundsCompensateF setOrgFinance(OrgFinanceRv orgFinance) {
        this.orgFinanceRv = orgFinance;
        return this;
    }

    /**
     * 科目信息
     */
    private List<SubjectE> subjects;

    public AllocateFundsCompensateF setSubjects(List<SubjectE> subjects) {
        this.subjects = subjects;
        return this;
    }
}
