package com.wishare.finance.apps.service.pushbill;


import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.pushbill.fo.DelPushBillF;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushBillF;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillSysEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.service.VoucherBillDomainService;
import com.wishare.finance.infrastructure.remote.vo.fy.FYSendresultV;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillAppService implements ApiBase {
    private final VoucherBillDomainService voucherBillDomainService;

    public Boolean syncBatchPushBill(SyncBatchPushBillF syncBatchPushBillF) {
       return voucherBillDomainService.syncBatchPushBill(syncBatchPushBillF.getVoucherIds(), PushBillSysEnum.valueOfByCode(syncBatchPushBillF.getVoucherSystem()));
    }

    public FYSendresultV delVoucherBill(DelPushBillF delF) {
        return voucherBillDomainService.delVoucherBill(delF);
    }
}
