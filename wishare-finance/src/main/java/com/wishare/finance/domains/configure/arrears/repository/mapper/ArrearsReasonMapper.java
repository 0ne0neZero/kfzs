package com.wishare.finance.domains.configure.arrears.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.domains.bill.dto.ReasonBillTotalDto;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsReasonE;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArrearsReasonMapper extends BaseMapper<ArrearsReasonE> {
    IPage<ArrearsReasonBillV> queryPageBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    ReasonBillTotalDto batchReasonBillTotal(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    List<ArrearsReasonE> queryNewArrearsReason(@Param("billIds") List<Long> billIds, String tenantId);

    Integer queryPageBillCount(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);
}
