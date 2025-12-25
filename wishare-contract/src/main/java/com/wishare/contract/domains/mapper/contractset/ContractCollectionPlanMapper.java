package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.ContractCollectionPlanPageF;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.contractset.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同收款计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Mapper
public interface ContractCollectionPlanMapper extends BaseMapper<ContractCollectionPlanE> {

    void deleteCollectionPlan(@Param("contractId") Long contractId);

    /**
     * 分页查询收款/付款计划
     */
    IPage<ContractCollectionPlanDetailV> collectionPlanDetailPage(Page<ContractCollectionPlanPageF> pageF,
                                                                  @Param("ew") QueryWrapper<ContractCollectionPlanPageF> queryModel);

    /**
     * 根据合同id查询是否有已开票或已收付款的计划
     */
    Integer checkContractByPlan(@Param("contractId")Long id);
    /**
     * 收款/付款计划金额总和
     */
    CollectionPlanSumV collectionPlanAmountSum(@Param("ew") QueryWrapper<ContractCollectionPlanPageF> queryModel);

    List<ContractCollectionPlanV> collectionExpire(@Param("tenantId") String tenantId,
                                                   @Param("contractId") Long contractId,
                                                   @Param("contractNature") Integer contractNature,
                                                   @Param("id") Long id,
                                                   @Param("flag") Boolean flag);

    List<ContractCollectionPlanV> collectionAdvent(@Param("tenantId") String tenantId,
                                                   @Param("contractId") Long contractId,
                                                   @Param("contractNature") Integer contractNature,
                                                   @Param("id") Long id,
                                                   @Param("flag") Boolean flag,
                                                   @Param("dayNum") Integer dayNum);

    void updateWarnState(@Param("id")Long id,@Param("warnState") Integer warnState);

    ContractInfoV selectByContract(@Param("contractId")Long contractId);

    List<ContractBillListV> selectByContractIds(@Param("contractId") Long contractId);

    List<ContractCollectionPlanStatisticsV> selectCollectionPlanStatistics(@Param("contractNature") Integer contractNature,
                                                                           @Param("year") String year,
                                                                           @Param("tenantId") String tenantId);
}
