package com.wishare.finance.domains.invoicereceipt.facade;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.invoice.fo.ProvinceCityF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemAppService;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceRedA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.WithTaxFlagEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.*;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceZoningRepository;
import com.wishare.finance.domains.invoicereceipt.support.baiwang.BaiwangjsySupport;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.NuonuoInvoiceStatusEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums.PriceTaxMarkEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums.*;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.*;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response.CommonResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response.InvoiceSuccessResV;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response.RedInvoiceSuccessResV;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.NuonuoRedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;

/**
 * 发票外部申请接口-百望金税云实现
 * @author dongpeng
 * @date 2023/10/25 18:57
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(name = "invoice.supplier",havingValue = "baiwangjsy")
public class BaiwangjsyFacadeImpl extends InvoiceExternalAbService{

    private final BaiwangjsySupport baiwangjsySupport;

    private final TaxItemAppService taxItemAppService;

    private final SpaceClient spaceClient;

    private final InvoiceZoningRepository invoiceZoningRepository;


    @Override
    public String paperInvoices(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum invoiceTypeEnum, String billInfoNo) {
        return null;
    }

    @Override
    public String invoicesPrints(InvoicePrintF form, String tenantId) {
        return null;
    }

    /**
     * 百望金税云全电开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @return
     */
    @Override
    public String opeMplatformBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId) {
        // 开票流水号
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取请求数据体，并补充
        BlueInvoiceDataReqF dataReq = getBlueInvoiceDataReq(invoiceE, invoiceReceiptE, serialNo);
        // 获取请求数据体详情
        List<BlueInvoiceDetailF> blueInvoiceDetailReq = getBlueInvoiceDetailReq(invoiceDetailDtoList, dataReq,invoiceE);
        // 获取请求体
        dataReq.setMxxx(blueInvoiceDetailReq);
        if(BeanUtil.isNotEmpty(invoiceE.getBuildingServiceInfo())){
            // 建筑服务信息
            dataReq.setTdyslxdm("03");
            dataReq.setJzfw(invoiceE.getBuildingServiceInfo());
        }else if(BeanUtil.isNotEmpty(invoiceE.getRealEstateLeaseInfo())){
            // 房地产租赁信息
            dataReq.setTdyslxdm("06");
            dataReq.setBdcjyzlfw(invoiceE.getRealEstateLeaseInfo());
        }
        baiwangjsySupport.qdInvoiceIssue(MethodEnum.正票申请,dataReq,tenantId);
        return serialNo;
    }


    /**
     * 百望金税云全电开票结果查询接口
     * @param tenantId
     * @param serialNum
     * @param taxNum
     */
    @Override
    public List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(String tenantId, String serialNum, String taxNum, Long orderNo, InvoiceE invoiceE) {
        Map<String,String> map = new HashedMap<>();
        map.put("djbh",serialNum);
        String content = JSON.toJSONString(map);
        CommonResV commonResV = baiwangjsySupport.qdInvoiceSearch(MethodEnum.正票开具结果查询, content, tenantId);
        return opeMplatformQueryInvoiceResultTransferToNuonuoResulst(commonResV);
    }

    @Override
    public String invoiceRedApply(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE, List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList, String nuonuoCommunityId, Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId, com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum invoiceTypeEnum) {
        return null;
    }


    /**
     * 全电红冲申请单
     * @return
     */
    @Override
    public String electronInvoiceRedApply(InvoiceRedA invoiceRedA) {
        InvoiceReceiptE invoiceReceiptRedE = invoiceRedA.getInvoiceReceiptRedE();
        InvoiceE invoiceRedE = invoiceRedA.getInvoiceRedE();
        List<InvoiceDetailDto> invoiceDetailDtoList = invoiceRedA.getInvoiceDetailDtoList();
        // 获取请求数据体，并补充
        RedInvoiceDataReqF dataReq = getRedInvoiceDataReq(invoiceRedE, invoiceReceiptRedE, invoiceRedE.getInvoiceSerialNum());
        // 获取请求数据体详情
        List<RedInvoiceDetailF> redInvoiceDetailReq = getRedInvoiceDetailReq(invoiceDetailDtoList, dataReq,invoiceRedE);
        if(BeanUtil.isNotEmpty(invoiceRedE.getBuildingServiceInfo())){
            // 建筑服务信息
            dataReq.setTdyslxdm("03");
            dataReq.setJzfw(invoiceRedE.getBuildingServiceInfo());
        }else if(BeanUtil.isNotEmpty(invoiceRedE.getRealEstateLeaseInfo())){
            // 房地产租赁信息
            dataReq.setTdyslxdm("06");
            dataReq.setBdcjyzlfw(invoiceRedE.getRealEstateLeaseInfo());
        }
        // 获取请求体
        dataReq.setMxxx(redInvoiceDetailReq);
        baiwangjsySupport.qdRedInvoice(MethodEnum.红字申请, dataReq, invoiceReceiptRedE.getTenantId());
        return dataReq.getDjbh();
    }

    /**
     * 全电红冲申请单确认单查询
     * @return
     */
    @Override
    public List<NuonuoRedApplyQueryV> electronInvoiceRedApplyQuery(InvoiceRedApplyE applyE, InvoiceE invoiceE) {
        Map<String,String> map = new HashedMap<>();
        map.put("djbh",applyE.getInvoiceSerialNum());
        String content = JSON.toJSONString(map);
        CommonResV commonResV = baiwangjsySupport.qdRedInvoiceSearch(MethodEnum.红字发票确认单状态查询, content, invoiceE.getTenantId());
        return electronInvoiceRedApplyResultTransferToNuonuoResulst(commonResV, applyE);
    }


    /**
     * 百望金税云全电发票冲红
     * @return
     */
    @Override
    public String electronInvoiceRed(InvoiceE invoiceE, NuonuoRedApplyQueryV queryV) {
        /*
        Map<String,String> map = new HashedMap<>();
        map.put("djbh",invoiceE.getInvoiceSerialNum());
        String content = JSON.toJSONString(map);
        baiwangjsySupport.qdRedInvoiceSearch(MethodEnum.红字开具,content,invoiceE.getTenantId());
        */
        // 百望金税云红冲单申请后默认开具发票不需要再次红冲
        return invoiceE.getInvoiceSerialNum();
    }

    /**
     * 经营租赁租赁费 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     * @param invoiceReceiptE
     */
    @Override
    public void checkRealPropertyRentInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE, List<Long> chargeItemIds, InvoiceReceiptE invoiceReceiptE) {
        // 判断发票类型  如果不是全电发票就return
        // 只校验全电普票
        if(!InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) && !InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())){
            return;
        }
        // 包含 304050202-编码 都是租赁服务 税目编码 304050202-不动产经营租赁  30405020202-车辆停放服务
        // 判断账单是否是 同一个房号
        List<TaxChargeItemD> taxChargeItemRvs = taxItemAppService.queryByChargeIdList(chargeItemIds);
        if (taxChargeItemRvs.stream().anyMatch(s -> (s.getTaxItem().getCode().contains("304050202")))) {
            // 获取空间详情列表
            List<SpaceDetails> details = super.getSpaceDetails(billDetailMoreVList);
            log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(details));
            if (CollectionUtils.isNotEmpty(details)){
                // 根据空间详情获取项目信息
                SpaceCommunityV communityInfo = super.getSpaceCommunityV(details);
                // 获取账单日期列表
                List<LocalDateTime> billDate = super.getBillDate(billDetailMoreVList);
                // 根据名字 获取省级的code 获取诺诺提供的区划
                // 获取 该code下的市一级
                // 该code 没有下一级  则
                // 判断财务中台的区级 是否在诺诺区划中 若在 则该
                InvoiceZoningE byAreaName = invoiceZoningRepository.getByAreaName(communityInfo.getProvince());
                // 获取省市区信息
                String areaName = super.getStringBuilder(byAreaName, communityInfo).getFullName();
                // 设置地址信息
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                InvoiceRealEstateLeaseInfoF realEstateLeaseInfoF = InvoiceRealEstateLeaseInfoF.builder().
                        bdcdz(areaName.toString()).
                        fulladdress(super.getAddress(communityInfo, areaName)).
                        zlqqz(billDate.get(0).format(dateTimeFormatter) +" "+ billDate.get(billDate.size()-1).format(dateTimeFormatter)).
                        kdsbz("N").
                        cqzsh(StringUtils.isBlank(communityInfo.getEstateProperty()) ? "无" : communityInfo.getEstateProperty()).
                        dw("平方米").
                        build();
                log.info("不动产发票开具地址信息:" + JSONObject.toJSONString(realEstateLeaseInfoF));
                /** 不动产经营租赁服务 */
                invoiceE.setRealEstateLeaseInfo(realEstateLeaseInfoF);
            }
        }
    }

    /**
     * 建筑服务 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     */
    @Override
    public void checkBuildingServiceInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                                         List<Long> chargeItemIds, InvoiceReceiptE invoiceReceiptE,
                                         InvoiceBuildingServiceInfoF serviceInfoF){
        // 判断发票类型  如果不是全电发票就return
        // 只校验全电普票
        if(!InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) && !InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())){
            return;
        }
        // 包含 305030-编码 都是建筑服务
        // 判断账单是否是 同一个房号
        if (ObjectUtil.isNotNull(serviceInfoF)){
            invoiceE.setBuildingServiceInfo(serviceInfoF);
        }else {
            serviceInfoF = handleBuildingServiceInfo(chargeItemIds, billDetailMoreVList);
            if (ObjectUtil.isNotNull(serviceInfoF)){
                invoiceE.setBuildingServiceInfo(serviceInfoF);
            }
        }

    }

    /** 返回示例：建筑服务发票开具地址信息:{"fullAddress":"杭州市西湖区文一西路、崇仁路口东南侧","jzfwfsd":"浙江省杭州市余杭区","jzxmmc":"杭州万福里","kdsbz":"N","tdzzsxmbh":""}
     * @param chargeItemIds
     * @param billDetailMoreVList
     * @return
     */
    @Override
    public InvoiceBuildingServiceInfoF handleBuildingServiceInfo(List<Long> chargeItemIds,List<BillDetailMoreV> billDetailMoreVList){
        List<TaxChargeItemD> taxChargeItemRvs = taxItemAppService.queryByChargeIdList(chargeItemIds);
        if (taxChargeItemRvs.stream().anyMatch(s -> (s.getTaxItem().getCode().contains("305030")))) {
            // 获取空间详情列表
            List<SpaceDetails> details = super.getSpaceDetails(billDetailMoreVList);
            log.info("建筑服务发票开具查询空间信息:" + JSON.toJSONString(details));
            if (CollectionUtils.isNotEmpty(details)){
                // 根据空间详情获取项目信息
                SpaceCommunityV communityInfo = super.getSpaceCommunityV(details);
                // 根据名字 获取省级的code 获取诺诺提供的区划
                // 获取 该code下的市一级
                // 该code 没有下一级  则
                // 判断财务中台的区级 是否在诺诺区划中 若在 则该
                InvoiceZoningE byAreaName = invoiceZoningRepository.getByAreaName(communityInfo.getProvince());
                // 获取省市区信息
                ProvinceCityF cityF = super.getStringBuilder(byAreaName, communityInfo);
                String areaName = cityF.getFullName();
                // 设置地址信息
                InvoiceBuildingServiceInfoF buildingServiceInfoF = InvoiceBuildingServiceInfoF.builder().
                        tdzzsxmbh(gettdzzsxmbh(details)).
                        jzfwfsd(String.valueOf(areaName)).
                        fullAddress(super.getAddress(communityInfo, areaName)).
                        jzxmmc(communityInfo.getName()).
                        kdsbz("N").provinceCityCode(cityF.getProvinceCityCode()).
                        build();
                log.info("建筑服务发票开具地址信息:" + JSONObject.toJSONString(buildingServiceInfoF));
                return buildingServiceInfoF;
            }
        }

        return null;
    }
    private String gettdzzsxmbh(List<SpaceDetails> details){
        String s ="";
        for (SpaceDetails detail : details) {
            if (StringUtils.isNotBlank(detail.getEstateProperty())){
                s = detail.getEstateProperty();
                return s;
            }
        }

        return s;
    }

    /**
     * 获取发票开具请求数据体(全电蓝票)
     */
    public BlueInvoiceDataReqF getBlueInvoiceDataReq(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, String serialNo) {
        BlueInvoiceDataReqF reqF = new BlueInvoiceDataReqF();
        reqF.setDjbh(serialNo);//单据编号(全局唯一) 必填
        reqF.setKpzddm(invoiceE.getTerminalCode());//开票终端代码 必填 91310115667791445X-QD01
        if(InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())){
            reqF.setFplxdm(InvoiceTypeEnum.普通发票.getValue());//发票类型代码 必填
        }else if(InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())){
            reqF.setFplxdm(InvoiceTypeEnum.增值税专用发票.getValue());//发票类型代码 必填
        }
        reqF.setKplx("0");//开票类型 必填
