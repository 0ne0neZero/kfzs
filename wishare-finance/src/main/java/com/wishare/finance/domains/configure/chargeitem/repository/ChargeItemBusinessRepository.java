package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemBusinessE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.ChargeItemBusinessMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeItemBusinessRepository extends ServiceImpl<ChargeItemBusinessMapper, ChargeItemBusinessE> {

    public List<ChargeItemBusinessE> getChargeItemBusinessE(Long chargeItemId) {
        LambdaQueryWrapper<ChargeItemBusinessE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemBusinessE::getChargeItemId, chargeItemId);
        queryWrapper.eq(ChargeItemBusinessE::getDeleted, 0);
        return list(queryWrapper);
    }
    public Boolean removeByChargeItemId(Long chargeItemId) {
        LambdaQueryWrapper<ChargeItemBusinessE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemBusinessE::getChargeItemId, chargeItemId);
        return remove(queryWrapper);
    }


}
