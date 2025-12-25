package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 对账记录表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-10-12
 */
@Mapper
public interface ReconciliationMapper extends BaseMapper<ReconciliationE> {
    ReconciliationClearStatisticsV getReconciliationClearStatistics(@Param(Constants.WRAPPER) QueryWrapper<ReconciliationE> queryWrapper);



    ReconciliationStatisticsV getReconciliationStatistics(@Param(Constants.WRAPPER) QueryWrapper<ReconciliationE> queryWrapper);

}
