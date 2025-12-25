package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.PayBillDto;
import com.wishare.finance.domains.bill.entity.PayBill;
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
public interface PayBillMapper extends BaseMapper<PayBill> {

    /**
     * 分页查询已审核付款单列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    IPage<PayBillDto> queryPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 统计付款单信息
     *
     * @param wrapper 统计条件
     * @return BillTotalDto
     */
    BillTotalDto queryTotal(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    /**
     * 根据id获取付款单
     *
     * @param payBillId 付款单id
     * @return PayBillDto
     */
    PayBillDto queryById(@Param(value = "payBillId") Long payBillId);

    /**
     * 根据id获取付款单
     *
     * @param payBillIdList 付款单id
     * @return PayBillDto
     */
    List<PayBillDto> queryByIdList(@Param(value = "payBillIdList") List<Long> payBillIdList);

    /**
     * 获取结算账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> pageBillInferenceInfo(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<BillInferenceV> queryModel);

    /**
     * 获取冲销作废账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> pageBillInferenceOffInfo(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<BillInferenceV> queryModel);

    /**
     * 查询凭证付款结算业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherPayBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                        @Param("sceneType") Integer sceneType,
                                                        @Param("businessBillType") Integer businessBillType, @Param("tableName") String tableName);

    @InterceptorIgnore(tenantLine = "true")
    int updateAccountId(@Param("sbAccountId") Long sbAccountId, @Param("pnAccountId") Long pnAccountId, @Param("list") List<Long> idList);
}
