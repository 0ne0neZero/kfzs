package com.wishare.finance.apps.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.voucher.IVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.BillBatchActionEvent;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * 账单事件处理器
 *
 * @Author dxclay
 * @Date 2022/11/4
 * @Version 1.0
 */
@Slf4j
@Component
public class BatchVoucherInferEventHandler implements Consumer<BillBatchActionEvent> {

    @Autowired
    private IVoucherInferenceAppService defaultVoucherInferenceAppService;;

    @Override
    public void accept(BillBatchActionEvent event) {
        /*log.info("账单批量推凭事件： " + JSONObject.toJSONString(event));

        // 加入租户隔离
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(event.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

        // 判断账单事件
        ActionEventEnum actionEvent = null;
        switch (event.getAction()) {
            case ADJUSTED_BATCH:
                actionEvent = ActionEventEnum.调整;
                break;
            case SETTLED_BATCH:
                actionEvent = ActionEventEnum.结算;
                break;
            case INVOICED_BATCH:
                actionEvent = ActionEventEnum.开票;
                break;
            case VERIFY_BATCH:
                actionEvent = ActionEventEnum.销账;
                break;
            case INVALIDED_BATCH:
            case REFUND_BATCH:
            case REVERSED_BATCH:
                actionEvent = ActionEventEnum.冲销;
                break;
            case APPROVED_BATCH:
                actionEvent = ActionEventEnum.计提;
                break;
            default:
                break;
        }
        if (actionEvent != null) {
            try {
                if (!CollectionUtils.isEmpty(event.getBillIds())) {
//                    List<Long> billIds = event.getBillIds();
//                    billIds.forEach(billId -> {
//                        defaultVoucherInferenceAppService.singleInference(billId,
//                                BillTypeEnum.valueOfByCode(event.getBillType()), finalActionEvent, 0L);
//                    });
//                    defaultVoucherInferenceAppService.batchSingleInference(event.getBillIds(), BillTypeEnum.valueOfByCode(event.getBillType()), actionEvent, 0L, false);
                }
            } catch (Exception e) {
                log.error("账单推凭失败！ 推凭事件：{}", JSON.toJSONString(event));
                log.error("账单推凭失败！ 失败原因:", e);
            }
        }*/
    }

}
