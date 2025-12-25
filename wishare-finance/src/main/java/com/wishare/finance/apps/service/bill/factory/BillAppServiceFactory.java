package com.wishare.finance.apps.service.bill.factory;

import com.wishare.finance.apps.service.bill.AdvanceBillAppService;
import com.wishare.finance.apps.service.bill.BillAppService;
import com.wishare.finance.apps.service.bill.PayableBillAppService;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 账单工厂
 * @author: pgq
 * @since: 2022/10/9 17:34
 * @version: 1.0.0
 */
@Component
public class BillAppServiceFactory {

    private Map<BillTypeEnum, BillAppService> billAppServiceMap = new HashMap<>();

    public BillAppServiceFactory(AdvanceBillAppService advanceBillAppService,
                                 ReceivableBillAppService receivableBillAppService,
                                 TemporaryChargeBillAppService temporaryChargeBillAppService,
                                 PayableBillAppService payableBillAppService) {
        billAppServiceMap.put(BillTypeEnum.应收账单, receivableBillAppService);
        billAppServiceMap.put(BillTypeEnum.预收账单, advanceBillAppService);
        billAppServiceMap.put(BillTypeEnum.临时收费账单, temporaryChargeBillAppService);
        billAppServiceMap.put(BillTypeEnum.应付账单, payableBillAppService);
    }

    public BillAppService getBillServiceByBillType(int billType) {
        return billAppServiceMap.get(BillTypeEnum.valueOfByCode(billType));
    }

}
