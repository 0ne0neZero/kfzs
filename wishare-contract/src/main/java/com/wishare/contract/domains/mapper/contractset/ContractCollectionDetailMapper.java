package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.entity.contractset.ContractCollectionDetailE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.contractset.CollectionDetailPlanV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同收款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Mapper
public interface ContractCollectionDetailMapper extends BaseMapper<ContractCollectionDetailE> {

    List<CollectionDetailPlanV> selectByCollectionPlanId(@Param("contractId") Long contractId,
                                                         @Param("collectionPlanId") Long collectionPlanId,
                                                         @Param("tenantId") String tenantId);

    void deleteByCollectionPlanId(Long collectionPlanId);

    void deleteByContractId(Long contractId);

    Long selectCountCurrentDate();
}
