package com.wishare.contract.domains.mapper.revision.projectInitiation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectPlanMonthlyAllocationE;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 合约规划月度分摊Mapper接口
 */
@Mapper
public interface ContractProjectPlanMonthlyAllocationMapper extends BaseMapper<ContractProjectPlanMonthlyAllocationE> {

    /**
     * 物理删除（绕过逻辑删除拦截器）
     */
    @Delete("DELETE FROM contract_project_plan_monthly_allocation WHERE projectInitiationId = #{projectInitiationId} AND monthlyAllocationType = #{monthlyAllocationType} AND deleted = #{deleted}")
    int physicalDelete(@Param("projectInitiationId") String projectInitiationId,
                       @Param("monthlyAllocationType") Integer monthlyAllocationType,
                       @Param("deleted") Integer deleted);

    /**
     * 物理删除（绕过逻辑删除拦截器）
     */
    @Delete("DELETE FROM contract_project_plan_monthly_allocation WHERE contractProjectPlanId = #{contractProjectPlanId} AND monthlyAllocationType = #{monthlyAllocationType} AND deleted = #{deleted}")
    int physicalDeleteByContractProjectPlanId(@Param("contractProjectPlanId") String contractProjectPlanId,
                       @Param("monthlyAllocationType") Integer monthlyAllocationType,
                       @Param("deleted") Integer deleted);

    /**
     * 恢复上一版逻辑删除的月度分摊
     */
    @Update("UPDATE contract_project_plan_monthly_allocation SET deleted = 0 WHERE projectInitiationId = #{projectInitiationId} AND monthlyAllocationType = #{monthlyAllocationType} AND deleted = #{deleted}")
    boolean update(@Param("projectInitiationId") String projectInitiationId,
                       @Param("monthlyAllocationType") Integer monthlyAllocationType,
                       @Param("deleted") Integer deleted);


}