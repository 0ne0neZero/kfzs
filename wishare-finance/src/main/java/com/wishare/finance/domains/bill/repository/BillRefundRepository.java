package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.repository.mapper.BillRefundMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 退款资源库
 * @Author dxclay
 * @Date 2022/9/28
 * @Version 1.0
 */
@Service
public class BillRefundRepository extends ServiceImpl<BillRefundMapper, BillRefundE> {

    /**
     * 根据审批记录id获取退款记录
     * @param billApproveId
     * @return
     */
    public BillRefundE getByBillApproveId(Long billApproveId) {
        LambdaQueryWrapper<BillRefundE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BillRefundE::getBillApproveId, billApproveId);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据账单id获取退款记录
     *
     * @param billId
     * @return
     */
    public List<BillRefundE> listByBillId(Long billId) {
        LambdaQueryWrapper<BillRefundE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BillRefundE::getBillId, billId);
        wrapper.in(BillRefundE::getState, RefundStateEnum.已退款.getCode());
        return baseMapper.selectList(wrapper);
    }

    /**
     * 分页查询应收账单退款列表
     *
     * @param form
     * @return
     */
    public Page<ReceivableBillRefundDto> queryReceivableRefundPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.state", RefundStateEnum.已退款.getCode());
        queryModel.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        queryModel.orderByDesc("b.gmt_create");
        PageQueryUtils.validQueryContainsFieldAndValue(form, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryReceivableRefundPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 查询账应收账单退款列表
     *
     * @param form
     * @return
     */
    public List<ReceivableBillRefundDto> queryReceivableRefundList(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(form, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryReceivableRefundList(queryModel);
    }

    /**
     * 分页查询账临时账单退款列表
     *
     * @param form
     * @return
     */
    public Page<TempChargeBillRefundDto> queryTempChargeRefundPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.state", RefundStateEnum.已退款.getCode());
        queryModel.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        queryModel.orderByDesc("b.gmt_create");
        PageQueryUtils.validQueryContainsFieldAndValue(form, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryTempChargeRefundPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 查询临时账单退款列表
     *
     * @param form
     * @return
     */
    public List<TempChargeBillRefundDto> queryTempChargeRefundList(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.state", RefundStateEnum.已退款.getCode());
        queryModel.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        queryModel.orderByDesc("b.gmt_create");
        PageQueryUtils.validQueryContainsFieldAndValue(form, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryTempChargeRefundList(queryModel);
    }

    /**
     * 分页查询预收账单退款列表
     *
     * @param form
     * @return
     */
    public Page<AdvanceBillRefundDto> queryAdvanceRefundPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("br.state", RefundStateEnum.已退款.getCode());
        queryModel.orderByDesc("b.gmt_create");
        return baseMapper.queryAdvanceRefundPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 批量更新退款记录的推凭状态
     * @param inferRefundIds
     */
    public void batchUpdateRefundInferenceState(List<Long> inferRefundIds) {
        baseMapper.batchUpdateRefundInferenceState(inferRefundIds);
    }
}
