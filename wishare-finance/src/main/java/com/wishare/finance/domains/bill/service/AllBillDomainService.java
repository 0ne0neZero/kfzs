package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.AllBillGroupQueryF;
import com.wishare.finance.apps.model.bill.fo.AllBillQueryF;
import com.wishare.finance.apps.model.bill.fo.BillAmountQueryF;
import com.wishare.finance.apps.model.bill.vo.AllBillPageVo;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillCarriedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillCarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleTypeEnum;
import com.wishare.finance.domains.bill.dto.AllBillGroupDto;
import com.wishare.finance.domains.bill.dto.AllBillPageDto;
import com.wishare.finance.domains.bill.dto.BillAmountDto;
import com.wishare.finance.domains.bill.dto.BillAmountExtendDto;
import com.wishare.finance.domains.bill.dto.BillGroupDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.ChargeBillGroupDto;
import com.wishare.finance.domains.bill.dto.ChargeBillGroupStatisticsDto;
import com.wishare.finance.domains.bill.dto.ChargeSettleRuleDto;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import com.wishare.finance.domains.bill.dto.FlowContractBillStatisticsDto;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.BillMapper;
import com.wishare.finance.domains.configure.organization.facade.CostCenterFacade;
import com.wishare.finance.domains.configure.organization.repository.mapper.BankAccountCostOrgMapper;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimGatherDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.ListPageUtils;
import com.wishare.finance.infrastructure.utils.MapperFacadeUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 账单领域Service
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class AllBillDomainService extends BillDomainServiceImpl<BillRepository<Bill>, Bill> {

    @Setter(onMethod_ = @Autowired)
    protected BillMapper billMapper;
    private final CostCenterFacade costCenterFacade;
    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final ReceiptAmountUtils receiptAmountUtils;
    private final FlowClaimGatherDetailRepository flowClaimGatherDetailRepository;
    private final FlowClaimRecordRepository flowClaimRecordRepository;
    private final BankAccountCostOrgMapper bankAccountCostOrgMapper;


    @Setter(onMethod_ = @Autowired)
    protected SharedBillAppService sharedBillAppService;


    /**
     * 根据收费规则分组查询账单
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<AllBillGroupDto> groupByRule(AllBillGroupQueryF queryF) {
        Boolean loadChildren = queryF.getLoadChildren();
        PageF<SearchF<?>> query = queryF.getQuery();
        List<AllBillGroupQueryF.RoomInfo> roomInfoList = queryF.getRoomInfoList();
        PageQueryUtils.validQueryContainsFieldAndValue(queryF.getQuery(), "b." + BillSharedingColumn.应收账单.getColumnName());
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel().eq("b.deleted", DataDisabledEnum.启用.getCode());
        if (CollectionUtils.isNotEmpty(roomInfoList)) {
            //租客根据房号和付款人获取账单，业主根据房号获取账单
            List<AllBillGroupQueryF.RoomInfo> tenantCondition = roomInfoList.stream().filter(s -> Objects.nonNull(s.getPayerId())).collect(Collectors.toList());
            //获取业主的房号id
            List<String> ownerRoomIdCondition = roomInfoList.stream().filter(s -> Objects.isNull(s.getPayerId())).map(a-> String.valueOf(a.getRoomId())).collect(Collectors.toList());
            //1.分页获取所有的分组
            wrapper.isNotNull("b.room_id");
            wrapper.isNotNull("b.start_time");
            wrapper.and(queryWrapper -> {
                //拼接租户账单条件
                for (AllBillGroupQueryF.RoomInfo roomInfo : tenantCondition) {
                    queryWrapper.or(tenantWrapper -> {
                        tenantWrapper.eq("b.room_id", String.valueOf(roomInfo.getRoomId())).eq("b.payer_id", roomInfo.getPayerId());
                    });
                }
                //拼接业主账单条件
                queryWrapper.or(CollectionUtils.isNotEmpty(ownerRoomIdCondition), ownerWrapper -> {
                    ownerWrapper.in("b.room_id", ownerRoomIdCondition);
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 start
        if (CollectionUtils.isNotEmpty(queryF.getBills())){
            //拼接业主账单条件
            wrapper.and(queryWrapper -> {
                queryWrapper.or(CollectionUtils.isNotEmpty(queryF.getBills()), ownerWrapper -> {
                    ownerWrapper.in("b.id", queryF.getBills());
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 end

        //装修办理缴费账单分享使用,不影响既存逻辑
        if (CollectionUtils.isNotEmpty(queryF.getDecorationBills())){
            //拼接业主账单条件
            wrapper.and(queryWrapper -> {
                queryWrapper.or(CollectionUtils.isNotEmpty(queryF.getDecorationBills()), ownerWrapper -> {
                    ownerWrapper.in("b.id", queryF.getDecorationBills());
                });
            });
        }

        List<BillGroupDto> list=new ArrayList<>();
        String receivableBillName = sharedBillAppService.getShareTableName(queryF.getQuery(),
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        Field supCpUnitIdField = query.getConditions().getFields().stream()
            .filter(v -> ("b." + BillSharedingColumn.应收账单.getColumnName()).equals(v.getName()))
            .findAny().get();
        //有合并费项的账单
        Map<String, ChargeSettleRuleDto> sortMap=queryF.getSortMap();
        if(!Objects.isNull(sortMap)){
            sortMap.forEach((z, k) -> {
                String ruleValue="a.room_id";
                if(!Optional.ofNullable(z).isEmpty()){
                    //设置分组条件
                    SettleTypeEnum typeEnum=SettleTypeEnum.valueOfByCode(k.getType());
                    switch (typeEnum) {
                        case 自然月:
                            ruleValue=ruleValue+",a.account_year,a.account_date";
                            break;
                        case 自然季度:
                            ruleValue=ruleValue+",a.account_year,a.account_quarter";
                            break;
                        case 自然半年度:
                            ruleValue=ruleValue+",a.account_year,a.account_part_year";
                            break;
                        default:
                            ruleValue=ruleValue+",a.account_year";
                            break;
                    }
                }
                Integer pageNnum=1;
                //复制查询条件
                QueryWrapper<?> newWrapper = wrapper.clone();
                List<Long> itmeIds=new ArrayList<>();
                String[] arr = k.getItemId().split(",");
                for(String id:arr){
                    itmeIds.add(Long.valueOf(id));
                }
                newWrapper.in("b.charge_item_id",itmeIds);
                newWrapper.eq("b.community_id",k.getCommunityId());
                while(true) {
                    List<BillGroupDto> billList = billMapper.billListGroup(Page.of(pageNnum, query.getPageSize()),newWrapper,ruleValue,receivableBillName);
                    if(CollectionUtils.isEmpty(billList)){
                        break;
                    }else {
                        billList.stream().forEach(e ->{e.setIsMerge(1);e.setType(k.getType());});
                    }
                    list.addAll(billList);
                    pageNnum++;
                };
            });
        }
        //没有合并费项的账单
        if(CollectionUtils.isNotEmpty(queryF.getItemIdList())) {
            List<String> notInItemIds = new ArrayList<>();
            for (String id : queryF.getItemIdList()) {
                notInItemIds.add(id);
            }
            wrapper.notIn("b.charge_item_id", notInItemIds);
        }
        String ruleValue="a.charge_item_id,a.room_id,a.account_year";
        Integer pageNnum=1;
        wrapper.eq("b.community_id",queryF.getCommunityId());
        while(true) {
            List<BillGroupDto> billList = billMapper.billListGroup(Page.of(pageNnum, query.getPageSize()),wrapper,ruleValue, receivableBillName);
            if(CollectionUtils.isEmpty(billList)){
                break;
            }else {
                billList.stream().forEach(e ->{e.setIsMerge(2);e.setType(SettleTypeEnum.自然年.getCode());});
            }
            list.addAll(billList);
            pageNnum++;
        };
        //list转page
        List<BillGroupDto> pageList= ListPageUtils.page(list,(int)queryF.getQuery().getPageNum(),(int)queryF.getQuery().getPageSize());
        List<AllBillGroupDto> billGroupPageList =new ArrayList<>();
        //查询子账单
        if (loadChildren && CollectionUtils.isNotEmpty(pageList)) {
            for (BillGroupDto record : pageList) {
                List<Long> receiveBillIdList = new ArrayList<>();
                List<Long> temporaryChargeBillIdList = new ArrayList<>();
                List<Long> advanceBillIdList = new ArrayList<>();
                String[] billIds = record.getBillIds().split(",");
                String[] billTypes = record.getBillTypes().split(",");
                for (int i = 0; i < billIds.length; i++) {
                    BillTypeEnum billType = BillTypeEnum.valueOfByCode(Integer.parseInt(billTypes[i]));
                    switch (billType) {
                        case 应收账单:
                            receiveBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        case 临时收费账单:
                            temporaryChargeBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        case 预收账单:
                            advanceBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        default:
                            break;
                    }
                }
                //查询账单明细
                ArrayList<AllBillGroupDto> allBillList = new ArrayList<>();
                ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
                if (CollectionUtils.isNotEmpty(receiveBillIdList)) {
                    List<AllBillGroupDto> receivableBillGroupDtoList = Global.mapperFacade.mapAsList(receivableBillRepository.listByIdsNotTenantId(receiveBillIdList,supCpUnitIdField.getValue().toString()), AllBillGroupDto.class);
                    receivableBillGroupDtoList.forEach(groupDto -> groupDto.setBillType(BillTypeEnum.应收账单.getCode()));
                    allBillList.addAll(receivableBillGroupDtoList);
                }

                TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(TemporaryChargeBillRepository.class);
                if (CollectionUtils.isNotEmpty(temporaryChargeBillIdList)) {
                    List<AllBillGroupDto> temporaryChargeBillGroupDtoList = Global.mapperFacade.mapAsList
                            (temporaryChargeBillRepository.listByIdsNotTenantId(temporaryChargeBillIdList,supCpUnitIdField.getValue().toString()), AllBillGroupDto.class);
                    temporaryChargeBillGroupDtoList.forEach(groupDto -> groupDto.setBillType(BillTypeEnum.临时收费账单.getCode()));
                    allBillList.addAll(temporaryChargeBillGroupDtoList);
                }
                record.setShowType(queryF.getShowType());
                record.setChildren(allBillList);
                record.setActualPayAmount(allBillList.stream().mapToLong(AllBillGroupDto::getActualPayAmount).sum());
                record.setActualUnpayAmount(allBillList.stream().mapToLong(AllBillGroupDto::getActualUnpayAmount).sum());
            }
            if(!CollectionUtils.isEmpty(pageList)){
                billGroupPageList = Global.mapperFacade.mapAsList(pageList, AllBillGroupDto.class);
            }
        }
        return PageV.of(queryF.getQuery().getPageNum(), queryF.getQuery().getPageSize(), list.size(), billGroupPageList);
    }




    /**
     * 分组查询账单
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<AllBillGroupDto> queryBillByGroup(AllBillGroupQueryF queryF) {
        Boolean loadChildren = queryF.getLoadChildren();
        PageF<SearchF<?>> query = queryF.getQuery();
        List<AllBillGroupQueryF.RoomInfo> roomInfoList = queryF.getRoomInfoList();
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDisabledEnum.启用.getCode());
        if (CollectionUtils.isNotEmpty(roomInfoList)) {
            //租客根据房号和付款人获取账单，业主根据房号获取账单
            List<AllBillGroupQueryF.RoomInfo> tenantCondition = roomInfoList.stream()
                .filter(s -> Objects.nonNull(s.getPayerId())).collect(Collectors.toList());
            //获取业主的房号id
            List<String> ownerRoomIdCondition = roomInfoList.stream()
                .filter(s -> Objects.isNull(s.getPayerId())).map(a -> String.valueOf(a.getRoomId()))
                .collect(Collectors.toList());
            //1.分页获取所有的分组
            wrapper.isNotNull("b.room_id");
            wrapper.isNotNull("b.start_time");
            wrapper.and(queryWrapper -> {
                //拼接租户账单条件
                for (AllBillGroupQueryF.RoomInfo roomInfo : tenantCondition) {
                    queryWrapper.or(tenantWrapper -> {
                        tenantWrapper.eq("b.room_id", String.valueOf(roomInfo.getRoomId()))
                            .eq("b.payer_id", roomInfo.getPayerId());
                    });
                }
                //拼接业主账单条件
                queryWrapper.or(CollectionUtils.isNotEmpty(ownerRoomIdCondition), ownerWrapper -> {
                    ownerWrapper.in("b.room_id", ownerRoomIdCondition);
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 start
        if (CollectionUtils.isNotEmpty(queryF.getBills())) {
            //拼接业主账单条件
            wrapper.and(queryWrapper -> {
                queryWrapper.or(CollectionUtils.isNotEmpty(queryF.getBills()), ownerWrapper -> {
                    ownerWrapper.in("b.id", queryF.getBills());
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 end
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.应收账单.getColumnName());
        Field supCpUnitIdField = query.getConditions().getFields().stream()
            .filter(v -> ("b." + BillSharedingColumn.应收账单.getColumnName()).equals(v.getName()))
            .findAny().get();
        String receivableBillName = sharedBillAppService.getShareTableName(query,
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        IPage<BillGroupDto> billGroupDtoPage = billMapper.pageWithGroup(
            Page.of(query.getPageNum(), query.getPageSize()), wrapper, receivableBillName);
        List<BillGroupDto> records = billGroupDtoPage.getRecords();
        List<AllBillGroupDto> billGroupPageList = Global.mapperFacade.mapAsList(records, AllBillGroupDto.class);
        //查询子账单
        if (loadChildren && CollectionUtils.isNotEmpty(billGroupPageList)) {
            //记录子节点账单id
            List<Long> receiveBillIdList = new ArrayList<>();
            List<Long> temporaryChargeBillIdList = new ArrayList<>();
            List<Long> advanceBillIdList = new ArrayList<>();
            for (BillGroupDto record : records) {
                String[] billIds = record.getBillIds().split(",");
                String[] billTypes = record.getBillTypes().split(",");
                for (int i = 0; i < billIds.length; i++) {
                    BillTypeEnum billType = BillTypeEnum.valueOfByCode(
                        Integer.parseInt(billTypes[i]));
                    switch (billType) {
                        case 应收账单:
                            receiveBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        case 临时收费账单:
                            temporaryChargeBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        case 预收账单:
                            advanceBillIdList.add(Long.parseLong(billIds[i]));
                            break;
                        default:
                            break;
                    }
                }
            }

            //2.1.查询所有的子分组账单
            //获取子账单信息
            ArrayList<AllBillGroupDto> allBillList = new ArrayList<>();
            ReceivableBillRepository receivableBillRepository = Global.ac.getBean(
                ReceivableBillRepository.class);
            if (CollectionUtils.isNotEmpty(receiveBillIdList)) {
                List<AllBillGroupDto> receivableBillGroupDtoList = Global.mapperFacade.mapAsList(
                    receivableBillRepository.listByIdsNotTenantId(receiveBillIdList,
                        supCpUnitIdField.getValue().toString()), AllBillGroupDto.class);
                receivableBillGroupDtoList.forEach(
                    groupDto -> groupDto.setBillType(BillTypeEnum.应收账单.getCode()));
                allBillList.addAll(receivableBillGroupDtoList);
            }

            TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(
                TemporaryChargeBillRepository.class);
            if (CollectionUtils.isNotEmpty(temporaryChargeBillIdList)) {
                List<AllBillGroupDto> temporaryChargeBillGroupDtoList = Global.mapperFacade.mapAsList(
                    temporaryChargeBillRepository.listByIdsNotTenantId(temporaryChargeBillIdList,supCpUnitIdField.getValue().toString()),
                    AllBillGroupDto.class);
                temporaryChargeBillGroupDtoList.forEach(
                    groupDto -> groupDto.setBillType(BillTypeEnum.临时收费账单.getCode()));
                allBillList.addAll(temporaryChargeBillGroupDtoList);
            }

            AdvanceBillRepository advanceBillRepository = Global.ac.getBean(
                AdvanceBillRepository.class);
            if (CollectionUtils.isNotEmpty(advanceBillIdList)) {
                List<AllBillGroupDto> advanceBillGroupDtoList = Global.mapperFacade.mapAsList(
                    advanceBillRepository.listByIdsNotTenantId(advanceBillIdList),
                    AllBillGroupDto.class);
                advanceBillGroupDtoList.forEach(
                    groupDto -> groupDto.setBillType(BillTypeEnum.预收账单.getCode()));
                allBillList.addAll(advanceBillGroupDtoList);
            }

            //2.2.对子分组账单进行分组，方便父分组和子分组进行关联
            Map<String, List<AllBillGroupDto>> groupDetailMap = allBillList.stream()
                .collect(Collectors.groupingBy(AllBillGroupDto::getGroupKey));

            //3.为父分组设置子分组
            AtomicInteger index = new AtomicInteger(0);
            billGroupPageList.forEach(groupItem -> {
                groupItem.setId(
                    Long.parseLong("66" + System.currentTimeMillis()) + (index.incrementAndGet()));
                List<AllBillGroupDto> rbds = groupDetailMap.get(
                    AllBillGroupDto.getGroupKey(groupItem));
                groupItem.setChildren(rbds);
                groupItem.setActualPayAmount(
                    rbds.stream().mapToLong(AllBillGroupDto::getActualPayAmount).sum());
                groupItem.setActualUnpayAmount(
                    rbds.stream().mapToLong(AllBillGroupDto::getActualUnpayAmount).sum());
            });
        }
        return PageV.of(billGroupDtoPage.getCurrent(), billGroupDtoPage.getSize(),
            billGroupDtoPage.getTotal(), billGroupPageList);
    }

    /**
     * 分页查询账单
     *
     * @param queryF 分页信息
     * @return PageV
     */
    public PageV<AllBillPageDto> queryBillByPage(AllBillGroupQueryF queryF) {
        List<AllBillGroupQueryF.RoomInfo> roomInfoList = queryF.getRoomInfoList();
        List<Long> roomIdList = new ArrayList<>();
        String payerId = null;
        for (AllBillGroupQueryF.RoomInfo roomInfo : roomInfoList) {
            roomIdList.add(roomInfo.getRoomId());
            if (Objects.nonNull(roomInfo.getPayerId())) {
                payerId = roomInfo.getPayerId();
            }
        }
        PageF<SearchF<?>> query = queryF.getQuery();
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDisabledEnum.启用.getCode());
        wrapper.in(CollectionUtils.isNotEmpty(roomIdList), "b.room_id", roomIdList);
        wrapper.eq(StringUtils.isNotBlank(payerId), "bs.payer_id", payerId);
        wrapper.groupBy("b.id");
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(query,
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
            TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        IPage<AllBillPageDto> billDtoPage = billMapper.queryPage(
            Page.of(query.getPageNum(), query.getPageSize()), wrapper, receivableBillName,
            gatherDetailName);
        return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(), billDtoPage.getTotal(),
            billDtoPage.getRecords());
    }

    /**
     * 根据id集合获取账单信息
     *
     * @param billQueryList 查询信息
     * @return List
     */
    public List<AllBillPageDto> queryBillByIdList(List<AllBillQueryF> billQueryList) {
        //根据账单类型分组
        Map<Integer, List<AllBillQueryF>> collect = billQueryList.stream()
            .collect(Collectors.groupingBy(AllBillQueryF::getBillType));
        ArrayList<AllBillPageDto> allBillList = new ArrayList<>();
        for (Map.Entry<Integer, List<AllBillQueryF>> entry : collect.entrySet()) {
            BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(entry.getKey());
            List<Long> billIdList = entry.getValue().stream().map(AllBillQueryF::getId)
                .collect(Collectors.toList());
            getGatherBillByIdList(allBillList, billTypeEnum, billIdList, billQueryList.get(0).getSupCpUnitId());
        }
        return allBillList;
    }

    /**
     * 查询账单数量
     *
     * @param queryF 查询参数
     * @return long
     */
    public Long queryBillCount(AllBillGroupQueryF queryF) {

        PageF<SearchF<?>> query = queryF.getQuery();
        List<AllBillGroupQueryF.RoomInfo> roomInfoList = queryF.getRoomInfoList();
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDisabledEnum.启用.getCode());
        if (CollectionUtils.isNotEmpty(roomInfoList)) {
            //租客根据房号和付款人获取账单，业主根据房号获取账单
            List<AllBillGroupQueryF.RoomInfo> tenantCondition = roomInfoList.stream()
                .filter(s -> Objects.nonNull(s.getPayerId())).collect(Collectors.toList());
            //获取业主的房号id
            List<String> ownerRoomIdCondition = roomInfoList.stream()
                .filter(s -> Objects.isNull(s.getPayerId())).map(a -> String.valueOf(a.getRoomId()))
                .collect(Collectors.toList());
            //1.分页获取所有的分组
            wrapper.isNotNull("b.room_id");
            wrapper.isNotNull("b.start_time");
            wrapper.and(queryWrapper -> {
                //拼接租户账单条件
                for (AllBillGroupQueryF.RoomInfo roomInfo : tenantCondition) {
                    queryWrapper.or(tenantWrapper -> {
                        tenantWrapper.eq("b.room_id", String.valueOf(roomInfo.getRoomId()))
                            .eq("b.payer_id", roomInfo.getPayerId());
                    });
                }
                //拼接业主账单条件
                queryWrapper.or(CollectionUtils.isNotEmpty(ownerRoomIdCondition), ownerWrapper -> {
                    ownerWrapper.in("b.room_id", ownerRoomIdCondition);
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 start
        if (CollectionUtils.isNotEmpty(queryF.getBills())) {
            //拼接业主账单条件
            wrapper.and(queryWrapper -> {
                queryWrapper.or(CollectionUtils.isNotEmpty(queryF.getBills()), ownerWrapper -> {
                    ownerWrapper.in("b.id", queryF.getBills());
                });
            });
        }
        //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 end

        //装修办理缴费账单分享使用,不影响既存逻辑
        if (CollectionUtils.isNotEmpty(queryF.getDecorationBills())){
            //拼接业主账单条件
            wrapper.and(queryWrapper -> {
                queryWrapper.or(CollectionUtils.isNotEmpty(queryF.getDecorationBills()), ownerWrapper -> {
                    ownerWrapper.in("b.id", queryF.getDecorationBills());
                });
            });
        }
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(query,
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        return billMapper.countBill(wrapper, receivableBillName);
    }


    /**
     * 根据外部审批标识查询账单
     *
     * @param outApprovedIdList 外部标识集合
     * @return List
     */
    public List<AllBillPageDto> queryBillByOutApprovedId(List<String> outApprovedIdList,
        String supCpUnitId) {
        List<BillApproveE> billApproveList = approveRepository.queryApproveByOutApprovedId(
            outApprovedIdList, supCpUnitId);
        //根据账单类型进行分组
        Map<Integer, List<BillApproveE>> collect = billApproveList.stream()
            .collect(Collectors.groupingBy(BillApproveE::getBillType));
        ArrayList<AllBillPageDto> allBillList = new ArrayList<>();
        for (Map.Entry<Integer, List<BillApproveE>> entry : collect.entrySet()) {
            BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(entry.getKey());
            List<Long> billIdList = entry.getValue().stream().map(BillApproveE::getBillId)
                .collect(Collectors.toList());
            getBillByIdList(allBillList, billTypeEnum, billIdList, supCpUnitId);
        }
        return allBillList;
    }


    /**
     * 根据账单类型和账单id获取账单
     *
     * @param allBillList  账单集合
     * @param billTypeEnum 账单类型
     * @param billIdList   账单id集合
     */
    private void getGatherBillByIdList(ArrayList<AllBillPageDto> allBillList,
        BillTypeEnum billTypeEnum, List<Long> billIdList, String supCpUnitId) {
        switch (billTypeEnum) {
            case 应收账单:
            case 临时收费账单:
                ReceivableBillRepository receivableBillRepository = Global.ac.getBean(
                    ReceivableBillRepository.class);
                List<AllBillPageDto> receivableBillPageDtoList = Global.mapperFacade.mapAsList(
                    receivableBillRepository.listBillsByIdsNotTenantId(billIdList, supCpUnitId),
                    AllBillPageDto.class);
                receivableBillPageDtoList.forEach(
                    pageDto -> pageDto.setBillType(pageDto.getBillType()));
                allBillList.addAll(receivableBillPageDtoList);
                break;
            case 预收账单:
                AdvanceBillRepository advanceBillRepository = Global.ac.getBean(
                    AdvanceBillRepository.class);
                List<AllBillPageDto> advanceBillPageDtoList = Global.mapperFacade.mapAsList(
                    advanceBillRepository.listByIdsNotTenantId(billIdList), AllBillPageDto.class);
                advanceBillPageDtoList.forEach(
                    pageDto -> pageDto.setBillType(BillTypeEnum.预收账单.getCode()));
                allBillList.addAll(advanceBillPageDtoList);
                break;
            default:
                break;
        }
    }


    /**
     * 根据账单类型和账单id获取账单
     *
     * @param allBillList  账单集合
     * @param billTypeEnum 账单类型
     * @param billIdList   账单id集合
     */
    private void getBillByIdList(ArrayList<AllBillPageDto> allBillList, BillTypeEnum billTypeEnum,
        List<Long> billIdList, String supCpUnitId) {
        switch (billTypeEnum) {
            case 应收账单:
                ReceivableBillRepository receivableBillRepository = Global.ac.getBean(
                    ReceivableBillRepository.class);
                List<AllBillPageDto> receivableBillPageDtoList = Global.mapperFacade.mapAsList(
                    receivableBillRepository.listByIdsNotTenantId(billIdList, supCpUnitId),
                    AllBillPageDto.class);
                receivableBillPageDtoList.forEach(
                    pageDto -> pageDto.setBillType(BillTypeEnum.应收账单.getCode()));
                allBillList.addAll(receivableBillPageDtoList);
                break;
            case 临时收费账单:
                TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(
                    TemporaryChargeBillRepository.class);
                List<AllBillPageDto> temporaryChargeBillPageDtoList = Global.mapperFacade.mapAsList(
                    temporaryChargeBillRepository.listByIdsNotTenantId(billIdList,supCpUnitId),
                    AllBillPageDto.class);
                temporaryChargeBillPageDtoList.forEach(
                    pageDto -> pageDto.setBillType(BillTypeEnum.临时收费账单.getCode()));
                allBillList.addAll(temporaryChargeBillPageDtoList);
                break;
            case 预收账单:
                AdvanceBillRepository advanceBillRepository = Global.ac.getBean(
                    AdvanceBillRepository.class);
                List<AllBillPageDto> advanceBillPageDtoList = Global.mapperFacade.mapAsList(
                    advanceBillRepository.listByIdsNotTenantId(billIdList), AllBillPageDto.class);
                advanceBillPageDtoList.forEach(
                    pageDto -> pageDto.setBillType(BillTypeEnum.预收账单.getCode()));
                allBillList.addAll(advanceBillPageDtoList);
                break;
            default:
                break;
        }
    }


    /**
     * 查询账单金额
     *
     * @param form
     * @return
     */
    public BillAmountDto queryBillAmount(BillAmountQueryF form) {
        QueryWrapper<?> queryModel = form.getQuery().getQueryModel();
        PageQueryUtils.validQueryContainsFieldAndValue(form.getQuery(),
            "b." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(form.getQuery(),
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(form.getQuery(),
            TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        List<AllBillPageDto> allBillPageDtos = billMapper.queryList(queryModel, receivableBillName,
            gatherDetailName);
        Long actualUnpayAmountAll = 0L;
        Long amountOwed = 0L;

        if (CollectionUtils.isNotEmpty(allBillPageDtos)) {
            for (AllBillPageDto allBillPageDto : allBillPageDtos) {
                //判断当前时间是否晚于结束时间
                if (allBillPageDto.getEndTime() != null && allBillPageDto.getEndTime().toLocalDate()
                    .isBefore(LocalDateTime.now().toLocalDate())) {
                    amountOwed = amountOwed + allBillPageDto.getActualUnpayAmount();
                }
                if (allBillPageDto.getActualUnpayAmount() != 0) {
                    actualUnpayAmountAll =
                        actualUnpayAmountAll + allBillPageDto.getActualUnpayAmount();
                }
            }
        }

        List<BillAmountExtendDto> billAmountExtendDtoList = Lists.newArrayList();
        for (AllBillPageDto allBillPageDto : allBillPageDtos) {
            //账单id,法定单位id,账单金额（可缴纳金额）
            BillAmountExtendDto billAmountExtendDto = new BillAmountExtendDto();
            billAmountExtendDto.setBillId(allBillPageDto.getId());
            billAmountExtendDto.setBillType(allBillPageDto.getBillType());
            billAmountExtendDto.setStatutoryBodyId(allBillPageDto.getStatutoryBodyId());
            billAmountExtendDto.setActualUnpayAmount(allBillPageDto.getActualUnpayAmount());
            billAmountExtendDtoList.add(billAmountExtendDto);
        }
        BillAmountDto billAmountDto = new BillAmountDto();
        billAmountDto.setActualUnpayAmount(actualUnpayAmountAll);//代收款金额
        billAmountDto.setAmountOwed(amountOwed);
        billAmountDto.setBillGroupDtoList(billAmountExtendDtoList);
        return billAmountDto;
    }

    /**
     * 分页查询无效收费账单
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<AllBillPageVo> invalidChargePage(PageF<SearchF<?>> query) {
        // 处理房产树
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(query,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(query);
        }
        //校验参数中是否含有账单类型，20230710以前的该接口可以查不同类型的账号，但是分表不支持union，因此需要分开查，此处用于校验是否传了账单类型
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b.bill_type");

        QueryWrapper<?> wrapper = null;

        Field billTypeField = query.getConditions().getFields().stream()
            .filter(v -> (("b.bill_type".equals(v.getName()))) && Objects.nonNull(v.getValue()))
            .findAny().get();
        BillTypeEnum billType = BillTypeEnum.valueOfByCode(
                Integer.parseInt(billTypeField.getValue().toString()));
        invalidStateQuery(query);
        IPage<AllBillPageDto> billDtoPage;
        switch (billType) {
            case 应收账单:
                PageQueryUtils.validQueryContainsFieldAndValue(query,
                    "b." + BillSharedingColumn.应收账单.getColumnName());
                //如果是应收账单，去除账单类型，把临时账单也查询出来
                query.getConditions().getFields().remove(billTypeField);
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                wrapper.groupBy("b.id");

                billDtoPage = billMapper.invalidChargePageForReceivableBill(
                    Page.of(query.getPageNum(), query.getPageSize()), wrapper);
                billDtoPage.getRecords().forEach(e -> e.setAmountUnit("元"));
                return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(),
                    billDtoPage.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(billDtoPage.getRecords(), AllBillPageVo.class));
            case 收款单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                Field communityIdField = spacePermissionAppService.getField(query, "b.community_id");
                Optional.ofNullable(communityIdField).ifPresent(a->a.setName("gd.sup_cp_unit_id"));
                Field roomIdField = spacePermissionAppService.getField(query, "b.room_id");
                Optional.ofNullable(roomIdField).ifPresent(a->a.setName("gd.cp_unit_id"));
                List<Field> fields = query.getConditions().getFields().stream()
                    .filter(v -> !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList());
                Field supCpUnitIdField = query.getConditions().getFields().stream()
                    .filter(v -> ("b." + BillSharedingColumn.收款账单.getColumnName()).equals(
                        v.getName())).findAny().get();
                fields.add(new Field("gd." + BillSharedingColumn.收款明细.getColumnName(),
                    supCpUnitIdField.getValue().toString(), 1));
                query.getConditions().setFields(fields);
                log.info("收款单查询参数:{}", query.getConditions().getFields());
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                wrapper.groupBy("b.id");
                String gatherBillTableName = sharedBillAppService.getShareTableName(
                    supCpUnitIdField.getValue().toString(), TableNames.GATHER_BILL);
                String gatherDetailTableName = sharedBillAppService.getShareTableName(
                    supCpUnitIdField.getValue().toString(), TableNames.GATHER_DETAIL);
                billDtoPage = billMapper.invalidChargePageForGatherBill(
                    Page.of(query.getPageNum(), query.getPageSize()), wrapper, gatherBillTableName,
                    gatherDetailTableName);
                billDtoPage.getRecords().forEach(e -> e.setAmountUnit("元"));
                return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(),
                    billDtoPage.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(billDtoPage.getRecords(), AllBillPageVo.class));
            case 预收账单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                billDtoPage = billMapper.invalidChargePageForAdvanceBill(
                    Page.of(query.getPageNum(), query.getPageSize()), wrapper);
                billDtoPage.getRecords().forEach(e -> e.setAmountUnit("元"));
                return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(),
                    billDtoPage.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(billDtoPage.getRecords(), AllBillPageVo.class));
            default:
                return null;
        }

    }

    /**
     * 无效状态判断赋值
     * @param query
     */
    private static void invalidStateQuery(PageF<SearchF<?>> query) {
        Field invalidState = query.getConditions().getFields().stream()
                .filter(v -> (("invalidState".equals(v.getName()))) && Objects.nonNull(v.getValue()))
                .findAny().orElse(null);
        if (Objects.nonNull(invalidState)){
            query.getConditions().getFields().remove(invalidState);
            if("1".equals(invalidState.getValue().toString())){
                query.getConditions().getFields().add(new Field("b.state", BillStateEnum.作废.getCode(), invalidState.getMethod()));
            }else if("2".equals(invalidState.getValue().toString())){
                query.getConditions().getFields().add(new Field("b.carried_state", BillCarryoverStateEnum.已结转.getCode(), invalidState.getMethod()));
            }else if("3".equals(invalidState.getValue().toString())){
                query.getConditions().getFields().add(new Field("b.reversed", BillReverseStateEnum.已冲销.getCode(), invalidState.getMethod()));
            }else if("4".equals(invalidState.getValue().toString())){
                query.getConditions().getFields().add(new Field("b.refund_state", BillRefundStateEnum.已退款.getCode(), invalidState.getMethod()));
            }
        }
    }

    /**
     * 分页查询无效付费账单
     *
     * @param query 查询参数
     * @return PageV
     */
    public PageV<AllBillPageVo> invalidPayPage(PageF<SearchF<?>> query) {
        // 处理房产树
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(query,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(query);
        }
        //校验参数中是否含有账单类型，20230710以前的该接口可以查不同类型的账号，但是分表不支持union，因此需要分开查，此处用于校验是否传了账单类型
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b.bill_type");
        QueryWrapper<?> wrapper = null;

        Field billTypeField = query.getConditions().getFields().stream()
            .filter(v -> (("b.bill_type".equals(v.getName()))) && Objects.nonNull(v.getValue()))
            .findAny().get();
        BillTypeEnum billType = BillTypeEnum.valueOfByCode(
            Integer.parseInt(billTypeField.getValue().toString()));

        invalidStateQuery(query);
        IPage<AllBillPageDto> billDtoPage;
        switch (billType) {
            case 应付账单:
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                wrapper.groupBy("b.id");
                billRepository.invalidBillCondition(wrapper);
                billDtoPage = billMapper.invalidPayPageForPayableBill(
                    Page.of(query.getPageNum(), query.getPageSize()), wrapper);
                billDtoPage.getRecords().forEach(e -> e.setAmountUnit("元"));
                return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(),
                    billDtoPage.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(billDtoPage.getRecords(), AllBillPageVo.class));
            case 付款单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                query.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
                Field communityIdField = spacePermissionAppService.getField(query, "b.sup_cp_unit_id");
                Optional.ofNullable(communityIdField).ifPresent(a->a.setName("pd.sup_cp_unit_id"));
                Field roomIdField = spacePermissionAppService.getField(query, "b.room_id");
                Optional.ofNullable(roomIdField).ifPresent(a->a.setName("pd.cp_unit_id"));
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                wrapper.groupBy("b.id");
                billRepository.invalidBillCondition(wrapper);
                billDtoPage = billMapper.invalidPayPageForPayBill(
                    Page.of(query.getPageNum(), query.getPageSize()), wrapper);
                billDtoPage.getRecords().forEach(e -> e.setAmountUnit("元"));
                return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(),
                    billDtoPage.getTotal(),
                        MapperFacadeUtil.getMoneyMapperFacade().mapAsList(billDtoPage.getRecords(), AllBillPageVo.class));
            default:
                return null;
        }

    }

    /**
     * 统计无效收费账单
     *
     * @param query query
     * @return BillTotalDto
     */
    public BillTotalDto invalidChargeStatistics(PageF<SearchF<?>> query) {
        // 处理房产树
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(query,"b");
        if(Objects.isNull(queryPermissionF)){
            return new BillTotalDto();
        }
        //校验参数中是否含有账单类型，20230710以前的该接口可以查不同类型的账号，但是分表不支持union，因此需要分开查，此处用于校验是否传了账单类型
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b.bill_type");
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
//        wrapper.groupBy("b.id");
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.应收账单.getColumnName());

        Field billTypeField = query.getConditions().getFields().stream()
            .filter(v -> (("b.bill_type".equals(v.getName()))) && Objects.nonNull(v.getValue()))
            .findAny().get();

        invalidStateQuery(query);
        BillTypeEnum billType = BillTypeEnum.valueOfByCode(
            Integer.parseInt(billTypeField.getValue().toString()));
        switch (billType) {
            case 应收账单:
                PageQueryUtils.validQueryContainsFieldAndValue(query,
                    "b." + BillSharedingColumn.应收账单.getColumnName());
                //如果是应收账单，去除账单类型，把临时账单也查询出来
                query.getConditions().getFields().remove(billTypeField);
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                return billMapper.invalidChargeStatisticsForReceivableBill(wrapper);
            case 收款单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                Field communityIdField = spacePermissionAppService.getField(query, "b.community_id");
                Optional.ofNullable(communityIdField).ifPresent(a->a.setName("gd.sup_cp_unit_id"));
                Field roomIdField = spacePermissionAppService.getField(query, "b.room_id");
                Optional.ofNullable(roomIdField).ifPresent(a->a.setName("gd.cp_unit_id"));
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                Field supCpUnitIdField = query.getConditions().getFields().stream()
                        .filter(v -> ("b." + BillSharedingColumn.收款账单.getColumnName()).equals(
                                v.getName())).findAny().get();
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                String gatherBillTableName = sharedBillAppService.getShareTableName(
                        supCpUnitIdField.getValue().toString(), TableNames.GATHER_BILL);
                String gatherDetailTableName = sharedBillAppService.getShareTableName(
                        supCpUnitIdField.getValue().toString(), TableNames.GATHER_DETAIL);
                return billMapper.invalidChargeStatisticsForGatherBill(wrapper,gatherBillTableName,gatherDetailTableName);
            case 预收账单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                return billMapper.invalidChargeStatisticsForAdvanceBill(wrapper);
            default:
                return null;
        }

    }

    /**
     * 统计无效付费账单
     *
     * @param query query
     * @return BillTotalDto
     */
    public BillTotalDto invalidPayStatistics(PageF<SearchF<?>> query) {
        // 处理房产树
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(query,"b");
        if(Objects.isNull(queryPermissionF)){
            return new BillTotalDto();
        }
        //校验参数中是否含有账单类型，20230710以前的该接口可以查不同类型的账号，但是分表不支持union，因此需要分开查，此处用于校验是否传了账单类型
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b.bill_type");
        QueryWrapper<?> wrapper = null;

        Field billTypeField = query.getConditions().getFields().stream()
            .filter(v -> (("b.bill_type".equals(v.getName()))) && Objects.nonNull(v.getValue()))
            .findAny().get();

        invalidStateQuery(query);
        BillTypeEnum billType = BillTypeEnum.valueOfByCode(
            Integer.parseInt(billTypeField.getValue().toString()));
        switch (billType) {
            case 应付账单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
//                wrapper.groupBy("b.id");
                return billMapper.invalidPayStatisticsForPayableBill(wrapper);
            case 付款单:
                //只有应收账单传入分表字段，其他表没有该字段，需要改造
                query.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
                Field communityIdField = spacePermissionAppService.getField(query, "b.sup_cp_unit_id");
                Optional.ofNullable(communityIdField).ifPresent(a->a.setName("pd.sup_cp_unit_id"));
                Field roomIdField = spacePermissionAppService.getField(query, "b.room_id");
                Optional.ofNullable(roomIdField).ifPresent(a->a.setName("pd.cp_unit_id"));
                query.getConditions().setFields(query.getConditions().getFields().stream()
                    .filter(v ->
                        !"b.bill_type".equals(v.getName())).collect(
                        Collectors.toList()));
                wrapper = query.getConditions().getQueryModel();
                billRepository.invalidBillCondition(wrapper);
                wrapper.groupBy("b.id");
                return billMapper.invalidPayStatisticsForPayBill(wrapper);
            default:
                return null;
        }
    }

    /**
     * 分页查询合同流水账单(用于流水认领)
     *
     * @param query 分页参数
     * @return PageV
     */
    public PageV<AllBillPageDto> flowContractPage(PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "pd." + BillSharedingColumn.收款明细.getColumnName());
        if (!costCenterFacade.changeNodeIdSearch(query.getConditions(), "cost_center_id")) {
            return PageV.of(new PageF<>());
        }
        String gatherBillName = sharedBillAppService.getShareTableName(query,
            TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
            TableNames.GATHER_DETAIL, "pd." + BillSharedingColumn.收款明细.getColumnName());
        query.getConditions().setFields(query.getConditions().getFields().stream()
            .filter(v -> !(("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())
                || ("pd." + BillSharedingColumn.收款明细.getColumnName()).equals(v.getName()))
            ).collect(
                Collectors.toList()));
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        billRepository.normalBillCondition(wrapper);
        wrapper.eq("b.sys_source", SysSourceEnum.合同系统.getCode());
        wrapper.isNull("fcd.id");
        // wrapper.groupBy("b.id");

        IPage<AllBillPageDto> billDtoPage = billMapper.flowContractPage(
            Page.of(query.getPageNum(), query.getPageSize()), wrapper, gatherBillName,
            gatherDetailName);
        return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(), billDtoPage.getTotal(),
            billDtoPage.getRecords());
    }

    public PageV<FlowBillPageDto> flowPageNewZJ (PageF<SearchF<?>> query) {
        // 拿到flowRecordId
        Field flowRecordIdField = query.getConditions().getFields().stream()
                .filter(field -> "flowClaimRecordId".equals(field.getName()))
                .findFirst().orElse(null);
        if (Objects.nonNull(flowRecordIdField)){
            String flowRecordId = flowRecordIdField.getValue().toString();
            FlowClaimRecordE record = flowClaimRecordRepository.getById(Long.parseLong(flowRecordId));
            // 获取ourBank
            String ourAccount = record.getOurAccount();
            if (StringUtils.isNotBlank(ourAccount)){
                // ourAccount反向查找statutory_body_id
                String statutoryBodyId = bankAccountCostOrgMapper.queryCostOrgIdByBankAccount(ourAccount);
                // 因为列表的法定单位条件已换成了成本中心，此处对参数进行覆盖
                Field statutoryBodyIdField = query.getConditions().getFields().stream()
                        .filter(field -> "b.statutory_body_id".equals(field.getName()))
                        .findFirst().orElse(null);
                if (StringUtils.isNotBlank(statutoryBodyId) && Objects.nonNull(statutoryBodyIdField)){
                    statutoryBodyIdField.setMethod(1);
                    statutoryBodyIdField.setValue(statutoryBodyId);
                }
            }
        }
        //按照认领的类型加额外的条件
        List<Field> payChannelTypeFields = query.getConditions().getFields().stream()
                .filter(field -> "pay_channel_type".equals(field.getName()))
                .collect(Collectors.toList());
        boolean hasPayChannelType = CollectionUtils.isNotEmpty(payChannelTypeFields);
        //将pay_channel_type条件移除
        query.getConditions().getFields().removeIf(field -> "pay_channel_type".equals(field.getName()));
        PageQueryUtils.validQueryContainsFieldAndValue(query,
                "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        if (!costCenterFacade.changeNodeIdSearch(query.getConditions(), "pd.cost_center_id")) {
            return PageV.of(new PageF<>());
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(query.getConditions(), "b.statutory_body_id")) {
            return PageV.of(new PageF<>());
        }
        query.getConditions().setFields(query.getConditions().getFields().stream()
                .filter(v -> !(("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())
                        || ("pd." + BillSharedingColumn.收款明细.getColumnName()).equals(v.getName()))
                ).collect(
                        Collectors.toList()));
        List<Long> claimInvoiceReceiptIds = getClaimGatherDetailReceiptIds(query.getConditions());

        RepositoryUtil.billClaimPageConvertSearchType(query.getConditions());
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
                .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        billRepository.payAndGatherBillNormalConditions(wrapper);
        wrapper.notIn("b.pay_channel",
                Stream.of(SettleChannelEnum.结转, SettleChannelEnum.其他, SettleChannelEnum.开发支付).map(SettleChannelEnum::getCode).collect(Collectors.toList())
        );
        if (CollectionUtils.isEmpty(claimInvoiceReceiptIds)) {
            wrapper.isNull("fcgd.gather_detail_id");
        } else {
            wrapper.and(w -> w.isNull("fcgd.gather_detail_id")
                    .or().in("fcgd.gather_detail_id", claimInvoiceReceiptIds));
        }
        wrapper.eq("b.inference_state", 0);
        wrapper.ne("reversed", BillReverseStateEnum.已冲销.getCode());
        //按照认领的类型加额外的条件
        if (hasPayChannelType) {
            Integer payChannelType = (Integer) payChannelTypeFields.get(0).getValue();
            if (payChannelType == 0) {
                //现金认领只能查询现金单子
                wrapper.eq("pd.pay_channel", "CASH");
            } else {
                //非现金认领只能查询非现金单子
                wrapper.ne("pd.pay_channel", "CASH");
            }
        }
        IPage<FlowBillPageDto> allBillPageDtoIPage = billMapper.flowPageZJ(
                Page.of(query.getPageNum(), query.getPageSize()), wrapper, gatherBillName,
                gatherDetailName);

        return PageV.of(allBillPageDtoIPage.getCurrent(), allBillPageDtoIPage.getSize(), allBillPageDtoIPage.getTotal(),
                allBillPageDtoIPage.getRecords());
    }














    /**
     * 分页查询合同流水账单(用于流水认领)-新模式
     *
     * @param query 分页参数
     * @return PageV
     */
    public PageV<AllBillPageDto> flowContractPageNew(PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query,
                "b." + BillSharedingColumn.收款账单.getColumnName());
//        PageQueryUtils.validQueryContainsFieldAndValue(query,
//                "pd." + BillSharedingColumn.收款明细.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        if (!costCenterFacade.changeNodeIdSearch(query.getConditions(), "pd.cost_center_id")) {
            return PageV.of(new PageF<>());
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(query.getConditions(), "b.statutory_body_id")) {
            return PageV.of(new PageF<>());
        }
        query.getConditions().setFields(query.getConditions().getFields().stream()
            .filter(v -> !(("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())
                || ("pd." + BillSharedingColumn.收款明细.getColumnName()).equals(v.getName()))
            ).collect(
                Collectors.toList()));
        // 补充查询条件
        // 判断是否是修改时查询，传入流水认领记录ID
        List<Long> claimInvoiceReceiptIds = getClaimInvoiceReceiptIds(query.getConditions());
        RepositoryUtil.billClaimPageConvertSearchType(query.getConditions());
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        billRepository.payAndGatherBillNormalConditions(wrapper);
        if (CollectionUtils.isEmpty(claimInvoiceReceiptIds)) {
            wrapper.isNull("fcd.id");
        } else {
            wrapper.and(w -> w.isNull("fcd.id")
                    .or().in("b.id", claimInvoiceReceiptIds));
        }
        if(TenantUtil.bf2()){
            wrapper.notIn("b.pay_channel",
                    Stream.of(SettleChannelEnum.结转, SettleChannelEnum.其他, SettleChannelEnum.开发支付).map(SettleChannelEnum::getCode).collect(Collectors.toList())
            );

            wrapper.eq("b.inference_state", 0);
        } else {
            wrapper.notIn("b.pay_channel",
                    Stream.of(SettleChannelEnum.结转, SettleChannelEnum.开发支付).map(SettleChannelEnum::getCode).collect(Collectors.toList())
            );
        }
        wrapper.ne("reversed", BillReverseStateEnum.已冲销.getCode());
        // wrapper.groupBy("b.id");

        IPage<AllBillPageDto> billDtoPage = billMapper.flowContractPage(
            Page.of(query.getPageNum(), query.getPageSize()), wrapper, gatherBillName,
            gatherDetailName);
        // 过滤掉 收款方式为结转和押金结转的收款金额
        receiptAmountUtils.handleNoCheckAmount(gatherDetailName,billDtoPage);
        return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(), billDtoPage.getTotal(),
            billDtoPage.getRecords());
    }

    private List<Long> getClaimInvoiceReceiptIds(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            // 传入了认领记录ID，说明是修改时查询
            if ("flowClaimRecordId".equals(field.getName())) {
                iterator.remove();
                return flowClaimDetailRepository.queryInvoiceIdsByFlowClaimRecordId(field.getValue());
            }
        }
        return Collections.emptyList();
    }
    private List<Long> getClaimGatherDetailReceiptIds(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            // 传入了认领记录ID，说明是修改时查询
            if ("flowClaimRecordId".equals(field.getName())) {
                iterator.remove();
                return flowClaimGatherDetailRepository.queryGaterDetailsByFlowClaimRecordId(field.getValue());
            }
        }
        return Collections.emptyList();
    }
    /**
     * 根据id集合查询合同流水账单(用于流水认领)
     *
     * @param idList 账单id集合
     * @return PageV
     */
    public List<AllBillPageDto> flowContractIdList(List<Long> idList, String supCpUnitId) {
        if (CollectionUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        List<AllBillPageDto> flowedContractIdList = billMapper.flowContractIdList(idList,
                sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL),gatherDetailName
                );

        // 过滤掉 收款方式为结转和押金结转的收款金额
        receiptAmountUtils.handleNoCheckAmount(gatherDetailName,flowedContractIdList);
        return flowedContractIdList;
    }


    public List<FlowBillPageDto>  flowPageZJ(List<Long> idList, String supCpUnitId) {
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        List<FlowBillPageDto> allBillPageDtos = billMapper.flowZjIdList(idList, gatherBillName, gatherDetailName);
        return allBillPageDtos;
    }


    /**
     * 费项分组分页查询账单列表(用于业务信息)
     *
     * @param queryF
     * @return
     */
    public Page<ChargeBillGroupDto> queryChargeBillByGroup(PageF<SearchF<?>> queryF) {
        SearchF<?> conditions = queryF.getConditions();
        QueryWrapper<?> wrapper = conditions.getQueryModel();
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        wrapper.groupBy("b.charge_item_id,year");
        normalBillCondition(wrapper);
        PageQueryUtils.validQueryContainsFieldAndValue(queryF,
            "b." + BillSharedingColumn.应收账单.getColumnName());
        String tableName = sharedBillAppService.getShareTableName(queryF,
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        return billMapper.queryChargeBillByGroup(Page.of(queryF.getPageNum(), queryF.getPageSize()),
            wrapper, tableName);
    }

    /**
     * 统计费项分组分页查询账单列表(用于业务信息)
     *
     * @param queryF
     * @return
     */
    public ChargeBillGroupStatisticsDto statisticsChargeBillByGroup(SearchF<?> queryF) {
        QueryWrapper<?> wrapper = queryF.getQueryModel()
            .eq("b.deleted", DataDisabledEnum.启用.getCode());
        normalBillCondition(wrapper);
        PageQueryUtils.validQueryContainsFieldAndValue(queryF,
            "b." + BillSharedingColumn.应收账单.getColumnName());
        String tableName = sharedBillAppService.getShareTableName(queryF,
            TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
        return billMapper.statisticsChargeBillByGroup(wrapper, tableName);
    }

    /**
     * 正常账单条件
     */
    private void normalBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper.ne("b.state", BillStateEnum.作废.getCode())
            .ne("b.reversed", BillReverseStateEnum.已冲销.getCode())
            .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
            .ne("b.refund_state", BillRefundStateEnum.已退款.getCode());
    }

    /**
     * 统计合同流水账单金额(用于流水认领)
     *
     * @param query 查询参数
     * @return FlowContractBillStatisticsDto
     */
    public FlowContractBillStatisticsDto flowContractAmount(PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query,
            "b." + BillSharedingColumn.收款账单.getColumnName());
        if (!costCenterFacade.changeNodeIdSearch(query.getConditions(), "cost_center_id")) {
            return new FlowContractBillStatisticsDto();
        }
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
            .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        billRepository.normalBillCondition(wrapper);
        wrapper.eq("b.sys_source", SysSourceEnum.合同系统.getCode());
        wrapper.isNull("fcd.id");
        wrapper.groupBy("b.id");
        String gatherBillName = sharedBillAppService.getShareTableName(query,
            TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
            TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        return billMapper.flowContractAmount(wrapper, gatherBillName, gatherDetailName);
    }

    public PageV<AllBillPageDto> channelFlowClaimPage(PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query,
                "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(query,
                TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        if (!costCenterFacade.changeNodeIdSearch(query.getConditions(), "pd.cost_center_id")) {
            return PageV.of(new PageF<>());
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(query.getConditions(), "b.statutory_body_id")) {
            return PageV.of(new PageF<>());
        }
        query.getConditions().setFields(query.getConditions().getFields().stream()
                .filter(v -> !(("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())
                        || ("pd." + BillSharedingColumn.收款明细.getColumnName()).equals(v.getName()))
                ).collect(
                        Collectors.toList()));
        RepositoryUtil.billClaimPageConvertSearchType(query.getConditions());
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel()
                .eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        billRepository.payAndGatherBillNormalConditions(wrapper);
        wrapper.notIn("b.pay_channel",
                Stream.of(SettleChannelEnum.结转, SettleChannelEnum.开发支付, SettleChannelEnum.其他).map(SettleChannelEnum::getCode).collect(Collectors.toList())
        );
        wrapper.ne("reversed", BillReverseStateEnum.已冲销.getCode());
        wrapper.ne("b.reconcile_state", 2);
        wrapper.ne("b.mc_reconcile_state", 2);
        // wrapper.groupBy("b.id");

        IPage<AllBillPageDto> billDtoPage = billMapper.channelFlowClaim(
                Page.of(query.getPageNum(), query.getPageSize()), wrapper, gatherBillName,
                gatherDetailName);
        return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(), billDtoPage.getTotal(),
                billDtoPage.getRecords());
    }

    public List<AllBillGroupDto> getChargePayBillGroupDetail(AllBillGroupQueryF query) {
        if (CollectionUtils.isEmpty(query.getBills())) {
            return new ArrayList<>();
        }
        String receivableBillName = sharedBillAppService.getShareTableName(query.getCommunityId(), TableNames.RECEIVABLE_BILL);
        return billMapper.getChargePayBillGroup(query.getBills(), receivableBillName);
    }
}
