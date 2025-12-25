package com.wishare.contract.domains.mapper.revision.pay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeSettlementContractSnapshotE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author longhuadmin
 */
@Mapper
public interface ContractPayConcludeSettlementContractSnapshotMapper extends BaseMapper<ContractPayConcludeSettlementContractSnapshotE> {

    ContractPayConcludeSettlementContractSnapshotE querySnapshotByContractId(@Param("contractId") String contractId);

    int deleteBySettlementId(@Param("settlementId") String settlementId);

    ContractPayConcludeSettlementContractSnapshotE querySnapshotBySettlementId(@Param("settlementId") String settlementId);
}
