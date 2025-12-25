package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.voucher.entity.VoucherBillDetailE;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/25 11:31
 * @version: 1.0.0
 */
@Mapper
public interface VoucherBillMapper extends BaseMapper<VoucherBillDetailE> {

}
