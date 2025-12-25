package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.PayerF;
import com.wishare.finance.apps.model.bill.fo.SceneF;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/9/6
 */
@Getter
public class BusinessFundsCompensateF {

    /**
     * 交易id
     */
    private String transactionNo;

    public BusinessFundsCompensateF setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }

    /**
     * 资金下拨流程入参
     */
    private BusinessProcessHandleF businessProcessHandleF;

    public BusinessFundsCompensateF setBusinessProcessHandleF(BusinessProcessHandleF businessProcessHandleF) {
        this.businessProcessHandleF = businessProcessHandleF;
        return this;
    }

    /**
     * 账簿信息
     */
    private List<AccountBookE> accountBooks;

    public BusinessFundsCompensateF setAccountBooks(List<AccountBookE> accountBooks) {
        this.accountBooks = accountBooks;
        return this;
    }

    /**
     * 法定单位信息
     */
    private List<OrgFinanceRv> orgFinanceRvs;

    public BusinessFundsCompensateF setOrgFinances(List<OrgFinanceRv> orgFinances) {
        this.orgFinanceRvs = orgFinances;
        return this;
    }

    private List<BusinessUnitE> businessUnits;

    public BusinessFundsCompensateF setBusinessUnits(List<BusinessUnitE> businessUnits) {
        this.businessUnits = businessUnits;
        return this;
    }

    /**
     * 科目信息
     */
    private List<SubjectE> subjects;

    public BusinessFundsCompensateF setSubjects(List<SubjectE> subjects) {
        this.subjects = subjects;
        return this;
    }

}
