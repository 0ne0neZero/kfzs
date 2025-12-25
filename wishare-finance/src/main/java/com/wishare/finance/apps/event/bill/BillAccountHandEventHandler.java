package com.wishare.finance.apps.event.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.bill.HandAccountAppService;
import com.wishare.finance.domains.bill.command.RefreshAccountHandCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 账单交账相关事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAccountHandEventHandler implements Consumer<BillActionEvent> {

    private final HandAccountAppService handAccountAppService;

    @Override
    public void accept(BillActionEvent billActionEvent) {
        // try {
        //     // log.info("交账事件MQ消息监听：{}", JSONObject.toJSONString(billActionEvent));
        //     String messageKey = "bill_hand" + billActionEvent.getUniqueMessageKey();
        //     if (!RedisHelper.setNotExists(messageKey, "1")) {
        //         log.info("交账账单事件MQ消息监听-事件已消费，账单id：{}", billActionEvent.getBillId());
        //         return;
        //     }
        //     RedisHelper.expire(messageKey, 60*60L);
        //     //补充身份标识
        //     IdentityInfo identityInfo = new IdentityInfo();
        //     identityInfo.setTenantId(billActionEvent.getTenantId());
        //     ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //     // 已作废和已冲销 需要删除交账列表账单
        //     if (billActionEvent.getAction().equals(BillAction.INVALIDED) || billActionEvent.getAction().equals(BillAction.REVERSED)) {
        //         handAccountAppService.updateAccountHandDelete(new RefreshAccountHandCommand(billActionEvent.getBillId(),
        //                 BillTypeEnum.valueOfByCode(billActionEvent.getBillType()), billActionEvent.getAction(), billActionEvent.getSupCpUnitId()));
        //     }else if (billActionEvent.getAction().equals(BillAction.FLOW_CLAIM) ){
        //         handAccountAppService.flowClaimAutoAccountHand(new RefreshAccountHandCommand(billActionEvent.getBillId(),
        //                 BillTypeEnum.valueOfByCode(billActionEvent.getBillType()), billActionEvent.getAction(), billActionEvent.getSupCpUnitId()));
        //     }else {
        //         handAccountAppService.refreshOrAddAccountHand(new RefreshAccountHandCommand(billActionEvent.getBillId(),
        //                 BillTypeEnum.valueOfByCode(billActionEvent.getBillType()), billActionEvent.getAction(), billActionEvent.getSupCpUnitId()));
        //     }
        // }catch (Exception e){
        //     log.error("账单交账单条变动异常, 变动参数：" + JSON.toJSONString(billActionEvent), e);
        // }
    }

}
