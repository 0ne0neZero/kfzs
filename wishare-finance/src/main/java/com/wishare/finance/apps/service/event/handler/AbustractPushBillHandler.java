package com.wishare.finance.apps.service.event.handler;

import com.wishare.finance.apps.model.voucher.IVoucherBillModel;
import com.wishare.finance.infrastructure.remote.enums.ApproveProcessCompleteEnum;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import org.apache.poi.ss.formula.functions.T;

public abstract class AbustractPushBillHandler implements PushBillHandler<T>{

}
