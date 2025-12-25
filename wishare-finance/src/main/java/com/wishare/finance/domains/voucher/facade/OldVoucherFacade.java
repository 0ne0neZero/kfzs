package com.wishare.finance.domains.voucher.facade;

import com.wishare.finance.apps.model.configure.chargeitem.vo.AssisteAccountV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectDetailV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectV;
import com.wishare.finance.apps.service.configure.chargeitem.AssisteAccountAppService;
import com.wishare.finance.apps.service.configure.subject.SubjectAppService;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.RateRv;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description: 凭证科目
 * @author: pgq
 * @since: 2022/10/26 11:12
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OldVoucherFacade {

    private final SubjectAppService subjectAppService;

    private final ExternalClient externalClient;

    private final AssisteAccountAppService assisteAccountAppService;

    /**
     * 根据科目id获取科目名称
     * @param id
     * @return
     */
    public String getSubjectName(Long id) {
        return subjectAppService.getNameById(id);
    }

    /**
     * 根据科目id获取科目信息
     * @param id
     */
    public SubjectDetailV getSubjectById(Long id) {
        return subjectAppService.getById(id);
    }

    /**
     * 查询科目
     * @param subjects
     * @return
     */
    public List<SubjectV> listByIds(List<Long> subjects) {
        return subjectAppService.listByIds(subjects);
    }

    /**
     * 推凭
     * @param form
     * @return
     */
    public SendresultV externalInference(UfinterfaceF form) {
        return externalClient.addVouchers(form);
    }

    /**
     * 根据费项id获取对应科目
     * @param headSubjectId 一级科目
     * @param chargeItemId 费项id
     */
    public SubjectV getSubjectByChargeItemIdAndHeadSubjectId(Long headSubjectId, Long chargeItemId) {
        return subjectAppService.getSubjectByChargeItemIdAndHeadSubjectId(headSubjectId, chargeItemId);
    }

    /**
     * 批量根据code获取辅助核算信息
     * @param auxiliaryCountList
     */
    public List<AssisteAccountV> listAssistAccount(List<String> auxiliaryCountList) {
        if (CollectionUtils.isEmpty(auxiliaryCountList)) {
            return Collections.emptyList();
        }
        return assisteAccountAppService.assisteAccountListByCodes(auxiliaryCountList);
    }

    /**
     * 获取所有增值税税率
     * @return
     */
    public List<RateRv> listRate() {
        return externalClient.listRate();
    }
}
