package com.wishare.finance.domains.configure.cashflow.service;

import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.repository.CashFlowRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 13:07
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CashFlowDomainService {

    private final CashFlowRepository cashFlowRepository;

    /**
     * 批量插入
     * @param firstCashFlow
     */
    public void batchInsert(List<CashFlowE> firstCashFlow) {
        cashFlowRepository.saveOrUpdateBatch(firstCashFlow);
    }

    /**
     * 根据科目查现金流量
     * @param subjectId
     * @param isMain
     * @return
     */
    public List<CashFlowE> listCashFlowBySubjectId(Long subjectId, boolean isMain) {
       return cashFlowRepository.listCashFlowBySubjectId(subjectId, isMain);
    }

    /**
     * 根据codes获取
     * @param codes
     * @return
     */
    public List<CashFlowE> listByCodes(List<String> codes) {
        return cashFlowRepository.listByCodes(codes);
    }

    /**
     * 批量新增或更新
     * @param cashFlows
     * @return
     */
    public boolean syncCashFlow(List<CashFlowE> cashFlows) {
        if (CollectionUtils.isEmpty(cashFlows)){
            return false;
        }
        List<String> cashFlowCodes = cashFlows.stream().map(CashFlowE::getCode).collect(Collectors.toList());
        //获取已存在的现金流量信息
        List<CashFlowE> oldCashFlows = cashFlowRepository.listByCodes(cashFlowCodes);
        Map<String, CashFlowE> oldCashFlowMap = new HashMap<>();
        for (CashFlowE oldCashFlow : oldCashFlows) {
            oldCashFlowMap.put(oldCashFlow.getCode(), oldCashFlow);
        }
        CashFlowE mCashFlow = null;
        cashFlows.sort(Comparator.comparing(CashFlowE::getCode)); //根据编码排序
        Map<String, List<Long>> pathMap = new HashMap<>();
        List<Long> mPath = null;
        List<Long> path = null;
        for (CashFlowE cashFlow : cashFlows) {
            mCashFlow = oldCashFlowMap.get(cashFlow.getCode());
            if(Objects.nonNull(mCashFlow)){
                //如果已存在
                cashFlow.setId(mCashFlow.getId());
                cashFlow.setPath(mCashFlow.getPath());
            }else {
                cashFlow.init();
                //获取上级现金流量信息
                CashFlowE supCashFlow = cashFlowRepository.getSupByCode(cashFlow.getCode());
                path = new ArrayList<>();
                if (Objects.nonNull(supCashFlow)){
                    mPath = supCashFlow.getPath();
                    path.addAll(mPath);
                }else {
                    mPath = pathMap.get(cashFlow.getFCode());
                    if (Objects.nonNull(mPath)){
                        path.addAll(mPath);
                    }
                }
                path.add(cashFlow.getId());
                cashFlow.setPath(path);
                pathMap.put(cashFlow.getCode(), path);
            }
        }
        return cashFlowRepository.saveOrUpdateBatch(cashFlows);
    }

    /**
     * 根据科目id获取现金流量信息
     * @param subjectId 科目id
     * @return 现金流量列表
     */
    public List<CashFlowE> listBySubjectId(Long subjectId) {
        return cashFlowRepository.listBySubjectId(subjectId);
    }

    public List<CashFlowE> listBySubjectId(List<Long> subjectIds) {
        return cashFlowRepository.listBySubjectIds(subjectIds);
    }

}
