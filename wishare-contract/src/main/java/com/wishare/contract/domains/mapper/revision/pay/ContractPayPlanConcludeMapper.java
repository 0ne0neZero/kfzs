package com.wishare.contract.domains.mapper.revision.pay;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludePageF;
import com.wishare.contract.apps.fo.revision.pay.SettlementParamQuery;
import com.wishare.contract.apps.fo.revision.pay.SettlementPlanListQuery;
import com.wishare.contract.apps.fo.revision.pay.report.ContractPayReportDetailListV;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementPeriodF;
import com.wishare.contract.apps.fo.revision.pay.settlement.PayPlanPeriodF;
import com.wishare.contract.domains.bo.CommonRangeDayAmountBO;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeInfoV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeSumV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanInnerInfoV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV2;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.contract.domains.vo.settle.SettlePlanListV;
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
public interface ContractPayPlanConcludeMapper extends BaseMapper<ContractPayPlanConcludeE> {
    /**
     * 分页查询付款计划
     */
    IPage<ContractPayPlanConcludeV> collectionPlanDetailPage(Page<ContractPayPlanConcludePageF> pageF,
                                                             @Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryModel);

    /**
     * 分页查询付款计划
     */
    IPage<ContractPayPlanConcludeInfoV> pageInfo(Page<ContractPayPlanConcludePageF> pageF,
                                                 @Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryModel);

    /**
     * 分页查询付款计划
     */
    List<ContractPayPlanConcludeV> queryByPath(@Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryWrapper,
                                                @Param("parentIdList") List<String> parentIdList,
                                                @Param("tenantId")String tenantId);

    /**
     * 金额汇总
     */
    ContractPayPlanConcludeSumV accountAmountSum(@Param("ew") QueryWrapper<ContractPayPlanConcludePageF> queryModel);



    List<ContractPayPlanConcludeV> getHowOrder(@Param("contractId")String contractId, @Param("howOrder") Integer howOrder);

    List<ContractPayPlanConcludeInfoV> getHowOrderInfo(@Param("contractId")String contractId, @Param("howOrder") Integer howOrder);

    List<ContractPayPlanConcludeV> getChoosePlanInfo(@Param("nameNo")String nameNo);

    List<ContractPayPlanConcludeV> getChoosePlanInfoBak(@Param("nameNo")String nameNo, @Param("isNK")Integer isNK, @Param("communityId") String communityId);

    List<ContractPayPlanConcludeV> getChoosePlanInfoNew(@Param("nameNo") String nameNo, @Param("orgIds") Set<String> orgIds, @Param("isNK")Integer isNK, @Param("communityId") String communityId);


    List<ContractPayPlanConcludeV> getPayPlanByTermDate(@Param("contractId")String contractId, @Param("termDate") Integer termDate);

    List<PayPlanPeriodV> getPlanPeriod(@Param("contractId") String contractId,
                                       @Param("date") String date);

    List<ContractPayPlanForSettlementV> getPlanList(@Param("contractId") String contractId,
                                                    @Param("date") Date date);

    List<ContractPayPlanForSettlementV> getOriginPlanList(@Param("contractId") String contractId,
                                                          //@Param("periodList") List<PayPlanPeriodF> periodList,
                                                          @Param("date") Date date,
                                                          @Param("typeList") List<Integer> typeList);

    ContractPayPlanForSettlementV2 getPlanListByPlanId(@Param("planId") String planId);

    IPage<SettlePlanListV> listPlan(Page<SettlementPlanListQuery> pageF, @Param("ew")QueryWrapper<SettlementPlanListQuery> queryModel, @Param("query")SettlementParamQuery param);

    List<ContractPayPlanConcludeE> queryByCostTime(@Param("planIds") List<String> planIds, @Param("periodList") List<PayPlanPeriodV> periodList);

    List<ContractPayPlanConcludeE> queryByCostTimeNotFinished(@Param("planIds") List<String> planIds, @Param("periodList") List<PayPlanPeriodV> periodList);

    List<ContractPayPlanInnerInfoV> getInnerInfoByContractId(@Param("contractIds") List<String> contractIds);

    List<ContractPayPlanConcludeE> queryByCostTimeForBill(@Param("planIds") List<String> planIds, @Param("periodList") List<PayPlanPeriodV> periodList);

    List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdsForPayApp(@Param("contractIds") List<String> contractIds);

    List<ContractPayPlanConcludeE> getEffectivePlan(@Param("contractId") String contractId, @Param("contractIds") List<String> contractIds);

    List<String> getUsedPlanIdListOnSettlement(@Param("planIdList") List<String> planIdList);
    List<String> getNkUsedPlanIdListOnSettlement(@Param("planIdList") List<String> planIdList);

    List<String> getUsedPlanIdListOnPayCostPlan(@Param("planIdList") List<String> planIdList);

    List<String> notSettlementGroup(@Param("contractId") String contractId);

    //根据条件获取支出报表底数-已发生未结算数据
    List<ContractPayReportDetailListV> getDetailPayReportPlanList(@Param("paymentDateSatrt") String paymentDateSatrt,
                                                                        @Param("paymentDateEnd") String paymentDateEnd,
                                                                        @Param("plannedCollectionTimeStart") String plannedCollectionTimeStart,
                                                                        @Param("plannedCollectionTimeEnd") String plannedCollectionTimeEnd,
                                                                        @Param("contractList") List<String> contractList);

    int updatePayPlan(@Param("amountList") List<CommonRangeDayAmountBO> amountList);
    //还原结算计划
    int restoreplan(@Param("planIdList") List<String> planIdList);

    List<ContractPayPlanConcludeE> getFunDateList(@Param("contractId") String contractId,@Param("fundidList") List<String> fundidList, @Param("periodList") List<ContractPaySettlementPeriodF> periodList);
}
