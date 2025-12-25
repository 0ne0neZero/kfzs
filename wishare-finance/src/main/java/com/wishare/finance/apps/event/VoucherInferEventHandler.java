package com.wishare.finance.apps.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.voucher.IVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class VoucherInferEventHandler implements Consumer<BillActionEvent> {

    @Autowired
    private IVoucherInferenceAppService defaultVoucherInferenceAppService;;

    @Override
    public void accept(BillActionEvent event) {
        //log.info("账单推凭事件： " + JSONObject.toJSONString(event));

        //// 加入租户隔离
        //IdentityInfo identityInfo = new IdentityInfo();
        //identityInfo.setTenantId(event.getTenantId());
        //ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //
        //// 判断账单事件
        //ActionEventEnum actionEvent = null;
        //switch (event.getAction()) {
        //    case ADJUSTED:
        //        actionEvent = ActionEventEnum.调整;
        //        break;
        //    case SETTLED:
        //        actionEvent = ActionEventEnum.结算;
        //        break;
        //    case INVOICED:
        //        actionEvent = ActionEventEnum.开票;
        //        break;
        //    case VERIFY:
        //        actionEvent = ActionEventEnum.销账;
        //        break;
        //    case INVALIDED:
        //    case REFUND:
        //    case REVERSED:
        //        actionEvent = ActionEventEnum.冲销;
        //        break;
        //    case APPROVED:
        //        actionEvent = ActionEventEnum.计提;
        //        break;
        //    default:
        //        break;
        //}
        //if (actionEvent != null) {
        //    try {
        //        //defaultVoucherInferenceAppService.singleInference(event.getBillId(),
        //        //    BillTypeEnum.valueOfByCode(event.getBillType()), actionEvent, 0L);
        //    } catch (Exception e) {
        //        log.error("账单推凭失败！ 推凭事件：{}", JSON.toJSONString(event));
        //        log.error("账单推凭失败！ 失败原因:", e);
        //    }
        //}
    }

}
