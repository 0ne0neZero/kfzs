package com.wishare.finance.apps.service.notice;

import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.acl.AclMsgClientService;
import com.wishare.finance.apps.service.acl.AclSpaceClientService;
import com.wishare.finance.infrastructure.remote.enums.MsgModelCodeEnum;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
import com.wishare.starter.beans.WebSocketEvent;
import com.wishare.starter.enums.MsgCommandEnum;
import com.wishare.starter.msg.CommandMsgD;
import com.wishare.starter.msg.WishareMsgGateway;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Service
@Slf4j
public class NoticeService{

    @Setter(onMethod_ = {@Autowired})
    private AclMsgClientService aclMsgClientService;

    @Setter(onMethod_ = {@Autowired})
    private AclSpaceClientService aclSpaceClientService;

    @Setter(onMethod_ = {@Autowired})
    private WishareMsgGateway wishareMsgGateway;

    /**
     *
     * @param title
     * @param communityId
     * @param msgModelCodeEnum
     */
    public void noticeBusiness1(String title, String communityId, MsgModelCodeEnum msgModelCodeEnum,Integer noticeCardType,String userId) {
        NoticeBusinessD noticeBusinessD = new NoticeBusinessD();
        noticeBusinessD.setCardCode("CHARGE_ASSERT_CHANGE")
                .setNoticeCardType(1)
                .setNoticeCardTypeName("文章消息卡片")
                .setTitle(title)
                .setContent(title)
                .setModelCode(msgModelCodeEnum.getCode())
                .setModelCodeName(msgModelCodeEnum.getName())
                .setNoticeTime(LocalDateTime.now())
                .setUserId(List.of(userId));
        aclMsgClientService.add(noticeBusinessD);
    }




    /**
     *
     * @param noticeBusinessD
     * @param userId
     */
    public void messagesSend(NoticeBusinessD noticeBusinessD,String userId,String scene,String content) {
        noticeBusinessD.setUserId(List.of(userId));
        if(StringUtils.equals(noticeBusinessD.getWbsMessage(),"1")){
            //webSocket 弹窗
            this.sendAlertWbsMessage(userId,scene,content);
        }
        aclMsgClientService.add(noticeBusinessD);
    }


    /**
     * websocket消息处理
     * @param userId
     * @param scene
     * @param content
     * @return
     */
    private int sendAlertWbsMessage(String userId,String scene,String content) {
        log.info("sendAlertWbsMessage:userId[{}],scene[{}],content[{}]",userId,scene,content);
        try {
            CommandMsgD<WebSocketEvent> commandMsgD = new CommandMsgD();
            //doWebSocketEvent
            commandMsgD.setData(this.doWebSocketEvent(userId,scene,content));
            commandMsgD.setCommandType(MsgCommandEnum.WEBSOCKET);
            wishareMsgGateway.sendMessageCommand(commandMsgD);
            return 1;
        } catch (Exception e) {
            log.error("站内信发送异常",e);
            return 0;
        }
    }


    /**
     * 处理 WebSocketEvent
     * @param userId
     * @param scene 前端根据这个类型来针对处理
     * @param content 内容
     * @return
     */
    private WebSocketEvent doWebSocketEvent(String userId,String scene,String content){
        WebSocketEvent event = new WebSocketEvent();
        event.setClient("co-pc");
        event.setHolder(userId);
        event.setScene(scene);
        event.setContent(content);
        return event;
    }

}
