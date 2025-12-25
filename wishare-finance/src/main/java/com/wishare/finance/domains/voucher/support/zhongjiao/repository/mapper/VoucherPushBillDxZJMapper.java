package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDo2;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJMoneyV;
import com.wishare.finance.apps.pushbill.vo.dx.vo.VoucherBillAutoFileZJVo;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherPushBillDxZJMapper extends BaseMapper<VoucherBillDxZJ> {

    /**
     * 分页查询凭证明细
     * @param page 分页信息
     * @param queryWrapper 查询条件
     * @return
     */
    Page<VoucherBillDxZJ> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 分页查询凭证明细V2
     * @param page 分页信息
     * @param queryWrapper 查询条件
     * @return
     */
    Page<VoucherBillZJDo2> selectBySearch2(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    VoucherBillZJMoneyV getMoney(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    void delete(@Param("voucherBillId")Long voucherBillId);

    void updatePushStateByVoucherBIllNo(@Param("voucherBillNo")String voucherBillNo, @Param("remark") JSONObject  remark,
                                        @Param("pushState") Integer pushState);


    @InterceptorIgnore(tenantLine = "on")
    VoucherBillDxZJ queryByVoucherBillNo(@Param("voucherBillNo")String voucherBillNo);

    VoucherBillDxZJ selectByRecordId(@Param("recordId") Long recordId);

    List<VoucherBillAutoFileZJVo> selectAutoFileVo(@Param("voucherBillNo") String voucherBillNo,
                                                   @Param("billEventType") Integer billEventType);
    //根据条件查询报账单数据
    VoucherBillDxZJ getVoucherBillDxZJByQuery(@Param("voucherBillNo") String voucherBillNo);
    //根据结算单id查询报账单数据
    List<VoucherBillDxZJ> getBillDxZjBySettlementId(@Param("settlementId") String settlementId);
    //根据ID更改来自合同数据
    void updateBilDxZjFromContract(@Param("id")Long id, @Param("otherBusinessReasons") String otherBusinessReasons, @Param("externalDepartmentCode") String externalDepartmentCode,@Param("calculationMethod") Integer calculationMethod);

    void deleteByVoucherBIllNo(@Param("voucherBillNo")String voucherBillNo);
}
