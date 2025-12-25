package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeSettlementContractSnapshotE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementContractSnapshotE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author longhuadmin
 */
@Mapper
public interface ContractIncomeConcludeSettlementContractSnapshotMapper extends BaseMapper<ContractIncomeConcludeSettlementContractSnapshotE> {

    ContractIncomeConcludeSettlementContractSnapshotE querySnapshotByContractId(@Param("contractId") String contractId);

    int deleteBySettlementId(@Param("settlementId") String settlementId);

    ContractIncomeConcludeSettlementContractSnapshotE querySnapshotBySettlementId(@Param("settlementId") String settlementId);
}
