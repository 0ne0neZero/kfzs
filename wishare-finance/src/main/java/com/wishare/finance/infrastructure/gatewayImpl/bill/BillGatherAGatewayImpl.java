package com.wishare.finance.infrastructure.gatewayImpl.bill;

import com.alibaba.fastjson.JSONObject;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.gateway.bill.BillGatherAGateway;
import com.wishare.finance.domains.gateway.bill.BillGatherDetailAGateway;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author fengxiaolin
 * @date 2023/4/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BillGatherAGatewayImpl<B extends Bill> implements BillGatherAGateway<B> {


    private final GatherBillRepository gatherBillRepository;

    private final BillGatherDetailAGateway<B> billGatherDetailAGateway;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveBillGatherA(BillGatherA<B> billGatherDetailA) {
        if (Objects.isNull(billGatherDetailA) ||
                Objects.isNull(billGatherDetailA.getGatherBill()) ||
                CollectionUtils.isEmpty(billGatherDetailA.getGatherDetailAS())) {
            log.error("收款单保存异常----------------> 相关数据缺失！");
            throw BizException.throw400("收款单保存异常----------------> 相关数据缺失！");
        }
        // 保存收款单表
        GatherBill gatherBill = billGatherDetailA.getGatherBill();
        if (gatherBill.getTotalAmount() > 0) {
            gatherBillRepository.save(gatherBill);
        }
        //收款单日志
        BizLog.normal(String.valueOf(billGatherDetailA.getGatherBill().getId()),
                LogContext.getOperator(), LogObject.账单, LogAction.创建,
                new Content().option(new ContentOption(new PlainTextDataItem("账单结算", true))));
        // 保存收款单明细信息
        List<BillGatherDetailA<B>> gatherDetailAS = billGatherDetailA.getGatherDetailAS();
        for (BillGatherDetailA<B> item : gatherDetailAS) {
            billGatherDetailAGateway.saveBillGatherDetailA(item);
        }

        return gatherBill.getTotalAmount() > 0 ? gatherBill.getId() : null;
    }
}
