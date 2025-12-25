package com.wishare.finance.domains.voucher.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.voucher.consts.enums.FilterTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.LogicTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateTypeEnum;
import com.wishare.finance.domains.voucher.entity.*;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.repository.VoucherTemplateRepository;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 凭证规则应用id
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherTemplateDomainService {

    private final VoucherTemplateRepository voucherTemplateRepository;
    private final VoucherRuleRepository voucherRuleRepository;

    /**
     * 新增模板
     *
     * @param voucherTemplate
     * @return
     */
    public Long addTemplate(VoucherTemplate voucherTemplate) {
        voucherTemplate.checkEntries();
        checkFilterParam(voucherTemplate.getEntries());
        voucherTemplate.init();
        if (Objects.nonNull(voucherTemplate.getType()) && VoucherTemplateTypeEnum.BPM.equalsByCode(voucherTemplate.getType())) {
            checkBPMTemplateUpdate(voucherTemplate);
        } else {
            getTemplateName(voucherTemplate.getName(), null);
        }
        voucherTemplateRepository.save(voucherTemplate);
        return voucherTemplate.getId();
    }

    /**
     * 获取BPM凭证模板
     * @param templateName
     * @return
     */
    public List<VoucherTemplate> getBPMTemplateListByName(String templateName) {
        return voucherTemplateRepository.list(new LambdaQueryWrapper<VoucherTemplate>()
                .eq(VoucherTemplate::getName, templateName)
                .eq(VoucherTemplate::getType, VoucherTemplateTypeEnum.BPM.getCode())
                .eq(VoucherTemplate::getDeleted, 0)
                .eq(VoucherTemplate::getDisabled, 0));
    }

    /**
     * 获取BPM凭证模板
     * @param typeCode
     * @return
     */
    public List<VoucherTemplate> getBPMTemplateListByTypeCode(String typeCode) {
        return voucherTemplateRepository.list(new LambdaQueryWrapper<VoucherTemplate>()
                .eq(VoucherTemplate::getBizCode, typeCode)
                .eq(VoucherTemplate::getType, VoucherTemplateTypeEnum.BPM.getCode())
                .eq(VoucherTemplate::getDeleted, 0)
                .eq(VoucherTemplate::getDisabled, 0));
    }

    /**
     * 更新凭证模板
     *
     * @param voucherTemplate
     * @return
     */
    public boolean updateTemplate(VoucherTemplate voucherTemplate) {
        checkFilterParam(voucherTemplate.getEntries());
        VoucherTemplate byIdWithCheck = getByIdWithCheck(voucherTemplate.getId());
        if (Objects.nonNull(voucherTemplate.getType()) && VoucherTemplateTypeEnum.BPM.equalsByCode(voucherTemplate.getType())) {
            checkBPMTemplateUpdate(voucherTemplate);
        } else {
            getTemplateName(voucherTemplate.getName(), byIdWithCheck);
        }
        voucherTemplate.checkEntries();
        return voucherTemplateRepository.updateById(voucherTemplate);
    }

    public static void main(String[] args) {

        List<VoucherTemplateFilterItem> filterItemList = JSONObject.parseObject("[{\"filterType\":\"org\",\"logicType\":\"equal\",\"deptCode\":[\"demoData\",\"ddd\"],\"deptName\":[\"demoData\"],\"taxRateCode\":[\"demoData\"],\"taxRate\":[1]}]",
                new TypeReference<List<VoucherTemplateFilterItem>>() {
                });
        checkFilterItem(filterItemList);

    }

    private static void checkFilterParam( List<VoucherTemplateEntryOBV> entries){
        if (CollUtil.isEmpty(entries)){
            return;
        }
        entries.forEach(v->{
            FilterConditions conditions = v.getFilterConditions();
            if (ObjectUtil.isNull(conditions)){
                return;
            }
            List<VoucherTemplateFilterItem> filterItemList = conditions.getFilterItems();
            if (CollUtil.isEmpty(filterItemList)){
                return;
            }
            checkFilterItem(filterItemList);
        });

    }

    private static void checkFilterItem(List<VoucherTemplateFilterItem> filterItemList) {
        if (filterItemList.size() >2){
            throw BizException.throw400("BPM凭证模板过滤条件最多两条");
        }
        if (filterItemList.size() ==2){
            Set<FilterTypeEnum> set = filterItemList.stream().map(VoucherTemplateFilterItem::getFilterType).collect(Collectors.toSet());
            if (set.size() == 1){
                throw BizException.throw400("BPM凭证模板过滤条件类型不能相同");
            }
        }
        for (VoucherTemplateFilterItem item : filterItemList) {
            if (FilterTypeEnum.org.equals(item.getFilterType())){
                ErrorAssertUtils.notEmptyThrow400(item.getDeptCode(),"deptCode不能为空");
                ErrorAssertUtils.notEmptyThrow400(item.getDeptName(),"deptName不能为空");

                if (LogicTypeEnum.equal.equals(item.getLogicType())){
                    ErrorAssertUtils.isTrueThrow300(item.getDeptCode().size() ==1,"选择等于，只能选择一个值");
                }
            }
            if (FilterTypeEnum.taxRate.equals(item.getFilterType())){
                ErrorAssertUtils.notEmptyThrow400(item.getTaxRateCode(),"taxRateCode不能为空");
                ErrorAssertUtils.notEmptyThrow400(item.getTaxRate(),"taxRate不能为空");
                if (LogicTypeEnum.equal.equals(item.getLogicType())){
                    ErrorAssertUtils.isTrueThrow300(item.getTaxRate().size() ==1,"选择等于，只能选择一个值");
                }
            }

        }
    }


    /**
     * 检查BPM凭证模板更新是否正确
     * @param voucherTemplate
     */
    public void checkBPMTemplateUpdate(VoucherTemplate voucherTemplate) {
        List<VoucherTemplate> templates = voucherTemplateRepository.list(new LambdaQueryWrapper<VoucherTemplate>()
                .eq(VoucherTemplate::getName, voucherTemplate.getName())
                .eq(VoucherTemplate::getDeleted, 0));
        Integer templateNum = voucherTemplate.getTemplateNum();
        for (VoucherTemplate template : templates) {
            if (Objects.isNull(voucherTemplate.getId()) || !template.getId().equals(voucherTemplate.getId())) {
                if (Objects.nonNull(template.getTemplateNum()) && templateNum.equals(template.getTemplateNum())) {
                    throw BizException.throw400("已存在模板序号为" + templateNum + "的BPM凭证模板");
                }
            }
        }
    }

    public void getTemplateName(String templateName, VoucherTemplate byIdWithCheck) {
        long count = voucherTemplateRepository.count(new LambdaQueryWrapper<VoucherTemplate>()
                .eq(VoucherTemplate::getName, templateName)
                .eq(VoucherTemplate::getDeleted, 0));

        if (Objects.nonNull(byIdWithCheck)) {
            if(!templateName.equals(byIdWithCheck.getName())){
                ErrorAssertUtil.isTrueThrow403(count == 0, ErrorMessage.VOUCHER_TEMPLATE_NAME_EXIST, count);
            }
        } else {
            ErrorAssertUtil.isTrueThrow403(count <= 0, ErrorMessage.VOUCHER_TEMPLATE_NAME_EXIST, count);
        }
    }


    /**
     * 启用禁用模板
     *
     * @param voucherTemplateId 模板id
     * @param disabled          启用禁用信息
     * @return
     */
    public boolean enableTemplate(Long voucherTemplateId, Integer disabled) {
        ErrorAssertUtil.notNullThrow403(DataDisabledEnum.valueOfByCode(disabled), ErrorMessage.DISABLE_PARAM_ERROR);
        this.getRule(voucherTemplateId);
        VoucherTemplate repVoucherTemplate = getByIdWithCheck(voucherTemplateId);
        return voucherTemplateRepository.updateByDisabled(repVoucherTemplate.getId(), disabled);
    }

    public void getRule(Long voucherTemplateId) {
        List<VoucherRule> voucherRules = voucherRuleRepository.list(new LambdaQueryWrapper<VoucherRule>()
                .eq(VoucherRule::getVoucherTemplateId, voucherTemplateId)
                .eq(VoucherRule::getDeleted, 0));
        if (CollectionUtils.isNotEmpty(voucherRules)) {
            String collect = voucherRules.stream().map(r -> r.getRuleName()).collect(Collectors.joining(","));
            throw BizException.throw400("该模板被" + collect + "引用,不能禁用或删除");
        }
    }

    /**
     * 删除凭证模板
     *
     * @param voucherTemplateId 模板id
     * @return
     */
    public boolean deleteTemplate(Long voucherTemplateId) {
        VoucherTemplate repVoucherTemplate = getByIdWithCheck(voucherTemplateId);
        this.getRule(voucherTemplateId);
        return voucherTemplateRepository.removeById(repVoucherTemplate.getId());
    }

    public VoucherTemplate getByIdWithCheck(Long voucherTemplateId) {
        VoucherTemplate repVoucherTemplate = voucherTemplateRepository.getById(voucherTemplateId);
        ErrorAssertUtil.notNullThrow403(repVoucherTemplate, ErrorMessage.VOUCHER_TEMPLATE_NOT_EXIST);
        return repVoucherTemplate;
    }
}
