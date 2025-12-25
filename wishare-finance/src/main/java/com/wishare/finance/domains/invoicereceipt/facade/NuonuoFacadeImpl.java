package com.wishare.finance.domains.invoicereceipt.facade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillDetailV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.FastInvoiceRedF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.BillingNewV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemAppService;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceRedA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceRedApplyE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceZoningE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceZoningRepository;
import com.wishare.finance.domains.invoicereceipt.support.nuonuo.NuonuoSupport;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.NuonuoApplySourceEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoPushModeEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoRedReasonEnum;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.*;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.exception.BizException;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 发票外部申请接口-诺诺实现
 * @author xujian
 * @date 2022/9/18
 * @Description:
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "invoice.supplier",havingValue = "nuonuo")
public class NuonuoFacadeImpl extends InvoiceExternalAbService {

    @Setter(onMethod_ = {@Autowired})
    private ExternalClient externalClient;

    @Setter(onMethod_ = {@Autowired})
    private NuonuoSupport nuonuoSupport;

    @Setter(onMethod_ = {@Autowired})
    private TaxItemAppService taxItemAppService;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceZoningRepository invoiceZoningRepository;

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;
    @Value("${invoice.callBackUrl:demo}")
    private String callBackUrl;

    /**
     * 诺诺申请开票
     *
     * @return
     */
    @Override
    public String nuonuoBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                   List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                   String nuonuoCommunityId,
                                   Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                                   InvoiceTypeEnum invoiceTypeEnum) {
        OrderF order;
        switch (invoiceTypeEnum) {
            case 红票:
                order = invoiceRed(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, nuonuoCommunityId, taxItemMapByChargeId);
                break;
            case 蓝票:
            default:
                order = handleNuonuoOrderBlue(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, handlePushModeStr(invoiceE.getPushMode()), nuonuoCommunityId, taxItemMapByChargeId);
                break;
        }
        BillingNewF billingNewF = new BillingNewF();
        billingNewF.setTenantId(tenantId);
        billingNewF.setOrder(order);
        billingNewF.setTaxnum(invoiceE.getSalerTaxNum());
        BillingNewV billingNewV = nuonuoSupport.billingNew(billingNewF);
        log.info("诺诺申请开票反参,流水号为：{}", JSON.toJSONString(billingNewV));
        return billingNewV.getInvoiceSerialNum();
    }

    @Override
    public String paperInvoices(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, InvoiceTypeEnum invoiceTypeEnum, String billInfoNo) {
        return null;
    }

    @Override
    public String invoicesPrints(InvoicePrintF form, String tenantId) {
       return null;
    }


    /**
     * 诺诺全电开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @return
     */
    @Override
    public String opeMplatformBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                         List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                         String nuonuoCommunityId,
                                         Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId) {
        OrderF order = handleNuonuoOrderBlue(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, handlePushModeStr(invoiceE.getPushMode()), nuonuoCommunityId, taxItemMapByChargeId);
        if (invoiceE.getExtensionNumber() != null) {
            order.setExtensionNumber(invoiceE.getExtensionNumber().toString());
        }
        if (invoiceE.getTerminalNumber() != null) {
            order.setTerminalNumber(invoiceE.getTerminalNumber().toString());
        }
        BillingNewF billingNewF = new BillingNewF();
        billingNewF.setTenantId(tenantId);
        billingNewF.setOrder(order);
        billingNewF.setTaxnum(invoiceE.getSalerTaxNum());
        BillingNewV billingNewV = nuonuoSupport.opeMplatformBillingNew(billingNewF);
        log.info("诺诺全电开票反参：{}", JSON.toJSONString(billingNewV));
        return billingNewV.getInvoiceSerialNum();
    }

    /**
     * 全电红冲申请单
     * @return
     */
    @Override
    public String electronInvoiceRedApply(InvoiceRedA invoiceRedA) {
        InvoiceReceiptE invoiceReceiptRedE = invoiceRedA.getInvoiceReceiptRedE();
        InvoiceE invoiceRedE = invoiceRedA.getInvoiceRedE();
        ElectronInvoiceRedApplyF redApplyF = this.generateRedApply(invoiceRedE, invoiceReceiptRedE, String.valueOf(invoiceRedA.getInvoiceRedApplyE().getId()));
        nuonuoSupport.electronInvoiceRedApply(redApplyF);
        // 返回的id是红字确认单记录的id，不需要回填到发票的流水号字段中，在红字确认成功后会快捷红冲然后才有真正的发票流水号
        return null;
    }

    @Override
    public List<NuonuoRedApplyQueryV> electronInvoiceRedApplyQuery(InvoiceRedApplyE applyE, InvoiceE invoiceE) {
        ElectronInvoiceRedQueryF queryF = this.generateRedApplyQuery(applyE, invoiceE.getSalerTaxNum());
        String result = nuonuoSupport.electronInvoiceRedQuery(queryF);
        JSONObject jsonObject = JSON.parseObject(result);
        Integer total = jsonObject.getInteger("total");
        if (total > 0) {
            List<NuonuoRedApplyQueryV> resultList = JSON.parseArray(jsonObject.getString("list"),
                    NuonuoRedApplyQueryV.class);
            NuonuoRedApplyQueryV nuonuoRedApplyQueryV = resultList.get(0);
            nuonuoRedApplyQueryV.setErrMsg(nuonuoRedApplyQueryV.getBillMessage());
            return resultList;
        }
        return new ArrayList<>();
    }

    /**
     * 诺诺全电发票快捷冲红
     * @return
     */
    @Override
    public String electronInvoiceRed(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV) {
        FastInvoiceRedF fastInvoiceRedF = new FastInvoiceRedF();
        fastInvoiceRedF.setTenantId(invoiceE.getTenantId());
        fastInvoiceRedF.setOrderNo(String.valueOf(invoiceE.getInvoiceReceiptId()));
        fastInvoiceRedF.setBillUuid(queryV.getBillUuid());
        fastInvoiceRedF.setInvoiceNumber(invoiceE.getInvoiceNo());
        fastInvoiceRedF.setInvoiceCode(invoiceE.getInvoiceCode());
        fastInvoiceRedF.setBillNo(queryV.getBillNo());
        fastInvoiceRedF.setTaxnum(invoiceE.getSalerTaxNum());
        fastInvoiceRedF.setTaxNum(invoiceE.getSalerTaxNum());
        BillingNewV billingNewV = nuonuoSupport.fastInvoiceRed(fastInvoiceRedF);
        log.info("诺诺全电开票反参：{}", JSON.toJSONString(billingNewV));
        return billingNewV.getInvoiceSerialNum();
    }

    /**
     * 诺诺开票结果查询接口
     *
     * @param serialNum
     */
    @Override
    public List<QueryInvoiceResultV> queryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo) {
        List<String> serialNums = Arrays.asList(serialNum.split(","));
        QueryInvoiceResultF queryInvoiceResultF = new QueryInvoiceResultF();
        queryInvoiceResultF.setTenantId(tenantId);
        queryInvoiceResultF.setSerialNos(serialNums);
        queryInvoiceResultF.setTaxnum(taxnum);
        queryInvoiceResultF.setIsOfferInvoiceDetail("1");//是否需要提供明细 1-是, 0-否(不填默认 0)
        XxlJobHelper.log("[开始查询开票中的发票]-[serialNum = {}]", serialNum);
        List<QueryInvoiceResultV> queryInvoiceResultRVS = nuonuoSupport.queryInvoiceResult(queryInvoiceResultF);
        XxlJobHelper.log("[开始查询开票中的发票]-[结果]-[queryInvoiceResultRVS = {}]", JSON.toJSONString(queryInvoiceResultRVS));
        if(CollectionUtils.isEmpty(queryInvoiceResultRVS)){return null;}
        List<String> status = queryInvoiceResultRVS.stream().map(QueryInvoiceResultV::getStatus)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

        return status.size() == 1?queryInvoiceResultRVS:null;
    }

    /**
     * 诺诺全电开票结果查询接口
     *
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @param orderNo 诺诺对应账单编号，我们这边是invoice_receipt_id
     */
    @Override
    public List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo,InvoiceE invoiceE) {
        QueryInvoiceResultF queryInvoiceResultF = new QueryInvoiceResultF();
        queryInvoiceResultF.setTenantId(tenantId);
        if (StringUtils.isNotBlank(serialNum)) {
            queryInvoiceResultF.setSerialNos(Lists.newArrayList(serialNum));
        } else {
            queryInvoiceResultF.setOrderNos(Lists.newArrayList(String.valueOf(orderNo)));
        }
        queryInvoiceResultF.setTaxnum(taxnum);
        queryInvoiceResultF.setIsOfferInvoiceDetail("1");//是否需要提供明细 1-是, 0-否(不填默认 0)
        List<QueryInvoiceResultV> queryInvoiceResultRVS = nuonuoSupport.opeMplatformQueryInvoiceResult(queryInvoiceResultF);
        if (CollectionUtils.isNotEmpty(queryInvoiceResultRVS)) {
            List<String> status = queryInvoiceResultRVS.stream().map(QueryInvoiceResultV::getStatus).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (status.size() == 1) {
                return queryInvoiceResultRVS;
            }
        }
        return null;
    }

    /**
     * 开票重试接口
     *
     * @return
     */
    @Override
    public String reInvoice(String tenantId, String serialNum) {
        ReInvoiceF reInvoiceF = new ReInvoiceF();
        reInvoiceF.setTenantId(tenantId);
        reInvoiceF.setFpqqlsh(serialNum);
        return externalClient.reInvoice(reInvoiceF);
    }


    @Override
    public void checkRealPropertyRentInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                                     List<Long> chargeItemIds,  InvoiceReceiptE invoiceReceiptE ) {
        // 判断发票类型  如果不是全电发票就return
        // 只校验全电普票
        if(!InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) && !InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())){
            return;
        }
        // 判断是否包含 3040502029902000000-经营租赁租赁费  税目编码  且是否只有这一种税目编码
        // 判断账单是否是 同一个房号
        List<TaxChargeItemD> taxChargeItemRvs = taxItemAppService.queryByChargeIdList(chargeItemIds);
        if (taxChargeItemRvs.stream().anyMatch(s -> s.getTaxItem().getCode().equals("3040502029902000000"))) {
            // update by  hq 2023-11-20 新需求
            // 取消单条限制
            // 允许同房号、同费项、账单周期连续的账单批量开票

           // 判断账单是否只有一条
//            if (CollectionUtils.isNotEmpty(billDetailMoreVList) && billDetailMoreVList.size() != 1){
//                throw BizException.throw400("不动产经营租赁服务只允许单个账单开票!");
//            }
            // 判断房间是否一致
            List<String> list = billDetailMoreVList.stream().map(BillDetailMoreV::getRoomId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(list) && list.size() != 1,"该批次账单房号不一致!");
            // 判断费项是否一致
            List<Long> chargeItemIdList = billDetailMoreVList.stream().map(BillDetailMoreV::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(chargeItemIdList) && chargeItemIdList.size() != 1,"该批次账单费项不一致!");
            // 判断账单周期是否连续
            List<LocalDateTime> localDateTimeList = new ArrayList<>();
            // 按照账单id分组
            Map<String, List<BillDetailMoreV>> billGroupByBillNo = billDetailMoreVList.stream().collect(Collectors.groupingBy(BillDetailV::getBillNo));
            for (Map.Entry<String, List<BillDetailMoreV>> entry : billGroupByBillNo.entrySet()) {
                localDateTimeList.add(entry.getValue().get(0).getStartTime());
                localDateTimeList.add(entry.getValue().get(0).getEndTime());
            }
            // 按照时间排序
            List<LocalDateTime> collect = localDateTimeList.stream().sorted().collect(Collectors.toList());
            ErrorAssertUtils.isTrueThrow400(this.isContains(collect),"该批次账单周期不连续");

            // 通过房号去空间中心获取
            BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
            String roomId = billDetailMoreV.getRoomId();
            ErrorAssertUtils.isFalseThrow400(StringUtils.isBlank(roomId),"该账单下不存在房间");
            List<Long> longList = new ArrayList<>();
            longList.add(Long.valueOf(roomId));
            List<SpaceDetails> details = spaceClient.getDetails(longList);
            log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(details));
            if (CollectionUtils.isEmpty(details)){return;}

            StringBuilder stringBuilder = new StringBuilder();
            SpaceDetails spaceDetails = details.get(0);
            SpaceCommunityV communityInfo = spaceClient.getCommunityDetail(spaceDetails.getCommunityId());
            log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(communityInfo));
            ErrorAssertUtils.isFalseThrow400(StringUtils.isBlank(communityInfo.getProvince()) && StringUtils.isBlank(communityInfo.getCity())
                    ,"开票失败，开票项目包含不动产经营租赁服务，请先在园区档案中完善地址信息后再进行开票!");
            // 根据名字 获取省级的code 获取诺诺提供的区划
            // 获取 该code下的市一级
            // 该code 没有下一级  则
            // 判断财务中台的区级 是否在诺诺区划中 若在 则该
            InvoiceZoningE byAreaName = invoiceZoningRepository.getByAreaName(communityInfo.getProvince());
            stringBuilder.append(byAreaName.getAreaName());
            List<InvoiceZoningE> invoiceZoningBySuperiorCode = invoiceZoningRepository.getInvoiceZoningBySuperiorCode(byAreaName.getAreaCode());
            // 根据父id,获取列表
            for (InvoiceZoningE invoiceZoningE : invoiceZoningBySuperiorCode) {
                if (communityInfo.getCity().equals(invoiceZoningE.getAreaName()) || communityInfo.getArea().equals(invoiceZoningE.getAreaName())) {
                    // 判断type 级别 如果是3  直接就是区级别 直接拼接  例如直辖市
                    if (StringUtils.equals(invoiceZoningE.getAreaLevel(),"3")) {
                        stringBuilder.append(invoiceZoningE.getAreaName());
                    } else {
                        // 如果是2  则获取子列表
                        stringBuilder.append(invoiceZoningE.getAreaName());
                        //
                        List<InvoiceZoningE> zoningBySuperiorCode = invoiceZoningRepository.getInvoiceZoningBySuperiorCode(invoiceZoningE.getAreaCode());
                        // 如果查询不到 则该级别没有下一层级  不需要进行其他操作
                        // 否则就循环 获取 对应的区 去匹配
                        if (CollectionUtils.isEmpty(zoningBySuperiorCode)) {continue;}
                        for (InvoiceZoningE zoningE : zoningBySuperiorCode) {
                            if (StringUtils.equals(zoningE.getAreaName(), communityInfo.getArea())) {
                                stringBuilder.append(zoningE.getAreaName());
                            }
                        }
                    }
                    break;
                }
            }
            ErrorAssertUtils.isFalseThrow400(StringUtils.isBlank(communityInfo.getAddress()), "开票失败，开票项目包含不动产经营租赁服务，请先在园区档案中完善地址信息后再进行开票。");
            /** 诺诺经营租赁租赁费 开票 不动产的 详细地址 */
            invoiceE.setAddressInfo(this.assembleAddressInfo(stringBuilder, spaceDetails, communityInfo, collect));
        }
    }

    @Override
    public String invoiceRedApply(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, InvoiceTypeEnum invoiceTypeEnum) {
        return null;
    }


    /**
     * 组装 诺诺经营租赁租赁费 开票 不动产的 详细地址
     * @param stringBuilder
     * @param spaceDetails
     * @param communityInfo
     * @param collect
     * @return
     */
    private String assembleAddressInfo(StringBuilder stringBuilder,SpaceDetails spaceDetails,SpaceCommunityV communityInfo,List<LocalDateTime> collect){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("realPropertyAddress",stringBuilder);
        String replace = spaceDetails.getAddress().contains(stringBuilder) ?
                communityInfo.getAddress().replace(stringBuilder, "") : communityInfo.getAddress();
        jsonObject.put("detailAddress",replace);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jsonObject.put("rentStartDate", collect.get(0).format(dateTimeFormatter));
        jsonObject.put("rentEndDate", collect.get(collect.size()-1).format(dateTimeFormatter));
        jsonObject.put("crossCityFlag", "0");
        jsonObject.put("realPropertyCertificate", "");
        jsonObject.put("unit", "2");
        String jsonString = JSONObject.toJSONString(jsonObject);
        log.info("不动产发票开具地址信息:" + jsonString);
        return jsonString;
    }

    public  Boolean isContains(List<LocalDateTime> localDateTimeList){
        // 如果只有两个对象 则直接返回true
        if (localDateTimeList.size() == 2){
            return true;
        } else if (localDateTimeList.size() > 0){
            // 遍历  0开始   12   连续  34 连续  。。。
            for (int i = 1; i < localDateTimeList.size() - 1; i+=2) {
                // 判断第一组结束时间的下一天是否等于第二组的开始时间。
                LocalDate localDate = LocalDate.from(localDateTimeList.get(i).plusDays(1));
                LocalDate from = LocalDate.from(localDateTimeList.get(i + 1));
                if (!localDate.isEqual(from)){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    /**
     * 处理诺诺开蓝票入参 (根据同费项，同税率合并明细)
     *
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param pushMode
     * @param nuonuoCommunityId
     * @param taxIteamMapByChargeId
     * @return
     */
    public OrderF handleNuonuoOrderBlue(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                        List<Integer> pushMode, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        OrderF orderF = generalOrderF(invoiceReceiptE, invoiceE, pushMode, nuonuoCommunityId);
        orderF.setInvoiceDetail(generalInvoiceDetailMerge(invoiceE, invoiceReceiptDetailES, taxIteamMapByChargeId, invoiceReceiptE));
        return orderF;
    }


    @Override
    public ElectronInvoiceRedApplyF generateRedApply(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptRedE, String billId) {
        ElectronInvoiceRedApplyF redApplyF = new ElectronInvoiceRedApplyF();
        redApplyF.setTenantId(invoiceReceiptRedE.getTenantId());
        redApplyF.setBlueInvoiceLine(InvoiceLineEnum.valueOfByCode(invoiceReceiptRedE.getType()).getNuonuoCode());
        redApplyF.setApplySource(String.valueOf(NuonuoApplySourceEnum.销方.getCode()));
        redApplyF.setBlueInvoiceNumber(invoiceE.getInvoiceNo());
        redApplyF.setSellerTaxNo(invoiceE.getSalerTaxNum());
        redApplyF.setBuyerName(invoiceE.getBuyerName());
        redApplyF.setBuyerTaxNo(invoiceE.getBuyerTaxNum());
        redApplyF.setRedReason(NuonuoRedReasonEnum.开票有误.getCode());
        redApplyF.setBillId(billId);
        return redApplyF;
    }

    @Override
    public ElectronInvoiceRedQueryF generateRedApplyQuery(InvoiceRedApplyE applyE, String taxnum) {

        ElectronInvoiceRedQueryF queryF = new ElectronInvoiceRedQueryF();
        queryF.setIdentity(String.valueOf(NuonuoApplySourceEnum.销方.getCode()));
        queryF.setBillId(String.valueOf(applyE.getId()));
        queryF.setTenantId(applyE.getTenantId());
        queryF.setTaxnum(taxnum);
        return queryF;
    }

    /**
     * 根据同费项，同税率合并明细
     *
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @return
     */
    private List<InvoiceDetailF> generalInvoiceDetailMerge(InvoiceE invoiceE,
                                                           List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                           Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId,
                                                           InvoiceReceiptE invoiceReceiptE) {
        Map<String, List<InvoiceReceiptDetailE>> invoiceReceiptDetailGroupMap = invoiceReceiptDetailES.stream().collect(
                Collectors.groupingBy(
                        detail -> detail.getChargeItemId() + "-" + detail.getTaxRate(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<InvoiceDetailF> invoiceDetailFList = Lists.newArrayList();
        // 行号
        int lineNo = 1;
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : invoiceReceiptDetailGroupMap.entrySet()) {
            InvoiceDetailF invoiceDetailF = generalMergeInvoiceDetail(invoiceE, entry.getValue(), taxIteamMapByChargeId,
                    invoiceReceiptE,lineNo);
            lineNo += 1;
            invoiceDetailFList.add(invoiceDetailF);
        }
        return invoiceDetailFList;
    }

    /**
     * 红冲使用，根据同费项，同税率合并明细
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @return
     */
    private List<InvoiceDetailF> generalInvoiceDetailMergeForRed(List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Map<Long,
            List<TaxChargeItemD>> taxIteamMapByChargeId,InvoiceE invoiceE) {
        Map<String, List<InvoiceReceiptDetailE>> invoiceReceiptDetailGroupMap = invoiceReceiptDetailES.stream().collect(
                Collectors.groupingBy(
                        detail -> detail.getChargeItemId() + "-" + detail.getTaxRate(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<InvoiceDetailF> invoiceDetailFList = Lists.newArrayList();
        int lineNo=1;
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : invoiceReceiptDetailGroupMap.entrySet()) {
            InvoiceDetailF invoiceDetailF = generalMergeInvoiceDetailForRed(entry.getValue(), taxIteamMapByChargeId,invoiceE.getFreeTax(),lineNo);
            lineNo+=1;
            invoiceDetailFList.add(invoiceDetailF);
        }
        return invoiceDetailFList;
    }

    /**
     * 红冲使用，构建合并之后的明细
     *
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @param lineNo
     * @return
     */
    private InvoiceDetailF generalMergeInvoiceDetailForRed(List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Map<Long,
            List<TaxChargeItemD>> taxIteamMapByChargeId, Integer freeTax, int lineNo) {
        InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
        BigDecimal taxExcludedAmountSum = BigDecimal.ZERO;
        BigDecimal taxSum = BigDecimal.ZERO;
        BigDecimal invoiceAmountSum = BigDecimal.ZERO;
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailES) {
            invoiceReceiptDetailE.setLineNo(lineNo);
            invoiceDetailF.setWithTaxFlag(invoiceReceiptDetailE.getWithTaxFlag().toString());
            invoiceDetailF.setTaxRate(invoiceReceiptDetailE.getTaxRate());
            //零税率标识  1,免税;2,不征税;3,普通零税率；
            if (StringUtils.isBlank(invoiceReceiptDetailE.getTaxRate()) || invoiceReceiptDetailE.getTaxRate().equals("0.00000")
                    || (Objects.nonNull(freeTax) && freeTax == 1)) {
                //暂定普通零税率
                invoiceDetailF.setZeroRateFlag("3");
                invoiceDetailF.setTaxRate("0");
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                taxExcludedAmountSum = taxExcludedAmountSum.add(invoiceAmountSum);
            } else {
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
                BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(invoiceReceiptDetailE.getTaxRate()));

                //含税金额
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                //不含税金额 = 含税金额/(1+税率)
                BigDecimal taxExcludedAmount = invoiceAmount.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
                taxExcludedAmountSum = taxExcludedAmountSum.add(taxExcludedAmount);

                //税额=(数量*含税单价)*税率/(1+税率）
                BigDecimal denominator = new BigDecimal(1).multiply(invoiceAmount).multiply(new BigDecimal(invoiceReceiptDetailE.getTaxRate()));
                BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
                taxSum = taxSum.add(tax);
            }
            invoiceDetailF.setGoodsCode(handleGoodsCode(invoiceReceiptDetailE, taxIteamMapByChargeId));
            invoiceDetailF.setGoodsName(invoiceReceiptDetailE.getGoodsName());
        }
        invoiceDetailF.setSelfCode(null);
        invoiceDetailF.setInvoiceLineProperty("0");
        invoiceDetailF.setNum("-1");
        invoiceDetailF.setFavouredPolicyFlag(null);
        invoiceDetailF.setUnit(null);
        invoiceDetailF.setDeduction(null);
        //单价为合并展示账单的开票金额
        invoiceDetailF.setPrice(String.valueOf(invoiceAmountSum.negate()));
        invoiceDetailF.setTaxIncludedAmount(invoiceAmountSum.toString());//含税金额
        invoiceDetailF.setTax(handleTax(freeTax, invoiceAmountSum, invoiceReceiptDetailES.get(0).getTaxRate()));//处理税额
        invoiceDetailF.setTaxExcludedAmount(handleTaxExcludedAmount(freeTax, invoiceAmountSum,invoiceReceiptDetailES.get(0).getTaxRate()));//处理不含税金额
        // 将票面税额与不含税金额冗余存在明细表
        invoiceReceiptDetailES.forEach(detail -> {
            detail.setFaceTaxAmount(AmountUtils.toLong(invoiceDetailF.getTax()));
            detail.setFaceTaxExcludedAmount(AmountUtils.toLong(invoiceDetailF.getTaxExcludedAmount()));
        });
        return invoiceDetailF;
    }


    /**
     * 构建合并之后的明细
     *
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @param lineNo
     * @return
     */
    private InvoiceDetailF generalMergeInvoiceDetail(InvoiceE invoiceE,
                                                     List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                     Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId,
                                                     InvoiceReceiptE invoiceReceiptE, int lineNo) {
        InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
        BigDecimal taxExcludedAmountSum = BigDecimal.ZERO;
        BigDecimal taxSum = BigDecimal.ZERO;
        BigDecimal invoiceAmountSum = BigDecimal.ZERO;
        Integer freeTax = invoiceE.getFreeTax();
        BigDecimal simpleCollectionRate = new BigDecimal("0.05");
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailES) {
            // 行号
            invoiceReceiptDetailE.setLineNo(lineNo);
            invoiceDetailF.setInvoiceLineProperty("0");
            invoiceDetailF.setNum("1");
            invoiceDetailF.setWithTaxFlag(invoiceReceiptDetailE.getWithTaxFlag().toString());
            invoiceDetailF.setFavouredPolicyFlag(null);
            invoiceDetailF.setTaxRate(invoiceReceiptDetailE.getTaxRate());
            invoiceDetailF.setUnit(null);
            invoiceDetailF.setDeduction(null);
            BigDecimal detailTaxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
            //零税率标识  1,免税;2,不征税;3,普通零税率；
            if (StringUtils.isBlank(invoiceReceiptDetailE.getTaxRate()) || invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                //暂定普通零税率
                invoiceDetailF.setZeroRateFlag("3");
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                taxExcludedAmountSum = taxExcludedAmountSum.add(invoiceAmountSum);
            } else {
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
                BigDecimal oneAddTaxrate = new BigDecimal(1).add(detailTaxRate);

                //含税金额
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                //不含税金额 = 含税金额/(1+税率)
                BigDecimal taxExcludedAmount = invoiceAmount.divide(oneAddTaxrate, 2, RoundingMode.HALF_EVEN);
                taxExcludedAmountSum = taxExcludedAmountSum.add(taxExcludedAmount);

                //税额=(数量*含税单价)*税率/(1+税率）
                BigDecimal denominator = new BigDecimal(1).multiply(invoiceAmount)
                        .multiply(detailTaxRate);
                BigDecimal tax = denominator.divide(oneAddTaxrate, 2, RoundingMode.HALF_EVEN);
                taxSum = taxSum.add(tax);
            }

            if (BigDecimal.ZERO.compareTo(detailTaxRate) == 0 || freeTax != null && freeTax == 1) {
                if (InvoiceLineEnum.增值税电子发票.getCode().equals(invoiceReceiptE.getType())) {
                    invoiceDetailF.setZeroRateFlag("1");
                    invoiceDetailF.setTaxRate("0");
                    invoiceDetailF.setFavouredPolicyName("免税");
                    invoiceDetailF.setFavouredPolicyFlag("1");
                } else if (InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())){
                    invoiceDetailF.setTaxRate("0");
                    invoiceDetailF.setFavouredPolicyFlag("03");
                }
            } else if (simpleCollectionRate.compareTo(detailTaxRate) >= 0) {
                if (InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) || InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())) {
                    if (TaxpayerTypeEnum.一般纳税人.equalsByCode(invoiceE.getTaxpayerType())) {
                        invoiceDetailF.setFavouredPolicyFlag("01");
                    } else if (TaxpayerTypeEnum.小规模纳税人.equalsByCode(invoiceE.getTaxpayerType())) {
                        // do nothing 不传优惠标识
                    }
                }
            }
            invoiceDetailF.setGoodsCode(handleGoodsCode(invoiceReceiptDetailE, taxIteamMapByChargeId));
            invoiceDetailF.setSelfCode(null);
            invoiceDetailF.setGoodsName(invoiceReceiptDetailE.getGoodsName());
        }

        //单价为合并展示账单的开票金额
        invoiceDetailF.setPrice(String.valueOf(invoiceAmountSum));
        invoiceDetailF.setTaxIncludedAmount(invoiceAmountSum.toString());//含税金额
        invoiceDetailF.setTax(handleTax(freeTax, invoiceAmountSum, invoiceReceiptDetailES.get(0).getTaxRate()));//处理税额
        invoiceDetailF.setTaxExcludedAmount(handleTaxExcludedAmount(freeTax, invoiceAmountSum,invoiceReceiptDetailES.get(0).getTaxRate()));//处理不含税金额
        // 将票面税额与不含税金额冗余存在明细表
        invoiceReceiptDetailES.forEach(detail -> {
            detail.setFaceTaxAmount(AmountUtils.toLong(invoiceDetailF.getTax()));
            detail.setFaceTaxExcludedAmount(AmountUtils.toLong(invoiceDetailF.getTaxExcludedAmount()));
        });
        return invoiceDetailF;
    }

    /**
     * 处理不含税金额
     *
     * @return
     */
    private String handleTaxExcludedAmount(Integer freeTax, BigDecimal taxIncludedAmount,String taxRate) {
        if (freeTax != null && freeTax == 1) {
            return taxIncludedAmount.toString();
        }
        BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(taxRate));
        //不含税金额 = 含税金额/(1+税率)
        BigDecimal taxExcludedAmount = taxIncludedAmount.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
        return taxExcludedAmount.toString();
    }

    /**
     * 处理税额
     * @param freeTax
     * @param taxIncludedAmount
     * @param taxRate
     * @return
     */
    private String handleTax(Integer freeTax, BigDecimal taxIncludedAmount, String taxRate) {
        if (freeTax != null && freeTax == 1) {
            return "0.00";
        }
        BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(taxRate));
        //税额=(数量*含税单价)*税率/(1+税率）
        BigDecimal denominator = new BigDecimal(1).multiply(taxIncludedAmount).multiply(new BigDecimal(taxRate));
        BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
        return tax.toString();
    }


    /**
     * 根据红票的聚合对象处理诺诺红票入参
     *
     * @param invoiceReceiptE
     * @param nuonuoCommunityId
     * @param taxIteamMapByChargeId
     * @return
     */
    public OrderF invoiceRed(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                             List<InvoiceReceiptDetailE> invoiceReceiptDetailES, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        OrderF orderF = generalOrderF(invoiceReceiptE, invoiceE, handlePushModeStr(invoiceE.getPushMode()), nuonuoCommunityId);
        orderF.setInvoiceDetail(generalInvoiceDetailMergeForRed(invoiceReceiptDetailES, taxIteamMapByChargeId,invoiceE));
        return orderF;
    }



    /**
     * 处理诺诺开红票入参
     *
     * @param invoiceReceiptRedE
     * @param invoiceRedE
     * @param invoiceReceiptDetailES
     * @param pushMode
     * @param nuonuoCommunityId
     * @param taxIteamMapByChargeId
     * @return
     */
    @Deprecated
    public OrderF handleNuonuoOrderRed(InvoiceReceiptE invoiceReceiptRedE, InvoiceE invoiceRedE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                       List<Integer> pushMode, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        OrderF orderF = generalOrderF(invoiceReceiptRedE, invoiceRedE, pushMode, nuonuoCommunityId);
        orderF.setInvoiceDetail(generalInvoiceDetailRed(invoiceReceiptDetailES, taxIteamMapByChargeId));
        return orderF;
    }

    /**
     * 处理开红票入参
     *
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @return
     */
    private List<InvoiceDetailF> generalInvoiceDetailRed(List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        List<InvoiceDetailF> list = Lists.newArrayList();
        invoiceReceiptDetailES.forEach(detailE -> {
            InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
            invoiceDetailF.setSpecType(detailE.getSpectype());
            invoiceDetailF.setInvoiceLineProperty("0");
            invoiceDetailF.setNum(detailE.getNum());
            invoiceDetailF.setWithTaxFlag(detailE.getWithTaxFlag().toString());
            invoiceDetailF.setFavouredPolicyFlag(null);
            invoiceDetailF.setTaxRate(detailE.getTaxRate().equalsIgnoreCase("0.00000") ? null : detailE.getTaxRate());
            invoiceDetailF.setUnit(null);
            invoiceDetailF.setDeduction(null);
            invoiceDetailF.setZeroRateFlag(null);
            invoiceDetailF.setGoodsCode(handleGoodsCode(detailE, taxIteamMapByChargeId));
            invoiceDetailF.setSelfCode(null);
            invoiceDetailF.setGoodsName(detailE.getGoodsName());

            BigDecimal invoiceAmount = BigDecimal.valueOf(detailE.getInvoiceAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(detailE.getTaxRate()));

            //不含税金额 = 含税金额/(1+税率)
            BigDecimal taxExcludedAmount = invoiceAmount.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);

            //税额=(数量*含税单价)*税率/(1+税率）
            BigDecimal denominator = new BigDecimal(1).multiply(invoiceAmount).multiply(new BigDecimal(detailE.getTaxRate()));
            BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);

            invoiceDetailF.setPrice(invoiceAmount.negate().toString());
            invoiceDetailF.setTax(tax.toString());
            invoiceDetailF.setTaxIncludedAmount(invoiceAmount.toString());
            invoiceDetailF.setTaxExcludedAmount(taxExcludedAmount.toString());
            list.add(invoiceDetailF);
        });
        return list;
    }

    /**
     * 根据费项获取商品编号
     *
     * @param invoiceReceiptDetailE
     * @param taxIteamMapByChargeId
     * @return
     */
    private String handleGoodsCode(InvoiceReceiptDetailE invoiceReceiptDetailE, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        if (taxIteamMapByChargeId != null && !taxIteamMapByChargeId.isEmpty()) {
            List<TaxChargeItemD> taxChargeItemDs = taxIteamMapByChargeId.get(invoiceReceiptDetailE.getChargeItemId());
            if (CollectionUtils.isEmpty(taxChargeItemDs)) {
                throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
            }
            TaxChargeItemD taxChargeItemRv = taxChargeItemDs.get(0);
            if (taxChargeItemRv == null) {
                throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + " 该费项未配置对应的税目编码");
            }
            return taxChargeItemRv.getTaxItem().getCode();
        }
        return null;
    }

    /**
     * 构建order
     *
     * @return
     */
    private OrderF generalOrderF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<Integer> pushMode, String nuonuoCommunityId) {
        OrderF orderF = new OrderF();
        orderF.setBuyerName(invoiceE.getBuyerName());
        orderF.setBuyerPhone(invoiceE.getBuyerPhone());
        orderF.setBuyerTel(invoiceE.getBuyerTel());
        orderF.setBuyerAddress(invoiceE.getBuyerAddress());
        orderF.setBuyerAccount(invoiceE.getBuyerAccount());
        orderF.setBuyerTaxNum(invoiceE.getBuyerTaxNum());
        orderF.setSalerTaxNum(invoiceE.getSalerTaxNum());
        orderF.setSalerAccount(invoiceE.getSalerAccount());
        orderF.setSalerAddress(invoiceE.getSalerAddress());
        orderF.setSalerTel(invoiceE.getSalerTel());
        orderF.setOrderNo(invoiceReceiptE.getId().toString());
        orderF.setInvoiceDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        orderF.setPushMode(NuonuoPushModeEnum.不推送.getCode().toString());
        //orderF.setPushMode(handlePushMode(pushMode).toString());不适用诺诺的推送
        orderF.setEmail(invoiceE.getEmail());
        orderF.setInvoiceType(invoiceE.getInvoiceType().toString());
        orderF.setInvoiceLine(InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType()).getNuonuoCode());
        orderF.setInvoiceNum(invoiceE.getInvoiceNo());
        orderF.setClerk(invoiceReceiptE.getClerk());
        orderF.setInvoiceType(invoiceE.getInvoiceType().toString());
        orderF.setInvoiceCode(invoiceE.getInvoiceCode());
        orderF.setInvoiceNum(invoiceE.getInvoiceNo());
        orderF.setMachineCode(invoiceE.getMachineCode());//机器编码
        orderF.setDepartmentId(nuonuoCommunityId);//部门门店id
        orderF.setRemark(invoiceReceiptE.getRemark());
        orderF.setCallBackUrl(callBackUrl + "/invoice/scan");
        if (StringUtils.isNotBlank(invoiceE.getAddressInfo())){
            RealPropertyRentInfoF realPropertyRentInfoF = new RealPropertyRentInfoF();
            JSONObject jsonObject = JSONObject.parseObject(invoiceE.getAddressInfo());
            realPropertyRentInfoF.setRealPropertyAddress(jsonObject.getString("realPropertyAddress"));
            realPropertyRentInfoF.setDetailAddress(jsonObject.getString("detailAddress"));
            realPropertyRentInfoF.setRentEndDate(jsonObject.getString("rentEndDate"));
            realPropertyRentInfoF.setRentStartDate(jsonObject.getString("rentStartDate"));
            realPropertyRentInfoF.setCrossCityFlag(jsonObject.getString("crossCityFlag"));
            realPropertyRentInfoF.setUnit(jsonObject.getString("unit"));
            orderF.setRealPropertyRentInfo(realPropertyRentInfoF);
            orderF.setSpecificFactor("6");
        }
        return orderF;
    }

    /**
     * 处理推送方式
     * 推送方式：-1,不推送（默认）,0,邮箱;1,手机;2,站内信
     *
     * @param pushMode
     * @return
     */
    private Integer handlePushMode(List<Integer> pushMode) {
        if (pushMode.size() == 1) {
            Integer integer = pushMode.get(0);
            if (integer == -1) {
                return NuonuoPushModeEnum.不推送.getCode();
            } else if (integer == 0) {
                return NuonuoPushModeEnum.邮箱.getCode();
            } else if (integer == 1) {
                return NuonuoPushModeEnum.手机.getCode();
            }
        } else if (pushMode.size() == 2) {
            List<Integer> pushModes = Lists.newArrayList(NuonuoPushModeEnum.邮箱.getCode(), NuonuoPushModeEnum.手机.getCode());
            if (pushMode.containsAll(pushModes)) {
                return 2;
            }
        }
        return -1;
    }

    /**
     * 根据字符串转换推送方式
     *
     * @param pushModeStr
     * @return
     */
    private List<Integer> handlePushModeStr(String pushModeStr) {
        if (StringUtils.isNotBlank(pushModeStr)){
            List<Integer> pushModeList = JSONObject.parseObject(pushModeStr, List.class);
            return pushModeList;
        }
        return Lists.newArrayList(NuonuoPushModeEnum.不推送.getCode());
    }

}
