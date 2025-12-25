package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.voucher.entity.BusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Mapper
public interface BusinessBillMapper extends BaseMapper<BusinessBill> {

    BusinessBill getByVoucherId(@Param("voucherId") String voucherId);

}
