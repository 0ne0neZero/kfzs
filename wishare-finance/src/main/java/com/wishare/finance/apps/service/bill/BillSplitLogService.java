package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.domains.bill.entity.BillSplitLog;
import com.wishare.finance.domains.bill.repository.BillSplitLogRepository;
import com.wishare.finance.domains.bill.service.BillSettleDomainService;
import com.wishare.finance.domains.conts.CommonConst;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 结算应用服务
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillSplitLogService {

    private final BillSplitLogRepository billSplitLogRepository;


    public void saveBatch(Set<String> errorOriginalBillNos) {
        if (CollectionUtils.isEmpty(errorOriginalBillNos)) {
            return;
        }
        List<BillSplitLog> billSplitLogs = new ArrayList<>();
        errorOriginalBillNos.forEach(originalBillNo -> {
            BillSplitLog billSplitLog = new BillSplitLog();
            billSplitLog.setId(IdentifierFactory.getInstance().generateLongIdentifier("bill_split_log"));
            billSplitLog.setBillNo(originalBillNo);
            billSplitLog.setState(2);
            billSplitLog.setReason(CommonConst.BILL_SPLIT_LOG_REASON);
            billSplitLogs.add(billSplitLog);
        });
        billSplitLogRepository.saveBatch(billSplitLogs);
    }
}
