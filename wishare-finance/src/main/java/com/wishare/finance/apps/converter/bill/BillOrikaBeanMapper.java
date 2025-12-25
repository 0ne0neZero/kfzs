package com.wishare.finance.apps.converter.bill;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.fo.ApproveBatchReceivableBillF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.domains.bill.command.BatchApproveBillCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.AllBillPageDto;
import com.wishare.finance.domains.bill.dto.GatherBillDto;
import com.wishare.finance.domains.bill.dto.PayBillDto;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.infrastructure.easyexcel.*;
import com.wishare.finance.infrastructure.remote.vo.bill.AdvanceBillAllDetailRV;
import com.wishare.finance.infrastructure.remote.vo.bill.PayableBillAllDetailRV;
import com.wishare.finance.infrastructure.remote.vo.bill.ReceivableBillAllDetailRV;
import com.wishare.finance.infrastructure.remote.vo.bill.TemporaryChargeBillAllDetailRV;
import com.wishare.starter.Global;
import com.wishare.tools.starter.vo.FileVo;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 账单orika字段映射
 *
 * @author yancao
 */
@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class BillOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {

        Global.mapperFactory.classMap(ReceivableBillAllDetailRV.class, BillDetailMoreV.class)
                .field("id", "billId")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(TemporaryChargeBillAllDetailRV.class, BillDetailMoreV.class)
                .field("id", "billId")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(AdvanceBillAllDetailRV.class, BillDetailMoreV.class)
                .field("id", "billId")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(PayableBillAllDetailRV.class, BillDetailMoreV.class)
                .field("id", "billId")
                .byDefault()
                .register();

        //账单批量审核状态枚举映射
        Global.mapperFactory.classMap(ApproveBatchReceivableBillF.class, BatchApproveBillCommand.class).exclude("approveState").byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ApproveBatchReceivableBillF form, BatchApproveBillCommand command, MappingContext context) {
                        command.setApproveState(BillApproveStateEnum.valueOfByCode(form.getApproveState()));
                    }
                }).register();

        //调整信息中附件实体转换
        Global.mapperFactory.classMap(BillAdjustE.class, BillAdjustV.class).exclude("fileVos").byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(BillAdjustE eo, BillAdjustV vo, MappingContext context) {
                        vo.setFileVos(JSON.parseArray(JSON.toJSONString(eo.getFileVos()), FileVo.class));
                    }
                }).register();

