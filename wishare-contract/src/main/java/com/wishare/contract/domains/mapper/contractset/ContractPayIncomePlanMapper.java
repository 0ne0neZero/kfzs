package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.PayIncomeListQuery;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.vo.revision.income.PayIncomeListV;
import com.wishare.contract.domains.vo.revision.income.settlement.IncomePlanPeriodV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 收款计划减免明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-07
 */
@Mapper
public interface ContractPayIncomePlanMapper extends BaseMapper<PayIncomePlanE> {

    IPage<PayIncomeListV> listPlan(Page<PayIncomeListQuery> pageF, @Param("ew") QueryWrapper<PayIncomeListQuery> queryModel);

    int updateIncomePlan(@Param("amountList") List<PayIncomePlanE> amountList);

    void deletePayIncomePlan(@Param("idList") List<String> idList);
    //根据结算计划ID及结算周期查询范围内的成本计划
    List<PayIncomePlanE> getCostListByPlanAndCostTime(@Param("contractId")String contractId, @Param("funId") String funId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
