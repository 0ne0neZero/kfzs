package com.wishare.finance.apps.service.voucher;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.voucher.fo.VoucherRuleF;
import com.wishare.finance.apps.model.voucher.vo.VoucherRuleEventV;
import com.wishare.finance.apps.model.voucher.vo.OldVoucherRuleV;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.model.VoucherRuleCondition;
import com.wishare.finance.domains.voucher.service.OldVoucherRuleDomainService;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.fo.config.DictionaryItemF;
import com.wishare.finance.infrastructure.remote.vo.config.DictionaryItemRV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/10 16:02
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OldVoucherRuleAppService implements ApiBase {

    private final OldVoucherRuleDomainService oldVoucherRuleDomainService;

    private final VoucherInferenceAppServiceFactory voucherInferenceAppServiceFactory;

    private final ConfigClient configClient;

    /**
     * 获取规则 （分页）
     * @param form
     * @param eventType
     * @param chargeItemId
     * @return
     */
    public Page<VoucherRuleE> page(PageF<SearchF<?>> form, Integer eventType, Long chargeItemId) {
        return oldVoucherRuleDomainService.queryPage(form, eventType, chargeItemId);
    }

    /**
     * 获取规则 （分页）
     * @param form
     * @param eventType
     * @return
     */
    public Page<VoucherRuleE> pageByEventType(PageF<SearchF<?>> form, Integer eventType) {
        return oldVoucherRuleDomainService.pageByEventType(form, eventType);
    }

    /**
     * 新增
     * @param voucherRuleF
     * @return
     */
    public Long add(VoucherRuleF voucherRuleF) {
        validCondition(voucherRuleF);
        return oldVoucherRuleDomainService.insert(Global.mapperFacade.map(voucherRuleF, VoucherRuleE.class));
    }

    /**
     * 修改
     * @param voucherRuleF
     * @return
     */
    public Boolean edit(VoucherRuleF voucherRuleF) {
        validCondition(voucherRuleF);
        oldVoucherRuleDomainService.update(Global.mapperFacade.map(voucherRuleF, VoucherRuleE.class));
        return Boolean.TRUE;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Boolean deleteById(Long id) {

        return oldVoucherRuleDomainService.deleteById(id);
    }

    /**
     * 禁用/启用规则
     * @param id
     * @param disabled
     * @return
     */
    public Boolean enableById(Long id, Integer disabled) {

        return oldVoucherRuleDomainService.enableById(id, disabled);
    }

    /**
     * 获取单个详情
     * @param id
     * @return
     */
    public OldVoucherRuleV queryById(Long id) {
        Optional<VoucherRuleE> voucherRuleE = oldVoucherRuleDomainService.queryById(id);
        if (voucherRuleE.isEmpty()) {
            throw BizException.throw400("该推凭规则不存在");
        }
        OldVoucherRuleV oldVoucherRuleV = Global.mapperFacade.map(voucherRuleE.get(), OldVoucherRuleV.class);
        oldVoucherRuleV.setEventName(EventTypeEnum.valueOfByCodeByEvent(oldVoucherRuleV.getEventType()).name());
        return oldVoucherRuleV;
    }

    /**
     * 排序
     * @param id
     * @param sortNum
     * @return
     */
    public Boolean sortById(Long id, Integer sortNum) {
        return oldVoucherRuleDomainService.sortById(id, sortNum);
    }

    /**
     * 手动推凭
     * @param id
     * @return
     */
    public Boolean runInferenceByRule(Long id) {
        VoucherRuleE voucherRuleE = oldVoucherRuleDomainService.getById(id);
        if (voucherRuleE == null) {
            throw BizException.throw404(ErrorMessage.VOUCHER_RULE_NOT_EXIST.msg());
        }
        ErrorAssertUtil.isTrueThrow403( DataDisabledEnum.启用.equalsByCode(voucherRuleE.getDisabled()), ErrorMessage.VOUCHER_RULE_IS_DISABLED);
        voucherInferenceAppServiceFactory.getInstance(EventTypeEnum.valueOfByCodeByEvent(voucherRuleE.getEventType())).inference(voucherRuleE,
            false);
        return Boolean.TRUE;
    }

    /**
     * 新版规则列表
     * @return
     */
    public List<VoucherRuleEventV> listRule() {

        DictionaryItemF dictionaryItemF = new DictionaryItemF();
        dictionaryItemF.setDictionaryCode("INFER_VOUCHER_EVENT_TYPE");
        List<DictionaryItemRV> dictionaryItemRVS = configClient.listDictionary(dictionaryItemF);

        if (CollectionUtils.isEmpty(dictionaryItemRVS)) {
            throw BizException.throw404("找不到触发事件！");
        }

        List<VoucherRuleE> voucherRuleList = oldVoucherRuleDomainService.listAll();

        Map<Integer, List<VoucherRuleE>> map = null;
        if (!CollectionUtils.isEmpty(voucherRuleList)) {
            map = voucherRuleList.stream()
                .collect(Collectors.groupingBy(VoucherRuleE::getEventType));
        }

        List<VoucherRuleEventV> list = new ArrayList<>(dictionaryItemRVS.size());
        Map<Integer, List<VoucherRuleE>> finalMap = map;
        dictionaryItemRVS.forEach(rv -> {
            VoucherRuleEventV eventV = new VoucherRuleEventV();
            eventV.setEventCode(Integer.valueOf(rv.getCode()));
            eventV.setEventName(rv.getName());
            if (finalMap != null && finalMap.containsKey(eventV.getEventCode())) {
                eventV.setList(finalMap.get(eventV.getEventCode()));
            }
            list.add(eventV);
        });

        return list;

    }

    private void validCondition(VoucherRuleF voucherRuleF) {
        if (StringUtils.isBlank(voucherRuleF.getFilterConditions())) {
            throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_NOT_EXIST.msg());
        }
        List<VoucherRuleCondition> conditionList = JSONArray.parseArray(voucherRuleF.getFilterConditions(), VoucherRuleCondition.class);
        if (CollectionUtils.isEmpty(conditionList)) {
            throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_NOT_EXIST.msg());
        }
        boolean conditionMatch = conditionList.stream().anyMatch(condition -> Objects.isNull(
            condition.getConditions()) || Objects.isNull(condition.getCompare()) || Objects.isNull(condition.getValue()));
        if (conditionMatch) {
            throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_NOT_EXIST.msg());
        }
        // 收付款银行
        List<String> bank = Arrays.asList("9", "10");
        boolean bankMatch = conditionList.stream()
            .anyMatch(voucherRuleCondition -> bank.contains(voucherRuleCondition.getConditions()));
        if (bankMatch) {
            List<String> beforeBank = Arrays.asList("1", "5");
            boolean beforeBankMatch = conditionList.stream()
                .anyMatch(voucherRuleCondition -> beforeBank.contains(voucherRuleCondition.getConditions()));
            if (!beforeBankMatch) {
                throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_ACCOUNT_BEFORE_NOT_EXIST.msg());
            }
        }
    }
}
