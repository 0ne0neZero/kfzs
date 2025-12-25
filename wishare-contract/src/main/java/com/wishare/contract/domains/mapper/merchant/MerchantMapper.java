package com.wishare.contract.domains.mapper.merchant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.merchant.MerchantE;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * <p>
 * 客商基础信息表 Mapper 接口
 * </p>
 *
 * @author yancao
 * @since 2022-08-17
 */
public interface MerchantMapper extends BaseMapper<MerchantE> {

    /**
     * 获取当天该租户下的客商数量
     *
     * @param tenantId tenantId
     * @param startTime startTime
     * @param endTime endTime
     * @return long
     */
    long queryCountByToday(@Param("tenantId") String tenantId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
