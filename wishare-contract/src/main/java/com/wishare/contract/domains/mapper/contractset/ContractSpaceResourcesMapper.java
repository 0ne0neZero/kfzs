package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Mapper
public interface ContractSpaceResourcesMapper extends BaseMapper<ContractSpaceResourcesE> {

    void deleteByContractId(Long contractId);
}
