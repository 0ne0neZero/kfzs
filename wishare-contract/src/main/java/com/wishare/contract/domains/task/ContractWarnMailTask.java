package com.wishare.contract.domains.task;

import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.service.contractset.ContractCollectionPlanService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanV;
import com.wishare.contract.domains.vo.contractset.ContractDetailsV;
import com.wishare.contract.infrastructure.utils.MsgFacade;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author :wangrui
 * @Date: 2022/12/05
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
public class ContractWarnMailTask implements ApplicationListener<ApplicationReadyEvent> {

    private ContractConcludeService contractConcludeService;

    private ContractCollectionPlanService contractCollectionPlanService;
    private MsgFacade msgFacade;
    private UserFeignClient userFeignClient;
    private static final String TITLE = "合同系统通知";

    /**
     * 每天10点00分执行 推动邮箱提醒到期的合同
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void contractExpireDate() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("推送到期的合同");
        List<ContractDetailsV> contractList1 = contractConcludeService.expireContract(null, null, null);
        if (!CollectionUtils.isEmpty(contractList1)) {
            for (ContractDetailsV concludeInfoV : contractList1) {
                if (StringUtils.hasText(concludeInfoV.getCreator())) {
                    List<String> fields = new ArrayList<>();
                    fields.add("email");
                    UserInfoRv userInfoRv = userFeignClient.getUserInfoById(concludeInfoV.getCreator(),fields,"saas");
                    if (userInfoRv != null && StringUtils.hasText(userInfoRv.getEmail())) {
                        msgFacade.sendEmail(userInfoRv.getEmail(), TITLE,
                                "【合同名：" + concludeInfoV.getName() + "】已于【" + concludeInfoV.getGmtExpireEnd().toString() + "】到期，请及时处理！本邮件为合同系统自动发出", null);
                    }
                }
            }
            log.info("推送合同到期邮箱=================" + contractList1.size());
        }
        stopWatch.stop();
        log.info("推送合同到期邮箱任务结束=================\n{}", stopWatch.prettyPrint());
    }

    /**
     * 每天10点00分执行，发送合同临期预警邮箱提醒
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void contractAdventWarnState() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("临期的合同");
        contractAdvent(90);
        contractAdvent(30);
        contractAdvent(15);
        stopWatch.stop();
        log.info("执行变更合同已到期状态任务结束=================\n{}", stopWatch.prettyPrint());
    }

    public void contractAdvent(Integer dayNum) {
        List<ContractDetailsV> contractList = contractConcludeService.contractAdvent(null, null, false, dayNum);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractDetailsV concludeInfoV : contractList) {
                if (StringUtils.hasText(concludeInfoV.getCreator())) {
                    List<String> fields = new ArrayList<>();
                    fields.add("email");
                    UserInfoRv userInfoRv = userFeignClient.getUserInfoById(concludeInfoV.getCreator(),fields,"saas");
                    if (userInfoRv != null && StringUtils.hasText(userInfoRv.getEmail())) {
                        msgFacade.sendEmail(userInfoRv.getEmail(), TITLE,
                                "距离【合同名：" + concludeInfoV.getName() + "】到期日期仅剩【" + dayNum.toString() + "】天，请及时处理！本邮件为合同系统自动发出", null);
                    }
                }
            }
            log.info("推送" + dayNum + "天" + "推送合同临期邮箱任务结束=================" + contractList.size());
        }
    }

    /**
     * 每天10点00分执行，收款计划临期提醒
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void collectPlantWarnState() {
        log.info("执行变更收款账单临期状态任务开始=================");
        collectionPlanAdvent(ContractSetConst.INCOME, 30);
    }

    /**
     * 每天10点00分执行，付款计划临期提醒
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void payPlantWarnState() {
        log.info("执行变更付款账单临期状态任务开始=================");
        collectionPlanAdvent(ContractSetConst.PAY, 30);
    }

    public void collectionPlanAdvent(Integer contractSet, Integer dayNum) {
        List<ContractCollectionPlanV> contractList = contractCollectionPlanService.collectionAdvent(
                null, null, contractSet, null, false, dayNum);
        if (!CollectionUtils.isEmpty(contractList)) {
            for (ContractCollectionPlanV concludeInfoV : contractList) {
                if (StringUtils.hasText(concludeInfoV.getCreator())) {
                    List<String> fields = new ArrayList<>();
                    fields.add("email");
                    UserInfoRv userInfoRv = userFeignClient.getUserInfoById(concludeInfoV.getCreator(),fields,"saas");
                    if (userInfoRv != null && StringUtils.hasText(userInfoRv.getEmail())) {
                        msgFacade.sendEmail(userInfoRv.getEmail(), TITLE,
                                "距离【" + concludeInfoV.getContractName() + "】计划收款日期仅剩【" + "30" + "】天，请及时处理！本邮件为合同系统自动发出", null);
                    }
                }
            }
            log.info("推送" + dayNum + "天" + "临期收付款账单邮箱任务结束=================" + contractList.size());
        }
    }

    /**
     * 每天10点00分执行，收款计划到期提醒
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void collectPlantState() {
        log.info("执行变更收款账单到期状态任务开始=================");
        List<ContractCollectionPlanV> contractList1 = contractCollectionPlanService.collectionExpire(
                null, null, ContractSetConst.INCOME, null, null);
        if (!CollectionUtils.isEmpty(contractList1)) {
            for (ContractCollectionPlanV concludeInfoV : contractList1) {
                if (StringUtils.hasText(concludeInfoV.getCreator())) {
                    List<String> fields = new ArrayList<>();
                    fields.add("email");
                    UserInfoRv userInfoRv = userFeignClient.getUserInfoById(concludeInfoV.getCreator(),fields,"saas");
                    if (userInfoRv != null && StringUtils.hasText(userInfoRv.getEmail())) {
                        msgFacade.sendEmail(userInfoRv.getEmail(), TITLE,
                                "【" + concludeInfoV.getContractName() + "】收款已逾期，请及时处理！本邮件为合同系统自动发出", null);
                    }
                }
            }
            log.info("推送到期账单邮箱任务结束=================", contractList1.size());
        }
    }

    /**
     * 每天10点00分执行，付款计划到期提醒
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void payPlantState() {
        log.info("执行变更付款账单到期状态任务开始=================");
        List<ContractCollectionPlanV> contractList1 = contractCollectionPlanService.collectionExpire(
                null, null, ContractSetConst.PAY, null, null);
        if (!CollectionUtils.isEmpty(contractList1)) {
            log.info("推送到期付款账单邮箱开始=================");
            for (ContractCollectionPlanV concludeInfoV : contractList1) {
                if (StringUtils.hasText(concludeInfoV.getCreator())) {
                    List<String> fields = new ArrayList<>();
                    fields.add("email");
                    UserInfoRv userInfoRv = userFeignClient.getUserInfoById(concludeInfoV.getCreator(),fields,"saas");
                    if (userInfoRv != null && StringUtils.hasText(userInfoRv.getEmail())) {
                        msgFacade.sendEmail(userInfoRv.getEmail(), TITLE,
                                "【" + concludeInfoV.getContractName() + "】付款已逾期，请及时处理！本邮件为合同系统自动发出", null);
                    }
                }
            }
            log.info("推送到期付款账单邮箱任务结束=================", contractList1.size());
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
//        this.contractExpireDate();
//        this.payPlantState();
//        this.collectPlantState();
//        this.payPlantWarnState();
//        this.collectPlantWarnState();
//        this.contractAdventWarnState();
    }
}
