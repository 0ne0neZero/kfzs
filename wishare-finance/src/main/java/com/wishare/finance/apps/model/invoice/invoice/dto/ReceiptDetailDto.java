package com.wishare.finance.apps.model.invoice.invoice.dto;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillMethodEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.dto.BillStartEndTimeDTO;
import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums.MethodEnum;
import com.wishare.finance.infrastructure.utils.ChineseNumber;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/28
 * @Description:
 */
@Getter
@Setter
@Slf4j
@ApiModel("收据详情")
public class ReceiptDetailDto {

    @ApiModelProperty("收据id")
    private Long invoiceReceiptId;

    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
    private Integer sysSource;
    @ApiModelProperty("系统来源描述")
    private String sysSourceStr;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收据编号")
    private String receiptNo;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("缴费方式")
    private String settleWayChannelStr;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("开具时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("缴费日期")
    private LocalDateTime payTime;

    @ApiModelProperty("开票人")
    private String clerk;

    @ApiModelProperty(value = "是否展示开票人(收据pdf文件)")
    private String clerkStatus;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("收款备注")
    private String GatherRemark;

    @ApiModelProperty("票据明细")
    private List<InvoiceReceiptDetailV> invoiceReceiptDetail;

    @ApiModelProperty("合计金额（单位：分）")
    private Long invoiceAmountTotal;

    @ApiModelProperty("合计金额（单位：分）大写中文")
    private String invoiceAmountTotalUppercase;

    @ApiModelProperty("其他金额（单位：分）")
    private List<OtherAmountDto> otherAmountDto;

    @ApiModelProperty("结算时间（年月日时分秒）")
    private List<LocalDateTime> settleTimeList;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败 8.部分红冲")
    private Integer state;

    @ApiModelProperty(value = "票据模板ID")
    private Long receiptTemplateId;

    @ApiModelProperty(value = "票据模板名称")
    private String receiptTemplateName;

    @ApiModelProperty(value = "票据模板")
    private TemplateV receiptTemplate;

    @ApiModelProperty(value = "是否需要签章：0 - 是 1 - 否")
    private Integer signStatus;

    @ApiModelProperty("收款人名称")
    private String payeeName;

    @ApiModelProperty("收款方式")
    private String payChannel;

    @ApiModelProperty("应收金额之和")
    private Long receivableAmountSum;

    @ApiModelProperty("实收金额之和")
    private Long actualPayAmountSum;



    public String getSysSourceStr() {
        if (this.getSysSource() != null) {
            return SysSourceEnum.valueOfByCode(this.getSysSource()).getDes();
        }
        return sysSourceStr;
    }


    public ReceiptDetailDto setPayeeName(String payeeName) {
        this.payeeName = payeeName;
        return this;
    }

    public ReceiptDetailDto setPayChannel(String payChannel) {
        this.payChannel = payChannel;
        return this;
    }

    /**
     * 构建收费系统收据详情
     *
     * @param invoiceReceiptE 收据主表
     * @param billDetailMoreVList    账单详情
     * @param invoiceReceiptDetailES 开票明细表
     * @param templateV 收据模板
     * @return
     */
    public ReceiptDetailDto general(InvoiceReceiptE invoiceReceiptE,
                                    List<BillDetailMoreV> billDetailMoreVList,
                                    List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                    ReceiptE receiptE,
                                    TemplateV templateV,
                                    ReceiptTemplateE receiptTemplateE) {
        ReceiptDetailDto receiptDetailDto = Global.mapperFacade.map(invoiceReceiptE, ReceiptDetailDto.class);
        receiptDetailDto.setInvoiceReceiptId(invoiceReceiptE.getId());
        receiptDetailDto.setReceiptNo(invoiceReceiptE.getInvoiceReceiptNo());
        if(CollectionUtils.isNotEmpty(billDetailMoreVList)) {
//            LocalDateTime newPayTime = billDetailMoreVList.stream().map(BillDetailMoreV::getPayTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
//            receiptDetailDto.setPayTime(newPayTime);
            // 房间超过一间显示 第一间名称+等x间房
            Set<String> roomNameSet = billDetailMoreVList.stream().map(BillDetailMoreV::getRoomName).collect(Collectors.toSet());
            int roomNum = roomNameSet.size();
            if (roomNum > 1) {
                // 如果是中交，使用逗号的方式隔开
                if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
                    receiptDetailDto.setRoomName(StringUtils.join(roomNameSet.toArray(), ","));
                } else {
                    receiptDetailDto.setRoomName(billDetailMoreVList.get(0).getRoomName() + "等" + roomNum + "间房");
                }
            } else {
                receiptDetailDto.setRoomName(billDetailMoreVList.get(0).getRoomName());
            }
            // 收据详情的缴费时间，从收据明细中计算获取
            LocalDateTime newPayTime = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillPayTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
            receiptDetailDto.setPayTime(newPayTime);
            //票据明细
            receiptDetailDto.setInvoiceReceiptDetail(handleInvoiceReceiptDetailVS(invoiceReceiptE,billDetailMoreVList, invoiceReceiptDetailES));
            receiptDetailDto.setReceiptTemplate(templateV);
            receiptDetailDto.setCustomerName(billDetailMoreVList.get(0).getPayerName());
            receiptDetailDto.setOtherAmountDto(handleOtherAmount(billDetailMoreVList));
            receiptDetailDto.setSettleTimeList(handleSettleTime(invoiceReceiptE.getId(), billDetailMoreVList));
            if (EnvConst.YUANYANG.equals(EnvData.config)){
                receiptDetailDto.setGatherRemark(handleGatherRemark(billDetailMoreVList));
            }
        }

        //合计金额
        Long invoiceAmountTotal = invoiceReceiptDetailES.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
        receiptDetailDto.setInvoiceAmountTotal(invoiceAmountTotal);
        double invoiceAmountTotaDouble = (double) invoiceAmountTotal / 100;
        receiptDetailDto.setInvoiceAmountTotalUppercase(ChineseNumber.number2CNMontrayUnit(String.valueOf(invoiceAmountTotaDouble)));
        receiptDetailDto.setCommunityId(invoiceReceiptE.getCommunityId());
        receiptDetailDto.setSettleWayChannelStr(receiptE.getPaymentType());
        //HXYUN-16318,慧享云不要备注
        log.info("ReceiptDetailDto.general.EnvData.config:{}",EnvData.config);
        if (EnvConst.YUANYANG.equals(EnvData.config) || EnvConst.ZHONGJIAO.equals(EnvData.config)){
            receiptDetailDto.setRemark(invoiceReceiptE.getRemark());
        }else if(!EnvConst.HUIXIANGYUN.equals(EnvData.config)){
            receiptDetailDto.setRemark(invoiceReceiptE.getRemark() + handleChargeRemark(billDetailMoreVList));
        }else {
            receiptDetailDto.setRemark(null);
        }
        receiptDetailDto.setSysSource(invoiceReceiptE.getSysSource());
        receiptDetailDto.setState(invoiceReceiptE.getState());
        receiptDetailDto.setPayeeName(invoiceReceiptE.getPayeeName());
        receiptDetailDto.setPayChannel(invoiceReceiptE.getPayChannel());
        receiptDetailDto.setReceivableAmountSum(billDetailMoreVList.stream().mapToLong(BillDetailMoreV::getReceivableAmount).sum());
        receiptDetailDto.setActualPayAmountSum(billDetailMoreVList.stream().mapToLong(BillDetailMoreV::getActualPayAmount).sum());
        return receiptDetailDto;
    }

