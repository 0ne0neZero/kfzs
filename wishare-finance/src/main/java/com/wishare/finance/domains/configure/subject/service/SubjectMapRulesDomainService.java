package com.wishare.finance.domains.configure.subject.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.subject.fo.BatchSettingPageF;
import com.wishare.finance.apps.model.configure.subject.fo.ListSubjectMapRulesF;
import com.wishare.finance.apps.model.configure.subject.fo.SubjectLevelJson;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailPageV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectMapUnitDetailV;
import com.wishare.finance.domains.bill.dto.ItemDto;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.repository.CashFlowRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.subject.command.subject.AddSubjectMapRulesCommand;
import com.wishare.finance.domains.configure.subject.command.subject.BatchSettingCommand;
import com.wishare.finance.domains.configure.subject.command.subject.UpdateSubjectMapRulesCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubMapTypeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapRulesE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapRulesRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.report.enums.ChargeItemAttributeEnum;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectMapRulesDomainService {

    private final SubjectMapRulesRepository subjectMapRulesRepository;

    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;

    private final ChargeItemRepository chargeItemRepository;

    private final CashFlowRepository cashFlowRepository;

    private final SubjectRepository subjectRepository;

    /**
     * 新增科目映射规则
     *
     * @param command
     * @return
     */
    public Long addSubjectMapRules(AddSubjectMapRulesCommand command) {
        //如果存在相同的映射规则名称，则不允许创建
        SubjectMapRulesE entity = subjectMapRulesRepository.getBySubMapName(command.getSubMapName());
        if (null != entity) {
            throw BizException.throw400("已存在相同的映射规则名称，请修改");
        }
        //如果规则存在相同的科目体系、映射单元、映射单元属性（费项属性/辅助核算项目），则不允许创建
        entity = subjectMapRulesRepository.getBySysAndUnit(command.getSubSysId(), command.getSubMapType(), command.getChargeItemAttribute());
        if (null != entity) {
            throw BizException.throw400("已存在相同组别的映射规则，请勿重复创建");
        }
        SubjectMapRulesE subjectMapRulesE = command.getSubjectMapRules();
        subjectMapRulesRepository.save(subjectMapRulesE);
        return subjectMapRulesE.getId();
    }

    /**
     * 修改科目映射规则
     *
     * @param command
     * @return
     */
    public Long updateSubjectMapRules(UpdateSubjectMapRulesCommand command) {
        //如果存在相同的映射规则名称，则不允许创建
        SubjectMapRulesE entity = subjectMapRulesRepository.getBySubMapName(command.getSubMapName());
        if (entity != null) {
            if (entity.getId().longValue() != command.getId().longValue() && entity.getSubMapName().equals(command.getSubMapName())) {
                throw BizException.throw400("已存在相同的映射规则名称，请修改");
            }

        }
        //如果规则存在相同的科目体系、映射单元、映射单元属性（费项属性/辅助核算项目），则不允许创建
        SubjectMapRulesE entity2 = subjectMapRulesRepository.getBySysAndUnit(command.getSubSysId(), command.getSubMapType(), command.getChargeItemAttribute());
        if (Objects.nonNull(entity2) && !entity2.getId().equals(command.getId())) {
            throw BizException.throw400("已存在相同组别的映射规则，请勿重复创建");
        }
        SubjectMapRulesE subjectMapRulesE = command.getSubjectMapRules();
        subjectMapRulesRepository.updateById(subjectMapRulesE);
        return subjectMapRulesE.getId();
    }

    /**
     * 删除科目映射规则
     *
     * @param id
     * @return
     */
    public Boolean deleteSubjectMapRules(Long id) {
        //删除科目规则的同时，删除科目规则对应的明细
        List<SubjectMapUnitDetailE> detailES = subjectMapUnitDetailRepository.list(new QueryWrapper<SubjectMapUnitDetailE>().eq("sub_map_rule_id", id));
        List<Long> ids = detailES.stream().map(item -> item.getId()).collect(Collectors.toList());
        subjectMapRulesRepository.removeById(id);
        return subjectMapUnitDetailRepository.removeByIds(ids);
    }

    /**
     * 获取科目映射规则详情
     *
     * @param id
     * @return
     */
    public SubjectMapRulesE detailSubjectMapRules(Long id) {
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesRepository.getById(id);
        if (null == subjectMapRulesE) {
            throw BizException.throw400("当前科目映射不存在");
        }
        return subjectMapRulesE;
    }

    /**
     * 获取科目映射规则列表
     *
     * @param form
     * @return
     */
    public List<SubjectMapRulesE> listSubjectMapRules(ListSubjectMapRulesF form) {
        return subjectMapRulesRepository.listSubjectMapRules(form);
    }

    /**
     * 映射单元批量设置
     *
     * @param commands
     * @return
     */
    public Boolean batchSetting(List<BatchSettingCommand> commands) {
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesRepository.getById(commands.get(0).getSubjectMapRuleId());
        if (null == subjectMapRulesE) {
            throw BizException.throw400(ErrorMessage.SUBJECT_MAP_RULES_NOT_FOUND.msg());
        }
        commands.forEach(settingCommand -> {
            SubjectMapUnitDetailE subjectMapUnitDetailE = subjectMapUnitDetailRepository.getSubjectMapUnit(settingCommand.getSubjectMapRuleId(),settingCommand.getSubMapUnitId(),settingCommand.getSubjectLevelOneId());
            if (subjectMapUnitDetailE != null) {
                SubjectMapUnitDetailE detailE = settingCommand.getUpdateSubjectMapUnitDetail(subjectMapUnitDetailE);
                subjectMapUnitDetailRepository.updateById(detailE);
            } else {
                SubjectMapUnitDetailE detailE = settingCommand.getSubjectMapUnitDetail(subjectMapRulesE.getSubMapType());
                subjectMapUnitDetailRepository.save(detailE);
            }
        });
        return true;
    }

    /**
     * 根据科目映射规则id获取映射单元详情列表
     *
     * @param form
     * @return
     */
    public PageV<SubjectMapUnitDetailPageV> batchSettingPage(PageF<SearchF<BatchSettingPageF>> form) {
        Long subjectMapRuleId = getSubjectMapRuleIdFromSearchF(form);
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesRepository.getById(subjectMapRuleId);
        if (subjectMapRulesE == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_MAP_RULES_NOT_FOUND.msg());
        }
        if (subjectMapRulesE.getSubMapType() == SubMapTypeEnum.费项.getCode()) {
            //分页获取费项属性
            Page<ChargeItemE> chargeItemEPage = getChargeItemEPage(form, subjectMapRulesE.getChargeItemAttribute());
            //根据科目映射规则id和费翔属性id获取详情
            if (CollectionUtils.isNotEmpty(chargeItemEPage.getRecords())) {
                return getSubjectMapUnitDetailPageV(chargeItemEPage,subjectMapRuleId);
            }
        } else if (subjectMapRulesE.getSubMapType() == SubMapTypeEnum.辅助核算.getCode()) {
            throw BizException.throw400("辅助核算相关映射接口开发中");
        }
        return null;
    }

    /**
     * 获取科目映射规则明细详情分页
     *
     * @param chargeItemEPage
     * @param subjectMapRuleId
     * @return
     */
    private PageV<SubjectMapUnitDetailPageV> getSubjectMapUnitDetailPageV(Page<ChargeItemE> chargeItemEPage, Long subjectMapRuleId) {
        List<ChargeItemE> records = chargeItemEPage.getRecords();

        if (CollectionUtils.isNotEmpty(records)) {
            List<SubjectMapUnitDetailPageV> detailList = Lists.newArrayList();
            for (ChargeItemE chargeItemE : records) {
                SubjectMapUnitDetailPageV subjectMapUnitDetailPageV = new SubjectMapUnitDetailPageV();
                subjectMapUnitDetailPageV.setSubMapUnitId(chargeItemE.getId());
                subjectMapUnitDetailPageV.setSubMapUnitName(chargeItemE.getName());
                subjectMapUnitDetailPageV.setSubMapUnitCode(chargeItemE.getCode());
                subjectMapUnitDetailPageV.setSubMapUnitParentName(chargeItemE.getParentName());
                List<SubjectMapUnitDetailE> subjectMapUnitDetailES = subjectMapUnitDetailRepository.getBySubjectMapRuleId(subjectMapRuleId, Lists.newArrayList(chargeItemE.getId()));
                Map<Long, SubjectLevelJson> stringSubjectLevelJsonMap = new HashedMap<>();

                if (CollectionUtils.isNotEmpty(subjectMapUnitDetailES)) {
                    for (SubjectMapUnitDetailE subjectMapUnitDetailE : subjectMapUnitDetailES) {
                        if (subjectMapUnitDetailE.getSubMapUnitId().equals(chargeItemE.getId())) {
                            SubjectLevelJson subjectLevelJson = new SubjectLevelJson();
                            subjectLevelJson.setSubjectId(subjectMapUnitDetailE.getSubjectLevelLastId());
                            subjectLevelJson.setSubjectName(subjectMapUnitDetailE.getSubjectLevelLastName());
                            if(SubjectRuleMapTypeEnum.现金流量.getCode() == subjectMapUnitDetailE.getMapType()){
                                CashFlowE cashFlowE = cashFlowRepository.getById(subjectMapUnitDetailE.getSubjectLevelLastId());
                                if(Objects.nonNull(cashFlowE)){
                                    subjectLevelJson.setItemType(cashFlowE.getItemType());
                                    subjectLevelJson.setCode(cashFlowE.getCode());
                                }
                            }else{
                                SubjectE subject = subjectRepository.getSubject(subjectMapUnitDetailE.getSubjectLevelLastId());
                                if(Objects.nonNull(subject)){
                                    subjectLevelJson.setCode(subject.getSubjectCode());
                                }
                            }
                            stringSubjectLevelJsonMap.put(subjectMapUnitDetailE.getSubjectLevelOneId(), subjectLevelJson);
                        }
                    }
                    if (!stringSubjectLevelJsonMap.isEmpty()) {
                        subjectMapUnitDetailPageV.setSubjectLevelLastMap(stringSubjectLevelJsonMap);
                    }
                }
                detailList.add(subjectMapUnitDetailPageV);
            }
            PageV<SubjectMapUnitDetailPageV> pageV = new PageV<>();
            pageV.setPageNum(chargeItemEPage.getCurrent());
            pageV.setPageSize(chargeItemEPage.getSize());
            pageV.setTotal(chargeItemEPage.getTotal());
            pageV.setRecords(detailList);
            return pageV;
        }
        return null;
    }

    /**
     * 根据费项属性获取费项分页数据
     *
     * @param form
     * @param chargeItemAttribute
     * @return
     */
    private Page<ChargeItemE> getChargeItemEPage(PageF<SearchF<BatchSettingPageF>> form, Integer chargeItemAttribute) {
        //根据费项属性获取费翔分页数据
        QueryWrapper<ChargeItemE> queryModel = new QueryWrapper<>();
        queryModel.eq("ci.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("ci.disabled", DataDisabledEnum.启用.getCode());
        queryModel.eq("ci.attribute", chargeItemAttribute);
        String chargeItemName = getChargeItemNameFromSearchF(form);
        queryModel.like(StringUtils.isNotEmpty(chargeItemName),"ci.name", chargeItemName);
        Page<ChargeItemE> page = Page.of(form.getPageNum(), form.getPageSize());
        return chargeItemRepository.queryLastStageAndParentByPage(page, queryModel);
    }

    private String getChargeItemNameFromSearchF(PageF<SearchF<BatchSettingPageF>> form) {
        SearchF<BatchSettingPageF> conditions = form.getConditions();
        if (conditions == null) {
            throw BizException.throw400("查询条件为空");
        }
        List<Field> fields = conditions.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.equals("name")) {
                return (String) field.getValue();
            }
        }
       return "";
    }

    /**
     * 获取科目映射规则id
     *
     * @param form
     * @return
     */
    private Long getSubjectMapRuleIdFromSearchF(PageF<SearchF<BatchSettingPageF>> form) {
        SearchF<BatchSettingPageF> conditions = form.getConditions();
        if (conditions == null) {
            throw BizException.throw400("查询条件为空");
        }
        List<Field> fields = conditions.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.equals("subjectMapRuleId")) {
                Long value = (Long) field.getValue();
                return value;
            }
        }
        throw BizException.throw400("查询条件异常,科目映射规则id不存在");
    }

    /**
     * 根据id启用或禁用科目映射
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesRepository.getById(id);
        if (null == subjectMapRulesE) {
            throw BizException.throw400("当前科目映射不存在");
        }
        subjectMapRulesE.setDisabled(disableState);
        return subjectMapRulesRepository.updateById(subjectMapRulesE);
    }

    /**
     * 根据映射单元id获取映射详情列表
     * @param unitIds 映射单元id雷彪
     * @param subMapType 映射类型 1 费项 2 辅助核算
     * @param mapType 映射类别： 1科目，2现金流量
     * @return 映射详情列表
     */
    public List<SubjectMapUnitDetailE> getMapDetailByUnitIds(List<Long> unitIds, int subMapType, int mapType, String subjectCode) {
       return subjectMapUnitDetailRepository.getByUnitIds(unitIds, subMapType);
    }

    /**
     * 获取映射详情列表
     * @param unitIds 映射单元id雷彪
     * @param firstSubjectId 一级科目id
     * @param subMapType 映射类型 1 费项 2 辅助核算
     * @return 映射详情列表
     */
    public SubjectMapUnitDetailE getMapDetail(List<Long> unitIds, String firstSubjectId, int subMapType) {
        return subjectMapUnitDetailRepository.getMapDetail(unitIds, firstSubjectId, subMapType);
    }

    public SubjectLevelJson getByUnitId(Long unitId) {
        SubjectMapUnitDetailE subjectMapUnitDetailE = subjectMapUnitDetailRepository.getByUnitId(unitId);
        SubjectLevelJson subjectLevelJson = new SubjectLevelJson();
        if (Objects.nonNull(subjectMapUnitDetailE)) {
            subjectLevelJson.setSubjectId(subjectMapUnitDetailE.getSubjectLevelLastId());
            subjectLevelJson.setSubjectName(subjectMapUnitDetailE.getSubjectLevelLastName());

            CashFlowE cashFlowE = cashFlowRepository.getById(subjectMapUnitDetailE.getSubjectLevelLastId());
            if (Objects.nonNull(cashFlowE)) {
                subjectLevelJson.setItemType(cashFlowE.getItemType());
                subjectLevelJson.setCode(cashFlowE.getCode());
                subjectLevelJson.setCashFlowName(cashFlowE.getName());
                subjectLevelJson.setCashFlowId(cashFlowE.getId());
            }
        }

        return subjectLevelJson;
    }


    /**
     * 查询科目映射详情信息
     *
     * @param subjectMapRuleId
     * @return
     */
    public List<SubjectMapUnitDetailV> queryDetail(Long subjectMapRuleId,Long subMapUnitId) {
        LambdaQueryWrapper<SubjectMapUnitDetailE> queryModel = new LambdaQueryWrapper<>();
        queryModel.eq(SubjectMapUnitDetailE::getSubMapRuleId,subjectMapRuleId).
                eq(SubjectMapUnitDetailE::getSubMapUnitId,subMapUnitId);
        List<SubjectMapUnitDetailE> subjectMapUnitDetailES = subjectMapUnitDetailRepository.list(queryModel);
        List<SubjectMapUnitDetailV> subjectMapUnitDetailVS = Global.mapperFacade.mapAsList(subjectMapUnitDetailES, SubjectMapUnitDetailV.class);
        return subjectMapUnitDetailVS;
    }

    //获取【科目映射】中【代收代付属性费项映射】中所有【其他应收款】编码
    public List<String> getAllTPPReceiveblesList() {
        List<String> resultList = new ArrayList<>();
        //获取代收代付
        SubjectMapRulesE subjectMapRulesE = subjectMapRulesRepository.getSubMapRulesByAttribute(ChargeItemAttributeEnum.代收代付及其他.getCode());
        if(Objects.isNull(subjectMapRulesE)){
            return resultList;
        }

        //根据体系ID及属性获取所有单元ID
        List<String> itemIdList = chargeItemRepository.getItemIdList(subjectMapRulesE.getTenantId(),ChargeItemAttributeEnum.代收代付及其他.getCode());
        if(CollectionUtils.isEmpty(itemIdList)){
            return resultList;
        }
        resultList = subjectMapUnitDetailRepository.getSubMapUnitDetIdList(subjectMapRulesE.getTenantId(), subjectMapRulesE.getId(), itemIdList, "其他应收款");
        return CollectionUtils.isEmpty(resultList) ? new ArrayList<>() : resultList;
    }
}
