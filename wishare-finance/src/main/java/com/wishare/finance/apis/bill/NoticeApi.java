package com.wishare.finance.apis.bill;

import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.mq.vo.TicketContent;
import com.wishare.finance.apps.service.notice.NoticeService;
import com.wishare.finance.domains.reconciliation.service.FlowClaimRecordDomainService;
import com.wishare.finance.infrastructure.remote.enums.MsgModelCodeEnum;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
import com.wishare.starter.interfaces.ApiBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账单api
 *
 * @author yancao
 */
@Api(tags = {"通知api"})
@Validated
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NoticeApi implements ApiBase {

    private final NoticeService noticeService;



    @PostMapping("/test")
    @ApiOperation(value = "通知测试", notes = "通知测试")
    public Boolean outApproveByOperationId(String communityId,Integer noticeCardType) {
        noticeService.noticeBusiness1("观望测试",communityId,MsgModelCodeEnum.FLOW_CLAIM,noticeCardType, getUserId().get());
        return true;
    }


    /**
     *
     * @param noticeBusinessD
     * @param scene
     * @return
     */
    @PostMapping("/messagesSend")
    @ApiOperation(value = "站内信发送", notes = "站内信发送")
    public Boolean messagesSend(@RequestBody NoticeBusinessD noticeBusinessD,String scene,String content) {
        String msg = "收款单号【{no}】，{roomName}，开具的收据调用E签宝签章失败，失败原因：${errMsg}。";
        String resultMsg = msg.replace("{no}", "TTYSJ202404220000001")
                .replace("{roomName}", "roomName")
                .replace("${errMsg}", "errMsg");
        noticeBusinessD.setModelCode("模块编码");
        noticeBusinessD.setModelCodeName("模块编码名称");
        noticeBusinessD.setContent(resultMsg);
        TicketContent build = TicketContent.builder()
                .title("收据签章失败")
                .bodyMsg(noticeBusinessD.getContent())
                .urlFlag(true)
                .urlName("点击重新签章")
                .url("/finance/receipt/eSignReceipt/163367366529102")
                .build();
        noticeService.messagesSend(noticeBusinessD, getUserId().get(),"sign", JSONObject.toJSONString(build));
        return true;
    }

}
