package com.wishare.contract.domains.service.revision.relation;

import com.wishare.contract.domains.enums.revision.ActionTypeEnum;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.mapper.revision.relation.ContractRelationRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordV;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordListV;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordPageF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordSaveF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordUpdateF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordListF;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import io.swagger.models.auth.In;
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
 * 
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractRelationRecordService extends ServiceImpl<ContractRelationRecordMapper, ContractRelationRecordE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRelationRecordMapper contractRelationRecordMapper;

    /**
     * 新增记录
     * @param type 类型
     * @param newId 新ID
     * @param oldId 旧ID
     * @return
     */
    public String insertNewOne(Integer type,
                               String newId,
                               String oldId,
                               Integer contractType) {
        if (Objects.isNull(ActionTypeEnum.parseName(type)) || StringUtils.isBlank(newId) || StringUtils.isBlank(oldId)) {
            throw new OwlBizException("执行变更或续签操作时关键信息不可为空");
        }
        ContractRelationRecordE map = new ContractRelationRecordE();
        map.setTenantId(tenantId())
                .setType(type)
                .setContractType(contractType)
                .setAddId(newId)
                .setOldId(oldId);

        contractRelationRecordMapper.insert(map);
        return map.getId();
    }


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractRelationRecordV> get(ContractRelationRecordF conditions){
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractRelationRecordE.ID, conditions.getId());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractRelationRecordE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getAddId())) {
            queryWrapper.eq(ContractRelationRecordE.ADD_ID, conditions.getAddId());
        }

        if (StringUtils.isNotBlank(conditions.getOldId())) {
            queryWrapper.eq(ContractRelationRecordE.OLD_ID, conditions.getOldId());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRelationRecordE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractRelationRecordE contractRelationRecordE = contractRelationRecordMapper.selectOne(queryWrapper);
        if (contractRelationRecordE != null) {
            return Optional.of(Global.mapperFacade.map(contractRelationRecordE, ContractRelationRecordV.class));
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
    public ContractRelationRecordListV list(ContractRelationRecordListF conditions){
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractRelationRecordE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getAddId())) {
            queryWrapper.eq(ContractRelationRecordE.ADD_ID, conditions.getAddId());
        }

        if (StringUtils.isNotBlank(conditions.getOldId())) {
            queryWrapper.eq(ContractRelationRecordE.OLD_ID, conditions.getOldId());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRelationRecordE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractRelationRecordE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractRelationRecordE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractRelationRecordV> retVList = Global.mapperFacade.mapAsList(contractRelationRecordMapper.selectList(queryWrapper),ContractRelationRecordV.class);
        ContractRelationRecordListV retV = new ContractRelationRecordListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractRelationRecordSaveF contractRelationRecordF){
        ContractRelationRecordE map = Global.mapperFacade.map(contractRelationRecordF, ContractRelationRecordE.class);
        contractRelationRecordMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractRelationRecordF 根据Id更新
    */
    public void update(ContractRelationRecordUpdateF contractRelationRecordF){
        if (contractRelationRecordF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractRelationRecordE map = Global.mapperFacade.map(contractRelationRecordF, ContractRelationRecordE.class);
        contractRelationRecordMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractRelationRecordMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractRelationRecordV> page(PageF<ContractRelationRecordPageF> request) {
        ContractRelationRecordPageF conditions = request.getConditions();
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractRelationRecordE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getAddId())) {
            queryWrapper.eq(ContractRelationRecordE.ADD_ID, conditions.getAddId());
        }

        if (StringUtils.isNotBlank(conditions.getOldId())) {
            queryWrapper.eq(ContractRelationRecordE.OLD_ID, conditions.getOldId());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRelationRecordE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRelationRecordE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRelationRecordE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRelationRecordE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractRelationRecordE.GMT_CREATE);
        }
        Page<ContractRelationRecordE> page = contractRelationRecordMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractRelationRecordV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractRelationRecordV> frontPage(PageF<SearchF<ContractRelationRecordE>> request) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractRelationRecordE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractRelationRecordE> page = contractRelationRecordMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractRelationRecordV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractRelationRecordE中仅包含a字段的值
    *
    * @param fields ContractRelationRecordE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractRelationRecordE selectOneBy(Consumer<QueryWrapper<ContractRelationRecordE>> consumer,String... fields) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractRelationRecordMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractRelationRecordE中id字段的值, select 指定字段
    *
    * @param fields ContractRelationRecordE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractRelationRecordE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractRelationRecordMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractRelationRecordE>> consumer) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractRelationRecordMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractRelationRecordE中仅包含a字段的值
     *
     * @param fields ContractRelationRecordE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractRelationRecordE> selectListBy(Consumer<QueryWrapper<ContractRelationRecordE>> consumer,String... fields) {
         QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractRelationRecordMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractRelationRecordE中仅包含a字段的值
    *
    * @param fields ContractRelationRecordE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractRelationRecordE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractRelationRecordE>> consumer, String... fields) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractRelationRecordMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractRelationRecordE中id字段的值, select 指定字段
     *
     * @param fields ContractRelationRecordE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractRelationRecordE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractRelationRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractRelationRecordE> page = Page.of(pageNum, pageSize, count);
        Page<ContractRelationRecordE> queryPage = contractRelationRecordMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
