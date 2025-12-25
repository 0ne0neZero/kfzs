package com.wishare.contract.domains.mapper.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.merchant.MerchantAccountE;
import com.wishare.contract.domains.entity.merchant.MerchantContactsE;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 客商联系人信息Repository
 *
 * @author yancao
 */
@Repository
public class MerchantContactsRepository extends ServiceImpl<MerchantContactsMapper, MerchantContactsE> {

    /**
     * 根据客商id删除客商账户信息
     *
     * @param id id
     */
    public void removeByMerchantId(Long id) {
        LambdaQueryWrapper<MerchantContactsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantContactsE::getMerchantId, id);
        this.remove(queryWrapper);
    }

    /**
     * 根据客商id查询客商联系人详情
     *
     * @param merchantId merchantId
     * @return List
     */
    public List<MerchantContactsE> queryByMerchantId(Long merchantId) {
        LambdaQueryWrapper<MerchantContactsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantContactsE::getMerchantId, merchantId);
        return list(queryWrapper);
    }

    /**
     * 根据客商id移除不在集合中的联系人信息
     *
     * @param merchantId merchantId
     * @param idList idList
     */
    public void removeByMerchantIdAndIdNotIn(Long merchantId, List<Long> idList) {
        LambdaQueryWrapper<MerchantContactsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantContactsE::getMerchantId,merchantId);
        queryWrapper.notIn(!CollectionUtils.isEmpty(idList), MerchantContactsE::getId, idList);
        remove(queryWrapper);
    }
}
