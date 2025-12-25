package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanConcludePageF;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同收入损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-24
 */
@Mapper
public interface ContractIncomeConcludeProfitLossMapper extends BaseMapper<ContractIncomeConcludeProfitLossE> {


    /**
     * 分页查询付款计划
     */
    IPage<ContractIncomePlanConcludeV> collectionPlanDetailPage(Page<ContractIncomePlanConcludePageF> pageF,
                                                                @Param("ew") QueryWrapper<ContractIncomePlanConcludePageF> queryModel);



    List<ContractIncomePlanConcludeV> getByContractId(@Param("contractId")String contractId);

}
