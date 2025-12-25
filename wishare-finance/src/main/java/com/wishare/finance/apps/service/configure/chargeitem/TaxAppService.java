package com.wishare.finance.apps.service.configure.chargeitem;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.GetTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.SyncTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.TaxRateInfoF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxCategoryF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxRateF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.*;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxRateCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.GetTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxRateCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxCategoryD;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateDetailD;
import com.wishare.finance.domains.configure.chargeitem.entity.BPMDeptE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateA;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxCategoryE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.BPMDeptRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.service.TaxCategoryDomainService;
import com.wishare.finance.domains.configure.chargeitem.service.TaxRateDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.support.yuanyang.YuanYangTaxRateProperties;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaxAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private TaxCategoryDomainService taxCategoryDomainService;

    @Setter(onMethod_ = {@Autowired})
    private TaxRateDomainService taxRateDomainService;


    @Setter(onMethod_ = {@Autowired})
    private BPMDeptRepository bpmDeptRepository;

    @Setter(onMethod_ = {@Autowired})
    private ChargeItemRepository chargeItemRepository;

    /**
     * 新增税种信息
     *
     * @param form
     * @return
     */
    public Long addTaxCategory(AddTaxCategoryF form) {
        //通过父parentId找到父税种的税种路径，便于子税种追加路径
        TaxCategoryE taxCategoryEParent = null;
        if (form.getParentId() != null && form.getParentId() != 0) {
            taxCategoryEParent = taxCategoryDomainService.getByParentId(form.getParentId());
        }
        AddTaxCategoryCommand addTaxCategoryCommand = form.getAddTaxCategoryCommand(taxCategoryEParent, curIdentityInfo());
        taxCategoryDomainService.addTaxCategory(addTaxCategoryCommand);
        return addTaxCategoryCommand.getId();
    }

    /**
     * 修改税种信息
     *
     * @param form
     * @return
     */
    public Long updateTaxCategory(UpdateTaxCategoryF form) {
        UpdateTaxCategoryCommand updateTaxCategoryCommand = form.update(curIdentityInfo());
        return taxCategoryDomainService.updateTaxCategoryCommand(updateTaxCategoryCommand, curIdentityInfo());
    }


    /**
     * 删除税种信息
     *
     * @param id
     * @return
     */
    @Transactional
    public Boolean deleteTaxCategory(Long id) {
        List<TaxRateE> taxRateEList = taxRateDomainService.getByTaxCategoryId(id);
        taxRateDomainService.deleteBatch(taxRateEList);
        return taxCategoryDomainService.deleteById(id, curIdentityInfo());
    }

    /**
     * 根据id获取税种信息
     *
     * @param id
     * @return
     */
    public TaxCategoryV getTaxCategoryById(Long id) {
        TaxCategoryD taxCategoryDTO = taxCategoryDomainService.getTaxCategoryById(id);
        return Global.mapperFacade.map(taxCategoryDTO, TaxCategoryV.class);
    }

    /**
     * 获取税种信息列表
     *
     * @param form
     * @return
     */
    public List<TaxCategoryV> taxCategoryList(GetTaxCategoryF form, Long chargeItemId) {
        GetTaxCategoryCommand taxCategoryCommand = form.getTaxCategoryCommand(curIdentityInfo());
        List<TaxCategoryD> taxCategoryDTOS = taxCategoryDomainService.taxCategoryList(taxCategoryCommand);
        List<TaxCategoryV> taxCategoryVS = Global.mapperFacade.mapAsList(taxCategoryDTOS, TaxCategoryV.class);
        if (CollectionUtils.isEmpty(taxCategoryVS) || Objects.isNull(chargeItemId)) {
            return taxCategoryVS;
        }
        ChargeItemE chargeItem = chargeItemRepository.getById(chargeItemId);
        if (Objects.isNull(chargeItem) || StringUtils.isBlank(chargeItem.getTaxRateInfo())) {
            return taxCategoryVS;
        }
        List<TaxRateInfoF> taxRateInfos = JSON.parseArray(chargeItem.getTaxRateInfo(), TaxRateInfoF.class);
        if (CollectionUtils.isEmpty(taxRateInfos)) {
            return taxCategoryVS;
        }
        List<Long> taxCategoryIds = taxRateInfos.stream().map(TaxRateInfoF::getTaxCategoryId).distinct().collect(Collectors.toList());
        List<Long> taxRateIds = taxRateInfos.stream().map(TaxRateInfoF::getTaxRateId).distinct().collect(Collectors.toList());
        //一级过滤
        taxCategoryVS = taxCategoryVS.stream()
                .filter(ta -> taxCategoryIds.contains(ta.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(taxCategoryVS)) {
            //二级过滤
            for (TaxCategoryV taxCategoryV : taxCategoryVS) {
                List<TaxRateV> newTaxRateVS = taxCategoryV.getTaxRateS().stream()
                        .filter(ta -> taxRateIds.contains(ta.getId())).collect(Collectors.toList());
                taxCategoryV.setTaxRateS(newTaxRateVS);
            }
        }
        return taxCategoryVS;
    }

    /**
     * 获取税种信息树形图
     *
     * @param form
     * @return
     */
    public List<TaxCategoryTree> taxCategoryTrees(GetTaxCategoryF form) {
        GetTaxCategoryCommand taxCategoryCommand = form.getTaxCategoryCommand(curIdentityInfo());
        List<TaxCategoryD> taxCategoryE = taxCategoryDomainService.taxCategoryList(taxCategoryCommand);
        List<TaxCategoryTree> taxCategoryTrees = taxCategoryE.stream().map(item -> {
            TaxCategoryTree tree = Global.mapperFacade.map(item, TaxCategoryTree.class);
            tree.setPid(item.getParentId());
            return tree;
        }).collect(Collectors.toList());
        return TreeUtil.treeing(taxCategoryTrees);
    }

    /**
     * 新增税率信息
     *
     * @param form
     * @return
     */
    public Long addTaxRate(AddTaxRateF form) {
        AddTaxRateCommand addTaxRateCommand = form.getAddTaxRateCommand(curIdentityInfo());
        return taxRateDomainService.addTaxRate(addTaxRateCommand);
    }

    /**
     * 修改税率信息
     *
     * @param form
     * @return
     */
    public Long updateTaxRate(UpdateTaxRateF form) {
        UpdateTaxRateCommand command = form.getUpdateTaxRateCommand(curIdentityInfo());
        return taxRateDomainService.updateTaxRate(command);
    }

    /**
     * 删除税率信息
     *
     * @param id
     * @return
     */
    public Boolean deleteTaxRate(Long id) {
        return taxRateDomainService.deleteTaxRate(id, curIdentityInfo());
    }


    /**
     * 根据税率id获取税率详情
     *
     * @param id
     * @return
     */
    public TaxRateDetailV rateDetail(Long id) {
        TaxRateDetailD taxRateDetailD = taxRateDomainService.rateDetail(id);
        if (null == taxRateDetailD) {
            throw BizException.throw400(ErrorMessage.TAX_RATE_NO_EXISTS.getErrMsg());
        }
        TaxRateDetailV taxRateDetailV = Global.mapperFacade.map(taxRateDetailD, TaxRateDetailV.class);
        taxRateDetailV.setRate(taxRateDetailV.getRate());
        return taxRateDetailV;
    }

    /**
     * 同步税种税率
     * @param syncTaxRates
     * @return
     */
    @Transactional
    public boolean syncTaxRate(List<SyncTaxRateF> syncTaxRates) {
        return taxCategoryDomainService.syncTaxRate(Global.mapperFacade.mapAsList(syncTaxRates, TaxRateA.class));
    }

    public List<TaxRateV> BPMFilterTaxList() {

        return taxRateDomainService.BPMFilterTaxList();
    }

    public List<BPMDeptV> BPMFilterDeptList() {

        return bpmDeptRepository.BPMFilterDeptList();
    }
}
