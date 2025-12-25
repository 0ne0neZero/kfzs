package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.entity.PayableBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应付账单mapper
 *
 * @author yancao
 */
@Mapper
public interface PayableBillMapper extends BaseMapper<PayableBill> {

    /**
     * 根据账单ids批量获取应收账单信息（忽略租户隔离）
     * @param billIds
     * @return
     */
    List<PayableBill> getByIdsNoTenant(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);

    /**
     *  修正开票状态
     * 【注】此处定时任务触发，暂时无法解析上下文的租户id
     * @param billEList
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean updateInvoiceState(@Param("billEList") List<PayableBill> billEList);

    /**
     * 获取交账信息列表
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(@Param("billIds") List<Long> billIds,@Param("supCpUnitId")String supCpUnitId);

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param queryWrapper queryWrapper
     * @return BillTotalDto
     */
    BillTotalDto statisticsBillRefund(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 催缴欠缴账单统计
     *
     * @param queryWrapper queryWrapper
     * @return BillTotalDto
     */
    BillTotalDto call(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 根据检索条件统计账单金额总数
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryTotal(@Param("ew") QueryWrapper<?> wrapper);

    /**
     * 根据审核状态查询账单合计
     *
     * @return List
     */
    List<BillApproveTotalDto> queryApproveTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 统计账单合计查询
     *
     * @param query query
     * @return BillDiscountTotalDto
     */
    BillDiscountTotalDto queryDiscountTotal(@Param("query") BillDiscountTotalQuery query);

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param wrapper wrapper
     * @return IPage
     */
    IPage<PayableBill> queryBillByPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取所有初始审核应收账单
     *
     * @param wrapper wrapper
     * @return List
     */
    IPage<PayableBill> listByInitialBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 获取账单审核统计
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询预收账单信息
     *
     * @param page page
     * @param wrapper wrapper
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    IPage<PayableBill> queryBillByPageNoTenantLine(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 查询应收计提事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryAccrualBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 联合账单调整信息查询账单推凭信息
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryAdjustBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询冲销作废事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryOffBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询结账销账事件对应的账单
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryCloseBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询账单推凭信息
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> queryBillInferenceByPage(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询凭证应付业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherPayableBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                            @Param("sceneType") Integer sceneType,
                                                            @Param("businessBillType") Integer businessBillType, @Param("tableName") String tableName);

    @InterceptorIgnore(tenantLine = "true")
    int updateInferenceState(@Param("list") List<Long> idList,
                             @Param("inferenceState") Integer inferenceState);
}
