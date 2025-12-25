package com.wishare.finance.apps.event.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.bill.HandAccountAppService;
import com.wishare.finance.domains.bill.command.BatchRefreshAccountHandCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.BillBatchActionEvent;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 账单交账批量变动事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAccountHandBatchEventHandler implements Consumer<BillBatchActionEvent> {

    private final HandAccountAppService handAccountAppService;

    @Override
    public void accept(BillBatchActionEvent billBatchActionEvent) {
        // try {
        //     //补充身份标识
        //     // log.info("批量交账事件MQ消息监听：{}", JSONObject.toJSONString(billBatchActionEvent));
        //     IdentityInfo identityInfo = new IdentityInfo();
        //     identityInfo.setTenantId(billBatchActionEvent.getTenantId());
        //     ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //
        //     handAccountAppService.batchRefreshOrAddAccountHand(new BatchRefreshAccountHandCommand(billBatchActionEvent.getBillIds(),
        //             BillTypeEnum.valueOfByCode(billBatchActionEvent.getBillType()), billBatchActionEvent.getSupCpUnitId()));
        // }catch (Exception e){
        //     log.error("账单交账批量变动异常, 变动参数：" + JSON.toJSONString(billBatchActionEvent), e);
        // }
    }

}
