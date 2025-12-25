package com.wishare.finance.apps.service.remind;

import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.infrastructure.remote.vo.contract.FirstExamineMessageF;

public interface RemindMessageConfigService {

    void send(FirstExamineMessageF message, Boolean flag, PaymentApplicationFormZJ paymentApplicationFormZJ);
}
