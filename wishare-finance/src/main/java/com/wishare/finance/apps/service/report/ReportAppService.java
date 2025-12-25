package com.wishare.finance.apps.service.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.report.vo.AdvanceRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeCollectionRateReportPageV;
import com.wishare.finance.apps.model.report.vo.ChargeDailyReportTotalV;
import com.wishare.finance.apps.model.report.vo.GenerateBillReportPageV;
import com.wishare.finance.domains.report.dto.ChargeDailyReportPageDto;
import com.wishare.finance.domains.report.dto.ChargeReductionReportPageDto;
import com.wishare.finance.domains.report.facade.ReportOrgFacade;
import com.wishare.finance.domains.report.facade.ReportSpaceFacade;
import com.wishare.finance.domains.report.facade.ReportUserFacade;
import com.wishare.finance.domains.report.service.ReportDomainService;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.SpacePermissionF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceTreeRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityDetailRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.utils.SearchFileUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报表应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReportAppService implements ApiBase {

    private final ReportDomainService reportDomainService;

    private final ReportUserFacade reportUserFacade;

    private final ReportOrgFacade reportOrgFacade;

    private final ReportSpaceFacade reportSpaceFacade;

    /**
     * 分页查询收费日报
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<ChargeDailyReportPageDto> chargeDailyReportPage(PageF<SearchF<?>> query) {
        //获取用户拥有的项目权限
        List<String> communityIdList = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        IPage<ChargeDailyReportPageDto> page = reportDomainService.chargeDailyReportPage(query, communityIdList);
        List<ChargeDailyReportPageDto> records = page.getRecords();
        for (ChargeDailyReportPageDto record : records) {
            String buildingName = getBuildingName(record.getRoomName());
            record.setBuildingName(buildingName);
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    /**
     * 分页查询收费减免统计表
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<ChargeReductionReportPageDto> chargeReductionReportPage(PageF<SearchF<?>> query) {
        //获取用户拥有的项目权限
        List<String> communityIdList = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        IPage<ChargeReductionReportPageDto> page = reportDomainService.chargeReductionReportPage(query, communityIdList);
        List<ChargeReductionReportPageDto> records = page.getRecords();
        for (ChargeReductionReportPageDto record : records) {
            String buildingName = getBuildingName(record.getRoomName());
            record.setBuildingName(buildingName);
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    /**
     * 收费日报收费类型合计
     *
     * @param query 查询参数
     * @return ChargeDailyReportTotalV
     */
    public ChargeDailyReportTotalV chargeDailyReportTotal(PageF<SearchF<?>> query) {
        //获取用户拥有的项目权限
        List<String> communityIdList = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        return reportDomainService.chargeDailyReportTotal(query, communityIdList, getTenantId().get());
    }

    /**
     * 分页查询收缴率
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<ChargeCollectionRateReportPageV> chargeCollectionRateReportPage(PageF<SearchF<?>> query) {
        //获取用户拥有的项目权限
        List<String> communityIdList = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        PageV<ChargeCollectionRateReportPageV> page = reportDomainService.chargeCollectionRateReportPage(query, communityIdList);
        List<ChargeCollectionRateReportPageV> records = page.getRecords();
        //获取组织信息
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
        return page;
    }

    /**
     * 获取苑幢信息
     *
     * @param roomName 房号名称
     * @return String
     */
    private String getBuildingName(String roomName) {
        if (Objects.isNull(roomName)){
            return "";
        }
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

    /**
     * 分页查询账单生成报表
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<GenerateBillReportPageV> generateBillReportPage(PageF<SearchF<?>> query) {
        return null;
    }

    /**
     * 分页查询预收率统计表报表
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<AdvanceRateReportPageV> advanceRateReportPage(PageF<SearchF<?>> query) {
        List<String> communityIdList = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        PageV<AdvanceRateReportPageV> page = reportDomainService.advanceRateReportPage(query, communityIdList);
        List<AdvanceRateReportPageV> records = page.getRecords();
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
        return page;
    }

    /**
     * 导出_账单生成查询报表
     *
     * @param form
     * @param startDate
     * @param endDate
     * @return
     */
    public Boolean exportGenerateBillReport(PageF<SearchF<?>> form, HttpServletResponse response, LocalDate startDate, LocalDate endDate) {
        //获取传入的项目名称、房号名称
        String communityName = SearchFileUtil.getCommunityNameParam(form);
        String roomName = SearchFileUtil.getRoomNameParam(form);
        //获取用户拥有的项目权限
        List<String> communityIds = reportUserFacade.listDataPermissions(getUserId().get(), "1");

        List<CommunityDetailRv> communityDetails = getCommunityDetails(communityName,communityIds);
        List<SpaceDetails> roomDetailList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(communityDetails)){
            roomDetailList = getRoomDetailList(communityDetails.stream().map(CommunityDetailRv::getId).collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(roomDetailList) && StringUtils.isNotEmpty(roomName)){
                roomDetailList = roomDetailList.stream().filter( s -> s.getName().contains(roomName)).collect(Collectors.toList());
            }
        }
        return reportDomainService.exportGenerateBillReport(form, response, communityDetails,startDate,endDate,roomDetailList);
    }

    /**
     * 获取房号信息
     */
    private List<SpaceDetails> getRoomDetailList(List<String> communityIds) {
        //获取用户拥有权限的房号信息
        SpacePermissionF spacePermissionF = new SpacePermissionF();
        spacePermissionF.setAllReturn(true);
        spacePermissionF.setSpaceTypeClassify(List.of("1"));
        spacePermissionF.setSpaceIds(communityIds);
        List<String> roomIdListStr = reportSpaceFacade.getRoomIdList(spacePermissionF);
        List<Long> roomIdList = roomIdListStr.stream().map(Long::valueOf).collect(Collectors.toList());
        return reportSpaceFacade.getDetails(roomIdList);
    }

    /**
     * 根据项目名称获取项目信息
     *
     * @param communityName
     * @return
     */
    private List<CommunityDetailRv> getCommunityDetails(String communityName,List<String> communityIds) {
        if (StringUtils.isBlank(communityName)) {
            //获取默认
            if (CollectionUtils.isNotEmpty(communityIds)) {
                return reportSpaceFacade.getCommunityNameByIds(Lists.newArrayList(communityIds.get(0)));
            }
        }else {
            //根据传参匹配
            List<CommunityDetailRv> list = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(communityIds)) {
                List<CommunityDetailRv> communityNameByIds = reportSpaceFacade.getCommunityNameByIds(communityIds);
                list = communityNameByIds.stream().filter(s -> s.getName().contains(communityName)).collect(Collectors.toList());
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 导出收费日报报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportChargeDailyReport(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        //获取用户拥有的项目权限
        List<String> communityIds = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        reportDomainService.exportChargeDailyReport(queryF,response,communityIds);
    }

    /**
     * 导出收费减免统计报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportChargeReductionReport(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        //获取用户拥有的项目权限
        List<String> communityIds = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        reportDomainService.exportChargeReductionReport(queryF,response,communityIds);
    }

    /**
     * 导出收缴率报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportChargeCollectionRateReport(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        //获取用户拥有的项目权限
        List<String> communityIds = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        reportDomainService.exportChargeCollectionRateReport(queryF,response,communityIds);
    }

    /**
     * 导出预收率统计表报表
     *
     * @param queryF 查询参数
     * @param response response
     */
    public void exportAdvanceRateReport(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        //获取用户拥有的项目权限
        List<String> communityIds = reportUserFacade.listDataPermissions(getUserId().get(), "1");
        reportDomainService.exportAdvanceRateReport(queryF,response,communityIds);
    }
}
