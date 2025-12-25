package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.chargeitem.command.tax.GetTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxCategoryD;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxCategoryE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxCategoryMapper;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/7/22
 * @Description:
 */
@Service
public class TaxCategoryRepository extends ServiceImpl<TaxCategoryMapper, TaxCategoryE> {

    @Autowired
    private TaxCategoryMapper taxCategoryMapper;

    /**
     * 根据id查询税种信息，包含税率
     *
     * @param id
     * @return
     */
    public TaxCategoryD getTaxCategoryById(Long id) {
        TaxCategoryE taxCategoryE = taxCategoryMapper.selectById(id);
        if (taxCategoryE != null) {
            TaxCategoryD taxCategoryD = Global.mapperFacade.map(taxCategoryE, TaxCategoryD.class);
            List<TaxRateE> taxRateEList = taxCategoryMapper.getTaxRate(taxCategoryD.getId());
            if (CollectionUtils.isNotEmpty(taxRateEList)) {
                List<TaxRateD> taxRateDList = Lists.newArrayList();
                for (TaxRateE taxRateE : taxRateEList) {
                    TaxRateD taxRateD = new TaxRateD();
                    taxRateD.setTaxRateId(taxRateE.getId());
                    taxRateD.setRate(taxRateE.getRate());
                    taxRateD.setTaxTypeStr("税率");
                    taxRateDList.add(taxRateD);
                }
                taxCategoryD.setTaxRateS(taxRateDList);
            }else {
                taxCategoryD.setTaxRateS(Lists.newArrayList());
            }
            return taxCategoryD;
        }
        return null;
    }

    /**
     * 根据条件获取税种信息，包含税率
     *
     * @param command
     * @return
     */
    public List<TaxCategoryD> taxCategoryList(GetTaxCategoryCommand command) {
        //根据条件找税种
        List<TaxCategoryE> taxCategoryEList = taxCategoryMapper.getTaxCategory(command);
        if (CollectionUtils.isNotEmpty(taxCategoryEList)) {
            List<TaxCategoryD> taxCategoryDList = Global.mapperFacade.mapAsList(taxCategoryEList, TaxCategoryD.class);
            for (TaxCategoryD taxCategoryD : taxCategoryDList) {
                List<TaxRateE> taxRateEList = taxCategoryMapper.getTaxRate(taxCategoryD.getId());
                if (CollectionUtils.isNotEmpty(taxRateEList)) {
                    List<TaxRateD> taxRateDList = Lists.newArrayList();
                    for (TaxRateE taxRateE : taxRateEList) {
                        TaxRateD taxRateD = new TaxRateD();
                        taxRateD.setTaxRateId(taxRateE.getId());
                        taxRateD.setRate(taxRateE.getRate());
                        taxRateD.setTaxTypeStr("税率");
                        taxRateDList.add(taxRateD);
                    }
                    taxCategoryD.setTaxRateS(taxRateDList);
                }else {
                    taxCategoryD.setTaxRateS(Lists.newArrayList());
                }
            }
            return taxCategoryDList;
        }
        return Lists.newArrayList();
    }

    public TaxCategoryE getByParentId(Long parentId) {
        LambdaQueryWrapper<TaxCategoryE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaxCategoryE::getId, parentId);
        TaxCategoryE taxCategoryE = taxCategoryMapper.selectOne(wrapper);
        if (taxCategoryE == null) {
            throw BizException.throw400(ErrorMessage.TAX_CATEGORY_PARENT_NOT_EXIT.getErrMsg());
        }
        return taxCategoryE;
    }

    /**
     * 批量新增或更新
     * @param taxCategorys
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<TaxCategoryE> taxCategorys) {
        return taxCategoryMapper.saveOrUpdateBatch(taxCategorys) >0;
    }

    public TaxCategoryE getByCode(String code) {
        return getOne(new LambdaUpdateWrapper<TaxCategoryE>().eq(TaxCategoryE::getCode, code), false);
    }
}
