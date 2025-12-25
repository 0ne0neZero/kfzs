package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.repository.mapper.ReconciliationMapper;
import org.springframework.stereotype.Service;

/**
 * 对象单资源库
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Service
public class ReconciliationRepository extends ServiceImpl<ReconciliationMapper, ReconciliationE> {
    public ReconciliationClearStatisticsV getReconciliationClearStatistics(QueryWrapper<ReconciliationE> queryWrapper){
        return baseMapper.getReconciliationClearStatistics(queryWrapper);
    }

    public ReconciliationStatisticsV getReconciliationStatistics(QueryWrapper<ReconciliationE> queryWrapper){
        return baseMapper.getReconciliationStatistics(queryWrapper);
    }

}
