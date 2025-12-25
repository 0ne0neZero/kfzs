package com.wishare.contract.domains.mapper.revision.pay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementPeriodE;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.SettlementSimpleStr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface ContractPayConcludeSettlementPeriodMapper extends BaseMapper<ContractPayConcludeSettlementPeriodE> {

    int deleteBySettlementId(@Param("settlementId") String settlementId);


    List<PayPlanPeriodV> getPeriodList(@Param("settlementId") String settlementId);

    List<SettlementSimpleStr> getSimpleStrList(@Param("settlementIdList") List<String> settlementIdList);

    int insertBatch(@Param("periodList")List<ContractPayConcludeSettlementPeriodE> periodList);

    PayPlanPeriodV getPeriodDate(@Param("settlementId") String settlementId);
}
