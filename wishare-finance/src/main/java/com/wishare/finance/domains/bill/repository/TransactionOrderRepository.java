package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.bill.repository.mapper.TransactionOrderMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 交易订单资源库
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Service
public class TransactionOrderRepository extends ServiceImpl<TransactionOrderMapper, TransactionOrder> {

    public TransactionOrder getByBizTransactionNo(String bizTransactionNo) {
        return Objects.isNull(bizTransactionNo) ? null : getOne(new LambdaQueryWrapper<TransactionOrder>()
                .eq(TransactionOrder::getBizTransactionNo, bizTransactionNo).orderByDesc(TransactionOrder::getGmtCreate), false);
    }

    public List<TransactionOrder> getByBizTransactionNos(List<String> bizTransactionNos) {
        return CollectionUtils.isEmpty(bizTransactionNos) ? new ArrayList<>() : list(new LambdaQueryWrapper<TransactionOrder>()
                .in(TransactionOrder::getBizTransactionNo, bizTransactionNos).orderByDesc(TransactionOrder::getGmtCreate));
    }

    public List<TransactionOrder> getListByBizTransactionNo(String bizTransactionNo) {
        return Objects.isNull(bizTransactionNo) ? null : list(new LambdaQueryWrapper<TransactionOrder>()
                .eq(TransactionOrder::getBizTransactionNo, bizTransactionNo).orderByDesc(TransactionOrder::getGmtCreate));
    }

    public TransactionOrder getByTransactionNo(String transactionNo) {
        return Objects.isNull(transactionNo) ? null : getOne(new LambdaQueryWrapper<TransactionOrder>()
                .eq(TransactionOrder::getTransactionNo, transactionNo).orderByDesc(TransactionOrder::getGmtCreate), false);
    }

    public TransactionOrder getByBizTransactionNo(String bizTransactionNo, List<Integer> transactionStates) {
        return Objects.isNull(bizTransactionNo) ? null : getOne(new LambdaQueryWrapper<TransactionOrder>()
                .eq(TransactionOrder::getBizTransactionNo, bizTransactionNo).in(TransactionOrder::getTransactState, transactionStates));
    }

    public TransactionOrder getByVoucherId(String voucherId) {
        return baseMapper.getByVoucherId(voucherId);
    }

}
