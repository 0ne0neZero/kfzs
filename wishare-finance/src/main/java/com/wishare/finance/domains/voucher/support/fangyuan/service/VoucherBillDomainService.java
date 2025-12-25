package com.wishare.finance.domains.voucher.support.fangyuan.service;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.pushbill.fo.DelPushBillF;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.ChargeCustomer;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBill;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillSysEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.fybill.FangYuanBillDataClient;
import com.wishare.finance.domains.voucher.support.fangyuan.model.BillHeadF;
import com.wishare.finance.domains.voucher.support.fangyuan.model.DelF;
import com.wishare.finance.domains.voucher.support.fangyuan.model.DillDetailsF;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.ChargeCustomerRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherPushBillRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbQueryRF;
import com.wishare.finance.infrastructure.remote.vo.external.mdmmb.MdmMbCommunityRV;
import com.wishare.finance.infrastructure.remote.vo.fy.FYSendresultV;
import com.wishare.finance.infrastructure.remote.vo.space.SAssetV;
import com.wishare.finance.infrastructure.support.thread.AppRunnable;
import com.wishare.finance.infrastructure.support.thread.AppThreadManager;
import com.wishare.finance.infrastructure.utils.XStreamUtils;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum.*;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDomainService {
    private final VoucherPushBillRepository voucherPushBillRepository;
    private final VoucherBillDetailRepository voucherBillDetailRepository;
    private final FangYuanBillDataClient fangYuanBillDataClient;
    private final ChargeItemRepository chargeItemRepository;
    private final SpaceClient spaceClient;
    private final String CHARGE_ITEM_CODE = "0110";
    private final ReceivableBillRepository receivableBillRepository;
    private final ChargeCustomerRepository chargeCustomerRepository;
    private final ExternalClient externalClient;
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;
    private final ReconciliationDetailRepository reconciliationDetailRepository;
    private final ReconciliationRepository reconciliationRepository;

    public Boolean syncBatchPushBill(List<Long> voucherIds, PushBillSysEnum valueOfByCode) {


        List<VoucherBill> voucherBills = voucherPushBillRepository.list(new LambdaQueryWrapper<VoucherBill>()
                .in(VoucherBill::getId, voucherIds)
                .in(VoucherBill::getPushState, 1, 3)
                .eq(VoucherBill::getDeleted, 0));

        List<VoucherBill> collect = voucherBills.stream().map(s -> {
            s.setPushState(4);
            return s;
        }).collect(Collectors.toList());
        voucherPushBillRepository.updateBatchById(collect);
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                List<Long> errorList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(voucherBills)) {
                    for (VoucherBill voucherBill : voucherBills) {
                        try {
                            FYSendresultV fySendresultV = syncToFy(voucherBill);
                            if (Objects.nonNull(fySendresultV) && 0 == fySendresultV.getCode()) {
                                voucherBill.setPushState(PushBillTypeEnum.已推送.getCode());
                                voucherBill.setRemark(fySendresultV.getMsg());
                                String msg = fySendresultV.getMsg();
                                String ncFinanceNo = fySendresultV.getMsg().substring(msg.indexOf("<content>") + 9, msg.indexOf("</content>"));
                                voucherBill.setNcFinanceNo(ncFinanceNo);
                                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetail>()
                                        .eq(VoucherPushBillDetail::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                        .set(VoucherPushBillDetail::getPushBillState, 2));
                            } else {
                                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                                voucherBill.setRemark(fySendresultV.getMsg());
                                errorList.add(voucherBill.getId());
                                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetail>()
                                        .eq(VoucherPushBillDetail::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                        .set(VoucherPushBillDetail::getPushBillState, 3));
                            }
                        } catch (Exception e) {
                            log.error("凭证推送失败 ---------> : ", e);
                            voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                            voucherBill.setRemark(e.getMessage());
                            errorList.add(voucherBill.getId());
                            voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetail>()
                                    .eq(VoucherPushBillDetail::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                    .set(VoucherPushBillDetail::getPushBillState, 3));
                        }
                        voucherPushBillRepository.updateById(voucherBill);
                    }
                }
            }
        });
        return true;
    }

    private FYSendresultV syncToFy(VoucherBill voucherBill) {
        List<VoucherPushBillDetail> list = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetail>()
                .eq(VoucherPushBillDetail::getVoucherBillNo, voucherBill.getVoucherBillNo())
                .eq(VoucherPushBillDetail::getVisible, 0));
        //获取账单id
        List<Long> billIds = list.stream().map(VoucherPushBillDetail::getBillId).collect(Collectors.toList());
        List<VoucherPushBillDetail> voucherPushBillDetails = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetail>()
                .eq(VoucherPushBillDetail::getBillEventType, 3)
                .in(VoucherPushBillDetail::getBillId, billIds));
        Map<Long, Integer> pushState = Maps.newHashMap();
        int billEventType;
        if (CollectionUtils.isNotEmpty(voucherPushBillDetails)) {
            billEventType = voucherPushBillDetails.get(0).getBillEventType();
            if (欠费计提.equalsByCode(billEventType)) {
                pushState = voucherPushBillDetails.stream().collect(Collectors.toMap(VoucherPushBillDetail::getBillId, VoucherPushBillDetail::getPushBillState));
            }
        }
        MdmMbQueryRF mdmMbProjectF = new MdmMbQueryRF();
        mdmMbProjectF.setMdmId(list.get(0).getCommunityId());
        mdmMbProjectF.setRemoteSystemId(11000000001L);
        mdmMbProjectF.setTableName("mdm_mb_community");
        List<MdmMbCommunityRV> mdmMbProjectRVS = externalClient.queryCodeInfo(mdmMbProjectF);
        log.info("方圆推单MdmMbProjectRV信息 ：{},项目id :{}", JSON.toJSONString(mdmMbProjectRVS), list.get(0).getCommunityId());
        if (CollectionUtils.isEmpty(mdmMbProjectRVS)) {
            throw BizException.throw400(list.get(0).getCommunityId() + "对应项目编码不存在");
        }
        List<ChargeItemE> chargeItemES = chargeItemRepository.list();
        Map<Long, String> chargeItemESMap = chargeItemES.stream().collect(Collectors.toMap(ChargeItemE::getId, ChargeItemE::getCode, (key1, key2) -> key2));
        BillHeadF billHeadF = new BillHeadF();
        String createTime = voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String newTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        billHeadF.setPk_org(mdmMbProjectRVS.get(0).getBusinessId());
        billHeadF.setCreationtime(createTime);
        billHeadF.setBilldate(newTime);
        billHeadF.setApprovedate(newTime);
        billHeadF.setEffectdate(newTime);
        billHeadF.setDef28(voucherBill.getVoucherBillNo());
        billHeadF.setDef29(voucherBill.getId());
        billHeadF.setPk_deptid(mdmMbProjectRVS.get(0).getBusinessId());
        List<DillDetailsF> dillDetailsFS = Lists.newArrayList();
        for (VoucherPushBillDetail voucherPushBillDetail : list) {
            DillDetailsF dillDetailsF = new DillDetailsF();
            //根据账单id查询已开票状态的
            List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.getByBillId(null, voucherPushBillDetail.getBillId());
            if (CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
                log.info("账单id:" + voucherPushBillDetail.getBillId() + "无需处理相关票据");
            } else {
                //具有的发票收据id
                List<Long> invoiceReceiptIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getInvoiceReceiptId).collect(Collectors.toList());
                List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.getByState(invoiceReceiptIds, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(), InvoiceReceiptStateEnum.部分红冲.getCode()));
                List<Long> invoiceIds = invoiceReceiptES.stream().map(InvoiceReceiptE::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(invoiceIds)) {
                    List<InvoiceE> invoiceEList = invoiceRepository.list(new LambdaQueryWrapper<InvoiceE>()
                            .in(InvoiceE::getInvoiceReceiptId, invoiceIds));
                    if (CollectionUtils.isNotEmpty(invoiceEList)) {
                        List<String> invoiceNoS = invoiceEList.stream().map(InvoiceE::getInvoiceNo).collect(Collectors.toList());
                        log.info("发票号:" + StringUtils.join(invoiceNoS, ","));
                        dillDetailsF.setInvoiceno(StringUtils.join(invoiceNoS, ","));
                        log.info("发票购买方:  " + invoiceEList.get(0).getBuyerName());
                        dillDetailsF.setDef2(invoiceEList.get(0).getBuyerName());
                    } else {
                        log.info("账单付款方:  " + voucherPushBillDetail.getPayeeName());
                        dillDetailsF.setDef2(voucherPushBillDetail.getPayeeName());
                    }
                }
            }

            ChargeCustomer one = chargeCustomerRepository.getOne(new LambdaQueryWrapper<ChargeCustomer>().eq(ChargeCustomer::getChargeItemCode, chargeItemESMap.get(voucherPushBillDetail.getChargeItemId())));
            if (Objects.isNull(one)) {
                throw BizException.throw400("费项" + chargeItemESMap.get(voucherPushBillDetail.getChargeItemId()) + "对应客商不存在");
            }
            //获取账单对应的所有房屋信息
            if (StringUtils.isNotBlank(voucherPushBillDetail.getRoomId())) {
                List<SAssetV> bySpaceId = spaceClient.getBySpaceId(Long.valueOf(voucherPushBillDetail.getRoomId()));
                if (CollectionUtils.isNotEmpty(bySpaceId)) {
                    dillDetailsF.setDef1(bySpaceId.get(0).getSpaceName());
                }
            }

            dillDetailsF.setPk_org(mdmMbProjectRVS.get(0).getBusinessId());
            dillDetailsF.setPk_deptid(mdmMbProjectRVS.get(0).getBusinessId());
            dillDetailsF.setBilldate(newTime);
            //方圆支付推编码 过滤其他的支付方式
            if (!"OTHER".equals(voucherPushBillDetail.getPayChannel())) {
                String fYPayNoOfByCode = SettleChannelEnum.fYPayNoOfByCode(voucherPushBillDetail.getPayChannel());
                dillDetailsF.setPk_balatype(fYPayNoOfByCode);
            }
            Integer pushBillState = pushState.get(voucherPushBillDetail.getBillId());
            log.info("账单id:" + voucherPushBillDetail.getBillId() + "状态   " + pushBillState);
            if (Objects.nonNull(pushBillState) && pushBillState == 2) {
                dillDetailsF.setDef15("Y");
            } else {
                dillDetailsF.setDef15("null");
            }
            dillDetailsF.setCashitem(voucherPushBillDetail.getCashFlowItem());
            dillDetailsF.setPk_subjcode(one.getChargeItemName());
            dillDetailsF.setMoney_de(voucherPushBillDetail.getTaxIncludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxIncludAmount()));
            dillDetailsF.setLocal_money_de(voucherPushBillDetail.getTaxIncludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxIncludAmount()));
            dillDetailsF.setMoney_bal(voucherPushBillDetail.getTaxIncludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxIncludAmount()));
            dillDetailsF.setLocal_money_bal(voucherPushBillDetail.getTaxIncludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxIncludAmount()));
            dillDetailsF.setLocal_tax_de(voucherPushBillDetail.getTaxAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxAmount()));
            dillDetailsF.setNotax_de(voucherPushBillDetail.getTaxExcludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxExcludAmount()));
            dillDetailsF.setLocal_notax_de(voucherPushBillDetail.getTaxExcludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxExcludAmount()));
            dillDetailsF.setTaxrate(voucherPushBillDetail.getTaxRate().multiply(new BigDecimal(100)).stripTrailingZeros());
            dillDetailsF.setRecaccount(voucherPushBillDetail.getBankAccount());
            dillDetailsF.setOccupationmny(voucherPushBillDetail.getTaxIncludAmount() == null ? "0" : this.BigDecimal(voucherPushBillDetail.getTaxIncludAmount()));
            dillDetailsF.setDef2(voucherPushBillDetail.getPayerName());
            dillDetailsF.setDef17(voucherPushBillDetail.getBillEventType());
            dillDetailsF.setDef20(voucherPushBillDetail.getGatherBillNc());
            LocalDate accountDate = voucherPushBillDetail.getAccountDate();
            // 违约金和手续费等合并的账单没有归属月
            if (Objects.nonNull(accountDate)) {
                String accountTime = accountDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                dillDetailsF.setDef3(accountTime);
                dillDetailsF.setDef12(accountTime.substring(0, 4) + "年");
            }
            dillDetailsF.setDef13(voucherPushBillDetail.getBankSerialNumber());
            dillDetailsF.setDef14(voucherPushBillDetail.getBillCostType());
            if (BillInvoiceStateEnum.未开票.getCode() == voucherPushBillDetail.getInvoiceState() ||
                    BillInvoiceStateEnum.开票中.getCode() == voucherPushBillDetail.getInvoiceState()) {
                dillDetailsF.setDef16("未开票");
            } else {
                dillDetailsF.setDef16("已开票");
            }

            //物管期：费项编号为0110，取前期
            //其他费项就默认是物管期
            String chargeItemCode = chargeItemESMap.get(voucherPushBillDetail.getChargeItemId());
            if (CHARGE_ITEM_CODE.equals(chargeItemCode)) {
                dillDetailsF.setDef27("前期");
                dillDetailsF.setCustomer(voucherPushBillDetail.getPayerName());
                //收+“收费对象”内容+“收费项目”名称
                dillDetailsF.setScomment("收" + voucherPushBillDetail.getPayerName() + one.getChargeItemName());
            } else {
                dillDetailsF.setCustomer(one.getCustomer());
                //收+“收费对象”内容+“收费项目”名称
                dillDetailsF.setScomment("收" + one.getCustomer() + one.getChargeItemName());
                dillDetailsF.setDef27("物管期");
            }
            dillDetailsF.setDef29(voucherPushBillDetail.getVoucherBillDetailNo());
            dillDetailsFS.add(dillDetailsF);
        }
        billHeadF.setBodys(dillDetailsFS);
        String toXml = XStreamUtils.toXml(billHeadF);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>");
        sb.append("\n<ufinterface account=\"01\" billtype=\"F0\" businessunitcode=\"\" filename=\"\" groupcode=\"\" isexchange=\"Y\" orgcode=\"\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"WYSF\">");
        // 资金计划编号
        sb.append("\n<bill id=\"").append(voucherBill.getId()).append("\">\n");
        sb.append(toXml);
        sb.append("\n</bill>");
        sb.append("\n</ufinterface>");
        log.info("方圆推单xml信息 ：{}", sb);
        return fangYuanBillDataClient.getXml(sb.toString());
    }

    private String BigDecimal(Long bigDecimal) {
        return new BigDecimal(bigDecimal).divide(new BigDecimal(100)).toString();
    }

    @Transactional
    public FYSendresultV delVoucherBill(DelPushBillF delF) {
        VoucherBill byId = voucherPushBillRepository.getById(delF.getId());
        FYSendresultV fySendresultV = new FYSendresultV();
        if (Objects.isNull(byId)) {
            throw BizException.throw400("单据不存在不存在");
        }
        if (StringUtils.isEmpty(byId.getNcFinanceNo())) {
            removeVoucherBill(byId);
            fySendresultV.setCode(0);
            fySendresultV.setMsg("删除成功");
            return fySendresultV;
        }
        DelF del = new DelF();
        del.setBillid(byId.getNcFinanceNo().substring(0, byId.getNcFinanceNo().indexOf("_")));
        fySendresultV = fangYuanBillDataClient.delFYBill(del);
        log.info("方圆删单fySendresultV信息 ：{}", JSON.toJSONString(fySendresultV));
        if (Objects.nonNull(fySendresultV) && fySendresultV.getCode() == 0) {
            log.info("推单数据删除");
            removeVoucherBill(byId);
        }
        return fySendresultV;
    }

    public void removeVoucherBill(VoucherBill voucherBill) {
        voucherPushBillRepository.delete(voucherBill);
        List<VoucherPushBillDetail> list = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetail>()
                .eq(VoucherPushBillDetail::getVoucherBillNo, voucherBill.getVoucherBillNo()));
        if (CollectionUtils.isNotEmpty(list)) {
            voucherBillDetailRepository.deleteBatch(list);
            Integer billEventType = list.get(0).getBillEventType();
            if (未收款开票.equalsByCode(billEventType)) {
                // 恢复已生成报账单的红字凭证状态
                List<Long> billIds = list.stream().filter(bill -> bill.getTaxIncludAmount() < 0).map(VoucherPushBillDetail::getBillId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(billIds)) {
                    voucherBillDetailRepository.update(
                            new LambdaUpdateWrapper<VoucherPushBillDetail>()
                                    .in(VoucherPushBillDetail::getBillId, billIds)
                                    .eq(VoucherPushBillDetail::getBillEventType, 未收款开票.getCode())
                                    .set(VoucherPushBillDetail::getInvoiceRedType, 0));
                }
            }
            if (对账核销.equalsByCode(billEventType)) {
                ReconciliationE reconciliationE = reconciliationRepository.getById(voucherBill.getReconciliationId());
                if (Objects.nonNull(reconciliationE)) {
                    String voucherBillNo = reconciliationE.getVoucherBillNo();
                    String voucherBillId = reconciliationE.getVoucherBillId();
                    if (StringUtils.isNotBlank(voucherBillNo)) {
                        voucherBillNo = voucherBillNo.replace("," + voucherBill.getVoucherBillNo(), "");
                        voucherBillNo = voucherBillNo.replace(voucherBill.getVoucherBillNo(), "");
                        reconciliationE.setVoucherBillNo(voucherBillNo);
                    }
                    if (StringUtils.isNotBlank(voucherBillId)) {
                        voucherBillId = voucherBillId.replace("," + voucherBill.getId(), "");
                        voucherBillId = voucherBillId.replace(voucherBill.getId().toString(), "");
                        reconciliationE.setVoucherBillId(voucherBillId);
                    }
                    reconciliationRepository.updateById(reconciliationE);
                    List<ReconciliationDetailE> reconciliationDetailES = reconciliationDetailRepository.list(new LambdaQueryWrapper<ReconciliationDetailE>()
                            .eq(ReconciliationDetailE::getReconciliationId, reconciliationE.getId()));
                    for (ReconciliationDetailE reconciliationDetailE : reconciliationDetailES) {
                        String detailEVoucherBillNo = reconciliationDetailE.getVoucherBillNo();
                        if (StringUtils.isNotBlank(detailEVoucherBillNo)) {
                            detailEVoucherBillNo = detailEVoucherBillNo.replace("," + voucherBill.getVoucherBillNo(), "");
                            detailEVoucherBillNo = detailEVoucherBillNo.replace(voucherBill.getVoucherBillNo(), "");
                            reconciliationDetailE.setVoucherBillNo(detailEVoucherBillNo);
                        }
                    }
                    reconciliationDetailRepository.updateBatchById(reconciliationDetailES);

                }
            }
        }
    }
}
