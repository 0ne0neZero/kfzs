package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.BillApplyBatchRf;
import com.wishare.contract.apps.remote.fo.BillApplyRf;
import com.wishare.contract.apps.remote.fo.StatisticsBillTotalRf;
import com.wishare.contract.apps.remote.vo.BillPageInfoRv;
import com.wishare.contract.apps.remote.vo.BillTotalRv;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractDetailsV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FinanceBillAppService {

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;

    public PageV<BillPageInfoRv> getBillPage(PageF<SearchF<?>> queryF) {
        SearchF<?> conditions = queryF.getConditions();
        Field appId = fields("b.app_id", 1, "85e822bdf5b54d27a8c49ed1d5ec234e");
        Field supCpUnitId = fields("b.sup_cp_unit_id", 1, "default");
        conditions.getFields().add(appId);
        conditions.getFields().add(supCpUnitId);
        PageV<BillPageInfoRv> billPage = financeFeignClient.getBillPage(queryF);
        billPage.getRecords().forEach(item -> {
            if (Strings.isNotBlank(item.getOutBusId())) {
                Long contractId = Long.valueOf(item.getOutBusId());
                item.setContractId(contractId);
                ContractDetailsV contractDetailsV = contractConcludeService.selectById(contractId);
                if (Objects.nonNull(contractDetailsV)) {
                    item.setContractName(contractDetailsV.getName());
                }
            }
        });
        return billPage;
    }

    public Boolean billApply(BillApplyRf from) {
        return financeFeignClient.billApply(from);
    }

    public Boolean billApplyBatch(BillApplyBatchRf from) {
        return financeFeignClient.billApplyBatch(from);
    }

    public BillTotalRv statisticsBills(StatisticsBillTotalRf from) {
        SearchF<?> conditions = from.getQuery().getConditions();
        Field appId = fields("b.app_id", 1, "85e822bdf5b54d27a8c49ed1d5ec234e");
        conditions.getFields().add(appId);
        return financeFeignClient.statisticsBills(from);
    }

    public Field fields(String name, int method, Object value) {
        Field field = new Field();
        field.setName(name);
        field.setMethod(method);
        field.setValue(value);
        return field;
    }
}
