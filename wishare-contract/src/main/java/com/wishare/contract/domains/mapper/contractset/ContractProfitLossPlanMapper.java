package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanF;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossPlanE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.contractset.ContractInfoV;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossPlanV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangrui
 * @since 2022-09-13
 */
@Mapper
public interface ContractProfitLossPlanMapper extends BaseMapper<ContractProfitLossPlanE> {

    void deleteProfitLossPlan(@Param("collectionPlanId") Long collectionPlanId,@Param("contractId") Long contractId);

    BigDecimal selectAmountSum(Long id);

    BigDecimal localCurrencyAmount(Long id);

    List<ContractProfitLossPlanV> getProfitLossPlanList(@Param("p") ContractProfitLossPlanF profitLossPlanF);

    void updateBillId( @Param("id")  Long id ,@Param("billId") Long billId);

    List<ContractProfitLossPlanV> selectProfitLossPlanByIds(@Param("ids") List<Long> ids);

    List<ContractProfitLossPlanV> selectByContract(@Param("contractId") Long contractId);

    void deletePlan(@Param("contractId") Long contractId);

    ContractInfoV selectAmountByContract(@Param("contractId")Long contractId);
}
