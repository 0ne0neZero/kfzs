package com.wishare.finance.domains.invoicereceipt.aggregate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.model.invoice.invoice.dto.OtherAmountDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBillAmount;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.AfterPaymentEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceClaimStatusEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.PushStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.BillSettleStateEnum;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/12
 * @Description:
 */
@Getter
@Setter
@ApiModel("收据聚合")
public class ReceiptA {

    /**
     * 收据主表信息
     */
    private InvoiceReceiptE invoiceReceiptE;

    /**
     * 收据表信息
     */
    private ReceiptE receiptE;

    /**
     * 收据明细表信息
     */
    private List<InvoiceReceiptDetailE> invoiceReceiptDetailEList;

    /**
     * 构建收据聚合
     *
     * @param command
     * @param billDetailMoreVList
     */
    public ReceiptA(AddReceiptCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        invoiceReceiptE = getInvoiceReceipt(command,billDetailMoreVList);
        receiptE = getInvoice(command,billDetailMoreVList);
        invoiceReceiptDetailEList = getInvoiceReceiptDetail(command,billDetailMoreVList);
        invoiceReceiptE.verify(invoiceReceiptDetailEList);
    }



    /**
     * 构建收据聚合信息
     *
     * @param invoiceReceiptE
     * @param receiptE
     * @param invoiceReceiptDetailES
     */
    public ReceiptA(InvoiceReceiptE invoiceReceiptE, ReceiptE receiptE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        if (null == invoiceReceiptE) {
            throw BizException.throw400("该收据不存在");
        }
        this.invoiceReceiptE = invoiceReceiptE;
        this.receiptE = receiptE;
        this.invoiceReceiptDetailEList = invoiceReceiptDetailES;
        invoiceReceiptE.verify(invoiceReceiptDetailEList);
    }

