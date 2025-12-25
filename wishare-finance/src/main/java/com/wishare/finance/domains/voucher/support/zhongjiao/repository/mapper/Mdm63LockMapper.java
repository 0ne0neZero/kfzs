package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.mdm.entity.Mdm63LockE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author longhuadmin
 */
@Mapper
public interface Mdm63LockMapper extends BaseMapper<Mdm63LockE> {

    int deleteByVoucherBillNo(@Param("voucherBillNo") String voucherBillNo);
    //根据报账单ID删除锁定MDM63数据
    void updateDeletedByVoucherBIllId(@Param("voucherBillId") Long voucherBillId);
    //
    int deleteMdm63Lock(@Param("ftId") String ftId, @Param("voucherBillNo") String voucherBillNo);
}
