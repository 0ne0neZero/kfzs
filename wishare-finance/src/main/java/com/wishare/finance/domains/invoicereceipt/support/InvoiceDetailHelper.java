package com.wishare.finance.domains.invoicereceipt.support;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceInfoDto;
import com.wishare.finance.domains.configure.accountbook.facade.AmpFinanceFacade;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceDetailTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.WithTaxFlagEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Linitly
 * @date: 2023/7/22 16:04
 * @descrption: 发票明细帮助类
 */
@Slf4j
public class InvoiceDetailHelper {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final int DEFAULT_SCALE = 6;

    /**
     * 蓝票明细合并转换为统一发票明细
     * @param blueInvoiceDetailList
     * @param taxItemMapByChargeId
     * @return
     */
    public static List<InvoiceDetailDto> blueDetailsToInvoiceDetailDtoList(List<InvoiceReceiptDetailE> blueInvoiceDetailList,
                                                                     Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId) {
        if (CollectionUtils.isEmpty(blueInvoiceDetailList)) {
            return Collections.emptyList();
        }
        Map<String, List<InvoiceReceiptDetailE>> groupedBlueInvoiceDetailMap = InvoiceDetailHelper.detailGroup(blueInvoiceDetailList);
        List<InvoiceDetailDto> result = new ArrayList<>();
        // 价税合计，为实际金额*100
        int lineNum = 1;
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : groupedBlueInvoiceDetailMap.entrySet()) {
            // 组装实际要开票的开票明细
            InvoiceReceiptDetailE blueDetail = entry.getValue().get(0);
            InvoiceDetailDto detailDto = InvoiceDetailHelper.getCommonDtoMsgByDetail(blueDetail, taxItemMapByChargeId);
            detailDto.setKey(entry.getKey());
            detailDto.setDetailType(InvoiceDetailTypeEnum.蓝票明细.getCode());
            detailDto.setBlueLineNo(lineNum);

            // 循环分组后的invoiceDetail
            // 价税合计，为实际金额*100
            long totalPriceTax = entry.getValue().stream().filter(Objects::nonNull).mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();

            // 税额，小数位为DEFAULT_SCALE位
            BigDecimal tax = InvoiceDetailHelper.getTax(totalPriceTax, detailDto.getTaxRate(), detailDto.getWithTaxFlag());
            detailDto.setTax(tax);
            // 价税合计，小数位为DEFAULT_SCALE位
            detailDto.setGoodsTotalPriceTax(InvoiceDetailHelper.divideOneHundred(totalPriceTax));
            // 设置行号
            int finalLineNum = lineNum;
            entry.getValue().forEach(e -> e.setLineNo(finalLineNum));
            // 添加并重置
            result.add(detailDto);
            lineNum++;
        }

