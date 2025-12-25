package com.wishare.finance.domains.configure.chargeitem.service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.GetTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxCategoryD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxCategoryE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateA;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxCategoryRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxCategoryDomainService {

    private final TaxRateRepository taxRateRepository;
    private final TaxCategoryRepository taxCategoryRepository;

    /**
     * 新增税种信息
     *
     * @param command
     * @return
     */
    public TaxCategoryE addTaxCategory(AddTaxCategoryCommand command) {
        //校检税种编码是否重复
        checkTaxCategoryCode(command.getCode(), command.getTenantId());
        //校检税种名称是否重复
        checkTaxCategoryName(command.getName(), command.getTenantId());
        TaxCategoryE taxCategoryE = Global.mapperFacade.map(command, TaxCategoryE.class);
        taxCategoryRepository.save(taxCategoryE);
        return taxCategoryE;
    }

    /**
     * 修改税种信息
     *
     * @param command
     * @param identityInfo
     * @return
     */
    public Long updateTaxCategoryCommand(UpdateTaxCategoryCommand command, IdentityInfo identityInfo) {
        TaxCategoryE taxCategoryBefore = taxCategoryRepository.getById(command.getId());
        if (taxCategoryBefore == null) {
            throw BizException.throw400(ErrorMessage.TAX_CATEGORY_NO_EXISTS.getErrMsg());
        }
        //校检税种编码是否重复
        if (StringUtils.isNotBlank(command.getCode()) && !StringUtils.equals(taxCategoryBefore.getCode(), command.getCode())) {
            checkTaxCategoryCode(command.getCode(), identityInfo.getTenantId());
        }
        //校检税种名称是否重复
        if (StringUtils.isNotBlank(command.getName()) && !StringUtils.equals(taxCategoryBefore.getName(), command.getName())) {
            checkTaxCategoryName(command.getName(), identityInfo.getTenantId());
        }
        TaxCategoryE taxCategoryE = Global.mapperFacade.map(command, TaxCategoryE.class);
        taxCategoryRepository.updateById(taxCategoryE);
        return taxCategoryE.getId();
    }

    /**
     * 删除税种信息
     *
     * @param id
     * @param identityInfo
     * @return
     */
    public Boolean deleteById(Long id, IdentityInfo identityInfo) {
        TaxCategoryE taxCategoryE = taxCategoryRepository.getById(id);
        if (null == taxCategoryE) {
            throw BizException.throw400(ErrorMessage.TAX_CATEGORY_NO_EXISTS.getErrMsg());
        }
        taxCategoryE.delete();
        taxCategoryE.updateOperator(identityInfo);
        return taxCategoryRepository.removeById(taxCategoryE);
    }

    /**
     * 根据id获取税种信息
     *
     * @param id
     * @return
     */
    public TaxCategoryD getTaxCategoryById(Long id) {
        TaxCategoryD dto = taxCategoryRepository.getTaxCategoryById(id);
        return dto;
    }

    /**
     * 根据条件获取税种列表
     *
     * @param command
     * @return
     */
    public List<TaxCategoryD> taxCategoryList(GetTaxCategoryCommand command) {
        List<TaxCategoryD> taxCategoryDTOS = taxCategoryRepository.taxCategoryList(command);
        return taxCategoryDTOS;
    }


    /**
     * 根据父id获取税种信息
     *
     * @param parentId
     * @return
     */
    public TaxCategoryE getByParentId(Long parentId) {
        TaxCategoryE taxCategoryE = taxCategoryRepository.getByParentId(parentId);
        return taxCategoryE;
    }

    /**
     * 根据税种名称和租户id校检税种是否存在
     *
     * @param name
     * @param tenantId
     */
    private void checkTaxCategoryName(String name, String tenantId) {
        LambdaQueryWrapper<TaxCategoryE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaxCategoryE::getName, name);
        wrapper.eq(TaxCategoryE::getTenantId, tenantId);
        wrapper.eq(TaxCategoryE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        TaxCategoryE taxCategoryE = taxCategoryRepository.getOne(wrapper);
        if (null != taxCategoryE) {
            throw BizException.throw400(ErrorMessage.TAX_CATEGORY_NAME_EXISTS.getErrMsg());
        }
    }

    /**
     * 根据税种编码和租户id校检税种是否存在
     *
     * @param code
     * @param tenantId
     */
    private void checkTaxCategoryCode(String code, String tenantId) {
        LambdaQueryWrapper<TaxCategoryE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaxCategoryE::getCode, code);
        wrapper.eq(TaxCategoryE::getTenantId, tenantId);
        wrapper.eq(TaxCategoryE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        TaxCategoryE taxCategoryE = taxCategoryRepository.getOne(wrapper);
        if (null != taxCategoryE) {
            throw BizException.throw400(ErrorMessage.TAX_CATEGORY_CODE_EXISTS.getErrMsg());
        }
    }

    /**
     * 同步税种税率
     * @param taxRateAS
     * @return
     */
    public boolean syncTaxRate(List<TaxRateA> taxRateAS) {
        List<TaxRateE> taxRates = null;
        for (TaxRateA taxRateA : taxRateAS) {
            TaxCategoryE taxCategory = taxCategoryRepository.getByCode(taxRateA.getCode());
            if (Objects.nonNull(taxCategory)){
                taxRateA.setId(taxCategory.getId());
            }
            taxRateA.updateOrInsert();
            taxRateA.setPath(JSON.toJSONString(List.of(taxRateA.getId())));
            taxCategoryRepository.saveOrUpdateBatchByCode(List.of(taxRateA));
            if (CollectionUtils.isNotEmpty(taxRateA.getTaxRates())){
                taxRates = taxRateA.getTaxRates();
                taxRates.forEach(taxRate -> {
                    taxRate.updateOrInsert();
                    taxRate.setTaxCategoryId(taxRateA.getId());
                });
                taxRates.addAll(taxRates);
                taxRateRepository.saveOrUpdateBatchByCode(taxRates);
            }
        }
        return true;
    }


}
