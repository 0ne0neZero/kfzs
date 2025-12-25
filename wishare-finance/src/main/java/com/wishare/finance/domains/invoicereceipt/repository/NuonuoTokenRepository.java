package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.nuonuo.NuonuoTokenE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.NuonuoTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Service
public class NuonuoTokenRepository extends ServiceImpl<NuonuoTokenMapper, NuonuoTokenE> {

    @Autowired
    private NuonuoTokenMapper nuonuoTokenMapper;

    /**
     * 获取诺诺token
     *
     * @param tenantId
     * @param taxnum
     * @return
     */
    public NuonuoTokenE getToken(String tenantId, String taxnum) {
        LambdaQueryWrapper<NuonuoTokenE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NuonuoTokenE::getTenantId, tenantId);
        wrapper.eq(NuonuoTokenE::getSalerTaxNum, taxnum);
        return nuonuoTokenMapper.selectOne(wrapper);
    }
}
