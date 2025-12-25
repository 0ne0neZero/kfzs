package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/11 19:06
 * @version: 1.0.0
 */
@Mapper
public interface VoucherInferenceRecordMapper extends BaseMapper<VoucherInferenceRecordE> {

    Page<VoucherInferenceRecordE> queryPage(Page<Object> page, @Param("ew") QueryWrapper<?> queryModel);
}
