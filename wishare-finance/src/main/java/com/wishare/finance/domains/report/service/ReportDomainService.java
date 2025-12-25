package com.wishare.finance.domains.report.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.report.vo.AdvanceRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeCollectionRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeDailyReportTotalV;
import com.wishare.finance.apps.model.report.vo.GenerateBillReportPageV;
import com.wishare.finance.domains.report.dto.AdvanceRateReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeClosingDateDto;
import com.wishare.finance.domains.report.dto.ChargeCollectionRateReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeDailyReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeDailyReportTotalDto;
import com.wishare.finance.domains.report.dto.ChargeGenerateDto;
import com.wishare.finance.domains.report.dto.ChargeLastYearArrearsReceiveDto;
import com.wishare.finance.domains.report.dto.ChargeLastYearArrearsSettleDto;
import com.wishare.finance.domains.report.dto.ChargeReductionReportPageDto;
import com.wishare.finance.domains.report.facade.ReportOrgFacade;
import com.wishare.finance.domains.report.facade.ReportSpaceFacade;
import com.wishare.finance.domains.report.repository.mapper.ReportMapper;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportAdvanceCollectionRateReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeCollectionRateReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeDailyReportData;
import com.wishare.finance.infrastructure.easyexcel.ExportChargeReductionReportData;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceTreeRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityDetailRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.SearchFileUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报表领域层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReportDomainService {

    private final ReportMapper reportMapper;

    private final ReportOrgFacade reportOrgFacade;

    private final ReportSpaceFacade reportSpaceFacade;

    /**
     * 分页查询收费日报
     *
     * @param query           查询参数
     * @param communityIdList 项目id
     * @return PageV
     */
    public IPage<ChargeDailyReportPageDto> chargeDailyReportPage(PageF<SearchF<?>> query, List<String> communityIdList) {
        SearchF<?> conditions = query.getConditions();
        List<Field> fields = conditions.getFields();
        List<Field> chargeDateCollect = fields.stream().filter(s -> "chargeDate".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(chargeDateCollect);
        LocalDate chargeDate = LocalDate.now();
        if (CollectionUtils.isNotEmpty(chargeDateCollect)) {
            chargeDate = LocalDate.parse(chargeDateCollect.get(0).getValue().toString());
        }
        QueryWrapper<?> queryWrapper = conditions.getQueryModel();
        queryWrapper.in(CollectionUtils.isNotEmpty(communityIdList), "rb.sup_cp_unit_id", communityIdList);
        return reportMapper.chargeDailyReportPage(Page.of(query.getPageNum(), query.getPageSize()), queryWrapper, chargeDate);
    }

    /**
     * 分页查询收费减免统计表
     *
     * @param query           查询参数
     * @param communityIdList 项目id
     * @return PageV
     */
    public IPage<ChargeReductionReportPageDto> chargeReductionReportPage(PageF<SearchF<?>> query, List<String> communityIdList) {
        LocalDate now = LocalDate.now();
        LocalDate reductionEndDate = null;
        LocalDate reductionStartDate = null;
        SearchF<?> conditions = query.getConditions();
        List<Field> fields = conditions.getFields();
        List<Field> reductionStartDateCollect = fields.stream().filter(s -> "b.start_time".equals(s.getName())).collect(Collectors.toList());
        //周期为空给默认周期
        if (CollectionUtils.isEmpty(reductionStartDateCollect)) {
            reductionStartDate = LocalDate.of(now.getYear(), LocalDate.MIN.getMonth(), LocalDate.MIN.getDayOfMonth());
            reductionEndDate = LocalDate.of(now.getYear(), LocalDate.MAX.getMonth(), LocalDate.MAX.getDayOfMonth());
        }
        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel();
        queryWrapper.in(CollectionUtils.isNotEmpty(communityIdList), "b.sup_cp_unit_id", communityIdList);
        return reportMapper.chargeReductionReportPage(Page.of(query.getPageNum(), query.getPageSize()), queryWrapper, reductionEndDate, reductionStartDate);
    }

    /**
     * 收费日报统计收费项目
     *
     * @param query           查询参数
     * @param communityIdList 项目id
     * @param tenantId        租户id
     * @return ChargeDailyReportTotalV
     */
    public ChargeDailyReportTotalV chargeDailyReportTotal(PageF<SearchF<?>> query, List<String> communityIdList, String tenantId) {
        SearchF<?> conditions = query.getConditions();
        List<Field> fields = conditions.getFields();
        List<Field> chargeDateCollect = fields.stream().filter(s -> "chargeDate".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(chargeDateCollect);
        LocalDate chargeDate = LocalDate.now();
        if (CollectionUtils.isNotEmpty(chargeDateCollect)) {
            chargeDate = LocalDate.parse(chargeDateCollect.get(0).getValue().toString());
        }
        ChargeDailyReportTotalV result = new ChargeDailyReportTotalV();
        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel();
        queryWrapper.in(CollectionUtils.isNotEmpty(communityIdList), "rb.sup_cp_unit_id", communityIdList);
        queryWrapper.eq("rb.tenant_id", tenantId);
        List<ChargeDailyReportTotalDto> chargeItemTotalList = reportMapper.chargeDailyReportChargeItemTotal(queryWrapper, chargeDate);
        List<ChargeDailyReportTotalDto> payChannelTotalList = reportMapper.chargeDailyReportPayChannelTotal(queryWrapper, chargeDate);
        result.setChargeItemTotalList(chargeItemTotalList);
        result.setPaymentTotalList(payChannelTotalList);
        return result;
    }

    /**
     * 分页查询收缴率
     *
     * @param query           查询参数
     * @param communityIdList 项目id
     * @return IPage
     */
    public PageV<ChargeCollectionRateReportPageV> chargeCollectionRateReportPage(PageF<SearchF<?>> query, List<String> communityIdList) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = null;
        LocalDate startDate = null;
        LocalDate queryDate = now;
        SearchF<?> conditions = query.getConditions();
        List<Field> fields = conditions.getFields();
        List<Field> startDateCollect = fields.stream().filter(s -> "b.start_time".equals(s.getName())).collect(Collectors.toList());
        List<Field> queryDateCollect = fields.stream().filter(s -> "queryDate".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(queryDateCollect);
        //周期为空设置默认周期
        if (CollectionUtils.isEmpty(startDateCollect)) {
            startDate = LocalDate.of(now.getYear(), LocalDate.MIN.getMonth(), LocalDate.MIN.getDayOfMonth());
            endDate = LocalDate.of(now.getYear(), LocalDate.MAX.getMonth(), LocalDate.MAX.getDayOfMonth());
        }
        if (CollectionUtils.isNotEmpty(queryDateCollect)) {
            queryDate = LocalDate.parse(queryDateCollect.get(0).getValue().toString());
        }

        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel();
        queryWrapper.in(CollectionUtils.isNotEmpty(communityIdList), "b.sup_cp_unit_id", communityIdList);
        //根据房号费项分组获取周期内的数据
        IPage<ChargeCollectionRateReportPageDto> page = reportMapper.chargeCollectionRateReportPage(Page.of(query.getPageNum(), query.getPageSize()), queryWrapper, startDate, endDate);
        List<ChargeCollectionRateReportPageDto> records = page.getRecords();
        List<Long> roomIdList = records.stream().map(ChargeCollectionRateReportPageDto::getRoomId).collect(Collectors.toList());
        List<Long> chargeItemIdList = records.stream().map(ChargeCollectionRateReportPageDto::getChargeItemId).collect(Collectors.toList());

        List<ChargeCollectionRateReportPageV> collectionRateList = new ArrayList<>();
        for (ChargeCollectionRateReportPageDto record : records) {
            ChargeCollectionRateReportPageV rateReportV = Global.mapperFacade.map(record, ChargeCollectionRateReportPageV.class);
            rateReportV.setInvoiceTypeList(StringUtils.isNotEmpty( record.getInvoiceType() ) ? Arrays.asList(record.getInvoiceType().split(",")) : new ArrayList<>());
            if(rateReportV.getTotalAmount() - rateReportV.getDeductibleAmount() > 0){
                rateReportV.setCollectionRate(BigDecimal.valueOf((double)rateReportV.getActualPayAmount() / ((double)rateReportV.getTotalAmount() - (double)rateReportV.getDeductibleAmount())).setScale(4, RoundingMode.DOWN));
            }else{
                rateReportV.setCollectionRate(BigDecimal.ZERO);
            }

            collectionRateList.add(rateReportV);
        }
        //获取对应房号和费项的收款截止日期
        List<ChargeClosingDateDto> chargeClosingDateDtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(roomIdList) && CollectionUtils.isNotEmpty(chargeItemIdList)){
            chargeClosingDateDtoList = reportMapper.queryChargeClosingDate(roomIdList,chargeItemIdList);
        }
        Map<String, List<ChargeClosingDateDto>> chargeTimeCollect = chargeClosingDateDtoList.stream().collect(Collectors.groupingBy(ChargeClosingDateDto::getRoomIdAndChargeItemId));
        for (ChargeCollectionRateReportPageV record : collectionRateList) {
            List<ChargeClosingDateDto> list = chargeTimeCollect.get(record.getRoomId().toString() + record.getChargeItemId().toString());
            if (CollectionUtils.isNotEmpty(list)) {
                record.setChargeTime(list.get(0).getChargeTime());
            }
        }

        //获取对应房号和费项的上一年未缴账单信息
//        LocalDate lastYearStartDate = LocalDate.of(startDate.getYear() - 1, LocalDate.MIN.getMonth(), LocalDate.MIN.getDayOfMonth());
//        LocalDate lastYearEndDate = LocalDate.of(startDate.getYear() - 1, LocalDate.MIN.getMonth(), LocalDate.MIN.getDayOfMonth());
//        List<ChargeLastYearArrearsReceiveDto> chargeLastYearArrearsReceiveDtoList = reportMapper.queryLastYearArrearsReceiveInfo(roomIdList, chargeItemIdList, lastYearStartDate, lastYearEndDate);
        List<ChargeLastYearArrearsReceiveDto> chargeLastYearArrearsReceiveDtoList = new ArrayList<>();
        Map<String, List<ChargeLastYearArrearsReceiveDto>> arrearsReceiveCollect = chargeLastYearArrearsReceiveDtoList.stream().collect(Collectors.groupingBy(ChargeLastYearArrearsReceiveDto::getRoomIdAndChargeItemId));
        //设置前期欠收应收金额
        for (ChargeCollectionRateReportPageV record : collectionRateList) {
            List<ChargeLastYearArrearsReceiveDto> list = arrearsReceiveCollect.get(record.getRoomId().toString() + record.getChargeItemId().toString());
            if (CollectionUtils.isNotEmpty(list)) {
                record.setEarlyReceivableAmount(list.get(0).getEarlyReceivableAmount());
                record.setEarlyDeductibleAmount(list.get(0).getEarlyDeductibleAmount());
            }else{
                record.setEarlyReceivableAmount(0L);
                record.setEarlyDeductibleAmount(0L);
            }
        }

        //获取对应房号和费项的上一年结束到查询时间的实收金额
        LocalDate currentYearStartDate = LocalDate.of(now.getYear(), LocalDate.MIN.getMonth(), LocalDate.MIN.getDayOfMonth());
//        List<ChargeLastYearArrearsSettleDto> chargeLastYearArrearsSettleDtoList = reportMapper.queryLastYearArrearsSettleInfo(roomIdList, chargeItemIdList, currentYearStartDate, queryDate);
        List<ChargeLastYearArrearsSettleDto> chargeLastYearArrearsSettleDtoList = new ArrayList<>();
        Map<String, List<ChargeLastYearArrearsSettleDto>> arrearsSettleCollect = chargeLastYearArrearsSettleDtoList.stream().collect(Collectors.groupingBy(ChargeLastYearArrearsSettleDto::getRoomIdAndChargeItemId));
        //设置前期欠收实收金额
        for (ChargeCollectionRateReportPageV record : collectionRateList) {
            List<ChargeLastYearArrearsSettleDto> list = arrearsSettleCollect.get(record.getRoomId().toString() + record.getChargeItemId().toString());
            if (CollectionUtils.isNotEmpty(list)) {
                record.setEarlyActualPayAmount(list.get(0).getEarlyActualPayAmount());
                record.setEarlyDiscountAmount(list.get(0).getEarlyDiscountAmount());
            }else{
                record.setEarlyActualPayAmount(0L);
                record.setEarlyDiscountAmount(0L);
            }
            record.setEarlyActualUnPayAmount(record.getEarlyReceivableAmount() - record.getEarlyDeductibleAmount() - record.getEarlyActualPayAmount());
            record.setSumReceivableAmount(record.getTotalAmount() + record.getEarlyReceivableAmount() - record.getDeductibleAmount() - record.getEarlyDeductibleAmount());
            record.setSumActualPayAmount(record.getActualPayAmount() + record.getEarlyActualPayAmount());
            if(((double)record.getEarlyReceivableAmount() - (double)record.getEarlyDeductibleAmount()) > 0){
                record.setEarlyCollectionRate(BigDecimal.valueOf((double)record.getEarlyActualPayAmount() / ((double)record.getEarlyReceivableAmount() - (double)record.getEarlyDeductibleAmount())).setScale(4, RoundingMode.DOWN));
            }else{
                record.setEarlyCollectionRate(BigDecimal.ZERO);
            }
            if(record.getSumReceivableAmount() > 0){
                record.setSumCollectionRate(BigDecimal.valueOf((double)record.getSumActualPayAmount() / (double)record.getSumReceivableAmount()).setScale(4, RoundingMode.DOWN));
            }else{
                record.setSumCollectionRate(BigDecimal.ZERO);
            }
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), collectionRateList);
    }

    /**
     * 导出_账单生成查询报表
     *
     * @param form
     * @param response
     * @param communityDetailRvs
     * @param startDate
     * @param endDate
     * @param roomDetailList
     * @return 项目下的所有房号（辉神的项目下所有房号）
     * 楼栋名称（输入框）；房屋名称（查询输入框）；业主名称（查询输入框）；
     */
    public Boolean exportGenerateBillReport(PageF<SearchF<?>> form, HttpServletResponse response, List<CommunityDetailRv> communityDetailRvs,
                                            LocalDate startDate, LocalDate endDate, List<SpaceDetails> roomDetailList) {
        //获取应该生成的所有日期
        List<LocalDate> startEndDateAll = getStartEndDateAll(startDate,endDate);
        //获取费项名称
        String chargeItemName = SearchFileUtil.getChargeItemNameParam(form);
        chargeItemName = StringUtils.isNotEmpty(chargeItemName) ? chargeItemName : "物业管理费";

        List<String> communityIds = communityDetailRvs.stream().map(CommunityDetailRv::getId).collect(Collectors.toList());
        List<Long> roomIds = roomDetailList.stream().map(SpaceDetails::getId).collect(Collectors.toList());
        List<GenerateBillReportPageV> reportPageVS = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(roomIds) && CollectionUtils.isNotEmpty(communityIds)){
            List<ChargeGenerateDto> chargeGenerateDtos = reportMapper.exportGenerateBillReportList(communityIds,roomIds, chargeItemName, startDate, endDate);
            if (CollectionUtils.isEmpty(chargeGenerateDtos)) {
                //如果为空,根据必填返回全量未生成
                for (CommunityDetailRv communityDetailRv : communityDetailRvs) {
                    String communityName = communityDetailRv.getName();
                    reportPageVS.addAll(getAllReportData(startEndDateAll,chargeItemName,communityName,roomDetailList));
                }
            }else {
                reportPageVS.addAll(getReportData(startEndDateAll, chargeGenerateDtos,roomDetailList));
                for (CommunityDetailRv communityDetailRv : communityDetailRvs) {
                    String communityName = communityDetailRv.getName();
                    reportPageVS.addAll(getAllReportData(startEndDateAll,chargeItemName,communityName,roomDetailList));
                }
            }
        }
        //执行导出
        String fileName = "账单生成查询报表导出" + UidHelper.nextIdByDTStr("GenerateBillReport");
        try {
            ExcelUtil.export(fileName, reportPageVS, GenerateBillReportPageV.class, response);
            return true;
        } catch (Exception e) {
            throw BizException.throw400("数据导出失败：" + e.getMessage());
        }
    }

    /**
     * 返回全量未生成
     *
     * @param startEndDateAll
     * @param communityName
     * @param roomDetailList
     * @return
     */
    private List<GenerateBillReportPageV> getAllReportData(List<LocalDate> startEndDateAll, String chargeItemName, String communityName, List<SpaceDetails> roomDetailList) {
        List<GenerateBillReportPageV> list = Lists.newArrayList();
        roomDetailList.forEach(roomDetail->{
            List<GenerateBillReportPageV> reportPageVS = returnUnGeneralBill(communityName, chargeItemName, roomDetail.getStandardSpaceName(), startEndDateAll);
            list.addAll(reportPageVS);
        });
        return list;
    }

    /**
     * 根据已经生成的反处理未生成的
     *
     * @param startEndDateAll 应该生成账单的时间段
     * @param chargeGenerateDtos 已经生成的应收账单
     * @return
     */
    private List<GenerateBillReportPageV> getReportData(List<LocalDate> startEndDateAll, List<ChargeGenerateDto> chargeGenerateDtos,List<SpaceDetails> roomDetailList) {
        Map<Long, List<SpaceDetails>> roomCollect = roomDetailList.stream().collect(Collectors.groupingBy(SpaceDetails::getId));
        List<GenerateBillReportPageV> reportPageVS = Lists.newArrayList();
        for (ChargeGenerateDto chargeGenerateDto : chargeGenerateDtos) {
            //获取已经生成账单的年月
            if (StringUtils.isNotBlank(chargeGenerateDto.getStartTime())) {
                List<String> strings = Arrays.asList(chargeGenerateDto.getStartTime().split(","));
                List<LocalDate> startDates = DateTimeUtil.handleMonthBetweenDateYMD(strings);
                startDates = handeLocaldateMonthFirstDay(startDates);
                List<LocalDate> newAllDate = Lists.newArrayList(startEndDateAll);
                //根据需要生成的日期移除已生成的日期得到未生成的日期
                newAllDate.removeAll(startDates);
                List<GenerateBillReportPageV> reportPageVS1 = returnUnGeneralBill(chargeGenerateDto.getCommunityName(), chargeGenerateDto.getChargeItemName(), chargeGenerateDto.getRoomName(), newAllDate);
                if (CollectionUtils.isNotEmpty(reportPageVS1)) {
                    reportPageVS.addAll(reportPageVS1);
                }

                //移除已生成账单的房号
                if(CollectionUtils.isNotEmpty(roomCollect.get(chargeGenerateDto.getRoomId()))){
                    roomDetailList.removeAll(roomCollect.get(chargeGenerateDto.getRoomId()));
                }
            }
        }
        return reportPageVS;
    }

    /**
     * 转换当前月的第一天
     *
     * @param localDateList
     * @return
     */
    private List<LocalDate> handeLocaldateMonthFirstDay(List<LocalDate> localDateList) {
        List<LocalDate> list = Lists.newArrayList();
        localDateList.forEach(localDate -> {
            LocalDate firstDayOfMonty = localDate.with(TemporalAdjusters.firstDayOfMonth());
            list.add(firstDayOfMonty);
        });
        return list;
    }

    /**
     * 根据开始日期和结束日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private List<LocalDate> getStartEndDateAll(LocalDate startDate, LocalDate endDate) {
        List<String> monthBetweenDate = DateTimeUtil.getMonthBetweenDate(startDate, endDate);
        return DateTimeUtil.handleMonthBetweenDateYM(monthBetweenDate);
    }


    /**
     * 构建导出对象
     *
     * @param chargeItemName
     * @param roomName
     * @param startDates
     * @return
     */
    private List<GenerateBillReportPageV> returnUnGeneralBill(String coummunityName, String chargeItemName, String roomName, List<LocalDate> startDates) {
        List<GenerateBillReportPageV> reportVS = Lists.newArrayList();
        for (LocalDate startDate : startDates) {
            GenerateBillReportPageV generateBillReportPageV = new GenerateBillReportPageV();
            if (startDate != null) {
                generateBillReportPageV.setYear(String.valueOf(startDate.getYear()));
                generateBillReportPageV.setMonth(String.valueOf(startDate.getMonthValue()));
            }
            generateBillReportPageV.setCommunityName(coummunityName);
            generateBillReportPageV.setRoomName(roomName);
            generateBillReportPageV.setChargeItemName(chargeItemName);
            generateBillReportPageV.setBuildingName(getBuildingName(roomName));//楼栋名称
            generateBillReportPageV.setProjectGroup(null);//项目群
            generateBillReportPageV.setOwnerName(null);//业主名称
            generateBillReportPageV.setGeneration("未生成");
            reportVS.add(generateBillReportPageV);
        }
        return reportVS;
    }

    /**
     * 获取苑幢信息
     *
     * @param roomName 房号名称
     * @return String
     */
    private String getBuildingName(String roomName) {
        if (StringUtils.isNotBlank(roomName)) {
            String[] roomNameList = roomName.split("-");
            StringBuilder buildingName = new StringBuilder();
            for (String name : roomNameList) {
                if (name.endsWith("苑") || name.endsWith("幢") || name.endsWith("楼") || name.endsWith("栋")) {
                    if(name.startsWith(">")){
                        name = name.substring(1);
                    }
                    buildingName.append(buildingName.length() == 0 ? name : "-" + name);
                }
            }
            return buildingName.toString();
        }
        return null;
    }

    /**
     * 分页查询预收率统计表报表
     *
     * @param query 查询参数
     * @param communityIdList    项目id
     * @return IPage
     */
    public PageV<AdvanceRateReportPageV> advanceRateReportPage(PageF<SearchF<?>> query, List<String> communityIdList) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = null;
        LocalDate startDate = null;
        SearchF<?> conditions = query.getConditions();
        List<Field> fields = conditions.getFields();
        List<Field> startDateCollect = fields.stream().filter(s -> "b.start_time".equals(s.getName())).collect(Collectors.toList());
        //周期为空设置默认周期
        if(CollectionUtils.isEmpty(startDateCollect)){
            startDate = LocalDate.of(now.getYear(),LocalDate.MIN.getMonth(),LocalDate.MIN.getDayOfMonth());
            endDate = LocalDate.of(now.getYear(),LocalDate.MAX.getMonth(),LocalDate.MAX.getDayOfMonth());
        }else{
            startDate = LocalDate.parse(startDateCollect.get(0).getMap().get("start").toString());
        }

        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel();
        queryWrapper.in(CollectionUtils.isNotEmpty(communityIdList), "b.sup_cp_unit_id", communityIdList);
        //根据房号费项分组获取周期内的数据
        IPage<AdvanceRateReportPageDto> page = reportMapper.advanceRateReportPage(Page.of(query.getPageNum(), query.getPageSize()), queryWrapper, startDate, endDate);
        List<AdvanceRateReportPageDto> records = page.getRecords();
        List<Long> roomIdList = records.stream().map(AdvanceRateReportPageDto::getRoomId).collect(Collectors.toList());
        List<Long> chargeItemIdList = records.stream().map(AdvanceRateReportPageDto::getChargeItemId).collect(Collectors.toList());

        List<AdvanceRateReportPageV> collectionRateList = new ArrayList<>();
        for (AdvanceRateReportPageDto record : records) {
            AdvanceRateReportPageV rateReportV = Global.mapperFacade.map(record, AdvanceRateReportPageV.class);
            rateReportV.setInvoiceTypeList(StringUtils.isNotEmpty( record.getInvoiceType() ) ? Arrays.asList(record.getInvoiceType().split(",")) : new ArrayList<>());
            collectionRateList.add(rateReportV);
        }

        //获取对应房号和费项的收款截止日期
        List<ChargeClosingDateDto> chargeClosingDateDtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(roomIdList) && CollectionUtils.isNotEmpty(chargeItemIdList)){
            chargeClosingDateDtoList = reportMapper.queryChargeClosingDate(roomIdList,chargeItemIdList);
        }
        Map<String, List<ChargeClosingDateDto>> chargeTimeCollect = chargeClosingDateDtoList.stream().collect(Collectors.groupingBy(ChargeClosingDateDto::getRoomIdAndChargeItemId));
        for (AdvanceRateReportPageV record : collectionRateList) {
            List<ChargeClosingDateDto> list = chargeTimeCollect.get(record.getRoomId().toString() + record.getChargeItemId().toString());
            if(CollectionUtils.isNotEmpty(list)){
                record.setChargeTime(list.get(0).getChargeTime());
            }
            record.setActualUnPayAmount(record.getTotalAmount() - record.getActualPayAmount());
            if(record.getTotalAmount() > 0){
                record.setAdvanceRate(BigDecimal.valueOf((double)record.getActualPayAmount() / (double)record.getTotalAmount()).setScale(4, RoundingMode.DOWN));
            }else{
                record.setAdvanceRate(BigDecimal.ZERO);
            }
        }
        return PageV.of(page.getCurrent(),page.getSize(), page.getTotal(), collectionRateList);
    }

    /**
     * 导出收费日报报表
     *  @param queryF 查询参数
     * @param response response
     * @param communityIds 项目id
     */
    public void exportChargeDailyReport(PageF<SearchF<?>> queryF, HttpServletResponse response, List<String> communityIds) {
        String fileName = "收费日报报表";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportChargeDailyReportData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportChargeDailyReportData> resultList;
            while (pageNumber <= totalPage) {
                queryF.setPageSize(pageSize);
                queryF.setPageNum(pageNumber);
                IPage<ChargeDailyReportPageDto> data = chargeDailyReportPage(queryF, communityIds);
                List<ChargeDailyReportPageDto> records = data.getRecords();
                resultList = Global.mapperFacade.mapAsList(records, ExportChargeDailyReportData.class);
                for (ExportChargeDailyReportData record : resultList) {
                    String buildingName = getBuildingName(record.getRoomName());
                    record.setBuildingName(buildingName);
                }
                if (CollectionUtils.isEmpty(resultList)) {
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet, writeTable);
                totalPage = data.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出收费减免统计报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportChargeReductionReport(PageF<SearchF<?>> queryF, HttpServletResponse response, List<String> communityIds) {
        String fileName = "收费减免统计报表";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportChargeReductionReportData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportChargeReductionReportData> resultList;
            while (pageNumber <= totalPage) {
                queryF.setPageSize(pageSize);
                queryF.setPageNum(pageNumber);
                IPage<ChargeReductionReportPageDto> data = chargeReductionReportPage(queryF, communityIds);
                resultList = Global.mapperFacade.mapAsList(data.getRecords(), ExportChargeReductionReportData.class);
                for (ExportChargeReductionReportData record : resultList) {
                    String buildingName = getBuildingName(record.getRoomName());
                    record.setBuildingName(buildingName);
                }
                if (CollectionUtils.isEmpty(resultList)) {
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet, writeTable);
                totalPage = data.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出收缴率报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportChargeCollectionRateReport(PageF<SearchF<?>> queryF, HttpServletResponse response, List<String> communityIds) {
        String fileName = "收缴率报表";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportChargeCollectionRateReportData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportChargeCollectionRateReportData> resultList;
            while (pageNumber <= totalPage) {
                queryF.setPageSize(pageSize);
                queryF.setPageNum(pageNumber);
                PageV<ChargeCollectionRateReportPageV> data = chargeCollectionRateReportPage(queryF, communityIds);
                //获取组织信息
                List<ChargeCollectionRateReportPageV> records = data.getRecords();
                List<Long> orgIdList = records.stream().map(ChargeCollectionRateReportPageV::getCostCenterId).filter(Objects::nonNull).collect(Collectors.toList());
                List<OrgFinanceTreeRv> orgList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(orgIdList)){
                    orgList = reportOrgFacade.getOrgListById(orgIdList);
                }
                Map<Long, List<OrgFinanceTreeRv>> orgCollect = orgList.stream().collect(Collectors.groupingBy(OrgFinanceTreeRv::getId));
                for (ChargeCollectionRateReportPageV record : records) {
                    String buildingName = getBuildingName(record.getRoomName());
                    record.setBuildingName(buildingName);
                    record.setCostCenterCode(CollectionUtils.isEmpty(orgCollect.get(record.getCostCenterId())) ? null : orgCollect.get(record.getCostCenterId()).get(0).getCode());
                }
                resultList = Global.mapperFacade.mapAsList(records, ExportChargeCollectionRateReportData.class);
                if (CollectionUtils.isEmpty(resultList)) {
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet, writeTable);
                totalPage = getPages(data.getPageSize(), data.getTotal());
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出预收率统计表报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportAdvanceRateReport(PageF<SearchF<?>> queryF, HttpServletResponse response, List<String> communityIds) {
        String fileName = "预收率统计表报表";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportAdvanceCollectionRateReportData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportAdvanceCollectionRateReportData> resultList;
            while (pageNumber <= totalPage) {
                queryF.setPageSize(pageSize);
                queryF.setPageNum(pageNumber);
                PageV<AdvanceRateReportPageV> data = advanceRateReportPage(queryF, communityIds);
                List<AdvanceRateReportPageV> records = data.getRecords();
                //获取组织信息
                List<Long> orgIdList = records.stream().map(AdvanceRateReportPageV::getCostCenterId).filter(Objects::nonNull).collect(Collectors.toList());
                List<OrgFinanceTreeRv> orgList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(orgIdList)) {
                    orgList = reportOrgFacade.getOrgListById(orgIdList);
                }
                //获取房屋建筑信息
                List<Long> roomIdList = records.stream().map(AdvanceRateReportPageV::getRoomId).filter(Objects::nonNull).collect(Collectors.toList());
                List<SpaceDetails> roomList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(roomIdList)) {
                    roomList = reportSpaceFacade.getDetails(roomIdList);
                }

                //设置组织代码
                Map<Long, List<OrgFinanceTreeRv>> orgCollect = orgList.stream().collect(Collectors.groupingBy(OrgFinanceTreeRv::getId));
                Map<Long, List<SpaceDetails>> roomCollect = roomList.stream().collect(Collectors.groupingBy(SpaceDetails::getId));
                for (AdvanceRateReportPageV record : records) {
                    String buildingName = getBuildingName(record.getRoomName());
                    record.setBuildingName(buildingName);
                    record.setCostCenterCode(CollectionUtils.isEmpty(orgCollect.get(record.getCostCenterId())) ? null : orgCollect.get(record.getCostCenterId()).get(0).getCode());
                    record.setBuildingArea(CollectionUtils.isEmpty(roomCollect.get(record.getRoomId())) ? null : roomCollect.get(record.getRoomId()).get(0).getBuildArea());
                    record.setChargingArea(CollectionUtils.isEmpty(roomCollect.get(record.getRoomId())) ? null : roomCollect.get(record.getRoomId()).get(0).getBillableArea());
                }
                resultList = Global.mapperFacade.mapAsList(records, ExportAdvanceCollectionRateReportData.class);
                if (CollectionUtils.isEmpty(resultList)) {
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet, writeTable);
                totalPage = getPages(data.getPageSize(), data.getTotal());
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取总页数
     *
     * @param size 每页大小
     * @param total 总数量
     * @return long
     */
    private long getPages(Long size, Long total) {
        if (size == 0) {
            return 0L;
        }
        long pages = total / size;
        if (total % size != 0) {
            pages++;
        }
        return pages;
    }
}
