package com.wishare.finance.apps.service.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.SaveSubjectCashFlowCodesF;
import com.wishare.finance.apps.model.configure.subject.fo.SaveSubjectCashFlowF;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.service.CashFlowDomainService;
import com.wishare.finance.domains.configure.subject.entity.OldSubjectCashFlowE;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.service.SubjectCashFlowDomainService;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.starter.Global;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 15:31
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCashFlowAppService {

    private final SubjectCashFlowDomainService subjectCashFlowDomainService;

    private final SubjectDomainService subjectDomainService;

    private final CashFlowDomainService cashFlowDomainService;

    /**
     * 关联 科目与现金流项目
     * @param form
     * @return
     */
    public Boolean concatCashFlow(List<SaveSubjectCashFlowF> form) {
        List<OldSubjectCashFlowE> oldSubjectCashFlowES = Global.mapperFacade.mapAsList(form, OldSubjectCashFlowE.class);
        subjectCashFlowDomainService.batchInsert(oldSubjectCashFlowES);
        return Boolean.TRUE;
    }

    /**
     * 通过code  关联 科目与现金流项目
     * @param form
     * @return
     */
    public Boolean concatCashFlowByCodes(List<SaveSubjectCashFlowCodesF> form) {
        List<SubjectE> subjects = subjectDomainService.listByCodes(
            form.stream().map(SaveSubjectCashFlowCodesF::getSubjectCode).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(subjects)) {
            return Boolean.FALSE;
        }

        List<CashFlowE> cashFlows = cashFlowDomainService.listByCodes(
            form.stream().map(SaveSubjectCashFlowCodesF::getCashFlowCode).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(cashFlows)) {
            return Boolean.FALSE;
        }

        Map<String, Long> subjectMap = subjects.stream()
            .collect(Collectors.toMap(SubjectE::getSubjectCode, SubjectE::getId));
        Map<String, Long> cashFlowMap = cashFlows.stream()
            .collect(Collectors.toMap(CashFlowE::getCode, CashFlowE::getId));

        List<OldSubjectCashFlowE> oldSubjectCashFlowES = new ArrayList<>();

        for (SaveSubjectCashFlowCodesF saveSubjectCashFlowCodesF : form) {
            OldSubjectCashFlowE oldSubjectCashFlowE = Global.mapperFacade.map(saveSubjectCashFlowCodesF, OldSubjectCashFlowE.class);
            oldSubjectCashFlowE.setSubjectId(subjectMap.get(saveSubjectCashFlowCodesF.getSubjectCode()));
            oldSubjectCashFlowE.setCashFlowId(cashFlowMap.get(saveSubjectCashFlowCodesF.getCashFlowCode()));
            oldSubjectCashFlowES.add(oldSubjectCashFlowE);
        }

        subjectCashFlowDomainService.batchInsert(oldSubjectCashFlowES);
        return Boolean.TRUE;
    }
}
