package com.wishare.finance.domains.invoicereceipt.aggregate;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceInfoDto;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceClaimStatusEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.*;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceDetailHelper;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedApplyStatusEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedReasonEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/7
 * @Description: 红票聚合实体类
 */
@Getter
@Setter
@Slf4j
public class InvoiceRedA {

    /**
     * 原蓝票来源
     */
    private Integer blueSource;
    /**
     * 红票发票主表信息
     */
    private InvoiceReceiptE invoiceReceiptRedE;

    /**
     * 红票发票表信息
     */
    private InvoiceE invoiceRedE;

    /**
     * 红票发票明细表信息
     */
    private List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList;

    /**
     * 发票子表的信息
     */
    private List<InvoiceChildE> invoiceChildRedEList;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 红字确认单信息
     */
    private InvoiceRedApplyE invoiceRedApplyE;

    /**
     * 红票明细转换的统一明细信息
     */
    private List<InvoiceDetailDto> invoiceDetailDtoList;

    /**
     * 构造需要被红冲的聚合对象（蓝票信息）
     *
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailEList
     */
    public InvoiceRedA(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, List<InvoiceChildE> invoiceChildEList) {
        this.invoiceReceiptRedE = invoiceReceiptE;
        this.invoiceRedE = invoiceE;
        this.invoiceReceiptDetailRedEList = invoiceReceiptDetailEList;
        this.invoiceChildRedEList = invoiceChildEList;
    }

    public void generalRedApply() {
        if (invoiceReceiptRedE.getType().equals(InvoiceLineEnum.全电普票.getCode()) || invoiceReceiptRedE.getType().equals(InvoiceLineEnum.全电专票.getCode())) {
            invoiceRedApplyE = new InvoiceRedApplyE();
            invoiceRedApplyE.setInvoiceReceiptId(invoiceRedE.getInvoiceReceiptId());
            invoiceRedApplyE.setBlueInvoiceReceiptId(invoiceRedE.getBlueInvoiceReceiptId());
            invoiceRedApplyE.setStatus(NuonuoRedApplyStatusEnum.申请中.getCode());
            invoiceRedApplyE.setRedReason(NuonuoRedReasonEnum.开票有误.getCode());
        }
    }

    /**
     * 根据票据id全部红冲
     *
     * @param redInvoiceInfoBefore 已经红冲的票
     */
    public void redByInvoiceReceiptId(Long invoiceReceiptId, List<InvoiceInfoDto> redInvoiceInfoBefore) {
        invoiceReceiptRedE = generalRedInvoiceReceipt(this.getInvoiceReceiptRedE(), redInvoiceInfoBefore, null);
        invoiceRedE = generalRedInvoice(invoiceReceiptId, this.getInvoiceRedE());
        invoiceReceiptDetailRedEList = generalRedInvoiceReceiptDetail(this.getInvoiceReceiptDetailRedEList(), redInvoiceInfoBefore, null, null);
        generalRedApply();
    }

    /**
     * 根据票据id红冲指定金额
     *
     * @param invoiceReceiptId
     * @param redInvoiceInfoBefore
     */
    public void redByInvoiceReceiptId(Long invoiceReceiptId, List<InvoiceInfoDto> redInvoiceInfoBefore, Long billId, Long redInvoiceAmount) {
        invoiceReceiptRedE = generalRedInvoiceReceipt(this.getInvoiceReceiptRedE(), redInvoiceInfoBefore, redInvoiceAmount);
        invoiceRedE = generalRedInvoice(invoiceReceiptId, this.getInvoiceRedE());
        invoiceReceiptDetailRedEList = generalRedInvoiceReceiptDetail(this.getInvoiceReceiptDetailRedEList(), redInvoiceInfoBefore, billId, redInvoiceAmount);
        generalRedApply();
    }

