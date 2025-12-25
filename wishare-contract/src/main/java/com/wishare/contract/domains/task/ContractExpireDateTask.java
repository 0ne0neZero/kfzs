package com.wishare.contract.domains.task;

import com.wishare.contract.apps.mq.MessageSend;
import com.wishare.contract.apps.remote.fo.message.CommandMsgD;
import com.wishare.contract.apps.remote.fo.message.MessageF;
import com.wishare.contract.apps.remote.fo.message.MsgCommandEnum;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.service.contractset.ContractCollectionPlanService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanV;
import com.wishare.contract.domains.vo.contractset.ContractDetailsV;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author :wangrui
 * @Date: 2022/9/21
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
public class ContractExpireDateTask implements ApplicationListener<ApplicationReadyEvent> {

    private ContractConcludeService contractConcludeService;

    private ContractCollectionPlanService contractCollectionPlanService;
    private MessageSend messageSend;

    /**
     * 每天23点59分执行，关闭今天到期的合同
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void contractExpireDate() {
        List<ContractDetailsV> contractList1 = contractConcludeService.expireContract(null, null, false);
        if (!CollectionUtils.isEmpty(contractList1)) {
            for (ContractDetailsV concludeInfoV : contractList1) {
                contractConcludeService.updateContractState(concludeInfoV.getId());
                sendMessage(11615622288904L, "合同到期", concludeInfoV.getTenantId(),
                        concludeInfoV.getName(), concludeInfoV.getGmtExpireEnd().toString());
            }
            log.info("推送合同到期站内信=================" + contractList1.size());
        }
    }

    /**
     * 每天00点01分执行，更改合同临期预警状态和提醒
     */
    @Scheduled(cron = "0 01 00 * * ?")
    public void contractAdventWarnState() {
        List<ContractDetailsV> contractList = contractConcludeService.contractAdvent(null, null, true, 90);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractDetailsV concludeInfoV : contractList) {
                contractConcludeService.updateWarnState(concludeInfoV.getId(), 1);
            }
            log.info("执行变更合同已临期状态定时任务=================" + contractList.size());
        }
        contractAdvent(90);
        contractAdvent(30);
        contractAdvent(15);
    }

    public void contractAdvent(Integer dayNum) {
        List<ContractDetailsV> contractList = contractConcludeService.contractAdvent(null, null, false, dayNum);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractDetailsV concludeInfoV : contractList) {
                sendMessage(11615542300701L, "合同临期", concludeInfoV.getTenantId(), concludeInfoV.getName(), dayNum.toString());
            }
            log.info("推送" + dayNum + "天" + "推送15天合同临期站内信任务结束=================" + contractList.size());
        }
    }

    /**
     * 每天00点01分执行，更改收款计划临期状态和提醒
     */
    @Scheduled(cron = "0 01 00 * * ?")
    public void collectPlantWarnState() {
        log.info("执行变更收款账单临期状态任务开始=================");
        //取出正常状态租户的到期时间
        List<ContractCollectionPlanV> contractList = contractCollectionPlanService.collectionAdvent(
                null, null, ContractSetConst.INCOME, null, true, 30);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractCollectionPlanV concludeInfoV : contractList) {
                contractCollectionPlanService.updateWarnState(concludeInfoV.getId(), 1);
            }
            log.info("执行变更收款账单临期状态任务=================" + contractList.size());
        }
        collectionPlanAdvent(ContractSetConst.INCOME, 30);
    }

    /**
     * 每天00点01分执行，更改付款计划临期状态和提醒
     */
    @Scheduled(cron = "0 01 00 * * ?")
    public void payPlantWarnState() {
        log.info("执行变更付款账单临期状态任务开始=================");
        //取出正常状态租户的到期时间
        List<ContractCollectionPlanV> contractList = contractCollectionPlanService.collectionAdvent(
                null, null, ContractSetConst.PAY, null, true, 30);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractCollectionPlanV concludeInfoV : contractList) {
                contractCollectionPlanService.updateWarnState(concludeInfoV.getId(), 1);
            }
            log.info("执行变更付款账单临期状态任务=================" + contractList.size());
        }
        collectionPlanAdvent(ContractSetConst.PAY, 30);
    }

    public void collectionPlanAdvent(Integer contractSet, Integer dayNum) {
        List<ContractCollectionPlanV> contractList = contractCollectionPlanService.collectionAdvent(
                null, null, contractSet, null, false, dayNum);
        String title = null;
        if (contractSet.equals(ContractSetConst.PAY)) {
            title = "应付临期";
        } else {
            title = "应收临期";
        }
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractCollectionPlanV concludeInfoV : contractList) {
                sendMessage(11615618991203L, title, concludeInfoV.getTenantId(), concludeInfoV.getContractName(), "30");
            }
            log.info("推送" + dayNum + "天" + "临期收付款账单站内信任务结束=================" + contractList.size());
        }
    }

    /**
     * 每天23点59分执行，更改收款计划到期状态和提醒
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void collectPlantState() {
        log.info("执行变更收款账单到期状态任务开始=================");
        List<ContractCollectionPlanV> contractList1 = contractCollectionPlanService.collectionExpire(
                null, null, ContractSetConst.INCOME, null, false);
        if (!CollectionUtils.isEmpty(contractList1)) {
            log.info("推送到期账单站内信开始=================");
            for (ContractCollectionPlanV concludeInfoV : contractList1) {
                contractCollectionPlanService.updateWarnState(concludeInfoV.getId(), 2);
                sendMessage(11615628661805L, "应收逾期", concludeInfoV.getTenantId(), concludeInfoV.getContractName(), null);
            }
            log.info("推送到期账单站内信任务结束=================", contractList1.size());
        }
    }

    /**
     * 每天23点59分执行，更改付款计划到期状态和提醒
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void payPlantState() {
        log.info("执行变更付款账单到期状态任务开始=================");
        List<ContractCollectionPlanV> contractList1 = contractCollectionPlanService.collectionExpire(
                null, null, ContractSetConst.PAY, null, false);
        if (!CollectionUtils.isEmpty(contractList1)) {
            log.info("推送到期付款账单站内信开始=================");
            for (ContractCollectionPlanV concludeInfoV : contractList1) {
                contractCollectionPlanService.updateWarnState(concludeInfoV.getId(), 2);
                sendMessage(11615638187506L, "应付逾期", concludeInfoV.getTenantId(), concludeInfoV.getContractName(), null);
            }
            log.info("推送到期付款账单站内信任务结束=================", contractList1.size());
        }
    }

    public void sendMessage(Long templateId, String title, String tenantId, String name, String date) {
        MessageF messageF = new MessageF();
        String[] s = new String[]{};
        if(StringUtils.hasText(date)){
            s = new String[]{name, date};
        }else{
            s = new String[]{name};
        }
        messageF.setParams(s)
                .setTypeId("1")
                .setTitle(title)
                .setTemplateId(templateId)
                .setTenantId(tenantId);
        CommandMsgD<MessageF> commandMsgD = new CommandMsgD<>();
        commandMsgD.setCommandType(MsgCommandEnum.SEND_MESSAGE)
                .setData(messageF);
        messageSend.sendMessage(commandMsgD);
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
//        this.contractExpireDate();
//        this.payPlantState();
//        this.collectPlantState();
//        this.payPlantWarnState();
//        this.collectPlantWarnState();
//        this.contractAdventWarnState();
//        this.contractExpireWarnState();
    }
}
