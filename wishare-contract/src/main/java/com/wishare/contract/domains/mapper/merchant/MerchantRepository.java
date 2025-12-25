package com.wishare.contract.domains.mapper.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.merchant.MerchantE;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 客商基础信息Repository
 *
 * @author yancao
 */
@Repository
public class MerchantRepository extends ServiceImpl<MerchantMapper, MerchantE> {

    /**
     * 根据客商名称获取客商信息
     *
     * @param filterId filterId
     * @param name     name
     * @param tenantId tenantId
     * @return MerchantE
     */
    public MerchantE queryByName(Long filterId, String name, String tenantId) {
        LambdaQueryWrapper<MerchantE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantE::getName, name);
        queryWrapper.eq(StringUtils.hasText(tenantId), MerchantE::getTenantId, tenantId);
        queryWrapper.ne(filterId != null, MerchantE::getId, filterId);
        return this.getOne(queryWrapper);
    }

    /**
     * 获取当天该租户下的客商数量
     *
     * @param tenantId tenantId
     * @return long
     */
    public long queryCountByToday(String tenantId) {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        return this.baseMapper.queryCountByToday(tenantId, startTime,endTime);
    }

    /**
     * 获取客商列表
     *
     * @param tenantId tenantId
     * @return List
     */
    public List<MerchantE> queryList(String tenantId) {
        LambdaQueryWrapper<MerchantE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tenantId) , MerchantE::getTenantId, tenantId);
        queryWrapper.select(MerchantE::getId, MerchantE::getCode, MerchantE::getName);
        return list(queryWrapper);
    }
}
