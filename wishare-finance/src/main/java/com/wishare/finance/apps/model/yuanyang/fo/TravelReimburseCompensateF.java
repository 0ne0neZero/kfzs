package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;

import java.util.List;
import java.util.Map;

/**
 * 差旅报销补偿信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/5/4
 */
public class TravelReimburseCompensateF {

    /**
     * 交易id
     */
    private String transactionNo;

    /**
     * 报销流程入参
     */
    private TravelReimbursementF travelReimbursementF;

    /**
     * 账簿信息
     */
    private AccountBookE accountBook;

    /**
     * 业务单元信息
     */
    private BusinessUnitE businessUnit;

    /**
     * 科目信息
     */
    private List<SubjectE> subjects;

    /**
     * 费项信息
     */
    private ChargeItemE chargeItem;

    /**
     * 税率信息
     */
    private Map<String, TaxRateE> taxRateMap;


    public String getTransactionNo() {
        return transactionNo;
    }

    public TravelReimburseCompensateF setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }

    public TravelReimbursementF getTravelReimbursementF() {
        return travelReimbursementF;
    }

    public TravelReimburseCompensateF setTravelReimbursementF(TravelReimbursementF travelReimbursementF) {
        this.travelReimbursementF = travelReimbursementF;
        return this;
    }

    public AccountBookE getAccountBook() {
        return accountBook;
    }

    public BusinessUnitE getBusinessUnit() {
        return businessUnit;
    }

    public TravelReimburseCompensateF setBusinessUnit(BusinessUnitE businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public TravelReimburseCompensateF setAccountBook(AccountBookE accountBook) {
        this.accountBook = accountBook;
        return this;
    }

    public List<SubjectE> getSubjects() {
        return subjects;
    }

    public TravelReimburseCompensateF setSubjects(List<SubjectE> subjects) {
        this.subjects = subjects;
        return this;
    }

    public ChargeItemE getChargeItem() {
        return chargeItem;
    }

    public TravelReimburseCompensateF setChargeItem(ChargeItemE chargeItem) {
        this.chargeItem = chargeItem;
        return this;
    }

    public Map<String, TaxRateE> getTaxRateMap() {
        return taxRateMap;
    }

    public TravelReimburseCompensateF setTaxRateMap(Map<String, TaxRateE> taxRateMap) {
        this.taxRateMap = taxRateMap;
        return this;
    }
}
