package com.wishare.finance.apps.scheduler.flow;

import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.service.FlowDetailDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 流水
 * @author: pgq
 * @since: 2022/12/24 11:20
 * @version: 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FlowScheduler {

    private final ExternalClient externalClient;

    private final FlowDetailDomainService flowDetailDomainService;

    @XxlJob(value = "flowSchedulerHandle")
    public void flowSchedulerHandle() {
        LocalDateTime yesdate = LocalDateTime.now().plusDays(-1L);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = df.format(yesdate);
        JSONObject flowDatas = externalClient.getFlowDatas(format, format);
        if (flowDatas.has("CBSERPPGK") && !flowDatas.isNull("EREXPTRSZ")) {
            JSONArray array = flowDatas.getJSONObject("CBSERPPGK").getJSONArray("EREXPTRSZ");
            List<FlowDetailE> list = new ArrayList<>();
            if (!array.isEmpty()) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject flow = array.getJSONObject(i);

                    FlowDetailE flowDetailE = new FlowDetailE();
                    flowDetailE.setSerialNumber(flow.getString("DTLSEQ"));
                    flowDetailE.setSettleAmount(flow.getLong("ACTBAL"));
                    flowDetailE.setPayTime(yesdate);
                    flowDetailE.setOppositeAccount(flow.getString("OTHACT"));
                    flowDetailE.setOppositeName(flow.getString("OTHNAM"));
                    flowDetailE.setOppositeBank(flow.getString("OTHOPN"));
                    flowDetailE.setOurAccount(flow.getString("ACTNBR"));
                    flowDetailE.setOurName(flow.getString("ACTNAM"));
                    flowDetailE.setOurBank(flow.getString("BNKNBR"));
                    flowDetailE.setSummary(flow.getString("TXTDSM"));
                    flowDetailE.setFundPurpose(flow.getString(""));
                    flowDetailE.setTradingPlatform(flow.getString("MONTYP"));
                    flowDetailE.setTransactionMode(flow.getString("PAYCTG"));
                    flowDetailE.setClaimStatus(0);
                    flowDetailE.setType("C".equals(flow.getString("ITMDIR")) ? 2 : 1);
                    flowDetailE.setSyncData(1);
                    JSONObject jsonObject = externalClient.downReceipt(flow.getString("ACTNBR"),
                        flow.getString("ISTNBR"), flow.getString("DTLSEQ"));
                    JSONObject info = jsonObject.getJSONObject("CBSERPPGK").getJSONObject("INFO");
                    if (info.has("RETCOD") && "0000000".equals(info.getString("RETCOD"))) {
                        JSONArray jsonArray = info.getJSONArray("DCPRTRCPZ");
                        flowDetailE.setReceiptUrl(jsonArray.getJSONObject(0).getString("IMPATH"));
                    }

                    list.add(flowDetailE);
                }
            }
            if (!list.isEmpty()) {
                flowDetailDomainService.batchInsert(list);
            }
        }
    }

}