        return result;
    }

    /**
     * 部分红冲业务获取发票明细
     * @param blueInvoiceDetailList
     * @param redInvoiceInfoBefore
     * @param billId
     * @param redAmount
     * @return
     */
    public static List<InvoiceDetailDto> partialRedFlushToInvoiceDetailDtoList(List<InvoiceReceiptDetailE> blueInvoiceDetailList,
                                                                               List<InvoiceInfoDto> redInvoiceInfoBefore,
                                                                               Long billId, Long redAmount) {
        // 获取原蓝票的税目信息
        Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId = InvoiceDetailHelper.getTaxItemMapByChargeId(blueInvoiceDetailList);
        // 账单退款进行红冲时，存在两种情况：
        // 1:本次红冲金额，不等于该账单所属的实际开票明细剩余的红冲金额，部分红冲，税额直接计算即可;
        // 2:本次红冲金额，正好等于该账单所属的实际开票明细剩余的红冲金额，剩余红冲；即使本次红冲等于全部红冲，也可以同样处理（不用关注剩余发票明细）;
        // 还可能本次红冲金额大于明细剩余金额或该账单的剩余金额，这种情况为异常情况，先校验处理
        PartialRedHelp partialRedHelp = new PartialRedHelp();
        InvoiceDetailHelper.checkBillIdAndAmount(blueInvoiceDetailList, redInvoiceInfoBefore, billId, redAmount, partialRedHelp);
        // 如果刚好是本条开票的明细的全部剩余额度，此种方式计算税额也是相减，而不是部分红冲的计算
        boolean partialRedFlush = InvoiceDetailHelper.checkPartialRedFlush(blueInvoiceDetailList, redAmount, partialRedHelp);
        // 获取该账单所在原蓝票的行号
        Integer blueLineNo = InvoiceDetailHelper.getBlueLineNo(blueInvoiceDetailList, taxItemMapByChargeId, partialRedHelp.getCurrentRedFlushDetail());
        // 不相等，说明是部分红冲，不是剩余红冲
        // 相等，说明本次红冲刚好是该账单所在的明细的剩余红冲
        return partialRedFlush ?
                InvoiceDetailHelper.partialRedFlush(partialRedHelp.getCurrentRedFlushDetail(), redAmount,
                        partialRedHelp.getGroupKey(), taxItemMapByChargeId, blueLineNo)
                :
                InvoiceDetailHelper.residueRedFlush(partialRedHelp, taxItemMapByChargeId, blueLineNo);
    }

    private static Map<Long, List<TaxChargeItemD>> getTaxItemMapByChargeId(List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        AmpFinanceFacade ampFinanceFacade = Global.ac.getBean(AmpFinanceFacade.class);
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).distinct().collect(Collectors.toList());
        Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
        if (org.springframework.util.CollectionUtils.isEmpty(taxItemMapByChargeId)) {
            throw BizException.throw400("该批次费项未配置对应的税目编码");
        }
        return taxItemMapByChargeId;
    }

    /**
     * 获取部分红冲时，该账单所在原蓝票明细的行号
     * @param blueInvoiceDetailList
     * @param taxItemMapByChargeId
     * @param currentRedFlushDetail
     * @return
     */
    private static Integer getBlueLineNo(List<InvoiceReceiptDetailE> blueInvoiceDetailList, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId,
                                         InvoiceReceiptDetailE currentRedFlushDetail) {
        if (Objects.nonNull(currentRedFlushDetail.getLineNo())) {
            return currentRedFlushDetail.getLineNo();
        }
        List<InvoiceDetailDto> blueDetailList = InvoiceDetailHelper.blueDetailsToInvoiceDetailDtoList(blueInvoiceDetailList, taxItemMapByChargeId);
        String groupKey = InvoiceDetailHelper.getGroupKey(currentRedFlushDetail);
        InvoiceDetailDto detailDto = blueDetailList.stream().filter(e -> e.getKey().equals(groupKey)).findFirst().orElseGet(InvoiceDetailDto::new);
        return detailDto.getBlueLineNo();
    }

    /**
     * 剩余红冲
     *
     * @param partialRedHelp
     * @param taxItemMapByChargeId
     * @param blueLineNo
     * @return
     */
    private static List<InvoiceDetailDto> residueRedFlush(PartialRedHelp partialRedHelp,
                                                          Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, Integer blueLineNo) {
        InvoiceDetailDto detailDto = InvoiceDetailHelper.getCommonDtoMsgByDetail(partialRedHelp.getCurrentRedFlushDetail(), taxItemMapByChargeId);
        detailDto.setKey(partialRedHelp.getGroupKey());
        detailDto.setDetailType(InvoiceDetailTypeEnum.剩余红冲明细.getCode());
        detailDto.setBlueLineNo(blueLineNo);
        // 该明细原蓝票的总价税合计
        long blueDetailAmount = partialRedHelp.getSameGroupBlueDetailList().stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
        BigDecimal tax = InvoiceDetailHelper.getTax(blueDetailAmount, detailDto.getTaxRate(), detailDto.getWithTaxFlag());
        detailDto.setTax(tax);
        detailDto.setGoodsTotalPriceTax(InvoiceDetailHelper.divideOneHundred(blueDetailAmount));
        // 补充红冲的金额信息
        List<BigDecimal> redFlushedTotalPriceTax = new ArrayList<>();
        List<BigDecimal> redFlushedTax = new ArrayList<>();
        for (InvoiceReceiptDetailE redDetail : partialRedHelp.getSameGroupRedDetailList()) {
            BigDecimal redFlushedAmount = InvoiceDetailHelper.divideOneHundred(redDetail.getInvoiceAmount());
            redFlushedTotalPriceTax.add(redFlushedAmount);
            BigDecimal redTax = InvoiceDetailHelper.getTax(redDetail.getInvoiceAmount(), new BigDecimal(redDetail.getTaxRate()), redDetail.getWithTaxFlag());
            redFlushedTax.add(redTax);
        }
        detailDto.setRedFlushedTax(redFlushedTax);
        detailDto.setRedFlushedTotalPriceTax(redFlushedTotalPriceTax);
        return Collections.singletonList(detailDto);
    }

    /**
     * 部分红冲
     * @return
     */
    private static List<InvoiceDetailDto> partialRedFlush(InvoiceReceiptDetailE currentRedFlushDetail, Long redAmount, String groupKey,
                                                          Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, Integer blueLineNo) {
        InvoiceDetailDto detailDto = InvoiceDetailHelper.getCommonDtoMsgByDetail(currentRedFlushDetail, taxItemMapByChargeId);
        detailDto.setKey(groupKey);
        detailDto.setDetailType(InvoiceDetailTypeEnum.部分红冲明细.getCode());
        detailDto.setBlueLineNo(blueLineNo);
        BigDecimal tax = InvoiceDetailHelper.getTax(-redAmount, detailDto.getTaxRate(), detailDto.getWithTaxFlag());
        detailDto.setTax(tax);
        detailDto.setGoodsTotalPriceTax(InvoiceDetailHelper.divideOneHundred(-redAmount));
        return Collections.singletonList(detailDto);
    }

    /**
     * 校验账单ID和金额（账单ID在蓝票明细中，金额不超过该账单剩余能红冲的金额）
     * @param blueInvoiceDetailList
     * @param redInvoiceInfoBefore
     * @param billId
     * @param redAmount
     * @param partialRedHelp
     */
    private static void checkBillIdAndAmount(List<InvoiceReceiptDetailE> blueInvoiceDetailList,
                                             List<InvoiceInfoDto> redInvoiceInfoBefore,
                                             Long billId, Long redAmount, PartialRedHelp partialRedHelp) {
        // 红票明细获取并根据账单ID分组
        List<InvoiceReceiptDetailE> allRedFlushDetail = InvoiceDetailHelper.getAllRedFlushDetail(redInvoiceInfoBefore);
        Map<Long, List<InvoiceReceiptDetailE>> redDetailMapByBillId = allRedFlushDetail.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
        // 红冲对应的蓝票明细，部分红冲时使用
        InvoiceReceiptDetailE currentRedFlushDetail = null;
        for (InvoiceReceiptDetailE blueDetail : blueInvoiceDetailList) {
            if (!blueDetail.getBillId().equals(billId)) {
                continue;
            }
            currentRedFlushDetail = blueDetail;
            long redFlushedAmount = 0L;
            List<InvoiceReceiptDetailE> redFlushedDetailByBillId = redDetailMapByBillId.get(blueDetail.getBillId());
            if (CollectionUtils.isNotEmpty(redFlushedDetailByBillId)) {
                // 该账单红冲过，获取红冲过的金额，该值为负数
                redFlushedAmount = redFlushedDetailByBillId.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            }

            // 当前账单ID为此处部分红冲的账单ID，判断之前红冲过的金额+本次红冲金额是否超过了原蓝票该发票的金额
            redFlushedAmount += -redAmount;
            if (redFlushedAmount + blueDetail.getInvoiceAmount() < 0) {
                // 红冲的金额为负数，蓝牌明细金额为正数，相加小于0则超出了，目前的业务应该不会出现这个问题
                log.error("该账单红冲金额超过了剩余金额，账单ID：{}，账单剩余金额：{}，本次红冲金额：{}",
                        billId,
                        InvoiceDetailHelper.divideOneHundred(redFlushedAmount + redAmount + blueDetail.getInvoiceAmount()).toString(),
                        InvoiceDetailHelper.divideOneHundred(redAmount).toString());
                throw BizException.throw400("该账单红冲金额超过了剩余金额");
            }
            break;
        }
        if (Objects.isNull(currentRedFlushDetail)) {
            // 说明该账单不在发票中，出现未知错误
            log.error("该账单未找到对应发票信息，账单ID：{}，红冲金额：{}，发票收据主表ID：{}",
                    billId,
                    InvoiceDetailHelper.divideOneHundred(redAmount).toString(),
                    blueInvoiceDetailList.get(0).getInvoiceReceiptId());
            throw BizException.throw400("未找到对应蓝票信息，无法红冲");
        }
        // 设置帮助信息
        partialRedHelp.setCurrentRedFlushDetail(currentRedFlushDetail);
        partialRedHelp.setAllRedFlushDetail(allRedFlushDetail);
    }

    /**
     * 校验是否是部分红冲
     * @param blueInvoiceDetailList
     * @param redAmount
     * @param partialRedHelp
     * @return
     */
    private static boolean checkPartialRedFlush(List<InvoiceReceiptDetailE> blueInvoiceDetailList, Long redAmount, PartialRedHelp partialRedHelp) {
        // 如果刚好是本条开票的明细的全部剩余额度，此种方式计算税额也是相减，而不是部分红冲的计算
        Map<String, List<InvoiceReceiptDetailE>> blueDetailGroupMap = InvoiceDetailHelper.detailGroup(blueInvoiceDetailList);
        Map<String, List<InvoiceReceiptDetailE>> redDetailGroupMap = InvoiceDetailHelper.detailGroup(partialRedHelp.getAllRedFlushDetail());
        String groupKey = InvoiceDetailHelper.getGroupKey(partialRedHelp.getCurrentRedFlushDetail());
        partialRedHelp.setGroupKey(groupKey);
        partialRedHelp.setSameGroupBlueDetailList(blueDetailGroupMap.get(groupKey));
        List<InvoiceReceiptDetailE> redInvoiceReceiptList = CollectionUtils.isEmpty(redDetailGroupMap.get(groupKey)) ? Collections.emptyList() : redDetailGroupMap.get(groupKey);
        partialRedHelp.setSameGroupRedDetailList(redInvoiceReceiptList);
        // 该明细剩余金额
        long detailBlueAmount = blueDetailGroupMap.get(groupKey).stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
        long detailRedAmount = redInvoiceReceiptList.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
        // 上面限制了红冲时同一账单金额不会超过原蓝票金额；所以这里同一实际开票明细红冲金额不会超过蓝票金额
        return detailRedAmount + detailBlueAmount != redAmount;
    }

    /**
     * 全部红冲时获取统一发票明细
     * @param blueInvoiceDetailList
     * @param redInvoiceInfoBefore
     * @return
     */
    public static List<InvoiceDetailDto> allRedFlushToInvoiceDetailDtoList(List<InvoiceReceiptDetailE> blueInvoiceDetailList,
                                                                           List<InvoiceInfoDto> redInvoiceInfoBefore) {
        if (CollectionUtils.isEmpty(blueInvoiceDetailList)) {
            return Collections.emptyList();
        }
        // 获取原蓝票的税目信息
        Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId = InvoiceDetailHelper.getTaxItemMapByChargeId(blueInvoiceDetailList);
        // 最初的蓝票明细分组合并成实际开票的明细
        List<InvoiceDetailDto> blueDetailList = InvoiceDetailHelper.blueDetailsToInvoiceDetailDtoList(blueInvoiceDetailList, taxItemMapByChargeId);
        // 之前红冲的明细分组
        List<InvoiceReceiptDetailE> allRedFlushDetail = InvoiceDetailHelper.getAllRedFlushDetail(redInvoiceInfoBefore);
        Map<String, List<InvoiceReceiptDetailE>> redFlushedDetailMap = InvoiceDetailHelper.detailGroup(allRedFlushDetail);
        //
        for (InvoiceDetailDto detailDto : blueDetailList) {
            detailDto.setDetailType(InvoiceDetailTypeEnum.剩余红冲明细.getCode());
            List<InvoiceReceiptDetailE> redDetailList = redFlushedDetailMap.get(detailDto.getKey());
            if (CollectionUtils.isEmpty(redDetailList)) {
                detailDto.setRedFlushedTotalPriceTax(Collections.emptyList());
                detailDto.setRedFlushedTax(Collections.emptyList());
                continue;
            }
            // 该明细红冲过
            List<BigDecimal> redFlushedTotalPriceTax = new ArrayList<>();
            List<BigDecimal> redFlushedTax = new ArrayList<>();
            for (InvoiceReceiptDetailE redDetail : redDetailList) {
                // 红冲过的金额，该值为负数
                BigDecimal redFlushedAmount = InvoiceDetailHelper.divideOneHundred(redDetail.getInvoiceAmount());
                redFlushedTotalPriceTax.add(redFlushedAmount);
                BigDecimal redTax = InvoiceDetailHelper.getTax(redDetail.getInvoiceAmount(), new BigDecimal(redDetail.getTaxRate()), redDetail.getWithTaxFlag());
                redFlushedTax.add(redTax);
            }
            detailDto.setRedFlushedTax(redFlushedTax);
            detailDto.setRedFlushedTotalPriceTax(redFlushedTotalPriceTax);
        }
        return InvoiceDetailHelper.filterRedFlushedDetail(blueDetailList);
    }

    /**
     * 过滤已经红冲掉的明细(蓝票金额和红冲过的金额相加为0)
     * @param detailDtoList
     * @return
     */
    private static List<InvoiceDetailDto> filterRedFlushedDetail(List<InvoiceDetailDto> detailDtoList) {
        return detailDtoList.stream().filter(e -> {
            BigDecimal redAmount = e.getRedFlushedTotalPriceTax().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return (e.getGoodsTotalPriceTax().add(redAmount)).compareTo(BigDecimal.ZERO) != 0;
        }).collect(Collectors.toList());
    }

    /**
     * 获取所有之前红冲的明细
     * @param redInvoiceInfoBefore 红冲单信息
     * @return
     */
    private static List<InvoiceReceiptDetailE> getAllRedFlushDetail(List<InvoiceInfoDto> redInvoiceInfoBefore) {
        List<InvoiceReceiptDetailE> redFlushDetailList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(redInvoiceInfoBefore)) {
            for (InvoiceInfoDto invoiceInfoDto : redInvoiceInfoBefore) {
                redFlushDetailList.addAll(invoiceInfoDto.getInvoiceReceiptDetailES());
            }
        }
        return redFlushDetailList;
    }

    private static InvoiceDetailDto getCommonDtoMsgByDetail(InvoiceReceiptDetailE detail, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId) {
        InvoiceDetailDto detailDto = new InvoiceDetailDto();
        detailDto.setGoodsId(Objects.nonNull(detail.getChargeItemId()) ? detail.getChargeItemId().toString() : "");
        detailDto.setGoodsName(detail.getGoodsName());
        detailDto.setGoodsCode(InvoiceDetailHelper.getGoodsCode(detail, taxItemMapByChargeId));
        detailDto.setGoodsUnit(detail.getUnit());
        // 单价并没有设置小数位，正常来讲，是通过单价*数量来获取明细金额；然我方开票是合并取数量1来处理，即单价也就是
        detailDto.setGoodsNum(1);
//        detailDto.setGoodsPrice(StringUtils.isBlank(detail.getPrice()) ? BigDecimal.ZERO : new BigDecimal(detail.getPrice()));
        detailDto.setGoodsSpecification(detail.getSpectype());
        detailDto.setWithTaxFlag(detail.getWithTaxFlag());
        //
//            detailDto.setFreeTaxMark();
        detailDto.setTaxRate(StringUtils.isBlank(detail.getTaxRate()) ? BigDecimal.ZERO : new BigDecimal(detail.getTaxRate()));
        return detailDto;
    }

    /**
     * 获取商品编码
     */
    private static String getGoodsCode(InvoiceReceiptDetailE invoiceReceiptDetailE, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId) {
        if (org.springframework.util.CollectionUtils.isEmpty(taxItemMapByChargeId)) {
            return "";
        }
        List<TaxChargeItemD> taxChargeItemDS = taxItemMapByChargeId.get(invoiceReceiptDetailE.getChargeItemId());
        if (org.springframework.util.CollectionUtils.isEmpty(taxChargeItemDS)) {
            throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
        }
        TaxChargeItemD taxChargeItemRv = taxChargeItemDS.get(0);
        if (taxChargeItemRv == null) {
            throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
        }
        return taxChargeItemRv.getTaxItem().getCode();
    }

    /**
     * 分组
     * @param invoiceDetailList
     * @return
     */
    public static Map<String, List<InvoiceReceiptDetailE>> detailGroup(List<InvoiceReceiptDetailE> invoiceDetailList) {
        return invoiceDetailList.stream().collect(
                Collectors.groupingBy(InvoiceDetailHelper::getGroupKey, TreeMap::new, Collectors.toList())
        ).descendingMap();
    }

    /**
     * 获取分组key
     * @param detail
     * @return
     */
    private static String getGroupKey(InvoiceReceiptDetailE detail) {
        return detail.getChargeItemId() + "-" + detail.getTaxRate();
    }

    /**
     * 除以100
     * @param amount
     * @return
     */
    public static BigDecimal divideOneHundred(Long amount, int resultScale) {
        if (Objects.isNull(amount) || amount.compareTo(0L) == 0) {
            // 为空或者等于0
            return BigDecimal.ZERO;
        }
        // 这里设置的小数多一些，对接第三方时自己转换自己需要的小数位
        return new BigDecimal(amount).divide(ONE_HUNDRED, resultScale, RoundingMode.HALF_EVEN);
    }

    /**
     * 除以100
     * @param amount
     * @return
     */
    public static BigDecimal divideOneHundred(Long amount) {
        return divideOneHundred(amount, DEFAULT_SCALE);
    }

    /**
     * 获取税额
     * @param priceTaxAmount
     * @param taxRate
     * @param withTaxFlag
     * @return
     */
    public static BigDecimal getTax(Long priceTaxAmount, BigDecimal taxRate, Integer withTaxFlag, int resultScale) {
        BigDecimal tax;
        if (WithTaxFlagEnum.含税.getCode().equals(withTaxFlag)) {
            // 含税
            tax = InvoiceDetailHelper.getTaxByIncludeTaxAmount(priceTaxAmount, taxRate, resultScale);
        } else {
            // 不含税
            tax = InvoiceDetailHelper.getTaxByExcludeTaxAmount(priceTaxAmount, taxRate, resultScale);
        }
        return tax;
    }

    /**
     * 获取税额
     * @param priceTaxAmount
     * @param taxRate
     * @param withTaxFlag
     * @return
     */
    public static BigDecimal getTax(Long priceTaxAmount, BigDecimal taxRate, Integer withTaxFlag) {
        return getTax(priceTaxAmount, taxRate, withTaxFlag, DEFAULT_SCALE);
    }

    /**
     * 计算税额
     * @param excludeTaxAmount 不含税金额，实际金额*100，正整数
     * @param taxRate 税率
     * @return
     */
    public static BigDecimal getTaxByExcludeTaxAmount(Long excludeTaxAmount, BigDecimal taxRate) {
        return getTaxByExcludeTaxAmount(excludeTaxAmount, taxRate, DEFAULT_SCALE);
    }

    /**
     * 计算税额
     * @param excludeTaxAmount 不含税金额，实际金额*100，正整数
     * @param taxRate 税率
     * @param resultScale 结果小数位
     * @return
     */
    public static BigDecimal getTaxByExcludeTaxAmount(Long excludeTaxAmount, BigDecimal taxRate, int resultScale) {
        // 税额
        BigDecimal tax = BigDecimal.ZERO;
        // 金额或者税率为空为0，返回0
        if (Objects.isNull(excludeTaxAmount) || Objects.isNull(taxRate) || taxRate.compareTo(BigDecimal.ZERO) <= 0) {
            return tax;
        }
        // 实际金额
        BigDecimal price = InvoiceDetailHelper.divideOneHundred(excludeTaxAmount);
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            // 金额为0，返回0
            return tax;
        }
        // 税额=金额*税率
        tax = price.multiply(taxRate).setScale(resultScale, RoundingMode.HALF_EVEN);
        return tax;
    }

    /**
     * 计算税额
     * @param includeTaxAmount 含税金额，实际金额*100，正整数
     * @param taxRate 税率
     * @return
     */
    public static BigDecimal getTaxByIncludeTaxAmount(Long includeTaxAmount, BigDecimal taxRate) {
        return getTaxByIncludeTaxAmount(includeTaxAmount, taxRate, DEFAULT_SCALE);
    }

    /**
     * 计算税额
     * @param includeTaxAmount 含税金额，实际金额*100，正整数
     * @param taxRate 税率
     * @param resultScale 结果小数位
     * @return
     */
    public static BigDecimal getTaxByIncludeTaxAmount(Long includeTaxAmount, BigDecimal taxRate, int resultScale) {
        // 税额
        BigDecimal tax = BigDecimal.ZERO;
        // 含税金额或者税率为空为0，返回0
        if (Objects.isNull(includeTaxAmount) || Objects.isNull(taxRate) || taxRate.compareTo(BigDecimal.ZERO) <= 0) {
            return tax;
        }
        // 实际含税金额
        BigDecimal priceTaxAmount = InvoiceDetailHelper.divideOneHundred(includeTaxAmount);
        if (priceTaxAmount.compareTo(BigDecimal.ZERO) == 0) {
            // 金额为0，返回0
            return tax;
        }
        // 税额=(数量*含税单价=含税金额)*税率/(1+税率）
        tax = priceTaxAmount.multiply(taxRate).divide(taxRate.add(BigDecimal.ONE), resultScale, RoundingMode.HALF_EVEN);
        return tax;
    }

    // 部分红冲帮助类
    @Data
    public static class PartialRedHelp {

        // 当前红冲账单对应的明细（我方数据库明细）
        private InvoiceReceiptDetailE currentRedFlushDetail;

        // 该账单对应的蓝票的所有红冲的明细
        private List<InvoiceReceiptDetailE> allRedFlushDetail;

        // 该账单对应的实际开票明细的分组key
        private String groupKey;

        // 该账单对应的蓝票明细（同一分组）
        private List<InvoiceReceiptDetailE> sameGroupBlueDetailList;

        // 该账单对应的红票明细（同一分组）
        private List<InvoiceReceiptDetailE> sameGroupRedDetailList;
    }
}
