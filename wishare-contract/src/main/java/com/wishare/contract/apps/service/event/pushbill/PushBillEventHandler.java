package com.wishare.contract.apps.service.event.pushbill;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.service.event.handler.CallBackHandlerProxy;
import com.wishare.contract.apps.service.event.model.ApproveProcessCompleteMsg;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author hhb
 * @describe
 * @date 2025/10/29 17:31
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillEventHandler implements Consumer<ApproveProcessCompleteMsg> {

    private final CallBackHandlerProxy callBackHandlerProxy;

    @Override
    public void accept(ApproveProcessCompleteMsg approveProcessCompleteMsg) {
        log.info("合同审批审核结果:{}", JSON.toJSONString(approveProcessCompleteMsg));
        // 加入中交租户隔离
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

        callBackHandlerProxy.handle(approveProcessCompleteMsg);
    }
}