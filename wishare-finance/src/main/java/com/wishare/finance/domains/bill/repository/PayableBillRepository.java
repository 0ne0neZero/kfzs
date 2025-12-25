package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.StatisticsBillAmountF;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.BillInferStateEnum;
import com.wishare.finance.domains.bill.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.PayableBill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.mapper.PayableBillMapper;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayableBillRepository extends ServiceImpl<PayableBillMapper, PayableBill> implements BillRepository<PayableBill> {

    /**
     * 查询周期性账单
     *
     * @param receivableBill receivableBill
     * @return List
     */
    public List<PayableBill> listByPeriodic(ReceivableBill receivableBill) {
        LambdaQueryWrapper<PayableBill> wrapper = new LambdaQueryWrapper<PayableBill>().eq(Bill::getCommunityId, receivableBill.getCommunityId())
                .eq(Bill::getChargeItemId, receivableBill.getChargeItemId())
                .eq(Bill::getRoomId, receivableBill.getRoomId())
                .ge(PayableBill::getStartTime, receivableBill.getStartTime())
                .le(PayableBill::getEndTime, receivableBill.getEndTime());
        return list(wrapper);
    }

    @Override
    public List<PayableBill> getByIdsNoTenant(List<Long> billIds,String supCpUnitId) {
        return baseMapper.getByIdsNoTenant(billIds,supCpUnitId);
    }

    @Override
    public Boolean updateInvoiceState(List<PayableBill> billEList) {
        return  baseMapper.updateInvoiceState(billEList);
    }

    @Override
    public BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid, Integer billRefund,String supCpUnitId) {
        return baseMapper.queryTotal(getQueryAndIdsWrapper(query, billIds, billInvalid,billRefund));
    }

    @Override
    public List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        return baseMapper.queryApproveTotal(wrapper);
    }

    @Override
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query) {
        return baseMapper.queryDiscountTotal(query);
    }

    @Override
    public IPage<PayableBill> queryBillByPage(PageF<SearchF<?>> query) {
        List<Field> fields = query.getConditions().getFields();
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
        QueryWrapper<?> wrapper;
        if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            wrapper = getInvalidBillWrapper(query);
        }else{
            wrapper = getWrapper(query);
        }
        return baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize()), wrapper);
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId) {
        return baseMapper.listBillHand(billIds,supCpUnitId);
    }

    @Override
    public BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        return baseMapper.queryBillReviewTotal(wrapper);
    }

    @Override
    public Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds) {
        SearchF<?> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<?> queryModel = searchF.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        if (billIds != null && !billIds.isEmpty()) {
            queryModel.notIn("b.id", billIds);
        }
        if (form.getFieldList() != null && !form.getFieldList().isEmpty()) {
            for (Field field : form.getFieldList()) {
                if ("adjust".equals(field.getName())) {
                    continue;
                }
                switch (field.getMethod()) {
                    case 1:
                        queryModel.eq(field.getName(), field.getValue());
                        break;
                    case 15:
                        queryModel.in(field.getName(), (List) field.getValue());
                        break;
                    case 6:
                        queryModel.le(field.getName(), field.getValue());
                        break;
                    case 4:
                        queryModel.ge(field.getName(), field.getValue());
                        break;
                    case 16:
                        queryModel.notIn(field.getName(), (List) field.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        if (form.getEventType() == EventTypeEnum.应收计提.getEvent() || form.getEventType() == EventTypeEnum.应付计提.getEvent()) {
            return baseMapper.queryAccrualBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.账单调整.getEvent() == form.getEventType()) {
            return baseMapper.queryAdjustBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.冲销作废.getEvent() == form.getEventType()) {
            return baseMapper.queryOffBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.预收应收核销.getEvent() == form.getEventType()) {
            return baseMapper.queryCloseBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        return baseMapper.queryBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
    }

    @Override
    public IPage<PayableBill> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF) {
        return baseMapper.queryBillByPageNoTenantLine(Page.of(queryF.getPageNum(), queryF.getPageSize()),getWrapper(queryF));
    }

    @Override
    public IPage<PayableBill> listByInitialBill(Page<Object> page,QueryWrapper<?> queryWrapper) {
        return baseMapper.listByInitialBill(page, queryWrapper);
    }

    /**
     * 批量获取应收账单信息
     *
     * @param billIds 账单id
     * @return List
     */
    public List<PayableBill> queryByIds(List<Long> billIds) {
        LambdaQueryWrapper<PayableBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PayableBill::getId, billIds);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param query query
     * @return BillTotalDto
     */
    public BillTotalDto statisticsBillRefund(StatisticsBillTotalQuery query) {
        QueryWrapper<?> queryWrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(query.getBillIds())) {
            queryWrapper = new QueryWrapper<>().in("br.id", query.getBillIds());
        } else {
            queryWrapper = query.getQuery().getConditions().getQueryModel();
        }
        queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode()).eq("b.deleted", DataDisabledEnum.启用.getCode());
        return baseMapper.statisticsBillRefund(queryWrapper);

    }

    /**
     * 催缴欠缴账单统计
     *
     * @param form form
     * @return BillTotalDto
     */
    public BillTotalDto call(StatisticsBillAmountF form) {
        QueryWrapper<?> queryWrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(form.getBillIds())) {
            queryWrapper = new QueryWrapper<>().in("b.id", form.getBillIds());
        } else {
            queryWrapper = form.getQuery().getConditions().getQueryModel();
        }
        queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode()).eq("b.deleted", DataDisabledEnum.启用.getCode());
        return baseMapper.call(queryWrapper);
    }

    /**
     * 查询凭证应付业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherPayableBillByQuery(QueryWrapper<?> wrapper, Integer sceneType, String tableName) {
        return baseMapper.listVoucherPayableBillByQuery(wrapper,
                sceneType,
                VoucherBusinessBillTypeEnum.应付单.getCode(),tableName);
    }

    public void updateInferenceState(List<Long> idList) {
        baseMapper.updateInferenceState(idList, BillInferStateEnum.已推凭.getCode());
    }
}
