package com.wishare.finance.apps.service.configure.businessunit;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.businessunit.fo.*;
import com.wishare.finance.apps.model.configure.businessunit.vo.*;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountV;
import com.wishare.finance.apps.model.fangyuan.vo.BusinessUnitSyncDto;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountBankDto;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountDto;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitDetailCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.DeletedBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.UpdateBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitDetailE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.enums.BusinessDetailTypeEnum;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitDetailRepository;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.domains.configure.businessunit.service.BusinessUnitDomainService;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrg;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgInfoRv;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务单元应用服务
 *
 * @author
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessUnitAppService implements ApiBase {

    private final BusinessUnitDomainService businessUnitDomainService;
    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitDetailRepository businessUnitDetailRepository;

    @Autowired
    protected AccountOrgFacade accountOrgFacade;

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    @Autowired
    private AssisteOrgRepository assisteOrgRepository;

    @Autowired
    private StatutoryBodyAccountRepository statutoryBodyAccountRepository;

    /**
     * 分页查询业务单元树
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public PageV<BusinessUnitV> queryByTreePage(PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        Page<BusinessUnitE> queryByPage = businessUnitDomainService.queryByTreePage(queryBusinessUnitPageF);
        List<BusinessUnitE> records = queryByPage.getRecords();
        List<BusinessUnitV> businessUnitVList = Global.mapperFacade.mapAsList(records, BusinessUnitV.class);
        //组装树结构
        for (BusinessUnitV businessUnitV : businessUnitVList) {
            if (businessUnitV.getParentId() != null) {
                businessUnitV.setPid(businessUnitV.getParentId());
            }
        }
        List<BusinessUnitV> treeing = TreeUtil.treeing(businessUnitVList);
        return PageV.of(queryBusinessUnitPageF, queryByPage.getTotal(), treeing);
    }

    /**
     * 启用业务单元
     *
     * @param id id
     * @return Boolean
     */
    public Boolean enable(Long id) {
        UpdateBusinessUnitF updateBusinessUnitF = new UpdateBusinessUnitF();
        updateBusinessUnitF.setId(id);
        updateBusinessUnitF.setDisabled(DataDisabledEnum.启用.getCode());
        UpdateBusinessUnitCommand command = updateBusinessUnitF.getUpdateBusinessUnitCommand(curIdentityInfo());
        return businessUnitDomainService.enableOrDisabled(command);
    }

    /**
     * 禁用业务单元
     *
     * @param id id
     * @return Boolean
     */
    public Boolean disable(Long id) {
        UpdateBusinessUnitF updateBusinessUnitF = new UpdateBusinessUnitF();
        updateBusinessUnitF.setId(id);
        updateBusinessUnitF.setDisabled(DataDisabledEnum.禁用.getCode());
        UpdateBusinessUnitCommand command = updateBusinessUnitF.getUpdateBusinessUnitCommand(curIdentityInfo());
        return businessUnitDomainService.enableOrDisabled(command);
    }

    /**
     * 删除业务单元
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        List<BusinessUnitE> list = businessUnitDomainService.getList(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getParentId, id));
        if (CollectionUtils.isNotEmpty(list)){
            throw BizException.throw400("该业务单元存在子节点，无法删除");
        }
        DeletedBusinessUnitCommand command = new DeletedBusinessUnitCommand();
        command.setId(id);
        command.setTenantId(curIdentityInfo().getTenantId());
        return businessUnitDomainService.delete(command);
    }

    /**
     * 树查询业务单元树
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public List<BusinessUnitV> queryByTree(SearchF<BusinessUnitE> queryBusinessUnitPageF) {
        List<BusinessUnitE> queryByTree = businessUnitDomainService.queryByTree(queryBusinessUnitPageF);
        List<BusinessUnitV> businessUnitVList = Global.mapperFacade.mapAsList(queryByTree, BusinessUnitV.class);
        //组装树结构
        for (BusinessUnitV businessUnitV : businessUnitVList) {
            if (businessUnitV.getParentId() != null) {
                businessUnitV.setPid(businessUnitV.getParentId());
            }
        }
        List<BusinessUnitV> treeing = TreeUtil.treeing(businessUnitVList);
        return treeing;
    }

    /**
     * 分页查询业务单元
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public PageV<BusinessUnitListV> queryByListPage(PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        Page<BusinessUnitE> queryByPage = businessUnitDomainService.queryByListPage(queryBusinessUnitPageF);
        List<BusinessUnitE> records = queryByPage.getRecords();
        List<BusinessUnitListV> businessUnitVList = Global.mapperFacade.mapAsList(records, BusinessUnitListV.class);

        List<Long> businessId = records.stream().map(BusinessUnitE::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(businessId)){
            List<BusinessUnitDetailE> businessUnitDetailEList = businessUnitDetailRepository.list(new LambdaQueryWrapper<BusinessUnitDetailE>().in(BusinessUnitDetailE::getBusinessUnitId, businessId));
            if (CollectionUtils.isNotEmpty(businessUnitDetailEList)){
                //法定单位列表
                OrgFinanceF orgFinanceF = new OrgFinanceF();
                orgFinanceF.setFinanceType("CORPORATOR_ITSELF");
                List<StatutoryBodyRv> list = accountOrgFacade.getOrgFinanceList(orgFinanceF);
                Map<Long, List<StatutoryBodyRv>> listcollect = list.stream().collect(Collectors.groupingBy(StatutoryBodyRv::getId));
                //成本中心
                orgFinanceF.setFinanceType("COST");
                List<StatutoryBodyRv> orgFinanceList = orgClient.getOrgFinanceList(orgFinanceF);
                Map<Long, List<StatutoryBodyRv>> orgFinanceListcollect = orgFinanceList.stream().collect(Collectors.groupingBy(StatutoryBodyRv::getId));
                //行政组织
                List<OrgInfoRv> orgInfoRvs = new ArrayList<>();
                Map<Integer, List<BusinessUnitDetailE>> typeCollection = businessUnitDetailEList.stream().collect(Collectors.groupingBy(BusinessUnitDetailE::getType));
                if (typeCollection!=null &&typeCollection.get(BusinessDetailTypeEnum.行政组织.getCode())!=null){
                    List<Long> collect = typeCollection.get(BusinessDetailTypeEnum.行政组织.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList());
                    Map<String, Object> stringObjectHashMap = new HashMap<>();
                    stringObjectHashMap.put("orgIds",collect);
                    orgInfoRvs = orgClient.getOrgListByOrgIds(stringObjectHashMap);
                }
                Map<Long, List<OrgInfoRv>> orgInfoListcollect = orgInfoRvs.stream().collect(Collectors.groupingBy(OrgInfoRv::getId));
                //银行账户
                List<StatutoryBodyAccountE> statutoryBodyAccountEList = statutoryBodyAccountRepository.list();
                List<StatutoryBodyAccountV> statutoryBodyAccountVS = Global.mapperFacade.mapAsList(statutoryBodyAccountEList, StatutoryBodyAccountV.class);
                Map<Long, List<StatutoryBodyAccountV>> accountListCollect = statutoryBodyAccountVS.stream().collect(Collectors.groupingBy(StatutoryBodyAccountV::getId));

                Map<Long, Map<Integer, List<BusinessUnitDetailE>>> collect1 = businessUnitDetailEList.stream().collect(Collectors.groupingBy(BusinessUnitDetailE::getBusinessUnitId, Collectors.groupingBy(BusinessUnitDetailE::getType)));
                for (BusinessUnitListV businessUnitListV : businessUnitVList) {
                    Map<Integer, List<BusinessUnitDetailE>> integerListMap = collect1.get(businessUnitListV.getId());
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.法定单位.getCode())!=null){
                        businessUnitListV.setLegalIds(integerListMap.get(BusinessDetailTypeEnum.法定单位.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.成本中心.getCode())!=null){
                        businessUnitListV.setCostIds(integerListMap.get(BusinessDetailTypeEnum.成本中心.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.行政组织.getCode())!=null){
                        businessUnitListV.setOrgIds(integerListMap.get(BusinessDetailTypeEnum.行政组织.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.银行账号.getCode())!=null){
                        businessUnitListV.setStatutoryBodyAccountId(integerListMap.get(BusinessDetailTypeEnum.银行账号.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    for (BusinessUnitDetailE businessUnitDetailE : businessUnitDetailEList) {
                        if (businessUnitListV.getId().equals(businessUnitDetailE.getBusinessUnitId())
                                &&BusinessDetailTypeEnum.法定单位.getCode().equals(businessUnitDetailE.getType())
                                &&CollectionUtils.isNotEmpty(listcollect.get(businessUnitDetailE.getRelevanceId()))){
                            businessUnitListV.getLegalArray().addAll(listcollect.get(businessUnitDetailE.getRelevanceId()));
                        }
                        if (businessUnitListV.getId().equals(businessUnitDetailE.getBusinessUnitId())
                                &&BusinessDetailTypeEnum.成本中心.getCode().equals(businessUnitDetailE.getType())
                                &&CollectionUtils.isNotEmpty(orgFinanceListcollect.get(businessUnitDetailE.getRelevanceId()))){
                            businessUnitListV.getCostArray().addAll(orgFinanceListcollect.get(businessUnitDetailE.getRelevanceId()));
                        }
                        if (businessUnitListV.getId().equals(businessUnitDetailE.getBusinessUnitId())
                                &&BusinessDetailTypeEnum.行政组织.getCode().equals(businessUnitDetailE.getType())
                                &&CollectionUtils.isNotEmpty(orgInfoListcollect.get(businessUnitDetailE.getRelevanceId()))){
                            businessUnitListV.getOrgArray().addAll(orgInfoListcollect.get(businessUnitDetailE.getRelevanceId()));
                        }
                        if (businessUnitListV.getId().equals(businessUnitDetailE.getBusinessUnitId())
                                &&BusinessDetailTypeEnum.银行账号.getCode().equals(businessUnitDetailE.getType())
                                &&CollectionUtils.isNotEmpty(accountListCollect.get(businessUnitDetailE.getRelevanceId()))){
                            businessUnitListV.getAccountArray().addAll(accountListCollect.get(businessUnitDetailE.getRelevanceId()));
                        }
                    }
                }
            }
        }
        List<Long> partnerBusinessId = records.stream().map(BusinessUnitE::getParentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(partnerBusinessId)){
            List<BusinessUnitE> partnerList = businessUnitDomainService.getByWrapper(new LambdaQueryWrapper<BusinessUnitE>().in(BusinessUnitE::getId, partnerBusinessId));
            a: for (BusinessUnitListV businessUnitListV : businessUnitVList) {
                for (BusinessUnitE businessUnitE : partnerList) {
                    if (businessUnitE.getId().equals(businessUnitListV.getParentId())){
                        businessUnitListV.setParentName(businessUnitE.getName());
                        businessUnitListV.setParentCode(businessUnitE.getCode());
                        continue a;
                    }
                }
            }
        }
        return PageV.of(queryBusinessUnitPageF, queryByPage.getTotal(), businessUnitVList);
    }

    /**
     * 查询业务单元
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public List<BusinessUnitListV> queryByList(SearchF<BusinessUnitE> queryBusinessUnitPageF) {
        List<BusinessUnitE> businessUnitEList = businessUnitDomainService.queryByTree(queryBusinessUnitPageF);
        List<BusinessUnitListV> businessUnitVList = Global.mapperFacade.mapAsList(businessUnitEList, BusinessUnitListV.class);
        return businessUnitVList;
    }

    /**
     * 根据id查询业务单元信息
     *
     * @param id
     * @return
     */
    public BusinessUnitV getById(Long id) {
        BusinessUnitE businessUnitE = businessUnitDomainService.queryByDetailId(id);
        return Global.mapperFacade.map(businessUnitE, BusinessUnitV.class);
    }

    /**
     * 根据name查询业务单元信息
     *
     * @param name
     * @return
     */
    public BusinessUnitV getByName(String name) {
        BusinessUnitE businessUnitE = businessUnitDomainService.getOne(new LambdaQueryWrapper<BusinessUnitE>(
        ).eq(BusinessUnitE::getName,name).eq(BusinessUnitE::getDeleted, DataDeletedEnum.NORMAL.getCode()));
        return Global.mapperFacade.map(businessUnitE, BusinessUnitV.class);
    }

    /**
     * 查询详情
     *
     * @param id id
     * @return BusinessUnitListV
     */
    public BusinessUnitListV queryByDetailId(Long id) {
        log.info("查询id：{}",id);
        BusinessUnitE businessUnitE = businessUnitDomainService.queryByDetailId(id);
        if (businessUnitE==null){
            return null;
        }
        BusinessUnitListV businessUnitListV = Global.mapperFacade.map(businessUnitE, BusinessUnitListV.class);
        List<BusinessUnitDetailE> businessUnitDetailEList = businessUnitDetailRepository.list(new LambdaQueryWrapper<BusinessUnitDetailE>().in(BusinessUnitDetailE::getBusinessUnitId, businessUnitE.getId()));
        if (CollectionUtils.isNotEmpty(businessUnitDetailEList)){
            Map<Integer, List<BusinessUnitDetailE>> collect = businessUnitDetailEList.stream().collect(Collectors.groupingBy(BusinessUnitDetailE::getType));
            if (collect!=null&&collect.get(BusinessDetailTypeEnum.法定单位.getCode())!=null){
                businessUnitListV.setLegalIds(collect.get(BusinessDetailTypeEnum.法定单位.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
            }
            if (collect!=null&&collect.get(BusinessDetailTypeEnum.成本中心.getCode())!=null){
                businessUnitListV.setCostIds(collect.get(BusinessDetailTypeEnum.成本中心.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
            }
            if (collect!=null&&collect.get(BusinessDetailTypeEnum.行政组织.getCode())!=null){
                businessUnitListV.setOrgIds(collect.get(BusinessDetailTypeEnum.行政组织.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
            }
            if (collect!=null&&collect.get(BusinessDetailTypeEnum.银行账号.getCode())!=null){
                businessUnitListV.setStatutoryBodyAccountId(collect.get(BusinessDetailTypeEnum.银行账号.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
            }
        }
        if (businessUnitE!=null && businessUnitE.getParentId()>0){
            BusinessUnitE parter = businessUnitDomainService.queryByDetailId(businessUnitE.getParentId());
            businessUnitListV.setParentName(parter.getName());
            businessUnitListV.setParentCode(parter.getCode());
        }
        return businessUnitListV;
    }

    /**
     * 修改
     */
    @Transactional
    public Boolean update(UpdateBusinessUnitF updateBusinessUnitF){
        //判断code是否重复
        if (updateBusinessUnitF.getParentId()!=null&&updateBusinessUnitF.getId().equals(updateBusinessUnitF.getParentId())){
            throw BizException.throw400("业务单元不可选择自己作为父节点");
        }
        BusinessUnitE businessUnitE = businessUnitDomainService.getOne(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getCode, updateBusinessUnitF.getCode()));
        if (businessUnitE!=null&&!businessUnitE.getId().equals(updateBusinessUnitF.getId())){
            throw BizException.throw400("该编码已存在");
        }
        UpdateBusinessUnitCommand command = updateBusinessUnitF.getUpdateBusinessUnitCommand(curIdentityInfo());
        BusinessUnitE newBusinessUnitE = Global.mapperFacade.map(command, BusinessUnitE.class);
        BusinessUnitE oldE = businessUnitDomainService.getOne(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getId, updateBusinessUnitF.getId()));
        if (!oldE.getParentId().equals(updateBusinessUnitF.getParentId())){
            List<BusinessUnitE> existBusinessUnitE = businessUnitDomainService.getByWrapper(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getParentId, updateBusinessUnitF.getId()));
            if (CollectionUtils.isNotEmpty(existBusinessUnitE)) {
                throw BizException.throw400("业务单元存在子节点,无法调整其父节点");
            }
        }
        if (oldE.getLastLevel() ==1 && (!StringUtils.equals(oldE.getCode(),updateBusinessUnitF.getCode()) || !StringUtils.equals(oldE.getName(),updateBusinessUnitF.getName()))){
            throw BizException.throw400("该数据属于其他系统同步数据，不支持修改code或name");
        }
        if (updateBusinessUnitF.getParentId() == null || updateBusinessUnitF.getParentId() <= 0){
            newBusinessUnitE.setPath(JSON.toJSONString(Collections.singletonList(updateBusinessUnitF.getId())));
            newBusinessUnitE.setParentId(0L);
        }else {
            BusinessUnitE partnerBusinessUnitE = businessUnitDomainService.getOne(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getId, updateBusinessUnitF.getParentId()));
            String path = partnerBusinessUnitE.getPath();
            List<Long> longs = JSONArray.parseArray(path, Long.class);
            longs.add(updateBusinessUnitF.getId());
            newBusinessUnitE.setPath(JSON.toJSONString(longs));
        }
        Boolean update = businessUnitDomainService.update(newBusinessUnitE);
        if (!update){
            throw BizException.throw400("修改业务单元失败");
        }
        businessUnitDetailRepository.remove(new LambdaQueryWrapper<BusinessUnitDetailE>().eq(BusinessUnitDetailE::getBusinessUnitId, updateBusinessUnitF.getId()));
        //新加映射表
        Boolean add=true;
        if (CollectionUtils.isNotEmpty(updateBusinessUnitF.getLegalIds())){
            add = addDetail(updateBusinessUnitF.getId(), updateBusinessUnitF.getLegalIds(), BusinessDetailTypeEnum.法定单位.getCode());
            if (!add){
                throw BizException.throw400("新增业务单元关联法定单位失败");
            }
        }
        if (CollectionUtils.isNotEmpty(updateBusinessUnitF.getCostIds())){
            add = addDetail(updateBusinessUnitF.getId(), updateBusinessUnitF.getCostIds(), BusinessDetailTypeEnum.成本中心.getCode());
            if (!add){
                throw BizException.throw400("新增业务单元关联成本中心失败");
            }
        }
        if (CollectionUtils.isNotEmpty(updateBusinessUnitF.getOrgIds())){
            add = addDetail(updateBusinessUnitF.getId(), updateBusinessUnitF.getOrgIds(), BusinessDetailTypeEnum.行政组织.getCode());
            if (!add){
                throw BizException.throw400("新增业务单元关联行政组织失败");
            }
        }
        if (CollectionUtils.isNotEmpty(updateBusinessUnitF.getStatutoryBodyAccountId())){
            add = addDetail(updateBusinessUnitF.getId(), updateBusinessUnitF.getStatutoryBodyAccountId(), BusinessDetailTypeEnum.银行账号.getCode());
            if (!add){
                throw BizException.throw400("新增业务单元关联银行账号失败");
            }
        }
        return true;
    }

    /**
     * 新加
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessUnitSyncDto syncBusinessDataAddOrUpdate(CreateBusinessUnitF createBusinessUnitF) {
        //判断code是否重复
        BusinessUnitE exist = businessUnitRepository.getOne(new LambdaQueryWrapper<BusinessUnitE>()
                .select(BusinessUnitE::getId,BusinessUnitE::getName,BusinessUnitE::getCode,BusinessUnitE::getPath)
                .eq(BusinessUnitE::getCode, createBusinessUnitF.getCode()));
        // 新增
        if (exist == null) {
            CreateBusinessUnitCommand command = createBusinessUnitF.getCreateBusinessUnitCommand(curIdentityInfo());
            BusinessUnitE addModel = Global.mapperFacade.map(command, BusinessUnitE.class);
            // 设置path
            handleAddModelPath(createBusinessUnitF, addModel, command);

            businessUnitDomainService.create(addModel);

            // 处理 business_unit_detail 表数据
            String s = handleBusinessUnitDetail(createBusinessUnitF, addModel.getId());
            log.info("处理business_unit_detail返回:{}",s);

            return BusinessUnitSyncDto.builder().id(addModel.getId()).code(addModel.getCode()).name(addModel.getName())
                    .path(JSONArray.parseArray(addModel.getPath(), Long.class)).build();
        }
        else {
            businessUnitRepository.update(new BusinessUnitE(),
                    Wrappers.<BusinessUnitE>lambdaUpdate().eq(BusinessUnitE::getId,exist.getId())
                            .set(BusinessUnitE::getName,createBusinessUnitF.getName())
            );

            return BusinessUnitSyncDto.builder().id(exist.getId()).code(exist.getCode()).name(createBusinessUnitF.getName())
                    .path(JSONArray.parseArray(exist.getPath(), Long.class)).build();
        }

    }

    /**处理 business_unit_detail 关联表数据
     * @param createBusinessUnitF
     * @param businessUnitId business_unit#id
     * @return
     */
    private String handleBusinessUnitDetail(CreateBusinessUnitF createBusinessUnitF, Long businessUnitId) {
        //新加映射表
        boolean add = true;
        if (CollectionUtils.isNotEmpty(createBusinessUnitF.getLegalIds())) {
            add = addDetail(businessUnitId, createBusinessUnitF.getLegalIds(), BusinessDetailTypeEnum.法定单位.getCode());
            if (!add) {
                return("新增业务单元关联法定单位失败");
            }
        }
        if (CollectionUtils.isNotEmpty(createBusinessUnitF.getCostIds())) {
            add = addDetail(businessUnitId, createBusinessUnitF.getCostIds(), BusinessDetailTypeEnum.成本中心.getCode());
            if (!add) {
                return("新增业务单元关联成本中心失败");
            }
        }
        if (CollectionUtils.isNotEmpty(createBusinessUnitF.getOrgIds())) {
            add = addDetail(businessUnitId, createBusinessUnitF.getOrgIds(), BusinessDetailTypeEnum.行政组织.getCode());
            if (!add) {
                return("新增业务单元关联行政组织失败");
            }
        }
        if (CollectionUtils.isNotEmpty(createBusinessUnitF.getStatutoryBodyAccountId())) {
            add = addDetail(businessUnitId, createBusinessUnitF.getStatutoryBodyAccountId(), BusinessDetailTypeEnum.银行账号.getCode());
            if (!add) {
                return("新增业务单元关联银行账号失败");
            }
        }
        return businessUnitId.toString();
    }

    private void handleAddModelPath(CreateBusinessUnitF createBusinessUnitF, BusinessUnitE addModel, CreateBusinessUnitCommand command) {
        if (createBusinessUnitF.getParentId() == null || createBusinessUnitF.getParentId() <= 0) {
            addModel.setPath(JSON.toJSONString(Collections.singletonList(command.getId())));
            addModel.setParentId(0L);
        } else {
            BusinessUnitE pModel = businessUnitDomainService.getOne(Wrappers.<BusinessUnitE>lambdaQuery()
                    .select(BusinessUnitE::getPath)
                    .eq(BusinessUnitE::getId, command.getParentId()));

            if (ObjectUtil.isNotNull(pModel)){
                List<Long> longs = JSONArray.parseArray(pModel.getPath(), Long.class);
                longs.add(command.getId());
                addModel.setPath(JSON.toJSONString(longs));
            }else {
                addModel.setPath(JSON.toJSONString(Collections.singletonList(command.getId())));
            }
        }
    }

    private boolean addDetail(Long id,List<Long> relevanceIds,Integer type){
        List<BusinessUnitDetailE> businessUnitDetailES = new ArrayList<>();
        for (Long relevanceId : relevanceIds) {
            CreateBusinessUnitDetailF createBusinessUnitDetailF = new CreateBusinessUnitDetailF();
            createBusinessUnitDetailF.setBusinessUnitId(id);
            createBusinessUnitDetailF.setRelevanceId(relevanceId);
            createBusinessUnitDetailF.setType(type);
            CreateBusinessUnitDetailCommand createBusinessUnitDetailCommand = createBusinessUnitDetailF.getCreateBusinessUnitDetailCommand(curIdentityInfo());
            BusinessUnitDetailE businessUnitDetailE = Global.mapperFacade.map(createBusinessUnitDetailCommand, BusinessUnitDetailE.class);
            businessUnitDetailES.add(businessUnitDetailE);
        }
        return businessUnitDetailRepository.saveBatch(businessUnitDetailES);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<BusinessUnitSyncDto> push(String code, String name){

        if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
            throw new BizException(400, "查询参数为空，不支持刷新");
        }

        ArrayList<BusinessUnitSyncDto> vo = new ArrayList<>();
        List<AssisteOrg> list = assisteOrgRepository.list(new LambdaQueryWrapper<AssisteOrg>()
                .eq(StringUtils.isNotBlank(code), AssisteOrg::getAscCode, code)
                .eq(StringUtils.isNotBlank(name), AssisteOrg::getAscName, name));
        if (CollectionUtils.isNotEmpty(list)){
            for (AssisteOrg assisteOrg : list) {
                vo.add(syncBusinessDataAddOrUpdate(CreateBusinessUnitF.builder().code(assisteOrg.getCode()).name(assisteOrg.getName()).lastLevel(1).build()));

            }
        }

        return vo;
    }

    public List<BusinessUnitListV> queryBusinessUnitList(QueryBusinessUnitF queryBusinessUnitF) {
        if (queryBusinessUnitF==null){
            return null;
        }
        LambdaQueryWrapper<BusinessUnitE> businessUnitELambdaQueryWrapper = new LambdaQueryWrapper<>();
        businessUnitELambdaQueryWrapper
                .eq(StringUtils.isNotBlank(queryBusinessUnitF.getCode()),BusinessUnitE::getCode,queryBusinessUnitF.getCode())
                .in(CollectionUtils.isNotEmpty(queryBusinessUnitF.getCodes()),BusinessUnitE::getCode,queryBusinessUnitF.getCodes())
                .eq(StringUtils.isNotBlank(queryBusinessUnitF.getName()),BusinessUnitE::getName,queryBusinessUnitF.getName())
                .eq(queryBusinessUnitF.getDisabled()!=null,BusinessUnitE::getDisabled,queryBusinessUnitF.getDisabled())
                .eq(queryBusinessUnitF.getId()!=null,BusinessUnitE::getId,queryBusinessUnitF.getId())
                .in(CollectionUtils.isNotEmpty(queryBusinessUnitF.getIds()),BusinessUnitE::getId,queryBusinessUnitF.getIds())
                .eq(StringUtils.isNotBlank(queryBusinessUnitF.getTenantId()),BusinessUnitE::getTenantId,queryBusinessUnitF.getTenantId());
        List<BusinessUnitE> list = businessUnitDomainService.getList(businessUnitELambdaQueryWrapper);

        List<BusinessUnitListV> businessUnitVList = Global.mapperFacade.mapAsList(list, BusinessUnitListV.class);
        if(CollectionUtils.isNotEmpty(businessUnitVList) && queryBusinessUnitF.getContainDetail()!=null && queryBusinessUnitF.getContainDetail()){
            List<Long> businessIds = list.stream().map(BusinessUnitE::getId).collect(Collectors.toList());
            List<BusinessUnitDetailE> businessUnitDetailEList = businessUnitDetailRepository.list(new LambdaQueryWrapper<BusinessUnitDetailE>().in(BusinessUnitDetailE::getBusinessUnitId, businessIds));
            if (CollectionUtils.isNotEmpty(businessUnitDetailEList)){
                Map<Long, Map<Integer, List<BusinessUnitDetailE>>> collect1 = businessUnitDetailEList.stream().collect(Collectors.groupingBy(BusinessUnitDetailE::getBusinessUnitId, Collectors.groupingBy(BusinessUnitDetailE::getType)));
                for (BusinessUnitListV businessUnitListV : businessUnitVList) {
                    Map<Integer, List<BusinessUnitDetailE>> integerListMap = collect1.get(businessUnitListV.getId());
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.法定单位.getCode())!=null){
                        businessUnitListV.setLegalIds(integerListMap.get(BusinessDetailTypeEnum.法定单位.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.成本中心.getCode())!=null){
                        businessUnitListV.setCostIds(integerListMap.get(BusinessDetailTypeEnum.成本中心.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.行政组织.getCode())!=null){
                        businessUnitListV.setOrgIds(integerListMap.get(BusinessDetailTypeEnum.行政组织.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                    if (integerListMap!=null&&integerListMap.get(BusinessDetailTypeEnum.银行账号.getCode())!=null){
                        businessUnitListV.setStatutoryBodyAccountId(integerListMap.get(BusinessDetailTypeEnum.银行账号.getCode()).stream().map(BusinessUnitDetailE::getRelevanceId).collect(Collectors.toList()));
                    }
                }
            }
        }
        return businessUnitVList;
    }

    public Boolean detailCreate(AddBusinessDetailUnitF addBusinessDetailUnitF) {
        BusinessUnitE one = businessUnitDomainService.getOne(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getId, addBusinessDetailUnitF.getId()));
        if (one==null){
            return false;
        }
        if (CollectionUtils.isNotEmpty(addBusinessDetailUnitF.getStatutoryBodyAccountId())){
            List<BusinessUnitDetailE> list = businessUnitDetailRepository.list(new LambdaQueryWrapper<BusinessUnitDetailE>()
                    .eq(BusinessUnitDetailE::getBusinessUnitId,addBusinessDetailUnitF.getId())
                    .in(BusinessUnitDetailE::getRelevanceId, addBusinessDetailUnitF.getStatutoryBodyAccountId())
                    .eq(BusinessUnitDetailE::getType, BusinessDetailTypeEnum.银行账号.getCode()));
            if (CollectionUtils.isNotEmpty(list)){
                businessUnitDetailRepository.removeBatchByIds(list);
            }
            return addDetail(addBusinessDetailUnitF.getId(), addBusinessDetailUnitF.getStatutoryBodyAccountId(),BusinessDetailTypeEnum.银行账号.getCode());
        }
        return true;
    }

    public List<BusinessUnitAccountV> queryBusinessUnitWithAccountByStatutoryBodysId(Long id) {
        List<Long> unitIds = businessUnitDetailRepository.queryBusinessUnitIdByRelevanceId(id);
        if(CollectionUtils.isEmpty(unitIds)){
            return new ArrayList<>();
        }
        List<BusinessUnitAccountV> businessUnitAccountVs = businessUnitDetailRepository.queryAccountByUnitId(unitIds);
        return businessUnitAccountVs;
    }

    /**
     * 根据传入的法定单位ID查询出与该法定单位关联的业务单元，再将这些业务单元的银行账号数据检索出来返回以供前端选择
     * @param id 法定单位ID
     * @return 银行账号数据
     */
    public List<BusinessUnitAccountV> queryBusinessUnitWithAccountByStatutoryBodysIdRemake(Long id) {
        List<BusinessUnitAccountV> result = new ArrayList<>();

        PageF<SearchF<BusinessUnitE>> pageF = new PageF<>();
        pageF.setPageNum(1);
        pageF.setPageSize(1000L);
        SearchF<BusinessUnitE> searchF = new SearchF<BusinessUnitE>();
        searchF.setFields(new ArrayList<>());
        pageF.setConditions(searchF);
        log.info("手动拼接PageF<SearchF<BusinessUnitE>>->:{}", pageF);

        PageV<BusinessUnitListV> unitListVPageV = queryByListPage(pageF);
        if (Objects.isNull(unitListVPageV) || CollectionUtils.isEmpty(unitListVPageV.getRecords())) {
            return new ArrayList<>();
        }

        Map<String, String> demoMap = new HashMap<>();//-- 标记去重
        //-- 将关联了传入法定单位的业务单元的银行账号数据加入结果集中
        for (BusinessUnitListV record : unitListVPageV.getRecords()) {
            Boolean isRelation = false;
            if (CollectionUtils.isNotEmpty(record.getLegalIds())) {
                for (Long legalId : record.getLegalIds()) {
                    if (Objects.equals(legalId, id)) {
                        isRelation = true;
                        break;
                    }
                }
            } else {
                continue;
            }

            if (isRelation) {
                if (CollectionUtils.isNotEmpty(record.getAccountArray())) {
                    List<BusinessUnitAccountV> accountVS = Global.mapperFacade.mapAsList(record.getAccountArray(), BusinessUnitAccountV.class);
                    for (BusinessUnitAccountV accountV : accountVS) {
                        String s = demoMap.get(accountV.getBankAccount() + "" + accountV.getBankName());
                        if (StringUtils.isBlank(s)) {
                            //-- 说明没有添加过
                            demoMap.put(accountV.getBankAccount() + "" + accountV.getBankName(), "---");
                            accountV.setSbAccountId(accountV.getId());
                            result.add(accountV);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 根据法定单位id查询业务单元以及银行信息
     *
     * @param id 关联id
     * @return {@link List}<>{@link BusinessUnitStatutoryV}</>
     */
    public List<BusinessUnitStatutoryV> queryBusinessUnitWithByStatutoryBodysId(Long id) {
        List<BusinessUnitAccountDto> unitAccountDtos = businessUnitDomainService.queryBusinessUnitWithByStatutoryBodysId(id);
        if (CollectionUtils.isNotEmpty(unitAccountDtos)){
            // 查询该批次业务单元的收款单元信息
            List<BusinessUnitStatutoryV> businessUnitStatutoryV = Global.mapperFacade.mapAsList(unitAccountDtos, BusinessUnitStatutoryV.class);
            for (BusinessUnitStatutoryV unitAccountDto : businessUnitStatutoryV) {
                if (Objects.nonNull(unitAccountDto.getBusinessUnitId())){
                    List<BusinessUnitAccountBankDto> result =  businessUnitDomainService.queryBusinessUnitAccount(unitAccountDto.getBusinessUnitId());
                    List<BusinessUnitAccountBankV> bankVList = Global.mapperFacade.mapAsList(result, BusinessUnitAccountBankV.class);
                    unitAccountDto.setAccountList(bankVList);
                }
            }
            //D7604需求，远洋要把凭证测试业务单元放在第一位
            if (EnvConst.YUANYANG.equals(EnvData.config)){
                for (int i = 0;  i < businessUnitStatutoryV.size();i++){
                    if("凭证测试业务单元".equals(businessUnitStatutoryV.get(i).getBusinessUnitName()) ||
                            businessUnitStatutoryV.get(i).getBusinessUnitId().equals(166642308162501L)){
                        Collections.swap(businessUnitStatutoryV,0,i);
                        break;
                    }
                }
            }
            return businessUnitStatutoryV;
        }
        return Lists.newArrayList();
    }
}
