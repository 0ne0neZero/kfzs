package com.wishare.finance.domains.bill.repository;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;

/**
 *
 *
 * @Author dxclay
 * @Date 2022/10/10
 * @Version 1.0
 */
public class BillRepositoryFactory {

    public static <B> BillRepository getBillRepository(BillTypeEnum billType){
        switch (billType){
            case 应收账单:
            case 临时收费账单:
                return Global.ac.getBean(ReceivableBillRepository.class);
            case 预收账单:
                return Global.ac.getBean(AdvanceBillRepository.class);
            case 应付账单:
                return Global.ac.getBean(PayableBillRepository.class);
            default:
                throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
        }
    }

}
