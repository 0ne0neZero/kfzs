package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeSettlementPeriodE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementPeriodE;
import com.wishare.contract.domains.vo.revision.income.settlement.IncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.SettlementSimpleStr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface ContractIncomeConcludeSettlementPeriodMapper extends BaseMapper<ContractIncomeConcludeSettlementPeriodE> {

    int deleteBySettlementId(@Param("settlementId") String settlementId);


    List<IncomePlanPeriodV> getPeriodList(@Param("settlementId") String settlementId);

    List<SettlementSimpleStr> getSimpleStrList(@Param("settlementIdList") List<String> settlementIdList);

    int insertBatch(@Param("periodList")List<ContractIncomeConcludeSettlementPeriodE> periodList);
}
