package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillJumpE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.repository.mapper.BillJumpMapper;
import com.wishare.finance.domains.bill.repository.mapper.BillRefundMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 跳收Repository
 * @Author zhenghui
 * @Date 2023/7/14
 * @Version 1.0
 */
@Service
public class BillJumpRepository extends ServiceImpl<BillJumpMapper, BillJumpE> {

    public BillJumpE getByApproveId(Long approveId){
        return getOne(new LambdaQueryWrapper<BillJumpE>().eq(BillJumpE::getBillApproveId, approveId));
    }

}
