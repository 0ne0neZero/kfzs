package com.wishare.contract.domains.mapper.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.merchant.MerchantAccountE;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 客商基础信息Repository
 *
 * @author yancao
 */
@Repository
public class MerchantAccountRepository extends ServiceImpl<MerchantAccountMapper, MerchantAccountE> {

    /**
     * 根据客商id删除客商账户信息
     *
     * @param id id
     */
    public void removeByMerchantId(Long id) {
        LambdaQueryWrapper<MerchantAccountE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantAccountE::getMerchantId, id);
        this.remove(queryWrapper);
    }

    /**
     * 根据客商id查询客商账户详情
     *
     * @param merchantId merchantId
     * @return List
     */
    public List<MerchantAccountE> getByMerchantId(Long merchantId) {
        LambdaQueryWrapper<MerchantAccountE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantAccountE::getMerchantId, merchantId);
        return list(queryWrapper);
    }

    /**
     * 根据客商id移除不在集合中的账户信息
     *
     * @param merchantId merchantId
     * @param idList idList
     */
    public void removeByMerchantIdAndIdNotIn(Long merchantId, List<Long> idList) {
        LambdaQueryWrapper<MerchantAccountE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantAccountE::getMerchantId,merchantId);
        queryWrapper.notIn(!CollectionUtils.isEmpty(idList), MerchantAccountE::getId, idList);
        remove(queryWrapper);
    }
}
