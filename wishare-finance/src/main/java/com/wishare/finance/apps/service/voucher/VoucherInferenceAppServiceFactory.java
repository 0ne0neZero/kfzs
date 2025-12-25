package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 推凭事件工厂类
 * @author: pgq
 * @since: 2022/11/16 9:34
 * @version: 1.0.0
 */
@Component
public class VoucherInferenceAppServiceFactory {

    // 注入各个事件对应的业务service
    private static final Map<EventTypeEnum, IVoucherInferenceAppService> eventVoucherInferMap = new HashMap<>();

    // 使用构造方法自动注入service 如有新增只需实现自己的类以及
    public VoucherInferenceAppServiceFactory(
        List<IVoucherInferenceAppService> voucherInferenceAppServices) {
        for (IVoucherInferenceAppService voucherInferenceAppService : voucherInferenceAppServices) {
            eventVoucherInferMap.put(voucherInferenceAppService.getEventType(), voucherInferenceAppService);
        }
    }

    // 获取对应事件的service
    public IVoucherInferenceAppService getInstance(EventTypeEnum eventType) {
        return eventVoucherInferMap.get(eventType);
    }

}
