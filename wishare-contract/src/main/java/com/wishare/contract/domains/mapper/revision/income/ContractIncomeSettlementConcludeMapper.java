package com.wishare.contract.domains.mapper.revision.income;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludePageF;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomeSettlementPageV2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 支出合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Mapper
public interface ContractIncomeSettlementConcludeMapper extends BaseMapper<ContractIncomeSettlementConcludeE> {


    /**
     * 分页查询付款计划
     */
    IPage<ContractIncomeSettlementConcludeV> collectionPaySettlementPage(Page<ContractIncomeSettlementConcludePageF> pageF,
                                                                         @Param("ew") QueryWrapper<ContractIncomeSettlementConcludePageF> queryModel);



    /**
     * 金额汇总
     */
    ContractIncomeSettlementConcludeSumV accountAmountSum(@Param("ew") QueryWrapper<ContractIncomeSettlementConcludePageF> queryModel);


    /**
     * 通过合同查是否有确收单
     */
    ContractIncomeSettlementConcludeV getContractIncomeSettlementInfo(@Param("contractId")String contractId);

    /**
     * V2.12 - 维护结算单的步骤信息
     **/
    int updateSettlementStep(@Param("settlementId") String settlementId, @Param("step") Integer step);

    /**
     * 更新审核状态
     **/
    void updateReviewStatus(@Param("settlementId") String settlementId,
                            @Param("reviewStatus") Integer reviewStatus);


    IPage<String> getPidsByCondition(Page<?> pageF,
                                     @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询V2
     * 根据pid查询结算单信息[无合同信息]
     **/
    List<ContractIncomeSettlementPageV2> selectPageV2ByPids(@Param("pids") List<String> pids,
                                                            @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询V2
     * 根据合同id查询合同维度数据
     **/
    List<ContractIncomeSettlementPageV2> selectPageV2OfContract(@Param("contractIdList") List<String> contractIdList);

    List<String> checkSettleStatus(@Param("list")List<String> planIdList);

    List<ContractIncomeSettlementConcludeV> getContractIncomeSettlementList(@Param("list")List<String> planIdList);

    List<String> queryBySettleId(@Param("settleId")String settleId);

    ContractIncomeSettlementConcludeV getApprovingContractPaySettlementInfo(@Param("contractId") String contractId);

    List<String> getSettlementByPlan(@Param("list")List<String> planIdList);
    List<String> getPlanBySettlement(@Param("settlementId") String settlementId);
    int deletedSettlement(@Param("id") String id);
}
