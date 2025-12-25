package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流水领用记录mapper
 *
 * @author yancao
 */
@Mapper
public interface FlowClaimDetailMapper extends BaseMapper<FlowClaimDetailE> {

}
