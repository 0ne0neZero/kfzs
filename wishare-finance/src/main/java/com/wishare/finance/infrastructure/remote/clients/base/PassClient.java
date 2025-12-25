package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.apps.model.configure.chargeitem.fo.VisitorApprovalProcessF;
import com.wishare.starter.annotations.OpenFeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@OpenFeignClient(name = "wishare-pass", serverName = "pass服务", path = "/pass")
public interface PassClient {

    @PostMapping("/visitor/callback/approval")
    void visitorApprovalCallback(VisitorApprovalProcessF visitorApprovalProcessF);
}
