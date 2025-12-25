package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.infrastructure.remote.fo.MessageSendReqF;
import com.wishare.finance.infrastructure.remote.vo.MessageRespV;
import com.wishare.finance.infrastructure.remote.vo.TokenDetailV;
import com.wishare.starter.annotations.OpenFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 中建client
 */
@OpenFeignClient(name = "jjMessageClient", url = "${zj.jjt.client.url:https://jjt.ccccltd.cn}")
public interface JJMessageClient {

    /**
     * 获取中建交互的token
     *
     * @param corpid
     * @param corpsecret
     * @return
     */
    @GetMapping("/cgi-bin/gettoken")
    TokenDetailV getToken(@RequestParam("corpid") String corpid, @RequestParam("corpsecret") String corpsecret);

    /**
     * 发送交建消息
     *
     * @param access_token
     * @param req
     * @return
     */
    @PostMapping("/cgi-bin/message/send")
    MessageRespV sendMessage(@RequestParam("access_token") String access_token, @RequestBody MessageSendReqF req);

}
