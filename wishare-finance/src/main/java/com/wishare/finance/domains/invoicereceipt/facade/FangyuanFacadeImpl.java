package com.wishare.finance.domains.invoicereceipt.facade;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillDetailV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.InvoiceRedApplyDetailF;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.InvoiceRedApplyF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.BillingNewV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoicePrintBatchV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoiceRedApplyV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemAppService;
import com.wishare.finance.domains.configure.accountbook.facade.AmpFinanceFacade;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
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
import com.wishare.finance.domains.invoicereceipt.entity.nuonuo.NuonuoTokenE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceZoningRepository;
import com.wishare.finance.domains.invoicereceipt.repository.NuonuoTokenRepository;
import com.wishare.finance.domains.invoicereceipt.support.nuonuo.FangyuanSupport;
import com.wishare.finance.domains.invoicereceipt.support.nuonuo.NuonuoSupport;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.*;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.ElectroniceDetailF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.ElectroniceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.FptxxF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.InterfaceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.InvoiceDataReqF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.RealPropertyRentInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.XmxxsF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.*;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.FptxxsResV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.InvoiceResDatalV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.InvoiceSuccessResV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.RedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.ReturnElectroniceV;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.exception.BizException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.SUCCESS_CODE0000;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.SUCCESS_CODE00000;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.VERSION;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.XXFPFPCX;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.XXFPFPKJ;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.XXFPREDAPPLY;
import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.XXFPREDAPPLYQUERY;


