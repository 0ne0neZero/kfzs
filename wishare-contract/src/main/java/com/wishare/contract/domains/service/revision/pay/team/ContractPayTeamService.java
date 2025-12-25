package com.wishare.contract.domains.service.revision.pay.team;

import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.pay.team.ContractPayTeamE;
import com.wishare.contract.domains.mapper.revision.pay.team.ContractPayTeamMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamV;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamListV;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamPageF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamSaveF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamUpdateF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamListF;
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
 * 支出合同-团队表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPayTeamService extends ServiceImpl<ContractPayTeamMapper, ContractPayTeamE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayTeamMapper contractPayTeamMapper;


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
    public Optional<ContractPayTeamV> get(ContractPayTeamF conditions){
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractPayTeamE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractPayTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractPayTeamE contractPayTeamE = contractPayTeamMapper.selectOne(queryWrapper);
        if (contractPayTeamE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayTeamE, ContractPayTeamV.class));
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
    public ContractPayTeamListV list(ContractPayTeamListF conditions){
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractPayTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractPayTeamE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractPayTeamE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractPayTeamV> retVList = Global.mapperFacade.mapAsList(contractPayTeamMapper.selectList(queryWrapper),ContractPayTeamV.class);
        ContractPayTeamListV retV = new ContractPayTeamListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractPayTeamSaveF contractPayTeamF){
        ContractPayTeamE map = Global.mapperFacade.map(contractPayTeamF, ContractPayTeamE.class);

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

        map.setTenantId(tenantId());

        contractPayTeamMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractPayTeamF 根据Id更新
    */
    public void update(ContractPayTeamUpdateF contractPayTeamF){
        if (contractPayTeamF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayTeamE map = Global.mapperFacade.map(contractPayTeamF, ContractPayTeamE.class);
        contractPayTeamMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractPayTeamMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayTeamV> page(PageF<ContractPayTeamPageF> request) {
        ContractPayTeamPageF conditions = request.getConditions();
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayTeamE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayTeamE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayTeamE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayTeamE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getRole())) {
            queryWrapper.eq(ContractPayTeamE.ROLE, conditions.getRole());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayTeamE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayTeamE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayTeamE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayTeamE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayTeamE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayTeamE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayTeamE.GMT_CREATE);
        }
        Page<ContractPayTeamE> page = contractPayTeamMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayTeamV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayTeamV> frontPage(PageF<SearchF<ContractPayTeamE>> request) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayTeamE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(ContractPayTeamE.TENANT_ID, tenantId())
                .orderByDesc(ContractPayTeamE.GMT_CREATE);

        Page<ContractPayTeamE> page = contractPayTeamMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayTeamV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayTeamE中仅包含a字段的值
    *
    * @param fields ContractPayTeamE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayTeamE selectOneBy(Consumer<QueryWrapper<ContractPayTeamE>> consumer,String... fields) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayTeamMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayTeamE中id字段的值, select 指定字段
    *
    * @param fields ContractPayTeamE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayTeamE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayTeamMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayTeamE>> consumer) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayTeamMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayTeamE中仅包含a字段的值
     *
     * @param fields ContractPayTeamE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayTeamE> selectListBy(Consumer<QueryWrapper<ContractPayTeamE>> consumer,String... fields) {
         QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayTeamMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayTeamE中仅包含a字段的值
    *
    * @param fields ContractPayTeamE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayTeamE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayTeamE>> consumer, String... fields) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayTeamMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayTeamE中id字段的值, select 指定字段
     *
     * @param fields ContractPayTeamE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayTeamE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayTeamE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayTeamE> queryPage = contractPayTeamMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
