package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 预收账单mapper
 *
 * @author yancao
 */
@Mapper
public interface BillMapper {


    /**
     * 分组分页查询账单（去除租户隔离）
     *
     * @param wrapper 查询信息
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    List<BillGroupDto> listByGroup(Page<Object> page,@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param(value = "ruleValue") String ruleValue, @Param("receivableBillName")String receivableBillName);

    /**
     * 分组分页查询账单（去除租户隔离）
     *
     * @param wrapper 查询信息
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    List<BillGroupDto> billListGroup(Page<Object> page,@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param(value = "ruleValue") String ruleValue, @Param("receivableBillName")String receivableBillName);

    /**
     * 分组分页查询账单（去除租户隔离）
     *
     * @param page    分页信息
     * @param wrapper 查询信息
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    IPage<BillGroupDto> pageWithGroup(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("receivableBillName")String receivableBillName);

    /**
     * 统计账单数量（去除租户隔离）
     *
     * @param wrapper 查询信息
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    Long countBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("receivableBillName")String receivableBillName);

    /**
     * 分组分页查询账单（去除租户隔离）
     *
     * @param page    分页信息
     * @param wrapper 查询信息
     * @return IPage
     */
    @InterceptorIgnore(tenantLine = "on")
    IPage<AllBillPageDto> queryPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
        @Param("receivableBillName")String receivableBillName
        , @Param("gatherDetailName")String gatherDetailName);

    /**
     * 分页查询无效收费账单 - 应收账单
     *
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return PageV
     */
    IPage<AllBillPageDto> invalidChargePageForReceivableBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询无效收费账单 - 收款单
     *
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return PageV
     */
    IPage<AllBillPageDto> invalidChargePageForGatherBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
        @Param("gatherBillTableName")String gatherBillTableName, @Param("gatherDetailTableName")String gatherDetailTableName);

    /**
     * 分页查询无效收费账单  - 预收单
     *
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return PageV
     */
    IPage<AllBillPageDto> invalidChargePageForAdvanceBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询无效付费账单  - 应付单
     *
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return PageV
     */
    IPage<AllBillPageDto> invalidPayPageForPayableBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询无效付费账单  - 付款单
     *
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return PageV
     */
    IPage<AllBillPageDto> invalidPayPageForPayBill(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);



    /**
     * 统计无效收费账单
     *
     * @param wrapper 查询条件
     * @return BillTotalDto
     */
    BillTotalDto invalidChargeStatisticsForReceivableBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 统计无效收费账单
     *
     * @param wrapper 查询条件
     * @return BillTotalDto
     */
    BillTotalDto invalidChargeStatisticsForGatherBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                                      @Param("gatherBillName")String gatherBillTableName, @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 统计无效收费账单
     *
     * @param wrapper 查询条件
     * @return BillTotalDto
     */
    BillTotalDto invalidChargeStatisticsForAdvanceBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 统计无效付费账单
     *
     * @param wrapper 查询条件
     * @return BillTotalDto
     */
    BillTotalDto invalidPayStatisticsForPayableBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 统计无效付费账单
     *
     * @param wrapper 查询条件
     * @return BillTotalDto
     */
    BillTotalDto invalidPayStatisticsForPayBill(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    /**
     * 分页查询合同流水账单
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return IPage
     */
    IPage<AllBillPageDto> flowContractPage(Page<Object> page,
                                           @Param(Constants.WRAPPER)QueryWrapper<?> wrapper,
                                            @Param("gatherBillName")String gatherBillName,
                                           @Param("gatherDetailName")String gatherDetailName);


    IPage<FlowBillPageDto> flowPageZJ(Page<Object> page,
                                           @Param(Constants.WRAPPER)QueryWrapper<?> wrapper,
                                           @Param("gatherBillName")String gatherBillName,
                                           @Param("gatherDetailName")String gatherDetailName);


    List<FlowBillPageDto> flowZjIdList(@Param(value = "idList") List<Long> idList,
                                            @Param("gatherBillName")String gatherBillName, @Param("gatherDetailName")String gatherDetailName);

    /**
     * 分页查询支付流水账单
     * @param page 分页参数
     * @param wrapper 查询参数
     * @return IPage
     */
    IPage<AllBillPageDto> channelFlowClaim(Page<Object> page, @Param(Constants.WRAPPER)QueryWrapper<?> wrapper,
                                           @Param("gatherBillName")String gatherBillName, @Param("gatherDetailName")String gatherDetailName);


    /**
     * 根据id集合查询合同流水账单(用于流水认领)
     *
     * @param idList 账单id集合
     * @return PageV
     */
    List<AllBillPageDto> flowContractIdList(@Param(value = "idList") List<Long> idList,
        @Param("gatherBillName")String gatherBillName, @Param("gatherDetailName")String gatherDetailName);

    /**
     * 费项分组分页查询账单列表(用于业务信息)
     *
     * @param of
     * @param wrapper
     * @return
     */
    Page<ChargeBillGroupDto> queryChargeBillByGroup(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("table") String table);

    /**
     * 统计费项分组分页查询账单列表(用于业务信息)
     *
     * @param wrapper
     * @return
     */
    ChargeBillGroupStatisticsDto statisticsChargeBillByGroup(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper, @Param("table") String table);

    /**
     * 根据条件获取所有账单
     *
     * @param wrapper
     * @return
     */
    List<AllBillPageDto> queryList(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
        @Param("receivableBillName")String receivableBillName, @Param("gatherDetailName")String gatherDetailName);

    /**
     * 统计合同流水账单金额(用于流水认领)
     *
     * @param wrapper 查询参数
     * @return FlowContractBillStatisticsDto
     */
    FlowContractBillStatisticsDto flowContractAmount(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
        @Param("gatherBillName")String gatherBillName, @Param("gatherDetailName")String gatherDetailName);

    List<ReceiptAmountDto> getReceiptAmount(@Param("gatherDetailName") String gatherDetailName, @Param("list") Set<Long> gatheIdSet);



    /**
     * 根据id集合查询账单
     *
     * @param idList 账单id集合
     * @param receivableBillName 名称
     * @return List<AllBillGroupDto>
     */
    List<AllBillGroupDto> getChargePayBillGroup(@Param(value = "idList") List<Long> idList, @Param("receivableBillName")String receivableBillName);
}
