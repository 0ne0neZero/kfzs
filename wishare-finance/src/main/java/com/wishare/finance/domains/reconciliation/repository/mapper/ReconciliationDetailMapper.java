package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 对账单明细表,管理对账单单条账单的对账信息 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-10-13
 */
public interface ReconciliationDetailMapper extends BaseMapper<ReconciliationDetailE> {
    ReconciliationClearStatisticsV reconciliationClearDetailStatistics(@Param(Constants.WRAPPER) QueryWrapper<ReconciliationDetailE> queryWrapper);



    ReconciliationStatisticsV reconciliationDetailStatistics(@Param(Constants.WRAPPER) QueryWrapper<ReconciliationDetailE> queryWrapper);


    List<ReconciliationDetailE> getByBillIds(@Param("billIds") List<Long> billIds);
    List<ReconciliationDetailE> getByBillIdsAndReconciliationId(@Param("billIds") List<Long> billIds, @Param("reconciliationId") Long reconciliationId);

    List<ReconciliationDetailE>  getByReconciliationId(@Param("reconciliationId") Long reconciliationId);


    Page<ReconciliationDetailE> reconciliationDetailPageYuan(Page<T> of,
                                                                 @Param(Constants.WRAPPER) QueryWrapper<ReconciliationDetailE> queryModel);

    Page<ReconciliationDetailE> reconciliationDetailPageNoYuan(Page<Object> of,
                                                               @Param(Constants.WRAPPER) QueryWrapper<ReconciliationDetailE> queryModel);

    List<ReconciliationDetailE> getByBillIdsAndResult(@Param("billIds") List<Long> billIds);
}
