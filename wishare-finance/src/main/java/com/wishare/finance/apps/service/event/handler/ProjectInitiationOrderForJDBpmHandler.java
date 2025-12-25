package com.wishare.finance.apps.service.event.handler;

import com.alibaba.fastjson.JSONArray;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 立项管理慧采下单审批回调
 */
@Component
@Slf4j
public class ProjectInitiationOrderForJDBpmHandler extends AbustractPushBillHandler{

    @Autowired
    private ContractClient contractClient;

    @Override
    public void handle(ApproveProcessCompleteMsg param) {
        log.info("【BPM】立项管理慧采下单BPM流程回调：{}", JSONArray.toJSON( param));
        if("通过".equals(param.getBusinessStatus())){
            approveAgree(param.getBusinessKey(), param.getProcInstId());
        }else{
            approveRefuse(param.getBusinessKey(), param.getProcInstId());
        }
    }

    @Override
    public boolean support(ApproveProcessCompleteMsg param) {
        return OperateTypeEnum.立项管理慧采下单.getDes().equals(param.getType());
    }

    @Override
    public void approveAgree(Long voucherBillId, String procInstId) {

    }

    @Override
    public void approveRefuse(Long voucherBillId, String procInstId) {

    }

    /**
     * 通过
     * @param id
     * @param procInstId
     */
    @Override
    @Transactional
    public void approveAgree(String id, String procInstId) {
        log.info("【BPM】立项管理慧采下单BPM流程回调：审批通过");
        contractClient.projectInitiationOrderForJDBpmProcess(id,2);
    }

    /**
     * 驳回
     * @param id
     * @param procInstId
     */
    @Override
    @Transactional
    public void approveRefuse(String id, String procInstId) {
        log.info("【BPM】立项管理慧采下单BPM流程回调：审批驳回");
        contractClient.projectInitiationOrderForJDBpmProcess(id,3);
    }


}