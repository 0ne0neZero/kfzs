package com.wishare.contract.domains.service.revision.pay.settdetails;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPayConcludeSettdeductionMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionListV;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionPageF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionListF;
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
 * 结算单扣款明细表信息
 * </p>
 *
 * @author zhangfy
 * @since 2024-05-20
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPayConcludeSettdeductionService extends ServiceImpl<ContractPayConcludeSettdeductionMapper, ContractPayConcludeSettdeductionE>  {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeSettdeductionMapper contractPayConcludeSettdeductionMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractPayConcludeSettdeductionV> get(ContractPayConcludeSettdeductionF conditions){
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getSettlementId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.SETTLEMENT_ID, conditions.getSettlementId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractPayConcludeSettdeductionE contractPayConcludeSettdeductionE = contractPayConcludeSettdeductionMapper.selectOne(queryWrapper);
        if (contractPayConcludeSettdeductionE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayConcludeSettdeductionE, ContractPayConcludeSettdeductionV.class));
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
    public ContractPayConcludeSettdeductionListV list(ContractPayConcludeSettdeductionListF conditions){
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getSettlementId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.SETTLEMENT_ID, conditions.getSettlementId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractPayConcludeSettdeductionE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractPayConcludeSettdeductionE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractPayConcludeSettdeductionV> retVList = Global.mapperFacade.mapAsList(contractPayConcludeSettdeductionMapper.selectList(queryWrapper),ContractPayConcludeSettdeductionV.class);
        ContractPayConcludeSettdeductionListV retV = new ContractPayConcludeSettdeductionListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractPayConcludeSettdeductionSaveF contractPayConcludeSettdeductionF){
        ContractPayConcludeSettdeductionE map = Global.mapperFacade.map(contractPayConcludeSettdeductionF, ContractPayConcludeSettdeductionE.class);
        contractPayConcludeSettdeductionMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractPayConcludeSettdeductionF 根据Id更新
    */
    public void update(ContractPayConcludeSettdeductionUpdateF contractPayConcludeSettdeductionF){
        if (contractPayConcludeSettdeductionF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayConcludeSettdeductionE map = Global.mapperFacade.map(contractPayConcludeSettdeductionF, ContractPayConcludeSettdeductionE.class);
        contractPayConcludeSettdeductionMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractPayConcludeSettdeductionMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeSettdeductionV> page(PageF<ContractPayConcludeSettdeductionPageF> request) {
        ContractPayConcludeSettdeductionPageF conditions = request.getConditions();
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getSettlementId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.SETTLEMENT_ID, conditions.getSettlementId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeSettdeductionE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeSettdeductionE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayConcludeSettdeductionE.GMT_CREATE);
        }
        Page<ContractPayConcludeSettdeductionE> page = contractPayConcludeSettdeductionMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeSettdeductionV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeSettdeductionV> frontPage(PageF<SearchF<ContractPayConcludeSettdeductionE>> request) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayConcludeSettdeductionE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractPayConcludeSettdeductionE> page = contractPayConcludeSettdeductionMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeSettdeductionV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayConcludeSettdeductionE selectOneBy(Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>> consumer,String... fields) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeSettdeductionMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeSettdeductionE中id字段的值, select 指定字段
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayConcludeSettdeductionMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>> consumer) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayConcludeSettdeductionMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
     *
     * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayConcludeSettdeductionE> selectListBy(Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>> consumer,String... fields) {
         QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayConcludeSettdeductionMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayConcludeSettdeductionE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>> consumer, String... fields) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeSettdeductionMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeSettdeductionE中id字段的值, select 指定字段
     *
     * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayConcludeSettdeductionE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayConcludeSettdeductionE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayConcludeSettdeductionE> queryPage = contractPayConcludeSettdeductionMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    public List<ContractPayConcludeSettdeductionV> getDetailsBySettlementId(String id){
        LambdaQueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeSettdeductionE::getSettlementId, id);
        List<ContractPayConcludeSettdeductionE> contractPaySettlementConcludeEList = contractPayConcludeSettdeductionMapper.selectList(queryWrapper);
        List<ContractPayConcludeSettdeductionV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractPaySettlementConcludeEList, ContractPayConcludeSettdeductionV.class);
        return contractPaySettDetailsVList;
    }

}
