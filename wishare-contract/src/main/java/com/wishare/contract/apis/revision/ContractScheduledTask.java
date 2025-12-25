package com.wishare.contract.apis.revision;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.mq.MessageSend;
import com.wishare.contract.apps.remote.clients.AlertFeignClient;
import com.wishare.contract.apps.remote.fo.message.CommandMsgD;
import com.wishare.contract.apps.remote.fo.message.ContractF;
import com.wishare.contract.apps.remote.fo.message.MessageF;
import com.wishare.contract.apps.remote.fo.message.MsgCommandEnum;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.apps.service.revision.relation.ContractRelationBusinessService;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.entity.contractset.ContractBondPlanE;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  19:28
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
@RequiredArgsConstructor
public class ContractScheduledTask {

    private final ContractPayConcludeService contractPayConcludeService;

    private final ContractIncomeConcludeService contractIncomeConcludeService;

    private final ContractRelationRecordService contractRelationRecordService;

    private final ContractRelationBusinessService contractRelationBusinessService;

    private final ContractOrgRelationService orgRelationService;

    private final MessageSend messageSend;

    private final AlertFeignClient alertFeignClient;

//    /**
//     * 每天凌晨一点执行
//     * 支出收入合同更新状态定时方法
//     */
//    @Scheduled(cron = "0 0 1 * * ? ")
//    public void contractPayStatusUpdateTask() {
//
//        Set<String> customerId = new HashSet<>();
//
//        Set<String> supplierId = new HashSet<>();
//
//        log.info("执行支出合同更新状态定时方法开始=================");
//
//        //-- 查询出已经审批通过的合同数据
//        List<ContractPayConcludeE> listPay = contractPayConcludeService.list(new QueryWrapper<ContractPayConcludeE>()
//                .eq(ContractPayConcludeE.REVIEW_STATUS, ReviewStatusEnum.已通过.getCode()));
//        if (CollectionUtils.isNotEmpty(listPay)) {
//            for (ContractPayConcludeE concludeE : listPay) {
//                Boolean isChange = contractPayConcludeService.checkEForUpdateStatus(concludeE.getId());
//                if (isChange) {
//                    supplierId.add(concludeE.getOppositeOneId());
//                    supplierId.add(concludeE.getOppositeTwoId());
//                }
//            }
//        }
//
//        log.info("执行支出合同更新状态定时方法结束=================");
//
//        log.info("执行收入合同更新状态定时方法开始=================");
//
//        //-- 查询出已经审批通过的合同数据
//        List<ContractIncomeConcludeE> listIncome = contractIncomeConcludeService.list(new QueryWrapper<ContractIncomeConcludeE>()
//                .eq(ContractPayConcludeE.REVIEW_STATUS, ReviewStatusEnum.已通过.getCode()));
//        if (CollectionUtils.isNotEmpty(listIncome)) {
//            for (ContractIncomeConcludeE concludeE : listIncome) {
//                Boolean isChange = contractIncomeConcludeService.checkEForUpdateStatus(concludeE.getId());
//                if (isChange) {
//                    customerId.add(concludeE.getOppositeOneId());
//                    customerId.add(concludeE.getOppositeTwoId());
//                }
//            }
//        }
//
//        log.info("执行收入合同更新状态定时方法结束=================");
//
//        for (String id : supplierId) {
//            if (StringUtils.isNotBlank(id)) {
//                orgRelationService.mutualSupplierLasted(id);
//                log.info("合同定时更新关联ORG数据更新 --- 供应商 --- > " + id);
//            }
//        }
//
//        for (String id : customerId) {
//            if (StringUtils.isNotBlank(id)) {
//                orgRelationService.mutualCustomerLasted(id);
//                log.info("合同定时更新关联ORG数据更新 --- 客户 --- > " + id);
//            }
//        }
//
//    }

    /**
     * 每天凌晨一点执行
     * 收入支出id推送
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void contractPayIncomeInfoTask() {
        log.info("收入支出id推送定时方法结束=================");
        List<ContractPayConcludeV> contractPayConcludeVList = contractPayConcludeService.queryContractInfo(null);
        if (CollectionUtils.isNotEmpty(contractPayConcludeVList)) {
            List<String> idList = contractPayConcludeVList.stream().map(s -> s.getId()).collect(Collectors.toList());
            for(String s : idList){
                alertFeignClient.addContractAlertScan(Long.parseLong(s));
            }
        }
    }

}
