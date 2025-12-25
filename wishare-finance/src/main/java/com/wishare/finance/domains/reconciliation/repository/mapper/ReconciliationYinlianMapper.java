package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimStatisticsV;
import com.wishare.finance.domains.reconciliation.dto.ChannelFlowClaimRecordDto;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Mapper
public interface ReconciliationYinlianMapper extends BaseMapper<ReconciliationYinlianE> {
    ReconciliationYinlianE getReconciliationYinlianEByFileName(@Param("fileName") String fileName);

    IPage<ChannelFlowClaimRecordDto>  pageRecord(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                 @Param("shareTableName")String shareTableName);



    IPage<ChannelFlowClaimRecordDto>  pageRecordForYY(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                 @Param("shareTableName")String shareTableName);

    ChannelFlowClaimStatisticsV getStatistics(@Param("shareTableName")String shareTableName,@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    ChannelFlowClaimStatisticsV getYYStatistics(@Param("shareTableName")String shareTableName,@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);


    List<ReconciliationYinlianE> getReconciliationYinlianBySeqId(@Param("seqList")List<String> seqList);


    List<ReconciliationYinlianE> getReconciliationYinlianByRefNo(@Param("seqList")List<String> seqList);

    List<ChannelFlowClaimRecordDto> pageRecordMap(@Param("list") Set<String> collect);

    List<ChannelFlowClaimRecordDto> pageRecordMapYY(@Param("list") Set<String> collect);
}
