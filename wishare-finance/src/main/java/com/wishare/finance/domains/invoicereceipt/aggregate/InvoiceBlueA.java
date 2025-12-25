package com.wishare.finance.domains.invoicereceipt.aggregate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillDetailV;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchBlueF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchDetailBlueF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBillAmount;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.apps.template.BlueInvoiceImportT;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillStartEndTimeDTO;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvSourceEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceClaimStatusEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTitleTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.enums.InvoiceReceiptSourceEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.BillSettleStateEnum;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/10/27
 * @Description:
 */
@Getter
@Setter
public class InvoiceBlueA {

    /**
     * 发票主表信息
     */
    private InvoiceReceiptE invoiceReceiptE;

    /**
     * 发票表信息
     */
    private InvoiceE invoiceE;

    /**
     * 发票明细表信息
     */
    private List<InvoiceReceiptDetailE> invoiceReceiptDetailEList;

    public InvoiceBlueA() {}

    /**
     * 构造蓝票
     *
     * @param billOjvs
     */
//    public InvoiceBlueA(List<BillDetailMoreV> billOjvs, AddInvoiceCommand command, OrgFinanceRv orgFinanceRv) {
//        invoiceReceiptE = getInvoiceReceipt(command, billOjvs);
//        invoiceE = getInvoice(command, invoiceReceiptE.getId(), orgFinanceRv);
//        invoiceReceiptDetailEList = getInvoiceReceiptDetail(command, invoiceReceiptE.getId(), billOjvs);
//    }

    /**
     * 构造蓝票
     *
     * @param billOjvs
     */
    public InvoiceBlueA(List<BillDetailMoreV> billOjvs, AddInvoiceCommand command, OrgFinanceRv orgFinanceRv) {
        invoiceReceiptE = getInvoiceReceipt(command, billOjvs);
        invoiceReceiptDetailEList = getInvoiceReceiptDetail(command, invoiceReceiptE.getId(), billOjvs);
        invoiceE = getInvoice(command, invoiceReceiptE.getId(), orgFinanceRv);
        invoiceReceiptE.verify(invoiceReceiptDetailEList);
    }

    /**
     * 构造补录蓝票
     * @param invoiceImport
     */
    public InvoiceBlueA(BlueInvoiceImportT invoiceImport) {
        invoiceReceiptE = getInvoiceReceipt(invoiceImport);
        invoiceE = getInvoice(invoiceImport, invoiceReceiptE.getId());
    }

    /**
     * 构造蓝票(无账单)
     * @param form
     */
    public InvoiceBlueA(InvoiceBatchBlueF form) {
        List<InvoiceBatchDetailBlueF> invoiceDetailList = form.getInvoiceBatchDetailBlueFList();
        invoiceReceiptE = getInvoiceReceipt(form,invoiceDetailList);
        invoiceE = getInvoice(form,invoiceReceiptE.getId());
        invoiceReceiptDetailEList = getInvoiceReceiptDetail(form,invoiceReceiptE.getId(),invoiceDetailList);
        invoiceReceiptE.verify(invoiceReceiptDetailEList);
    }

    /**
     * 构建发票明细数据
     *
     * @return
     */
    public List<InvoiceReceiptDetailE> getInvoiceReceiptDetail(InvoiceBatchBlueF form, Long invoiceReceiptId, List<InvoiceBatchDetailBlueF> invoiceDetailList) {
        List<InvoiceReceiptDetailE> list = Lists.newArrayList();
        long taxIncludedAmountSum = invoiceDetailList.stream().mapToLong(InvoiceBatchDetailBlueF::getTaxIncludedAmount).sum();
        invoiceDetailList.forEach(detail->{
            InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
            invoiceReceiptDetailE.setInvoiceReceiptId(invoiceReceiptId);
            invoiceReceiptDetailE.setGoodsName(detail.getGoodsName());
            invoiceReceiptDetailE.setNum(detail.getNum());
            invoiceReceiptDetailE.setTaxRate(detail.getTaxRate());
            invoiceReceiptDetailE.setUnit(detail.getUnit());
            invoiceReceiptDetailE.setPrice(detail.getPrice());
            invoiceReceiptDetailE.setWithTaxFlag(1);
            invoiceReceiptDetailE.setBillId(detail.getBillId());
            invoiceReceiptDetailE.setBillNo(detail.getBillNo());
            invoiceReceiptDetailE.setBillType(form.getBillType());
            invoiceReceiptDetailE.setBillStartTime(null);
            invoiceReceiptDetailE.setBillEndTime(null);
            invoiceReceiptDetailE.setPriceTaxAmount(taxIncludedAmountSum);
            invoiceReceiptDetailE.setInvoiceAmount(detail.getTaxIncludedAmount());
            invoiceReceiptDetailE.setSettleAmount(null);
            invoiceReceiptDetailE.setRoomId(null);
            invoiceReceiptDetailE.setRoomName(null);
            invoiceReceiptDetailE.setChargeItemId(null);
            invoiceReceiptDetailE.setChargeItemName(null);
            list.add(invoiceReceiptDetailE);
        });
        return list;
    }

    public void reRecord(List<BillDetailMoreV> billDetailMoreVList) {
        reRecord(invoiceReceiptE, billDetailMoreVList);
    }

