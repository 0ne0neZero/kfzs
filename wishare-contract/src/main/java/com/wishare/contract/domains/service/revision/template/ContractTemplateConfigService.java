package com.wishare.contract.domains.service.revision.template;

import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.template.ContractTemplateConfigE;
import com.wishare.contract.domains.mapper.revision.template.ContractTemplateConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigV;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigListV;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigPageF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigUpdateF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigListF;
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
 * 合同范本字段配置表
 * </p>
 *
 * @author zhangfuyu
 * @since 2023-07-26
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractTemplateConfigService extends ServiceImpl<ContractTemplateConfigMapper, ContractTemplateConfigE>  {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractTemplateConfigMapper contractTemplateConfigMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractTemplateConfigV> get(ContractTemplateConfigF conditions){
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractTemplateConfigE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractTemplateConfigE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getFieldName())) {
            queryWrapper.eq(ContractTemplateConfigE.FIELD_NAME, conditions.getFieldName());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractTemplateConfigE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractTemplateConfigE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_MODIFY, conditions.getGmtModify());
        }
        ContractTemplateConfigE contractTemplateConfigE = contractTemplateConfigMapper.selectOne(queryWrapper);
        if (contractTemplateConfigE != null) {
            return Optional.of(Global.mapperFacade.map(contractTemplateConfigE, ContractTemplateConfigV.class));
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
    public ContractTemplateConfigListV list(ContractTemplateConfigListF conditions){
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractTemplateConfigE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getFieldName())) {
            queryWrapper.eq(ContractTemplateConfigE.FIELD_NAME, conditions.getFieldName());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractTemplateConfigE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractTemplateConfigE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractTemplateConfigE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractTemplateConfigE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractTemplateConfigV> retVList = Global.mapperFacade.mapAsList(contractTemplateConfigMapper.selectList(queryWrapper),ContractTemplateConfigV.class);
        ContractTemplateConfigListV retV = new ContractTemplateConfigListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractTemplateConfigSaveF contractTemplateConfigF){
        ContractTemplateConfigE map = Global.mapperFacade.map(contractTemplateConfigF, ContractTemplateConfigE.class);
        contractTemplateConfigMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractTemplateConfigF 根据Id更新
    */
    public void update(ContractTemplateConfigUpdateF contractTemplateConfigF){
        if (contractTemplateConfigF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractTemplateConfigE map = Global.mapperFacade.map(contractTemplateConfigF, ContractTemplateConfigE.class);
        contractTemplateConfigMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractTemplateConfigMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractTemplateConfigV> page(PageF<ContractTemplateConfigPageF> request) {
        ContractTemplateConfigPageF conditions = request.getConditions();
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractTemplateConfigE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getFieldName())) {
            queryWrapper.eq(ContractTemplateConfigE.FIELD_NAME, conditions.getFieldName());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractTemplateConfigE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractTemplateConfigE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractTemplateConfigE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractTemplateConfigE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractTemplateConfigE.GMT_CREATE);
        }
        Page<ContractTemplateConfigE> page = contractTemplateConfigMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractTemplateConfigV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractTemplateConfigV> frontPage(PageF<SearchF<ContractTemplateConfigE>> request) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractTemplateConfigE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractTemplateConfigE> page = contractTemplateConfigMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractTemplateConfigV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractTemplateConfigE中仅包含a字段的值
    *
    * @param fields ContractTemplateConfigE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractTemplateConfigE selectOneBy(Consumer<QueryWrapper<ContractTemplateConfigE>> consumer,String... fields) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractTemplateConfigMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractTemplateConfigE中id字段的值, select 指定字段
    *
    * @param fields ContractTemplateConfigE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractTemplateConfigE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractTemplateConfigMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractTemplateConfigE>> consumer) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractTemplateConfigMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractTemplateConfigE中仅包含a字段的值
     *
     * @param fields ContractTemplateConfigE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractTemplateConfigE> selectListBy(Consumer<QueryWrapper<ContractTemplateConfigE>> consumer,String... fields) {
         QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractTemplateConfigMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractTemplateConfigE中仅包含a字段的值
    *
    * @param fields ContractTemplateConfigE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractTemplateConfigE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractTemplateConfigE>> consumer, String... fields) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractTemplateConfigMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractTemplateConfigE中id字段的值, select 指定字段
     *
     * @param fields ContractTemplateConfigE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractTemplateConfigE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractTemplateConfigE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractTemplateConfigE> page = Page.of(pageNum, pageSize, count);
        Page<ContractTemplateConfigE> queryPage = contractTemplateConfigMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
