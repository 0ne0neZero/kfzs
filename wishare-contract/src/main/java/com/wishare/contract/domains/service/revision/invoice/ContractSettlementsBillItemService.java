package com.wishare.contract.domains.service.revision.invoice;

import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillItemE;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillItemMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemV;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemListV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemPageF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemUpdateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemListF;
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
 * 收票款项明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractSettlementsBillItemService extends ServiceImpl<ContractSettlementsBillItemMapper, ContractSettlementsBillItemE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillItemMapper contractSettlementsBillItemMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractSettlementsBillItemV> get(ContractSettlementsBillItemF conditions){
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_ID, conditions.getBussTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_NAME, conditions.getBussTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getChangeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_ID, conditions.getChangeId());
        }

        if (StringUtils.isNotBlank(conditions.getChangeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_NAME, conditions.getChangeName());
        }

        if (StringUtils.isNotBlank(conditions.getItemId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_ID, conditions.getItemId());
        }

        if (StringUtils.isNotBlank(conditions.getItemName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_NAME, conditions.getItemName());
        }

        if (StringUtils.isNotBlank(conditions.getWriteOffInfo())) {
            queryWrapper.eq(ContractSettlementsBillItemE.WRITE_OFF_INFO, conditions.getWriteOffInfo());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractSettlementsBillItemE contractSettlementsBillItemE = contractSettlementsBillItemMapper.selectOne(queryWrapper);
        if (contractSettlementsBillItemE != null) {
            return Optional.of(Global.mapperFacade.map(contractSettlementsBillItemE, ContractSettlementsBillItemV.class));
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
    public ContractSettlementsBillItemListV list(ContractSettlementsBillItemListF conditions){
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_ID, conditions.getBussTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_NAME, conditions.getBussTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getChangeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_ID, conditions.getChangeId());
        }

        if (StringUtils.isNotBlank(conditions.getChangeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_NAME, conditions.getChangeName());
        }

        if (StringUtils.isNotBlank(conditions.getItemId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_ID, conditions.getItemId());
        }

        if (StringUtils.isNotBlank(conditions.getItemName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_NAME, conditions.getItemName());
        }

        if (StringUtils.isNotBlank(conditions.getWriteOffInfo())) {
            queryWrapper.eq(ContractSettlementsBillItemE.WRITE_OFF_INFO, conditions.getWriteOffInfo());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractSettlementsBillItemE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractSettlementsBillItemE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractSettlementsBillItemV> retVList = Global.mapperFacade.mapAsList(contractSettlementsBillItemMapper.selectList(queryWrapper),ContractSettlementsBillItemV.class);
        ContractSettlementsBillItemListV retV = new ContractSettlementsBillItemListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractSettlementsBillItemSaveF contractSettlementsBillItemF){
        ContractSettlementsBillItemE map = Global.mapperFacade.map(contractSettlementsBillItemF, ContractSettlementsBillItemE.class);
        map.setTenantId(tenantId());
        map.setGmtCreate(LocalDateTime.now());
        map.setGmtModify(LocalDateTime.now());
        contractSettlementsBillItemMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractSettlementsBillItemF 根据Id更新
    */
    public void update(ContractSettlementsBillItemUpdateF contractSettlementsBillItemF){
        if (contractSettlementsBillItemF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractSettlementsBillItemE map = Global.mapperFacade.map(contractSettlementsBillItemF, ContractSettlementsBillItemE.class);
        contractSettlementsBillItemMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractSettlementsBillItemMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillItemV> page(PageF<ContractSettlementsBillItemPageF> request) {
        ContractSettlementsBillItemPageF conditions = request.getConditions();
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_ID, conditions.getBussTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getBussTypeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.BUSS_TYPE_NAME, conditions.getBussTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getChangeId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_ID, conditions.getChangeId());
        }

        if (StringUtils.isNotBlank(conditions.getChangeName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CHANGE_NAME, conditions.getChangeName());
        }

        if (StringUtils.isNotBlank(conditions.getItemId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_ID, conditions.getItemId());
        }

        if (StringUtils.isNotBlank(conditions.getItemName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.ITEM_NAME, conditions.getItemName());
        }

        if (StringUtils.isNotBlank(conditions.getWriteOffInfo())) {
            queryWrapper.eq(ContractSettlementsBillItemE.WRITE_OFF_INFO, conditions.getWriteOffInfo());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillItemE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillItemE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillItemE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractSettlementsBillItemE.GMT_CREATE);
        }
        Page<ContractSettlementsBillItemE> page = contractSettlementsBillItemMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillItemV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillItemV> frontPage(PageF<SearchF<ContractSettlementsBillItemE>> request) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractSettlementsBillItemE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractSettlementsBillItemE> page = contractSettlementsBillItemMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillItemV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractSettlementsBillItemE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillItemE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractSettlementsBillItemE selectOneBy(Consumer<QueryWrapper<ContractSettlementsBillItemE>> consumer,String... fields) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillItemMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillItemE中id字段的值, select 指定字段
    *
    * @param fields ContractSettlementsBillItemE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillItemE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractSettlementsBillItemMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractSettlementsBillItemE>> consumer) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractSettlementsBillItemMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractSettlementsBillItemE中仅包含a字段的值
     *
     * @param fields ContractSettlementsBillItemE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractSettlementsBillItemE> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillItemE>> consumer,String... fields) {
         QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractSettlementsBillItemMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractSettlementsBillItemE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillItemE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractSettlementsBillItemE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractSettlementsBillItemE>> consumer, String... fields) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillItemMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillItemE中id字段的值, select 指定字段
     *
     * @param fields ContractSettlementsBillItemE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractSettlementsBillItemE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractSettlementsBillItemE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractSettlementsBillItemE> page = Page.of(pageNum, pageSize, count);
        Page<ContractSettlementsBillItemE> queryPage = contractSettlementsBillItemMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
