package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.ContractCollectionPlanPageF;
import com.wishare.contract.domains.entity.contractset.ContractBondPlanE;
import com.wishare.contract.domains.vo.contractset.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同保证金计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Mapper
public interface ContractBondPlanMapper extends BaseMapper<ContractBondPlanE> {

    void deleteBondPlan(Long contractId);

    IPage<ContractBondPlanPageV> pageContractBondPlan(Page<?> pageF,
                                                      @Param("ew") QueryWrapper<?> queryModel);

    ContractBondPlanSumV pageContractBondPlanSum(@Param("ew") QueryWrapper<?> queryModel);

    void updateBillId(@Param("id") Long id, @Param("billId") Long billId);

    List<Long> contractBondPlanBillIds(@Param("contractId") Long contractId,
                                       @Param("bondType") Integer bondType,
                                       @Param("paymentStatus") Integer paymentStatus,
                                       @Param("refundStatus") Integer refundStatus,
                                       @Param("bidBond") Integer bidBond);

    ContractInfoV selectAmountByContract(@Param("contractId")Long contractId);

    List<ContractBondListV> selectByContractIds(@Param("contractId") Long contractId);
}
