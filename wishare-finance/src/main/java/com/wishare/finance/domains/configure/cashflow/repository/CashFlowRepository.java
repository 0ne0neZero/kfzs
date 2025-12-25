package com.wishare.finance.domains.configure.cashflow.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.cashflow.comamnd.CashFlowDtoQuery;
import com.wishare.finance.domains.configure.cashflow.dto.CashFlowDto;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.repository.mapper.CashFlowMapper;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 13:09
 * @version: 1.0.0
 */
@Service
public class CashFlowRepository extends ServiceImpl <CashFlowMapper, CashFlowE> {


    /**
     * 根据科目id查现金流量
     * @param subjectId
     * @param isMain
     */
    public List<CashFlowE> listCashFlowBySubjectId(Long subjectId, boolean isMain) {
        return baseMapper.listCashFlowBySubjectId(subjectId, isMain);
    }

    /**
     * 根据科目code查现金流量
     * @param codes
     * @return
     */
    public List<CashFlowE> listByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<CashFlowE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CashFlowE::getCode, codes);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 批量新增或更新
     * @param cashFlows
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<CashFlowE> cashFlows) {
        return baseMapper.saveOrUpdate(cashFlows) > 0;
    }

    /**
     * 查询上级现金流量
     * @param code 编码
     * @return 上级现金流量
     */
    public CashFlowE getSupByCode(String code) {
        return baseMapper.selectSupByCode(code);
    }

    /**
     * 根据科目id获取现金流量信息
     * @param subejctId 科目id
     * @return 现金量流量列表
     */
    public List<CashFlowE> listBySubjectId(Long subejctId){
        return baseMapper.selectBySubjectId(subejctId);
    }

    /**
     * 根据科目id获取现金流量信息
     * @param subjectIds 科目id列表
     * @return 现金量流量列表
     */
    public List<CashFlowE> listBySubjectIds(List<Long> subjectIds) {
        return baseMapper.selectBySubjectIds(subjectIds);
    }


    /**
     * 查询现金流量信息
     * @param query 查询信息
     * @return 现金流量信息 限制500条
     */
    public List<CashFlowDto> getDtoByCodeName(CashFlowDtoQuery query){
        return baseMapper.selectDtoByCodeName(query);
    }

}
