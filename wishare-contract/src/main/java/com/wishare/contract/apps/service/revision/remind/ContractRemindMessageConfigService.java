package com.wishare.contract.apps.service.revision.remind;

import com.wishare.contract.apps.fo.remind.ContractRemindMessageConfigF;
import com.wishare.contract.apps.remote.fo.message.FirstExamineMessageF;
import com.wishare.contract.domains.vo.revision.remind.ContractRemindMessageConfigV;

public interface ContractRemindMessageConfigService {

    /**
     * 保存提醒与预警消息配置
     *
     * @param param
     */
    void save(ContractRemindMessageConfigF param);

    /**
     * 查询当前生效的消息配置
     *
     * @return
     */
    ContractRemindMessageConfigV detail();

    /**
     * 消息推送
     */
    void send();

    void send(FirstExamineMessageF message, Boolean flag);
}