    public void reRecord(InvoiceReceiptE invoiceReceiptE, List<BillDetailMoreV> billDetailMoreVList) {
        BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
        invoiceReceiptE.setCommunityId(billDetailMoreV.getCommunityId());
        invoiceReceiptE.setCommunityName(billDetailMoreV.getCommunityName());
        invoiceReceiptE.setStatutoryBodyId(billDetailMoreV.getStatutoryBodyId());
        invoiceReceiptE.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        invoiceReceiptE.setCostCenterId(billDetailMoreV.getCostCenterId());
        invoiceReceiptE.setCostCenterName(billDetailMoreV.getCostCenterName());
        invoiceReceiptE.setAppId(billDetailMoreV.getAppId());
        invoiceReceiptE.setAppName(billDetailMoreV.getAppName());
        invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getStatutoryBodyName());
    }

    /**
     * 构建发票表信息
     *
     * @return
     */
    public InvoiceE getInvoice(InvoiceBatchBlueF form, Long invoiceReceiptId) {
        InvoiceE invoiceE = new InvoiceE();
        invoiceE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
        invoiceE.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
        invoiceE.setInvoiceReceiptId(invoiceReceiptId);
        invoiceE.setInvoiceTitleType(form.getInvoiceTitleType());
        invoiceE.setBuyerName(form.getBuyerName());
        invoiceE.setBuyerTaxNum(form.getBuyerTaxNum());
        invoiceE.setBuyerTel(form.getBuyerTel());
        invoiceE.setBuyerAddress(form.getBuyerAddress());
        invoiceE.setBuyerAccount(form.getBuyerAccount());
        invoiceE.setBuyerPhone(form.getBuyerPhone());
        if (form.getInvoiceTitleType() == InvoiceTitleTypeEnum.企业.getCode()) {
            if (StringUtils.isBlank(form.getBuyerTaxNum())) {
                throw BizException.throw400("企业税号为空，请补充");
            }
            if (StringUtils.isBlank(form.getBuyerTel())) {
                throw BizException.throw400("企业联系方式为空，请补充");
            }
            if (StringUtils.isBlank(form.getBuyerAddress())) {
                throw BizException.throw400("企业地址为空，请补充");
            }
        }
        invoiceE.setSalerTaxNum(form.getSalerTaxNum());
        invoiceE.setSalerTel(form.getSalerTel());
        invoiceE.setSalerAddress(form.getSalerAddress());
        invoiceE.setSalerAccount(form.getSalerAccount());
        invoiceE.setPushMode(JSON.toJSONString(form.getPushMode()));
        invoiceE.setEmail(form.getEmail());
        invoiceE.setFreeTax(form.getFreeTax());
        return invoiceE;
    }

    /**
     * 根据中账单映射发票收据主表信息（无账单）
     *
     * @param form
     * @return
     */
    public InvoiceReceiptE getInvoiceReceipt(InvoiceBatchBlueF form, List<InvoiceBatchDetailBlueF> invoiceDetailList) {
        //含税总金额
//        long taxIncludedAmountSum = invoiceDetailList.stream().mapToLong(InvoiceBatchDetailBlueF::getTaxIncludedAmount).sum();
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setInvoiceReceiptNo(IdentifierFactory.getInstance().serialNumber("invoice_receipt_no", "FP", 20));
        invoiceReceiptE.setType(form.getType());
        invoiceReceiptE.setBillType(form.getBillType());
        invoiceReceiptE.setCommunityId(form.getCommunityId());
        invoiceReceiptE.setCommunityName(form.getCommunityName());
        invoiceReceiptE.setCustomerId(form.getCustomerId());
        invoiceReceiptE.setCustomerName(form.getCustomerName());
        invoiceReceiptE.setCustomerPhone(null);
        invoiceReceiptE.setStatutoryBodyId(form.getStatutoryBodyId());
        invoiceReceiptE.setStatutoryBodyName(form.getStatutoryBodyName());
        invoiceReceiptE.setCostCenterId(form.getCostCenterId());
        invoiceReceiptE.setCostCenterName(form.getCostCenterName());
        invoiceReceiptE.setChargeItemId(form.getChargeItemId());
        invoiceReceiptE.setChargeItemName(form.getChargeItemName());
        invoiceReceiptE.setApplyTime(LocalDateTime.now());
        invoiceReceiptE.setPriceTaxAmount(form.getPriceTaxAmount());
        invoiceReceiptE.setTaxRate(form.getTaxRate());
        invoiceReceiptE.setTaxRateId(form.getTaxRateId());
        invoiceReceiptE.setBusinessCode(form.getBusinessCode());
        invoiceReceiptE.setBusinessName(form.getBusinessName());
        invoiceReceiptE.setClerk(form.getClerk());
        invoiceReceiptE.setSysSource(form.getSysSource());
        invoiceReceiptE.setInvSource(InvSourceEnum.开具的发票.getCode());
        invoiceReceiptE.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptE.setAppId(form.getAppId());
        invoiceReceiptE.setAppName(form.getAppName());
        invoiceReceiptE.setExtendFieldOne(form.getExtendFieldOne());
        if (form.getType() == InvoiceLineEnum.增值税电子发票.getCode() || form.getType() == InvoiceLineEnum.全电普票.getCode()
                || form.getType() == InvoiceLineEnum.全电专票.getCode()) {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票中.getCode());
        } else {
            throw BizException.throw400("该接口暂不支持此类开票");
        }
        invoiceReceiptE.setRemark(form.getRemark());
        invoiceReceiptE.setInvRecUnitId(form.getInvRecUnitId());
        invoiceReceiptE.setInvRecUnitName(form.getInvRecUnitName());
        return invoiceReceiptE;
    }

    /**
     * 根据中账单映射发票收据主表信息
     *
     * @param command
     * @return
     */
    public InvoiceReceiptE getInvoiceReceipt(AddInvoiceCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        if (StringUtils.isNotBlank(command.getInvoiceNo())) {
            invoiceReceiptE.setInvoiceReceiptNo(command.getInvoiceNo());
        }else {
            invoiceReceiptE.setInvoiceReceiptNo(IdentifierFactory.getInstance().serialNumber("invoice_receipt_no", "FP", 20));
        }
        invoiceReceiptE.setType(command.getType());
        invoiceReceiptE.setBillType(command.getBillType());
        invoiceReceiptE.setCommunityId(billDetailMoreV.getCommunityId());
        invoiceReceiptE.setCommunityName(billDetailMoreV.getCommunityName());
        invoiceReceiptE.setCustomerId(null);
        invoiceReceiptE.setCustomerName(command.getBuyerName());
        invoiceReceiptE.setCustomerPhone(null);
        invoiceReceiptE.setStatutoryBodyId(billDetailMoreV.getStatutoryBodyId());
        invoiceReceiptE.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        invoiceReceiptE.setCostCenterId(billDetailMoreV.getCostCenterId());
        invoiceReceiptE.setCostCenterName(billDetailMoreV.getCostCenterName());
        invoiceReceiptE.setApplyTime(LocalDateTime.now());
        invoiceReceiptE.setPriceTaxAmount(command.getPriceTaxAmount());
        invoiceReceiptE.setClerk(command.getClerk());
        invoiceReceiptE.setPayeeName(command.getPayee());
        invoiceReceiptE.setSysSource(Objects.nonNull(billDetailMoreV.getSysSource()) ? billDetailMoreV.getSysSource() : command.getSysSource());
        invoiceReceiptE.setInvSource(command.getInvSource());
        invoiceReceiptE.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptE.setAppId(billDetailMoreV.getAppId());
        invoiceReceiptE.setAppName(billDetailMoreV.getAppName());
        invoiceReceiptE.setCostCenterId(billDetailMoreV.getCostCenterId());
        invoiceReceiptE.setCostCenterName(billDetailMoreV.getCostCenterName());
        invoiceReceiptE.setExtendFieldOne(command.getExtendFieldOne());
        if (command.getType() == InvoiceLineEnum.增值税电子发票.getCode() || command.getType() == InvoiceLineEnum.全电普票.getCode()
                || command.getType() == InvoiceLineEnum.全电专票.getCode() || (command.getType() == InvoiceLineEnum.增值税专用发票.getCode() && EnvConst.FANGYUAN.equals(EnvData.config)) ) {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票中.getCode());
        } else {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票成功.getCode());
            invoiceReceiptE.setBillingTime(LocalDateTime.now());
        }
        if (EnvConst.FANGYUAN.equals(EnvData.config) || EnvConst.LINGANG.equals(EnvData.config)) {
            if("Y".equals(command.getBatch())){
                invoiceReceiptE.setRemark(handleRemark(command.getRemark(), command.getSysSource(), billDetailMoreVList, invoiceReceiptE.getType()));
            } else {
                invoiceReceiptE.setRemark(command.getRemark());
            }
        } else if (EnvConst.NIANHUAWAN.equals(EnvData.config)) {
            invoiceReceiptE.setRemark(handleNhwRemark(command.getRemark(), command.getSysSource(), billDetailMoreVList));
        } else {
            invoiceReceiptE.setRemark(handleRemark(command.getRemark(), command.getSysSource(), billDetailMoreVList, invoiceReceiptE.getType()));
        }
        invoiceReceiptE.setCallBackUrl(command.getCallBackUrl());
        if (command.getSysSource() == SysSourceEnum.收费系统.getCode()) {
            invoiceReceiptE.setInvRecUnitName(command.getInvRecUnitName());
            if(StringUtils.isBlank(command.getInvRecUnitName())){
                invoiceReceiptE.setInvRecUnitId(String.valueOf(billDetailMoreV.getStatutoryBodyId()));
                invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getStatutoryBodyName());
            }
        } else if (command.getSysSource() == SysSourceEnum.合同系统.getCode()) {
            if (billDetailMoreV.getCostCenterId() != null) {
                invoiceReceiptE.setInvRecUnitId(billDetailMoreV.getCostCenterId().toString());
            }
            invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getCostCenterName());
        } else {
            // 其余系统默认与收费系统一致
            invoiceReceiptE.setInvRecUnitName(command.getInvRecUnitName());
            if(StringUtils.isBlank(command.getInvRecUnitName())){
                invoiceReceiptE.setInvRecUnitId(String.valueOf(billDetailMoreV.getStatutoryBodyId()));
                invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getStatutoryBodyName());
            }
        }
        return invoiceReceiptE;
    }

    /** 构造拈花湾的备注
     * 【房号】 goodsName: 账单周期start-end;
     * @param remark
     * @param sysSource
     * @param billDetailMoreV
     * @return
     */
    private String handleNhwRemark(String remark, Integer sysSource, List<BillDetailMoreV> billDetailMoreV) {
        List<String> parkingSpace = List.of("parking_space", "parking_garage");
        /*Map<String, List<BillDetailMoreV>> billDetailMoreVMap = billDetailMoreV.stream()
                .filter(t -> t.getTypeNameFlag()!=null&&!parkingSpace.contains(t.getTypeNameFlag()))
                .collect(Collectors.groupingBy(t -> t.getRoomId() + "-" + (StringUtils.isBlank(t.getGoodsName())?t.getChargeItemName():t.getGoodsName())
                , TreeMap::new, Collectors.toList()));*/
        Map<String, List<BillDetailMoreV>> billDetailMoreVMap = billDetailMoreV.stream()
                .filter(t -> t.getTypeNameFlag()!=null&&!parkingSpace.contains(t.getTypeNameFlag()))
                .collect(Collectors.groupingBy(BillDetailV::getRoomId
                        , TreeMap::new, Collectors.toList()));
        remark = "";
        remark = buildNhwInvoiceRemark(remark, billDetailMoreVMap,false);

        // 车位
        billDetailMoreVMap = billDetailMoreV.stream()
                .filter(t -> t.getTypeNameFlag() != null && parkingSpace.contains(t.getTypeNameFlag()))
                .collect(Collectors.groupingBy(t -> t.getRoomId() + "-" + (StringUtils.isBlank(t.getGoodsName())?t.getChargeItemName():t.getGoodsName())));
        remark = buildNhwInvoiceRemark(remark, billDetailMoreVMap, true);

        remark = remark.substring(0, remark.length() - 1) + "。";
        return remark;
    }

    /** 构建nhw的备注，备注需要按照明细里的顺序排序，先按照房间号分组，再对每个房间下的数据按开票那边的费项+税率进行分组，这样可以达到一致
     *
     * @param remark
     * @param billDetailMoreVMap
     * @param parkingFlag 车位标识，车位不进行此排序操作
     * @return
     */
    @NotNull
    private String buildNhwInvoiceRemark(String remark, Map<String, List<BillDetailMoreV>> billDetailMoreVMap, boolean parkingFlag) {

        for (Map.Entry<String, List<BillDetailMoreV>> entry : billDetailMoreVMap.entrySet()) {
            if (parkingFlag){
                List<BillDetailMoreV> billDetailMoreVList = entry.getValue();
                remark += buildNhwRemark(remark, billDetailMoreVList);
            }else {
                // 按费项分组
                TreeMap<String, List<BillDetailMoreV>> groupByChargeItemId = entry.getValue().stream().collect(
                        Collectors.groupingBy(
                                detail -> detail.getChargeItemId() + "-" + detail.getTaxRate() + "-" + detail.getOverdue(),
                                TreeMap::new,
                                Collectors.toList()
                        ));
                for (Map.Entry<String, List<BillDetailMoreV>> listEntry : groupByChargeItemId.entrySet()) {
                    remark += buildNhwRemark(remark, listEntry.getValue());
                }
            }

        }
        return remark;
    }

    public InvoiceReceiptE getInvoiceReceipt(BlueInvoiceImportT invoiceImport) {
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setInvoiceReceiptNo(invoiceImport.getInvoiceNo());
        invoiceReceiptE.setType(com.wishare.finance.domains.report.enums.InvoiceLineEnum.getCodeByDes(invoiceImport.getInvoiceType()));
        invoiceReceiptE.setBillType(0);
        invoiceReceiptE.setCommunityId(invoiceImport.getCommunityId());
        invoiceReceiptE.setCommunityName(invoiceImport.getCommunityName());
        invoiceReceiptE.setCustomerId(null);
        invoiceReceiptE.setCustomerName(null);
        invoiceReceiptE.setCustomerPhone(null);
        invoiceReceiptE.setStatutoryBodyId(null);
        invoiceReceiptE.setStatutoryBodyName(null);
        invoiceReceiptE.setCostCenterId(null);
        invoiceReceiptE.setCostCenterName(null);
        invoiceReceiptE.setApplyTime(invoiceImport.getBillingTime());
        invoiceReceiptE.setPriceTaxAmount(AmountUtils.toLong(invoiceImport.getPriceTaxAmount()));
        invoiceReceiptE.setClerk(invoiceImport.getClerk());
        invoiceReceiptE.setSysSource(SysSourceEnum.未知系统.getCode());
        invoiceReceiptE.setSource(InvoiceReceiptSourceEnum.系统导入.getCode());
        invoiceReceiptE.setInvSource(1);
        invoiceReceiptE.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptE.setAppId(null);
        invoiceReceiptE.setAppName(null);
        invoiceReceiptE.setGatherBillId(null);
        invoiceReceiptE.setCostCenterId(null);
        invoiceReceiptE.setCostCenterName(null);
        invoiceReceiptE.setExtendFieldOne(null);
        // D6937 定额发票不带导入时间需要设置为未开票
        if (InvoiceLineEnum.定额发票.getCode().equals(invoiceReceiptE.getType()) && invoiceImport.getBillingTime() == null){
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.未开票.getCode());
        }else {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票成功.getCode());
        }
        invoiceReceiptE.setBillingTime(invoiceImport.getBillingTime());
        invoiceReceiptE.setRemark(invoiceImport.getRemark());
        invoiceReceiptE.setInvRecUnitId(null);
        invoiceReceiptE.setInvRecUnitName(null);
        return invoiceReceiptE;
    }

    /**
     * 处理开票的备注
     *
     * @param remark
     * @param sysSource
     * @param billDetailMoreV
     * @return
     */
    public String handleRemark(String remark, Integer sysSource, List<BillDetailMoreV> billDetailMoreV, Integer invoiceType) {
        SysSourceEnum sysSourceEnum = SysSourceEnum.valueOfByCode(sysSource);
        if (StringUtils.isBlank(remark)) {
            remark = "";
        } else {
            return remark;
        }
        switch (sysSourceEnum) {
            //电子发票备注中，只用保留项目名称、房号、账单周期，去除收费方式。
            case 合同系统:
                break;
            default:
                Map<String, List<BillDetailMoreV>> billDetailMoreVMap = billDetailMoreV.stream().collect(Collectors.groupingBy(BillDetailMoreV::getRoomId));
                for (Map.Entry<String, List<BillDetailMoreV>> entry : billDetailMoreVMap.entrySet()) {
                    List<BillDetailMoreV> billDetailMoreVList = entry.getValue();
                    remark = handeRemark(remark,billDetailMoreVList);
                }
        }
        if (EnvConst.HUIXIANGYUN.equals(EnvData.config) || EnvConst.YUANYANG.equals(EnvData.config)) {
            if ((InvoiceLineEnum.增值税电子发票.equalsByCode(invoiceType) || InvoiceLineEnum.增值税电子专票.equalsByCode(invoiceType)) && remark.length() >= 90) {
                remark = remark.substring(0, 90);
            } else if ((InvoiceLineEnum.全电普票.equalsByCode(invoiceType) || InvoiceLineEnum.全电专票.equalsByCode(invoiceType)) && remark.length() >= 200) {
                remark = remark.substring(0, 200);
            }
        }
        return remark;
    }

    /**
     * 分组处理备注
     * 构造拈花湾的备注
     * 【房号】 goodsName: 账单周期start-end;
     *
     * @param remark
     * @param billDetailMoreVList
     * @return
     */
    private String buildNhwRemark(String remark, List<BillDetailMoreV> billDetailMoreVList) {
        List<BillStartEndTimeDTO> startEndTimeDateTime = Lists.newArrayList();
        StringBuffer sb=new StringBuffer();
        String communityName = "";
        String roomName =  "";
        String goodsName =  "";
        for (BillDetailMoreV detailMoreV : billDetailMoreVList) {
            communityName = detailMoreV.getCommunityName();
            roomName = detailMoreV.getCpUnitName();
            goodsName = StringUtils.isBlank(detailMoreV.getGoodsName())?detailMoreV.getChargeItemName():detailMoreV.getGoodsName();
            //计费周期分组去重体现
            BillStartEndTimeDTO startEndTimeDTO = new BillStartEndTimeDTO();
            if (Objects.nonNull(detailMoreV.getStartTime()) && Objects.nonNull(detailMoreV.getEndTime())) {
                startEndTimeDTO.setStartTime(detailMoreV.getStartTime());
                startEndTimeDTO.setEndTime(detailMoreV.getEndTime());
                startEndTimeDTO.setChargeTime(detailMoreV.getChargeTime());
                startEndTimeDTO.setPayTime(detailMoreV.getPayTime());
                startEndTimeDTO.setBillMethod(detailMoreV.getBillMethod());
                startEndTimeDateTime.add(startEndTimeDTO);
            }
        }

        if (CollectionUtils.isNotEmpty(startEndTimeDateTime)) {
            ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto();
            List<BillStartEndTimeDTO> billStartEndTimeDTOS = receiptDetailDto.handleAndMergeStartEndTimeDate(startEndTimeDateTime);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            for (int i = 0; i < billStartEndTimeDTOS.size(); i++) {
                BillStartEndTimeDTO timeDTO = billStartEndTimeDTOS.get(i);
                String roomNameTitle= "【" + communityName + "-" + roomName + "】";
                if (!remark.contains(roomNameTitle)) {
                    sb.append(roomNameTitle);
                }
                sb.append(" ").append(goodsName).append(": ");
                sb.append(timeDTO.getStartTime().format(dateTimeFormatter)).append("-").append(timeDTO.getEndTime().format(dateTimeFormatter));
                // 当只有一个元素或最后一个元素时，直接拼接句号
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * 分组处理备注
     *
     * @param remark
     * @param billDetailMoreVList
     * @return
     */
    private String handeRemark(String remark, List<BillDetailMoreV> billDetailMoreVList) {
        List<String> communityNames = Lists.newArrayList();
        List<String> roomNames = Lists.newArrayList();
        List<BillStartEndTimeDTO> startEndTimeDateTime = Lists.newArrayList();
        for (BillDetailMoreV detailMoreV : billDetailMoreVList) {
            //收费系统的开票备注为“项目名称”、“房号”、“费用期间”.

            //计费周期分组去重体现
            BillStartEndTimeDTO startEndTimeDTO = new BillStartEndTimeDTO();
            if (Objects.nonNull(detailMoreV.getStartTime()) && Objects.nonNull(detailMoreV.getEndTime())) {
                startEndTimeDTO.setStartTime(detailMoreV.getStartTime());
                startEndTimeDTO.setEndTime(detailMoreV.getEndTime());
                startEndTimeDTO.setChargeTime(detailMoreV.getChargeTime());
                startEndTimeDTO.setPayTime(detailMoreV.getPayTime());
                startEndTimeDTO.setBillMethod(detailMoreV.getBillMethod());
                startEndTimeDateTime.add(startEndTimeDTO);
            }

            communityNames.add(detailMoreV.getCommunityName());
            roomNames.add(detailMoreV.getRoomName());
        }
        if (CollectionUtils.isNotEmpty(communityNames)) {
            communityNames = communityNames.stream().distinct().collect(Collectors.toList());
            String communityNameRemark = StringUtils.join(communityNames, ",");
            remark = remark  + communityNameRemark + ";";
        }
        if (CollectionUtils.isNotEmpty(roomNames)) {
            roomNames = roomNames.stream().distinct().collect(Collectors.toList());
            String roomNameStr = StringUtils.join(roomNames, ",");
            remark = remark  + roomNameStr + ";";
        }
        if (CollectionUtils.isNotEmpty(startEndTimeDateTime)) {
            ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto();
            List<BillStartEndTimeDTO> billStartEndTimeDTOS = receiptDetailDto.handleAndMergeStartEndTimeDate(startEndTimeDateTime);
            if (CollectionUtils.isNotEmpty(billStartEndTimeDTOS)) {
                remark = remark + "费用期间：" + receiptDetailDto.getBillingCycleRemark(billStartEndTimeDTOS);
            }
        }
        return remark;
    }

    /**
     * 构建发票表信息
     *
     * @return
     */
    public InvoiceE getInvoice(AddInvoiceCommand command, Long invoiceReceiptId, OrgFinanceRv orgFinanceRv) {
        InvoiceE invoiceE = new InvoiceE();
        invoiceE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
        invoiceE.setInvoiceType(command.getInvoiceType());
        invoiceE.setInvoiceReceiptId(invoiceReceiptId);
        invoiceE.setInvoiceTitleType(command.getInvoiceTitleType());
        invoiceE.setBuyerName(command.getBuyerName());
        invoiceE.setBuyerTaxNum(command.getBuyerTaxNum());
        invoiceE.setBuyerTel(command.getBuyerTel());
        invoiceE.setBuyerAddress(command.getBuyerAddress());
        invoiceE.setBuyerAccount(command.getBuyerAccount());
        invoiceE.setTaxpayerType(orgFinanceRv.getTaxpayerType());
        if (command.getInvoiceTitleType() == InvoiceTitleTypeEnum.企业.getCode()) {
            if (StringUtils.isBlank(command.getSalerTaxNum()) && StringUtils.isBlank(orgFinanceRv.getTaxpayerNo())) {
                throw BizException.throw400("该法定单位的税号为空，请补充");
            }
            if (StringUtils.isBlank(command.getSalerTel()) && StringUtils.isBlank(orgFinanceRv.getMobile())) {
                throw BizException.throw400("该法定单位的联系方式为空，请补充");
            }
            if (StringUtils.isBlank(command.getSalerAddress()) && StringUtils.isBlank(orgFinanceRv.getAddress())) {
                throw BizException.throw400("该法定单位的地址为空，请补充");
            }
        }
        invoiceE.setMachineCode(command.getMachineCode());
        invoiceE.setExtensionNumber(command.getExtensionNumber());
        invoiceE.setTerminalNumber(command.getTerminalNumber());
        invoiceE.setTerminalCode(command.getTerminalCode());
        invoiceE.setUserCode(command.getUserCode());
        invoiceE.setSalerTaxNum(StringUtils.isBlank(command.getSalerTaxNum()) ? orgFinanceRv.getTaxpayerNo() : command.getSalerTaxNum());
        invoiceE.setSalerTel(StringUtils.isBlank(command.getSalerTel()) ? orgFinanceRv.getMobile() : command.getSalerTel());
        invoiceE.setSalerAddress(StringUtils.isBlank(command.getSalerAddress()) ? orgFinanceRv.getAddress() : command.getSalerAddress());
        invoiceE.setSalerAccount(command.getSalerAccount());
        invoiceE.setPushMode(JSON.toJSONString(command.getPushMode()));
        invoiceE.setBuyerPhone(command.getBuyerPhone());
        invoiceE.setEmail(command.getEmail());
        invoiceE.setFreeTax(command.getFreeTax());
        if (command.getInvoiceType() == InvoiceTypeEnum.蓝票.getCode()) {
            if (command.getType() == InvoiceLineEnum.增值税电子专票.getCode() || command.getType() == InvoiceLineEnum.增值税电子发票.getCode()) {

            } else {
                invoiceE.setInvoiceCode(command.getInvoiceCode());
                invoiceE.setInvoiceNo(command.getInvoiceNo());
            }
        } else if (command.getInvoiceType() == InvoiceTypeEnum.红票.getCode()) {
        }
        return invoiceE;
    }

    public InvoiceE getInvoice(BlueInvoiceImportT invoiceImport, Long invoiceReceiptId) {
        InvoiceE invoiceE = new InvoiceE();
        invoiceE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
        invoiceE.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
        invoiceE.setInvoiceReceiptId(invoiceReceiptId);
        invoiceE.setInvoiceTitleType(InvoiceTitleTypeEnum.getCodeByDes(invoiceImport.getInvoiceTitleType()));
        invoiceE.setBuyerName(invoiceImport.getBuyerName());
        invoiceE.setBuyerTaxNum(invoiceImport.getBuyerTaxNum());
        invoiceE.setBuyerTel(invoiceImport.getBuyerTel());
        invoiceE.setBuyerAddress(invoiceImport.getBuyerAddress());
        invoiceE.setBuyerAccount(invoiceImport.getBuyerAccount());
        invoiceE.setSalerTaxNum(invoiceImport.getSalerTaxNum());
        invoiceE.setSalerTel(invoiceImport.getSalerTel());
        invoiceE.setSalerAddress(invoiceImport.getSalerAddress());
        invoiceE.setSalerAccount(invoiceImport.getSalerAccount());
        invoiceE.setPushMode(JSON.toJSONString(Lists.newArrayList(0)));
        invoiceE.setBuyerPhone(invoiceImport.getBuyerTel());
        invoiceE.setEmail(null);
        invoiceE.setFreeTax(0);
        invoiceE.setInvoiceCode(invoiceImport.getInvoiceCode());
        invoiceE.setInvoiceNo(invoiceImport.getInvoiceNo());
        invoiceE.setTaxAmount(AmountUtils.toLong(invoiceImport.getTaxAmount()));
        invoiceE.setNuonuoUrl(invoiceImport.getPdfUrl());
        return invoiceE;
    }

    /**
     * 构建发票明细数据
     *
     * @return
     */
    /*public List<InvoiceReceiptDetailE> getInvoiceReceiptDetail(AddInvoiceCommand command, Long invoiceReceiptId, List<BillDetailMoreV> billDetailMoreVList) {
        List<InvoiceReceiptDetailE> list = Lists.newArrayList();
        Map<Long, List<InvoiceBillAmount>> invoiceAmountMap = null;
        if (CollectionUtils.isNotEmpty(command.getInvoiceBillAmounts())) {
            invoiceAmountMap = command.getInvoiceBillAmounts().stream().collect(Collectors.groupingBy(InvoiceBillAmount::getBillId));
        }
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
            invoiceReceiptDetailE.setInvoiceReceiptId(invoiceReceiptId);
            if(StringUtils.isNotBlank(billDetailMoreV.getGoodsName())){
                invoiceReceiptDetailE.setGoodsName(billDetailMoreV.getGoodsName());
            }else{
                invoiceReceiptDetailE.setGoodsName(billDetailMoreV.getChargeItemName());
            }
            invoiceReceiptDetailE.setNum(null);
            invoiceReceiptDetailE.setTaxRate(null == billDetailMoreV.getTaxRate()? "0.00000" :billDetailMoreV.getTaxRate().toString());
            invoiceReceiptDetailE.setUnit(null == billDetailMoreV.getBillMethod() ? null : billDetailMoreV.getBillMethod().toString());// 单位
            invoiceReceiptDetailE.setPrice(null == billDetailMoreV.getUnitPrice() ? null : billDetailMoreV.getUnitPrice().toString());
            invoiceReceiptDetailE.setWithTaxFlag(1);
            invoiceReceiptDetailE.setBillId(billDetailMoreV.getBillId());
            invoiceReceiptDetailE.setBillNo(billDetailMoreV.getBillNo());
//            invoiceReceiptDetailE.setGatherBillId(billDetailMoreV.getGatherBillId());
//            invoiceReceiptDetailE.setGatherBillNo(billDetailMoreV.getGatherBillNo());
//            invoiceReceiptDetailE.setGatherDetailId(billDetailMoreV.getGatherDetailId());
            invoiceReceiptDetailE.setBillType(Integer.parseInt(billDetailMoreV.getBillType()));
            invoiceReceiptDetailE.setBillStartTime(billDetailMoreV.getStartTime());
            invoiceReceiptDetailE.setBillEndTime(billDetailMoreV.getEndTime());
            if (CollectionUtils.isNotEmpty(command.getInvoiceBillAmounts())) {
                //校检剩余开票金额
                InvoiceBillAmount invoiceBillAmount = invoiceAmountMap.get(billDetailMoreV.getBillId()).get(0);
                checkBillCanInvoiceAmount(billDetailMoreV,invoiceBillAmount.getInvoiceAmount());
                invoiceReceiptDetailE.setInvoiceAmount(invoiceBillAmount.getInvoiceAmount());
            }else {
                invoiceReceiptDetailE.setInvoiceAmount(billDetailMoreV.getCanInvoiceAmount());
            }
            invoiceReceiptDetailE.setPriceTaxAmount(invoiceReceiptDetailE.getInvoiceAmount());
            invoiceReceiptDetailE.setSettleAmount(billDetailMoreV.getSettleAmount());
            invoiceReceiptDetailE.setRoomId(billDetailMoreV.getRoomId());
            invoiceReceiptDetailE.setRoomName(billDetailMoreV.getRoomName());
            invoiceReceiptDetailE.setChargeItemId(billDetailMoreV.getChargeItemId());
            invoiceReceiptDetailE.setChargeItemName(billDetailMoreV.getChargeItemName());
            list.add(invoiceReceiptDetailE);
        }
        return list;
    }*/

    /**
     * 构建发票明细数据
     *
     * @return
     */
    public List<InvoiceReceiptDetailE> getInvoiceReceiptDetail(AddInvoiceCommand command, Long invoiceReceiptId, List<BillDetailMoreV> billDetailMoreVList) {
        List<InvoiceReceiptDetailE> list = Lists.newArrayList();
        Map<Long, List<InvoiceBillAmount>> invoiceAmountMap = null;
        if (CollectionUtils.isNotEmpty(command.getInvoiceBillAmounts())) {
            invoiceAmountMap = command.getInvoiceBillAmounts().stream().collect(Collectors.groupingBy(InvoiceBillAmount::getBillId));
        }
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
            invoiceReceiptDetailE.setInvoiceReceiptId(invoiceReceiptId);
            if(StringUtils.isNotBlank(billDetailMoreV.getGoodsName())){
                invoiceReceiptDetailE.setGoodsName(billDetailMoreV.getGoodsName());
            }else{
                invoiceReceiptDetailE.setGoodsName(billDetailMoreV.getChargeItemName());
            }
            invoiceReceiptDetailE.setOverdue(billDetailMoreV.getOverdue());
            invoiceReceiptDetailE.setNum(null);
            invoiceReceiptDetailE.setTaxRate(null == billDetailMoreV.getTaxRate()? "0.00000" :billDetailMoreV.getTaxRate().toString());
            invoiceReceiptDetailE.setUnit(null == billDetailMoreV.getBillMethod() ? null : billDetailMoreV.getBillMethod().toString());//todo 单位
            invoiceReceiptDetailE.setPrice(null == billDetailMoreV.getUnitPrice() ? null : billDetailMoreV.getUnitPrice().toString());
            invoiceReceiptDetailE.setWithTaxFlag(1);
            invoiceReceiptDetailE.setBillId(billDetailMoreV.getBillId());
            invoiceReceiptDetailE.setBillNo(billDetailMoreV.getBillNo());
            invoiceReceiptDetailE.setGatherBillId(billDetailMoreV.getGatherBillId());
            invoiceReceiptDetailE.setGatherBillNo(billDetailMoreV.getGatherBillNo());
            invoiceReceiptDetailE.setGatherDetailId(billDetailMoreV.getGatherDetailId());
            invoiceReceiptDetailE.setBillType(Integer.parseInt(billDetailMoreV.getBillType()));
            invoiceReceiptDetailE.setBillStartTime(billDetailMoreV.getStartTime());
            invoiceReceiptDetailE.setBillEndTime(billDetailMoreV.getEndTime());
            // 具体明细金额不由前端控制
            /*if (CollectionUtils.isNotEmpty(command.getInvoiceBillAmounts())) {
                //校检剩余开票金额
                InvoiceBillAmount invoiceBillAmount = invoiceAmountMap.get(billDetailMoreV.getBillId()).get(0);
                checkBillCanInvoiceAmount(billDetailMoreV,invoiceBillAmount.getInvoiceAmount());
                invoiceReceiptDetailE.setInvoiceAmount(invoiceBillAmount.getInvoiceAmount());
            }else {
            }*/
            invoiceReceiptDetailE.setInvoiceAmount(billDetailMoreV.getCanInvoiceAmount());
            invoiceReceiptDetailE.setPriceTaxAmount(invoiceReceiptDetailE.getInvoiceAmount());
            invoiceReceiptDetailE.setSettleAmount(billDetailMoreV.getSettleAmount());
            invoiceReceiptDetailE.setRoomId(billDetailMoreV.getRoomId());
            invoiceReceiptDetailE.setRoomName(billDetailMoreV.getRoomName());
            invoiceReceiptDetailE.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(invoiceReceiptDetailE.getRoomId()));
            invoiceReceiptDetailE.setChargeItemId(billDetailMoreV.getChargeItemId());
            invoiceReceiptDetailE.setChargeItemName(billDetailMoreV.getChargeItemName());
            invoiceReceiptDetailE.setPayerType(billDetailMoreV.getPayerType());
            setDiscountInfo(invoiceReceiptDetailE, billDetailMoreV);
            list.add(invoiceReceiptDetailE);
        }
        return list;
    }

    /**
     * 设置折扣信息
     * @param invoiceReceiptDetailE
     * @param billDetailMoreV
     */
    public void setDiscountInfo(InvoiceReceiptDetailE invoiceReceiptDetailE, BillDetailMoreV billDetailMoreV) {
        // 方圆逻辑：预收账单有减免信息的话，开折扣发票
        if (EnvConst.FANGYUAN.equals(EnvData.config)) {
            if (BillTypeEnum.预收账单.equalsByCode(invoiceReceiptDetailE.getBillType())) {
                invoiceReceiptDetailE.setDiscountAmount(billDetailMoreV.getDeductibleAmount());
            }
        }
    }

    /**
     * 校检账单的可开票金额
     *
     * @param billDetailMoreV
     * @param invoiceBillAmount
     */
    private void checkBillCanInvoiceAmount(BillDetailMoreV billDetailMoreV, Long invoiceBillAmount) {
        Long canInvoiceAmount = 0L;
        if (billDetailMoreV.getSettleState() == BillSettleStateEnum.未结算.getCode()) {
            canInvoiceAmount = billDetailMoreV.getReceivableAmount() - billDetailMoreV.getDiscountAmount()
                    - billDetailMoreV.getInvoiceAmount();
        } else {
            canInvoiceAmount = billDetailMoreV.getActualPayAmount() - billDetailMoreV.getInvoiceAmount();
        }
        if (canInvoiceAmount.longValue() < invoiceBillAmount.longValue()) {
            throw BizException.throw400("当前账单开票金额异常，可开票金额为 "+canInvoiceAmount/100 + " 实际开票金额为：" + invoiceBillAmount/100);
        }
    }

}
