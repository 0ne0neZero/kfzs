package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.ContractConcludeF;
import com.wishare.contract.apps.fo.contractset.PageContractTemplateF;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.contractset.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 合同订立信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Mapper
public interface ContractConcludeMapper extends BaseMapper<ContractConcludeE> {

    Integer selectContractCount(@Param("pid") Long pid,@Param("tenantId")String tenantId);

    ContractConcludeSumV amountSum(@Param("contractIds") List<Long> contractIds);

    List<ConcludeInfoV> listContractConclude(@Param("p") ContractConcludeF contractConcludeF);

    List<ConcludeInfoV> relevanceListContractConclude(@Param("p") ContractConcludeF contractConcludeF);

    List<ContractDetailsV> contractList();

    void updateContractState(@Param("id")Long id);

    IPage<ContractConcludeV> queryByPage(Page<ContractConcludeE> pageF,
                                         @Param("ew")QueryWrapper<ContractConcludeE> queryWrapper,
                                         @Param("tenantId")String tenantId);

    List<ContractConcludeV> queryByPath(@Param("ew") QueryWrapper<ContractConcludeE> queryWrapper,
                                        @Param("parentIdList") List<Long> parentIdList,
                                        @Param("tenantId")String tenantId);


    List<ContractDetailsV> expireContract(@Param("tenantId") String tenantId,@Param("id") Long id,@Param("flag") Boolean flag);

    List<ContractDetailsV> contractAdvent(@Param("tenantId") String tenantId,@Param("id") Long id,@Param("flag") Boolean flag,@Param("dayNum") Integer dayNum);

    void updateWarnState(@Param("id")Long id,@Param("warnState") Integer warnState);

    Long checkContract(@Param("id") Long id, @Param("signingMethod") Integer signingMethod);

    IPage<ContractInfoV> queryByContractPage(Page<ContractConcludeE> pageF,
                                         @Param("ew")QueryWrapper<ContractConcludeE> queryWrapper,
                                         @Param("tenantId")String tenantId,
                                             @Param("dayDate") String dayDate,
                                             @Param("method") Integer method,
                                             @Param("dayDateStart") String dayDateStart,
                                             @Param("dayDateEnd") String dayDateEnd);

    List<ContractInfoV> queryByContractPath(@Param("ew") QueryWrapper<ContractConcludeE> queryWrapper,
                                            @Param("parentIdList") List<Long> parentIdList,
                                            @Param("tenantId") String tenantId,
                                            @Param("dayDate") String dayDate,
                                            @Param("method") Integer method,
                                            @Param("dayDateStart") String dayDateStart,
                                            @Param("dayDateEnd") String dayDateEnd);

    ContractAccountSumV countContractSum(@Param("contractIds") List<Long> contractIds);

    void contractBackups(@Param("pid") Long pid);

    List<ContractPlanV> selectContractTimeList(@Param("tenantId") String tenantId);

    Long selectOneByBpmRecordId(Long recordId);
}
