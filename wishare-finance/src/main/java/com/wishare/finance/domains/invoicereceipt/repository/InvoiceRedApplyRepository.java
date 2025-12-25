package com.wishare.finance.domains.invoicereceipt.repository;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceRedApplyE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceRedApplyMapper;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedApplyStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceRedApplyRepository extends ServiceImpl<InvoiceRedApplyMapper, InvoiceRedApplyE> {

    private final InvoiceRedApplyMapper invoiceRedApplyMapper;

    public List<InvoiceRedApplyE> getApplyByStatus(List<NuonuoRedApplyStatusEnum> statusEnums, DateTime dateTime) {
        List<String> statusList = statusEnums.stream().map(NuonuoRedApplyStatusEnum::getCode).collect(Collectors.toList());
        return CollectionUtils.isEmpty(statusList) ? new ArrayList<>() : invoiceRedApplyMapper.getApplyByStatus(statusList,dateTime);
    }


}