    public String handleChargeRemark(List<BillDetailMoreV> billDetailMoreVList) {
        StringBuilder chargeRemark = new StringBuilder();
//        List<String> paydetails = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(billDetailMoreVList)) {
//            for (BillDetailMoreV detail : billDetailMoreVList) {
//                if (CollectionUtils.isNotEmpty(detail.getBillSettleDtos())) {
//                    for (BillSettleV settleInfo : detail.getBillSettleDtos()) {
//                        paydetails.add(settleInfo.getSettleWayString() +
//                                SettleChannelEnum.valueNameOfByCode(settleInfo.getSettleChannel()) + "缴纳" +
//                                detail.getChargeItemName() + "【" +
//                                new BigDecimal(settleInfo.getSettleAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) + "】元");
//                    }
////                settleInfos.stream().map(info -> info.getSettleWayString() + info.getSettleChannel() + )
//                }
//            }
//        }
        List<String> chargeRemarkPayDetails = this.getChargeRemarkPayDetails(billDetailMoreVList);
        if (CollectionUtils.isNotEmpty(chargeRemarkPayDetails)) {
            chargeRemark.append("<br/>缴费包含:").append(StringUtils.join(chargeRemarkPayDetails, "，"));
        }
        return chargeRemark.toString();
    }

    public String handleGatherRemark(List<BillDetailMoreV> billDetailMoreVList) {
        String remarks = "";
        List<BillSettleV> billSettleVS = billDetailMoreVList.stream().flatMap(billDetailMoreV -> billDetailMoreV.getBillSettleDtos().stream()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(billSettleVS)) {
             remarks = billSettleVS.stream().map(BillSettleV::getRemark).filter(StringUtils::isNotBlank).collect(Collectors.joining(";"));
        }
        return remarks;
    }

