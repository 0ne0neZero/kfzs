package com.wishare.contract.domains.service.revision.bond.pay;

import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.mapper.revision.bond.pay.PayBondRelationBillMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillV;
import com.wishare.contract.domains.vo.revision.bond.pay.PayBondRelationBillListV;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillPageF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillUpdateF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondRelationBillListF;
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
 * 缴纳保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class PayBondRelationBillService extends ServiceImpl<PayBondRelationBillMapper, PayBondRelationBillE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private PayBondRelationBillMapper payBondRelationBillMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<PayBondRelationBillV> get(PayBondRelationBillF conditions){
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(PayBondRelationBillE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(PayBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(PayBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(PayBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(PayBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(PayBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(PayBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(PayBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(PayBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(PayBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(PayBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }

        queryWrapper.eq(PayBondRelationBillE.TENANT_ID, tenantId());

        PayBondRelationBillE payBondRelationBillE = payBondRelationBillMapper.selectOne(queryWrapper);
        if (payBondRelationBillE != null) {
            return Optional.of(Global.mapperFacade.map(payBondRelationBillE, PayBondRelationBillV.class));
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
    public PayBondRelationBillListV list(PayBondRelationBillListF conditions){
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(PayBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(PayBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(PayBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(PayBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(PayBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(PayBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(PayBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(PayBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(PayBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(PayBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(PayBondRelationBillE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(PayBondRelationBillE.ID);
        queryWrapper.eq(PayBondRelationBillE.TENANT_ID, tenantId());
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<PayBondRelationBillV> retVList = Global.mapperFacade.mapAsList(payBondRelationBillMapper.selectList(queryWrapper),PayBondRelationBillV.class);
        PayBondRelationBillListV retV = new PayBondRelationBillListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(PayBondRelationBillSaveF payBondRelationBillF){
        PayBondRelationBillE map = Global.mapperFacade.map(payBondRelationBillF, PayBondRelationBillE.class);
        payBondRelationBillMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param payBondRelationBillF 根据Id更新
    */
    public void update(PayBondRelationBillUpdateF payBondRelationBillF){
        if (payBondRelationBillF.getId() == null) {
            throw new IllegalArgumentException();
        }
        PayBondRelationBillE map = Global.mapperFacade.map(payBondRelationBillF, PayBondRelationBillE.class);
        payBondRelationBillMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        payBondRelationBillMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<PayBondRelationBillV> page(PageF<PayBondRelationBillPageF> request) {
        PayBondRelationBillPageF conditions = request.getConditions();
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBondId())) {
            queryWrapper.eq(PayBondRelationBillE.BOND_ID, conditions.getBondId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(PayBondRelationBillE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(PayBondRelationBillE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(PayBondRelationBillE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getDealWayCode())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY_CODE, conditions.getDealWayCode());
        }

        if (StringUtils.isNotBlank(conditions.getDealWay())) {
            queryWrapper.eq(PayBondRelationBillE.DEAL_WAY, conditions.getDealWay());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(PayBondRelationBillE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getDealDate())) {
            queryWrapper.gt(PayBondRelationBillE.DEAL_DATE, conditions.getDealDate());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(PayBondRelationBillE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getReason())) {
            queryWrapper.eq(PayBondRelationBillE.REASON, conditions.getReason());
        }

        if (Objects.nonNull(conditions.getResidueAmount())) {
            queryWrapper.eq(PayBondRelationBillE.RESIDUE_AMOUNT, conditions.getResidueAmount());
        }

        if (Objects.nonNull(conditions.getProcId())) {
            queryWrapper.eq(PayBondRelationBillE.PROC_ID, conditions.getProcId());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(PayBondRelationBillE.STATUS, conditions.getStatus());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(PayBondRelationBillE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(PayBondRelationBillE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(PayBondRelationBillE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(PayBondRelationBillE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        queryWrapper.eq(PayBondRelationBillE.TENANT_ID, tenantId());
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(PayBondRelationBillE.GMT_CREATE);
        }
        Page<PayBondRelationBillE> page = payBondRelationBillMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), PayBondRelationBillV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<PayBondRelationBillV> frontPage(PageF<SearchF<PayBondRelationBillE>> request) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        SearchF<PayBondRelationBillE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(PayBondRelationBillE.TENANT_ID, tenantId())
                .orderByDesc(PayBondRelationBillE.GMT_CREATE);

        Page<PayBondRelationBillE> page = payBondRelationBillMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), PayBondRelationBillV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的PayBondRelationBillE中仅包含a字段的值
    *
    * @param fields PayBondRelationBillE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public PayBondRelationBillE selectOneBy(Consumer<QueryWrapper<PayBondRelationBillE>> consumer,String... fields) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return payBondRelationBillMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的PayBondRelationBillE中id字段的值, select 指定字段
    *
    * @param fields PayBondRelationBillE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<PayBondRelationBillE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(payBondRelationBillMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<PayBondRelationBillE>> consumer) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        payBondRelationBillMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的PayBondRelationBillE中仅包含a字段的值
     *
     * @param fields PayBondRelationBillE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<PayBondRelationBillE> selectListBy(Consumer<QueryWrapper<PayBondRelationBillE>> consumer,String... fields) {
         QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return payBondRelationBillMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的PayBondRelationBillE中仅包含a字段的值
    *
    * @param fields PayBondRelationBillE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<PayBondRelationBillE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<PayBondRelationBillE>> consumer, String... fields) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return payBondRelationBillMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的PayBondRelationBillE中id字段的值, select 指定字段
     *
     * @param fields PayBondRelationBillE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<PayBondRelationBillE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<PayBondRelationBillE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<PayBondRelationBillE> page = Page.of(pageNum, pageSize, count);
        Page<PayBondRelationBillE> queryPage = payBondRelationBillMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
