package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.BillRefundConditionF;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.BillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.service.BillRefundDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
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
public class BillRefundAppService {

    private final BillRefundDomainService billRefundDomainService;

    /**
     * 分页查询应收账单退款列表
     *
     * @param form
     * @return
     */
    public PageV<ReceivableBillRefundDto> queryReceivableRefundPage(PageF<SearchF<?>> form) {
        Page<ReceivableBillRefundDto> receivableBillRefundDtoPage = billRefundDomainService.queryReceivableRefundPage(form);
        return PageV.of(form, receivableBillRefundDtoPage.getTotal(), receivableBillRefundDtoPage.getRecords());
    }

    /**
     * 查询账应收账单退款列表
     *
     * @param form
     * @return
     */
    public List<ReceivableBillRefundDto> queryReceivableRefundList(PageF<SearchF<?>> form) {
        List<ReceivableBillRefundDto> receivableBillRefundDtoList = billRefundDomainService.queryReceivableRefundList(form);
        return receivableBillRefundDtoList;
    }

    /**
     * 分页查询账临时账单退款列表
     * @param form
     * @return
     */
    public PageV<TempChargeBillRefundDto> queryTempChargeRefundPage(PageF<SearchF<?>> form) {
        Page<TempChargeBillRefundDto> tempChargeBillRefundDtoPage = billRefundDomainService.queryTempChargeRefundPage(form);
        return PageV.of(form, tempChargeBillRefundDtoPage.getTotal(), tempChargeBillRefundDtoPage.getRecords());
    }

    /**
     * 查询临时账单退款列表
     *
     * @param form
     * @return
     */
    public List<TempChargeBillRefundDto> queryTempChargeRefundList(PageF<SearchF<?>> form) {
        List<TempChargeBillRefundDto> tempChargeBillRefundDtos = billRefundDomainService.queryTempChargeRefundList(form);
        return tempChargeBillRefundDtos;
    }

    /**
     * 分页查询预收账单退款列表
     *
     * @param form
     * @return
     */
    public PageV<AdvanceBillRefundDto> queryAdvanceRefundPage(PageF<SearchF<?>> form) {
        Page<AdvanceBillRefundDto> advanceBillRefundDtoPage = billRefundDomainService.queryAdvanceRefundPage(form);
        return PageV.of(form, advanceBillRefundDtoPage.getTotal(), advanceBillRefundDtoPage.getRecords());
    }

    /**
     * 根据账单id获取退款记录
     *
     * @param billId
     * @return
     */
    public List<BillRefundDto> getByBillId(Long billId) {
        List<BillRefundE> billRefundEList = billRefundDomainService.getByBillId(billId);
        return Global.mapperFacade.mapAsList(billRefundEList, BillRefundDto.class);
    }

    /**
     * 根据账单ids获取退款记录
     *
     * @param param
     * @return
     */
    public List<BillRefundDto> getByBillIds(BillRefundConditionF param) {
        List<BillRefundE> billRefundEList = billRefundDomainService.getByBillIds(param);
        return Global.mapperFacade.mapAsList(billRefundEList, BillRefundDto.class);
    }

    /**
     * 批量更新退款记录的推凭状态
     * @param inferRefundIds
     * @return
     */
    public void batchUpdateRefundInferenceState(List<Long> inferRefundIds) {
        billRefundDomainService.batchUpdateRefundInferenceState(inferRefundIds);
    }
}