//        Global.mapperFactory.classMap(GatherBillDto.class, GatherBillV.class).exclude("discounts").byDefault().customize(
//                new CustomMapper<>() {
//                    @Override
//                    public void mapAtoB(GatherBillDto dto, GatherBillV vo, MappingContext context) {
//                        if (StringUtils.isNotEmpty(dto.getDiscounts())) {
//                            vo.setDiscounts(JSON.parseArray(dto.getDiscounts(), DiscountOBV.class));
//                        } else {
//                            vo.setDiscounts(new ArrayList<>());
//                        }
//                    }
//                }).register();

        Global.mapperFactory.classMap(ReceivableBill.class, ReceivableBillsV.ReceivableBillV.class)
                .field("payerId", "targetObjId")
                .field("payerName", "targetObjName")
                .field("settleState", "payState")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(ReceivableBill.class, HistoryV.class)
                .field("id", "billId")
                .field("payerId", "targetObjId")
                .field("payerName", "targetObjName")
                .byDefault()
                .register();

        Global.mapperFactory.classMap(GatherDetail.class, BillSettleE.class)
                .field("recBillId", "billId")
                .field("gatherBillNo", "settleNo")
                .field("payChannel", "settleChannel")
                .field("payWay", "settleWay")
                .field("payTime", "settleTime")
                .byDefault()
                .register();

        //导出收款单字段转换
        Global.mapperFactory.classMap(GatherBillDto.class, ExportGatherBillData.class)
                .exclude("totalAmount").exclude("payChannel").exclude("invoiceState").exclude("sysSource")
                .byDefault().customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(GatherBillDto dto, ExportGatherBillData exportData, MappingContext context) {
                                exportData.setTotalAmount(BigDecimal.valueOf(((double) dto.getTotalAmount()) / 100));
                                exportData.setPayChannel(SettleWayEnum.valueOfByCode(dto.getPayWay()) + "-" + SettleChannelEnum.valueOfByCode(dto.getPayChannel()));
                                exportData.setInvoiceState(InvoiceStateEnum.valueOfByCode(dto.getInvoiceState()).toString());
                                exportData.setSysSource(Objects.nonNull(SysSourceEnum.valueOfByCode(dto.getSysSource())) ? SysSourceEnum.valueOfByCode(dto.getSysSource()).toString() : null);
                            }
                        }).register();

        //导出付款单字段转换
        Global.mapperFactory.classMap(PayBillDto.class, ExportPayBillData.class)
                .exclude("totalAmount").exclude("payChannel").exclude("invoiceState").exclude("sysSource")
                .byDefault().customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(PayBillDto dto, ExportPayBillData exportData, MappingContext context) {
                                exportData.setTotalAmount(BigDecimal.valueOf(((double) dto.getTotalAmount()) / 100));
                                exportData.setPayChannel(SettleWayEnum.valueOfByCode(dto.getPayWay()) + "-" + SettleChannelEnum.valueOfByCode(dto.getPayChannel()));
                                exportData.setInvoiceState(InvoiceStateEnum.valueOfByCode(dto.getInvoiceState()).toString());
                                exportData.setSysSource(Objects.nonNull(SysSourceEnum.valueOfByCode(dto.getSysSource())) ? SysSourceEnum.valueOfByCode(dto.getSysSource()).toString() : null);
                            }
                        }).register();

        //导出应收单字段转换
        Global.mapperFactory.classMap(ReceivableBill.class, ExportReceivableBillData.class)
                .exclude("totalAmount").exclude("receivableAmount").exclude("settleAmount").exclude("settleState").exclude("approvedState").exclude("sysSource")
                .byDefault().customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(ReceivableBill bill, ExportReceivableBillData exportData, MappingContext context) {
                                exportData.setTotalAmount(BigDecimal.valueOf(((double) bill.getTotalAmount()) / 100));
                                exportData.setReceivableAmount(BigDecimal.valueOf(((double) bill.getReceivableAmount()) / 100));
                                exportData.setSettleAmount(BigDecimal.valueOf(((double) bill.getSettleAmount()) / 100));
                                exportData.setApprovedState(BillApproveStateEnum.valueOfByCode(bill.getApprovedState()).getValue());
                                exportData.setSettleState(BillSettleStateEnum.valueOfByCode(bill.getSettleState()).getValue());
                                exportData.setSysSource(Objects.nonNull(SysSourceEnum.valueOfByCode(bill.getSysSource())) ? SysSourceEnum.valueOfByCode(bill.getSysSource()).toString() : null);
                            }
                        }).register();

        //导出应付单字段转换
        Global.mapperFactory.classMap(PayableBill.class, ExportPayableBillData.class)
                .exclude("totalAmount").exclude("receivableAmount").exclude("settleAmount").exclude("settleState").exclude("approvedState").exclude("sysSource")
                .byDefault().customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(PayableBill bill, ExportPayableBillData exportData, MappingContext context) {
                                exportData.setTotalAmount(BigDecimal.valueOf(((double) bill.getTotalAmount()) / 100));
                                exportData.setReceivableAmount(BigDecimal.valueOf(((double) bill.getReceivableAmount()) / 100));
                                exportData.setSettleAmount(BigDecimal.valueOf(((double) bill.getSettleAmount()) / 100));
                                exportData.setApprovedState(BillApproveStateEnum.valueOfByCode(bill.getApprovedState()).getValue());
                                exportData.setSettleState(BillSettleStateEnum.valueOfByCode(bill.getSettleState()).getValue());
                                exportData.setSysSource(Objects.nonNull(SysSourceEnum.valueOfByCode(bill.getSysSource())) ? SysSourceEnum.valueOfByCode(bill.getSysSource()).toString() : null);
                            }
                        }).register();

        //导出预收单字段转换
        Global.mapperFactory.classMap(AdvanceBill.class, ExportAdvanceBillData.class)
                .exclude("totalAmount").exclude("payChannel").exclude("invoiceState").exclude("sysSource")
                .byDefault().customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(AdvanceBill bill, ExportAdvanceBillData exportData, MappingContext context) {
                                exportData.setTotalAmount(BigDecimal.valueOf(((double) bill.getTotalAmount()) / 100));
                                exportData.setPayChannel(SettleWayEnum.valueOfByCode(bill.getPayWay()) + (StringUtils.isNotEmpty(bill.getPayChannel()) ? ("-" + SettleChannelEnum.valueOfByCode(bill.getPayChannel())): ""));
                                exportData.setInvoiceState(InvoiceStateEnum.valueOfByCode(bill.getInvoiceState()).toString());
                                exportData.setSysSource(Objects.nonNull(SysSourceEnum.valueOfByCode(bill.getSysSource())) ? SysSourceEnum.valueOfByCode(bill.getSysSource()).toString() : null);
                            }
                        }).register();


    }



}
