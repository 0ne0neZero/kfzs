package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.domains.invoicereceipt.entity.base.FinanceBaseEntity;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptSendLogE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptSendLogMapper;
import com.wishare.finance.infrastructure.identifier.TenantInfo;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReceiptSendLogRepository extends ServiceImpl<ReceiptSendLogMapper, ReceiptSendLogE> {

    /**
     * 当前的处理方式因为可能存在非用户操作（定时任务）获取不到部分信息 租户等
     * @return
     */
    public int insert(ReceiptVDto receiptVDto,Integer mode,Integer sendStatus,String msg){
        ReceiptSendLogE build = ReceiptSendLogE.builder()
                .invoiceReceiptId(receiptVDto.getInvoiceReceiptId())
                .pushModes(List.of(mode))
                .sendResult(sendStatus)
                .message(msg)
                .build();
        //尝试获取租户id 获取不到则定时任务等触发
        if (ObjectUtils.isEmpty(ThreadLocalUtil.curIdentityInfo())) {
            build.setTenantId(receiptVDto.getTenantId());
            build.setCreator(receiptVDto.getCreator());
            build.setCreatorName(receiptVDto.getCreatorName());
            build.setOperator(receiptVDto.getOperator());
            build.setOperatorName(receiptVDto.getOperatorName());
            return super.getBaseMapper().insert(build);
        }
        return super.getBaseMapper().insert(build);
    }
}
