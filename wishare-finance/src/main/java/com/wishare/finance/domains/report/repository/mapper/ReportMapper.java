package com.wishare.finance.domains.report.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.report.dto.AdvanceRateReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeClosingDateDto;
import com.wishare.finance.domains.report.dto.ChargeCollectionRateReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeDailyReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeDailyReportTotalDto;
import com.wishare.finance.domains.report.dto.ChargeGenerateDto;
import com.wishare.finance.domains.report.dto.ChargeLastYearArrearsReceiveDto;
import com.wishare.finance.domains.report.dto.ChargeLastYearArrearsSettleDto;
import com.wishare.finance.domains.report.dto.ChargeReductionReportPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 报表mapper
 *
 * @author yancao
 */
@Mapper
public interface ReportMapper {

    /**
     * 分页查询收费日报
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @param chargeDate   收费时间
     * @return IPage
     */
    IPage<ChargeDailyReportPageDto> chargeDailyReportPage(Page<Object> page,
                                                          @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                          @Param(value = "chargeDate") LocalDate chargeDate);

    /**
     * 分页查询收费减免统计表
     *
     * @param page               分页参数
     * @param queryWrapper       查询参数
     * @param reductionEndDate   减免结束时间
     * @param reductionStartDate 减免开始时间
     * @return IPage
     */
    IPage<ChargeReductionReportPageDto> chargeReductionReportPage(Page<Object> page,
                                                                  @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                                  @Param(value = "reductionEndDate") LocalDate reductionEndDate,
                                                                  @Param(value = "reductionStartDate") LocalDate reductionStartDate);

    /**
     * 收费项目合计
     *
     * @param queryWrapper 查询参数
     * @param chargeDate   收费时间
     * @return List
     */
    List<ChargeDailyReportTotalDto> chargeDailyReportChargeItemTotal(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                                     @Param(value = "chargeDate") LocalDate chargeDate);

    /**
     * 收费方式合计
     *
     * @param queryWrapper 查询参数
     * @param chargeDate   收费时间
     * @return List
     */
    List<ChargeDailyReportTotalDto> chargeDailyReportPayChannelTotal(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                                     @Param(value = "chargeDate") LocalDate chargeDate);


    /**
     * 分页查询收缴率
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return IPage
     */
    IPage<ChargeCollectionRateReportPageDto> chargeCollectionRateReportPage(Page<Object> page,
                                                                            @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                                            @Param(value = "startDate") LocalDate startDate,
                                                                            @Param(value = "endDate") LocalDate endDate);

    /**
     * 根据房号和费项获取账单收款截止日期
     *
     * @param roomIdList       房号id集合
     * @param chargeItemIdList 费项id集合
     * @return List
     */
    List<ChargeClosingDateDto> queryChargeClosingDate(@Param(value = "roomIdList") List<Long> roomIdList,
                                                      @Param(value = "chargeItemIdList") List<Long> chargeItemIdList);

    /**
     * 根据开始时间结束时间获取欠费信息
     *
     * @param roomIdList        房号id
     * @param chargeItemIdList  费项id
     * @param lastYearStartDate 开始时间
     * @param lastYearEndDate   结束时间
     * @return List
     */
    List<ChargeLastYearArrearsReceiveDto> queryLastYearArrearsReceiveInfo(@Param(value = "roomIdList") List<Long> roomIdList,
                                                                          @Param(value = "chargeItemIdList") List<Long> chargeItemIdList,
                                                                          @Param(value = "lastYearStartDate") LocalDate lastYearStartDate,
                                                                          @Param(value = "lastYearEndDate") LocalDate lastYearEndDate);

    /**
     * 根据开始时间结束时间获取欠费信息
     *
     * @param roomIdList       房号id
     * @param chargeItemIdList 费项id
     * @param startDate        开始时间
     * @param endDate          结束时间
     * @return List
     */
    List<ChargeLastYearArrearsSettleDto> queryLastYearArrearsSettleInfo(@Param(value = "roomIdList") List<Long> roomIdList,
                                                                        @Param(value = "chargeItemIdList") List<Long> chargeItemIdList,
                                                                        @Param(value = "startDate") LocalDate startDate,
                                                                        @Param(value = "endDate") LocalDate endDate);

    /**
     * 导出_账单生成查询报表数据
     *
     * @return
     */
    List<ChargeGenerateDto> exportGenerateBillReportList(@Param(value = "communitIds") List<String> communitId,
                                                         @Param(value = "roomId") List<Long> roomId,
                                                         @Param(value = "chargeItemName") String chargeItemName,
                                                         @Param(value = "startDate") LocalDate startDate,
                                                         @Param(value = "endDate") LocalDate endDate);

    /**
     * 分页查询预收率报表数据
     *
     * @param page         分页信息
     * @param queryWrapper 查询参数
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return IPage
     */
    IPage<AdvanceRateReportPageDto> advanceRateReportPage(Page<Object> page,
                                                          @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                          @Param(value = "startDate") LocalDate startDate,
                                                          @Param(value = "endDate") LocalDate endDate);

}
