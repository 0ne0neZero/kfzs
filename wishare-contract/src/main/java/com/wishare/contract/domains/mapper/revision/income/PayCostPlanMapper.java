package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.PayIncomeListQuery;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.PayCostPlanE;
import com.wishare.contract.domains.vo.revision.income.PayIncomeListV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanPageV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 成本计划
 * </p>
 *
 * @author ljx
 * @since 2022-11-07
 */
@Mapper
public interface PayCostPlanMapper extends BaseMapper<PayCostPlanE> {


    IPage<PayCostPlanPageV> pageInfo(Page<?> pageF,
                                     @Param("ew") QueryWrapper<?> queryModel);

    List<String> getNoListByPrefix(@Param("prefix") String prefix);

    List<PayCostPlanV> queryPlanVListByPlanIdList(@Param("contractId")String contractId, @Param("funId") String funId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int updatePayPlan(@Param("amountList") List<PayCostPlanE> amountList);

    //根据结算计划ID及结算周期查询范围内的成本计划
    List<PayCostPlanE> getCostListByPlanAndCostTime(@Param("contractId")String contractId, @Param("funId") String funId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
