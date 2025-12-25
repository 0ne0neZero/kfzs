package com.wishare.finance.apps.scheduler.bill;

import com.wishare.finance.apps.service.bill.HandAccountAppService;
import com.wishare.finance.domains.bill.command.BatchRefreshAccountHandCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.BillAccountHand;
import com.wishare.finance.domains.bill.repository.BillAccountHandRepository;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RefreshAccountHandScheduler {
    private final HandAccountAppService handAccountAppService;

    private final BillAccountHandRepository billAccountHandRepository;
    @XxlJob("refreshAccountHandHandler")
    public void refreshAccountHandHandler(){
        log.info("更新交账账单信息定时任务开始");
        //获取所有项目
        List<String> communityIdList = billAccountHandRepository.queryAllCommunityIdList();
        communityIdList.forEach(v -> {
            // 查询已开票 但是交账管理中 没有发票信息的数据
            List<BillAccountHand> billAccountHands = billAccountHandRepository.queryNoInvoiceInfoList(v);
            for (BillAccountHand billAccountHand : billAccountHands) {
                IdentityInfo identityInfo = new IdentityInfo();
                identityInfo.setTenantId(billAccountHand.getTenantId());
                ThreadLocalUtil.set("IdentityInfo", identityInfo);
                BatchRefreshAccountHandCommand command = new BatchRefreshAccountHandCommand();
                ArrayList<Long> list = new ArrayList<>();
                list.add(billAccountHand.getBillId());
                command.setBillIds(list);
                command.setBillType(BillTypeEnum.valueOfByCode(billAccountHand.getBillType()));
                command.setSupCpUnitId(v);
                handAccountAppService.batchRefreshOrAddAccountHand(command);
            }
        });

        log.info("更新交账账单信息定时任务执行结束");
    }
}
