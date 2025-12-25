package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.bill.entity.BillHandE;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 交账记录明细
 * @author: pgq
 * @since: 2022/10/9 14:40
 * @version: 1.0.0
 */
@Mapper
public interface BillHandMapper extends BaseMapper<BillHandE> {

}
