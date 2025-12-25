package com.wishare.contract.domains.mapper.revision.pay.bill;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludePageF;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:20
 */
@Mapper
public interface ContractSettlementsBillMapper extends BaseMapper<ContractSettlementsBillE> {

    /**
     * 分页查询付款计划
     */
    IPage<ContractPayBillV> settlementsBillForContractDetail(Page<ContractPayPlanConcludePageF> pageF,
                                                             @Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryModel);


    List<ContractPayBillV> getDetailsByIdSettle(@Param("oppositeId")String oppositeId);
}
