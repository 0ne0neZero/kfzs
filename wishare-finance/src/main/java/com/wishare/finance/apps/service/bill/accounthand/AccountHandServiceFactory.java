package com.wishare.finance.apps.service.bill.accounthand;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @description: 交账接口工厂类
 * @author: pgq
 * @since: 2022/9/26 19:20
 * @version: 1.0.0
 */
@Component
public class AccountHandServiceFactory {

    private static final Map<BillTypeEnum, AccountHandService> factory = new HashMap<>();

    public AccountHandServiceFactory(List<AccountHandService> accountHandServices) {
        for (AccountHandService accountHandService : accountHandServices) {
            factory.put(accountHandService.getBillType(), accountHandService);
        }
    }

    public AccountHandService getAccountHandService(Integer billType) {
        return factory.get(BillTypeEnum.valueOfByCode(billType));
    }


}
