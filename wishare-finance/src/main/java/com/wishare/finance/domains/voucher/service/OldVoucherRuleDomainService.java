package com.wishare.finance.domains.voucher.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.model.VoucherRuleCondition;
import com.wishare.finance.domains.voucher.repository.OldVoucherRuleRepository;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/10 16:09
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OldVoucherRuleDomainService {

    private final OldVoucherRuleRepository oldVoucherRuleRepository;

    @Value("${voucher.rule.timeConditions:17,18}")
    private String timeConditions;

    @Value("${voucher.rule.modelConditions:14,1,2,5}")
    private String modelConditions;

    /**
     * 推凭规则列表（分页）
     * @param form
     * @param eventType
     * @param chargeItemId
     * @return
     */
    public Page<VoucherRuleE> queryPage(PageF<SearchF<?>> form, Integer eventType, Long chargeItemId) {
        return oldVoucherRuleRepository.queryPage(form, eventType, chargeItemId);
    }

    /**
     * 推凭规则列表（分页） 新：2023-01-05版本
     * @param form
     * @param eventType
     * @return
     */
    public Page<VoucherRuleE> pageByEventType(PageF<SearchF<?>> form, Integer eventType) {
        return oldVoucherRuleRepository.pageByEventType(form, eventType);
    }

    /**
     * 新增推凭规则
     * @param voucherRuleE
     * @return
     */
    public Long insert(VoucherRuleE voucherRuleE) {
        Integer maxSortNum = oldVoucherRuleRepository.getMaxSortNumByChargeItemIdAndEventType(voucherRuleE.getEventType());
        if (maxSortNum == null) {
            maxSortNum = 0;
        }
        maxSortNum++;
        voucherRuleE.setSortNum(maxSortNum);
        dealCondition(voucherRuleE);
        oldVoucherRuleRepository.save(voucherRuleE);
        return voucherRuleE.getId();
    }

    /**
     * 处理过滤条件
     * @param voucherRuleE 规则
     */
    private void dealCondition(VoucherRuleE voucherRuleE) {
        List<Long> list = new ArrayList<>();
        if (StringUtils.isNotBlank(voucherRuleE.getFilterConditions())) {
            List<VoucherRuleCondition> conditionList = JSONArray.parseArray(
                voucherRuleE.getFilterConditions(), VoucherRuleCondition.class);
            if (!CollectionUtils.isEmpty(conditionList)) {
                conditionList.stream()
                    .filter(rule -> "14".equals(rule.getConditions())).findFirst()
                    .ifPresent(voucherRuleCondition -> {
                        JSONArray value = (JSONArray) voucherRuleCondition.getValue();
                        for (int i = 0; i < value.size(); i++) {
                            list.add(value.getJSONObject(i).getLong("id"));
                        }
                    });
                List<String> timeConditionArray = Arrays.asList(timeConditions.split(","));

                conditionList.forEach(condition -> {
                    if (timeConditionArray.contains(condition.getConditions())) {
                        condition.setValueName((String) condition.getValue());
                    }
                });

                String jsonString = JSON.toJSONString(conditionList);
                voucherRuleE.setFilterConditions(jsonString);

                List<VoucherRuleCondition> ruleConditions = JSONArray.parseArray(jsonString, VoucherRuleCondition.class);
                List<String> modelConditionsArray = Arrays.asList(modelConditions.split(","));
                ruleConditions.forEach(condition -> {
                    if (modelConditionsArray.contains(condition.getConditions())) {
                        JSONArray value = (JSONArray) condition.getValue();
                        JSONArray valueList = new JSONArray();
                        for (int i = 0; i < value.size(); i++) {
                            valueList.add(value.getJSONObject(i).getLong("id"));
                        }
                        condition.setValue(valueList);
                    }
                });
                voucherRuleE.setConditions(JSON.toJSONString(ruleConditions));
            }
        } else {
            voucherRuleE.setConditions(voucherRuleE.getFilterConditions());
        }
        //voucherRuleE.setChargeItemId(JSON.toJSONString(list));
    }

    /**
     * 更新推凭规则
     * @param voucherRuleE
     * @return
     */
    public Long update(VoucherRuleE voucherRuleE) {
        dealCondition(voucherRuleE);
        oldVoucherRuleRepository.updateById(voucherRuleE);
        return voucherRuleE.getId();
    }

    /**
     * 删除推凭规则
     * @param id
     * @return
     */
    public Boolean deleteById(Long id) {
        VoucherRuleE voucherRuleE = oldVoucherRuleRepository.getById(id);
        if (Optional.ofNullable(voucherRuleE).isEmpty()) {
            throw BizException.throw400("该推凭规则不存在");
        }
        oldVoucherRuleRepository.removeById(id);
        return Boolean.TRUE;
    }

    /**
     * 禁用/启用推凭规则
     * @param id
     * @param disabled
     * @return
     */
    public Boolean enableById(Long id, Integer disabled) {
        VoucherRuleE voucherRuleE = oldVoucherRuleRepository.getById(id);
        if (Optional.ofNullable(voucherRuleE).isEmpty()) {
            throw BizException.throw400("该推凭规则不存在");
        }
        voucherRuleE.setDisabled(disabled);
        return oldVoucherRuleRepository.updateById(voucherRuleE);
    }

    /**
     * 查询单个推凭规则
     * @param id
     * @return
     */
    public Optional<VoucherRuleE> queryById(Long id) {
        return Optional.ofNullable(oldVoucherRuleRepository.getById(id));
    }

    /**
     * 推凭规则排序
     * @param id
     * @param sortNum
     * @return
     */
    @Transactional
    public Boolean sortById(Long id, Integer sortNum) {

        VoucherRuleE voucherRuleE = oldVoucherRuleRepository.getById(id);
        if (Optional.ofNullable(voucherRuleE).isEmpty()) {
            throw BizException.throw400("该凭证不存在");
        }
        Integer beforeSortNum = voucherRuleE.getSortNum();
        if (beforeSortNum < sortNum) {
            oldVoucherRuleRepository.updateSortNumBySort(voucherRuleE.getEventType(), beforeSortNum, sortNum);
        } else if (beforeSortNum > sortNum) {
            oldVoucherRuleRepository.updateSortNumBySortBigger(voucherRuleE.getEventType(), beforeSortNum, sortNum);
        }
        voucherRuleE.setSortNum(sortNum);
        return oldVoucherRuleRepository.updateById(voucherRuleE);
    }

    /**
     * 获取分页推凭规则 用于定时推凭
     * @param form
     * @return
     */
    public PageV<VoucherRuleE> listForInference(PageF<SearchF<?>> form) {
        Page<VoucherRuleE> page = oldVoucherRuleRepository.listForInference(form);
        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        return PageV.of(form, page.getTotal(), page.getRecords());
    }

    /**
     * 根据费项id和触发事件类型获取推凭规则
     * @param chargeItemId
     * @param type
     * @param executeType
     * @return
     */
    public List<VoucherRuleE> getByChargeItemIdAndEventType(Long chargeItemId, int type, int executeType) {
        return oldVoucherRuleRepository.getByChargeItemIdAndEventType(chargeItemId, type, executeType);
    }

    /**
     * 获取单个规则
     * @param id
     * @return
     */
    public VoucherRuleE getById(Long id) {
        return oldVoucherRuleRepository.getById(id);
    }


    /**
     * 根据费项ids和触发事件类型批量获取推凭规则
     * @param chargeItemIds
     * @param eventType
     * @param executeType
     * @return
     */
    public List<VoucherRuleE> listByChargeItemIdAndEventTypeAndExecuteType(Set<Long> chargeItemIds, EventTypeEnum eventType, int executeType) {
        return oldVoucherRuleRepository.listByChargeItemIdAndEventTypeAndExecuteType(chargeItemIds, eventType, executeType);
    }


    public List<VoucherRuleE> listAll() {

        return oldVoucherRuleRepository.listAll();
    }
}
