package com.wishare.contract.domains.mapper.revision.income;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanConcludePageF;
import com.wishare.contract.apps.fo.revision.income.IncomePlanListQuery;
import com.wishare.contract.apps.fo.revision.income.settlement.IncomePlanPeriodF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementPeriodF;
import com.wishare.contract.domains.bo.CommonRangeDayAmountBO;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeInfoV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.income.IncomePlanListV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanForSettlementV;
import com.wishare.contract.domains.vo.revision.income.settlement.IncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanInnerInfoV;
import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 支出合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Mapper
public interface ContractIncomePlanConcludeMapper extends BaseMapper<ContractIncomePlanConcludeE> {

    /**
     * 分页查询付款计划
     */
    IPage<ContractIncomePlanConcludeV> collectionPlanDetailPage(Page<ContractIncomePlanConcludePageF> pageF,
                                                                @Param("ew") QueryWrapper<ContractIncomePlanConcludePageF> queryModel);

    /**
     * 分页查询付款计划
     */
    IPage<ContractIncomePlanConcludeInfoV> pageInfo(Page<ContractIncomePlanConcludePageF> pageF,
                                                    @Param("ew") QueryWrapper<ContractIncomePlanConcludePageF> queryModel);

    List<ContractIncomePlanConcludeV> queryByPath(@Param("ew") QueryWrapper<ContractIncomePlanConcludePageF> queryWrapper,
                                        @Param("parentIdList") List<String> parentIdList,
                                        @Param("tenantId")String tenantId);


    /**
     * 金额汇总
     */
    ContractIncomePlanConcludeSumV accountAmountSum(@Param("ew") QueryWrapper<ContractIncomePlanConcludePageF> queryModel);


    List<ContractIncomePlanConcludeV> getHowOrder(@Param("contractId")String contractId, @Param("howOrder") Integer howOrder);

    List<ContractIncomePlanConcludeInfoV> getHowOrderInfo(@Param("contractId")String contractId, @Param("howOrder") Integer howOrder);

    /**
     * 查询还有remindDays天过去的收款计划以及其所属合同相关信息
     *
     * @param remindDays
     * @return
     */
    List<ContractAndPlanInfoV> selectRemindContractPlan(@Param("remindDays") List<Integer> remindDays);

    /**
     * 查询已经过期了overdueDays天的收款计划以及其所属合同相关信息
     *
     * @param overdueDays
     * @return
     */
    List<ContractAndPlanInfoV> selectOverdueContractPlan(@Param("overdueDays") List<Integer> overdueDays);

    IPage<IncomePlanListV> listPlan(Page<IncomePlanListQuery> pageF,  @Param("ew")QueryWrapper<IncomePlanListQuery> queryModel);

    List<IncomePlanListV> queryByNewPath( @Param("ew") QueryWrapper<IncomePlanListQuery> queryWrapper,
                                             @Param("parentIdList") List<String> parentIdList,
                                             @Param("tenantId")String tenantId);

    List<IncomePlanPeriodV> getPlanPeriod(@Param("contractId") String contractId);

    List<ContractIncomePlanForSettlementV> getPlanList(@Param("contractId") String contractId,
                                                       @Param("date") Date date);

    List<ContractIncomePlanForSettlementV> getOriginPlanList(@Param("contractId") String contractId,
                                            @Param("periodList")List<IncomePlanPeriodF> periodList);

    List<ContractIncomePlanForSettlementV> getOriginPlanDateList(@Param("contractId") String contractId,
                                                                 @Param("splitMode") Integer splitMode,
                                                             @Param("date") Date date);

    List<ContractIncomePlanConcludeV> getChoosePlanInfoNew(@Param("nameNo") String nameNo, @Param("orgIds") Set<String> orgIds);

    List<ContractIncomePlanConcludeE> queryByCostTime(@Param("planIds") List<String> planIds, @Param("periodList") List<IncomePlanPeriodV> periodList);

    List<ContractIncomePlanConcludeE> queryByCostTimeNotFinished(@Param("planIds") List<String> planIds, @Param("periodList") List<IncomePlanPeriodV> periodList);

    List<ContractPayPlanInnerInfoV> getInnerInfoByContractId(@Param("contractIds") List<String> contractIds);

    List<ContractIncomePlanConcludeE> queryByCostTimeForBill(@Param("planIds") List<String> planIds, @Param("periodList") List<IncomePlanPeriodV> periodList);

    List<ContractIncomePlanConcludeE> getEffectivePlan(@Param("contractId") String contractId);

    List<String> getUsedPlanIdListOnSettlement(@Param("planIdList") List<String> planIdList);

    List<String> getUsedPlanIdListOnPayCostPlan(@Param("planIdList") List<String> planIdList);

    List<String> notSettlementGroup(@Param("contractId") String contractId);

    int updateIncomePlan(@Param("amountList") List<CommonRangeDayAmountBO> amountList);
    //还原结算计划
    int restoreplan(@Param("planIdList") List<String> planIdList);

    List<ContractIncomePlanConcludeE> getFunDateList(@Param("contractId") String contractId, @Param("fundidList") List<String> fundidList, @Param("periodList") List<IncomePlanPeriodV> periodList);
}
