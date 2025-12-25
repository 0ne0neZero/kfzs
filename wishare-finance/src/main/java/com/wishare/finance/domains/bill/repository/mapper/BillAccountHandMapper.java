package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.AccountHandCountDto;
import com.wishare.finance.domains.bill.dto.AccountHandTotalDto;
import com.wishare.finance.domains.bill.entity.BillAccountHand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 交账信息表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-01-11
 */
@Mapper
public interface BillAccountHandMapper extends BaseMapper<BillAccountHand> {


    /**
     * 分页查询信息
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return 分页列表
     */
    Page<BillAccountHand> queryPageBySearch(Page<?> page, @Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);


    /**
     * 查询交账合计数据
     * @param queryWrapper 查询参数
     * @return 合计数据
     */
    AccountHandTotalDto queryTotal(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    /**
     * 交账更新
     * @param queryWrapper 查询条件
     * @return 更新数
     */
    int updateOfHandAccount(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    /**
     * 应收账单交账状态更新
     * @param queryWrapper 查询条件
     * @return 更新数
     */
    int updateRecBillOfHandAccount(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper
        , @Param("supCpUnitId")String supCpUnitId
        , @Param("receivableBillName")String receivableBillName
        , @Param("billAccountHandName")String billAccountHandName);

    /**
     * 应收账单交账状态更新
     * @param queryWrapper 查询条件
     * @return 更新数
     */
    int updateAdvBillOfHandAccount(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper
        , @Param("billAccountHandName")String billAccountHandName);

    /**
     * 查询可交账数量
     * @param queryWrapper 查询条件
     * @return 更新数
     */
    List<AccountHandCountDto> queryHandAccountCount(@Param(Constants.WRAPPER)QueryWrapper<?> queryWrapper);

    /**
     * 查询已开票但BILL_ACCOUNT_HAND 表中无开票信息的数据
     */
    @InterceptorIgnore(tenantLine = "on")
    List<BillAccountHand> queryNoInvoiceInfoList(@Param("supCpUnitId")String supCpUnitId);

    /**
     * 获取交账账单id列表
     * @param page
     * @param putLogicDeleted
     * @return
     */
    Page<String> getHandAccountBillIdPage(Page<String> page, @Param(Constants.WRAPPER)QueryWrapper<?> putLogicDeleted);

    /**
     * 获取交账信息表所有项目
     * @return
     */
    List<String> queryAllCommunityIdList();
}
