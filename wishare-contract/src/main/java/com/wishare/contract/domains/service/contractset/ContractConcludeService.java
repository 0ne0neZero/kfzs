package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractConcludeF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeSaveF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeUpdateF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.fo.ReferenceBillRf;
import com.wishare.contract.apps.remote.fo.bpm.*;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.apps.remote.vo.bpm.BpmContractReturnV;
import com.wishare.contract.apps.service.contractset.ContractCategoryAppService;
import com.wishare.contract.apps.vo.contractset.ContractCategoryDetailV;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.consts.contractset.ContractBpmProcessRecordFieldConst;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.mapper.contractset.*;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.utils.FileStorageUtil;
import com.wishare.tools.starter.vo.FileVo;
import java.util.Set;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同订立信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Service
@Slf4j
public class ContractConcludeService extends ServiceImpl<ContractConcludeMapper, ContractConcludeE> implements
    IOwlApiBase, ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeMapper contractConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionPlanMapper contractCollectionPlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanMapper contractProfitLossPlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanMapper contractBondPlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private ContractCategoryAppService contractCategoryAppService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractBpmProcessRecordMapper contractBpmProcessRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    private OrgEnhanceComponent orgEnhanceComponent;

    /**
     * 合同基本信息表
     */
    private static final String CONTRACT_TABLE = "contract_conclude";
    /**
     * 逗号,用于分隔图片
     */
    private static final String COMMA = ",";

    @Nullable
    public ContractDetailsV getContractConclude(Long id) {
        ContractConcludeE contractConcludeE = contractConcludeMapper.selectById(id);
        if (contractConcludeE != null) {
            ContractDetailsV contractConcludeV = Global.mapperFacade.map(contractConcludeE, ContractDetailsV.class);
            //其他说明文件FileVo
            contractConcludeV.setOtherDocumentsFileVo(fileStorageUtils.getFileNameList(contractConcludeV.getOtherDocuments(), contractConcludeE.getOtherDocName()));
            //合同附件FileVo
            contractConcludeV.setContractEnclosureFileVo(fileStorageUtils.getFileNameList(contractConcludeV.getContractEnclosure(), contractConcludeE.getEnclosureName()));
            //合同文本FileVo
            FileVo fileVo = FileStorageUtil.createFormalFileVo(contractConcludeV.getContractText());
            fileVo.setName(contractConcludeE.getContractTextName());
            contractConcludeV.setContractTextFileVo(fileVo);
            List<String> orgIds = Arrays.asList(contractConcludeE.getOrgId().split(COMMA));
            contractConcludeV.setOrgIds(orgIds.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            if (contractConcludeV.getContractNature().equals(ContractSetConst.INCOME)) {
                //收入类合同，甲方为客商，乙方组织架构
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.queryById(contractConcludeV.getPartyAId()).getName());
                }
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getNameCn());
                }
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.PAY)) {
                //支出类合同，甲方为组织，乙方客商
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getNameCn());
                }
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.queryById(contractConcludeV.getPartyBId()).getName());
                }
            }
            //合同分类
            ContractCategoryDetailV contractCategoryDetailV = contractCategoryAppService.queryById(contractConcludeV.getCategoryId());
            if (null != contractCategoryDetailV) {
                contractConcludeV.setCategoryName(contractCategoryDetailV.getName());
                contractConcludeV.setCategoryPath(contractCategoryDetailV.getPathList());
                //  查询合同分类  业务类型
                contractConcludeV.setNatureTypeId(contractCategoryDetailV.getNatureTypeId());
                contractConcludeV.setNatureTypeCode(contractCategoryDetailV.getNatureTypeCode());
                contractConcludeV.setNatureTypeName(contractCategoryDetailV.getNatureTypeName());
            }
            return contractConcludeV;
        } else {
            return null;
        }
    }

    public ContractDetailsV selectById(Long id) {
        ContractConcludeE contractConcludeE = contractConcludeMapper.selectById(id);
        return Global.mapperFacade.map(contractConcludeE, ContractDetailsV.class);
    }

    public Long saveContract(ContractConcludeSaveF contractConcludeF) {
        Long id = UidHelper.nextId(ContractConcludeFieldConst.CONTRACT_INFO);
        ContractConcludeE map = Global.mapperFacade.map(contractConcludeF, ContractConcludeE.class);
        map.setId(id);
        contractConcludeMapper.insert(map);
        return map.getId();
    }

    @Nonnull
    public List<ConcludeInfoV> listContractConclude(ContractConcludeF contractConcludeF) {
        return contractConcludeMapper.listContractConclude(contractConcludeF);
    }

    public Long saveContractConclude(ContractConcludeSaveF contractConcludeF) {
        contractConcludeF.setContractAmount(contractConcludeF.getAmountTaxIncluded());
        Long id = UidHelper.nextId(ContractConcludeFieldConst.CONTRACT_INFO);
        //上传合同文本
        if (contractConcludeF.getContractTextFileVo() != null) {
            contractConcludeF.setContractText(fileStorageUtils.submitFile(
                    contractConcludeF.getContractTextFileVo(), ContractConcludeE.class, contractConcludeF.getTenantId()));
            contractConcludeF.setContractTextName(contractConcludeF.getContractTextFileVo().getName());
        }
        //上传合同附件（批量最多5张）
        contractConcludeF.setContractEnclosure(fileStorageUtils.batchSubmitFile(
                contractConcludeF.getContractEnclosureFileVo(), null, id, contractConcludeF.getTenantId()));
        contractConcludeF.setEnclosureName(fileStorageUtils.batchSubmitName(contractConcludeF.getContractEnclosureFileVo()));
        contractConcludeF.setOtherDocuments(fileStorageUtils.batchSubmitFile(
                contractConcludeF.getOtherDocumentsFileVo(), null, id, contractConcludeF.getTenantId()));
        contractConcludeF.setOtherDocName(fileStorageUtils.batchSubmitName(contractConcludeF.getOtherDocumentsFileVo()));
        ContractConcludeE map = Global.mapperFacade.map(contractConcludeF, ContractConcludeE.class);
        map.setId(id);
        contractConcludeMapper.insert(map);
        return map.getId();
    }

    public void updateContractConclude(ContractConcludeUpdateF contractConcludeF) {
        //查询合同先删除旧的然后换成新的
        ContractConcludeE contractConcludeE = contractConcludeMapper.selectById(contractConcludeF.getId());
        if (null != contractConcludeE) {
            //上传合同文本
            if (contractConcludeF.getContractTextFileVo() != null) {
                if (contractConcludeF.getContractTextFileVo().getType() == 0) {
                    if (contractConcludeE.getContractText() != null) {
                        fileStorageUtils.delete(contractConcludeE.getContractText());
                    }
                    contractConcludeF.setContractText(fileStorageUtils.
                            handleFileOV(contractConcludeE.getContractText(),
                                    contractConcludeF.getContractTextFileVo(), contractConcludeF.getTenantId(), ContractSetConst.CONTRACT, ContractConcludeE.class));
                    contractConcludeF.setContractTextName(contractConcludeF.getContractTextFileVo().getName());
                }
            }
            //上传合同附件（批量最多5张）
            if (CollectionUtils.isEmpty(contractConcludeF.getContractEnclosureFileVo())) {
                contractConcludeF.setEnclosureName(null);
                contractConcludeF.setContractEnclosure(null);
            }
            if (!CollectionUtils.isEmpty(contractConcludeF.getContractEnclosureFileVo()) && contractConcludeF.getContractEnclosureFileVo().size() > 0) {
                contractConcludeF.setContractEnclosure(fileStorageUtils.batchSubmitFile(contractConcludeF.
                        getContractEnclosureFileVo(), contractConcludeE.getContractEnclosure(), contractConcludeF.getId(), contractConcludeF.getTenantId()));
                contractConcludeF.setEnclosureName(fileStorageUtils.batchSubmitName(contractConcludeF.getContractEnclosureFileVo()));
            }
            if (CollectionUtils.isEmpty(contractConcludeF.getOtherDocumentsFileVo())) {
                contractConcludeF.setOtherDocuments(null);
                contractConcludeF.setOtherDocName(null);
            }
            if (!CollectionUtils.isEmpty(contractConcludeF.getOtherDocumentsFileVo()) && contractConcludeF.getOtherDocumentsFileVo().size() > 0) {
                contractConcludeF.setOtherDocuments(fileStorageUtils.batchSubmitFile(contractConcludeF.
                        getOtherDocumentsFileVo(), contractConcludeE.getOtherDocuments(), contractConcludeF.getId(), contractConcludeF.getTenantId()));
                contractConcludeF.setOtherDocName(fileStorageUtils.batchSubmitName(contractConcludeF.getOtherDocumentsFileVo()));
            }
            ContractConcludeE map = Global.mapperFacade.map(contractConcludeF, ContractConcludeE.class);
            contractConcludeMapper.updateById(map);
        }
    }

    public boolean removeContractConclude(Long id) {
        ContractConcludeE map = new ContractConcludeE();
        map.setId(id);
        map.setDeleted(Const.State._1);
        contractConcludeMapper.updateById(map);
        return true;
    }

    public PageV<ContractConcludeV> pageContractConcludePage(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        Page<ContractConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        QueryWrapper<ContractConcludeE> queryWrapper = request.getConditions().getQueryModel();
        //客户平台需要数据隔离
        queryWrapper.eq(ContractConcludeFieldConst.TENANT_ID, tenantId);
        //组织权限隔离
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            queryWrapper.in(ContractConcludeFieldConst.BELONG_ORG_ID, orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class)));
        }
        // 查最上级
        IPage<ContractConcludeV> pageList = contractConcludeMapper.queryByPage(pageF, queryWrapper, tenantId);
        List<ContractConcludeV> records = pageList.getRecords();
        // 查所有子级
        List<Long> parentIdList = records.stream().map(ContractConcludeV::getId).collect(Collectors.toList());
        List<ContractConcludeV> concludeVList = contractConcludeMapper.queryByPath(queryWrapper, parentIdList, tenantId);
        for (ContractConcludeV contractConcludeV : concludeVList) {
            //部门对应的区域名称
            if (null != orgFeignClient.getById(contractConcludeV.getTenantId())) {
                OrgInfoRv orgInfoRv = orgFeignClient.getByOrgId(Long.parseLong(contractConcludeV.getBelongOrgId()));
                if(ObjectUtils.isNotEmpty(orgInfoRv)){
                    OrgInfoRv orgInfoRv1 = orgFeignClient.getByOrgId(orgInfoRv.getPid());
                    if(ObjectUtils.isNotEmpty(orgInfoRv1)){
                        contractConcludeV.setTenantName(orgInfoRv1.getOrgName());
                    }
                }
            }
            //已存在业务表中，列表不需要查询
//            if (contractConcludeV.getCommunityId() != null && contractConcludeV.getCommunityId().length() < 20) {
//                if (null != orgFeignClient.getByFinanceId(Long.valueOf(contractConcludeV.getCommunityId()))) {
//                    contractConcludeV.setCommunityName(orgFeignClient.getByFinanceId(Long.valueOf(contractConcludeV.getCommunityId())).getNameCn());
//                }
//            }
            if (StringUtils.hasText(contractConcludeV.getContractText())) {
                contractConcludeV.setContractTextFileVo(FileStorageUtil.createFormalFileVo(contractConcludeV.getContractText()));
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.INCOME)) {
                //收入类合同，甲方为客商，乙方组织架构
                if(contractConcludeV.getPartyAId() != null){
                    if (null != orgFeignClient.queryById(contractConcludeV.getPartyAId())) {
                        contractConcludeV.setPartyAName(orgFeignClient.queryById(contractConcludeV.getPartyAId()).getName());
                    }
                }
                if(contractConcludeV.getPartyBId() != null){
                    if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId())) {
                        contractConcludeV.setPartyBName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getNameCn());
                    }
                }
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.PAY)) {
                //支出类合同，甲方为组织，乙方客商
                if(contractConcludeV.getPartyAId() != null){
                    if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId())) {
                        contractConcludeV.setPartyAName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getNameCn());
                    }
                }
                if(contractConcludeV.getPartyBId() != null){
                    if (null != orgFeignClient.queryById(contractConcludeV.getPartyBId())) {
                        contractConcludeV.setPartyBName(orgFeignClient.queryById(contractConcludeV.getPartyBId()).getName());
                    }
                }
            }
        }
        List<ContractConcludeV> list = TreeUtil.treeing(concludeVList);
        return PageV.of(request, pageList.getTotal(), list);
    }

    public Integer selectContractCount(Long pid, String tenantId) {
        return contractConcludeMapper.selectContractCount(pid, tenantId);
    }

    public void contractApprove(ContractDetailsV contractConclude) {
        ContractConcludeE map = new ContractConcludeE();
        assert contractConclude != null;
        //调用bpm审核接口 并把状态改为审核中
        String resId;
        Long recordId;
        //提交审核 并用审核记录表记录
        BpmContractReturnV bpmContractReturnV = doCommitReview(contractConclude);
        Long id = contractConclude.getId();
        if (bpmContractReturnV.isSuccessFlag()) {
            resId = bpmContractReturnV.getResId();
            String uuid = bpmContractReturnV.getUuid();
            recordId = UidHelper.nextId(ContractBpmProcessRecordFieldConst.CONTRACT_BPM_PROCESS_RECORD_INFO);
            ContractBpmProcessRecordE contractBpmProcessRecordE = new ContractBpmProcessRecordE();
            contractBpmProcessRecordE.setId(recordId);
            contractBpmProcessRecordE.setBpmBoUuid(uuid);
            contractBpmProcessRecordE.setProcessId(resId);
            contractBpmProcessRecordE.setReviewStatus(ContractSetConst.POSSING);
            contractBpmProcessRecordE.setType(contractConclude.getContractNature());
            contractBpmProcessRecordE.setTenantId(contractConclude.getTenantId());
            contractBpmProcessRecordE.setContractId(id);
            contractBpmProcessRecordMapper.insert(contractBpmProcessRecordE);
        } else {
            throw BizException.throw400("提交审核失败");
        }
        //记录调用流程
        map.setReviewStatus(ContractSetConst.POSSING);
        map.setProcId(recordId);
        map.setId(id);
        contractConcludeMapper.updateById(map);
    }

    public ContractConcludeSumV amountSum(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        //客户平台需要数据隔离
        Page<ContractConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        QueryWrapper<ContractConcludeE> queryWrapper = request.getConditions().getQueryModel();
        //客户平台需要数据隔离
        queryWrapper.eq(ContractConcludeFieldConst.TENANT_ID, tenantId);
        queryWrapper.eq(ContractConcludeFieldConst.DELETED, 0);
        //组织权限隔离
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            queryWrapper.in(ContractConcludeFieldConst.BELONG_ORG_ID, orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class)));
        }
        // 查最上级
        IPage<ContractConcludeV> pageList = contractConcludeMapper.queryByPage(pageF, queryWrapper, tenantId);
        List<ContractConcludeV> records = pageList.getRecords();
        // 查所有子级
        List<Long> parentIdList = records.stream().map(ContractConcludeV::getId).collect(Collectors.toList());
        List<ContractConcludeV> concludeVList = contractConcludeMapper.queryByPath(queryWrapper, parentIdList, tenantId);
        BigDecimal dueSumAmount = new BigDecimal("0.00");//累计已到期金额
        //循环统计金额
        for(ContractConcludeV concludeV : concludeVList){
            if(ContractSetConst.BECOME_DUE.equals(concludeV.getContractState())){//统计已到期金额
                dueSumAmount = dueSumAmount.add(concludeV.getAmountTaxIncluded());
            }
        }
        List<Long> contractIds = records.stream().map(ContractConcludeV::getId).collect(Collectors.toList());
        ContractConcludeSumV contractConcludeSumV = contractConcludeMapper.amountSum(contractIds);
        if(ObjectUtils.isNotEmpty(contractConcludeSumV)){
            contractConcludeSumV.setDueSumAmount(dueSumAmount);
        }
        return contractConcludeSumV;
    }

    public List<ContractDetailsV> contractList() {
        return contractConcludeMapper.contractList();
    }

    public void updateContractState(Long id) {
        contractConcludeMapper.updateContractState(id);
    }

    public void contractState(Long id, Integer contractState, Integer reviewStatus) {
        ContractConcludeE map = new ContractConcludeE();
        map.setId(id);
        map.setContractState(contractState);
        map.setReviewStatus(reviewStatus);
        contractConcludeMapper.updateById(map);
    }

    public void stopContract(Long id, String reason) {
        ContractConcludeE map = new ContractConcludeE();
        map.setId(id);
        map.setSigningMethod(Const.State._6);
        map.setContractState(Const.State._4);
        map.setReason(reason);
        contractConcludeMapper.updateById(map);
        //根据id查询pid=id,且状态！=5和4的，将这些子合同变成终止中状态
        ContractConcludeF contractConcludeF = new ContractConcludeF();
        contractConcludeF.setPid(id);
        contractConcludeF.setContractState(1);
        List<ConcludeInfoV> concludeInfoV = contractConcludeMapper.listContractConclude(contractConcludeF);
        if (!CollectionUtils.isEmpty(concludeInfoV)) {
            for (ConcludeInfoV infoV : concludeInfoV) {
                ContractConcludeE map1 = new ContractConcludeE();
                map1.setId(infoV.getId());
                map1.setContractState(Const.State._4);
                contractConcludeMapper.updateById(map1);
            }
        }
    }

    public List<ContractDetailsV> expireContract(String tenantId, Long id, Boolean flag) {
        return contractConcludeMapper.expireContract(tenantId, id, flag);
    }

    public List<ContractDetailsV> contractAdvent(String tenantId, Long id, Boolean flag, Integer dayNum) {
        return contractConcludeMapper.contractAdvent(tenantId, id, flag, dayNum);
    }

    public void updateWarnState(Long id, Integer warnState) {
        contractConcludeMapper.updateWarnState(id, warnState);
    }

    public Long checkContract(Long id, Integer signingMethod) {
        return contractConcludeMapper.checkContract(id, signingMethod);
    }

    /**
     * 临时账单引用
     *
     * @param referenceState 0 解除关联 1设置关联
     */
    public Boolean reference(Long billId, Integer referenceState) {
        log.info("招投标保证金关联账单：" + billId + "-关联：" + referenceState);
        if (Objects.isNull(billId)) {
            return false;
        }
        ReferenceBillRf referenceBillRf = new ReferenceBillRf();
        referenceBillRf.setBillId(billId);
        referenceBillRf.setReferenceState(referenceState);
        //-- TODO supUnitId default
        referenceBillRf.setSupCpUnitId("default");
        return financeFeignClient.temporaryReference(referenceBillRf);
    }

    public PageV<ContractInfoV> pageContractPage(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        Page<ContractConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        List<String> stringList = new ArrayList<>();
        String dayDate = null;
        Integer method = null;
        String dayDateEnd = null;
        String dayDateStart = null;
        stringList.add("dayDate");
        QueryWrapper<ContractConcludeE> queryWrapper = request.getConditions().getQueryModel(stringList);
        request.getConditions().getFields();
        Field field = request.getConditions().getSpecialMap2().get("dayDate");
        if (field != null) {
            method = field.getMethod();
            dayDate = (String) field.getValue();
            if (method == 13) {
                dayDateEnd = (String) field.getMap().get("end");
                dayDateStart = (String) field.getMap().get("start");
            }
        }
        //客户平台需要数据隔离
        queryWrapper.eq(ContractConcludeFieldConst.TENANT_ID, tenantId);
        // 查最上级
        IPage<ContractInfoV> pageList = contractConcludeMapper.queryByContractPage(pageF, queryWrapper, tenantId, dayDate, method, dayDateStart, dayDateEnd);
        List<ContractInfoV> records = pageList.getRecords();
        // 查所有子级
        List<Long> parentIdList = records.stream().map(ContractInfoV::getId).collect(Collectors.toList());
        List<ContractInfoV> concludeVList = contractConcludeMapper.queryByContractPath(queryWrapper, parentIdList, tenantId, dayDate, method, dayDateStart, dayDateEnd);
        for (ContractInfoV contractConcludeV : concludeVList) {
            //项目名称+租户名称
            if (null != orgFeignClient.getById(contractConcludeV.getTenantId())) {
                contractConcludeV.setTenantName(orgFeignClient.getById(contractConcludeV.getTenantId()).getName());
            }
            //已存在业务表中，列表不需要查询
//            if (contractConcludeV.getCommunityId().length() < 20) {
//                if (null != orgFeignClient.getByFinanceId(Long.valueOf(contractConcludeV.getCommunityId()))) {
//                    contractConcludeV.setCostName(orgFeignClient.getByFinanceId(Long.valueOf(contractConcludeV.getCommunityId())).getNameCn());
//                }
//            }
            Integer taxpayerType = null;
            if (contractConcludeV.getContractNature().equals(ContractSetConst.INCOME)) {
                //收入类合同，甲方为客商，乙方组织架构
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.queryById(contractConcludeV.getPartyAId()).getName());
                }
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getNameCn());
                    taxpayerType = orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getTaxpayerType();
                }
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.PAY)) {
                //支出类合同，甲方为组织，乙方客商
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getNameCn());
                    taxpayerType = orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getTaxpayerType();
                }
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.queryById(contractConcludeV.getPartyBId()).getName());
                }
            }
            //年差
            if (contractConcludeV.getGmtExpireEnd() == null) {
                contractConcludeV.setGmtExpireEnd(LocalDate.parse("2029-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            int years = contractConcludeV.getGmtExpireEnd().getYear() - contractConcludeV.getGmtExpireStart().getYear();
            //月差
            int months = years * 12 + (contractConcludeV.getGmtExpireEnd().getMonthValue() - contractConcludeV.getGmtExpireStart().getMonthValue());
            contractConcludeV.setDayNum(months + 1);
            if (contractConcludeV.getSigningMethod() != null && !(ContractSetConst.SUPPLEMENT.equals(contractConcludeV.getSigningMethod()) ||
                    ContractSetConst.STOP.equals(contractConcludeV.getSigningMethod()))) {
                //收款总金额//开收票金额
                ContractInfoV contractInfoV = contractCollectionPlanMapper.selectByContract(contractConcludeV.getId());
                if (contractInfoV != null) {
                    contractConcludeV.setPaymentAmount(contractInfoV.getPaymentAmount());
                    contractConcludeV.setInvoiceAmount(contractInfoV.getInvoiceAmount());
                    contractConcludeV.setLocalCurrencyAmount(contractInfoV.getLocalCurrencyAmount());
                    if (contractInfoV.getLocalCurrencyAmount().compareTo(contractInfoV.getPaymentAmount()) > 0) {
                        contractConcludeV.setCollectionAmount(contractInfoV.getLocalCurrencyAmount().subtract(contractInfoV.getPaymentAmount()));
                    } else {
                        contractConcludeV.setCollectionAmount(new BigDecimal("0.00"));
                    }
                }
                //损益计划金额
                ContractInfoV amountByContract = contractProfitLossPlanMapper.selectAmountByContract(contractConcludeV.getId());
                if (amountByContract != null) {
                    contractConcludeV.setProfitAmount(amountByContract.getProfitTaxExcludedAmount());
                    if (taxpayerType != null && taxpayerType.equals(1)) {
                        contractConcludeV.setProfitAmount(amountByContract.getProfitLocalCurrencyAmount());
                    }
                }
                //保证金余额
                ContractInfoV amountByContract1 = contractBondPlanMapper.selectAmountByContract(contractConcludeV.getId());
                if (amountByContract1 != null) {
                    contractConcludeV.setBondBalance(amountByContract1.getBondBalance());
                }
            }
        }
        List<ContractInfoV> list = TreeUtil.treeing(concludeVList);
        return PageV.of(request, pageList.getTotal(), list);
    }

    public ContractAccountSumV accountAmountSum(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        Page<ContractConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        List<String> stringList = new ArrayList<>();
        String dayDate = null;
        Integer method = null;
        String dayDateEnd = null;
        String dayDateStart = null;
        stringList.add("dayDate");
        QueryWrapper<ContractConcludeE> queryWrapper = request.getConditions().getQueryModel(stringList);
        request.getConditions().getFields();
        Field field = request.getConditions().getSpecialMap2().get("dayDate");
        if (field != null) {
            method = field.getMethod();
            dayDate = (String) field.getValue();
            if (method == 13) {
                dayDateEnd = (String) field.getMap().get("end");
                dayDateStart = (String) field.getMap().get("start");
            }
        }
        //客户平台需要数据隔离
        queryWrapper.eq(ContractConcludeFieldConst.TENANT_ID, tenantId);
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            queryWrapper.in(ContractConcludeFieldConst.BELONG_ORG_ID, orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class)));
        }
        // 查最上级
        IPage<ContractInfoV> pageList = contractConcludeMapper.queryByContractPage(pageF, queryWrapper, tenantId, dayDate, method, dayDateStart, dayDateEnd);
        List<ContractInfoV> records = pageList.getRecords();
        // 查所有子级
        List<Long> parentIdList = records.stream().map(ContractInfoV::getId).collect(Collectors.toList());
        List<ContractInfoV> concludeVList = contractConcludeMapper.queryByContractPath(queryWrapper, parentIdList, tenantId, dayDate, method, dayDateStart, dayDateEnd);
        BigDecimal profitAmount = new BigDecimal("0.00");
        BigDecimal dueSumAmount = new BigDecimal("0.00");//累计已到期金额
        List<Long> contractIds = records.stream().map(ContractInfoV::getId).collect(Collectors.toList());
        ContractAccountSumV accountSumV = contractConcludeMapper.countContractSum(contractIds);
        for (ContractInfoV contractConcludeV : concludeVList) {
            Integer taxpayerType = null;
            if (contractConcludeV.getSigningMethod() != null && !(ContractSetConst.SUPPLEMENT.equals(contractConcludeV.getSigningMethod()) ||
                    ContractSetConst.STOP.equals(contractConcludeV.getSigningMethod()))) {
                if (contractConcludeV.getContractNature().equals(ContractSetConst.INCOME)) {
                    //收入类合同，甲方为客商，乙方组织架构
                    if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId())) {
                        taxpayerType = orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getTaxpayerType();
                    }
                }
                if (contractConcludeV.getContractNature().equals(ContractSetConst.PAY)) {
                    //支出类合同，甲方为组织，乙方客商
                    if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId())) {
                        taxpayerType = orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getTaxpayerType();
                    }
                }
                //损益计划金额
                ContractInfoV amountByContract = contractProfitLossPlanMapper.selectAmountByContract(contractConcludeV.getId());
                if (amountByContract != null) {
                    contractConcludeV.setProfitAmount(amountByContract.getProfitTaxExcludedAmount());
                    if (taxpayerType != null && taxpayerType.equals(1)) {
                        contractConcludeV.setProfitAmount(amountByContract.getProfitLocalCurrencyAmount());
                    }
                }
            }
            if (contractConcludeV.getProfitAmount() != null) {
                profitAmount = profitAmount.add(contractConcludeV.getProfitAmount());
            }
            if(ContractSetConst.BECOME_DUE.equals(contractConcludeV.getContractState())){//统计已到期金额
                dueSumAmount = dueSumAmount.add(contractConcludeV.getAmountTaxIncluded());
            }
        }
        if(ObjectUtils.isNotEmpty(accountSumV)){
            accountSumV.setDueSumAmount(dueSumAmount);
            accountSumV.setProfitAmount(profitAmount);
        }
        return accountSumV;
    }

    public void contractBackups(Long pid) {
        contractConcludeMapper.contractBackups(pid);
    }

    public List<ContractPlanV> selectContractTimeList(String tenantId) {
        //查询今天发生变更的合同以及新增的合同
        List<ContractPlanV> contractPlanVList = contractConcludeMapper.selectContractTimeList(tenantId);
        if (!CollectionUtils.isEmpty(contractPlanVList)) {
            for (ContractPlanV planV : contractPlanVList) {
                //客商
                if (null != orgFeignClient.queryById(planV.getPartyAId())) {
                    String code = orgFeignClient.queryById(planV.getPartyAId()).getCode();
                    planV.setCustomerCode(code);
                    if (!StringUtils.hasText(code)) {
                        planV.setCustomerCode("KSBH00001");
                    }
                }
                //查询合同对应的收款计划
                List<ContractBillListV> contractBillListVS = contractCollectionPlanMapper.selectByContractIds(planV.getId());
                if (!CollectionUtils.isEmpty(contractBillListVS)) {
                    for (ContractBillListV contractBillListV : contractBillListVS) {
                        contractBillListV.setInvoiceType("电子发票");
                        //费项
                        //补充身份标识
                        IdentityInfo identityInfo = new IdentityInfo();
                        identityInfo.setTenantId(tenantId);
                        ThreadLocalUtil.set("IdentityInfo", identityInfo);
                        if (null != financeFeignClient.chargeName(contractBillListV.getChargeItemId())) {
                            contractBillListV.setFeeName(financeFeignClient.chargeName(contractBillListV.getChargeItemId()));
                        }
                        if (!StringUtils.hasText(contractBillListV.getFeeName())) {
                            contractBillListV.setFeeName("费项");
                        }
                        //成本中心
                        if (null != orgFeignClient.getByFinanceId(contractBillListV.getCostId())) {
                            contractBillListV.setCostCenterName(orgFeignClient.getByFinanceId(contractBillListV.getCostId()).getNameCn());
                        }
                        //责任部门
                        if (null != orgFeignClient.getByOrgId(contractBillListV.getOrgId())) {
                            contractBillListV.setDeptName(orgFeignClient.getByOrgId(contractBillListV.getOrgId()).getOrgName());
                        }
                    }
                }
                //查询合同对应的保证金计划
                List<ContractBondListV> contractBondListVS = contractBondPlanMapper.selectByContractIds(planV.getId());
                if (!CollectionUtils.isEmpty(contractBondListVS)) {
                    for (ContractBondListV contractBondListV : contractBondListVS) {
                        //费项
                        //补充身份标识
                        IdentityInfo identityInfo = new IdentityInfo();
                        identityInfo.setTenantId(tenantId);
                        ThreadLocalUtil.set("IdentityInfo", identityInfo);
                        System.out.println("获取保证金费项名称 = " + financeFeignClient.chargeName(contractBondListV.getChargeItemId()));
                        if (StringUtils.hasText(financeFeignClient.chargeName(contractBondListV.getChargeItemId()))) {
                            contractBondListV.setFeeName(financeFeignClient.chargeName(contractBondListV.getChargeItemId()));
                        } else {
                            contractBondListV.setFeeName("费项");
                        }
                        //成本中心
                        if (null != orgFeignClient.getByFinanceId(contractBondListV.getCostId())) {
                            contractBondListV.setCostCenterName(orgFeignClient.getByFinanceId(contractBondListV.getCostId()).getNameCn());
                        }
                        //责任部门
                        if (null != orgFeignClient.getByOrgId(contractBondListV.getOrgId())) {
                            contractBondListV.setDeptName(orgFeignClient.getByOrgId(contractBondListV.getOrgId()).getOrgName());
                        }
                    }
                }
                planV.setBillList(contractBillListVS);
                planV.setBondList(contractBondListVS);
            }
        }
        return contractPlanVList;
    }


    /**
     * 提交审核
     * @param id
     * @return
     */
    public BpmContractReturnV doCommitReview(ContractDetailsV contractConclude) {
        //获取审核所需数据并进行拼装
        BpmContractReturnV bpmContractReturnV = null;
        List<ContractCollectionPlanV> collectionPlanVList = contractConclude.getCollectionPlanVList();
        List<ContractProfitLossPlanV> profitLossPlanList = contractConclude.getProfitLossPlanList();
        if (contractConclude.getContractNature().equals(ContractSetConst.PAY)) {
            BpmContractExpendF map = Global.mapperFacade.map(contractConclude, BpmContractExpendF.class);
            if (!CollectionUtils.isEmpty(collectionPlanVList)) {
                map.setCollectionPlanList(Global.mapperFacade.mapAsList(collectionPlanVList, BpmContractCollectionPlanF.class));
            }
            if (!CollectionUtils.isEmpty(profitLossPlanList)) {
                map.setProfitLossPlanList(Global.mapperFacade.mapAsList(profitLossPlanList, BpmContractProfitLossPlanF.class));
            }
            map.setBpmContractBodyList(getContractBody(contractConclude.getPartyAName(), contractConclude.getPartyBName(), contractConclude.getPartyCName()));
            bpmContractReturnV = externalFeignClient.expendApply(map);
        } else if (contractConclude.getContractNature().equals(ContractSetConst.INCOME)) {
            BpmContractNoExpendF map = Global.mapperFacade.map(contractConclude, BpmContractNoExpendF.class);
            if (!CollectionUtils.isEmpty(collectionPlanVList)) {
                map.setCollectionPlanList(Global.mapperFacade.mapAsList(collectionPlanVList, BpmContractCollectionPlanF.class));
            }
            if (!CollectionUtils.isEmpty(profitLossPlanList)) {
                map.setProfitLossPlanList(Global.mapperFacade.mapAsList(profitLossPlanList, BpmContractProfitLossPlanF.class));
            }
            map.setBpmContractBodyList(getContractBody(contractConclude.getPartyAName(), contractConclude.getPartyBName(), contractConclude.getPartyCName()));
            bpmContractReturnV = externalFeignClient.noExpendApply(map);
        }
        return bpmContractReturnV;
    }

    public List<BpmContractBodyF> getContractBody(String partyAName,String partyBName,String partyCName){
        List<BpmContractBodyF> bpmContractBodyList = new ArrayList<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(partyAName)){
            BpmContractBodyF bpmContractBodyF = new BpmContractBodyF();
            bpmContractBodyF.setRole("甲方");
            bpmContractBodyF.setBodyName(partyAName);
            bpmContractBodyList.add(bpmContractBodyF);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(partyAName)){
            BpmContractBodyF bpmContractBodyF = new BpmContractBodyF();
            bpmContractBodyF.setRole("乙方");
            bpmContractBodyF.setBodyName(partyBName);
            bpmContractBodyList.add(bpmContractBodyF);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(partyAName)){
            BpmContractBodyF bpmContractBodyF = new BpmContractBodyF();
            bpmContractBodyF.setRole("丙方");
            bpmContractBodyF.setBodyName(partyCName);
            bpmContractBodyList.add(bpmContractBodyF);
        }
        return bpmContractBodyList;
    }

    public Boolean checkContractNo(ContractConcludeF contractConcludeF) {
        Boolean check = true;
        QueryWrapper<ContractConcludeE> concludeEQuery = new QueryWrapper<>();
        concludeEQuery.eq("contractNo",contractConcludeF.getContractNo());
        concludeEQuery.eq("deleted",0);
        if(ObjectUtils.isNotEmpty(contractConcludeF.getId())){
            concludeEQuery.ne("id",contractConcludeF.getId());
        }
        Long count = contractConcludeMapper.selectCount(concludeEQuery);
        if(count > 0){
            throw BizException.throw400("该合同编号已重复，需要修改！");
        }
        return check;
    }
}
