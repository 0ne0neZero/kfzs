package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.BillRefundConditionF;
import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.repository.BillRefundRepository;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillRefundDomainService {

    private final BillRefundRepository billRefundRepository;


    /**
     * 分页查询应收账单退款列表
     *
     * @param form
     * @return
     */
    public Page<ReceivableBillRefundDto> queryReceivableRefundPage(PageF<SearchF<?>> form) {
        return billRefundRepository.queryReceivableRefundPage(form);
    }


    /**
     * 查询账应收账单退款列表
     *
     * @param form
     * @return
     */
    public List<ReceivableBillRefundDto> queryReceivableRefundList(PageF<SearchF<?>> form) {
        return billRefundRepository.queryReceivableRefundList(form);
    }

    /**
     * 分页查询账临时账单退款列表
     *
     * @param form
     * @return
     */
    public Page<TempChargeBillRefundDto> queryTempChargeRefundPage(PageF<SearchF<?>> form) {
        return billRefundRepository.queryTempChargeRefundPage(form);
    }

    /**
     * 查询临时账单退款列表
     *
     * @param form
     * @return
     */
    public List<TempChargeBillRefundDto> queryTempChargeRefundList(PageF<SearchF<?>> form) {
        return billRefundRepository.queryTempChargeRefundList(form);
    }

    /**
     * 分页查询预收账单退款列表
     *
     * @param form
     * @return
     */
    public Page<AdvanceBillRefundDto> queryAdvanceRefundPage(PageF<SearchF<?>> form) {
        return billRefundRepository.queryAdvanceRefundPage(form);
    }

    /**
     * 根据账单id获取退款记录
     *
     * @param billId
     */
    public List<BillRefundE> getByBillId(Long billId) {
        List<BillRefundE> billRefundEList = billRefundRepository.listByBillId(billId);
        return billRefundEList;
    }

    /**
     * 根据账单ids获取退款记录
     *
     * @param param
     */
    public List<BillRefundE> getByBillIds(BillRefundConditionF param) {
        LambdaQueryWrapper<BillRefundE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BillRefundE::getBillId, param.getBillIds());
        wrapper.in(BillRefundE::getDeleted, 0);
        wrapper.in(StringUtils.isNotBlank(param.getProcInstId()),BillRefundE::getOutRefundNo, param.getProcInstId());
        List<BillRefundE> billRefundEList = billRefundRepository.list(wrapper);
        return billRefundEList;
    }

    /**
     * 批量更新退款记录的推凭状态
     * @param inferRefundIds
     */
    public void batchUpdateRefundInferenceState(List<Long> inferRefundIds) {
        billRefundRepository.batchUpdateRefundInferenceState(inferRefundIds);
    }
}