    private List<String> getChargeRemarkPayDetails(List<BillDetailMoreV> billDetailMoreVList) {
        List<String> payDetails = new ArrayList<>();
        if (CollectionUtils.isEmpty(billDetailMoreVList)) {
            return payDetails;
        }
        Map<Long, List<BillDetailMoreV>> billGroupByChargeItem = billDetailMoreVList.stream().collect(Collectors.groupingBy(BillDetailMoreV::getChargeItemId));
        for (Map.Entry<Long, List<BillDetailMoreV>> entry : billGroupByChargeItem.entrySet()) {
            // 同一费项下
            String chargeItemName = entry.getValue().get(0).getChargeItemName();
            List<BillSettleV> settleList = entry.getValue().stream().map(BillDetailMoreV::getBillSettleDtos)
                    .filter(CollectionUtils::isNotEmpty)
                    .flatMap(Collection::stream)
                    .filter(settle -> settle.getAvailable() == 0)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(settleList)) {
                continue;
            }
            // 同一费项的同一支付方式下
            Map<String, List<BillSettleV>> settleGroupByChannel = settleList.stream().collect(Collectors.groupingBy(BillSettleV::getSettleChannel));
            for (Map.Entry<String, List<BillSettleV>> settleEntry : settleGroupByChannel.entrySet()) {
                // 总缴纳费用
                Long totalSettleAmount = settleEntry.getValue().stream().map(BillSettleV::getSettleAmount).filter(Objects::nonNull).reduce(Long::sum).orElse(0L);
                BillSettleV settleV = settleEntry.getValue().get(0);
                payDetails.add(settleV.getSettleWayString() +
                        SettleChannelEnum.valueNameOfByCode(settleV.getSettleChannel()) + "缴纳" +
                        chargeItemName + "【" +
                        new BigDecimal(totalSettleAmount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) + "】元");
            }
        }
        return payDetails;
    }

    /**
     * 处理结算时间
     *
     * @param invocieReceiptId
     * @param billDetailMoreVList
     * @return
     */
    private List<LocalDateTime> handleSettleTime(Long invocieReceiptId, List<BillDetailMoreV> billDetailMoreVList) {
        List<LocalDateTime> settleTimes = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(billDetailMoreVList)) {
            for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
                List<BillSettleV> billSettleDtos = billDetailMoreV.getBillSettleDtos();
                //过滤票据id
                List<BillSettleV> billSettleVS = billSettleDtos.stream().filter(billSettle -> billSettle.getInvoiceReceiptId() != null && billSettle.getInvoiceReceiptId().equals(invocieReceiptId)).collect(Collectors.toList());
                for (BillSettleV billSettleV : billSettleVS) {
                    if (billSettleV.getSettleTime() != null) {
                        settleTimes.add(billSettleV.getSettleTime());
                    }
                }
            }
        }
        return settleTimes;
    }

    /**
     * 处理账单明细
     * 需要根据相同费翔/相同单价/相同计费方式的账单合并展示
     *
     * @param invoiceReceiptE
     * @param billDetailMoreVList
     * @param invoiceReceiptDetailES
     * @return
     */
    private List<InvoiceReceiptDetailV> handleInvoiceReceiptDetailVS(InvoiceReceiptE invoiceReceiptE, List<BillDetailMoreV> billDetailMoreVList, List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        Map<Long, List<InvoiceReceiptDetailE>> invoiceReceiptDetailMap = invoiceReceiptDetailES.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
        Map<String, List<BillDetailMoreV>> billDetailMoreVMap;
        switch (SysSourceEnum.valueOfByCode(invoiceReceiptE.getSysSource())) {
            case 合同系统:
                billDetailMoreVMap = getBillDetailMoreVMapHeTong(billDetailMoreVList);
                return billDetailMoreVMap.values().stream().map(bills -> getInvoiceReceiptDetailVInfoHeTong(bills, invoiceReceiptDetailMap)).collect(Collectors.toList());
            default:
                // 保留合同原有逻辑不变 只修改收费系统
                billDetailMoreVMap = getBillDetailMoreVMapShouFei(billDetailMoreVList);
                return billDetailMoreVMap.values().stream().map(bills -> getInvoiceReceiptDetailVInfoShouFei(bills, invoiceReceiptDetailMap)).collect(Collectors.toList());
        }
    }

    private Map<String, List<BillDetailMoreV>> getBillDetailMoreVMapHeTong(List<BillDetailMoreV> billDetailMoreVList) {
        if (EnvConst.HUIXIANGYUN.equals(EnvData.config) || EnvConst.YUANYANG.equals(EnvData.config)
                || EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            return billDetailMoreVList.stream().collect(
                    Collectors.groupingBy(
                            bill -> bill.getChargeItemId() + "-" + bill.getUnitPrice() + "-" + bill.getBillMethod()+"-"+bill.getChargingArea() + hasTime(bill)
                    ));
        }
        return billDetailMoreVList.stream().collect(
                Collectors.groupingBy(
                        bill -> bill.getChargeItemId() + "-" + bill.getUnitPrice() + "-" + bill.getBillMethod() + hasTime(bill)
                ));
    }

    private Map<String, List<BillDetailMoreV>> getBillDetailMoreVMapShouFei(List<BillDetailMoreV> billDetailMoreVList) {
        if (EnvConst.HUIXIANGYUN.equals(EnvData.config) || EnvConst.YUANYANG.equals(EnvData.config)
                || EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            // 修改为根据账单类型和费项id分组
            return billDetailMoreVList.stream().collect(
                    Collectors.groupingBy(
                            bill -> bill.getBillType() + "-" + bill.getChargeItemId() + "-" + hasTime(bill)
                    ));
        }
        return billDetailMoreVList.stream().collect(
                Collectors.groupingBy(
                        bill -> bill.getChargeItemId() + "-" + bill.getUnitPrice() + "-" + bill.getBillMethod() + hasTime(bill)
                ));
    }

    private String hasTime(BillDetailMoreV billDetailMoreV) {
        if (billDetailMoreV.getStartTime() == null || billDetailMoreV.getEndTime() == null){
            return "N";
        } else {
            return "Y";
        }
    }

    /**
     * 获取收据明细
     *
     * @param bills
     * @param invoiceReceiptDetailMap
     * @return
     */
    private InvoiceReceiptDetailV getInvoiceReceiptDetailVInfoHeTong(List<BillDetailMoreV> bills, Map<Long, List<InvoiceReceiptDetailE>> invoiceReceiptDetailMap) {
        InvoiceReceiptDetailV detailV = new InvoiceReceiptDetailV();
        Long invoiceAmount = 0L;
        BigDecimal chargingAreaSum = BigDecimal.ZERO;
        List<Long> invoiceReceiptDetailIdList = Lists.newArrayList();
        List<String> remarkList = Lists.newArrayList();
        for (BillDetailMoreV b : bills) {
            detailV.setChargeItemName(b.getChargeItemName());
            detailV.setPrice(b.getUnitPrice().toString());
            List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailMap.get(b.getBillId());
            InvoiceReceiptDetailE invoiceReceiptDetailE = invoiceReceiptDetailES.get(0);
            invoiceAmount = invoiceAmount + invoiceReceiptDetailES.stream()
                    .mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            if (b.getChargingArea() != null) {
                chargingAreaSum = chargingAreaSum.add(b.getChargingArea());
            }
            invoiceReceiptDetailIdList.add(invoiceReceiptDetailE.getId());
            if (StringUtils.isNotBlank(invoiceReceiptDetailE.getRemark())) {
                remarkList.add(invoiceReceiptDetailE.getRemark());
            }
            if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
                remarkList = getBillingGatherRemark(bills);
            }
        }
        if (CollectionUtils.isNotEmpty(remarkList)) {
            remarkList = remarkList.stream().distinct().collect(Collectors.toList());
            detailV.setRemark(remarkList.stream().map(String::valueOf).collect(Collectors.joining(";")));
        }
        BillDetailMoreV billDetailMoreV = bills.get(0);
        detailV.setNumStr(BillMethodEnum.handleNumStr(billDetailMoreV.getBillMethod(), bills, chargingAreaSum, billDetailMoreV.getChargingCount()));
        //此处因为此次开票小计
        detailV.setInvoiceAmount(invoiceAmount);
        detailV.setInvoiceReceiptDetails(invoiceReceiptDetailIdList);
        return detailV;
    }

    /**
     * 获取收据明细
     *
     * @param bills
     * @param invoiceReceiptDetailMap
     * @return
     */
    private InvoiceReceiptDetailV getInvoiceReceiptDetailVInfoShouFei(List<BillDetailMoreV> bills, Map<Long, List<InvoiceReceiptDetailE>> invoiceReceiptDetailMap) {
        InvoiceReceiptDetailV detailV = new InvoiceReceiptDetailV();
        long invoiceAmount = 0L;
        BigDecimal chargingAreaSum = BigDecimal.ZERO;
        List<Long> invoiceReceiptDetailIdList = Lists.newArrayList();
        String remark = "";
        String remarkNew = "";
        Long receivableAmount = 0L;
        Long actualPayAmount = 0L;
        List<BillStartEndTimeDTO> startEndTimeDateTime = Lists.newArrayList();
        String price = bills.get(0).getUnitPrice().toString();
        for (BillDetailMoreV b : bills) {
            detailV.setChargeItemName(b.getChargeItemName());
            if (Objects.nonNull(b.getUnitPrice())){
                price = b.getUnitPrice().toString().equals(price) ? price : null;
            }
            if (Objects.isNull(b.getBillMethod())) {
                throw BizException.throw400("账单计费方式为空，请检查");
            }
            BillMethodEnum billMethodEnumByCode = BillMethodEnum.getBillMethodEnumByCode(b.getBillMethod());
            if (EnvConst.YUANYANG.equals(EnvData.config) &&
                    Objects.nonNull(billMethodEnumByCode) &&
                    BillMethodEnum.IS_INSTRUMENT_METHOD.test(billMethodEnumByCode)
            ) {
                // 处理仪表度数
                dealInstrumentNum(b, detailV);
            }
            //计费周期分组去重体现
            BillStartEndTimeDTO startEndTimeDTO = new BillStartEndTimeDTO();
            startEndTimeDTO.setStartTime(b.getStartTime());
            startEndTimeDTO.setEndTime(b.getEndTime());
            startEndTimeDTO.setPayTime(b.getPayTime());
            startEndTimeDTO.setChargeTime(b.getChargeTime());
            startEndTimeDTO.setBillMethod(b.getBillMethod());
            startEndTimeDateTime.add(startEndTimeDTO);
            List<InvoiceReceiptDetailE> receiptDetailEList = invoiceReceiptDetailMap.get(b.getBillId());
            InvoiceReceiptDetailE invoiceReceiptDetailE = receiptDetailEList.get(0);
            if (receiptDetailEList.stream().anyMatch(a -> StringUtils.isNotBlank(a.getRemark()))){
                remark = invoiceReceiptDetailE.getRemark();
            }
            if (receiptDetailEList.stream().anyMatch(a -> StringUtils.isNotBlank(a.getRemark()))){
                remarkNew = invoiceReceiptDetailE.getRemarkNew();
            }
            invoiceAmount = invoiceAmount + receiptDetailEList.stream()
                    .mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            if (b.getChargingArea() != null) {
                chargingAreaSum = chargingAreaSum.add(b.getChargingArea());
            }
            invoiceReceiptDetailIdList.add(invoiceReceiptDetailE.getId());
            receivableAmount += b.getReceivableAmount();
            actualPayAmount += b.getActualPayAmount();
        }
        if (StringUtils.isBlank(remark)) {
            if (startEndTimeDateTime.stream().anyMatch(time -> time.getStartTime() == null || time.getEndTime() == null)) {
            } else {
                List<BillStartEndTimeDTO> billStartEndTimeDTOS = handleAndMergeStartEndTimeDate(startEndTimeDateTime);
                if (CollectionUtils.isNotEmpty(billStartEndTimeDTOS)) {
                    remark = "计费周期：" + getBillingCycleRemark(billStartEndTimeDTOS);
                    getExpensePeriod(billStartEndTimeDTOS, detailV);
                }
            }
        }
        //方圆套打收据取缴费时备注,默认收据取计费周期
        if (StringUtils.isBlank(remarkNew)){
            if (EnvConst.FANGYUAN.equals(EnvData.config)) {
                List<String> remarks = getBillingGatherRemark(bills);
                remarkNew = CollectionUtils.isEmpty(remarks) ? "" : String.join(";", remarks);
            }
        }
        // 如果是中交环境，备注设置收款单备注（缴费时的备注）
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            List<String> remarks = getBillingGatherRemark(bills);
            remark = CollectionUtils.isEmpty(remarks) ? "" : String.join(";", remarks);
        }
        detailV.setPrice(price);
        detailV.setRemarkNew(remarkNew);
        detailV.setRemark(remark);
        BillDetailMoreV billDetailMoreV = bills.get(0);
        //HXYUN-16318,慧享云计量新展示方式  慧享云、远洋、拈花湾
        if (TenantUtil.bf84()){
            detailV.setNumStr(BillMethodEnum.huixiangHandleNumStr(billDetailMoreV.getBillMethod(), bills, chargingAreaSum, billDetailMoreV.getChargingCount()));
        } else if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            detailV.setNumStr(BillMethodEnum.zhongjiaoHandleNumStr(billDetailMoreV.getBillMethod(), bills, chargingAreaSum, billDetailMoreV.getChargingCount()));
        } else {
            detailV.setNumStr(BillMethodEnum.handleNumStr(billDetailMoreV.getBillMethod(), bills, chargingAreaSum, billDetailMoreV.getChargingCount()));
        }
        //此处因为此次开票小计
        detailV.setInvoiceAmount(invoiceAmount);
        detailV.setInvoiceReceiptDetails(invoiceReceiptDetailIdList);
        detailV.setReceivableAmount(receivableAmount);
        detailV.setActualPayAmount(actualPayAmount);
        detailV.initInstrumentStr();
        // 如果是临时收费 不需要展示单价和数量
        if(CollectionUtils.isNotEmpty(bills) && String.valueOf(BillTypeEnum.临时收费账单.getCode()).equals(bills.get(0).getBillType())){
            detailV.setPrice(null);
            detailV.setNum("");
            detailV.setNumStr("");
        }
        return detailV;
    }


    private void dealInstrumentNum(BillDetailMoreV item, InvoiceReceiptDetailV detailV) {
        if (Objects.isNull(item.getBillMethod())) {
            return;
        }
        BillMethodEnum billMethodEnumByCode = BillMethodEnum.getBillMethodEnumByCode(item.getBillMethod());
        if (!BillMethodEnum.IS_INSTRUMENT_METHOD.test(billMethodEnumByCode)) {
            return;
        }

        if (StringUtils.isNotBlank(item.getExtField9())) {
            detailV.setMinNum(
                    Objects.isNull(detailV.getMinNum()) ? new BigDecimal(item.getExtField9()) :
                    detailV.getMinNum().min(new BigDecimal(item.getExtField9()))
            );
        }

        if (StringUtils.isNotBlank(item.getExtField7())) {
            detailV.setMaxNum(
                    Optional.ofNullable(detailV.getMaxNum()).orElse(BigDecimal.ZERO)
                            .max(new BigDecimal(item.getExtField7()))
            );
        }

        if (BillTypeEnum.临时收费账单.equalsByCode(Integer.parseInt(item.getBillType())) && StringUtils.isNotBlank(item.getExtField8())) {
            detailV.setMagnification(new BigDecimal(item.getExtField8()));
        }

        if (Objects.nonNull(item.getBillMethod())) {
            detailV.setBillMethod(item.getBillMethod());
        }
    }

    public List<String> getBillingGatherRemark(List<BillDetailMoreV> bills) {
        List<String> remarks = new ArrayList<>();
        if (CollectionUtils.isEmpty(bills)) {
            return remarks;
        }
        //
        for (BillDetailMoreV bill : bills) {
            List<BillSettleV> billSettleDtos = bill.getBillSettleDtos();
            if (CollectionUtils.isEmpty(billSettleDtos)) {
                continue;
            }
            for (BillSettleV dto : billSettleDtos) {
                if (StringUtils.isNotBlank(dto.getRemark())) {
                    remarks.add(dto.getRemark());
                }
            }
        }

        return remarks;
    }

    /**
     * 整合计费周期备注
     * @param startEndTimeDateTime
     * @return
     */
    public String getBillingCycleRemark(List<BillStartEndTimeDTO> startEndTimeDateTime) {
        StringBuilder sb = new StringBuilder();
        for (BillStartEndTimeDTO timeDTO : startEndTimeDateTime) {
            sb.append(timeDTO.getStartTime().toLocalDate()).append("-").append(timeDTO.getEndTime().toLocalDate());
            // 计费方式包含时间时
            if (timeDTO.hasTimeBilling()) {
                // 缴费时间在账单结束时间之前时
                if (timeDTO.getChargeTime() != null && DateTimeUtil.daysBetween(timeDTO.getChargeTime(), timeDTO.getEndTime()) > 1) {
                    sb.append(",缴至").append(timeDTO.getChargeTime().toLocalDate());
                }
            }
            sb.append(";");
        }
        return sb.toString();
    }

    public void getExpensePeriod(List<BillStartEndTimeDTO> startEndTimeDateTime, InvoiceReceiptDetailV detailV) {
        // 当多个账单的计费周期(年月日一致时)相同时，进行合并
        Set<String> expensePeriodSet = new HashSet<>();
        List<String> expensePeriodList = new ArrayList<>();
        for (BillStartEndTimeDTO timeDTO : startEndTimeDateTime) {
            String expensePeriod = timeDTO.getStartTime().toLocalDate() + "至" + timeDTO.getEndTime().toLocalDate();
            expensePeriodList.add(expensePeriod);
            if (expensePeriodSet.contains(expensePeriod)) {
                continue;
            }
            expensePeriodSet.add(expensePeriod);
        }
        detailV.setExpensePeriod(String.join("<br/>", expensePeriodList));
        detailV.setExpensePeriodList(expensePeriodList);
    }

    /**
     * 处理计费周期,合并相邻的计费周期
     * 若周期中有部分缴费，该周期不合并
     * @param startEndTimeDates
     * @return
     */
    public List<BillStartEndTimeDTO> handleAndMergeStartEndTimeDate(List<BillStartEndTimeDTO> startEndTimeDates) {
        // 创建一个新的集合
        List<BillStartEndTimeDTO> finalList = new ArrayList<>();
        startEndTimeDates.sort((o1, o2) -> {
            if (o1.getStartTime().isEqual(o2.getStartTime()) && o1.getEndTime().isEqual(o2.getEndTime())) {
                return 0;
            }
            if (o1.getStartTime().isAfter(o2.getEndTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getEndTime())) {
                return -1;
            }
            return 0;
        });
        Iterator<BillStartEndTimeDTO> iterator = startEndTimeDates.iterator();
        BillStartEndTimeDTO timeDTO = iterator.next();
        // a1
        while (iterator.hasNext()) {
            BillStartEndTimeDTO nextTimeDTO = iterator.next();
            LocalDateTime firstStartTime = timeDTO.getStartTime();
            LocalDateTime firstEndTime = timeDTO.getEndTime();
            LocalDateTime firstChargeTime = timeDTO.getChargeTime();
            // 包含时间计费方式时才进行合并
            if (timeDTO.hasTimeBilling()) {
                if (firstChargeTime != null && DateTimeUtil.daysBetween(firstChargeTime, firstEndTime) > 0) {
                    BillStartEndTimeDTO dto = new BillStartEndTimeDTO();
                    dto.setStartTime(firstStartTime);
                    dto.setEndTime(firstEndTime);
                    dto.setChargeTime(firstChargeTime);
                    dto.setBillMethod(timeDTO.getBillMethod());
                    finalList.add(dto);
                    // 账单已缴时间小于账单结束时间的，不进行合并，单独展示
                    timeDTO = nextTimeDTO;
                    continue;
                }
            }
            LocalDateTime secondStartTime = nextTimeDTO.getStartTime();
            LocalDateTime secondEndTime = nextTimeDTO.getEndTime();
            LocalDateTime secondChargeTime = nextTimeDTO.getChargeTime();
            // 包含时间计费方式时才进行合并
            if (timeDTO.hasTimeBilling()) {
                if (secondChargeTime != null && DateTimeUtil.daysBetween(secondChargeTime, secondEndTime) > 0) {
                    BillStartEndTimeDTO dto = new BillStartEndTimeDTO();
                    dto.setStartTime(timeDTO.getStartTime());
                    dto.setEndTime(secondEndTime);
                    dto.setChargeTime(secondChargeTime);
                    dto.setBillMethod(timeDTO.getBillMethod());
                    finalList.add(dto);
                    // 账单已缴时间小于账单结束时间的，不进行合并，单独展示
                    timeDTO = nextTimeDTO;
                    continue;
                }
            }
            // 判断是否需要合并
            if (DateTimeUtil.daysBetween(firstEndTime, secondStartTime) <= 1) {
                if (DateTimeUtil.daysBetween(firstEndTime, secondEndTime) >= 0) {
                    if(DateTimeUtil.daysBetween(firstStartTime,secondStartTime) < 1){
                        timeDTO.setStartTime(secondStartTime);
                    }
                    timeDTO.setEndTime(secondEndTime);
                    timeDTO.setChargeTime(nextTimeDTO.getChargeTime());
                    iterator.remove();
                }
            } else {
                BillStartEndTimeDTO dto = new BillStartEndTimeDTO();
                dto.setStartTime(firstStartTime);
                dto.setEndTime(firstEndTime);
                dto.setChargeTime(timeDTO.getChargeTime());
                dto.setBillMethod(timeDTO.getBillMethod());
                finalList.add(dto);

                timeDTO.setStartTime(secondStartTime);
                timeDTO.setEndTime(secondEndTime);
                timeDTO.setChargeTime(nextTimeDTO.getChargeTime());
            }
        }
        finalList.add(timeDTO);
        //对list处理重复数据
        return this.deduplication(finalList);
    }

    /**
     * 在处理timeDTO如果不连续 就会出现重复数据处理去重。临近2条比较开始时间、结束时间、缴至时间即可
     *  例子：2024-03-01-2024-03-31,缴至2024-03-01;2024-03-01-2024-03-31,缴至2024-03-01;
     * 在处理timeDTO如果存在不连续数据，就会出现包含周期数据
     *  例子：2023-06-01-2023-07-31,缴至2023-07-01;2023-07-01-2023-07-31,缴至2023-07-01;
     * @param finalList
     * @return
     */
    private List<BillStartEndTimeDTO> deduplication(List<BillStartEndTimeDTO> finalList){
        List<BillStartEndTimeDTO> filteredList = new ArrayList<>();
        BillStartEndTimeDTO prevDTO = null;
        for (BillStartEndTimeDTO dto : finalList) {
            if (prevDTO != null && (areLocalDateTimesEqual(prevDTO, dto) || isTimeContained(prevDTO, dto))) {
                // 如果相邻数据的开始时间、结束时间、缴至时间都相同，则跳过
                continue;
            }
            // 如果不相同，则添加到filteredList中
            filteredList.add(dto);
            prevDTO = dto;
        }

        return filteredList;
    }


    /**
     * 校验开始时间、结束时间是否包含
     * @param dto1
     * @param dto2
     * @return
     */
    public static boolean isTimeContained(BillStartEndTimeDTO dto1, BillStartEndTimeDTO dto2) {
        // 检查dto1的时间范围是否包含dto2的时间范围
        return (dto1.getStartTime().isBefore(dto2.getStartTime()) || dto1.getStartTime().isEqual(dto2.getStartTime())) &&
                (dto1.getEndTime().isAfter(dto2.getEndTime()) || dto1.getEndTime().isEqual(dto2.getEndTime()));
    }


    /**
     * 校验开始时间、结束时间、缴费时间
     * @param dto1
     * @param dto2
     * @return
     */
    public static boolean areLocalDateTimesEqual(BillStartEndTimeDTO dto1, BillStartEndTimeDTO dto2) {
        // 检查a、b、c是否相同，考虑可能存在空值的情况
        return (dto1.getStartTime() == null ? dto2.getStartTime() == null : dto1.getStartTime().equals(dto2.getStartTime())) &&
                (dto1.getEndTime() == null ? dto2.getEndTime() == null : dto1.getEndTime().equals(dto2.getEndTime())) &&
                (dto1.getChargeTime() == null ? dto2.getChargeTime() == null : dto1.getChargeTime().equals(dto2.getChargeTime()));
    }




    /**
     * 处理   优惠/抵扣/减免
     *
     * @param billDetailMoreVList
     * @return
     */
    private List<OtherAmountDto> handleOtherAmount(List<BillDetailMoreV> billDetailMoreVList) {
        OtherAmountDto dto = new OtherAmountDto();
        dto.setOtherAmountName("优惠金额");
        BigDecimal otherAmount = BigDecimal.ZERO;
        String remark = "";
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            otherAmount = otherAmount.add(new BigDecimal(billDetailMoreV.getDeductibleAmount())).add(new BigDecimal(billDetailMoreV.getDiscountAmount()));
            List<BillAdjustV> billAdjustDtos = billDetailMoreV.getBillAdjustDtos();
            if (CollectionUtils.isNotEmpty(billAdjustDtos)) {
                for (BillAdjustV billAdjustDto : billAdjustDtos) {
                    //凭证号
                    if (StringUtils.isNotBlank(billAdjustDto.getVoucher())) {
                        remark = remark + "【优惠/抵扣/减免】凭证号为：" + billAdjustDto.getVoucher();
                    }
                }
            }
            List<BillSettleV> billSettleDtos = billDetailMoreV.getBillSettleDtos();
            if (CollectionUtils.isNotEmpty(billSettleDtos)) {
                for (BillSettleV billSettleDto : billSettleDtos) {
                    if (StringUtils.isNotBlank(billSettleDto.getDiscounts())) {
                        List<DiscountOBV> discountOBVS = JSON.parseArray(billSettleDto.getDiscounts(), DiscountOBV.class);
                        if (CollectionUtils.isNotEmpty(discountOBVS)) {
                            //优惠
                            String discount = discountOBVS.stream().map(DiscountOBV::getDescription).collect(Collectors.joining(","));
                            remark = remark + "【优惠/抵扣/减免】凭证号为：" + discount;
                        }
                    }
                }
            }
        }

        if (otherAmount != BigDecimal.ZERO) {
            dto.setNum(1L);
            dto.setPrice(otherAmount.negate());
            dto.setOtherAmount(otherAmount);
            //HXYUN-16318,慧享云不要备注
            if (!EnvConst.HUIXIANGYUN.equals(EnvData.config)){
                dto.setRemark(remark);
            }
            return Lists.newArrayList(dto);
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 测试方法：用于测试 handleAndMergeStartEndTimeDate 方法
     */
    public static void main(String[] args) {
        ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto();
        
        // 构造测试数据 - 模拟您提供的账单数据
        List<BillStartEndTimeDTO> testData = new ArrayList<>();
        
        // 账单1: 2025-11-20 至 2025-11-20
        BillStartEndTimeDTO bill1 = new BillStartEndTimeDTO();
        bill1.setStartTime(LocalDateTime.of(2025, 11, 20, 0, 0, 0));
        bill1.setEndTime(LocalDateTime.of(2025, 11, 20, 23, 59, 59));
        bill1.setBillMethod(null);
        testData.add(bill1);
        
        // 账单2: 2025-11-21 至 2025-11-30
        BillStartEndTimeDTO bill2 = new BillStartEndTimeDTO();
        bill2.setStartTime(LocalDateTime.of(2025, 11, 21, 0, 0, 0));
        bill2.setEndTime(LocalDateTime.of(2025, 11, 30, 23, 59, 59));
        bill2.setBillMethod(null);
        testData.add(bill2);
        
        // 账单3: 2025-12-01 至 2025-12-31
        BillStartEndTimeDTO bill3 = new BillStartEndTimeDTO();
        bill3.setStartTime(LocalDateTime.of(2025, 12, 1, 0, 0, 0));
        bill3.setEndTime(LocalDateTime.of(2025, 12, 31, 23, 59, 59));
        bill3.setBillMethod(null);
        testData.add(bill3);
        
        // 账单4: 2026-01-01 至 2026-01-31
        BillStartEndTimeDTO bill4 = new BillStartEndTimeDTO();
        bill4.setStartTime(LocalDateTime.of(2026, 1, 1, 0, 0, 0));
        bill4.setEndTime(LocalDateTime.of(2026, 1, 31, 23, 59, 59));
        bill4.setBillMethod(null);
        testData.add(bill4);
        
        // 账单5: 2026-02-01 至 2026-02-28
        BillStartEndTimeDTO bill5 = new BillStartEndTimeDTO();
        bill5.setStartTime(LocalDateTime.of(2026, 2, 1, 0, 0, 0));
        bill5.setEndTime(LocalDateTime.of(2026, 2, 28, 23, 59, 59));
        bill5.setBillMethod(null);
        testData.add(bill5);
        
        // 账单6: 2026-03-01 至 2026-03-31
        BillStartEndTimeDTO bill6 = new BillStartEndTimeDTO();
        bill6.setStartTime(LocalDateTime.of(2026, 3, 1, 0, 0, 0));
        bill6.setEndTime(LocalDateTime.of(2026, 3, 31, 23, 59, 59));
        bill6.setBillMethod(null);
        testData.add(bill6);
        
        // 账单7: 2026-04-01 至 2026-04-30
        BillStartEndTimeDTO bill7 = new BillStartEndTimeDTO();
        bill7.setStartTime(LocalDateTime.of(2026, 4, 1, 0, 0, 0));
        bill7.setEndTime(LocalDateTime.of(2026, 4, 30, 23, 59, 59));
        bill7.setBillMethod(null);
        testData.add(bill7);
        
        // 账单8: 2026-05-01 至 2026-05-31
        BillStartEndTimeDTO bill8 = new BillStartEndTimeDTO();
        bill8.setStartTime(LocalDateTime.of(2026, 5, 1, 0, 0, 0));
        bill8.setEndTime(LocalDateTime.of(2026, 5, 31, 23, 59, 59));
        bill8.setBillMethod(null);
        testData.add(bill8);
        
        // 保存原始数据副本用于打印（因为合并方法会修改原始列表）
        List<BillStartEndTimeDTO> originalData = new ArrayList<>();
        for (BillStartEndTimeDTO dto : testData) {
            BillStartEndTimeDTO copy = new BillStartEndTimeDTO();
            copy.setStartTime(dto.getStartTime());
            copy.setEndTime(dto.getEndTime());
            copy.setBillMethod(dto.getBillMethod());
            copy.setChargeTime(dto.getChargeTime());
            copy.setPayTime(dto.getPayTime());
            originalData.add(copy);
        }
        
        // 打印输入数据
        System.out.println("========== 输入数据 ==========");
        for (int i = 0; i < originalData.size(); i++) {
            BillStartEndTimeDTO dto = originalData.get(i);
            System.out.printf("账单%d: %s 至 %s%n", 
                i + 1,
                dto.getStartTime().toLocalDate(),
                dto.getEndTime().toLocalDate());
        }
        
        // 调用合并方法（注意：该方法会修改输入的testData列表）
        System.out.println("\n========== 开始合并 ==========");
        List<BillStartEndTimeDTO> result = receiptDetailDto.handleAndMergeStartEndTimeDate(testData);
        
        // 打印合并结果
        System.out.println("\n========== 合并结果 ==========");
        for (int i = 0; i < result.size(); i++) {
            BillStartEndTimeDTO dto = result.get(i);
            System.out.printf("周期%d: %s 至 %s%n",
                i + 1,
                dto.getStartTime().toLocalDate(),
                dto.getEndTime().toLocalDate());
        }
        
        System.out.println("\n========== 合并完成 ==========");
        System.out.printf("输入账单数: %d, 合并后周期数: %d%n", originalData.size(), result.size());
    }
}
