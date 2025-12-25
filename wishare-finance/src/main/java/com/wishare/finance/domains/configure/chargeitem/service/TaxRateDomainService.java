package com.wishare.finance.domains.configure.chargeitem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apps.model.configure.chargeitem.vo.TaxRateV;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxRateCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxRateCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateDetailD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateA;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.support.yuanyang.YuanYangTaxRateProperties;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxRateDomainService {

    private final TaxRateRepository taxRateRepository;
    private final YuanYangTaxRateProperties yuanYangTaxRateProperties;

    /**
     * 新增税率信息
     *
     * @param command
     * @return
     */
    public Long addTaxRate(AddTaxRateCommand command) {
        TaxRateE taxRateE = Global.mapperFacade.map(command, TaxRateE.class);
        taxRateRepository.save(taxRateE);
        return taxRateE.getId();
    }

    /**
     * 更新税率
     *
     * @param command
     * @return
     */
    public Long updateTaxRate(UpdateTaxRateCommand command) {
        TaxRateE taxRateEBefore = taxRateRepository.getById(command.getId());
        if (null == taxRateEBefore) {
            throw BizException.throw400(ErrorMessage.TAX_RATE_NO_EXISTS.getErrMsg());
        }
        TaxRateE taxRateE = Global.mapperFacade.map(command, TaxRateE.class);
        taxRateRepository.updateById(taxRateE);
        return taxRateE.getId();
    }

    /**
     * 逻辑删除税率信息
     *
     * @param id
     * @param identityInfo
     */
    public Boolean deleteTaxRate(Long id, IdentityInfo identityInfo) {
        TaxRateE taxRateE = taxRateRepository.getById(id);
        if (null == taxRateE) {
            throw BizException.throw400(ErrorMessage.TAX_RATE_NO_EXISTS.getErrMsg());
        }
        taxRateE.delete();
        taxRateE.updateOperator(identityInfo);
        taxRateRepository.removeById(taxRateE);
        return true;
    }

    /**
     * 逻辑删除税率信息批量删除税率
     *
     * @param taxRateEList
     */
    public void deleteBatch(List<TaxRateE> taxRateEList) {
        taxRateRepository.removeBatchByIds(taxRateEList);
    }

    /**
     * 根据税种id获取税率ids
     *
     * @param id
     * @return
     */
    public List<TaxRateE> getByTaxCategoryId(Long id) {
        LambdaQueryWrapper<TaxRateE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaxRateE::getTaxCategoryId, id);
        return taxRateRepository.list(wrapper);
    }

    /**
     * 根据税率id获取税率详情
     *
     * @param id
     * @return
     */
    public TaxRateDetailD rateDetail(Long id) {
        TaxRateDetailD taxRateDetailD = taxRateRepository.rateDetail(id);
        return taxRateDetailD;
    }

    /**
     * 根据税率获取增值税税率
     * @param rate 税率值
     * @return 税率
     */
    public TaxRateE getByRate(BigDecimal rate) {
       return taxRateRepository.getByRate(rate);
    }

    /**
     * 根据税种税率获取增值税税率
     * @param categoryCode 税种编码
     * @param rate 税率值
     * @return 税率
     */
    public TaxRateE getByCategoryRate(String categoryCode, BigDecimal rate) {
        return taxRateRepository.getByCategoryRate(categoryCode, rate);
    }

    public List<TaxRateV> BPMFilterTaxList() {

        return taxRateRepository.BPMFilterTaxList(yuanYangTaxRateProperties.getTaxCategoryCode());

    }
}
