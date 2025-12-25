package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.apps.model.voucher.vo.VoucherMiniV;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/25 11:31
 * @version: 1.0.0
 */
@Mapper
public interface VoucherBusinessDetailMapper extends BaseMapper<VoucherBusinessDetail> {




    List<VoucherMiniV> fillInAccountBookId();

    void upAccountBookId(@Param("voucherId") Long voucherId, @Param("accountBookId") Long accountBookId);

}
