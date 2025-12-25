package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.contractset.ContractMaterialListE;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同采购物资清单
 * </p>
 *
 * @author ljx
 * @since 2022-09-29
 */
@Mapper
public interface ContractMaterialListMapper extends BaseMapper<ContractMaterialListE> {

    void deleteByContractId(Long contractId);
}
