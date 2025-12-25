package com.wishare.finance.apps.service.pushbill;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.FlowClaimFilesV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJCashFlowV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJConvertDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJFileSV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJFlowDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherContractInvoiceZJV;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractMeasurementDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractMeasurementDetailZJRepository;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherContractMeasurementDetailAppService implements ApiBase {

    private final VoucherContractMeasurementDetailZJRepository measurementDetailZJRepository;
    private final VoucherContractInvoiceZJRepository contractInvoiceZJRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;



    private static LambdaQueryWrapper<VoucherContractMeasurementDetailZJ> getWrapper() {
        return Wrappers.<VoucherContractMeasurementDetailZJ>lambdaQuery();
    }

}
