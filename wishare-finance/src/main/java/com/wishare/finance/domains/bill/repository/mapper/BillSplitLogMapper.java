package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.GatherBillDto;
import com.wishare.finance.domains.bill.dto.GatherDto;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.dto.UnInvoiceGatherBillDto;
import com.wishare.finance.domains.bill.entity.BillSplitLog;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Mapper
public interface BillSplitLogMapper extends BaseMapper<BillSplitLog> {

}
