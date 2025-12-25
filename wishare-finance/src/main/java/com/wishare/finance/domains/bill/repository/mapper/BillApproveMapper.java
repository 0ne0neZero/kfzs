package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单审核记录映射类
 * @Author dxclay
 * @Date 2022/8/25
 * @Version 1.0
 */
@Mapper
public interface BillApproveMapper extends BaseMapper<BillApproveE> {

    @InterceptorIgnore(tenantLine = "on")
    List<BillApproveE> selectBillApproveEList(@Param(value = "operationId")Long operationId,@Param(value = "supCpUnitId") String supCpUnitId);
}
