package com.wishare.finance.apps.event.pushbill;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.service.event.handler.CallBackHandlerProxy;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillEventHandler implements Consumer<ApproveProcessCompleteMsg> {

    private final CallBackHandlerProxy callBackHandlerProxy;

    @Override
    public void accept(ApproveProcessCompleteMsg approveProcessCompleteMsg) {
        log.info("报账审批审核结果:{}", JSON.toJSONString(approveProcessCompleteMsg));
        // 加入中交租户隔离
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
//        boolean pass = ApproveProcessCompleteEnum.通过.getValue()
//                .equals(approveProcessCompleteMsg.getBusinessStatus());
//        if (pass) {
//            pushBillZJAppService.approveAgree(
//                    Long.valueOf(approveProcessCompleteMsg.getBusinessKey()), approveProcessCompleteMsg.getProcInstId());
//        } else {
//            pushBillZJAppService.approveRefuse(
//                    Long.valueOf(approveProcessCompleteMsg.getBusinessKey()), approveProcessCompleteMsg.getProcInstId());
//        }

        callBackHandlerProxy.handle(approveProcessCompleteMsg);
    }
}
