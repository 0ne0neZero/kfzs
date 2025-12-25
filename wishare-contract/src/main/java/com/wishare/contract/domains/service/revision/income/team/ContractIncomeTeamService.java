package com.wishare.contract.domains.service.revision.income.team;

import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.income.team.ContractIncomeTeamE;
import com.wishare.contract.domains.mapper.revision.income.team.ContractIncomeTeamMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamV;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamListV;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamPageF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamSaveF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamUpdateF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamListF;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 收入合同-团队表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeTeamService extends ServiceImpl<ContractIncomeTeamMapper, ContractIncomeTeamE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeTeamMapper contractIncomeTeamMapper;


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeTeamV> get(ContractIncomeTeamF conditions){
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractIncomeTeamE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeTeamE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractIncomeTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractIncomeTeamE contractIncomeTeamE = contractIncomeTeamMapper.selectOne(queryWrapper);
        if (contractIncomeTeamE != null) {
            return Optional.of(Global.mapperFacade.map(contractIncomeTeamE, ContractIncomeTeamV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public ContractIncomeTeamListV list(ContractIncomeTeamListF conditions){
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeTeamE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractIncomeTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractIncomeTeamE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractIncomeTeamE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractIncomeTeamV> retVList = Global.mapperFacade.mapAsList(contractIncomeTeamMapper.selectList(queryWrapper),ContractIncomeTeamV.class);
        ContractIncomeTeamListV retV = new ContractIncomeTeamListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractIncomeTeamSaveF contractIncomeTeamF){
        ContractIncomeTeamE map = Global.mapperFacade.map(contractIncomeTeamF, ContractIncomeTeamE.class);

        map.setTenantId(tenantId());

        //-- 所属公司
        if (StringUtils.isNotBlank(map.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getOrgId()))).ifPresentOrElse(v -> {
                map.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属公司ID检索组织名称失败");});
        }
        //-- 所属部门
        if (StringUtils.isNotBlank(map.getDepartId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getDepartId()))).ifPresentOrElse(v -> {
                map.setDepartName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        }
        //-- 团队角色
        if (StringUtils.isNotBlank(map.getRoleId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.团队角色.getCode(), map.getRoleId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setRole(value.get(0).getName());
            }
        }

        contractIncomeTeamMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractIncomeTeamF 根据Id更新
    */
    public void update(ContractIncomeTeamUpdateF contractIncomeTeamF){
        if (contractIncomeTeamF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeTeamE map = Global.mapperFacade.map(contractIncomeTeamF, ContractIncomeTeamE.class);
        contractIncomeTeamMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractIncomeTeamMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeTeamV> page(PageF<ContractIncomeTeamPageF> request) {
        ContractIncomeTeamPageF conditions = request.getConditions();
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeTeamE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractIncomeTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractIncomeTeamE.GMT_CREATE);
        }
        Page<ContractIncomeTeamE> page = contractIncomeTeamMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeTeamV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeTeamV> frontPage(PageF<SearchF<ContractIncomeTeamE>> request) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractIncomeTeamE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.orderByDesc(ContractIncomeTeamE.GMT_CREATE)
                .eq(ContractIncomeTeamE.TENANT_ID, tenantId());

        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractIncomeTeamE> page = contractIncomeTeamMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeTeamV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractIncomeTeamE中仅包含a字段的值
    *
    * @param fields ContractIncomeTeamE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeTeamE selectOneBy(Consumer<QueryWrapper<ContractIncomeTeamE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeTeamMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeTeamE中id字段的值, select 指定字段
    *
    * @param fields ContractIncomeTeamE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeTeamE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeTeamMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeTeamE>> consumer) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeTeamMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractIncomeTeamE中仅包含a字段的值
     *
     * @param fields ContractIncomeTeamE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeTeamE> selectListBy(Consumer<QueryWrapper<ContractIncomeTeamE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeTeamMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractIncomeTeamE中仅包含a字段的值
    *
    * @param fields ContractIncomeTeamE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeTeamE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeTeamE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeTeamMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeTeamE中id字段的值, select 指定字段
     *
     * @param fields ContractIncomeTeamE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeTeamE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeTeamE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeTeamE> queryPage = contractIncomeTeamMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
