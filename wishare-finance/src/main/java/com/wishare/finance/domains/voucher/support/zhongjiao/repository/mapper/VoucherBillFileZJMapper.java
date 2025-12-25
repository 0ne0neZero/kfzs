package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherBillFileZJMapper extends BaseMapper<VoucherBillFileZJ> {

    List<VoucherBillFileZJ> selectBySearch(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    //根据报账单id获取附件列表
    List<VoucherBillFileZJ> selectByVoucherBillId(@Param("voucherBillId") Long voucherBillId);
    //根据报账单编号获取附件列表
    List<VoucherBillFileZJ> selectByVoucherBillNo(@Param("voucherBillNo") String voucherBillNo);
    //根据报账单ID获取附件列表
    List<VoucherBillFileZJ> getByBillIdList(@Param("billIdList") List<Long> billIdList);

}
