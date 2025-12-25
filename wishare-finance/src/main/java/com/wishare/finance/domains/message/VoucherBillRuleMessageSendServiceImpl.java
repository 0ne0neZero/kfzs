package com.wishare.finance.domains.message;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.mq.MessageSend;
import com.wishare.finance.infrastructure.remote.clients.base.JJMessageClient;
import com.wishare.finance.infrastructure.remote.clients.base.MsgClient;
import com.wishare.finance.infrastructure.remote.fo.MessageSendReqF;
import com.wishare.finance.infrastructure.remote.vo.MessageRespV;
import com.wishare.finance.infrastructure.remote.vo.MsgTemplateV;
import com.wishare.finance.infrastructure.remote.vo.TokenDetailV;
import com.wishare.starter.enums.MsgCommandEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.msg.CommandMsgD;
import com.wishare.starter.msg.MessageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class VoucherBillRuleMessageSendServiceImpl implements VoucherBillRuleMessageSendService {


    @Value("${contract.remind.appId:wl2c5e89d5c4}")
    private String appId;

    @Value("${contract.remind.secret:8qChgMSNtZ2zrMKfw0fnClkLObqXx72RxPElrZoPYeM}")
    private String secret;

    @Value("${contract.remind.agentId:1002659}")
    private String agentId;

    @Value("${voucher.rule.tokenCacheKey:voucher_rule_remind_key}")
    private String tokenCacheKey;

    @Value("${bill.rule.msgTemplateId:1853082662351}")
    private Long msgTemplateId;

    @Value("${bill.rule.msgTypeId:1}")
    private String msgTypeId;

    @Value("${bill.rule.remindProd:true}")
    private Boolean billRuleRemindProd;

    @Setter(onMethod_ = {@Autowired})
    private JJMessageClient jjMessageClient;

    @Setter(onMethod_ = {@Autowired})
    private MsgClient msgClient;

    @Setter(onMethod_ = {@Autowired})
    private MessageSend messageSend;


    @Override
    public void send2PC(Set<String> userIds, String title) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        MessageF messageF = new MessageF();
        messageF.setTypeId(msgTypeId)
                .setTitle(title)
                .setTemplateId(msgTemplateId)
                .setUserIds(String.join(",", userIds));
        CommandMsgD<MessageF> commandMsgD = new CommandMsgD<>();
        commandMsgD.setCommandType(MsgCommandEnum.SEND_MESSAGE).setData(messageF);
        messageSend.sendMessageCommand(commandMsgD);
    }

    @Override
    public void send2JJ(Set<String> user4ACodes, String title) {
        if (CollectionUtils.isEmpty(user4ACodes)) {
            return;
        }
        SearchF<?> searchF = new SearchF<>();
        List<Field> fields = new ArrayList<>();
        fields.add(new Field("id", msgTemplateId, 1));
        searchF.setFields(fields);
        List<MsgTemplateV> msgTemplateVS = msgClient.selectList(searchF);
        if (CollectionUtils.isEmpty(msgTemplateVS)) {
            return;
        }
        MessageSendReqF req = new MessageSendReqF();
        req.setTouser(String.join("|", user4ACodes));
        req.setAgentid(agentId);
        req.setMsgtype("text");
        Map<String, String> text = MapUtil.newHashMap();
        String contentTemplate = "【%s】%s";
        String content = String.format(contentTemplate, title, msgTemplateVS.get(0).getContent());
        text.put("content", content);
        req.setText(text);
        try {
            log.info("发送中建消息入参:{}", JSON.toJSONString(req));
            MessageRespV messageRespV = new MessageRespV();
            //只有线上环境才真正发交建通消息
            if (billRuleRemindProd) {
                messageRespV = jjMessageClient.sendMessage(getToken(), req);
            }
            log.info("发送中建消息出参:{}", JSON.toJSONString(messageRespV));
        } catch (Exception e) {
            log.error("发送中建消息失败,msg{}", e.getMessage(), e);
        }
    }

    private String getToken() {
        String token = RedisHelper.get(tokenCacheKey);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        TokenDetailV tokenDetail;
        try {
            log.info("获取中建交互token入参:{},{}", appId, secret);
            tokenDetail = jjMessageClient.getToken(appId, secret);
            log.info("获取中建交互token出参:{}", JSON.toJSONString(tokenDetail));
        } catch (Exception e) {
            log.error("获取中建交互token失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "获取中建交互token失败", e);
        }
        String access_token = tokenDetail.getAccess_token();
        String expiresIn = tokenDetail.getExpires_in();
        RedisHelper.setAtExpire(tokenCacheKey, Long.parseLong(expiresIn) - 10, access_token);
        return access_token;
    }
}
