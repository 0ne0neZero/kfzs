package com.wishare.contract.apps.service.revision.invoice;

import com.wishare.contract.apps.fo.revision.invoice.ContractInvoiceF;
import com.wishare.contract.domains.service.revision.invoice.ContractInvoiceService;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/1/20:18
 */
@Service
@Slf4j
public class ContractInvoiceAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceService contractInvoiceService;


    public boolean removeById(String id){
        return contractInvoiceService.removeById(id);
    }


    public List<ContractPayBillV> getDetailsByIdSettle(PageF<SearchF<ContractInvoiceF>> contractInvoiceF){
        String id = contractInvoiceF.getConditions().getFields().stream().filter(field -> "id".equals(field.getName())).findFirst().map(field -> (String) field.getValue()).orElse("");
        return contractInvoiceService.getDetailsByIdSettle(id);
    }
}
