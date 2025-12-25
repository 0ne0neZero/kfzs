package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/10 16:13
 * @version: 1.0.0
 */
@Mapper
public interface OldVoucherRuleMapper extends BaseMapper<VoucherRuleE> {

    @InterceptorIgnore(tenantLine = "on")
    Page<VoucherRuleE> queryPage(Page<?> page, @Param("ew") QueryWrapper<?> queryModel);

    void updateSortNumBySort(@Param("eventType") Integer eventType,
        @Param("beforeSortNum") Integer beforeSortNum, @Param("sortNum") Integer sortNum);

    void updateSortNumBySortBigger(@Param("eventType") Integer eventType,
        @Param("beforeSortNum") Integer beforeSortNum, @Param("sortNum") Integer sortNum);

    @Select("select max(sort_num) from voucher_rule where event_type = #{eventType}")
    Integer getMaxSortNumByChargeItemIdAndEventType(@Param("eventType") Integer eventType);

    @Select("select * from voucher_rule where charge_item_id = #{chargeItemId} and event_type = #{eventType}"
        + " and execute_type = #{executeType} and deleted = 0 and disabled = 0 order by sort_num")
    List<VoucherRuleE> getByChargeItemIdAndEventType(@Param("chargeItemId") Long chargeItemId, @Param("eventType") int eventType, @Param("executeType") int executeType);

    List<VoucherRuleE> selectListByWrapper(@Param("ew") LambdaQueryWrapper<VoucherRuleE> wrapper);
}
