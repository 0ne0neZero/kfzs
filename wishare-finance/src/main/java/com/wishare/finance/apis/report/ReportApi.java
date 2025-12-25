package com.wishare.finance.apis.report;

import com.wishare.finance.apps.model.report.vo.AdvanceRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeCollectionRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeDailyReportTotalV;
import com.wishare.finance.apps.model.report.vo.GenerateBillReportPageV;
import com.wishare.finance.apps.service.report.ReportAppService;
import com.wishare.finance.domains.report.dto.ChargeDailyReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeReductionReportPageDto;
import com.wishare.finance.infrastructure.utils.SearchFileUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 报表接口
 *
 * @author yancao
 */
@Api(tags = {"报表API"})
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReportApi {

    private final ReportAppService reportAppService;

    @PostMapping("/chargeDailyReportPage")
    @ApiOperation(value = "分页查询收费日报报表", notes = "分页查询收费日报报表")
    public PageV<ChargeDailyReportPageDto> chargeDailyReportPage(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.chargeDailyReportPage(query);
    }

    @PostMapping("/chargeDailyReport/total")
    @ApiOperation(value = "收费日报收费类型合计", notes = "收费日报收费类型合计")
    public ChargeDailyReportTotalV chargeDailyReportTotal(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.chargeDailyReportTotal(query);
    }

    @PostMapping("/chargeReductionReportPage")
    @ApiOperation(value = "分页查询收费减免统计报表", notes = "分页查询收费减免统计报表")
    public PageV<ChargeReductionReportPageDto> chargeReductionReportPage(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.chargeReductionReportPage(query);
    }

    @PostMapping("/chargeCollectionRateReportPage")
    @ApiOperation(value = "分页查询收缴率报表", notes = "分页查询收缴率报表")
    public PageV<ChargeCollectionRateReportPageV> chargeCollectionRateReportPage(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.chargeCollectionRateReportPage(query);
    }

    @PostMapping("/generateBillReportPage")
    @ApiOperation(value = "分页查询账单生成报表", notes = "分页账单生成报表")
    public PageV<GenerateBillReportPageV> generateBillReportPage(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.generateBillReportPage(query);
    }

    @PostMapping("/advanceRateReportPage")
    @ApiOperation(value = "分页查询预收率统计表报表", notes = "分页查询预收率统计表报表")
    public PageV<AdvanceRateReportPageV> advanceRateReportPage(@Validated @RequestBody PageF<SearchF<?>> query) {
        return reportAppService.advanceRateReportPage(query);
    }

    @PostMapping("/export/generateBillReport")
    @ApiOperation(value = "导出_账单生成查询报表", notes = "导出_账单生成查询报表")
    public Boolean exportGenerateBillReport(@Validated @RequestBody PageF<SearchF<?>> form, HttpServletResponse response) {
        LocalDate startDate = SearchFileUtil.getStartDate(form);
        LocalDate endDate = SearchFileUtil.getEndDate(form);
        checkStartAndEndDate(startDate, endDate);
        //计费周期（必填，日期范围选择框，默认为近3个月）
        if (ObjectUtils.allNull(startDate, endDate)) {
            LocalDate now = LocalDate.now();
            startDate = now.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
            endDate = now.with(TemporalAdjusters.lastDayOfMonth());
        }
        return reportAppService.exportGenerateBillReport(form, response,startDate,endDate);
    }

    /**
     * 校检开始日期和结束日期，需要同时存在或者同时不存在
     */
    private void checkStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate == null) {
            throw BizException.throw400("结束日期不能为空");
        }
        if (startDate == null && endDate != null) {
            throw BizException.throw400("开始日期不能为空");
        }
    }

    @ApiOperation(value = "导出收费日报报表", notes = "导出收费日报报表")
    @PostMapping("/chargeDailyReport/export")
    public void exportChargeDailyReport(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        reportAppService.exportChargeDailyReport(queryF, response);
    }

    @ApiOperation(value = "导出收费减免统计报表", notes = "导出收费减免统计报表")
    @PostMapping("/chargeReductionReport/export")
    public void exportChargeReductionReport(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        reportAppService.exportChargeReductionReport(queryF, response);
    }

    @ApiOperation(value = "导出收缴率报表", notes = "导出收缴率报表")
    @PostMapping("/chargeCollectionRateReport/export")
    public void exportChargeCollectionRateReport(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        reportAppService.exportChargeCollectionRateReport(queryF, response);
    }

    @ApiOperation(value = "导出预收率统计表报表", notes = "导出预收率统计表报表")
    @PostMapping("/advanceRateReport/export")
    public void exportAdvanceRateReport(@RequestBody PageF<SearchF<?>> queryF, HttpServletResponse response) {
        reportAppService.exportAdvanceRateReport(queryF, response);
    }

}
