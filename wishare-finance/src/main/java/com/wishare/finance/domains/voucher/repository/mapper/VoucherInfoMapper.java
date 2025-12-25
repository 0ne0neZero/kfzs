package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 凭证明细 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Mapper
public interface VoucherInfoMapper extends BaseMapper<Voucher> {

    /**
     * 分页查询凭证明细
     * @param page 分页信息
     * @param queryWrapper 查询条件
     * @return
     */
    Page<Voucher> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 统计凭证金额
     * @param queryWrapper
     * @return
     */
    Long staticVoucherAmount(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    Long staticVoucherAmountByTableName(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,@Param("tableName") String tableName);

    /**todo 此接口暂时无人调用，且需要传入账单项目
     * 根据业务单据
     * @param businessId
     * @param businessType
     * @return
     */
    List<Voucher> selectListByBusinessId(@Param("businessId") Long businessId, @Param("businessType") int businessType);

    Integer deleteByIds(@Param("ids")List<Long> ids);

    Integer deleteBusinessDetailByIds(@Param("voucherIds")List<Long> voucherIds, @Param("accountBookId") Long accountBookId);

    Integer selectBusinessDetailByVouchIds(@Param("voucherIds")List<Long> ids, @Param("accountBookId") Long accountBookId);

    List<Long> autoPushList(@Param("recordId") Long recordId);

    Page<Voucher> selectDetailsBySearch(Page<SearchF<?>> searchFPage,@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,@Param("tableName") String tableName);
}
