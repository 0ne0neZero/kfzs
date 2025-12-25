package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordStatisticsV;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流水领用记录mapper
 * @author yancao
 */
@Mapper
public interface FlowClaimRecordMapper extends BaseMapper<FlowClaimRecordE> {

    /**
     * 分页查询领用记录
     *
     * @param page page
     * @param queryWrapper queryWrapper
     * @return IPage
     */
    IPage<FlowClaimRecordE> queryRecordPage(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 根据流水ID获取流水认领记录ID
     * @param flowId
     * @return
     */
    Long queryIdByFlowId(@Param("flowId") Long flowId);

    List<Long> queryIdByFlowIds(@Param("flowIds") List<Long> flowIds);


    IPage<FlowRecordPageV> getPageRecord(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    FlowRecordStatisticsV getFlowRecordStatistics(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    List<Long> queryIdByRecordIds(@Param("recordIds") List<Long> recordIds);
}