//        reqF.setHsje();//含税金额 必填
        reqF.setZsfs(TaxationMethodEnum.普通征税.getCode());//征收方式 必填
        reqF.setGsdm(invoiceE.getSalerTaxNum());//公司代码（销方税号） 必填 91310115667791445X
//        reqF.setBmdm("608e149a988c494aa90f4bc4f84b8b61");//部门代码 系统组织机构代码
        reqF.setYhdm(invoiceE.getUserCode());//用户代码 必填  bb7f396d857941428b6d5d1083d53635
        reqF.setSjlx(DataTypeEnum.零售系统.getCode());//数据类型 必填
        reqF.setKpr(invoiceReceiptE.getClerk());//开票人
        reqF.setBz(invoiceReceiptE.getRemark());//备注
        reqF.setKhmc(invoiceE.getBuyerName());//购货单位名称 必填
        reqF.setKhsh(invoiceE.getBuyerTaxNum());//购货单位税号 (发票类型代码为01时必填)
        /*
        todo 非必填 暂不填充
        reqF.setQdbz();//清单标识
        reqF.setTdyslxdm();//特定约束类型代码
        reqF.setXfdzdh();//销方地址电话
        reqF.setXfyhzh();//销方银行账户
        reqF.setKhdzdh();//购货单位地址电话
        reqF.setKhyhzh();//购货单位银行账号
        reqF.setGmfMobile();//购货单位手机号
        reqF.setGmfEmail();//购货单位邮箱
        reqF.setGmfEmail1();//购货单位抄送邮箱1
        reqF.setGmfEmail2();//购货单位抄送邮箱2
        reqF.setGmfEmail3();//购货单位抄送邮箱3
        reqF.setHjse();//合计税额
        reqF.setHjje();//合计金额
        reqF.setKce();//扣除额
        reqF.setKjly();//发票理由
        reqF.setFpdm();//发票代码
        reqF.setFphm();//发票号码
        reqF.setSfwzzfp();//是否为纸质发票
        reqF.setZppzdm();//纸票票种代码
        reqF.setHsbz();//含税标志
        reqF.setBmdm();//部门代码
        reqF.setDjrq();//单据日期
        reqF.setSjly();//数据来源
        reqF.setSkr();//收款人
        reqF.setFhr();//复核人
        reqF.setZdr();//制单人
        reqF.setKz1();//扩展1
        reqF.setKz2();//扩展2
        reqF.setKz3();//扩展3
        reqF.setGmfzrrbs();//购买方自然人标识
        reqF.setSpflxConfirm();//事业单位
        reqF.setSxedDefptxgz();//大额发票开具提醒
        reqF.setSfzsgmfyhzh();//是否展示购买方银行账号
        reqF.setSfzsxsfyhzh();//是否展示销售方银行账户
         */
        return reqF;
    }


    /**
     * 获取发票详情（全电蓝票）
     */
    public List<BlueInvoiceDetailF> getBlueInvoiceDetailReq(List<InvoiceDetailDto> invoiceDetailDtoList,
                                                            BlueInvoiceDataReqF dataReq,
                                                            InvoiceE invoiceE) {
        // 结果
        List<BlueInvoiceDetailF> detailReqList = Lists.newArrayList();
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
        BigDecimal totalTax = BigDecimal.ZERO;
        // 免税标识
        Integer freeTax = invoiceE.getFreeTax();
        // 外层data的含税标志
        for(InvoiceDetailDto detailDto : invoiceDetailDtoList){
            BlueInvoiceDetailF detailF = new BlueInvoiceDetailF();
            detailF.setDjhh(String.valueOf(detailDto.getBlueLineNo())); //单据行号 必填
            detailF.setFphxz(InvoicingHouseEnum.正常行.getCode()); //发票行性质 必填
            detailF.setSpmc(detailDto.getGoodsName()); //商品名称 必填
            detailF.setSsbm(detailDto.getGoodsCode()); //税收编码 必填
            BigDecimal priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN);
            detailF.setHsdj(priceTax.abs()); //含税单价
            detailF.setSpsl(BigDecimal.valueOf(detailDto.getGoodsNum())); //商品数量
            detailF.setTax(detailDto.getTaxRate().setScale(3, RoundingMode.DOWN)); //税率 必填
            BigDecimal tax = BigDecimal.ZERO;//税额
            //判断是否免税
            if (freeTax == 1) {
                BigDecimal TaxExclusiveAmount = priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN);
                detailF.setBhsje(TaxExclusiveAmount); //不含税金额（含税标志为0时必填 小数点2位）
                detailF.setZzstsgl(SpecialVATEnum.免税.getName());//增值税特殊管理
                detailF.setYhzcbs(PreferentialPolicyEnum.使用优惠政策.getCode());//优惠政策标识--必填
                detailF.setLslbs(FreeTaxMarkEnum.免税.getCode());//零税率标识
                detailF.setTax(BigDecimal.ZERO);
                detailF.setSe(tax);
            } else {
                if(WithTaxFlagEnum.含税.getCode().equals(detailDto.getWithTaxFlag())) {
                    tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN);
                    detailF.setHsje(priceTax); //含税金额（含税标志为1时必填 小数点2位）
                    detailF.setSe(tax);
                    detailF.setYhzcbs(PreferentialPolicyEnum.不使用.getCode());//优惠政策标识--必填
                    detailF.setLslbs(FreeTaxMarkEnum.正常税率.getCode());//零税率标识
                    if (TaxpayerTypeEnum.一般纳税人.equalsByCode(invoiceE.getTaxpayerType()) && new BigDecimal("0.03").compareTo(detailDto.getTaxRate()) == 0) {
                        detailF.setYhzcbs(PreferentialPolicyEnum.使用优惠政策.getCode());//优惠政策标识--必填
                        detailF.setZzstsgl(SpecialVATEnum.按百分之3简易征收.getName());
                    }
                } else {
                    // 无税率，不含税
                    BigDecimal TaxExclusiveAmount = priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN);
                    detailF.setSe(tax); //税额(含税标志为0时必填 小数点2位)
                    detailF.setBhsje(TaxExclusiveAmount); //不含税金额（含税标志为0时必填 小数点2位）
                    detailF.setYhzcbs(PreferentialPolicyEnum.不使用.getCode());//优惠政策标识--必填
                    detailF.setLslbs(FreeTaxMarkEnum.普通零税率.getCode());//零税率标识
                }
            }
            // 合计
            totalTax = totalTax.add(tax);
            totalPriceTax = totalPriceTax.add(priceTax);
            detailReqList.add(detailF);
            String unit = billMethodUnitMap.get(detailDto.getGoodsUnit());
            if (StringUtils.isNotBlank(unit)) {
                detailF.setJldw(unit);
            }

            /* todo 非必填 暂不填充
            detailF.setBhsdj(); //不含税单价
            detailF.setZzstsgl(); //增值税特殊管理
            detailF.setSpfwjc(); //商品简称
            detailF.setSppc(); //商品批次
            detailF.setSpdm(); //商品代码
            detailF.setSpfl(); //商品分类
            detailF.setZkje(); //商品折扣金额
            detailF.setGgxh(); //规格型号
            detailF.setJldw(); //计量单位
            */
        }
        BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(2, RoundingMode.HALF_EVEN);
        // 根据不同的请求对象赋值总金额、税额、价税合计
        if (Objects.nonNull(dataReq)) {
            if (freeTax == null || freeTax != 1) {
                dataReq.setHsbz(PriceTaxMarkEnum.含税.getCode());//含税标志
            } else {
                dataReq.setHsbz(PriceTaxMarkEnum.不含税.getCode());//含税标志
            }
            dataReq.setHjse(totalTax);//合计税额
            dataReq.setHsje(totalPriceTax);//价税合计
            dataReq.setHjje(totalPrice);//合计金额
        }
        return detailReqList;
    }


    /**
     * 获取发票开具请求数据体(全电红票)
     */
    public RedInvoiceDataReqF getRedInvoiceDataReq(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, String serialNo) {
        RedInvoiceDataReqF reqF = new RedInvoiceDataReqF();
        // 开票流水号
        String djbh = UUID.randomUUID().toString().replaceAll("-", "");
        reqF.setDjbh(djbh);//单据编号(全局唯一) 必填
        reqF.setYqdfphm(invoiceE.getInvoiceNo());//原全电发票号码 必填
//        reqF.setYdjbh(invoiceE.getInvoiceSerialNum());//原单据编号 必填
//        reqF.setYfpdm(invoiceE.getInvoiceCode());//原发票代码
//        reqF.setYfphm(invoiceE.getInvoiceNo());//原发票号码
        reqF.setChyydm(RedReasonEnum.开票有误.getCode());//冲红原因代码 必填
        reqF.setSjlx(DataTypeEnum.零售系统.getCode());//数据类型 必填
        /*reqF.setHsje();//含税金额 必填
        reqF.setHjje();//合计金额 必填
        reqF.setHjse();//合计税额 必填*/
        return reqF;
    }


    /**
     * 获取发票详情（全电红票）
     */
    public List<RedInvoiceDetailF> getRedInvoiceDetailReq(List<InvoiceDetailDto> invoiceDetailDtoList,
                                                          RedInvoiceDataReqF dataReq,
                                                          InvoiceE invoiceE) {
        // 结果
        List<RedInvoiceDetailF> detailReqList = Lists.newArrayList();
        // 价税合计
        BigDecimal totalPriceTax = BigDecimal.ZERO;
        // 合计税额
        BigDecimal totalTax = BigDecimal.ZERO;
        // 行号
        int num = 1;
        // 外层data的含税标志
        Integer freeTax = invoiceE.getFreeTax();
        for(InvoiceDetailDto detailDto : invoiceDetailDtoList){
            RedInvoiceDetailF detailF = new RedInvoiceDetailF();
            detailF.setDjhh(String.valueOf(num)); //单据行号 必填
            BigDecimal priceTax = detailDto.getGoodsTotalPriceTax().setScale(2, RoundingMode.HALF_EVEN);
            detailF.setHsdj(priceTax.abs()); //含税单价
            detailF.setSpsl(BigDecimal.valueOf(detailDto.getGoodsNum())); //商品数量
            BigDecimal tax = BigDecimal.ZERO;
            //判断是否免税
            if (freeTax != null && freeTax == 1) {
                BigDecimal TaxExclusiveAmount = priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN);
                detailF.setBhsje(TaxExclusiveAmount); //不含税金额（含税标志为0时必填 小数点2位）
                detailF.setSe(tax);
            } else {
                // 我方目前都是含税
                if(WithTaxFlagEnum.含税.getCode().equals(detailDto.getWithTaxFlag())) {
                    tax = detailDto.getTax().setScale(2, RoundingMode.HALF_EVEN);
                    detailF.setHsje(priceTax); //含税金额（含税标志为1时必填 小数点2位）
                    detailF.setSe(tax);
                } else {
                    // 无税率，不含税
                    BigDecimal TaxExclusiveAmount = priceTax.subtract(tax).setScale(2, RoundingMode.HALF_EVEN);
                    detailF.setSe(tax); //税额(含税标志为0时必填 小数点2位)
                    detailF.setBhsje(TaxExclusiveAmount); //不含税金额（含税标志为0时必填 小数点2位）
                }
            }
            // 合计
            totalTax = totalTax.add(tax);
            totalPriceTax = totalPriceTax.add(priceTax);
            detailReqList.add(detailF);
            num++;
        }
        BigDecimal totalPrice = totalPriceTax.subtract(totalTax).setScale(2, RoundingMode.HALF_EVEN);
        // 根据不同的请求对象赋值总金额、税额、价税合计
        if (Objects.nonNull(dataReq)) {
            dataReq.setHjse(totalTax);//合计税额
            dataReq.setHsje(totalPriceTax);//价税合计
            dataReq.setHjje(totalPrice);//合计金额
        }
        return detailReqList;
    }


    /**
     * 将百望金税云全电发票查询请求体转换为nuonuo响应体
     * @param commonResV
     * @return
     */
    private List<QueryInvoiceResultV> opeMplatformQueryInvoiceResultTransferToNuonuoResulst(CommonResV commonResV) {
        List<QueryInvoiceResultV> result = new ArrayList<>();
        QueryInvoiceResultV resultV = new QueryInvoiceResultV();
        if(StringUtils.isNotBlank(commonResV.getData())){
            InvoiceSuccessResV data = JSON.parseObject(commonResV.getData(), InvoiceSuccessResV.class);
            resultV.setSerialNo(data.getDjbh());//发票流水号
            //转换为时间戳
            resultV.setInvoiceTime(str2TimeStamp(data.getKprq()));//开票时间
            resultV.setInvoiceCode(data.getFpdm());//发票代码
            resultV.setInvoiceNo(data.getQdfphm());//发票号码(全电和增值税电子发票取值不一致)
            resultV.setPdfUrl(data.getPdf());//下载链接
            resultV.setOrderAmount(data.getHjje().toString());//含税金额
        }
        invoiceStatusTransferToNuonuoStatus(commonResV.getCode(),commonResV.getMessage(),resultV);
        resultV.setFailCause(commonResV.getRenson());//结果描述
        result.add(resultV);
        return result;
    }


    private void invoiceStatusTransferToNuonuoStatus(String code, String message, QueryInvoiceResultV resultV) {
        if (ReturnCodeEnum.开票成功.getCode().equals(code)) {
            // 开具成功
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票完成.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票完成.getDes());
        } else if (ReturnCodeEnum.开票中.getCode().equals(code)) {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票中.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票中.getDes());
        } else {
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票失败.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票失败.getDes());
        }
        if("发票开具成功，正在获取版式文件！".equals(message)){
            resultV.setStatus(NuonuoInvoiceStatusEnum.开票中.getCode().toString());
            resultV.setStatusMsg(NuonuoInvoiceStatusEnum.开票中.getDes());
        }
    }


    /**
     * 将方圆全电红冲确认单查询请求体转换为nuonuo响应体
     * @param commonResV
     * @return
     */
    public static List<NuonuoRedApplyQueryV> electronInvoiceRedApplyResultTransferToNuonuoResulst(CommonResV commonResV, InvoiceRedApplyE applyE) {
        List<NuonuoRedApplyQueryV> resultList = new ArrayList<>();
        if(StringUtils.isBlank(commonResV.getData())){
            LocalDateTime now = LocalDateTime.now();
            if (Objects.nonNull(applyE.getGmtCreate()) && LocalDateTimeUtil.between(applyE.getGmtCreate(), now,
                    ChronoUnit.DAYS) > 3) {
                log.info("申请单时间为{},已超过3天时间，默认为失败", LocalDateTimeUtil.format(applyE.getGmtCreate(), NORM_DATETIME_FORMATTER));
                NuonuoRedApplyQueryV resultV = new NuonuoRedApplyQueryV();
                resultV.setBillStatus("16");
                resultList.add(resultV);
            } else if (StringUtils.equals("确认单状态不存在", commonResV.getMessage()) && StringUtils.equals("-1", commonResV.getCode())) {
                // 百旺示例报错 {"message":"确认单状态不存在","renson":"确认单状态不存在","code":-1,"data":null,"code1":9999,"renson1":"系统异常"}
                // 百旺回复说是可能单据被删了，当做失败
                log.info("确认单状态不存在，默认失败");
                NuonuoRedApplyQueryV resultV = new NuonuoRedApplyQueryV();
                resultV.setBillStatus("16");
                resultV.setErrMsg("确认单状态不存在");
                resultList.add(resultV);
            }else {
                NuonuoRedApplyQueryV resultV = new NuonuoRedApplyQueryV();
                resultV.setBillStatus(null);
                resultList.add(resultV);
            }
            return resultList;
            // throw BizException.throw400(commonResV.getMessage());
        }
        RedInvoiceSuccessResV resV = JSON.parseObject(commonResV.getData(), RedInvoiceSuccessResV.class);
        NuonuoRedApplyQueryV resultV = new NuonuoRedApplyQueryV();
        resultV.setBillStatus(resV.getHzqrxxztDm());
        resultV.setOpenStatus(resV.getYkjhzfpbz());
        resultV.setBillNo(resV.getDjbh());
        resultV.setBillUuid(resV.getUuid());
        resultList.add(resultV);
        return resultList;
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
            log.error("str2TimeStamp fail", e);
            return null;
        }
    }

    /**
     * 计费方式与发票单位对应参数映射，计费方式参考枚举 BillMethodEnum
     */
    private static final Map<String, String> billMethodUnitMap =
            Map.of("5","度",
                    "6", "立方米（方）",
                    "7", "吨");


}
