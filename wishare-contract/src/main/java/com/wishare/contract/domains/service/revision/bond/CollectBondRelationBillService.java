package com.wishare.contract.domains.service.revision.bond;

import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import com.wishare.contract.domains.mapper.revision.bond.CollectBondRelationBillMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillV;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillListV;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillPageF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillUpdateF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillListF;
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
 * 收取保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-27
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class CollectBondRelationBillService extends ServiceImpl<CollectBondRelationBillMapper, CollectBondRelationBillE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private CollectBondRelationBillMapper collectBondRelationBillMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<CollectBondRelationBillV> get(CollectBondRelationBillF conditions){
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(CollectBondRelationBillE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(CollectBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(CollectBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(CollectBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(CollectBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(CollectBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(CollectBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(CollectBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }

        queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, tenantId());

        CollectBondRelationBillE collectBondRelationBillE = collectBondRelationBillMapper.selectOne(queryWrapper);
        if (collectBondRelationBillE != null) {
            return Optional.of(Global.mapperFacade.map(collectBondRelationBillE, CollectBondRelationBillV.class));
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
    public CollectBondRelationBillListV list(CollectBondRelationBillListF conditions){
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(CollectBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(CollectBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(CollectBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(CollectBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(CollectBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(CollectBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(CollectBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(CollectBondRelationBillE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(CollectBondRelationBillE.ID);

        queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, tenantId());

        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<CollectBondRelationBillV> retVList = Global.mapperFacade.mapAsList(collectBondRelationBillMapper.selectList(queryWrapper),CollectBondRelationBillV.class);
        CollectBondRelationBillListV retV = new CollectBondRelationBillListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(CollectBondRelationBillSaveF collectBondRelationBillF){
        CollectBondRelationBillE map = Global.mapperFacade.map(collectBondRelationBillF, CollectBondRelationBillE.class);
        collectBondRelationBillMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param collectBondRelationBillF 根据Id更新
    */
    public void update(CollectBondRelationBillUpdateF collectBondRelationBillF){
        if (collectBondRelationBillF.getId() == null) {
            throw new IllegalArgumentException();
        }
        CollectBondRelationBillE map = Global.mapperFacade.map(collectBondRelationBillF, CollectBondRelationBillE.class);
        collectBondRelationBillMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        collectBondRelationBillMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<CollectBondRelationBillV> page(PageF<CollectBondRelationBillPageF> request) {
        CollectBondRelationBillPageF conditions = request.getConditions();
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(CollectBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(CollectBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(CollectBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(CollectBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(CollectBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(CollectBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(CollectBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(CollectBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(CollectBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(CollectBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(CollectBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(CollectBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(CollectBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, tenantId());

        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(CollectBondRelationBillE.GMT_CREATE);
        }
        Page<CollectBondRelationBillE> page = collectBondRelationBillMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), CollectBondRelationBillV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<CollectBondRelationBillV> frontPage(PageF<SearchF<CollectBondRelationBillE>> request) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        SearchF<CollectBondRelationBillE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(CollectBondRelationBillE.TENANT_ID, tenantId())
                .orderByDesc(CollectBondRelationBillE.GMT_CREATE);

        Page<CollectBondRelationBillE> page = collectBondRelationBillMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), CollectBondRelationBillV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的CollectBondRelationBillE中仅包含a字段的值
    *
    * @param fields CollectBondRelationBillE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public CollectBondRelationBillE selectOneBy(Consumer<QueryWrapper<CollectBondRelationBillE>> consumer,String... fields) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return collectBondRelationBillMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的CollectBondRelationBillE中id字段的值, select 指定字段
    *
    * @param fields CollectBondRelationBillE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<CollectBondRelationBillE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(collectBondRelationBillMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<CollectBondRelationBillE>> consumer) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        collectBondRelationBillMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的CollectBondRelationBillE中仅包含a字段的值
     *
     * @param fields CollectBondRelationBillE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<CollectBondRelationBillE> selectListBy(Consumer<QueryWrapper<CollectBondRelationBillE>> consumer,String... fields) {
         QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return collectBondRelationBillMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的CollectBondRelationBillE中仅包含a字段的值
    *
    * @param fields CollectBondRelationBillE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<CollectBondRelationBillE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<CollectBondRelationBillE>> consumer, String... fields) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return collectBondRelationBillMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的CollectBondRelationBillE中id字段的值, select 指定字段
     *
     * @param fields CollectBondRelationBillE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<CollectBondRelationBillE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<CollectBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<CollectBondRelationBillE> page = Page.of(pageNum, pageSize, count);
        Page<CollectBondRelationBillE> queryPage = collectBondRelationBillMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
