package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
@Mapper
public interface BillRefundMapper extends BaseMapper<BillRefundE> {

    /**
     * 分页查询应收账单退款列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<ReceivableBillRefundDto> queryReceivableRefundPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 查询账应收账单退款列表
     *
     * @param queryModel
     * @return
     */
    List<ReceivableBillRefundDto> queryReceivableRefundList(@Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询账临时账单退款列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<TempChargeBillRefundDto> queryTempChargeRefundPage(Page<?> of, @Param("ew")  QueryWrapper<?> queryModel);

    /**
     * 查询临时账单退款列表
     *
     * @param queryModel
     * @return
     */
    List<TempChargeBillRefundDto> queryTempChargeRefundList(@Param("ew")QueryWrapper<?> queryModel);

    /**
     * 分页查询预收账单退款列表
     *
     * @param queryModel
     * @return
     */
    Page<AdvanceBillRefundDto> queryAdvanceRefundPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 批量更新退款记录的推凭状态
     * @param inferRefundIds
     */
    void batchUpdateRefundInferenceState(@Param("list") List<Long> inferRefundIds);
}
