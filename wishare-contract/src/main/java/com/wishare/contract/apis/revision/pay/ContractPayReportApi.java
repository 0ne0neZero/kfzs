package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.fo.revision.pay.report.*;
import com.wishare.contract.apps.service.revision.pay.ContractPayReportService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/5/28 14:15
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同台账"})
@RequestMapping("/payReport")
public class ContractPayReportApi {

    @Autowired
    private ContractPayReportService contractPayReportService;

    @ApiOperation(value = "根据条件获取统计表数据")
    @PostMapping("/getTotalPayReportList")
    public List<ContractPayReportListV> getTotalPayReportList(@RequestBody ContractPayReportF contractPayReportF) {
        return contractPayReportService.getTotalPayReportList(contractPayReportF);
    }

    @ApiOperation(value = "根据条件导出统计表数据")
    @PostMapping("/exportTotalPayReportList")
    public PageV<ContractPayReportListV> exportTotalPayReportList(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.exportTotalPayReportList(request);
    }

    @ApiOperation(value = "根据条件获取统计表明细数据")
    @PostMapping("/getDetailPayReportList")
    public PageV<ContractPayReportDetailListV> getDetailPayReportList(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.getDetailPayReportList(request);
    }

    @ApiOperation(value = "根据条件导出统计表明细数据")
    @PostMapping("/exportDetailPayReportList")
    public PageV<ContractPayReportDetailListV> exportDetailPayReportList(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.getDetailPayReportList(request);
    }

    @ApiOperation(value = "YJ数据分析报表")
    @PostMapping("/getYjDataAnalysisReport")
    public PageV<ContractPayYjListV> getYjDataAnalysisReport(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.getYjDataAnalysisReport(request);
    }

    @ApiOperation(value = "根据条件导出YJ数据分析报表")
    @PostMapping("/exportYjDataAnalysisReport")
    public PageV<ContractPayYjListV> exportYjDataAnalysisReport(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.exportYjDataAnalysisReport(request);
    }

    @ApiOperation(value = "NK数据分析汇总报表")
    @PostMapping("/getNKDataAnalysisTotalReport")
    public PageV<ContractPayNkTotalListV> getNKDataAnalysisTotalReport(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.getNKDataAnalysisTotalReport(request);
    }
    @ApiOperation(value = "NK数据分析汇总报表-导出")
    @PostMapping("/exportNKDataAnalysisTotalReport")
    public PageV<ContractPayNkTotalListV> exportNKDataAnalysisTotalReport(@RequestBody PageF<SearchF<?>> request) {
        return contractPayReportService.getNKDataAnalysisTotalReport(request);
    }

}