/**
 * 发票外部申请接口-方圆实现
 * @author dongpeng
 * @date 2023/6/21 9:54
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "invoice.supplier",havingValue = "fangyuan")
public class FangyuanFacadeImpl extends InvoiceExternalAbService{


    @Setter(onMethod_ = {@Autowired})
    private FangyuanSupport fangyuanSupport;
    @Setter(onMethod_ = {@Autowired})
    private  InvoiceReceiptRepository invoiceReceiptRepository;
    @Setter(onMethod_ = {@Autowired})
    private  InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    @Setter(onMethod_ = {@Autowired})
    private  AmpFinanceFacade ampFinanceFacade;
    @Setter(onMethod_ = {@Autowired})
    private NuonuoTokenRepository nuonuoTokenRepository;
    @Setter(onMethod_ = {@Autowired})
    private TaxItemAppService taxItemAppService;
    @Setter(onMethod_ = {@Autowired})
    private InvoiceZoningRepository invoiceZoningRepository;
    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;
    @Setter(onMethod_ = {@Autowired})
    private NuonuoSupport nuonuoSupport;


    /**
     * 方圆申请电子发票开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @param invoiceTypeEnum
     * @return
     */
    @Override
    public String nuonuoBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                   List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                   String nuonuoCommunityId,
                                   Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                                   InvoiceTypeEnum invoiceTypeEnum){
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        ElectroniceInfoF electroniceInfoF = handleElectroniceInfoF(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, taxItemMapByChargeId,invoiceTypeEnum);
        electroniceInfoF.setFPQQLSH(serialNo);
        fangyuanSupport.billingNew(electroniceInfoF);
        return serialNo;
    }

    @Override
    public String paperInvoices(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, InvoiceTypeEnum invoiceTypeEnum, String billInfoNo) {
        OrderF order = handleNuonuoOrderBlue(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, handlePushModeStr(invoiceE.getPushMode()), nuonuoCommunityId, taxItemMapByChargeId, billInfoNo);
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
        log.info("诺诺申请开票反参,流水号为：{}", JSON.toJSONString(billingNewV));
        return billingNewV.getInvoiceSerialNum();
    }


    @Override
    public String invoicesPrints(InvoicePrintF form, String tenantId) {
        InvoicePrintBatchF invoicePrintBatchF = new InvoicePrintBatchF();
        invoicePrintBatchF.setTenantId(tenantId);
        invoicePrintBatchF.setTaxnum(form.getSalerTaxNum());
        OutInvoicePrintRequestsF outInvoicePrintRequestsF = new OutInvoicePrintRequestsF();
        outInvoicePrintRequestsF.setInvoiceCode(form.getInvoiceCode());
        outInvoicePrintRequestsF.setInvoiceNum(form.getInvoiceNo());
        outInvoicePrintRequestsF.setTaxNum(form.getSalerTaxNum());
        outInvoicePrintRequestsF.setMoney(new BigDecimal(form.getPriceTaxAmount().toString()).divide(new BigDecimal("100")).toString());
        List<OutInvoicePrintRequestsF> list = new ArrayList<>();
        list.add(outInvoicePrintRequestsF);
        invoicePrintBatchF.setOutInvoicePrintRequests(list);
        InvoicePrintBatchV invoicePrintBatchV = nuonuoSupport.invoicePrintBatch(invoicePrintBatchF);
        if ("0000".equals(invoicePrintBatchV.getStatus())){
            return invoicePrintBatchV.getData().getBatchId();
        } else {
            return null;
        }
    }


    /**
     * 方圆电子发票开票结果查询接口
     *
     * @param serialNum
     */
    @Override
    public List<QueryInvoiceResultV> queryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo){
        ElectroniceInfoF electroniceInfoF = new ElectroniceInfoF();
        electroniceInfoF.setFPQQLSH(serialNum);//发票请求唯一流水号--必填
        electroniceInfoF.setDDH(null == orderNo? "": Long.toString(orderNo));//订单号（可重复）--必填
        electroniceInfoF.setKP_NSRSBH(taxnum);//开票方纳税人识别号--必填
        electroniceInfoF.setXHF_NSRSBH(taxnum);//销货方识别号--必填
        ReturnElectroniceV returnElectroniceV = fangyuanSupport.queryInvoiceResult(electroniceInfoF);
        return invoiceSearchResultTransferToNuonuoResult(returnElectroniceV);
    }

    public List<QueryInvoiceResultV> queryPaperInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo) {
        QueryInvoiceResultF queryInvoiceResultF = new QueryInvoiceResultF();
        queryInvoiceResultF.setTenantId(tenantId);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(serialNum)) {
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

    @Override
    public String invoiceRedApply(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, InvoiceTypeEnum invoiceTypeEnum) {
        InvoiceRedApplyF redApplyF = handleInvoiceRedApplyF(invoiceReceiptE,invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxItemMapByChargeId, tenantId, invoiceTypeEnum);
        InvoiceRedApplyV invoiceRedApplyV = nuonuoSupport.InvoiceRedApply(redApplyF);
        return invoiceRedApplyV.getBillInfoNo();
    }

    /**
     * 方圆全电开票
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

        InterfaceInfoF interfaceInfoF = handleInterfaceInfoF(invoiceReceiptE, invoiceE,invoiceReceiptDetailES,taxItemMapByChargeId,FangyuanQdKPLXEnum.蓝票,null,tenantId);
        fangyuanSupport.opeMplatformBillingNew(interfaceInfoF);
        return interfaceInfoF.getDataExchangeId();
    }

    /**
     * 方圆全电开票结果查询接口
     *
     * @param tenantId
     * @param serialNum
     * @param taxnum
     */
    @Override
    public List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo,InvoiceE invoiceE) {
        Map<String,String> map = new HashedMap<>();
        map.put("fpqqlsh",serialNum);
        String str = JSON.toJSONString(map);
        String encode = Base64.encode(str.getBytes(StandardCharsets.UTF_8));
        InterfaceInfoF interfaceInfoF = getInterfaceInfoF(serialNum, XXFPFPCX, encode, taxnum,invoiceE,tenantId);
        InvoiceSuccessResV invoiceSuccessResV = fangyuanSupport.opeMplatformQueryInvoiceResult(interfaceInfoF);
        return opeMplatformQueryInvoiceResultTransferToNuonuoResulst(invoiceSuccessResV);
    }

    /**
     * 全电红冲申请单
     * @return
     */
    @Override
    public String electronInvoiceRedApply(InvoiceRedA invoiceRedA) {
        InvoiceDataReqF invoiceDataReqF  = new InvoiceDataReqF();
        InvoiceReceiptE invoiceReceiptRedE = invoiceRedA.getInvoiceReceiptRedE();
        InvoiceE invoiceRedE = invoiceRedA.getInvoiceRedE();
        List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList = invoiceRedA.getInvoiceReceiptDetailRedEList();
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = getLongListMap(invoiceReceiptDetailRedEList);
        FptxxF fptxxF = queryFptxxF(invoiceReceiptRedE, invoiceRedE, FangyuanQdKPLXEnum.红票, String.valueOf(invoiceRedA.getInvoiceRedApplyE().getId()));
        invoiceDataReqF.setXmxxs(queryXmxxs(invoiceRedE,invoiceReceiptDetailRedEList, taxIteamMapByChargeId,FangyuanQdKPLXEnum.红票,fptxxF));
        invoiceDataReqF.setFptxx(fptxxF);
        InterfaceInfoF interfaceInfoF = extracted(invoiceDataReqF, XXFPREDAPPLY,invoiceRedE,invoiceReceiptRedE.getTenantId());
        return fangyuanSupport.electronInvoiceRedApply(interfaceInfoF);
    }

    /**
     * 全电红冲申请单查询
     * @return
     */
    @Override
    public List<NuonuoRedApplyQueryV> electronInvoiceRedApplyQuery(InvoiceRedApplyE applyE, InvoiceE invoiceE) {
        InvoiceDataReqF invoiceDataReqF  = new InvoiceDataReqF();
        invoiceDataReqF.setFptxx(queryFptxxRedApply(applyE, invoiceE));
        InterfaceInfoF interfaceInfoF = extracted(invoiceDataReqF, XXFPREDAPPLYQUERY,invoiceE,applyE.getTenantId());
        RedApplyQueryV redQuery = fangyuanSupport.electronInvoiceRedQuery(interfaceInfoF);
        return electronInvoiceRedApplyResultTransferToNuonuoResulst(redQuery);
    }

    /**
     * 方圆全电发票冲红
     * @return
     */
    @Override
    public String electronInvoiceRed(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV) {
        //获取发票主表的信息
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
        //获取发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptE.getId()));
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = getLongListMap(invoiceReceiptDetailES);
        InterfaceInfoF interfaceInfoF = handleInterfaceInfoF(invoiceReceiptE, invoiceE,invoiceReceiptDetailES,taxIteamMapByChargeId,FangyuanQdKPLXEnum.红票,queryV,invoiceE.getTenantId());
        fangyuanSupport.opeMplatformBillingNew(interfaceInfoF);
        return interfaceInfoF.getDataExchangeId();
    }

    /**
     * 发起红字确认单申请入参
     * @param invoiceE
     * @param invoiceReceiptRedE
     * @param billId
     * @return
     */
    @Override
    public ElectronInvoiceRedApplyF generateRedApply(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptRedE, String billId) {
        return null;
    }

    /**
     * 生成红字确认单查询入参
     * @param applyE
     * @param taxnum
     * @return
     */
    @Override
    public ElectronInvoiceRedQueryF generateRedApplyQuery(InvoiceRedApplyE applyE, String taxnum) {
        return null;
    }

    /**
     * 开票之前添加销方名称
     * @param invoiceBlueA
     * @return
     */
    @Override
    public InvoiceBlueA preInvoice(InvoiceBlueA invoiceBlueA) {
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        invoiceE.setSalerName(invoiceReceiptE.getInvRecUnitName());
        return invoiceBlueA;
    }

    /**
     *
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     * @param invoiceReceiptE
     */
    @Override
    public void checkRealPropertyRentInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE, List<Long> chargeItemIds, InvoiceReceiptE invoiceReceiptE) {
        // 判断发票类型  如果不是全电发票就return
        // 只校验全电普票
        if(!InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())){
            return;
        }
        // 判断是否包含 304050202-不动产经营租赁  30405020202-车辆停放服务  税目编码
        // 判断账单是否是 同一个房号
        List<TaxChargeItemD> taxChargeItemRvs = taxItemAppService.queryByChargeIdList(chargeItemIds);
        boolean anyMatch = taxChargeItemRvs.stream().anyMatch(s -> (s.getTaxItem().getCode().equals("304050202") || s.getTaxItem().getCode().equals("30405020202")));
        if (anyMatch) {
            // 取消单条限制
            // 允许同房号、同费项、账单周期连续的账单批量开票
            // 判断房间是否一致
            List<String> list = billDetailMoreVList.stream().map(BillDetailMoreV::getRoomId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list) && list.size() != 1) {
                throw BizException.throw400("该批次账单房号不一致!");
            }
            // 判断费项是否一致
            List<Long> chargeItemIdList = billDetailMoreVList.stream().map(BillDetailMoreV::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(chargeItemIdList) && chargeItemIdList.size() != 1) {
                throw BizException.throw400("该批次账单费项不一致!");
            }
            // 判断账单周期是否连续
            List<LocalDateTime> localDateTimeList = new ArrayList<>();
            // 按照账单id分组
            Map<String, List<BillDetailMoreV>> billGroupByBillNo = billDetailMoreVList.stream().collect(Collectors.groupingBy(BillDetailV::getBillNo));
            for (Map.Entry<String, List<BillDetailMoreV>> entry : billGroupByBillNo.entrySet()) {
                BillDetailMoreV billDetailMoreV = entry.getValue().get(0);
                if (Objects.isNull(billDetailMoreV.getStartTime()) || Objects.isNull(billDetailMoreV.getEndTime())) {
                    throw BizException.throw400("正在开具租赁发票，请检查账单开始结束时间是否正确，异常账单编号：" + billDetailMoreV.getBillNo());
                }
                localDateTimeList.add(billDetailMoreV.getStartTime());
                localDateTimeList.add(billDetailMoreV.getEndTime());
            }
            // 按照时间排序
            List<LocalDateTime> collect = localDateTimeList.stream().sorted().collect(Collectors.toList());
            if (!isContains(collect)){
                throw BizException.throw400("该批次账单周期不连续!");
            }
            // 通过房号去空间中心获取
            BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
            String roomId = billDetailMoreV.getRoomId();
            if(org.apache.commons.lang3.StringUtils.isBlank(roomId)){
                throw BizException.throw400("该账单下不存在房间!");
            }
            List<Long> longList = new ArrayList<>();
            longList.add(Long.valueOf(roomId));
            List<SpaceDetails> details = spaceClient.getDetails(longList);
            if(CollectionUtils.isEmpty(details)){
                throw BizException.throw400("该账单下不存在空间信息!");
            }
            log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(details));
            if (CollectionUtils.isNotEmpty(details)){
                StringBuilder stringBuilder = new StringBuilder();
                SpaceDetails spaceDetails = details.get(0);
                SpaceCommunityV communityInfo = spaceClient.getCommunityDetail(spaceDetails.getCommunityId());
                log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(communityInfo));
                if(org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getProvince()) && org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getCity())) {
                    throw BizException.throw400("开票失败，开票项目包含不动产经营租赁服务，请先在园区档案中完善地址信息后再进行开票!");
                }
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
                        if (invoiceZoningE.getAreaLevel().equals("3")) {
                            stringBuilder.append(invoiceZoningE.getAreaName());
                        } else {
                            // 如果是2  则获取子列表
                            stringBuilder.append(invoiceZoningE.getAreaName());
                            List<InvoiceZoningE> zoningBySuperiorCode = invoiceZoningRepository.getInvoiceZoningBySuperiorCode(invoiceZoningE.getAreaCode());
                            // 如果查询不到 则该级别没有下一层级  不需要进行其他操作
                            // 否则就循环 获取 对应的区 去匹配
                            if (!zoningBySuperiorCode.isEmpty()){
                                for (InvoiceZoningE zoningE : zoningBySuperiorCode) {
                                    if (zoningE.getAreaName().equals(communityInfo.getArea())){
                                        stringBuilder.append(zoningE.getAreaName());
                                    }
                                }
                            }
                        }
                        break;
                    }

                }
                JSONObject jsonObject = new JSONObject();
                if(org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getAddress())){
                    throw BizException.throw400("开票失败，开票项目包含不动产经营租赁服务，请先在园区档案中完善地址信息后再进行开票。");
                }
                jsonObject.put("bdcdz",stringBuilder);
                String replace = null;
                if (communityInfo.getAddress().contains(stringBuilder)){
                    replace = communityInfo.getAddress().replace(stringBuilder, "");
                } else {
                    replace = communityInfo.getAddress();
                }
                jsonObject.put("xxdz",replace);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                jsonObject.put("zlqq", collect.get(0).format(dateTimeFormatter));
                jsonObject.put("zlqz", collect.get(collect.size()-1).format(dateTimeFormatter));
                jsonObject.put("kdsbz", "0");
                jsonObject.put("zsbh", "");
                jsonObject.put("mjdw", "2");
                String jsonString = JSONObject.toJSONString(jsonObject);
                log.info("不动产发票开具地址信息:" + jsonString);
                invoiceE.setAddressInfo(jsonString);
            }
        }
    }

    /**
     * 处理方圆开票入参 (根据同费项，同税率合并明细)
     *
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @return
     */
    public ElectroniceInfoF handleElectroniceInfoF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                   Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId,InvoiceTypeEnum invoiceTypeEnum) {
        ElectroniceInfoF electroniceInfoF = getElectroniceInfoF(invoiceReceiptE, invoiceE,invoiceTypeEnum);
        electroniceInfoF.setDetails(getElectroniceDetailves(invoiceE,invoiceReceiptDetailES, taxIteamMapByChargeId,invoiceTypeEnum,electroniceInfoF));
        return electroniceInfoF;
    }


    /**
     * 增值税电子发票开票主体信息
     * @param invoiceReceiptE
     * @param invoiceE
     * @return
     */
    private ElectroniceInfoF getElectroniceInfoF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,InvoiceTypeEnum invoiceTypeEnum) {
        ElectroniceInfoF electroniceInfoF = new ElectroniceInfoF();
//        electroniceInfoF.setFPQQLSH(isNotBlank(invoiceE.getInvoiceSerialNum()));//发票请求唯一流水号--必填
        electroniceInfoF.setDDH(null == invoiceReceiptE.getId() ? " " : invoiceReceiptE.getId().toString());//订单号（可重复）--必填
        electroniceInfoF.setKP_NSRSBH(isNotBlank(invoiceE.getSalerTaxNum()));//开票方纳税人识别号--必填
        electroniceInfoF.setKP_NSRMC(invoiceReceiptE.getInvRecUnitName());//开票方名称--必填
        electroniceInfoF.setNSRDZDAH(null==invoiceE.getExtensionNumber()? null :invoiceE.getExtensionNumber().toString());//开票分机号
        electroniceInfoF.setSWJG_DM(FangyuanSWJGDMEnum.电子普票.getCode());//发票种类
        electroniceInfoF.setDKBZ(FangyuanDKBZEnum.自开.getCode());//代开标志--必填
        electroniceInfoF.setKPXM(isNotBlank(invoiceReceiptE.getCommunityName()));//主要开票项目
        electroniceInfoF.setXHF_NSRSBH(isNotBlank(invoiceE.getSalerTaxNum()));//销货方识别号--必填
        electroniceInfoF.setXHF_MC(invoiceReceiptE.getInvRecUnitName());//销货方名称--必填
        electroniceInfoF.setXHF_DZ(isNotBlank(invoiceE.getSalerAddress()));//销货方地址--必填
        if(StringUtils.isBlank(invoiceE.getSalerAddress())){
            throw BizException.throw400("销货方地址不能为空");
        }
        electroniceInfoF.setXHF_DH(isNotBlank(invoiceE.getSalerTel()));//销货方电话--必填
        electroniceInfoF.setXHF_YHZH(isNotBlank(invoiceE.getSalerAccount()));//销货方银行账号--必填
        electroniceInfoF.setGHF_MC(isNotBlank(invoiceE.getBuyerName()));//购货方名称--必填
        electroniceInfoF.setGHF_NSRSBH(isNotBlank(invoiceE.getBuyerTaxNum()));//购货方识别号
        electroniceInfoF.setGHF_DZ(isNotBlank(invoiceE.getBuyerAddress()));//购货方地址
        electroniceInfoF.setGHF_GDDH(isNotBlank(invoiceE.getBuyerTel()));//购货方固定电话
        electroniceInfoF.setGHF_SJ(isNotBlank(invoiceReceiptE.getBuyerPhone()));//购货方手机，此字段有值会进行短信推送
        electroniceInfoF.setGHF_EMAIL(isNotBlank(invoiceReceiptE.getEmail()));//购货方邮箱，此字段有值会进行邮箱推送
        electroniceInfoF.setGHF_YHZH(isNotBlank(invoiceE.getBuyerAccount()));//购货方银行账号
        electroniceInfoF.setKPR(isNotBlank(invoiceReceiptE.getClerk()));//开票员--必填
        // 紧急临时方案：收款人取开票人、复核人写死为“廖莹”。
        // 方圆紧急处理
        electroniceInfoF.setSKR(StringUtils.isNotBlank(invoiceReceiptE.getPayeeName()) ? invoiceReceiptE.getPayeeName() : isNotBlank(invoiceReceiptE.getClerk()));//收款员--必填

        electroniceInfoF.setFHR("廖莹");//复核人--必填
        electroniceInfoF.setYFP_DM(isNotBlank(invoiceE.getInvoiceCode()));//原发票代码，如果CZDM不是10时候都是必录
        electroniceInfoF.setYFP_HM(isNotBlank(invoiceE.getInvoiceNo()));//原发票号码，如果CZDM不是10时候都是必录
        electroniceInfoF.setKPLX(invoiceTypeEnum.getCode().toString());//开票类型--必填
        if(FangyuanKPLXEnum.红票.getCode().equals(invoiceTypeEnum.getCode())){
            electroniceInfoF.setCZDM(FangyuanCZDMEnum.退货折让红票.getCode());//操作代码--必填
            electroniceInfoF.setBZ("");//备注
        }else{
            electroniceInfoF.setCZDM(FangyuanCZDMEnum.正票正常开具.getCode());//操作代码--必填
            electroniceInfoF.setBZ(isNotBlank(invoiceReceiptE.getRemark()));//备注
        }
        electroniceInfoF.setCHYY("");//冲红原因
        electroniceInfoF.setBMB_BBH("33.0");//税收分类编码版本号--必填
        return electroniceInfoF;
    }


    /**
     * 增值税电子发票明细信息
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param taxItemMapByChargeId
     * @param invoiceTypeEnum
     * @return
     */
    private List<ElectroniceDetailF> getElectroniceDetailves(InvoiceE invoiceE,List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Map<Long,
            List<TaxChargeItemD>> taxItemMapByChargeId,InvoiceTypeEnum invoiceTypeEnum,ElectroniceInfoF electroniceInfoF) {
        // 按照费项和税率进行分组
        Map<String, List<InvoiceReceiptDetailE>> invoiceReceiptDetailGroupMap = invoiceReceiptDetailES.stream().collect(
                Collectors.groupingBy(
                        detail -> detail.getGoodsName() + "-" + detail.getTaxRate()
                        , TreeMap::new,
                        Collectors.toList()
                ));
        List<ElectroniceDetailF> electroniceDetailFList =new ArrayList<>();
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
//        BigDecimal totalTax = BigDecimal.ZERO;
        Integer freeTax = invoiceE.getFreeTax();
        // 行号
        int lineNo = 1;
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : invoiceReceiptDetailGroupMap.entrySet()) {
            // 税率
            BigDecimal taxRate = BigDecimal.ZERO;
            // 总金额（含税金额）
            BigDecimal goodsTotalPrice = BigDecimal.ZERO;
            //税额
//            BigDecimal goodsTotalTax = BigDecimal.ZERO;
            BigDecimal ONE_HUNDRED = new BigDecimal(100);
            ElectroniceDetailF electroniceDetailF =new ElectroniceDetailF();
            for (InvoiceReceiptDetailE invoiceReceiptDetailE : entry.getValue()) {
                // 行号
                invoiceReceiptDetailE.setLineNo(lineNo);
                electroniceDetailF.setXMMC(isNotBlank(invoiceReceiptDetailE.getGoodsName()));//项目名称--必填
                electroniceDetailF.setDW(isNotBlank(invoiceReceiptDetailE.getUnit()));//项目单位
                electroniceDetailF.setGGXH(isNotBlank(invoiceReceiptDetailE.getSpectype()));//规格型号
                electroniceDetailF.setXMSL((FangyuanKPLXEnum.正票.getCode().equals(invoiceTypeEnum.getCode()))? 1: -1);//项目数量
                electroniceDetailF.setHSBZ(null == invoiceReceiptDetailE.getWithTaxFlag() ? " " :String.valueOf(invoiceReceiptDetailE.getWithTaxFlag()));//含税标志--必填
                electroniceDetailF.setXMBM("");//项目编码
                electroniceDetailF.setSPBM(handleGoodsCode(invoiceReceiptDetailE, taxItemMapByChargeId));//商品分类编码--必填
                electroniceDetailF.setZXBM("");//自行编码
                // 税率
                if (StringUtils.isNotBlank(invoiceReceiptDetailE.getTaxRate()) && !invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                    // 有税率
                    taxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
                }
                // 当前单条详情（账单）的含税金额
                Long invoiceAmountLongValue = invoiceReceiptDetailE.getInvoiceAmount();
                Long discountAmountLongValue = Objects.isNull(invoiceReceiptDetailE.getDiscountAmount()) ? 0L : invoiceReceiptDetailE.getDiscountAmount();
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceAmountLongValue + discountAmountLongValue)
                        .divide(ONE_HUNDRED, 8, RoundingMode.HALF_EVEN);
                goodsTotalPrice = goodsTotalPrice.add(invoiceAmount);
                if(taxRate.compareTo(BigDecimal.ZERO)>0){
                    electroniceDetailF.setLSLBS("");//零税率标识
                    electroniceDetailF.setYHZCBS(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    electroniceDetailF.setZZSTSGL("");//增值税特殊管理
                    electroniceDetailF.setSL(taxRate.toString());//税率--必填
                    /*// 计算税额 税额=(数量*含税单价)*税率/(1+税率），也就是当前分组出来的记录的  总价格*税率/(1+税率）
                    BigDecimal goodsOneTax = invoiceAmount.multiply(taxRate).divide(taxRate.add(BigDecimal.ONE), 8, RoundingMode.HALF_EVEN);
                    goodsTotalTax = goodsTotalTax.add(goodsOneTax).setScale(8, RoundingMode.HALF_EVEN);*/
                }else{
                    electroniceDetailF.setSL("0");//税率--必填
                    electroniceDetailF.setLSLBS(FangyuanLSLBSEnum.普通零税率.getCode());//零税率标识
                    electroniceDetailF.setYHZCBS(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    electroniceDetailF.setZZSTSGL("");//增值税特殊管理
//                    electroniceDetailF.setSE(0.00);//税额
                }
                // todo 是否取总额减税额
                /*totalP    rice.subtract(totalTax).setScale(2, RoundingMode.FLOOR);
                electroniceDetailF.setKCE(totalPrice.doubleValue());//扣除额*/

                if (freeTax != null && freeTax == 1) {
                    electroniceDetailF.setZZSTSGL(FangyuanLSLBSEnum.免税.getDes());//增值税特殊管理
                    electroniceDetailF.setYHZCBS(FangyuanYHZCBSEnum.使用.getCode());//优惠政策标识--必填
                    electroniceDetailF.setLSLBS(FangyuanLSLBSEnum.免税.getCode());//零税率标识
                    electroniceDetailF.setSL("0");
//                    electroniceDetailF.setSE(0.00);
                }
            }
//            electroniceDetailF.setXMDJ(goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).doubleValue());//项目单价
            electroniceDetailF.setXMJE((goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).doubleValue()));//项目金额
            electroniceDetailFList.add(electroniceDetailF);
            BigDecimal discountGoodPrice = addElectronicDiscountDetail(entry.getValue(), electroniceDetailFList,
                    taxItemMapByChargeId, invoiceTypeEnum);
            totalPriceTax = totalPriceTax.add(discountGoodPrice).setScale(8, RoundingMode.HALF_EVEN);
            totalPriceTax = totalPriceTax.add(goodsTotalPrice).setScale(8, RoundingMode.HALF_EVEN);

           /* totalTax = totalTax.add(goodsTotalTax).setScale(8, RoundingMode.HALF_EVEN);
            electroniceDetailF.setSE(goodsTotalTax.setScale(2, RoundingMode.HALF_EVEN).doubleValue());//税额*/
            // 行号自增
            lineNo += 1;
        }
        // 价税合计和税额之前已计算为负数，直接相减即可
        electroniceInfoF.setKPHJJE(totalPriceTax.setScale(2, RoundingMode.HALF_EVEN).doubleValue());//价税合计金额--必填

        /*BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(2, RoundingMode.HALF_EVEN);
        electroniceInfoF.setHJBHSJE((totalPrice.doubleValue()));//合计不含税金额
        electroniceInfoF.setHJSE(totalTax.setScale(2, RoundingMode.HALF_EVEN).doubleValue());//合计税额
        if (freeTax != null && freeTax == 1) {
            electroniceInfoF.setHJBHSJE(totalPriceTax.doubleValue());//合计不含税金额
            electroniceInfoF.setHJSE(0);//合计税额
        }*/
        return electroniceDetailFList;
    }


    /**
     * 添加折扣列
     * @param invoiceReceiptDetailES
     * @param electroniceDetailFList
     */
    private BigDecimal addElectronicDiscountDetail(List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                   List<ElectroniceDetailF> electroniceDetailFList,
                                                   Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId,
                                                   InvoiceTypeEnum invoiceTypeEnum) {
        // 总金额（含税金额）
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        List<InvoiceReceiptDetailE> discountDetails = invoiceReceiptDetailES.stream()
                .filter(detail -> Objects.nonNull(detail.getDiscountAmount()) && detail.getDiscountAmount().compareTo(0L) != 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(discountDetails)) {
            // 税率
            BigDecimal taxRate = BigDecimal.ZERO;
            ElectroniceDetailF electroniceDetailF =new ElectroniceDetailF();
            BigDecimal ONE_HUNDRED = new BigDecimal(100);
            for (InvoiceReceiptDetailE invoiceReceiptDetailE : discountDetails) {
                electroniceDetailF.setXMMC(isNotBlank(invoiceReceiptDetailE.getGoodsName()));//项目名称--必填
                electroniceDetailF.setDW(isNotBlank(invoiceReceiptDetailE.getUnit()));//项目单位
                electroniceDetailF.setGGXH(isNotBlank(invoiceReceiptDetailE.getSpectype()));//规格型号
                electroniceDetailF.setXMSL(0);//项目数量 折扣票项目数量为0
                electroniceDetailF.setXMDJ(0);//项目单价 折扣票项目单价为0
                electroniceDetailF.setHSBZ(null == invoiceReceiptDetailE.getWithTaxFlag() ? " " :String.valueOf(invoiceReceiptDetailE.getWithTaxFlag()));//含税标志--必填
                electroniceDetailF.setXMBM("");//项目编码
                electroniceDetailF.setSPBM(handleGoodsCode(invoiceReceiptDetailE, taxItemMapByChargeId));//商品分类编码--必填
                electroniceDetailF.setZXBM("");//自行编码
                // 税率
                if (StringUtils.isNotBlank(invoiceReceiptDetailE.getTaxRate()) && !invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                    // 有税率
                    taxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
                }
                // 当前单条详情（账单）的含税金额
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getDiscountAmount()).negate().divide(ONE_HUNDRED, 8, RoundingMode.HALF_EVEN);
                goodsTotalPrice = goodsTotalPrice.add(invoiceAmount);
                if(taxRate.compareTo(BigDecimal.ZERO)>0){
                    electroniceDetailF.setLSLBS("");//零税率标识
                    electroniceDetailF.setYHZCBS(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    electroniceDetailF.setZZSTSGL("");//增值税特殊管理
                    electroniceDetailF.setSL(taxRate.toString());//税率--必填
                    /*// 计算税额 税额=(数量*含税单价)*税率/(1+税率），也就是当前分组出来的记录的  总价格*税率/(1+税率）
                    BigDecimal goodsOneTax = invoiceAmount.multiply(taxRate).divide(taxRate.add(BigDecimal.ONE), 8, RoundingMode.HALF_EVEN);
                    goodsTotalTax = goodsTotalTax.add(goodsOneTax).setScale(8, RoundingMode.HALF_EVEN);*/
                }else{
                    electroniceDetailF.setSL("0");//税率--必填
                    electroniceDetailF.setLSLBS(FangyuanLSLBSEnum.普通零税率.getCode());//零税率标识
                    electroniceDetailF.setYHZCBS(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    electroniceDetailF.setZZSTSGL("");//增值税特殊管理
                }

            }
            double xmje = goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            electroniceDetailF.setXMJE(xmje);//项目金额
            electroniceDetailFList.add(electroniceDetailF);
        }
        return goodsTotalPrice;
    }

    /**
     * 将方圆增值税电子发票查询请求体转换为nuonuo响应体
     * @param models
     * @return
     */
    private List<QueryInvoiceResultV> invoiceSearchResultTransferToNuonuoResult(ReturnElectroniceV models) {
        List<QueryInvoiceResultV> result = new ArrayList<>();
        QueryInvoiceResultV resultV = new QueryInvoiceResultV();
//        models.getCZDM();//操作码
        resultV.setOrderNo(models.getDDH());//订单号
        resultV.setSerialNo(models.getFPQQLSH());//发票流水号
//        models.getKPLX();//开票类型
        //转换为时间戳
        resultV.setInvoiceTime(str2TimeStamp(models.getKPRQ()));//开票时间
        resultV.setInvoiceCode(models.getFP_DM());//发票号码
        resultV.setInvoiceNo(models.getFP_HM());//发票代码
        resultV.setCheckCode(models.getJYM());//检验码
//        models.getPDF_FILE();//版式文件名称
        resultV.setPdfUrl(models.getPDF_URL());//下载链接
//        models.getSE();//税额
//        models.getHJBHSJE();//合计不含税金额
        resultV.setPayerTaxNo(models.getFPZL_DM());//购货方税号
        resultV.setOrderAmount(models.getKPHJJE());//含税金额
//        models.getEWM();//未知数据
//        models.getKPLSH();//未知数据
//        models.getFWM();//操作码
        invoiceStatusTransferToNuonuoStatus(models.getReturnCode(),models.getReturnMsg(),resultV);
        resultV.setFailCause(models.getReturnMsg());//结果描述
        result.add(resultV);
        return result;
    }

    /**
     * 将方圆全电发票查询请求体转换为nuonuo响应体
     * @param models
     * @return
     */
    private List<QueryInvoiceResultV> opeMplatformQueryInvoiceResultTransferToNuonuoResulst(InvoiceSuccessResV models) {
        List<QueryInvoiceResultV> result = new ArrayList<>();
        QueryInvoiceResultV resultV = new QueryInvoiceResultV();
        InvoiceResDatalV data = models.getData();
//        models.getCZDM();//操作码
        resultV.setSerialNo(data.getFpqqlsh());//发票流水号
//        models.getKPLX();//开票类型
        //转换为时间戳
        resultV.setInvoiceTime(str2TimeStamp(data.getKprq()));//开票时间
        resultV.setInvoiceCode(data.getFpdm());//发票代码
        resultV.setInvoiceNo(data.getFphm());//发票号码
        resultV.setCheckCode(data.getJym());//检验码
//        models.getPDF_FILE();//版式文件名称
        resultV.setPdfUrl(data.getPdfUrl());//下载链接
//        models.getSE();//税额
//        models.getHJBHSJE();//合计不含税金额
        resultV.setPayerTaxNo(data.getNsrsbh());//购货方税号
        resultV.setOrderAmount(data.getHjje());//含税金额
//        models.getEWM();//未知数据
//        models.getKPLSH();//未知数据
//        models.getFWM();//操作码
        invoiceStatusTransferToNuonuoStatus(models.getCode(),models.getMess(),resultV);
        resultV.setFailCause(models.getMess());//结果描述
        result.add(resultV);
        return result;
    }

    /**
     * 将方圆全电红冲确认单查询请求体转换为nuonuo响应体
     * @param models
     * @return
     */
    private List<NuonuoRedApplyQueryV> electronInvoiceRedApplyResultTransferToNuonuoResulst(RedApplyQueryV models) {
        List<NuonuoRedApplyQueryV> result = new ArrayList<>();

        NuonuoRedApplyQueryV resultV = new NuonuoRedApplyQueryV();
        if(SUCCESS_CODE0000.equals(models.getCode()) || SUCCESS_CODE00000.equals(models.getCode())){
            resultV.setBillStatus(NuonuoRedApplyStatusEnum.无需确认.getCode());
        }else {
            resultV.setBillStatus(NuonuoRedApplyStatusEnum.申请失败.getCode());
            resultV.setErrMsg(models.getMess());
            result.add(resultV);
            return result;
        }

        if (BeanUtil.isEmpty(models.getData().getFptxxs())) {
            throw BizException.throw400(models.getMess());
        }
        List<FptxxsResV> data = models.getData().getFptxxs();

        for (FptxxsResV resV : data){
            try {
                resultV.setDetail(new ObjectMapper().writeValueAsString(resV.getXmxxs()));
                resultV.setBillNo(resV.getFptxx().getHztzdh());
                resultV.setBillUuid(resV.getFptxx().getChuuid());
                resultV.setInvoiceCode(resV.getFptxx().getYfpdm());
                resultV.setInvoiceNo(resV.getFptxx().getYfphm());
                result.add(resultV);
            } catch (JsonProcessingException e) {
                log.error("全电红冲确认单结果转换失败:", e);
            }
        }
        return result;
    }

    private void invoiceStatusTransferToNuonuoStatus(String code,String msg, QueryInvoiceResultV resultV) {

        if (FangyuanReturnCodeEnum.开票成功.getCode().equals(code)
        || FangyuanQdReturnCodeEnum.开票成功.getCode().equals(code)) {
            // 开具成功
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票完成.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票完成.getDes());
        } else if (FangyuanReturnCodeEnum.开票成功但未签章.getCode().equals(code)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票成功签章中.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票成功签章中.getDes());
        } else if (FangyuanQdReturnCodeEnum.开票成功签章失败.getCode().equals(code)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票成功签章失败.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票成功签章失败.getDes());
        } else if (FangyuanReturnCodeEnum.单据已作废.getCode().equals(code)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.发票已作废.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.发票已作废.getDes());
        } else if (FangyuanReturnCodeEnum.单据不存在或未开票.getCode().equals(code)) {
            if("发票未开具,原因:正在排队".equals(msg)){
                resultV.setStatus(NuonuoInvoiceStatusEnum.开票中.getCode().toString());
                resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票中.getDes());
            }else{
                resultV.setStatus(NuonuoInvoiceStatusEnum.开票失败.getCode().toString());
                resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票失败.getDes());
            }
        } else if (FangyuanQdReturnCodeEnum.开票中.getCode().equals(code)) {
                resultV.setStatus(NuonuoInvoiceStatusEnum.开票中.getCode().toString());
                resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票中.getDes());
        } else {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票失败.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票失败.getDes());
        }
    }

    /**
     * 处理方圆全电普票开票入参 (根据同费项，同税率合并明细)
     *
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param taxIteamMapByChargeId
     * @return
     */
    public InterfaceInfoF handleInterfaceInfoF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                       Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId, FangyuanQdKPLXEnum fangyuanQdKPLXEnum,
                                               NuonuoRedApplyQueryV queryV,String tenantId) {
        InvoiceDataReqF invoiceDataReqF  = new InvoiceDataReqF();
        FptxxF fptxxF = queryFptxxF(invoiceReceiptE, invoiceE,fangyuanQdKPLXEnum,"");
        if(FangyuanQdKPLXEnum.红票.equals(fangyuanQdKPLXEnum)){
            fptxxF.setHztzdh(queryV.getBillNo());// 红字通知单号/红字确认单号
            fptxxF.setChuuid(queryV.getBillUuid());// 数电红票红字确认单 UUID
            fptxxF.setYfphm(queryV.getInvoiceNo());//原发票号码
            fptxxF.setYfpdm(queryV.getInvoiceCode());//原发票代码
            fptxxF.setBz("被红冲蓝字数电发票号码:"+ isNotBlank(queryV.getInvoiceNo())
                    +" 红字发票信息确认单编号:"+ queryV.getBillNo() +"");
        }
        invoiceDataReqF.setXmxxs(queryXmxxs(invoiceE,invoiceReceiptDetailES, taxIteamMapByChargeId,fangyuanQdKPLXEnum,fptxxF));
        invoiceDataReqF.setFptxx(fptxxF);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(invoiceE.getAddressInfo())){
            List<RealPropertyRentInfoF> realPropertyRentInfoFList = new ArrayList<>();
            RealPropertyRentInfoF realPropertyRentInfoF = new RealPropertyRentInfoF();
            JSONObject jsonObject = JSONObject.parseObject(invoiceE.getAddressInfo());
            realPropertyRentInfoF.setBdcdz(jsonObject.getString("bdcdz"));
            realPropertyRentInfoF.setXxdz(jsonObject.getString("xxdz"));
            realPropertyRentInfoF.setZlqq(jsonObject.getString("zlqq"));
            realPropertyRentInfoF.setZlqz(jsonObject.getString("zlqz"));
            realPropertyRentInfoF.setKdsbz(jsonObject.getString("kdsbz"));
            realPropertyRentInfoF.setMjdw(jsonObject.getString("mjdw"));
            realPropertyRentInfoFList.add(realPropertyRentInfoF);
            fptxxF.setTdys("6");
            invoiceDataReqF.setBdcjyzlfwxxs(realPropertyRentInfoFList);
        }
        return extracted(invoiceDataReqF, XXFPFPKJ,invoiceE,tenantId);
    }

    /**
     * 全电发票统一参数构建
     * @param invoiceDataReqF
     * @param interfaceCode
     * @return
     */
    private InterfaceInfoF extracted(InvoiceDataReqF invoiceDataReqF,String interfaceCode,InvoiceE invoiceE,String tenantId) {
        String invoceData = JSONUtil.toJsonStr(invoiceDataReqF);
        String encode = Base64.encode(invoceData.getBytes(StandardCharsets.UTF_8));
        return getInterfaceInfoF(invoiceDataReqF.getFptxx().getFpqqlsh(), interfaceCode, encode,invoiceDataReqF.getFptxx().getXsfnsrsbh(),invoiceE,tenantId);
    }

    /**
     * 全电发票统一参数构建--拆分
     * @param serialNo
     * @param interfaceCode
     * @param encode
     * @param taxnum
     * @return
     */
    @NotNull
    private InterfaceInfoF getInterfaceInfoF(String serialNo, String interfaceCode, String encode,String taxnum, InvoiceE invoiceE,String tenantId) {
        InterfaceInfoF interfaceInfoF = new InterfaceInfoF();
        interfaceInfoF.setVersion(VERSION);//接口版本
        interfaceInfoF.setInterfaceCode(interfaceCode);//接口编码
        interfaceInfoF.setTaxpayerId(taxnum);//纳税人识别号
        interfaceInfoF.setPassWord(DigestUtils.md5Hex(getToken(tenantId,taxnum)+"&"+ serialNo));//报文密码
        if(null != invoiceE.getExtensionNumber() && null != invoiceE.getTerminalNumber()){
            interfaceInfoF.setFjh(invoiceE.getExtensionNumber()+ "-" +invoiceE.getTerminalNumber());//开票分机号
        }else if(null != invoiceE.getExtensionNumber()){
            interfaceInfoF.setFjh(invoiceE.getExtensionNumber().toString());//开票分机号
        }
        interfaceInfoF.setJqbh(isNotBlank(invoiceE.getMachineCode()));//机器编号
        interfaceInfoF.setDataExchangeId(serialNo);//数据交换流水号
        interfaceInfoF.setZipCode("0");//压缩代码
        interfaceInfoF.setEncryptCode("0");//加密方式代码
        interfaceInfoF.setContent(encode);//实体转base64数据
        return interfaceInfoF;
    }

    /**
     * 获取方圆秘钥
     *
     * @param tenantId
     * @param taxnum
     * @return
     */
    private String getToken(String tenantId, String taxnum) {
        NuonuoTokenE nuonuoTokenE = nuonuoTokenRepository.getToken(tenantId, taxnum);
        if (null == nuonuoTokenE) {
            throw BizException.throw400("该税号:"+taxnum+"未配置相应token，请联系管理员");
        }
        return nuonuoTokenE.getToken();
    }

    /**
     * 全电发票开票主体信息
     * @param invoiceReceiptE
     * @param invoiceE
     * @return
     */
    private FptxxF queryFptxxF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,FangyuanQdKPLXEnum fangyuanQdKPLXEnum,String billId) {
        FptxxF fptxxF = new FptxxF();
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        fptxxF.setFpqqlsh(serialNo);//发票请求唯一流水号--必填
        fptxxF.setNsrsbh(isNotBlank(invoiceE.getSalerTaxNum()));//开票方纳税人识别号--必填
        fptxxF.setNsrmc(invoiceReceiptE.getInvRecUnitName());//纳税人名称--必填
        fptxxF.setBmbbbh(" ");//编码版本号
        fptxxF.setKplx(isNotBlank(invoiceReceiptE.getCommunityName()));//主要开票项目
        fptxxF.setXsfnsrsbh(isNotBlank(invoiceE.getSalerTaxNum()));//销货方识别号--必填
        fptxxF.setXsfmc(invoiceReceiptE.getInvRecUnitName());//销货方名称--必填
        fptxxF.setXsfdzdh(isNotBlank(invoiceE.getSalerAddress()) +" "+ invoiceE.getSalerTel());//销售方地址电话--必填
        fptxxF.setXsfyhzh(isNotBlank(invoiceE.getSalerAccount()));//销货方银行账号--必填
        fptxxF.setGmfmc(isNotBlank(invoiceE.getBuyerName()));//购货方名称--必填
        fptxxF.setGmfnsrsbh(isNotBlank(invoiceE.getBuyerTaxNum()));//购货方识别号
        fptxxF.setGmfdzdh(isNotBlank(invoiceE.getBuyerAddress()) + isNotBlank(invoiceE.getBuyerTel()));//购货方地址
        fptxxF.setGmfyhzh(isNotBlank(invoiceE.getBuyerAccount()));//购货方银行账号
        fptxxF.setGmfsj(isNotBlank(invoiceE.getBuyerPhone()));//购货方手机，此字段有值会进行短信推送
        fptxxF.setGmfemail(isNotBlank(invoiceE.getEmail()));//购货方邮箱，此字段有值会进行邮箱推送
        fptxxF.setKpr(isNotBlank(invoiceReceiptE.getClerk()));//开票员--必填
        fptxxF.setSkr(" ");//收款员--必填
        fptxxF.setFhr(" ");//复核人--必填
        fptxxF.setKplx(fangyuanQdKPLXEnum.getCode().toString());//开票类型--必填
        if(InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())){
            fptxxF.setFpzl(FangyuanSWJGDMEnum.数电普票.getCode());//发票种类--数电普票
        }else if(InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())){
            fptxxF.setFpzl(FangyuanSWJGDMEnum.数电专票.getCode());//发票种类--数电专票
        }
        fptxxF.setQdbz(null);// 清单标志--必填 todo 待定
        fptxxF.setChyy(NuonuoRedReasonEnum.开票有误.getCode());//冲红原因  数电红字确认单时必填
        fptxxF.setBz(isNotBlank(invoiceReceiptE.getRemark()));//备注
        fptxxF.setDkbz(FangyuanDKBZEnum.自开.getCode());//代开标志--必填
        fptxxF.setSqdh(billId);//申请单号--必填
        fptxxF.setYfphm(invoiceE.getInvoiceNo());//原发票号码
        fptxxF.setYfpdm(invoiceE.getInvoiceCode());//原发票代码
        fptxxF.setSqsm(FangyuanQdsqsmEnum.销方申请.getCode());//申请说明--必填
        fptxxF.setLpkprq(DateUtil.formatLocalDateTime(invoiceE.getGmtCreate()));//蓝票开票日期--必填
        fptxxF.setBz(invoiceReceiptE.getRemark());
        return fptxxF;
    }

    /**
     * 全电发票详情信息构建
     *
     * 折扣发票：蓝票时分被折扣行和折扣行，红冲时不分，就传算好后的正常行
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param taxItemMapByChargeId
     * @param fangyuanQdKPLXEnum
     * @return
     */
    private List<XmxxsF> queryXmxxs(InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Map<Long,
            List<TaxChargeItemD>> taxItemMapByChargeId, FangyuanQdKPLXEnum fangyuanQdKPLXEnum,FptxxF fptxxF) {
        // 按照费项和税率进行分组
        Map<String, List<InvoiceReceiptDetailE>> invoiceReceiptDetailGroupMap = invoiceReceiptDetailES.stream().collect(
                Collectors.groupingBy(
                        detail -> detail.getChargeItemId() + "-" + detail.getTaxRate() + "-" +detail.getOverdue(),
                        TreeMap::new,
                        Collectors.toList()
                ));
        List<XmxxsF> xmxxsFS =new ArrayList<>();
        Integer num = 1;
        // 行号
        Integer lineNum = 1;
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
//        BigDecimal totalTax = BigDecimal.ZERO;
        Integer freeTax = invoiceE.getFreeTax();
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> entry : invoiceReceiptDetailGroupMap.entrySet()) {
            // 税率
            BigDecimal taxRate = BigDecimal.ZERO;
            // 总金额（含税金额）
            BigDecimal goodsTotalPrice = BigDecimal.ZERO;
//            BigDecimal goodsTotalTax = BigDecimal.ZERO;
            BigDecimal ONE_HUNDRED = new BigDecimal(100);
            XmxxsF xmxxsF =new XmxxsF();
            xmxxsF.setFphxz("0");//发票行性质--必填  默认为 0 正常行
            for (InvoiceReceiptDetailE invoiceReceiptDetailE : entry.getValue()) {
                // 行号
                invoiceReceiptDetailE.setLineNo(lineNum);
                xmxxsF.setFphbh(String.valueOf(num));// 发票行编号
                if (Objects.nonNull(invoiceReceiptDetailE.getDiscountAmount())
                        && FangyuanQdKPLXEnum.蓝票.equalsByCode(fangyuanQdKPLXEnum.getCode())
                        && invoiceReceiptDetailE.getDiscountAmount().compareTo(0L) != 0) {
                    xmxxsF.setFphxz("2");//发票行性质--必填  如果有折扣信息，改为被折扣行
                }
                xmxxsF.setXmmc(isNotBlank(invoiceReceiptDetailE.getGoodsName()));//项目名称--必填
//                xmxxsF.setDw(isNotBlank(invoiceReceiptDetailE.getUnit()));//项目单位
//                xmxxsF.setGgxh(isNotBlank(invoiceReceiptDetailE.getSpectype()));//规格型号
                xmxxsF.setXmsl((FangyuanQdKPLXEnum.蓝票.getCode().equals(fangyuanQdKPLXEnum.getCode()))? "1": "-1");//项目数量
                xmxxsF.setHsbz(null == invoiceReceiptDetailE.getWithTaxFlag() ? " " :String.valueOf(invoiceReceiptDetailE.getWithTaxFlag()));//含税标志--必填
                xmxxsF.setSpbm(handleGoodsCode(invoiceReceiptDetailE, taxItemMapByChargeId));//商品分类编码--必填
                xmxxsF.setZxbm("");//自行编码
                // 税率
                if (StringUtils.isNotBlank(invoiceReceiptDetailE.getTaxRate()) && !invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                    // 有税率
                    taxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
                }
                // 当前单条详情（账单）的含税金额
                BigDecimal invoiceAmount = getInvoiceAmount(invoiceReceiptDetailE, fangyuanQdKPLXEnum);
                goodsTotalPrice = goodsTotalPrice.add(invoiceAmount).setScale(8, RoundingMode.HALF_EVEN);
                /*// 计算税额 税额=(数量*含税单价)*税率/(1+税率），也就是当前分组出来的记录的  总价格*税率/(1+税率）
                BigDecimal goodsOneTax = invoiceAmount.multiply(taxRate).divide(taxRate.add(BigDecimal.ONE), 8, RoundingMode.HALF_EVEN);
                goodsTotalTax = goodsTotalTax.add(goodsOneTax).setScale(8, RoundingMode.HALF_EVEN);*/
                if(taxRate.compareTo(BigDecimal.ZERO)>0){
                    if (taxRate.compareTo(new BigDecimal("0.03")) == 0 || taxRate.compareTo(new BigDecimal("0.05")) == 0){
                        xmxxsF.setSl(String.valueOf(taxRate.multiply(ONE_HUNDRED)));//税率--必填
                        xmxxsF.setLslbs("");//零税率标识
                        xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.使用.getCode());//优惠政策标识--必填
                        xmxxsF.setZzstsgl("简易征收");//增值税特殊管理
                    } else {
                        xmxxsF.setSl(BigDecimal.ZERO.equals(taxRate) ? "0": String.valueOf(taxRate.multiply(ONE_HUNDRED)));//税率--必填
                        xmxxsF.setLslbs("");//零税率标识
                        xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                        xmxxsF.setZzstsgl("");//增值税特殊管理
                    }
                } else{
                    xmxxsF.setSl("0");//税率--必填
                    xmxxsF.setLslbs(FangyuanLSLBSEnum.普通零税率.getCode());//零税率标识
                    xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    xmxxsF.setZzstsgl("");//增值税特殊管理
//                    xmxxsF.setSe("0.00");//税额
                }
                // todo 是否取总额减税额
               /* totalPrice.subtract(goodtotalTax).setScale(2, RoundingMode.FLOOR);
                xmxxsF.setKce(totalPrice.toString());//扣除额*/
                if (freeTax != null && freeTax == 1) {
                    xmxxsF.setZzstsgl(FangyuanLSLBSEnum.免税.getDes());//增值税特殊管理
                    xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.使用.getCode());//优惠政策标识--必填
                    xmxxsF.setLslbs(FangyuanLSLBSEnum.免税.getCode());//零税率标识
                    xmxxsF.setSl("0");
//                    xmxxsF.setSe("0.00");
                }
            }
            totalPriceTax = totalPriceTax.add(goodsTotalPrice);
            xmxxsF.setXmje((goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).toString()));//项目金额
            xmxxsFS.add(xmxxsF);
            num++;
            BigDecimal discountGoodPrice = addDigitizedDetails(invoiceE, entry.getValue(), num,
                    taxItemMapByChargeId, xmxxsFS, fangyuanQdKPLXEnum);
            totalPriceTax = totalPriceTax.add(discountGoodPrice).setScale(8, RoundingMode.HALF_EVEN);
           /* totalTax = totalTax.add(goodsTotalTax).setScale(8, RoundingMode.HALF_EVEN);
            xmxxsF.setSe(goodsTotalTax.setScale(2, RoundingMode.HALF_EVEN).toString());//税额
            xmxxsF.setXmdj(goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).abs().toString());//项目单价*/
            // 循环结束 行号加1
            lineNum += 1;
        }
        fptxxF.setJshj(String.valueOf(totalPriceTax.setScale(2, RoundingMode.HALF_EVEN)));//价税合计金额--必填
        /*
        // 价税合计和税额之前已计算为负数，直接相减即可
        BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(8, RoundingMode.HALF_EVEN);
        fptxxF.setHjje(String.valueOf(totalPrice.setScale(2, RoundingMode.HALF_EVEN)));//合计不含税金额--必填
        fptxxF.setHjse(String.valueOf(totalTax.setScale(2, RoundingMode.HALF_EVEN)));//合计税额--必填
        if (freeTax != null && freeTax == 1) {
            fptxxF.setHjje(String.valueOf(totalPriceTax.setScale(2, RoundingMode.HALF_EVEN)));//合计不含税金额
            fptxxF.setHjse("0");//合计税额
        }*/
        return xmxxsFS;
    }

    private BigDecimal getInvoiceAmount(InvoiceReceiptDetailE invoiceReceiptDetailE, FangyuanQdKPLXEnum fangyuanQdKPLXEnum) {
        Long invoiceAmountLongValue = invoiceReceiptDetailE.getInvoiceAmount();
        BigDecimal ONE_HUNDRED = new BigDecimal(100);
        if (FangyuanQdKPLXEnum.红票.equalsByCode(fangyuanQdKPLXEnum.getCode())) {
            return BigDecimal.valueOf(invoiceAmountLongValue).divide(ONE_HUNDRED, 8, RoundingMode.HALF_EVEN);
        }
        Long discountAmountLongValue = Objects.isNull(invoiceReceiptDetailE.getDiscountAmount()) ? 0L : invoiceReceiptDetailE.getDiscountAmount();
        return BigDecimal.valueOf(invoiceAmountLongValue + discountAmountLongValue)
                .divide(ONE_HUNDRED, 8, RoundingMode.HALF_EVEN);
    }

    private BigDecimal addDigitizedDetails(InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Integer num,
                                           Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, List<XmxxsF> xmxxsFS,
                                           FangyuanQdKPLXEnum fangyuanQdKPLXEnum) {
        // 总金额（含税金额）
        BigDecimal goodsTotalPrice = BigDecimal.ZERO;
        if (FangyuanQdKPLXEnum.红票.equalsByCode(fangyuanQdKPLXEnum.getCode())) {
            return goodsTotalPrice;
        }
        List<InvoiceReceiptDetailE> discountDetails = invoiceReceiptDetailES.stream()
                .filter(detail -> Objects.nonNull(detail.getDiscountAmount()) && detail.getDiscountAmount().compareTo(0L) != 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(discountDetails)) {
            Integer freeTax = invoiceE.getFreeTax();
            // 税率
            BigDecimal taxRate = BigDecimal.ZERO;
            BigDecimal ONE_HUNDRED = new BigDecimal(100);
            XmxxsF xmxxsF =new XmxxsF();
            for (InvoiceReceiptDetailE invoiceReceiptDetailE : discountDetails) {
                xmxxsF.setFphbh(String.valueOf(num));// 发票行编号
                xmxxsF.setFphxz("1");//发票行性质--必填--折扣行
                xmxxsF.setXmmc(isNotBlank(invoiceReceiptDetailE.getGoodsName()));//项目名称--必填
                xmxxsF.setXmsl("-1");//项目数量
                xmxxsF.setHsbz(null == invoiceReceiptDetailE.getWithTaxFlag() ? " " :String.valueOf(invoiceReceiptDetailE.getWithTaxFlag()));//含税标志--必填
                xmxxsF.setSpbm(handleGoodsCode(invoiceReceiptDetailE, taxItemMapByChargeId));//商品分类编码--必填
                xmxxsF.setZxbm("");//自行编码
                // 税率
                if (StringUtils.isNotBlank(invoiceReceiptDetailE.getTaxRate()) && !invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                    // 有税率
                    taxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
                }
                // 当前单条详情（账单）的折扣金额
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getDiscountAmount()).negate().divide(ONE_HUNDRED, 8, RoundingMode.HALF_EVEN);
                goodsTotalPrice = goodsTotalPrice.add(invoiceAmount).setScale(8, RoundingMode.HALF_EVEN);
                if(taxRate.compareTo(BigDecimal.ZERO)>0){
                    xmxxsF.setSl(BigDecimal.ZERO.equals(taxRate) ? "0": String.valueOf(taxRate.multiply(ONE_HUNDRED)));//税率--必填
                    xmxxsF.setLslbs("");//零税率标识
                    xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    xmxxsF.setZzstsgl("");//增值税特殊管理
                }else{
                    xmxxsF.setSl("0");//税率--必填
                    xmxxsF.setLslbs(FangyuanLSLBSEnum.普通零税率.getCode());//零税率标识
                    xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.不使用.getCode());//优惠政策标识--必填
                    xmxxsF.setZzstsgl("");//增值税特殊管理
                }
                if (freeTax != null && freeTax == 1) {
                    xmxxsF.setZzstsgl(FangyuanLSLBSEnum.免税.getDes());//增值税特殊管理
                    xmxxsF.setYhzcbs(FangyuanYHZCBSEnum.使用.getCode());//优惠政策标识--必填
                    xmxxsF.setLslbs(FangyuanLSLBSEnum.免税.getCode());//零税率标识
                    xmxxsF.setSl("0");
                }
            }
            xmxxsF.setXmje(goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).toEngineeringString());//项目金额
            // 蓝票时，数量是-1，单价是正数，上面的项目金额是负数
            xmxxsF.setXmdj(goodsTotalPrice.setScale(2, RoundingMode.HALF_EVEN).abs().toEngineeringString());
            xmxxsFS.add(xmxxsF);
        }
        return goodsTotalPrice;
    }

    /**
     * 全电红冲申请单查询入参信息
     * @param applyE
     * @param invoiceE
     * @return
     */
    private FptxxF queryFptxxRedApply(InvoiceRedApplyE applyE, InvoiceE invoiceE) {
        FptxxF fptxxF = new FptxxF();
//        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
//        fptxxF.setFpqqlsh(serialNo);//发票请求唯一流水号--必填
        fptxxF.setXsfnsrsbh(isNotBlank(invoiceE.getSalerTaxNum()));//销货方识别号--必填
        fptxxF.setGmfmc(isNotBlank(invoiceE.getBuyerName()));//购货方名称--必填
        fptxxF.setGmfnsrsbh(isNotBlank(invoiceE.getBuyerTaxNum()));//购货方识别号
        fptxxF.setFpzl(FangyuanSWJGDMEnum.数电普票.getCode());//发票种类
        fptxxF.setSqdh(String.valueOf(applyE.getId()));// 申请单号
        fptxxF.setSqsm(FangyuanQdsqsmEnum.销方申请.getCode());//申请说明--必填
        fptxxF.setXxblx("0");//信息表/确认单类型
        fptxxF.setSqsjq(DateUtil.formatLocalDateTime(invoiceE.getGmtCreate()));//申请时间开始--必填
        fptxxF.setSqsjz(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));//申请时间结束
        return fptxxF;
    }


    /**
     * 根据费项ids查询税目
     * @param invoiceReceiptDetailES
     * @return
     */
    private Map<Long, List<TaxChargeItemD>> getLongListMap(List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        //补齐税目
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
        if (taxIteamMapByChargeId == null) {
            throw BizException.throw400("该批次费项未配置对应的税目编码");
        }
        return taxIteamMapByChargeId;
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
     * 为空返回空字符串
     * @param str
     * @return
     */
    private String isNotBlank(String str){
        if(StringUtils.isNotBlank(str)){
            return str;
        }
        return " ";
    }

    /**
     * 字符串转时间戳(秒)
     * @param time
     * @return
     */
    private String str2TimeStamp(String time) {
        if(StringUtils.isBlank(time)){
            return null;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(time);
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderF handleNuonuoOrderBlue(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                        List<Integer> pushMode, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId, String billInfoNo) {
        OrderF orderF = generalOrderF(invoiceReceiptE, invoiceE, pushMode, nuonuoCommunityId, billInfoNo);
        orderF.setInvoiceDetail(generalInvoiceDetailMerge(invoiceE, invoiceReceiptDetailES, taxIteamMapByChargeId, invoiceReceiptE));
        return orderF;
    }
    private OrderF generalOrderF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<Integer> pushMode, String nuonuoCommunityId, String billInfoNo) {
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
        if("2".equals(orderF.getInvoiceType())){
            orderF.setBillInfoNo(billInfoNo);
            orderF.setRemark("开具红字增值税专用发票信息表编号" + billInfoNo);
        } else {
            orderF.setRemark(invoiceReceiptE.getRemark());
        }
        orderF.setInvoiceLine(InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType()).getNuonuoCode());
        orderF.setInvoiceNum(invoiceE.getInvoiceNo());
        orderF.setClerk(invoiceReceiptE.getClerk());
        // ---- begin------
        // update by haoqiang
        //  收款人 开票人  取一个值
        // 复核人  写死 廖莹  是吧
        // 是的
        orderF.setPayee(invoiceReceiptE.getClerk());
        orderF.setChecker("廖莹");
        //   ------  end-----
        orderF.setInvoiceCode(invoiceE.getInvoiceCode());
        orderF.setInvoiceNum(invoiceE.getInvoiceNo());
        orderF.setMachineCode(invoiceE.getMachineCode());//机器编码
        orderF.setDepartmentId(nuonuoCommunityId);//部门门店id


        return orderF;
    }
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
    private InvoiceDetailF generalMergeInvoiceDetail(InvoiceE invoiceE,
                                                     List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                     Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId,
                                                     InvoiceReceiptE invoiceReceiptE, int lineNo) {
        InvoiceDetailF invoiceDetailF = new InvoiceDetailF();
        BigDecimal taxExcludedAmountSum = BigDecimal.ZERO;
        BigDecimal taxSum = BigDecimal.ZERO;
        BigDecimal invoiceAmountSum = BigDecimal.ZERO;
        Integer freeTax = invoiceE.getFreeTax();
        BigDecimal simpleCollectionRate = new BigDecimal("5");
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailES) {
            // 行号
            invoiceReceiptDetailE.setLineNo(lineNo);
            invoiceDetailF.setInvoiceLineProperty("0");
            if("2".equals(invoiceE.getInvoiceType().toString())){
                invoiceDetailF.setNum("-1");
            } else {
                invoiceDetailF.setNum("1");
            }
            invoiceDetailF.setWithTaxFlag(invoiceReceiptDetailE.getWithTaxFlag().toString());
            invoiceDetailF.setFavouredPolicyFlag(null);
            invoiceDetailF.setTaxRate(invoiceReceiptDetailE.getTaxRate());
            invoiceDetailF.setUnit(null);
            invoiceDetailF.setDeduction(null);
            BigDecimal detailTaxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
            //零税率标识  1,免税;2,不征税;3,普通零税率；
            if (org.apache.commons.lang3.StringUtils.isBlank(invoiceReceiptDetailE.getTaxRate()) || invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
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
                if (InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())) {
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
        if (invoiceE.getInvoiceType() == 2){
            invoiceDetailF.setPrice(String.valueOf(invoiceAmountSum.multiply(new BigDecimal("-1"))));
        } else {
            //单价为合并展示账单的开票金额
            invoiceDetailF.setPrice(String.valueOf(invoiceAmountSum));
        }
        invoiceDetailF.setTaxIncludedAmount(invoiceAmountSum.toString());//含税金额
        invoiceDetailF.setTax(handleTax(freeTax, invoiceAmountSum, invoiceReceiptDetailES.get(0).getTaxRate()));//处理税额
        invoiceDetailF.setTaxExcludedAmount(handleTaxExcludedAmount(freeTax, invoiceAmountSum,invoiceReceiptDetailES.get(0).getTaxRate()));//处理不含税金额

        return invoiceDetailF;
    }

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
    private String handleTaxExcludedAmount(Integer freeTax, BigDecimal taxIncludedAmount,String taxRate) {
        if (freeTax != null && freeTax == 1) {
            return taxIncludedAmount.toString();
        }
        BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(taxRate));
        //不含税金额 = 含税金额/(1+税率)
        BigDecimal taxExcludedAmount = taxIncludedAmount.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
        return taxExcludedAmount.toString();
    }
    private List<Integer> handlePushModeStr(String pushModeStr) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pushModeStr)){
            List<Integer> pushModeList = JSONObject.parseObject(pushModeStr, List.class);
            return pushModeList;
        }
        return Lists.newArrayList(NuonuoPushModeEnum.不推送.getCode());
    }

    private InvoiceRedApplyF handleInvoiceRedApplyF(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, InvoiceTypeEnum invoiceTypeEnum) {
        InvoiceRedApplyF  redApplyF = new InvoiceRedApplyF();
        JSONObject jsonObject = JSONObject.parseObject(invoiceE.getThridReturnParameter());
        String machineCode = jsonObject.getString("machineCode");
        String time = DateTimeUtil.nowDateTimeNoc().substring(2);
        redApplyF.setBillNo(machineCode + time);
        redApplyF.setBuyerName(invoiceE.getBuyerName());
        redApplyF.setBuyerTaxNo(invoiceE.getBuyerTaxNum());
        redApplyF.setInvoiceLine("s");
        redApplyF.setOriInvoiceCode(invoiceE.getInvoiceCode());
        redApplyF.setOriInvoiceNumber(invoiceE.getInvoiceNo());
        redApplyF.setSellerName(invoiceE.getSalerName());
        redApplyF.setSellerTaxNo(invoiceE.getSalerTaxNum());
        redApplyF.setTaxnum(invoiceE.getSalerTaxNum());
        redApplyF.setTenantId(tenantId);
        redApplyF.setTaxAmount(new BigDecimal(invoiceReceiptE.getPriceTaxAmount().toString()).divide(new BigDecimal("100")));
        List<InvoiceDetailF> invoiceDetailves = generalInvoiceDetailMerge(invoiceE, invoiceReceiptDetailES, taxItemMapByChargeId, invoiceReceiptE);
        BigDecimal taxExcludedAmountSum = BigDecimal.ZERO;
        List<InvoiceRedApplyDetailF> invoiceRedApplyDetails = new ArrayList<>();
        for (InvoiceDetailF invoiceDetailve : invoiceDetailves) {
            taxExcludedAmountSum =  taxExcludedAmountSum.add(new BigDecimal(invoiceDetailve.getTaxExcludedAmount()));
            InvoiceRedApplyDetailF applyDetailF = new InvoiceRedApplyDetailF();
            applyDetailF.setGoodsCode(invoiceDetailve.getGoodsCode());
            applyDetailF.setGoodsName(invoiceDetailve.getGoodsName());
            applyDetailF.setTaxRate(invoiceDetailve.getTaxRate());
            applyDetailF.setWithTaxFlag("true");
            applyDetailF.setTaxAmount(invoiceDetailve.getTax());
            applyDetailF.setTaxExcludedAmount(invoiceDetailve.getTaxExcludedAmount());
            invoiceRedApplyDetails.add(applyDetailF);
        }
        redApplyF.setTaxExcludedAmount(taxExcludedAmountSum);
        redApplyF.setProductOilFlag("0");
        redApplyF.setInvoiceRedApplyDetails(invoiceRedApplyDetails);
        return redApplyF;
    }
}
