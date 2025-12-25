package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimGatherDetailE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowClaimGatherDetailMapper extends BaseMapper<FlowClaimGatherDetailE> {
    List<Long> queryIdByRecordIds(@Param("recordIds") List<Long> recordIds);
}
