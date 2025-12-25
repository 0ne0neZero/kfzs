package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeSettlementPlanRelationE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementPlanRelationE;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanForSettlementV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV;
import com.wishare.contract.domains.vo.revision.pay.settlement.SettlementSimpleStr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface ContractIncomeConcludeSettlementPlanRelationMapper extends BaseMapper<ContractIncomeConcludeSettlementPlanRelationE> {

    int deleteBySettlementId(@Param("settlementId") String settlementId);

    List<ContractIncomePlanForSettlementV> getPlanList(@Param("settlementId") String id);

    List<SettlementSimpleStr> getSimpleStrList(@Param("settlementIdList") List<String> settlementIdList);

    List<SettlementSimpleStr> getSettlementPlanIdSimpleStrList(@Param("settlementIdList") List<String> settlementIdList,
                                                               @Param("start") Date start,
                                                               @Param("end") Date end);

    int insertBatch(@Param("relationList") List<ContractIncomeConcludeSettlementPlanRelationE> relationList);

    List<String> selectExistRuningSettlement(@Param("planIdList") List<String> planIdList);
}
