package com.wishare.contract.domains.service.revision.pay;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceSaveF;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementInvoiceAttachmentE;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementInvoiceAttachmentMapper;
import com.wishare.starter.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractSettlementInvoiceAttachmentService extends ServiceImpl<ContractSettlementInvoiceAttachmentMapper, ContractSettlementInvoiceAttachmentE> {

    public void batchSaveAttachments(ContractSettlementInvoiceSaveF contractSettlementInvoiceSaveF) {
        List<ZJFileVo> attachments = contractSettlementInvoiceSaveF.getAttachments();
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }
        //先批量删除
        this.remove(Wrappers.<ContractSettlementInvoiceAttachmentE>lambdaQuery()
                .eq(ContractSettlementInvoiceAttachmentE::getSettlementId, contractSettlementInvoiceSaveF.getSettlementId()));
        //再批量保存
        List<ContractSettlementInvoiceAttachmentE> results = attachments.stream()
                .map(attach -> {
                    ContractSettlementInvoiceAttachmentE res = Global.mapperFacade.map(attach, ContractSettlementInvoiceAttachmentE.class);
                    res.setSettlementId(contractSettlementInvoiceSaveF.getSettlementId());
                    return res;
                }).collect(Collectors.toList());
        this.saveBatch(results);
    }
}
