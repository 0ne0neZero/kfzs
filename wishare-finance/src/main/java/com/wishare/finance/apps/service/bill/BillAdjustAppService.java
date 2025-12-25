package com.wishare.finance.apps.service.bill;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.bill.fo.BillingCostCenterAdjustCommunityF;
import com.wishare.finance.apps.model.bill.fo.BillingCostCenterAdjustF;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.service.acl.AclOrgClientService;
import com.wishare.finance.apps.service.acl.AclSpaceClientService;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.service.BillAdjustDomainService;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.remote.fo.space.SpaceLastNodesF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostForBlockF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceV;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.Global;
import com.wishare.starter.ThreadPoolManager;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/28 9:47
 * @version: 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAdjustAppService {

    private final BillAdjustDomainService billAdjustDomainService;

    private final AclOrgClientService aclOrgClientService;

    private final AclSpaceClientService aclSpaceClientService;

    private final ReceivableBillAppService receivableBillAppService;

    private final AdvanceBillAppService advanceBillAppService;


    /**
     * 根据账单id获取调整记录
     * @param billId
     * @return
     */
    public List<BillAdjustV> listByBillId(Long billId) {
        List<BillAdjustE> list = billAdjustDomainService.listByBillId(billId);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return Global.mapperFacade.mapAsList(list, BillAdjustV.class);
    }



    /**
     * 分页获取调整记录
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillAdjustV}</>
     */
    public PageV<BillAdjustV> getBillAdjustPage(PageF<SearchF<?>> queryF) {
        return billAdjustDomainService.getBillAdjustPage(queryF);
    }




    /**
     *
     * @param billingCostCenterAdjustF
     * @return
     */
    public Boolean orgInfo(BillingCostCenterAdjustF billingCostCenterAdjustF) {
        //1、获取成本中心信息 -- 校验成本中心信息
        List<OrgFinanceCostV> orgFinanceCostVS = aclOrgClientService.getByBlockIds(
                Global.mapperFacade.map(billingCostCenterAdjustF, OrgFinanceCostForBlockF.class));
        //校验成本中心 得到未匹配的成本中心
        List<String> blockIds = this.checkOrgFinanceCostVS(billingCostCenterAdjustF,
                orgFinanceCostVS);
        //存在未匹配的期区成本中心
        OrgFinanceCostV orgFinanceCostMaster;
        if(!CollectionUtils.isEmpty(blockIds)){
            //查询项目下的成本中心
            List<OrgFinanceCostV> orgFinanceCostMasters = aclOrgClientService.queryFinanceIdByCommunityId(
                    List.of(billingCostCenterAdjustF.getCommunityId()));
            ErrorAssertUtils.isFalseThrow400(CollectionUtils.isEmpty(orgFinanceCostMasters)||orgFinanceCostMasters.size()>1,"未配置项目的成本中心,请配置");
            orgFinanceCostMaster = orgFinanceCostMasters.get(0);
            OrgFinanceCostRv orgFinanceCostRv = aclOrgClientService.getOrgFinanceCostById(
                    orgFinanceCostMaster.getId());
            ErrorAssertUtils.isFalseThrow400(Objects.isNull(orgFinanceCostRv),"未配置项目的成本中心,请配置");
            /* 成本中心名称 */
            orgFinanceCostMaster.setNameCn(orgFinanceCostRv.getNameCn());
            //处理未配置的成本中心 组装 orgFinanceCostV
            for (String blockId : blockIds){
                if(CollectionUtils.isEmpty(orgFinanceCostVS)){
                    orgFinanceCostVS = Lists.newArrayList();
                }
                OrgFinanceCostV orgFinanceCostV = new OrgFinanceCostV();
                orgFinanceCostV.setBlock(blockId);
                orgFinanceCostVS.add(orgFinanceCostV);
            }
        } else {
            orgFinanceCostMaster = null;
        }
        //2、异步 查询对应项目||期区下的房间 && 3、针对所有账单的||收款明细的进行成本中心更新
        log.info("orgInfo--> start 异步[blockIds{}orgFinanceCostVS{}master{}]", JSONObject.toJSONString(blockIds),JSONObject.toJSONString(orgFinanceCostVS),JSONObject.toJSONString(orgFinanceCostMaster));
        orgFinanceCostVS.forEach(orgFinanceCostV->{
            ThreadPoolManager.execute(ThreadLocalUtil.curIdentityInfo(), "", String -> {
                //期区未配置成本中心
                if(blockIds.contains(orgFinanceCostV.getBlock())){
                    this.doOrgInfo(billingCostCenterAdjustF,orgFinanceCostV,orgFinanceCostMaster);
                }else{
                    this.doOrgInfo(billingCostCenterAdjustF,orgFinanceCostV,null);
                }
            });
        });
        return true;
    }


    /**
     * 2、异步 查询对应项目||期区下的房间 && 3、针对所有账单的||收款明细的进行成本中心更新
     * @param billingCostCenterAdjustF
     * @param orgFinanceCostV
     * @param master 项目成本中心
     */
    private void doOrgInfo(BillingCostCenterAdjustF billingCostCenterAdjustF,OrgFinanceCostV orgFinanceCostV,OrgFinanceCostV master){
        String communityId = billingCostCenterAdjustF.getCommunityId();
        //待处理房间
        List<SpaceV> spaceVS = aclSpaceClientService.getLastSpaceNodesByPids(
                SpaceLastNodesF.builder().pids(List.of(orgFinanceCostV.getBlock()))
                        .communityId(communityId).build());
        //若存在项目成本中心 则使用项目成本中心
        OrgFinanceCostV orgFinanceCostResult = Objects.nonNull(master)?master:orgFinanceCostV;
        List<String> roomIds = spaceVS.stream().map(spaceV->String.valueOf(spaceV.getId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(roomIds)){
            log.error("doOrgInfo no rooms");
        }
        log.info("doOrgInfo--> 待处理房间[roomIds{}orgFinanceCostResult{}]", JSONObject.toJSONString(roomIds), JSONObject.toJSONString(orgFinanceCostResult));
        List<List<String>> groupedRoomIds = IntStream.range(0, roomIds.size()) .boxed() .collect(Collectors.groupingBy(index -> index / 800)) .values() .stream() .map(indices -> indices.stream().map(roomIds::get).collect(Collectors.toList())) .collect(Collectors.toList());

        Integer billType = billingCostCenterAdjustF.getBillType();
        String gatherDetailName = Global.ac.getBean(SharedBillAppService.class).getShareTableName(communityId, TableNames.GATHER_DETAIL);
        switch (billType) {
            case 1:

                //sup_cp_unit_id
            case 3:
                log.info("billType[{}]",billType);
                String receivableBillName = Global.ac.getBean(SharedBillAppService.class).getShareTableName(communityId, TableNames.RECEIVABLE_BILL);
                for (List<String> roomGroup : groupedRoomIds){
                    //临时账单||应收账单
                    Global.ac.getBean(ReceivableBillRepository.class).updateCostMsgByRoomIds(billingCostCenterAdjustF.getBillType(),roomGroup,communityId,orgFinanceCostResult.getId(),orgFinanceCostResult.getNameCn(),receivableBillName);
                    //收款明细
                    Global.ac.getBean(GatherDetailRepository.class).updateCostMsgByRoomIds(roomGroup,billType,communityId,orgFinanceCostResult.getId(),orgFinanceCostResult.getNameCn(),gatherDetailName,receivableBillName);

                }
                break;
            case 2:
                log.info("billType[{}]",billType);
                for (List<String> roomGroup : groupedRoomIds){
                    //预收账单
                    Global.ac.getBean(AdvanceBillRepository.class).updateCostMsgByRoomIds(roomGroup,communityId,orgFinanceCostResult.getId(),orgFinanceCostResult.getNameCn());
                    //收款明细
                    Global.ac.getBean(GatherDetailRepository.class).updateCostMsgByRoomIds(roomGroup,communityId,orgFinanceCostResult.getId(),orgFinanceCostResult.getNameCn(),gatherDetailName);

                }
                break;
        }
        log.info("doOrgInfo 处理结束");
    }





    /**
     * 校验期区成本中心
     * 期区成本中心必须1对1
     * @param billingCostCenterAdjustF
     * @param orgFinanceCostVS
     */
    private List<String> checkOrgFinanceCostVS(
            BillingCostCenterAdjustF billingCostCenterAdjustF,List<OrgFinanceCostV> orgFinanceCostVS){
        if(CollectionUtils.isEmpty(orgFinanceCostVS)){
            //不存在任何期区成本中心
            return billingCostCenterAdjustF.getBlockIds();
        }
        //收集没有期区成本中心的期区信息
        List<String> blockIds = Lists.newArrayList();
        //处理 存在1个期区多个成本中心 && 期区未匹配成本中心的
        //map<期区id,数量>
        Map<String, Long> blockCountMap = orgFinanceCostVS.stream()
                .collect(Collectors.groupingBy(OrgFinanceCostV::getBlock, Collectors.counting()));
        List<String> multipleBlockKeys = blockCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        //存在1个期区多个成本中心
        if(!CollectionUtils.isEmpty(multipleBlockKeys)){
            String blockNames = multipleBlockKeys.stream()
                    .map(key -> billingCostCenterAdjustF.getBlockIdsName().get(key))
                    .collect(Collectors.joining(","));
            ErrorAssertUtils.isThrow400("修正失败，"+blockNames+"期区未直接关联启用的成本中心，请去成本中心页面检查");
        }
        //期区未匹配成本中心的 -- 调整为使用项目的
        if(billingCostCenterAdjustF.getBlockIds().size() != orgFinanceCostVS.size()){
            return billingCostCenterAdjustF.getBlockIds().stream()
                    .filter(tmpId -> !blockCountMap.containsKey(tmpId)).collect(
                            Collectors.toList());
            // String blockNames = collect.stream()
            //         .map(key -> billingCostCenterAdjustF.getBlockIdsName().get(key))
            //         .collect(Collectors.joining(","));
            // ErrorAssertUtils.isThrow400("修正失败，"+blockNames+"期区未直接关联启用的成本中心，请去成本中心页面检查");
        }
        return Lists.newArrayList();
    }


    /**
     *
     * @param billingCostCenterAdjustF
     * @return
     */
    public Boolean orgInfoCommunity(BillingCostCenterAdjustCommunityF billingCostCenterAdjustCommunityF) {
        //查询项目下的成本中心
        OrgFinanceCostV orgFinanceCostV = null;
        try {
            orgFinanceCostV = aclOrgClientService.getOrgFinanceCostByCommunityId(
                    billingCostCenterAdjustCommunityF.getCommunityId());
        }catch (Exception e){
            log.error("调用成本中心异常",e);
            ErrorAssertUtils.isThrow400("调用成本中心异常");
        }
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(orgFinanceCostV),"未配置项目的成本中心,请配置");
        log.info("orgInfo--> start 异步[master{}]", JSONObject.toJSONString(orgFinanceCostV));
        /* do 更新账单成本中心 */
        OrgFinanceCostV finalOrgFinanceCostV = orgFinanceCostV;
        ThreadPoolManager.execute(ThreadLocalUtil.curIdentityInfo(), "", String -> {
            this.doOrgInfoCommunity(billingCostCenterAdjustCommunityF, finalOrgFinanceCostV);
        });
        return true;
    }


    /**
     * 针对所有账单的||收款明细的进行成本中心更新
     *
     * @param billingCostCenterAdjustF 入参
     * @param orgFinanceCostV 项目成本中心
     */
    private void doOrgInfoCommunity(BillingCostCenterAdjustCommunityF billingCostCenterAdjustCommunityF,OrgFinanceCostV master){

        String communityId = billingCostCenterAdjustCommunityF.getCommunityId();
        Integer billType = billingCostCenterAdjustCommunityF.getBillType();
        String gatherDetailName = Global.ac.getBean(SharedBillAppService.class).getShareTableName(communityId, TableNames.GATHER_DETAIL);
        switch (billType) {
            case 1:
                //sup_cp_unit_id
            case 3:
                log.info("billType[{}]",billType);
                String receivableBillName = Global.ac.getBean(SharedBillAppService.class).getShareTableName(communityId, TableNames.RECEIVABLE_BILL);
                //临时账单||应收账单
                Global.ac.getBean(ReceivableBillRepository.class).updateCostMsgBySupCpUnitId(billingCostCenterAdjustCommunityF.getBillType(),communityId,master.getId(),master.getNameCn(),receivableBillName);
                //收款明细
                Global.ac.getBean(GatherDetailRepository.class).updateCostMsgBySupCpUnitId(billType,communityId,master.getId(),master.getNameCn(),gatherDetailName,receivableBillName);
                break;
            case 2:
                log.info("billType[{}]",billType);
                //预收账单
                Global.ac.getBean(AdvanceBillRepository.class).updateCostMsgBySupCpUnitId(communityId,master.getId(),master.getNameCn());
                //收款明细
                Global.ac.getBean(GatherDetailRepository.class).updateCostMsgBySupCpUnitId(communityId,master.getId(),master.getNameCn(),gatherDetailName);
                break;
        }
        log.info("doOrgInfoCommunity 处理结束");
    }




}
