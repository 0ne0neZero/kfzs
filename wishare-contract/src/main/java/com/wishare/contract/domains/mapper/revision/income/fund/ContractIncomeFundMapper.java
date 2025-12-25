package com.wishare.contract.domains.mapper.revision.income.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 收入合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Mapper
public interface ContractIncomeFundMapper extends BaseMapper<ContractIncomeFundE> {

    List<ContractIncomeFundV> getContractPaySettFundList(@Param("contractId") String contractId);

    //根据收入合同ID获取清单费项数据
    List<FunChargeItemF> getFundChargeItemById(@Param("contractId") String contractId);

    int deleteByContractId(@Param("contractId") String contractId);
}
