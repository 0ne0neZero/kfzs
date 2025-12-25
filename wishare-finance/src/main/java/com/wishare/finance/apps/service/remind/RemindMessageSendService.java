package com.wishare.finance.apps.service.remind;


import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;

import java.util.List;

public interface RemindMessageSendService {


    void sendPCNoticeMessage(List<String> userId, Boolean flag, String reason);

    void sendZJNoticeMessage(List<String> userThirdPartyId, Boolean flag, String reason, PaymentApplicationFormZJ paymentApplicationFormZJ);
}
