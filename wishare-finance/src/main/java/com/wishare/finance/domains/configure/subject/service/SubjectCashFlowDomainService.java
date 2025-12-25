package com.wishare.finance.domains.configure.subject.service;

import com.wishare.finance.domains.configure.subject.entity.OldSubjectCashFlowE;
import com.wishare.finance.domains.configure.subject.repository.OldSubjectCashFlowRepository;
import java.util.List;

import com.wishare.finance.domains.configure.subject.repository.SubjectCashFlowRepository;
import com.wishare.finance.domains.configure.subject.entity.SubjectCashFlow;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 15:47
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCashFlowDomainService {

    private final SubjectCashFlowRepository subjectCashFlowRepository;
    private final OldSubjectCashFlowRepository oldSubjectCashFlowRepository;

    public void batchInsert(List<OldSubjectCashFlowE> oldSubjectCashFlowES) {
        oldSubjectCashFlowRepository.saveBatch(oldSubjectCashFlowES);
    }

    /**
     * 根据科目id列表获取现金流量信息列表
     * @param subjectIds 科目id列表
     * @return 现金流量信息列表
     */
    public List<SubjectCashFlow> getSubjectCashFlowBySubjectIds(List<Long> subjectIds){
       return subjectCashFlowRepository.listBySubjectIds(subjectIds);
    }

    /**
     * 根据映射信息获取科目现金流量信息
     * @param subjectId 科目id
     * @param mapUnitId 映射单元id
     * @param mapType 映射类别： 1科目，2现金流量
     * @return 科目现金流量信息
     */
    public List<SubjectCashFlow> getListByMapDetail(Long subjectId, Long mapUnitId, Integer mapType){
        return subjectCashFlowRepository.listByMapDetail(subjectId, mapUnitId, mapType);
    }

    /**
     * 批量新增或保存信息
     * @param cashFlows 现金流量信息
     * @param empty 空信息是否默认删除
     * @return
     */
    public boolean saveOrUpdateSubjectCashFlow(Long subjectId, List<SubjectCashFlow> cashFlows, boolean empty){
        if (CollectionUtils.isEmpty(cashFlows) && !empty){
            return false;
        }
        subjectCashFlowRepository.deleteBySubjectId(subjectId); //先删除再新增
        return subjectCashFlowRepository.saveOrUpdateBatch(cashFlows);
    }

}
