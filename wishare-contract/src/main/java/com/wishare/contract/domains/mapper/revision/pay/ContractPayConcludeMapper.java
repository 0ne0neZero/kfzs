package com.wishare.contract.domains.mapper.revision.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeQuery;
import com.wishare.contract.apps.fo.revision.pay.report.*;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.revision.MockJson;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public interface ContractPayConcludeMapper extends BaseMapper<ContractPayConcludeE> {

    List<ContractPayConcludeV> queryInfo(@Param("nameNo")String nameNo, @Param("tenantId")String tenantId, @Param("contractId")String contractId, @Param("isNK")Integer isNK);

    List<ContractPayConcludeV> queryInfoBak(@Param("nameNo")String nameNo, @Param("tenantId")String tenantId, @Param("contractId")String contractId, @Param("isNK")Integer isNK);

    List<ContractPayConcludeV> queryInfoNew(@Param("nameNo")String nameNo, @Param("tenantId")String tenantId, @Param("contractId")String contractId, @Param("orgIds") Set<String> orgIds, @Param("isNK")Integer isNK);

    List<ContractPayConcludeV> queryContractInfo(@Param("id")String id);

    List<ContractPayConcludeV> queryContractCount(@Param("type")Integer type, @Param("contractNo")String contractNo, @Param("communityName")Integer communityName);


    List<ContractPayConcludeV>  queryIsExistContract(@Param("id")String id);

    IPage<ContractPayConcludeTreeV> collectionPayConcludePage(Page<ContractPayConcludeE> pageF,
                                                              @Param("ew") QueryWrapper<ContractPayConcludeE> queryModel);

    /**
     * 分页查询付款计划
     */
    List<ContractPayConcludeTreeV> queryByPath(@Param("ew") QueryWrapper<ContractPayConcludeE> queryWrapper,
                                           @Param("parentIdList") List<String> parentIdList);

    IPage<ContractPayConcludeTreeV> queryByPathV2(Page<ContractPayConcludeE> pageF, @Param("ew") QueryWrapper<ContractPayConcludeE> queryWrapper);

    /**
     * 查询还有expireDays天过期的支出合同
     *
     * @param expireDays
     * @return
     */
    List<ContractAndPlanInfoV> selectPayExpireContract(List<Integer> expireDays);

    /**
     * 查询非补充协议的，指定部门的合同号
     **/
    List<String> queryContractNosByRangeTimeAndCertainPropertyNotSupply(@Param("departId") String departId,
                                                                        @Param("isBackDate") Integer isBackDate,
                                                                        @Param("communityId") String communityId,
                                                                        @Param("startTime") Date startTime,
                                                                        @Param("endTime") Date endTime,
                                                                        @Param("year") Integer year);

    /**
     * 查询补充协议的，指定部门的合同号
     **/
    List<String> queryContractNosByRangeTimeAndCertainPropertySupply(@Param("departId") String departId,
                                                                     @Param("isBackDate") Integer isBackDate,
                                                                     @Param("communityId") String communityId,
                                                                     @Param("startTime") Date startTime,
                                                                     @Param("endTime") Date endTime,
                                                                     @Param("year") Integer year);

    /**
     * 查询需要推送中交的合同id
     **/
    List<String> queryToPullZjContractIds();

    /**
     * 推送失败计数器
     **/
    int incrementPullZjFailCountById(@Param("id") String id);

    /**
     * 查询指定合同的补充协议-编号
     **/
    List<String> queryContractNosByPid(@Param("pid") String pid);

    ContractPayConcludeE queryByContractId(@Param("contractId") String contractId);

    IPage<MockJson> mockJson(Page<MockJson> pageF);

    void updateContractInUse(@Param("targetDate") LocalDate targetDate);

    List<ContractPayConcludeE> getPageShowNumV2(@Param("ew") QueryWrapper<ContractPayConcludeQuery> queryWrapper);

    //根据条件获取支出报表底数
    List<ContractPayDataListD> getTotalPayReportList(ContractPayReportF reportF);

    //根据条件获取支出报表底数-应结算金额汇总
    List<ContractPayDataListD> getTotalPayReportSettlementList(@Param("planIdList") List<String> planIdList,
                                                               @Param("paymentDateSatrt") String paymentDateSatrt,
                                                               @Param("paymentDateEnd") String paymentDateEnd);

    IPage<ContractPayReportDetailListV> getDetailPayReportList(Page<ContractPayReportF> pageF,
                                                               @Param("paymentDateSatrt") String paymentDateSatrt,
                                                               @Param("paymentDateEnd") String paymentDateEnd,
                                                               @Param("plannedCollectionTimeStart") String plannedCollectionTimeStart,
                                                               @Param("plannedCollectionTimeEnd") String plannedCollectionTimeEnd,
                                                               @Param("contractList") List<String> contractList,
                                                               @Param("isNK") Integer isNK);
    //根据条件获取支出报表底数-已发生已结算金额汇总
    List<ContractPayReportDetailListV> getDetailPayReportSettlementList(@Param("paymentDateSatrt") String paymentDateSatrt,
                                                                        @Param("paymentDateEnd") String paymentDateEnd,
                                                                        @Param("plannedCollectionTimeStart") String plannedCollectionTimeStart,
                                                                        @Param("plannedCollectionTimeEnd") String plannedCollectionTimeEnd,
                                                                        @Param("contractList") List<String> contractList);

    ContractPayConcludeE queryNKContractById(@Param("contractId") String contractId);

    IPage<ContractPayYjListV> getYjDataAnalysisReport(Page<ContractPayReportF> pageF,
                                                      @Param("ew") ContractPayReportF reportSelect);


    int updateGmtExpireEndById(@Param("id") String id, @Param("gmtExpireEnd") LocalDate gmtExpireEnd);
    int updateChangContractAmountById(@Param("id") String id, @Param("changContractAmount") BigDecimal changContractAmount);

    List<ContractPayConcludeE> queryNKContractList(@Param("contractList") List<String> contractList);

    String getContractNoNum(@Param("communityId") String communityId, @Param("year") String year);

    int updateNKContractById(@Param("id") String id, @Param("nkStatus") Integer nkStatus, @Param("bpmStatus") Integer bpmStatus, @Param("bpmProcInstId") String bpmProcInstId, @Param("bpmApprovalDate") LocalDateTime bpmApprovalDate);

    IPage<ContractPayNkTotalListV> getNKDataAnalysisTotalReport(Page<ContractPayReportF> pageF,
                                                                @Param("ew") ContractPayReportF reportSelect);
}
