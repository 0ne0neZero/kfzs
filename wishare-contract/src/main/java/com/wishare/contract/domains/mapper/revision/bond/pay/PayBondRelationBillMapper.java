package com.wishare.contract.domains.mapper.revision.bond.pay;

import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 缴纳保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-28
 */
@Mapper
public interface PayBondRelationBillMapper extends BaseMapper<PayBondRelationBillE> {

}
