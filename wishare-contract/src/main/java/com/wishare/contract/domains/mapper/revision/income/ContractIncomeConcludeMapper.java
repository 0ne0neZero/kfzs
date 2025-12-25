package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeQuery;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeTreeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 收入合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Mapper
public interface ContractIncomeConcludeMapper extends BaseMapper<ContractIncomeConcludeE> {

    List<ContractIncomeConcludeV> queryInfo(@Param("nameNo")String nameNo, @Param("tenantId")String tenantId, @Param("contractId")String contractId);

    List<ContractIncomeConcludeV> queryContractCount(@Param("type")Integer type, @Param("contractNo")String contractNo, @Param("communityName")Integer communityName);

    List<ContractIncomeConcludeV> queryIsExistContract(@Param("id")String id);

    IPage<ContractIncomeConcludeTreeV> collectionIncomeConcludePage(Page<ContractIncomeConcludeE> pageF,
                                                                    @Param("ew") QueryWrapper<ContractIncomeConcludeE> queryModel);

    /**
     * 分页查询付款计划
     */
    List<ContractIncomeConcludeTreeV> queryByPath(@Param("ew") QueryWrapper<ContractIncomeConcludeE> queryWrapper,
                                                  @Param("parentIdList") List<String> parentIdList);

    /**
     * 查询还有expireDays天过期的收入合同
     *
     * @param expireDays
     * @return
     */
    List<ContractAndPlanInfoV> selectIncomeExpireContract(@Param("expireDays") List<Integer> expireDays);

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
     * 获取指定父合同id的补充协议编号
     **/
    List<String> queryContractNosByPid(@Param("pid") String pid);

    List<ContractIncomeConcludeV> queryInfoNew(@Param("nameNo")String nameNo, @Param("tenantId")String tenantId,
                                               @Param("contractId")String contractId, @Param("orgIds") Set<String> orgIds);

    ContractIncomeConcludeE queryByContractId(@Param("contractId") String contractId);

    void updateContractInUse(@Param("targetDate") LocalDate targetDate);

    IPage<ContractIncomeConcludeTreeV> queryByPathV2(Page<ContractIncomeConcludeE> pageF, @Param("ew") QueryWrapper<ContractIncomeConcludeE> queryWrapper);

    List<ContractIncomeConcludeE> getPageShowNumV2(@Param("ew") QueryWrapper<ContractIncomeConcludeQuery> queryWrapper);

    String getContractNoNum(@Param("communityId") String communityId, @Param("year") String year);

    int updateIsCorrectionAndPlan(@Param("id") String id, @Param("isCorrectionAndPlan") Integer isCorrectionAndPlan);
}
