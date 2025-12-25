package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 交易订单表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-06
 */
@Mapper
public interface TransactionOrderMapper extends BaseMapper<TransactionOrder> {

    TransactionOrder getByVoucherId(@Param("voucherId") String voucherId);

}
