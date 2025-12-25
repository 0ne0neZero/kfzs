package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.repository.mapper.OldVoucherRuleMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/10 16:12
 * @version: 1.0.0
 */
@Service
public class OldVoucherRuleRepository extends ServiceImpl<OldVoucherRuleMapper, VoucherRuleE> {

    @Autowired
    private OldVoucherRuleMapper oldVoucherRuleMapper;

    /**
     * 查询推凭规则
     * @param form
     * @param eventType
     * @param chargeItemId
     * @return
     */
    public Page<VoucherRuleE> queryPage(PageF<SearchF<?>> form, Integer eventType, Long chargeItemId) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("vr.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("vr.charge_item_id", chargeItemId);
        queryModel.eq("vr.event_type", eventType);
        queryModel.orderByAsc("vr.sort_num");
        return oldVoucherRuleMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 查询推凭规则
     * @param form
     * @param eventType
     * @return
     */
    public Page<VoucherRuleE> pageByEventType(PageF<SearchF<?>> form, Integer eventType) {SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("vr.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("vr.event_type", eventType);
        queryModel.orderByAsc("vr.sort_num");
        return oldVoucherRuleMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 排序（往前排）
     * @param eventType
     * @param beforeSortNum
     * @param sortNum
     */
    public void updateSortNumBySort(Integer eventType, Integer beforeSortNum, Integer sortNum) {
        oldVoucherRuleMapper.updateSortNumBySort(eventType, beforeSortNum, sortNum);
    }

    /**
     * 排序往后排
     * @param eventType
     * @param beforeSortNum
     * @param sortNum
     */
    public void updateSortNumBySortBigger(Integer eventType, Integer beforeSortNum, Integer sortNum) {
        oldVoucherRuleMapper.updateSortNumBySortBigger(eventType, beforeSortNum, sortNum);
    }

    /**
     * 获取当前事件的最大排序号
     * @param eventType
     * @return
     */
    public Integer getMaxSortNumByChargeItemIdAndEventType(Integer eventType) {
        return oldVoucherRuleMapper.getMaxSortNumByChargeItemIdAndEventType(eventType);
    }

    /**
     * 根据费项id和触发事件获取规则列表
     * @param chargeItemId
     * @param eventType
     * @param executeType
     * @return
     */
    public List<VoucherRuleE> getByChargeItemIdAndEventType(Long chargeItemId, int eventType, int executeType) {
        return oldVoucherRuleMapper.getByChargeItemIdAndEventType(chargeItemId, eventType, executeType);
    }

    /**
     * 根据条件获取规则列表
     * @param form
     * @return
     */
    public Page<VoucherRuleE> listForInference(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        conditions.setFields(new ArrayList<>());
        conditions.getFields().add(new Field("vr.disabled", DataDisabledEnum.启用.getCode(), 1));
        conditions.getFields().add(new Field("vr.deleted", DataDeletedEnum.NORMAL.getCode(), 1));
        conditions.getFields().add(new Field("vr.execute_type", 0, 1));
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.orderByAsc("vr.sort_num");
        return oldVoucherRuleMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 根据条件获取规则列表
     * @param chargeItemIds
     * @param eventType
     * @param executeType
     * @return
     */
    public List<VoucherRuleE> listByChargeItemIdAndEventTypeAndExecuteType(Set<Long> chargeItemIds, EventTypeEnum eventType, int executeType) {
        QueryWrapper<VoucherRuleE> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<VoucherRuleE> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(VoucherRuleE::getChargeItemId, chargeItemIds);
        StringJoiner joiner = new StringJoiner(",", "JSON_OVERLAPS(charge_item_id, JSON_ARRAY(", "))");
        if (!CollectionUtils.isEmpty(chargeItemIds)) {
            chargeItemIds.forEach(chargeItemId -> joiner.add(chargeItemId + ""));
        }
        wrapper.apply(joiner.toString(), (Object) null);
        wrapper.eq(VoucherRuleE::getEventType, eventType.getEvent());
        wrapper.eq(VoucherRuleE::getExecuteType, executeType);
        wrapper.eq(VoucherRuleE::getDisabled, DataDisabledEnum.启用.getCode());
        wrapper.eq(VoucherRuleE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        wrapper.orderByAsc(VoucherRuleE::getSortNum);
        return oldVoucherRuleMapper.selectList(wrapper);
    }

    public List<VoucherRuleE> listAll() {
        LambdaQueryWrapper<VoucherRuleE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherRuleE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        wrapper.orderByAsc(VoucherRuleE::getSortNum);
        return oldVoucherRuleMapper.selectListByWrapper(wrapper);
    }
}
