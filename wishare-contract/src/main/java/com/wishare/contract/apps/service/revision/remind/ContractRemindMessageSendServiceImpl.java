package com.wishare.contract.apps.service.revision.remind;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.fo.remind.MessageSendReqF;
import com.wishare.contract.apps.mq.MessageSend;
import com.wishare.contract.apps.remote.clients.ZJMessageClient;
import com.wishare.contract.apps.remote.fo.message.CommandMsgD;
import com.wishare.contract.apps.remote.fo.message.MessageF;
import com.wishare.contract.apps.remote.fo.message.MsgCommandEnum;
import com.wishare.contract.domains.enums.revision.RemindMessageConfigEnum;
import com.wishare.contract.domains.enums.revision.RevTypeEnum;
import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;
import com.wishare.contract.domains.vo.revision.remind.MessageRespV;
import com.wishare.contract.domains.vo.revision.remind.TokenDetailV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RefreshScope
public class ContractRemindMessageSendServiceImpl implements ContractRemindMessageSendService {

    @Autowired
    private ZJMessageClient zjMessageClient;

    @Resource
    private MessageSend messageSend;

    @Value("${contract.remind.appId:wl2c5e89d5c4}")
    private String appId;

    @Value("${contract.remind.secret:8qChgMSNtZ2zrMKfw0fnClkLObqXx72RxPElrZoPYeM}")
    private String secret;

    @Value("${contract.remind.agentId:1002659}")
    private String agentId;

    @Value("${contract.remind.tokenCacheKey:contract_remind_key}")
    private String tokenCacheKey;

    @Value("${contract.remind.templateId:168326536666018}")
    private Long contractTemplateId;

    @Value("${income.plan.remind.templateId:168326536666019}")
    private Long incomePlanTemplateId;

    @Value("${income.contract.detail.url:https://jxfw.ccccltd.cn/#/contractProcessing?flag=0&userCode=%s&uploadFlag=0&type=2&id=%s}")
    private String incomeContractDetailUrl;

    @Value("${pay.contract.detail.url:https://jxfw.ccccltd.cn/#/contractProcessing?flag=0&userCode=%s&uploadFlag=0&type=1&id=%s}")
    private String payContractDetailUrl;

    /**
     * 中建通没有测试环境，测试环境不发消息，线上配置为true
     */
    @Value("${zj.message.switch:false}")
    private Boolean zjMessageSwitch;

    @Value("${charge.remind.templateId:168326536666020}")
    private Long templateId;

    /**
     * 发送PC端提醒/预警通知
     *
     * @param userIds
     * @param info
     */
    @Override
    public void sendPCNoticeMessage(Set<String> userIds, ContractAndPlanInfoV info) {
        if (CollUtil.isEmpty(userIds) || Objects.isNull(info)) {
            log.info("人员id或合同信息为空");
            return;
        }
        RemindMessageConfigEnum messageConfig = info.getMessageConfig();
        String[] params;
        if (RemindMessageConfigEnum.EXPIRE_MSG.equals(messageConfig)) {
            String contractStartEndDate = info.getContractGmtExpireStart() + "至" + info.getContractGmtExpireEnd();
            params = new String[]{info.getContractName(), info.getContractNo(),
                    info.getHandlerName(), contractStartEndDate};
        } else {
            params = new String[]{info.getContractName(), info.getContractNo(),
                    info.getHandlerName(), info.getPlannedCollectionTime().toString()};
        }
        MessageF messageF = new MessageF();
        messageF.setParams(params)
                .setTypeId(messageConfig.getTypeId())
                .setTitle(messageConfig.getTitle())
                .setTemplateId(RemindMessageConfigEnum.EXPIRE_MSG.equals(messageConfig)
                        ? contractTemplateId : incomePlanTemplateId)
                .setUserIds(String.join(",", userIds));
        CommandMsgD<MessageF> commandMsgD = new CommandMsgD<>();
        commandMsgD.setCommandType(MsgCommandEnum.SEND_MESSAGE)
                .setData(messageF);
        messageSend.sendMessage(commandMsgD);
    }

