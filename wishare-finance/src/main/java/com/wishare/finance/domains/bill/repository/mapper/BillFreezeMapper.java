package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.bill.entity.BillFreezeE;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhenghui
 * @date 2023/7/14
 * @Description:跳收实体mapper
 */
@Mapper
public interface BillFreezeMapper extends BaseMapper<BillFreezeE> {}
