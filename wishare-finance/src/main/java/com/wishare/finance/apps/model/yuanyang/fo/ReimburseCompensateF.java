package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;

import java.util.List;
import java.util.Map;

/**
 * 支付报销补偿信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/5/4
 */
public class ReimburseCompensateF {

    /**
     * 交易id
     */
    private String transactionNo;

    /**
     * 报销流程入参
     */
    private ReimbursementF reimbursementF;

    /**
     * 账簿信息
     */
    private AccountBookE accountBook;

    /**
     * 业务单元
     */
    private BusinessUnitE businessUnit;

    /**
     * 科目信息
     */
    private List<SubjectE> subjects;

    /**
     * 费项信息
     */
    private Map<String, ChargeItemE> chargeItemMap;

    public String getTransactionNo() {
        return transactionNo;
    }

    public ReimburseCompensateF setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }

    public ReimbursementF getReimbursementF() {
        return reimbursementF;
    }

    public ReimburseCompensateF setReimbursementF(ReimbursementF reimbursementF) {
        this.reimbursementF = reimbursementF;
        return this;
    }

    public AccountBookE getAccountBook() {
        return accountBook;
    }

    public ReimburseCompensateF setAccountBook(AccountBookE accountBook) {
        this.accountBook = accountBook;
        return this;
    }

    public BusinessUnitE getBusinessUnit() {
        return businessUnit;
    }

    public ReimburseCompensateF setBusinessUnit(BusinessUnitE businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public List<SubjectE> getSubjects() {
        return subjects;
    }

    public ReimburseCompensateF setSubjects(List<SubjectE> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Map<String, ChargeItemE> getChargeItemMap() {
        return chargeItemMap;
    }

    public ReimburseCompensateF setChargeItemMap(Map<String, ChargeItemE> chargeItemMap) {
        this.chargeItemMap = chargeItemMap;
        return this;
    }
}
