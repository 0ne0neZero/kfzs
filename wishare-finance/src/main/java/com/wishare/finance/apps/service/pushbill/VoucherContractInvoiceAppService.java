package com.wishare.finance.apps.service.pushbill;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.pushbill.vo.VoucherContractInvoiceZJV;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractMeasurementDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractMeasurementDetailZJRepository;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherContractInvoiceAppService implements ApiBase {

    private final VoucherContractMeasurementDetailZJRepository measurementDetailZJRepository;
    private final VoucherContractInvoiceZJRepository contractInvoiceZJRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;

    /**
     * 根据发票号获取
     *
     * @param invoiceNo 发票号
     * @return
     */
    public VoucherContractInvoiceZJV queryContractInvoiceInfoByInvoiceNo(String invoiceNo) {
        if (StringUtils.isBlank(invoiceNo)) {
            throw BizException.throw400("发票号为空");
        }
        // 根据发票号查询
        VoucherContractInvoiceZJ contractInvoiceZJ = contractInvoiceZJRepository.getOne(getWrapper().eq(VoucherContractInvoiceZJ::getInvoiceNo, invoiceNo).last("limit 1"));
        VoucherContractInvoiceZJV contractInvoiceZJV = Global.mapperFacade.map(contractInvoiceZJ, VoucherContractInvoiceZJV.class);

        // 根据主表id查询计量明细
        List<VoucherContractMeasurementDetailZJ> measurementDetailZJS = measurementDetailZJRepository.getByContractInvoiceId(contractInvoiceZJV.getId());
        contractInvoiceZJV.setMeasurementDetailZJS(measurementDetailZJS);
        return contractInvoiceZJV;
    }

    /**
     * 根据发票号删除
     *
     * @param invoiceNo 发票号
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteContractInvoiceByInvoiceNo(String invoiceNo) {
        if (StringUtils.isBlank(invoiceNo)) {
            throw BizException.throw400("发票号为空");
        }

        List<VoucherContractInvoiceZJ> contractInvoiceZJS = contractInvoiceZJRepository.list(getWrapper().eq(VoucherContractInvoiceZJ::getInvoiceNo, invoiceNo));
        List<VoucherContractInvoiceZJV> voList = Global.mapperFacade.mapAsList(contractInvoiceZJS, VoucherContractInvoiceZJV.class);
        for (VoucherContractInvoiceZJV contractInvoiceZJ : voList) {
            measurementDetailZJRepository.deleteByContractInvoiceId(contractInvoiceZJ.getId());
        }
        contractInvoiceZJRepository.remove(getWrapper().eq(VoucherContractInvoiceZJ::getInvoiceNo, invoiceNo));
    }

    private static LambdaQueryWrapper<VoucherContractInvoiceZJ> getWrapper() {
        return Wrappers.<VoucherContractInvoiceZJ>lambdaQuery();
    }

}
