package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.aggregate.BillHandA;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillHandE;
import com.wishare.finance.domains.bill.repository.mapper.BillHandMapper;
import com.wishare.starter.Global;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 交账
 * @author: pgq
 * @since: 2022/10/9 14:39
 * @version: 1.0.0
 */
@Service
public class BillHandRepository<BR extends BillRepository>  extends ServiceImpl<BillHandMapper, BillHandE> {

    public <B extends Bill> boolean handBatch(List<BillHandA<B>> billHandAS) {

        return this.saveBatch(Global.mapperFacade.mapAsList(billHandAS, BillHandE.class));
    }

    public <B extends Bill> Boolean handReversal(BillHandA<B> billHandA) {
        return this.save(Global.mapperFacade.map(billHandA, BillHandE.class));
    }
}
