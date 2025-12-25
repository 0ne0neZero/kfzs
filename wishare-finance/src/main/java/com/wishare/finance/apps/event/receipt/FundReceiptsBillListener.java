package com.wishare.finance.apps.event.receipt;

import com.wishare.finance.apps.pushbill.fo.FundReceiptsBillZJF;
import com.wishare.finance.apps.service.pushbill.BillRuleAppService;
import com.wishare.starter.ThreadPoolManager;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FundReceiptsBillListener implements ApplicationListener<FundReceiptsBillEvent> {

    @Autowired
    private BillRuleAppService billRuleAppService;

    @Override
    public void onApplicationEvent(@NotNull FundReceiptsBillEvent event) {
        //监听处理时间，放入线程池中执行，多个报账单一起生成，每执行完
        ThreadPoolManager.asyncExecuteConsumer(ThreadLocalUtil.curIdentityInfo(), event, this::handleBillEvent);
    }

    /**
     * 生成报账单
     *
     * @param billEvent
     */
    private void handleBillEvent(FundReceiptsBillEvent billEvent) {
        try {
            log.info("开始生成报账单,当前处理的billEvent.ids:{},billEvent.supCpUnitIds:{}", billEvent.getIds(), billEvent.getSupCpUnitIds());
            FundReceiptsBillZJF singleReceiptsBill = new FundReceiptsBillZJF();
            singleReceiptsBill.setIds(billEvent.getIds());
            singleReceiptsBill.setSupCpUnitIds(billEvent.getSupCpUnitIds());
            //原生成报账单逻辑完全不动，直接调用即可
            boolean res = billRuleAppService.executeZjFundReceiptsBill(singleReceiptsBill);
            log.info("报账单生成结果:{}", res);
        } catch (Exception e) {
            log.error("生成报账单失败,当前处理的billEvent.ids:{},billEvent.supCpUnitIds:{},msg:{}",
                    billEvent.getIds(), billEvent.getSupCpUnitIds(), e.getMessage(), e);
        } finally {
            billEvent.getCountDownLatch().countDown();
        }
    }
}
