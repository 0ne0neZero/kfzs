package com.wishare.finance.apps.service.acl;

import com.wishare.finance.infrastructure.remote.clients.base.MsgClient;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ACL 防腐层
 * @author 强尼
 */
@Service
@Slf4j
public class AclMsgClientService {

    @Setter(onMethod_ = {@Autowired})
    private MsgClient msgClient;


    /**
     * 新增站内信
     * @param noticeBusinessD
     */
    public void add(NoticeBusinessD noticeBusinessD){
        msgClient.add(noticeBusinessD);
    }



}
