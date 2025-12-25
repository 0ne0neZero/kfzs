package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.entity.BillPrepayInfoE;
import com.wishare.finance.domains.bill.repository.mapper.BillPrepayInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoRepository
 * @date 2023.11.08  10:21
 * @description:账单预支付信息 repository
 */
@Service
public class BillPrepayInfoRepository extends ServiceImpl<BillPrepayInfoMapper, BillPrepayInfoE> {
}
