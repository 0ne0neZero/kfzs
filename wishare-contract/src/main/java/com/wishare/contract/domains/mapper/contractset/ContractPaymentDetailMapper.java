package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同付款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-29
 */
@Mapper
public interface ContractPaymentDetailMapper extends BaseMapper<ContractPaymentDetailE> {

    void deleteByCollectionPlanId(Long collectionPlanId);

    void deleteByContractId(Long contractId);

    Long selectCountCurrentDate();

    Long selectOneByBpmRecordId(Long recordId);
}
