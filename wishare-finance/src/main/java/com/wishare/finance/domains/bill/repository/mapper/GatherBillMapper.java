package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Mapper
public interface GatherBillMapper extends BaseMapper<GatherBill> {

    /**
     * 分页查询已审核付款单列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    IPage<GatherBillDto> queryPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                            @Param("gatherBillName")String gatherBillTableName,
                                            @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 分页查询已审核收款单列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    IPage<GatherBillDto> queryGatherPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
            @Param("gatherBillName")String gatherBillTableName,
            @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 分页查询未开票收款单列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    IPage<UnInvoiceGatherBillDto> unInvoiceGatherBillPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
        @Param("gatherBillName")String gatherBillTableName, @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 分页查询已审核付款单count
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    Integer queryPageCount(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
        @Param("gatherBillName")String gatherBillTableName, @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 统计付款单信息
     *
     * @param wrapper 统计条件
     * @return BillTotalDto
     */
    BillTotalDto queryTotal(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    BillTotalDto queryTotalNew(@Param(Constants.WRAPPER)QueryWrapper<?> wrapper,
                               @Param("gatherBillName")String gatherBillTableName, @Param("gatherDetailName")String gatherDetailTableName);

    /**
     * 查询历史缴费记录
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    Page<PayListDto> payList(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    /**
     * 查询开票收款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    List<PayListDto> payInvoiceList(@Param("page")Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
        @Param("gatherBillTableName")String gatherBillTableName, @Param("gatherDetailTableName")String gatherDetailTableName);

    Integer payInvoiceListCnt(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
        @Param("gatherBillTableName")String gatherBillTableName, @Param("gatherDetailTableName")String gatherDetailTableName);

    /**
     * 根据收款单id获取收款单
     *
     * @param gatherBillId 收款单id
     * @return GatherBillDto
     */
    GatherBillDto queryById(@Param(value = "gatherBillId") Long gatherBillId,
        @Param("gatherBillTableName")String gatherBillTableName, @Param("gatherDetailTableName")String gatherDetailTableName);

    /**
     * 根据id获取付款单
     *
     * @param gatherBillIdList 付款单id
     * @return PayBillDto
     */
    List<GatherBillDto> queryByIdList(@Param(value = "gatherBillIdList") List<Long> gatherBillIdList, @Param(value = "supCpUnitId") String supCpUnitId);

    /**
     * 获取结算账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> pageBillInferenceInfo(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 获取冲销作废账单推凭信息
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillInferenceV> pageBillInferenceOffInfo(Page<Object> of, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);

    /**
     * 查询凭证业务单据信息
     *
     * @param wrapper
     * @param special   特殊收款结算
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
            @Param("voucherEventType")int voucherEventType,
            @Param("special") boolean special,
            @Param("tableName") String tableName);

    /**
     * 查询凭证业务已银行对账单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherBankBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("tableName") String tableName);

    /**
     * 查询凭证业务已流水认领单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<VoucherBusinessBill> listVoucherClaimBillByQuery(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, @Param("gatherBillTableName") String gatherBillTableName,
                                                          @Param("gatherDetailTableName") String gatherDetailTableName,
                                                          @Param("receivableBillTableName") String receivableBillTableName, @Param("tableName") String tableName);

    /**
     * 根据TradeNo获取收款单条数，防止重复插入
     *
     * @param tradeNo 收款单id
     * @return Integer
     */
    Integer queryCountByTradeNo(@Param(value = "tradeNo") String tradeNo,@Param(value = "supCpUnitId") String supCpUnitId);

    /**
     * 分页查询收款记录(收款单维度无租户隔离)
     * @param of 分页参数
     * @param queryPayModel 参数
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<GatherDto> queryPageGatherBillIgnore(Page<Object> of, @Param("ew")  QueryWrapper<?> queryPayModel);

    @InterceptorIgnore(tenantLine = "true")
    int updateReceivableBillInferenceStateByGatherBillIds(@Param("list")List<Long> gatherBillIds,
                                                          @Param("inferenceState") Integer inferenceState,
                                                          @Param("supCpUnitId") String supCpUnitId);

    @InterceptorIgnore(tenantLine = "true")
    int updateGatherDetail(@Param("list") List<String> ids,
                           @Param("supCpUnitId") String supCpUnitId);

    @InterceptorIgnore(tenantLine = "true")
    List<Long> gatherBillIds(@Param("list")List<Long> gatherBillIds,
                                                          @Param("supCpUnitId") String supCpUnitId);

    List<VoucherBusinessBill>  listVoucherBillByQueryFromContract(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper,@Param("voucherEventType")int voucherEventType,@Param("businessBillType")int businessBillType);

    @InterceptorIgnore(tenantLine = "true")
    int updateSbAccountId(@Param("sbAccountId") Long sbAccountId, @Param("list") List<Long> idList, @Param("supCpUnitId") String supCpUnitId);

    List<GatherBill> getByOutBusIdV(@Param("hncId") String hncId, @Param("communityId") String communityId);

    List<GatherBill> getByTradeNo(@Param("tradeNoList") List<String> tradeNoList, @Param("supCpUnitId") String supCpUnitId);

}
