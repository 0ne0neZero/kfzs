package com.wishare.contract.domains.mapper.revision.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludePageF;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeProfitLossE;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@Mapper
public interface ContractPayConcludeProfitLossMapper extends BaseMapper<ContractPayConcludeProfitLossE> {

    List<ContractPayPlanConcludeV> getByContractId(@Param("contractId")String contractId);

    /**
     * 分页查询付款计划
     */
    IPage<ContractPayPlanConcludeV> collectionPlanDetailPage(Page<ContractPayPlanConcludePageF> pageF,
                                                             @Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryModel);
}
