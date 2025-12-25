package com.wishare.contract.domains.mapper.revision.pay;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.pay.ContractPaySettlementConcludePageF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementF;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementConcludeInfoV;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementConcludeSumV;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementPeriodAmountV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPaySettlementPageV2;
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
public interface ContractPaySettlementConcludeMapper extends BaseMapper<ContractPaySettlementConcludeE> {


    /**
     * 分页查询付款计划
     */
    IPage<ContractPaySettlementConcludeV> collectionPaySettlementPage(Page<ContractPaySettlementConcludePageF> pageF,
                                                                   @Param("ew") QueryWrapper<ContractPaySettlementConcludePageF> queryModel);


    /**
     * 分页查询付款计划
     */
    List<ContractPaySettlementConcludeV> queryByPath(@Param("ew") QueryWrapper<ContractPaySettlementConcludePageF> queryWrapper,
                                               @Param("parentIdList") List<String> parentIdList,
                                               @Param("tenantId")String tenantId);

    /**
     * 分页查询付款计划
     */
    IPage<ContractPaySettlementConcludeInfoV> queryByPathInfo(Page<ContractPaySettlementConcludePageF> pageF,
                                                             @Param("ew") QueryWrapper<ContractPaySettlementConcludePageF> queryModel);

    /**
     * 分页查询付款计划
     */
    ContractPaySettlementConcludeSumV accountAmountSum(@Param("ew") QueryWrapper<ContractPaySettlementConcludePageF> queryModel);


    /**
     * 通过合同查是否有付款结算单
     */
    ContractPaySettlementConcludeV getContractPaySettlementInfo(@Param("contractId")String contractId);

    /**
     * 分页查询V2
     * 获取指定条件对应的pid[锁定合同]
     **/
    IPage<String> getPidsByCondition(Page<?> pageF,
                                     @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询V2
     * 根据pid查询结算单信息[无合同信息]
     **/
    List<ContractPaySettlementPageV2> selectPageV2ByPids(@Param("pids") List<String> pids,
                                                         @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询V2
     * 根据合同id查询合同维度数据
     **/
    List<ContractPaySettlementPageV2> selectPageV2OfContract(@Param("contractIdList") List<String> contractIdList);

    /**
     * V2.12 - 维护结算单的步骤信息
     **/
    int updateSettlementStep(@Param("settlementId") String settlementId, @Param("step") Integer step);

    /**
     * 结算单-分页 金额汇总V2
     **/
    ContractPaySettlementConcludeSumV accountAmountSum2(@Param("ew") QueryWrapper<?> queryModel);

    /**
     * 更新审核状态
     **/
    void updateReviewStatus(@Param("settlementId") String settlementId,
                            @Param("reviewStatus") Integer reviewStatus);

    List<String> checkSettleStatus(@Param("list")List<String> planIdList);

    List<String> queryBySettleId(@Param("settleId")String settleId);

    ContractPaySettlementConcludeV getApprovingContractPaySettlementInfo(@Param("contractId") String contractId);

    List<ContractPaySettlementF> calculateTotalAmount(@Param("list") List<String> settlementIdList);
    //更新结算单中其他附件-业务事由
    int updateOtherBusinessReasons(@Param("settlementId") String settlementId, @Param("otherBusinessReasons") String otherBusinessReasons, @Param("externalDepartmentCode") String externalDepartmentCode,@Param("calculationMethod") Integer calculationMethod);

    List<String> getSettlementByPlan(@Param("list")List<String> planIdList);
    List<String> getPlanBySettlement(@Param("settlementId") String settlementId);


    int deletedSettlement(@Param("id") String id);

    List<ContractPaySettlementPeriodAmountV> getSettlementPeriodAmount(@Param("contractIdList") List<String> contractIdList);
    //获取审批通过结算单ID
    List<String> getApprovedSettlementId(@Param("contractIdList") List<String> contractIdList);
}