    /**
     * 构建收据明细表数据
     *
     * @return
     */
    private List<InvoiceReceiptDetailE> getInvoiceReceiptDetail(AddReceiptCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        List<InvoiceReceiptDetailE> list = Lists.newArrayList();
        Map<Long, List<InvoiceBillAmount>> invoiceAmountMap = null;
        if (CollectionUtils.isNotEmpty(command.getInvoiceBillAmounts())) {
            invoiceAmountMap = command.getInvoiceBillAmounts().stream().collect(Collectors.groupingBy(InvoiceBillAmount::getBillId));
        }
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
            invoiceReceiptDetailE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_detail_id"));
            invoiceReceiptDetailE.setInvoiceReceiptId(this.getInvoiceReceiptE().getId());
            invoiceReceiptDetailE.setGoodsName(billDetailMoreV.getChargeItemName());
            invoiceReceiptDetailE.setNum(null);

            invoiceReceiptDetailE.setTaxRate(null == billDetailMoreV.getTaxRate() ? null : billDetailMoreV.getTaxRate().toString());
            invoiceReceiptDetailE.setUnit(null == billDetailMoreV.getBillMethod() ? null : billDetailMoreV.getBillMethod().toString());//todo 单位
            invoiceReceiptDetailE.setPrice(null == billDetailMoreV.getUnitPrice() ? null : billDetailMoreV.getUnitPrice().toString());
            invoiceReceiptDetailE.setWithTaxFlag(1);
            invoiceReceiptDetailE.setBillId(billDetailMoreV.getBillId());
            invoiceReceiptDetailE.setBillNo(billDetailMoreV.getBillNo());
            invoiceReceiptDetailE.setGatherBillId(billDetailMoreV.getGatherBillId());
            invoiceReceiptDetailE.setGatherBillNo(billDetailMoreV.getGatherBillNo());
            invoiceReceiptDetailE.setGatherDetailId(billDetailMoreV.getGatherDetailId());
            invoiceReceiptDetailE.setBillType(Integer.valueOf(billDetailMoreV.getBillType()));
            invoiceReceiptDetailE.setBillStartTime(billDetailMoreV.getStartTime());
            invoiceReceiptDetailE.setBillEndTime(billDetailMoreV.getEndTime());
            // 设置账单的缴费日期
            invoiceReceiptDetailE.setBillPayTime(billDetailMoreV.getPayTime());
            Long priceTaxAmount = command.getPriceTaxAmount();
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
            invoiceReceiptDetailE.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(invoiceReceiptDetailE.getRoomId()));
            invoiceReceiptDetailE.setRoomName(billDetailMoreV.getRoomName());
            invoiceReceiptDetailE.setChargeItemId(billDetailMoreV.getChargeItemId());
            invoiceReceiptDetailE.setChargeItemName(billDetailMoreV.getChargeItemName());
            list.add(invoiceReceiptDetailE);
        }
        return list;
    }

    /**
     * 构建收据主表数据
     *
     * @return
     */
    private ReceiptE getInvoice(AddReceiptCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(IdentifierFactory.getInstance().generateLongIdentifier("receipt_id"));
        receiptE.setInvoiceReceiptId(this.getInvoiceReceiptE().getId());
        receiptE.setReceiptNo(command.getReceiptNo());
        receiptE.setPaymentTime(null);
        receiptE.setPaymentType(handleSettleWayChannelStr(billDetailMoreVList,this.getInvoiceReceiptE().getPriceTaxAmount()));
        receiptE.setStampUrl(command.getStampUrl());
        receiptE.setDiscountInfo(command.getDiscountInfo());
        receiptE.setSignStatus(command.getSignStatus());
        return receiptE;
    }

    /**
     * 构建收据主表
     *
     * @return
     */
    private InvoiceReceiptE getInvoiceReceipt(AddReceiptCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        //收据号
        if (EnvConst.NIANHUAWAN.equals(EnvData.config)&&StringUtils.isNotBlank(command.getInvoiceReceiptNo())){
            invoiceReceiptE.setInvoiceReceiptNo(command.getInvoiceReceiptNo());
        }else {
            invoiceReceiptE.setInvoiceReceiptNo(Objects.isNull(command.getReceiptNo()) ?
                    IdentifierFactory.getInstance().serialNumber("invoice_no", "SJ", 20) :
                    command.getReceiptNo().toString());
        }

        invoiceReceiptE.setType(command.getType());
        invoiceReceiptE.setBillType(command.getBillType());

        BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
        // 设置是否是缴费后开具，对应账单的缴费日期有值时为缴费后开具
        invoiceReceiptE.setAfterPayment(AfterPaymentEnum.直接开具.getCode());
        for (BillDetailMoreV detailMoreV : billDetailMoreVList) {
            if (Objects.nonNull(detailMoreV.getPayTime())) {
                invoiceReceiptE.setAfterPayment(AfterPaymentEnum.缴费后开具.getCode());
            }
        }

        invoiceReceiptE.setCommunityId(billDetailMoreV.getCommunityId());
        invoiceReceiptE.setCommunityName(billDetailMoreV.getCommunityName());
        //付款方ID
        if (StringUtils.isNotBlank(billDetailMoreV.getPayerId())) {
            invoiceReceiptE.setCustomerId(billDetailMoreV.getPayerId());
            invoiceReceiptE.setCustomerName(billDetailMoreV.getPayerName());
            invoiceReceiptE.setCustomerPhone(null);
        }
        invoiceReceiptE.setCostCenterId(billDetailMoreV.getCostCenterId());
        invoiceReceiptE.setCostCenterName(billDetailMoreV.getCostCenterName());
        invoiceReceiptE.setStatutoryBodyId(billDetailMoreV.getStatutoryBodyId());
        invoiceReceiptE.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        invoiceReceiptE.setApplyTime(LocalDateTime.now());
        invoiceReceiptE.setPriceTaxAmount(command.getPriceTaxAmount());
        invoiceReceiptE.setClerk(command.getClerk());
        invoiceReceiptE.setPushMode(JSON.toJSONString(command.getPushMode()));
        invoiceReceiptE.setBuyerPhone(command.getBuyerPhone());
        invoiceReceiptE.setEmail(command.getEmail());
        invoiceReceiptE.setPushState(PushStateEnum.未推送.getCode());
        // invoiceReceiptE.setRemark(handleRemark(billDetailMoreVList, null));
        invoiceReceiptE.setSysSource(Objects.nonNull(billDetailMoreV.getSysSource()) ?
                billDetailMoreV.getSysSource() : command.getSysSource());
        invoiceReceiptE.setInvSource(command.getInvSource());
        invoiceReceiptE.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票成功.getCode());
        invoiceReceiptE.setBillingTime(LocalDateTime.now());
        invoiceReceiptE.setAppId(billDetailMoreV.getAppId());
        invoiceReceiptE.setAppName(billDetailMoreV.getAppName());
        invoiceReceiptE.setReceiptTemplateId(command.getReceiptTemplateId());
        invoiceReceiptE.setReceiptTemplateName(command.getReceiptTemplateName());
        if (command.getSysSource() == SysSourceEnum.收费系统.getCode()) {
            invoiceReceiptE.setInvRecUnitId(billDetailMoreV.getCommunityId());
            invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getCommunityName());
        } else if (command.getSysSource() == SysSourceEnum.合同系统.getCode()) {
            if (billDetailMoreV.getCostCenterId() != null) {
                invoiceReceiptE.setInvRecUnitId(billDetailMoreV.getCostCenterId().toString());
            }
            invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getCostCenterName());
        } else {
            // 默认按照收费系统处理
            invoiceReceiptE.setInvRecUnitId(billDetailMoreV.getCommunityId());
            invoiceReceiptE.setInvRecUnitName(billDetailMoreV.getCommunityName());
        }
        return invoiceReceiptE;
    }


    /**
     *
     * @param billDetailMoreVList
     * @param receiptDetailDto
     * @param payChannel
     * @return
     */
    public String handleRemarkOpen(List<BillDetailMoreV> billDetailMoreVList, ReceiptDetailDto receiptDetailDto,String payChannel){
        return this.handleRemark(billDetailMoreVList,receiptDetailDto,payChannel);
    }



    /**
     * 处理收费描述
     * <p>
     * 【房号】业主本次应缴费【300.00】元，因【优惠/抵扣/减免】减少【30.00】元， 【优惠/抵扣/减免】凭证号为【2345432】，实际缴费【340.00】元，存入预收【70.00】元
     *
     * @param billDetailMoreVList
     * @param receiptDetailDto
     * @return
     */
    private String handleRemark(List<BillDetailMoreV> billDetailMoreVList, ReceiptDetailDto receiptDetailDto,String payChannel) {
        //BigDecimal totalAmountSum = BigDecimal.ZERO; //账单金额
        BigDecimal receivableAmountSum = BigDecimal.ZERO; //账单应收金额
        BigDecimal deductibleAmount = BigDecimal.ZERO; //减免金额 --> 应收减免 + 实收减免
        BigDecimal actualPayAmountSum = BigDecimal.ZERO; //实收金额
        String voucherSum = "";
        String discountSum = "";
        List<String> roomNames = Lists.newArrayList();
        Set<Long> deductibleBillIdSet = new HashSet<>();
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            roomNames.add(billDetailMoreV.getRoomName());
            //if (billDetailMoreV.getTotalAmount() != null) {
            //    totalAmountSum = totalAmountSum.add(new BigDecimal(billDetailMoreV.getTotalAmount()));
            //}
            if (billDetailMoreV.getReceivableAmount() != null){
                receivableAmountSum = receivableAmountSum.add(new BigDecimal(billDetailMoreV.getReceivableAmount()));
            }
            if (billDetailMoreV.getActualPayAmount() != null) {
                actualPayAmountSum = actualPayAmountSum.add(new BigDecimal(billDetailMoreV.getActualPayAmount()));
            }
            if (billDetailMoreV.getDeductibleAmount() != null && !deductibleBillIdSet.contains(billDetailMoreV.getBillId())) {
                deductibleAmount = deductibleAmount.add(new BigDecimal(billDetailMoreV.getDeductibleAmount()));
                deductibleBillIdSet.add(billDetailMoreV.getBillId());
            }
            if (billDetailMoreV.getDiscountAmount() != null) {
                deductibleAmount = deductibleAmount.add(new BigDecimal(billDetailMoreV.getDiscountAmount()));
            }
            List<BillAdjustV> billAdjustDtos = billDetailMoreV.getBillAdjustDtos();
            String voucherStr = "";
            if (CollectionUtils.isNotEmpty(billAdjustDtos)) {
                for (BillAdjustV billAdjustDto : billAdjustDtos) {
                    if (StringUtils.isNotBlank(billAdjustDto.getVoucher())) {
                        voucherStr = voucherStr + billAdjustDto.getVoucher();
                    }
                }
            }
            voucherSum = voucherSum + voucherStr;

            List<BillSettleV> billSettleDtos = billDetailMoreV.getBillSettleDtos();
            if (CollectionUtils.isNotEmpty(billSettleDtos)) {
                for (BillSettleV billSettleDto : billSettleDtos) {
                    if (StringUtils.isNotBlank(billSettleDto.getDiscounts())) {
                        List<DiscountOBV> discountOBVS = JSON.parseArray(billSettleDto.getDiscounts(), DiscountOBV.class);
                        if (CollectionUtils.isNotEmpty(discountOBVS)) {
                            //优惠
                            String discount = discountOBVS.stream().map(DiscountOBV::getDescription).collect(Collectors.joining(","));
                            discountSum = discountSum +  discount;
                        }
                    }
                }
            }
        }
        String otherRemark = "";
        if (null != receiptDetailDto) {
            if (receiptDetailDto.getOtherAmountDto() != null) {
                OtherAmountDto dto = receiptDetailDto.getOtherAmountDto().get(0);
                otherRemark = otherRemark + dto.getRemark() + ",";
            }
        }
        String deductibleAmountDes = "";
        if (deductibleAmount != null && deductibleAmount != BigDecimal.ZERO) {
            deductibleAmountDes = "因【优惠/抵扣/减免】减少【" + deductibleAmount.divide(new BigDecimal(100),2, RoundingMode.HALF_UP) + "】元,";
        }
        String voucherDes = "";
        if (StringUtils.isNotBlank(voucherSum)) {
            voucherDes = "【优惠/抵扣/减免】凭证号为【" + voucherSum + "】,";
        }
        String discountDes = "";
        if (StringUtils.isNotBlank(discountSum)) {
            discountDes = "【优惠/抵扣/减免】凭证号为：【" + discountSum +  "】,";
        }
        String roomNameStr = handleRoomName(roomNames);
        if(StringUtils.isNotBlank(roomNameStr)){
            roomNameStr = "【" + roomNameStr + "】业主";
        }else {
            roomNameStr = "客户";
        }
        if (StringUtils.isNotBlank(payChannel)) {
            payChannel = "使用" + payChannel;
        }else {
            payChannel = "";
        }
        String result = roomNameStr + "本次应缴费【" + receivableAmountSum.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) + "】元，" + deductibleAmountDes + discountDes + otherRemark + voucherDes + "实际缴费【" + actualPayAmountSum.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN) + "】元";
        //远洋收费备注定制
        if (EnvConst.YUANYANG.equals(EnvData.config)){
            result = roomNameStr + "本次应缴费【" + receivableAmountSum.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) + "】元，" +
                    "实际" + payChannel + "缴费【" + actualPayAmountSum.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN) + "】元";
        }
        return result;
    }

    /**
     * 去重处理房号
     *
     * @param roomNames
     * @return
     */
    private String handleRoomName(List<String> roomNames) {
        String roomNameStr ="";
        if (CollectionUtils.isNotEmpty(roomNames)) {
            roomNames = roomNames.stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());
            roomNameStr = String.join(",", roomNames);
        }
        return roomNameStr;
    }

    /**
     * 处理结算方式
     *
     * @param billDetailMoreVList
     * @param priceTaxAmount
     * @return
     */
    private String handleSettleWayChannelStr(List<BillDetailMoreV> billDetailMoreVList, Long priceTaxAmount) {
        String setttleWayChannelStr = "";
        if (CollectionUtils.isNotEmpty(billDetailMoreVList)) {
            List<String> setttleWayChannelList = Lists.newArrayList();
            Long settleAmountSum = 0L;
            for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
                List<BillSettleV> billSettleDtos = billDetailMoreV.getBillSettleDtos();
                //按照时间倒序
                billSettleDtos = billSettleDtos.stream().sorted(Comparator.comparing(BillSettleV::getSettleTime).reversed()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(billSettleDtos)) {
                    for (BillSettleV billSettleDto : billSettleDtos) {
                        SettleWayChannelEnum settleWayChannelEnum = SettleWayChannelEnum.valueOfByCode(billSettleDto.getSettleChannel());
                        setttleWayChannelList.add(settleWayChannelEnum.getValue());
                        Long settleAmount = billSettleDto.getSettleAmount();
                        settleAmountSum = settleAmountSum + settleAmount;
                        if (settleAmountSum.longValue() == priceTaxAmount) {
                            break;
                        }
                    }
                }
            }

            //去重
            List<String> setttleWayChannelStrList = setttleWayChannelList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(setttleWayChannelStrList)) {
                for (String str : setttleWayChannelStrList) {
                    setttleWayChannelStr = setttleWayChannelStr + str + ",";
                }
                if (setttleWayChannelStr.contains(",")){
                    return setttleWayChannelStr.substring(0,setttleWayChannelStr.length()-1);
                }
            }
        }
        return setttleWayChannelStr;
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
