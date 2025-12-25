package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.dto.contractset.ContractProfitLossBillD;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossBillE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 损益账单关联表
 * </p>
 *
 * @author ljx
 * @since 2022-10-17
 */
@Mapper
public interface ContractProfitLossBillMapper extends BaseMapper<ContractProfitLossBillE> {

    /**
     * 查询未开票的信息
     * @return
     */
    List<ContractProfitLossBillD> invoiceGetTask();
}