    /**
     * 发送中建端提醒/预警消息
     *
     * @param userThirdPartyIds
     * @param info
     */
    @Override
    public void sendZJNoticeMessage(Set<String> userThirdPartyIds, ContractAndPlanInfoV info) {
        if (CollUtil.isEmpty(userThirdPartyIds)) {
            log.info("员工三方id为空");
            return;
        }
        String description = RemindMessageConfigEnum.EXPIRE_MSG.equals(info.getMessageConfig())
                ? contractExpireDesc(info) : planRemindOverdueDesc(info);
        for (String userThirdPartyId : userThirdPartyIds) {
            MessageSendReqF req = new MessageSendReqF();
            req.setTouser(userThirdPartyId);
            req.setAgentid(agentId);
            req.setMsgtype("textcard");

            Map<String, String> textcard = MapUtil.newHashMap();
            textcard.put("title", info.getMessageConfig().getTitle());
            textcard.put("description", description);
            String url;
            if (RevTypeEnum.收入合同.equals(info.getContractNature())) {
                url = String.format(incomeContractDetailUrl, userThirdPartyId, info.getContractId());
            } else {
                url = String.format(payContractDetailUrl, userThirdPartyId, info.getContractId());
            }
            textcard.put("url", url);
            req.setTextcard(textcard);

            try {
                if (zjMessageSwitch) {
                    log.info("发送中建消息入参:{}", JSON.toJSONString(req));
                    MessageRespV messageRespV = zjMessageClient.sendMessage(getToken(), req);
                    log.info("发送中建消息出参:{}", JSON.toJSONString(messageRespV));
                }
            } catch (Exception e) {
                log.error("发送中建消息失败,msg{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 发送PC端提醒/预警通知
     * @param userId
     */
    @Override
    public void sendPCNoticeMessage(String userId, Boolean flag, String reason) {

        MessageF messageF = new MessageF();
        String[] params;
        if (flag) {
            params = new String[]{"支付流程提醒", "您的业务支付申请单已成功通过财务初审阶段，请即刻登录电脑系统，发起付款审批流程。"};
        }else{
            params = new String[]{"初审驳回提醒", "您的业务支付申请单财务初审阶段被驳回，驳回原因"+ reason +"，请即刻登录电脑系统处理。"};
        }
        messageF.setParams(params)
                .setTypeId("1")
                .setTitle("支付申请单初审结果")
                .setTemplateId(templateId)
                .setUserIds(userId);
        log.info("消息参数-userId:{} ", userId);

        CommandMsgD<MessageF> commandMsgD = new CommandMsgD<>();
        commandMsgD.setCommandType(MsgCommandEnum.SEND_MESSAGE)
                .setData(messageF);
        messageSend.sendMessage(commandMsgD);
    }

    /**
     * 发送中建端提醒/预警消息
     *
     * @param userThirdPartyId
     */
    @Override
    public void sendZJNoticeMessage(String userThirdPartyId, Boolean flag, String reason) {
        if (StringUtils.isBlank(userThirdPartyId)) {
            log.info("员工三方id为空");
            return;
        }
        String description;
        if (flag) {
            description = "【支付流程提醒】 您的业务支付申请单已成功通过财务初审阶段，请即刻登录电脑系统，发起付款审批流程。";
        }else{
            description = "【初审驳回提醒】 您的业务支付申请单财务初审阶段被驳回，驳回原因："+ reason +"，请即刻登录电脑系统处理。";
        }

        MessageSendReqF req = new MessageSendReqF();
        req.setTouser(userThirdPartyId);
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

                log.info("发送中建消息出参:{}", JSON.toJSONString(messageRespV));
            }
        } catch (Exception e) {
            log.error("发送中建消息失败,msg{}", e.getMessage(), e);
        }
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

    /**
     * 合同过期预警卡片文案描述
     *
     * @param info
     * @return
     */
    private String contractExpireDesc(ContractAndPlanInfoV info) {
        String startEnd = info.getContractGmtExpireStart() + "至" + info.getContractGmtExpireEnd();
        return String.format("<div class=\"gray\">合同名称：%s</div>" +
                        "<div class=\"gray\">合同编号：%s</div>" +
                        "<div class=\"gray\">经办人：%s</div>" +
                        "<div class=\"gray\">合同起止日期：%s</div>",
                info.getContractName(), info.getContractNo(), info.getHandlerName(), startEnd);
    }

    /**
     * 收款提醒/收款逾期预警卡片文案描述
     *
     * @param info
     * @return
     */
    private String planRemindOverdueDesc(ContractAndPlanInfoV info) {
        return String.format("<div class=\"gray\">合同名称：%s</div>" +
                        "<div class=\"gray\">合同编号：%s</div>" +
                        "<div class=\"gray\">经办人：%s</div>" +
                        "<div class=\"gray\">收款日期：%s</div>",
                info.getContractName(), info.getContractNo(), info.getHandlerName(), info.getPlannedCollectionTime());
    }
}
