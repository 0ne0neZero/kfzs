package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.Ignore;
import com.wishare.finance.apps.model.reconciliation.dto.FlowStatisticsDto;
import com.wishare.finance.apps.model.reconciliation.vo.FlowDetailComplexV;
import com.wishare.finance.domains.reconciliation.FlowDetailVo;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceDetailDto;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.dto.FlowReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 流水明细mapper
 * @author yancao
 */
@Mapper
public interface FlowDetailMapper extends BaseMapper<FlowDetailE> {

    /**
     * 根据id集合获取流水总金额
     *
     * @param idList 流水id集合
     * @return Long
     */
    FlowStatisticsDto statisticsAmount(@Param("idList") List<Long> idList);

    /**
     * 根据id集合获取流水总金额
     *
     * @return Long
     */
    FlowStatisticsDto statisticsAmount2(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);


    /**
     * 根据票据列表
     * @param invoiceIds
     * @return
     */
    List<FlowInvoiceDetailDto> getListByInvoiceIds(@Param("invoiceIds") List<Long> invoiceIds, @Param("state") Integer state);

    /**
     * 获取流水列表 对账用
     * @param invoiceIds
     * @param state
     * @return
     */
    List<FlowInvoiceReconciliationDetailDto> getReconciliationFlows(@Param("invoiceIds") List<Long> invoiceIds, @Param("state") Integer state);

    /**
     * 根据项目id 获取未对账的 以认领流水的记录
     */

    List<FlowReconciliationDetailDto> getNewReconciliationFlows(@Param("supCpUnitId") String supCpUnitId);

    int updateFlowClaimRecordFlag(@Param("invoiceIds") List<Long> invoiceIds);


    int updateReconcileFlag(@Param("flowClaimRecordId") Long flowClaimRecordId, @Param("reconcileFlag") Integer reconcileFlag);
    /**
     * 根据条件获取数据
     * @param queryModel
     * @return
     */
    List<FlowDetailE> listByConditions(@Param("ew") QueryWrapper<FlowDetailE> queryModel);

    /**
     * 根据流水id列表获取流水认领id列表
     * @param flowIds
     * @return
     */
    List<Long> getIdsByFlowIds(@Param("flowIds") List<Long> flowIds);

    /**
     * 查询流水明细
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<FlowDetailE> queryDetailPage(Page<FlowDetailE> page, @Param(Constants.WRAPPER)QueryWrapper<FlowDetailE> queryWrapper);

    /**
     * 查询流水明细
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<FlowDetailComplexV> queryFlowDetailComplexPage(Page<FlowDetailE> page, @Param(Constants.WRAPPER)QueryWrapper<FlowDetailE> queryWrapper);

    /**
     * 查询流水明细V2
     * @Param page page
     * @Param queryWrapper queryWrapper
     * @return 流水认领信息
     **/
    IPage<FlowDetailVo> queryDetailPage2(Page<FlowDetailE> page, @Param(Constants.WRAPPER) QueryWrapper<FlowDetailE> queryWrapper);


    List<FlowDetailE> queryDetailList(@Param(Constants.WRAPPER)QueryWrapper<FlowDetailE> queryWrapper);

    Set<String> querySerialNumbers(@Param("state") Integer state);

    FlowClaimRecordE getFlowClaimRecord(@Param("flowId") String flowId);


    List<FlowClaimDetailE> getFlowClaimDetailByInvoiceIds(@Param("invoiceIds") List<Long> invoiceIds);

    List<FlowClaimDetailE> getByFlowClaimRecordId(@Param("flowClaimRecordIds") List<Long> flowClaimRecordIds);

    Set<String> selectPayTime(@Param("invoiceIds") List<Long> invoiceIds);

    @InterceptorIgnore(tenantLine = "on")
    FlowDetailE getTenantId();

}
