package com.wishare.contract.domains.mapper.revision.projectInitiation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectOrderE;
import org.apache.ibatis.annotations.Mapper;

/**
 * 立项审批关联订单Mapper接口
 */
@Mapper
public interface ContractProjectOrderMapper extends BaseMapper<ContractProjectOrderE> {
}