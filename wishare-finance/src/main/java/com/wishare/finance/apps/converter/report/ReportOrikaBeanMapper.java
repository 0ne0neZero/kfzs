package com.wishare.finance.apps.converter.report;

import com.wishare.finance.apps.model.report.vo.AdvanceRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeCollectionRateReportPageV;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustReasonEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.report.dto.ChargeDailyReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeReductionReportPageDto;
import com.wishare.finance.domains.report.enums.InvoiceLineEnum;
import com.wishare.finance.infrastructure.easyexcel.ExportAdvanceCollectionRateReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeCollectionRateReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeDailyReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeReductionReportData;
import com.wishare.starter.Global;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ReportOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {

        //导出收费日报字段转换
        Global.mapperFactory.classMap(ChargeDailyReportPageDto.class, ExportChargeDailyReportData.class)
                .exclude("payChannel").exclude("payAmount")
                .byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ChargeDailyReportPageDto dto, ExportChargeDailyReportData exportData, MappingContext context) {
                        exportData.setPayAmount(BigDecimal.valueOf(((double) dto.getPayAmount()) / 100));
                        exportData.setPayChannel(SettleChannelEnum.valueOfByCode(dto.getPayChannel()).toString());
                    }
                }).register();

        //导出收费减免报表字段转换
        Global.mapperFactory.classMap(ChargeReductionReportPageDto.class, ExportChargeReductionReportData.class)
                .exclude("receivableAmount").exclude("reductionAmount").exclude("approvedState").exclude("reductionReason")
                .byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ChargeReductionReportPageDto dto, ExportChargeReductionReportData exportData, MappingContext context) {
                        exportData.setReceivableAmount(BigDecimal.valueOf(((double) dto.getReceivableAmount()) / 100));
                        exportData.setReductionAmount(BigDecimal.valueOf(((double) dto.getReductionAmount()) / 100));
                        exportData.setApprovedState(BillApproveStateEnum.valueOfByCode(dto.getApprovedState()).toString());
                        if(StringUtils.isNotEmpty(dto.getReductionReason())){
                            BillAdjustReasonEnum reasonEnum = BillAdjustReasonEnum.valueOfByCode(
                                    Integer.parseInt(dto.getReductionReason()));
                            exportData.setReductionReason(reasonEnum!=null?reasonEnum.getValue():null);
                        }
                    }
                }).register();

        //导出收缴率报表字段转换
        Global.mapperFactory.classMap(ChargeCollectionRateReportPageV.class, ExportChargeCollectionRateReportData.class)
                .exclude("invoiceTypeList")
                .exclude("totalAmount")
                .exclude("deductibleAmount")
                .exclude("actualPayAmount")
                .exclude("discountAmount")
                .exclude("actualUnPayAmount")
                .exclude("earlyReceivableAmount")
                .exclude("earlyDeductibleAmount")
                .exclude("earlyActualPayAmount")
                .exclude("earlyDiscountAmount")
                .exclude("earlyActualUnPayAmount")
                .exclude("sumReceivableAmount")
                .exclude("sumActualPayAmount")
                .byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ChargeCollectionRateReportPageV vo, ExportChargeCollectionRateReportData exportData, MappingContext context) {
                        StringBuilder invoiceTypeStr = new StringBuilder();
                        for (String s : vo.getInvoiceTypeList()) {
                            invoiceTypeStr.append(InvoiceLineEnum.valueOfByCode(Integer.parseInt(s)).toString());
                        }
                        exportData.setInvoiceTypeList(invoiceTypeStr.toString());
                        exportData.setTotalAmount(BigDecimal.valueOf(((double) vo.getTotalAmount()) / 100));
                        exportData.setDeductibleAmount(BigDecimal.valueOf(((double) vo.getDeductibleAmount()) / 100));
                        exportData.setActualPayAmount(BigDecimal.valueOf(((double) vo.getActualPayAmount()) / 100));
                        exportData.setDiscountAmount(BigDecimal.valueOf(((double) vo.getDiscountAmount()) / 100));
                        exportData.setActualUnPayAmount(BigDecimal.valueOf(((double) vo.getActualUnPayAmount()) / 100));
                        exportData.setEarlyReceivableAmount(BigDecimal.valueOf(((double) vo.getEarlyReceivableAmount()) / 100));
                        exportData.setEarlyDeductibleAmount(BigDecimal.valueOf(((double) vo.getEarlyDeductibleAmount()) / 100));
                        exportData.setEarlyActualPayAmount(BigDecimal.valueOf(((double) vo.getEarlyActualPayAmount()) / 100));
                        exportData.setEarlyDiscountAmount(BigDecimal.valueOf(((double) vo.getEarlyDiscountAmount()) / 100));
                        exportData.setEarlyActualUnPayAmount(BigDecimal.valueOf(((double) vo.getEarlyActualUnPayAmount()) / 100));
                        exportData.setSumReceivableAmount(BigDecimal.valueOf(((double) vo.getSumReceivableAmount()) / 100));
                        exportData.setSumActualPayAmount(BigDecimal.valueOf(((double) vo.getSumActualPayAmount()) / 100));
                    }
                }).register();

        //导出预收率字段转换
        Global.mapperFactory.classMap(AdvanceRateReportPageV.class, ExportAdvanceCollectionRateReportData.class)
                .exclude("invoiceTypeList").exclude("receivableAmount").exclude("actualPayAmount").exclude("actualUnPayAmount")
                .byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(AdvanceRateReportPageV vo, ExportAdvanceCollectionRateReportData exportData, MappingContext context) {
                        StringBuilder invoiceTypeStr = new StringBuilder();
                        for (String s : vo.getInvoiceTypeList()) {
                            invoiceTypeStr.append(InvoiceLineEnum.valueOfByCode(Integer.parseInt(s)).toString());
                        }
                        exportData.setInvoiceTypeList(invoiceTypeStr.toString());
                        exportData.setTotalAmount(BigDecimal.valueOf(((double) vo.getTotalAmount()) / 100));
                        exportData.setActualPayAmount(BigDecimal.valueOf(((double) vo.getActualPayAmount()) / 100));
                        exportData.setActualUnPayAmount(BigDecimal.valueOf(((double) vo.getActualUnPayAmount()) / 100));
                    }
                }).register();
    }
}
