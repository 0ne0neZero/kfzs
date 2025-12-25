package com.wishare.finance.domains.reconciliation.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 对账规则表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-10-12
 */
@Mapper
public interface ReconcileRuleMapper extends BaseMapper<ReconcileRuleE> {
    @InterceptorIgnore(tenantLine = "on")
    ReconcileRuleE getByScheduleId(@Param("scheduleId") String scheduleId);
}
