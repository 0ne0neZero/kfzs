package com.wishare.contract.apps.service.revision.relation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.enums.revision.ActionTypeEnum;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.RevTypeEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.contract.domains.mapper.revision.relation.ContractRelationRecordMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  15:14
 */
@Service
@Slf4j
public class ContractRelationBusinessService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractRelationRecordService contractRelationRecordService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRelationRecordMapper contractRelationRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionLogService logService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractOrgRelationService contractOrgRelationService;

    /**
     * 新增记录
     * @param type 类型
     * @param newId 新ID
     * @param oldId 旧ID
     * @return
     */
    @Transactional
    public String insertNewOne(Integer type,
                               String newId,
                               String oldId,
                               Integer contractType) {
        if (Objects.isNull(ActionTypeEnum.parseName(type)) || StringUtils.isBlank(newId) || StringUtils.isBlank(oldId)) {
            throw new OwlBizException("执行变更或续签操作时关键信息不可为空");
        }

        Boolean checkIsCanChangeOrRenewal = true;

        String bizName = null;

        if (RevTypeEnum.支出合同.getCode().equals(contractType)) {
            checkIsCanChangeOrRenewal = contractPayConcludeService.checkIsCanChangeOrRenewal(contractPayConcludeService.getById(oldId));
            bizName = contractPayConcludeService.getById(oldId).getName();
        } else if (RevTypeEnum.收入合同.getCode().equals(contractType)) {
            checkIsCanChangeOrRenewal = contractIncomeConcludeService.checkIsCanChangeOrRenewal(contractIncomeConcludeService.getById(oldId));
            bizName = contractIncomeConcludeService.getById(oldId).getName();
        }

        if (!checkIsCanChangeOrRenewal) {
            throw new OwlBizException("该合同存在待提交，审批中，或已拒绝的 变更或续签 流程，不可以执行变更和续签操作.");
        }

        ContractRelationRecordE map = new ContractRelationRecordE();
        map.setTenantId(tenantId())
                .setType(type)
                .setContractType(contractType)
                .setAddId(newId)
                .setOldId(oldId)
                .setIsDone(0);

        contractRelationRecordMapper.insert(map);

        logService.insertOneLog(oldId, bizName, ActionTypeEnum.变更.getCode().equals(type) ? LogActionTypeEnum.变更.getCode() : LogActionTypeEnum.续签.getCode());

        return map.getId();
    }

    /**
     * 变更 or 续签后 处理关联合同
     * @param id 关联记录ID
     */
    @Transactional
    public Boolean checkAfterContractAddAction(String id) {

        ContractRelationRecordE recordE = contractRelationRecordService.getById(id);
        if (Objects.isNull(recordE)) {
            return false;
        }

        if (RevTypeEnum.支出合同.getCode().equals(recordE.getContractType())) {
            ContractPayConcludeE concludeE = contractPayConcludeService.getById(recordE.getAddId());
            ContractPayConcludeE old = contractPayConcludeService.getById(recordE.getOldId());
            if (Objects.isNull(concludeE) || Objects.isNull(old)) {
                return false;
            }

            //-- 新增合同审核状态为 审批中 已拒绝 待提交， 则不做操作
            if (ReviewStatusEnum.审批中.getCode().equals(concludeE.getReviewStatus())
                    || ReviewStatusEnum.已拒绝.getCode().equals(concludeE.getReviewStatus())
                    || ReviewStatusEnum.待提交.getCode().equals(concludeE.getReviewStatus())) {
                return false;
            }

            if (ActionTypeEnum.变更.getCode().equals(recordE.getType())) {
                change(concludeE, old);
            } else if (ActionTypeEnum.续签.getCode().equals(recordE.getType())) {
                renewal(concludeE, old);
            }
        } else if (RevTypeEnum.收入合同.getCode().equals(recordE.getContractType())) {
            ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(recordE.getAddId());
            ContractIncomeConcludeE old = contractIncomeConcludeService.getById(recordE.getOldId());
            if (Objects.isNull(concludeE) || Objects.isNull(old)) {
                return false;
            }

            //-- 新增合同审核状态为 审批中 已拒绝 待提交， 则不做操作
            if (ReviewStatusEnum.审批中.getCode().equals(concludeE.getReviewStatus())
                    || ReviewStatusEnum.已拒绝.getCode().equals(concludeE.getReviewStatus())
                    || ReviewStatusEnum.待提交.getCode().equals(concludeE.getReviewStatus())) {
                return false;
            }

            if (ActionTypeEnum.变更.getCode().equals(recordE.getType())) {
                change(concludeE, old);
            } else if (ActionTypeEnum.续签.getCode().equals(recordE.getType())) {
                renewal(concludeE, old);
            }
        }

        recordE.setIsDone(1);
        contractRelationRecordService.updateById(recordE);

        return true;
    }

    /**
     * 变更-支出合同
     * @param concludeE
     * @param old
     * @return
     */
    public Boolean change(ContractPayConcludeE concludeE, ContractPayConcludeE old) {
        //-- 新合同审批通过
        if (ReviewStatusEnum.已通过.getCode().equals(concludeE.getReviewStatus())) {
            old.setStatus(ContractRevStatusEnum.合同停用.getCode())
                    .setEndDate(LocalDate.now());
            contractPayConcludeService.updateById(old);
            contractOrgRelationService.mutualForPay(old);
        }
        return true;
    }

    /**
     * 变更-收入合同
     * @param concludeE
     * @param old
     * @return
     */
    public Boolean change(ContractIncomeConcludeE concludeE, ContractIncomeConcludeE old) {
        return true;
    }

    /**
     * 续签-支出合同
     * @param concludeE
     * @param old
     * @return
     */
    public Boolean renewal (ContractPayConcludeE concludeE, ContractPayConcludeE old) {
        return true;
    }

    /**
     * 续签-收入合同
     * @param concludeE
     * @param old
     * @return
     */
    public Boolean renewal (ContractIncomeConcludeE concludeE, ContractIncomeConcludeE old) {
        return true;
    }

    /**
     * 提交合同后处理变更 && 续签关联操作
     * @param id
     */
    public void dealActionAfterPost(String id) {
        List<ContractRelationRecordE> list = contractRelationRecordService.list(new QueryWrapper<ContractRelationRecordE>()
                .eq(ContractRelationRecordE.ADD_ID, id)
                .eq(ContractRelationRecordE.IS_DONE, 0));
        if (CollectionUtils.isNotEmpty(list)) {
            for (ContractRelationRecordE recordE : list) {
                // CHECK 事务嵌套可能会导致死锁和阻塞，根据事务传播行为可能会回滚失败
                checkAfterContractAddAction(recordE.getId());
            }
        }
    }


}
