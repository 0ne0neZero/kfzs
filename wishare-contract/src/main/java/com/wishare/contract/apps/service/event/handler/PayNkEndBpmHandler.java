package com.wishare.contract.apps.service.event.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.wishare.contract.apps.service.event.model.ApproveProcessCompleteMsg;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.BPMStatusEnum;
import com.wishare.contract.domains.enums.revision.NkStatusEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 【BPM】支出合同结束NK
 * @author hhb
 * @describe
 * @date 2025/10/29 11:41
 */
@Component
@Slf4j
public class PayNkEndBpmHandler extends AbustractPushBillHandler{

    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Override
    public void handle(ApproveProcessCompleteMsg param) {
        log.info("【BPM】支出合同结束NK回调：{}", JSONArray.toJSON( param));
        if("通过".equals(param.getBusinessStatus())){
            approveAgree(param.getBusinessKey(), param.getProcInstId());
        }else{
            approveRefuse(param.getBusinessKey(), param.getProcInstId());
        }
    }

    @Override
    public boolean support(ApproveProcessCompleteMsg param) {
        return "支出合同结束NK".equals(param.getType());
    }

    /**
     * 通过
     * @param id
     * @param procInstId
     */
    @Override
    @Transactional
    public void approveAgree(String id, String procInstId) {
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(mainContract)){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        mainContract.setBpmStatus(BPMStatusEnum.已通过.getCode());
        mainContract.setNkStatus(NkStatusEnum.已关闭.getCode());
        mainContract.setBpmApprovalDate(LocalDateTime.now());
        contractPayConcludeMapper.updateById(mainContract);

        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(id);
        if(ObjectUtil.isNotNull(nkContract)){
            contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.已关闭.getCode(),BPMStatusEnum.已通过.getCode(),null,LocalDateTime.now());
        }
    }

    /**
     * 驳回
     * @param id
     * @param procInstId
     */
    @Override
    @Transactional
    public void approveRefuse(String id, String procInstId) {
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(mainContract)){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        mainContract.setBpmStatus(BPMStatusEnum.已驳回.getCode());
        mainContract.setNkStatus(NkStatusEnum.已开启.getCode());
        contractPayConcludeMapper.updateById(mainContract);
        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(id);
        if(ObjectUtil.isNotNull(nkContract)){
            contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.已开启.getCode(),BPMStatusEnum.已驳回.getCode(),null,null);
        }
    }


}
