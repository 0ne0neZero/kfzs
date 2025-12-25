package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.BatchAddBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.BatchDelBillInferenceF;
import com.wishare.finance.apps.service.bill.factory.BillAppServiceFactory;
import com.wishare.finance.domains.bill.command.BatchAddBillInferenceCommand;
import com.wishare.finance.domains.bill.command.BatchDelBillInferenceCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.bill.service.BillAdjustDomainService;
import com.wishare.finance.domains.bill.service.BillInferenceDomainService;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.bill.service.PayBillDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/26 9:35
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillInferenceAppService {

    private final BillInferenceDomainService billInferenceDomainService;

    private final PayBillDomainService payBillDomainService;

    private final GatherBillDomainService gatherBillDomainService;

    private final BillAdjustDomainService billAdjustDomainService;

    private final BillAppServiceFactory billAppServiceFactory;

    /**
     * 批量推凭账单记录
     * @param form
     * @return
     */
    public List<Long> batchInsertInference(BatchAddBillInferenceF form) {
        dealBillInferenceState(form.getConcatIds(), form.getEventType(), 1);
        if (BillTypeEnum.收款单.getCode() != form.getBillType() && BillTypeEnum.付款单.getCode() != form.getBillType()) {
            billAppServiceFactory.getBillServiceByBillType(form.getBillType())
                .inferBatch(form.getBillIds());
        }
        return billInferenceDomainService.batchInsertInference(Global.mapperFacade.map(form, BatchAddBillInferenceCommand.class));
    }

    public void dealBillInferenceState(List<Long> concatIds, int eventType, int state) {
        if (EventTypeEnum.付款结算.getEvent() == eventType) {
            payBillDomainService.batchUpdateDetailInferenceSate(concatIds, state);
        } else if (EventTypeEnum.收款结算.getEvent() == eventType) {
            gatherBillDomainService.batchUpdateDetailInferenceSate(concatIds, state);
        } else if (EventTypeEnum.账单调整.getEvent() == eventType) {
            billAdjustDomainService.batchUpdateInferenceSate(concatIds, state);
        }
    }

    public boolean batchDeleteInference(BatchDelBillInferenceF form) {
        dealBillInferenceState(form.getConcatIds(), form.getEventType(), 0);
        return billInferenceDomainService.batchDeleteInference(Global.mapperFacade.map(form, BatchDelBillInferenceCommand.class));
    }
}
