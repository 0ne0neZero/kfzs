package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJRecDetailE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface VoucherBillZJRecDetailMapper extends BaseMapper<VoucherBillZJRecDetailE> {

    int deleteByVoucherBillNo(@Param("voucherBillNo") String voucherBillNo);

    List<String> ftIdByVoucherBillNo(@Param("voucherBillNo") String voucherBillNo);

    //根据报账单编号删除应收款明细数据
    void updateDeletedByVoucherBIllNo(@Param("voucherBillNo") String voucherBillNo);

}
