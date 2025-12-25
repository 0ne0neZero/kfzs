package com.wishare.finance.domains.invoicereceipt.facade;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillDetailV;
import com.wishare.finance.apps.model.invoice.invoice.fo.ProvinceCityF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.*;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceZoningRepository;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.ElectronInvoiceRedApplyF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.ElectronInvoiceRedQueryF;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.starter.exception.BizException;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName InvoiceExternalAbService
 * @description:
 * @author: dp
 * @create: 2024-01-02 15:18
 * @Version 1.0
 **/
@Log4j
public abstract class InvoiceExternalAbService implements InvoiceExternalService{

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;
    @Setter(onMethod_ = {@Autowired})
    private  InvoiceZoningRepository invoiceZoningRepository;





    /**
     * 开票重试接口（暂不需要）
     * @param tenantId
     * @param serialNum
     * @return
     */
    @Override
    public String reInvoice(String tenantId, String serialNum){
        return null;
    }

    /**
     * 开票之前添加销方名称
     * @param invoiceBlueA
     * @return
     */
    @Override
    public InvoiceBlueA preInvoice(InvoiceBlueA invoiceBlueA) {
        return null;
    }

    /**
     * 申请增值税电子发票开票
     * @param invoiceReceiptE
     * @param invoiceE
     * @param invoiceReceiptDetailES
     * @param nuonuoCommunityId
     * @param taxItemMapByChargeId
     * @param tenantId
     * @param invoiceTypeEnum
     * @return 发票流水号
     */
    @Override
    public String nuonuoBillingNew(InvoiceReceiptE invoiceReceiptE, InvoiceE invoiceE,
                                   List<InvoiceReceiptDetailE> invoiceReceiptDetailES, List<InvoiceDetailDto> invoiceDetailDtoList,
                                   String nuonuoCommunityId,
                                   Map<Long, List<TaxChargeItemD>> taxItemMapByChargeId, String tenantId,
                                   InvoiceTypeEnum invoiceTypeEnum){
        return null;
    }
    /**
     * 组装红字确认单申请入参
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
     * 发票结果查询接口
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @return
     */
    @Override
    public List<QueryInvoiceResultV> queryInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo){
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
     * 发票结果查询接口
     *
     * @param tenantId
     * @param serialNum
     * @param taxnum
     * @return
     */
    public List<QueryInvoiceResultV> queryPaperInvoiceResult(String tenantId, String serialNum, String taxnum, Long orderNo) {
        return null;
    }

    public InvoiceBuildingServiceInfoF handleBuildingServiceInfo(List<Long> chargeItemIds, List<BillDetailMoreV> billDetailMoreVList){
        return null;
    }

    /**
     * 经营租赁租赁费 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     */
    @Override
    public void checkRealPropertyRentInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                                          List<Long> chargeItemIds, InvoiceReceiptE invoiceReceiptE){
    }

    /**
     * 建筑服务 校验并处理
     * @param billDetailMoreVList
     * @param invoiceE
     * @param chargeItemIds
     */
    @Override
    public void checkBuildingServiceInfo(List<BillDetailMoreV> billDetailMoreVList, InvoiceE invoiceE,
                                         List<Long> chargeItemIds, InvoiceReceiptE invoiceReceiptE, InvoiceBuildingServiceInfoF serviceInfoF){
    }


    /**
     * 获取不动产地址
     * @param communityInfo
     * @param stringBuilder
     * @return
     */
    @NotNull
    public String getAddress(SpaceCommunityV communityInfo, String  stringBuilder) {
        String replace;
        if (communityInfo.getAddress().contains(stringBuilder)){
            replace = communityInfo.getAddress().replace(stringBuilder, "");
        } else {
            replace = communityInfo.getAddress();
        }
        return replace;
    }

    /**
     * 获取省市区信息
     * @param province
     * @param communityInfo
     * @return
     */
    @NotNull
    public ProvinceCityF getStringBuilder(InvoiceZoningE province, SpaceCommunityV communityInfo) {
        ProvinceCityF provinceCityF = new ProvinceCityF();
        StringBuilder areaName = new StringBuilder();
        areaName.append(province.getAreaName());
        ArrayList<String> code = new ArrayList<>(3);
        code.add(province.getAreaCode());
        List<InvoiceZoningE> invoiceZoningBySuperiorCode = invoiceZoningRepository.getInvoiceZoningBySuperiorCode(province.getAreaCode());
        // 根据父id,获取列表
        for (InvoiceZoningE invoiceZoningE : invoiceZoningBySuperiorCode) {
            if (communityInfo.getCity().equals(invoiceZoningE.getAreaName()) || communityInfo.getArea().equals(invoiceZoningE.getAreaName())) {
                // 判断type 级别 如果是3  直接就是区级别 直接拼接  例如直辖市
                if (invoiceZoningE.getAreaLevel().equals("3")) {
                    areaName.append(invoiceZoningE.getAreaName());
                    code.add(invoiceZoningE.getAreaCode());
                } else {
                    // 如果是2  则获取子列表
                    areaName.append(invoiceZoningE.getAreaName());
                    code.add(invoiceZoningE.getAreaCode());
                    List<InvoiceZoningE> zoningBySuperiorCode = invoiceZoningRepository.getInvoiceZoningBySuperiorCode(invoiceZoningE.getAreaCode());
                    // 如果查询不到 则该级别没有下一层级  不需要进行其他操作
                    // 否则就循环 获取 对应的区 去匹配
                    if (!zoningBySuperiorCode.isEmpty()){
                        for (InvoiceZoningE zoningE : zoningBySuperiorCode) {
                            if (zoningE.getAreaName().equals(communityInfo.getArea())){
                                code.add(zoningE.getAreaCode());
                                areaName.append(zoningE.getAreaName());
                            }
                        }
                    }
                }
                break;
            }

        }
        provinceCityF.setFullName(areaName.toString());
        provinceCityF.setProvinceCityCode(code);
        return provinceCityF;
    }

    /**
     * 根据空间详情获取项目信息
     * @param details
     * @return
     */
    @NotNull
    public SpaceCommunityV getSpaceCommunityV(List<SpaceDetails> details) {
        SpaceDetails spaceDetails = details.get(0);
        SpaceCommunityV communityInfo = spaceClient.getCommunityDetail(spaceDetails.getCommunityId());
        log.info("不动产发票开具查询空间信息:" + JSON.toJSONString(communityInfo));
        if(org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getProvince()) && org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getCity())) {
            throw BizException.throw400("开票失败，开票项目包含特定约束信息，请先在园区档案中完善地址信息后再进行开票!");
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(communityInfo.getAddress())){
            throw BizException.throw400("开票失败，开票项目包含特定约束信息，请先在园区档案中完善地址信息后再进行开票。");
        }
        communityInfo.setParkingProperty(spaceDetails.getParkingProperty());
        communityInfo.setEstateProperty(spaceDetails.getEstateProperty());
        return communityInfo;
    }

    /**
     * 获取空间详情列表 980116
     * @param billDetailMoreVList
     * @return
     */
    @NotNull
    public List<SpaceDetails> getSpaceDetails(List<BillDetailMoreV> billDetailMoreVList) {
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
        return details;
    }

    /**
     * 获取账单周期
     * @param billDetailMoreVList
     * @return
     */
    @NotNull
    public List<LocalDateTime> getBillDate(List<BillDetailMoreV> billDetailMoreVList) {
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
            localDateTimeList.add(entry.getValue().get(0).getStartTime());
            localDateTimeList.add(entry.getValue().get(0).getEndTime());
        }
        // 按照时间排序
        List<LocalDateTime> collect = localDateTimeList.stream().sorted().collect(Collectors.toList());
        if (!isContains(collect)){
            throw BizException.throw400("该批次账单周期不连续!");
        }
        return collect;
    }

    public  Boolean isContains(List<LocalDateTime> localDateTimeList){
        // 如果只有两个对象 则直接返回true
        if (localDateTimeList.size() == 2){
            return true;
        } else if (!localDateTimeList.isEmpty()){
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

}
