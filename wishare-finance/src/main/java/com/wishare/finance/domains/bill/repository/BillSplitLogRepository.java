package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInferStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.GatherBillDto;
import com.wishare.finance.domains.bill.dto.GatherDto;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.dto.UnInvoiceGatherBillDto;
import com.wishare.finance.domains.bill.entity.BillSplitLog;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.repository.mapper.BillSplitLogMapper;
import com.wishare.finance.domains.bill.repository.mapper.GatherBillMapper;
import com.wishare.finance.domains.bill.repository.mapper.GatherBillOriginProxyMapper;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 收款单资源库
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillSplitLogRepository extends ServiceImpl<BillSplitLogMapper, BillSplitLog> {

}


