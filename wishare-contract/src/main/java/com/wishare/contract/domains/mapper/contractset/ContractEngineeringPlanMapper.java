package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.entity.contractset.ContractEngineeringPlanE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工程类合同计提信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@Mapper
public interface ContractEngineeringPlanMapper extends BaseMapper<ContractEngineeringPlanE> {

    Integer selectContractCount(@Param("tenantId")String tenantId);
}
