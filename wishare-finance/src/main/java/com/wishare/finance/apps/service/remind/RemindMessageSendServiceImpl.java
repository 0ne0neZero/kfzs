package com.wishare.finance.apps.service.remind;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.mq.MessageSend;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormRepository;
import com.wishare.finance.infrastructure.remote.clients.base.JJMessageClient;
import com.wishare.finance.infrastructure.remote.fo.MessageSendReqF;
import com.wishare.finance.infrastructure.remote.vo.MessageRespV;
import com.wishare.finance.infrastructure.remote.vo.TokenDetailV;
import com.wishare.starter.enums.MsgCommandEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.msg.CommandMsgD;
import com.wishare.starter.msg.MessageF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class RemindMessageSendServiceImpl implements RemindMessageSendService {

    @Resource
    private JJMessageClient zjMessageClient;

    @Resource
    private MessageSend messageSend;

    @Resource
    private PaymentApplicationFormRepository paymentApplicationFormRepository;

    @Value("${contract.remind.appId:wl2c5e89d5c4}")
    private String appId;

    @Value("${contract.remind.secret:8qChgMSNtZ2zrMKfw0fnClkLObqXx72RxPElrZoPYeM}")
    private String secret;

    @Value("${contract.remind.agentId:1002659}")
    private String agentId;

    @Value("${contract.remind.tokenCacheKey:contract_remind_key}")
    private String tokenCacheKey;

    /**
     * 中建通没有测试环境，测试环境不发消息，线上配置为true
     */
    @Value("${zj.message.switch:false}")
    private Boolean zjMessageSwitch;

    @Value("${charge.remind.templateId:168326536666020}")
    private Long templateId;


    /**
     * 发送PC端提醒/预警通知
     * @param userIds
     */
    @Override
    public void sendPCNoticeMessage(List<String> userIds, Boolean flag, String reason) {

        MessageF messageF = new MessageF();
        String[] params;
        if (flag) {
            params = new String[]{"支付流程提醒", "您的业务支付申请单已成功通过财务初审阶段，请即刻登录电脑系统，发起付款审批流程。"};
        }else{
            params = new String[]{"初审驳回提醒", "您的业务支付申请单财务初审阶段被驳回，驳回原因：“"+ reason +"”，请即刻登录电脑系统处理。"};
        }
        messageF.setParams(params)
                .setTypeId("1")
                .setTitle("支付申请单初审结果")
                .setTemplateId(templateId)
                .setUserIds(String.join(",", userIds));
        log.info("消息参数-userId:{} ", userIds);

        CommandMsgD<MessageF> commandMsgD = new CommandMsgD<>();
        commandMsgD.setCommandType(MsgCommandEnum.SEND_MESSAGE)
                .setData(messageF);
        messageSend.sendMessageCommand(commandMsgD);
    }

    /**
     * 发送中建端提醒/预警消息
     *
     * @param userThirdPartyIds
     */
    @Override
    public void sendZJNoticeMessage(List<String> userThirdPartyIds, Boolean flag, String reason, PaymentApplicationFormZJ paymentApplicationFormZJ) {
        if (CollectionUtils.isEmpty(userThirdPartyIds)) {
            log.info("员工三方id为空");
            paymentApplicationFormZJ.setPushJjtStatus(2);
            paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
            return;
        }
        String description;
        if (flag) {
            description = "【支付流程提醒】 您的业务支付申请单已成功通过财务初审阶段，请即刻登录电脑系统，发起付款审批流程。";
        }else{
            description = "【初审驳回提醒】 您的业务支付申请单财务初审阶段被驳回，驳回原因：“"+ reason +"”，请即刻登录电脑系统处理。";
        }

        MessageSendReqF req = new MessageSendReqF();
        req.setTouser(String.join("|", userThirdPartyIds));
        req.setAgentid(agentId);
        req.setMsgtype("textcard");

        Map<String, String> textcard = MapUtil.newHashMap();
        textcard.put("title", "支付申请单初审结果");
        textcard.put("description", description);
        req.setTextcard(textcard);

        try {
            if (zjMessageSwitch) {
                log.info("发送中建消息入参:{}", JSON.toJSONString(req));
                MessageRespV messageRespV = zjMessageClient.sendMessage(getToken(), req);
                paymentApplicationFormZJ.setPushJjtStatus(1);
                log.info("发送中建消息出参:{}", JSON.toJSONString(messageRespV));
            }
        } catch (Exception e) {
            paymentApplicationFormZJ.setPushJjtStatus(2);
            log.error("发送中建消息失败,msg{}", e.getMessage(), e);
        }
        paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
    }




    /**
     * 获取中建交互的token
     *
     * @return
     */
    private String getToken() {
        String token = RedisHelper.get(tokenCacheKey);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        TokenDetailV tokenDetail;
        try {
            log.info("获取中建交互token入参:{},{}", appId, secret);
            tokenDetail = zjMessageClient.getToken(appId, secret);
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
