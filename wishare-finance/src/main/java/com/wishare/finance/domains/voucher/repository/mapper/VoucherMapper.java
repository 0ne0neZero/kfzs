package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 20:06
 * @version: 1.0.0
 */
@Mapper
public interface VoucherMapper extends BaseMapper<VoucherE> {

    Page<VoucherE> queryPage(Page<Object> of, @Param("ew") QueryWrapper<?> queryModel);

    Long staticVoucherAmount(@Param("ew") QueryWrapper<?> queryModel);

    void updateVoucherNoByIds(@Param("ids") List<Long> ids, @Param("reason") String reason, @Param("state") int state);
}