    /**
     * 根据账单id
     *
     * @param invoiceReceiptId
     * @param billId
     * @param redInvoiceAmount
     * @param redInvoiceInfoBefore
     */
    public void redByBillId(Long invoiceReceiptId, Long billId, Long redInvoiceAmount, List<InvoiceInfoDto> redInvoiceInfoBefore) {
        invoiceReceiptRedE = generalRedInvoiceReceiptByBillId(this.getInvoiceReceiptRedE(), redInvoiceAmount);
        invoiceRedE = generalRedInvoice(invoiceReceiptId, this.getInvoiceRedE());
        InvoiceReceiptDetailE invoiceReceiptDetailE = this.getInvoiceReceiptDetailRedEList().stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId)).get(billId).get(0);
        invoiceReceiptDetailRedEList = generalRedInvoiceReceiptDetailByBillId(invoiceReceiptDetailE,billId, redInvoiceAmount, this,getInvoiceReceiptDetailRedEList(), redInvoiceInfoBefore);
        generalRedApply();
    }


    /**
     * 根据发票子表invoiceChild进行红冲
     *
     * @param redInvoiceInfoBefore
     * @param invoiceChildE
     */
    public void redByInvoiceChild(Long invoiceReceiptId, List<InvoiceInfoDto> redInvoiceInfoBefore, InvoiceChildE invoiceChildE) {
        redInvoiceByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE, null);
    }

    /**
     * 根据发票子表invoiceChild进行红冲
     *
     * @param invoiceReceiptId
     * @param invoiceChildE
     * @param redInvoiceInfoBefore
     */
    public void redBillIdByInvoiceChild(Long invoiceReceiptId, InvoiceChildE invoiceChildE, List<InvoiceInfoDto> redInvoiceInfoBefore) {
        Long redInvoiceAmount = invoiceChildE.getInvoiceAmount() + invoiceChildE.getRedInvoiceAmount();
        redInvoiceByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE, redInvoiceAmount);
    }

    /**
     * 根据发票子表invoiceChild指定金额进行红冲
     *
     * @param redInvoiceInfoBefore
     * @param invoiceChildE
     */
    public void redByInvoiceChild(Long invoiceReceiptId, List<InvoiceInfoDto> redInvoiceInfoBefore, InvoiceChildE invoiceChildE, Long redInvoiceAmount) {
        redInvoiceByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE, redInvoiceAmount);
    }

    /**
     * 根据发票子表invoiceChild处理相关红冲
     *
     * @param redInvoiceInfoBefore
     * @param invoiceChildE
     * @param redInvoiceAmount
     */
    public void redInvoiceByInvoiceChild(Long invoiceReceiptId, List<InvoiceInfoDto> redInvoiceInfoBefore, InvoiceChildE invoiceChildE, Long redInvoiceAmount) {
        this.redByInvoiceReceiptId(invoiceReceiptId, redInvoiceInfoBefore);

        //替换需要红冲的票据号码,编号
        InvoiceE redE = this.getInvoiceRedE();
        redE.setInvoiceCode(invoiceChildE.getInvoiceCode());
        redE.setInvoiceNo(invoiceChildE.getInvoiceNo());

        //设置当前红票的价税合计
        Long invoiceReceiptPriceTaxAmount = null == redInvoiceAmount ? -invoiceChildE.getInvoiceAmount() : -redInvoiceAmount;
        this.getInvoiceReceiptRedE().setPriceTaxAmount(invoiceReceiptPriceTaxAmount);

        //设置发票明细表数据
        InvoiceReceiptDetailE invoiceReceiptDetailE = this.getInvoiceReceiptDetailRedEList().get(0);
        if (null == redInvoiceAmount) {
            invoiceReceiptDetailE.setInvoiceAmount(-invoiceChildE.getInvoiceAmount());
            invoiceReceiptDetailE.setPriceTaxAmount(invoiceChildE.getInvoiceAmount());
            invoiceReceiptDetailE.setPrice(invoiceChildE.getInvoiceAmount().toString());

            invoiceChildE.setRedInvoiceAmount(-invoiceChildE.getInvoiceAmount());
            invoiceChildE.setState(generalInvoiceChildState(invoiceChildE.getInvoiceAmount(), invoiceChildE.getRedInvoiceAmount()));

        } else {
            invoiceReceiptDetailE.setInvoiceAmount(-redInvoiceAmount);
            invoiceReceiptDetailE.setPriceTaxAmount(redInvoiceAmount);
            invoiceReceiptDetailE.setPrice(redInvoiceAmount.toString());

            invoiceChildE.setRedInvoiceAmount(invoiceChildE.getRedInvoiceAmount() + (-redInvoiceAmount));
            invoiceChildE.setState(generalInvoiceChildState(invoiceChildE.getInvoiceAmount(), invoiceChildE.getRedInvoiceAmount()));

        }

    }

    /**
     * 构建发票子表的状态
     *
     * @param invoiceAmount    正数
     * @param redInvoiceAmount 负数
     * @return
     */
    private Integer generalInvoiceChildState(Long invoiceAmount, Long redInvoiceAmount) {
        if (invoiceAmount + redInvoiceAmount > 0) {
            return InvoiceReceiptStateEnum.部分红冲.getCode();
        } else if (invoiceAmount + redInvoiceAmount == 0) {
            return InvoiceReceiptStateEnum.已红冲.getCode();
        }
        return null;
    }

    /**
     * 构建红票InvoiceReceiptDetail明细
     *
     * @return
     */
    private List<InvoiceReceiptDetailE> generalRedInvoiceReceiptDetail(List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList,
                                                                       List<InvoiceInfoDto> redInvoiceInfoBefore,
                                                                       Long billId,
                                                                       Long redInvoiceAmount) {
        Map<Long, List<InvoiceReceiptDetailE>> billRedInvoiceBefore = handleBillHasRedInvoice(redInvoiceInfoBefore);
        List<InvoiceReceiptDetailE> redDetails = Lists.newArrayList();

        if (billId != null && redInvoiceAmount != null) {
            // 1.
            this.invoiceDetailDtoList = InvoiceDetailHelper.partialRedFlushToInvoiceDetailDtoList(invoiceReceiptDetailRedEList, redInvoiceInfoBefore, billId, redInvoiceAmount);
            // 2.
            InvoiceReceiptDetailE redDetail = invoiceReceiptDetailRedEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId)).get(billId).get(0);
            redDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
            redDetail.setInvoiceReceiptId(this.invoiceReceiptRedE.getId());
            redDetail.setNum(generalRedNum(redDetail.getNum()));
            redDetail.setInvoiceAmount(-redInvoiceAmount);
            if (Objects.nonNull(redDetail.getDiscountAmount())) {
                redDetail.setDiscountAmount(-redDetail.getDiscountAmount());
            }
            redDetail.setPrice(redInvoiceAmount.toString());
            redDetail.setGmtCreate(LocalDateTime.now());
            redDetail.setGmtModify(LocalDateTime.now());
            redDetails.add(redDetail);
            return redDetails;
        } else {
            // 1.
            this.invoiceDetailDtoList = InvoiceDetailHelper.allRedFlushToInvoiceDetailDtoList(invoiceReceiptDetailRedEList, redInvoiceInfoBefore);
            // 2.
            for (InvoiceReceiptDetailE redDetail : invoiceReceiptDetailRedEList) {
                redDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
                redDetail.setInvoiceReceiptId(this.invoiceReceiptRedE.getId());
                redDetail.setNum(generalRedNum(redDetail.getNum()));
                //获取账单已红冲金额
                List<InvoiceReceiptDetailE> billRedInvoice = billRedInvoiceBefore.get(redDetail.getBillId());
                Long hasRedInvoiceAmountSum = 0L;
                if (CollectionUtils.isNotEmpty(billRedInvoice)) {
                    hasRedInvoiceAmountSum = billRedInvoice.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
                }
                //红票金额 = 蓝票金额 + (-已红冲金额)
                Long invoiceAmount = redDetail.getInvoiceAmount() + hasRedInvoiceAmountSum;
                redDetail.setInvoiceAmount(-invoiceAmount);
                if (Objects.nonNull(redDetail.getDiscountAmount())) {
                    redDetail.setDiscountAmount(-redDetail.getDiscountAmount());
                }
                redDetail.setPrice(invoiceAmount.toString());

                redDetail.setGmtCreate(LocalDateTime.now());
                redDetail.setGmtModify(LocalDateTime.now());
                if (invoiceAmount != 0L) {
                    redDetails.add(redDetail);
                }
            }
            return redDetails;
        }
    }


    /**
     * 构建红票InvoiceReceiptDetail明细
     *
     * @param redDetail
     * @param billId
     * @param redInvoiceAmount
     * @param invoiceRedA
     * @param redInvoiceInfoBefore
     * @return
     */
    private List<InvoiceReceiptDetailE> generalRedInvoiceReceiptDetailByBillId(InvoiceReceiptDetailE redDetail, Long billId, Long redInvoiceAmount,
                                                                               InvoiceRedA invoiceRedA, List<InvoiceReceiptDetailE> blueInvoiceReceiptDetailList, List<InvoiceInfoDto> redInvoiceInfoBefore) {

        // 1.
        this.invoiceDetailDtoList = InvoiceDetailHelper.partialRedFlushToInvoiceDetailDtoList(blueInvoiceReceiptDetailList, redInvoiceInfoBefore, billId, redInvoiceAmount);
        // 2.
        List<InvoiceReceiptDetailE> redDetails = Lists.newArrayList();
        redDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        redDetail.setInvoiceReceiptId(this.invoiceReceiptRedE.getId());
        redDetail.setNum(generalRedNum(redDetail.getNum()));
        redDetail.setInvoiceAmount(-redInvoiceAmount);
        if (Objects.nonNull(redDetail.getDiscountAmount())) {
            redDetail.setDiscountAmount(-redDetail.getDiscountAmount());
        }
        redDetail.setPrice(redInvoiceAmount.toString());
        redDetail.setGmtCreate(LocalDateTime.now());
        redDetail.setGmtModify(LocalDateTime.now());
        return Lists.newArrayList(redDetail);
    }


    /**
     * 构建红票Invoice的信息
     *
     * @param invoiceReceiptId
     * @param invoiceE
     */
    private InvoiceE generalRedInvoice(Long invoiceReceiptId, InvoiceE invoiceE) {
        InvoiceE invoiceERed = new InvoiceE();
        invoiceERed.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
        invoiceERed.setInvoiceType(InvoiceTypeEnum.红票.getCode());
        invoiceERed.setInvoiceReceiptId(this.invoiceReceiptRedE.getId());
        invoiceERed.setInvoiceTitleType(invoiceE.getInvoiceTitleType());
        invoiceERed.setInvoiceSerialNum(invoiceE.getInvoiceSerialNum());
        invoiceERed.setSalerTaxNum(invoiceE.getSalerTaxNum());
        invoiceERed.setSalerName(invoiceE.getSalerName());
        invoiceERed.setSalerTel(invoiceE.getSalerTel());
        invoiceERed.setSalerAddress(invoiceE.getSalerAddress());
        invoiceERed.setSalerAccount(null);
        invoiceERed.setBuyerName(invoiceE.getBuyerName());
        invoiceERed.setBuyerTaxNum(invoiceE.getBuyerTaxNum());
        invoiceERed.setBuyerTel(invoiceE.getBuyerTel());
        invoiceERed.setBuyerAddress(invoiceE.getBuyerAddress());
        invoiceERed.setExtensionNumber(invoiceE.getExtensionNumber());
        invoiceERed.setTerminalNumber(invoiceE.getTerminalNumber());

        invoiceERed.setPushMode(invoiceE.getPushMode());
        invoiceERed.setBuyerPhone(invoiceE.getBuyerPhone());
        invoiceERed.setEmail(invoiceE.getEmail());
        invoiceERed.setInvoiceCode(invoiceE.getInvoiceCode());
        invoiceERed.setInvoiceNo(invoiceE.getInvoiceNo());
        invoiceERed.setBlueInvoiceReceiptId(invoiceReceiptId);
        invoiceERed.setFreeTax(invoiceE.getFreeTax());
        invoiceERed.setAddressInfo(invoiceE.getAddressInfo());
        invoiceERed.setThridReturnParameter(invoiceE.getThridReturnParameter());
        invoiceERed.setBuildingServiceInfo(invoiceE.getBuildingServiceInfo());
        invoiceERed.setRealEstateLeaseInfo(invoiceE.getRealEstateLeaseInfo());
        return invoiceERed;
    }

    /**
     * 构建红票InvoiceReceipt信息
     *
     * @param invoiceReceiptE
     * @param redInvoiceInfoBefore
     * @param redInvoiceAmount
     * @return
     */
    private InvoiceReceiptE generalRedInvoiceReceipt(InvoiceReceiptE invoiceReceiptE, List<InvoiceInfoDto> redInvoiceInfoBefore, Long redInvoiceAmount) {
        InvoiceReceiptE invoiceReceiptERed = new InvoiceReceiptE();
        invoiceReceiptERed.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        invoiceReceiptERed.setInvoiceReceiptNo(IdentifierFactory.getInstance().serialNumber("invoice_no","FPR", 21));
        invoiceReceiptERed.setType(invoiceReceiptE.getType());
        invoiceReceiptERed.setBillType(invoiceReceiptE.getBillType());
        invoiceReceiptERed.setCommunityId(invoiceReceiptE.getCommunityId());
        invoiceReceiptERed.setCommunityName(invoiceReceiptE.getCommunityName());
        invoiceReceiptERed.setCustomerId(invoiceReceiptE.getCustomerId());
        invoiceReceiptERed.setCustomerName(invoiceReceiptE.getCustomerName());
        invoiceReceiptERed.setCustomerPhone(null);
        invoiceReceiptERed.setStatutoryBodyId(invoiceReceiptE.getStatutoryBodyId());
        invoiceReceiptERed.setStatutoryBodyName(invoiceReceiptE.getStatutoryBodyName());
        invoiceReceiptERed.setCostCenterId(invoiceReceiptE.getCostCenterId());
        invoiceReceiptERed.setCostCenterName(invoiceReceiptE.getCostCenterName());
        invoiceReceiptERed.setApplyTime(LocalDateTime.now());
        invoiceReceiptERed.setRemark(invoiceReceiptE.getRemark());
        invoiceReceiptERed.setClerk(invoiceReceiptE.getClerk());
        invoiceReceiptERed.setSysSource(invoiceReceiptE.getSysSource());
        invoiceReceiptERed.setSource(invoiceReceiptE.getSource());
        invoiceReceiptERed.setInvSource(invoiceReceiptE.getInvSource());
        invoiceReceiptERed.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptERed.setState(InvoiceReceiptStateEnum.开票中.getCode());
        invoiceReceiptERed.setAppId(invoiceReceiptE.getAppId());
        invoiceReceiptERed.setAppName(invoiceReceiptE.getAppName());
        invoiceReceiptERed.setExtendFieldOne(invoiceReceiptE.getExtendFieldOne());
        invoiceReceiptERed.setTenantId(invoiceReceiptE.getTenantId());
        invoiceReceiptERed.setInvRecUnitId(invoiceReceiptE.getInvRecUnitId());
        invoiceReceiptERed.setInvRecUnitName(invoiceReceiptE.getInvRecUnitName());
        if (null != redInvoiceAmount) {
            invoiceReceiptERed.setPriceTaxAmount(-redInvoiceAmount);
        } else {
            if (CollectionUtils.isEmpty(redInvoiceInfoBefore)) {
                invoiceReceiptERed.setPriceTaxAmount(-(invoiceReceiptE.getPriceTaxAmount()));
            } else {
                Long redPriceTaxAmountSum = redInvoiceInfoBefore.stream().mapToLong(InvoiceInfoDto::getPriceTaxAmount).sum();
                invoiceReceiptERed.setPriceTaxAmount(-(invoiceReceiptE.getPriceTaxAmount() + redPriceTaxAmountSum));
            }
        }
        // 原蓝票开票时间暂时存储
        invoiceReceiptERed.setBillingTime(invoiceReceiptE.getBillingTime());
        return invoiceReceiptERed;
    }

    /**
     * 构建红票InvoiceReceipt信息
     *
     * @param invoiceReceiptE
     * @param thisRedInvoiceAmount
     * @return
     */
    private InvoiceReceiptE generalRedInvoiceReceiptByBillId(InvoiceReceiptE invoiceReceiptE, Long thisRedInvoiceAmount) {
        InvoiceReceiptE invoiceReceiptERed = new InvoiceReceiptE();
        invoiceReceiptERed.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        invoiceReceiptERed.setInvoiceReceiptNo(IdentifierFactory.getInstance().serialNumber("invoice_no", "FPR", 21));
        invoiceReceiptERed.setType(invoiceReceiptE.getType());
        invoiceReceiptERed.setBillType(invoiceReceiptE.getBillType());
        invoiceReceiptERed.setCommunityId(invoiceReceiptE.getCommunityId());
        invoiceReceiptERed.setCommunityName(invoiceReceiptE.getCommunityName());
        invoiceReceiptERed.setCustomerId(invoiceReceiptE.getCustomerId());
        invoiceReceiptERed.setCustomerName(invoiceReceiptE.getCustomerName());
        invoiceReceiptERed.setCustomerPhone(null);
        invoiceReceiptERed.setStatutoryBodyId(invoiceReceiptE.getStatutoryBodyId());
        invoiceReceiptERed.setStatutoryBodyName(invoiceReceiptE.getStatutoryBodyName());
        invoiceReceiptERed.setCostCenterId(invoiceReceiptE.getCostCenterId());
        invoiceReceiptERed.setCostCenterName(invoiceReceiptE.getCostCenterName());
        invoiceReceiptERed.setApplyTime(LocalDateTime.now());
        invoiceReceiptERed.setRemark(invoiceReceiptE.getRemark());
        invoiceReceiptERed.setClerk(invoiceReceiptE.getClerk());
        invoiceReceiptERed.setSysSource(invoiceReceiptE.getSysSource());
        invoiceReceiptERed.setInvSource(invoiceReceiptE.getInvSource());
        invoiceReceiptERed.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
        invoiceReceiptERed.setState(InvoiceReceiptStateEnum.开票中.getCode());
        invoiceReceiptERed.setAppId(invoiceReceiptE.getAppId());
        invoiceReceiptERed.setAppName(invoiceReceiptE.getAppName());
        invoiceReceiptERed.setExtendFieldOne(invoiceReceiptE.getExtendFieldOne());
        invoiceReceiptERed.setTenantId(invoiceReceiptE.getTenantId());
        invoiceReceiptERed.setPriceTaxAmount(-thisRedInvoiceAmount);
        invoiceReceiptERed.setInvRecUnitId(invoiceReceiptE.getInvRecUnitId());
        invoiceReceiptERed.setInvRecUnitName(invoiceReceiptE.getInvRecUnitName());
        return invoiceReceiptERed;
    }

    /**
     * 构造红冲数量
     *
     * @param num
     * @return
     */
    private String generalRedNum(String num) {
        if (StringUtils.isBlank(num)) {
            num = "-" + (StringUtils.isBlank(num) ? "1" : num);
        }
        return num;
    }

    /**
     * 处理账单已经红冲了的金额
     *
     * @param redInvoiceInfoBefore
     * @return
     */
    private Map<Long, List<InvoiceReceiptDetailE>> handleBillHasRedInvoice(List<InvoiceInfoDto> redInvoiceInfoBefore) {
        List<InvoiceReceiptDetailE> redInvoiceBill = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(redInvoiceInfoBefore)) {
            for (InvoiceInfoDto invoiceInfoDto : redInvoiceInfoBefore) {
                List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceInfoDto.getInvoiceReceiptDetailES();
                redInvoiceBill.addAll(invoiceReceiptDetailES);
            }
        }
        return redInvoiceBill.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
    }

    /**
     * 保存红票的第三方发票序列号
     *
     * @param invoiceSerialNum
     */
    public void addInvoiceSerialNum(String invoiceSerialNum) {
        InvoiceE redE = this.getInvoiceRedE();
        redE.setInvoiceSerialNum(invoiceSerialNum);
        redE.setInvoiceCode(null);
        redE.setInvoiceNo(null);
    }

}
