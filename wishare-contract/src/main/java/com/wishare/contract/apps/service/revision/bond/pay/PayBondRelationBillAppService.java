package com.wishare.contract.apps.service.revision.bond.pay;

import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillPageF;
import com.wishare.contract.domains.service.revision.bond.pay.PayBondRelationBillService;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillV;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillListF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillUpdateF;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 缴纳保证金改版关联单据明细表
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Service
@Slf4j
public class PayBondRelationBillAppService {

    @Setter(onMethod_ = {@Autowired})
    private PayBondRelationBillService payBondRelationBillService;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    public PayBondRelationBillV get(PayBondRelationBillF payBondRelationBillF){
        return payBondRelationBillService.get(payBondRelationBillF).orElse(null);
    }

    public PayBondRelationBillListV list(PayBondRelationBillListF payBondRelationBillListF){
        return payBondRelationBillService.list(payBondRelationBillListF);
    }

    public String save(PayBondRelationBillSaveF payBondRelationBillF){
        return payBondRelationBillService.save(payBondRelationBillF);
    }

    public void update(PayBondRelationBillUpdateF payBondRelationBillF){
        payBondRelationBillService.update(payBondRelationBillF);
    }

    public boolean removeById(String id){
        return payBondRelationBillService.removeById(id);
    }

    public PageV<PayBondRelationBillV> page(PageF<PayBondRelationBillPageF> request) {
        return payBondRelationBillService.page(request);
    }

    public PageV<PayBondRelationBillV> frontPage(PageF<SearchF<PayBondRelationBillE>> request) {
        PageV<PayBondRelationBillV> pageV = payBondRelationBillService.frontPage(request);
        for (PayBondRelationBillV record : pageV.getRecords()) {
            Optional.ofNullable(attachmentService.listOneById(record.getId())).ifPresent(v -> {
                record.setFilesRecord(attachmentService.listOneById(record.getId()))
                        .setFileKey(v.getFileKey())
                        .setFileName(v.getName());
            });
        }
        return pageV;
    }
}
