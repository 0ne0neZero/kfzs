package com.wishare.contract.domains.service.revision.invoice;

import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillCalculateE;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillCalculateMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateV;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateListV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculatePageF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateUpdateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateListF;
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
 * 结算单计量明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractSettlementsBillCalculateService extends ServiceImpl<ContractSettlementsBillCalculateMapper, ContractSettlementsBillCalculateE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillCalculateMapper contractSettlementsBillCalculateMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractSettlementsBillCalculateV> get(ContractSettlementsBillCalculateF conditions){
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE, conditions.getTaxRate());
        }

        if (Objects.nonNull(conditions.getTaxRateAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_AMOUNT, conditions.getTaxRateAmount());
        }

        if (Objects.nonNull(conditions.getAmountWithOutRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT_WITH_OUT_RATE, conditions.getAmountWithOutRate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractSettlementsBillCalculateE contractSettlementsBillCalculateE = contractSettlementsBillCalculateMapper.selectOne(queryWrapper);
        if (contractSettlementsBillCalculateE != null) {
            return Optional.of(Global.mapperFacade.map(contractSettlementsBillCalculateE, ContractSettlementsBillCalculateV.class));
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
    public ContractSettlementsBillCalculateListV list(ContractSettlementsBillCalculateListF conditions){
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE, conditions.getTaxRate());
        }

        if (Objects.nonNull(conditions.getTaxRateAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_AMOUNT, conditions.getTaxRateAmount());
        }

        if (Objects.nonNull(conditions.getAmountWithOutRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT_WITH_OUT_RATE, conditions.getAmountWithOutRate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractSettlementsBillCalculateE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractSettlementsBillCalculateE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractSettlementsBillCalculateV> retVList = Global.mapperFacade.mapAsList(contractSettlementsBillCalculateMapper.selectList(queryWrapper),ContractSettlementsBillCalculateV.class);
        ContractSettlementsBillCalculateListV retV = new ContractSettlementsBillCalculateListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractSettlementsBillCalculateSaveF contractSettlementsBillCalculateF){
        ContractSettlementsBillCalculateE map = Global.mapperFacade.map(contractSettlementsBillCalculateF, ContractSettlementsBillCalculateE.class);
        map.setTenantId(tenantId());
        map.setGmtCreate(LocalDateTime.now());
        map.setGmtModify(LocalDateTime.now());
        contractSettlementsBillCalculateMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractSettlementsBillCalculateF 根据Id更新
    */
    public void update(ContractSettlementsBillCalculateUpdateF contractSettlementsBillCalculateF){
        if (contractSettlementsBillCalculateF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractSettlementsBillCalculateE map = Global.mapperFacade.map(contractSettlementsBillCalculateF, ContractSettlementsBillCalculateE.class);
        contractSettlementsBillCalculateMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractSettlementsBillCalculateMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillCalculateV> page(PageF<ContractSettlementsBillCalculatePageF> request) {
        ContractSettlementsBillCalculatePageF conditions = request.getConditions();
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE, conditions.getTaxRate());
        }

        if (Objects.nonNull(conditions.getTaxRateAmount())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TAX_RATE_AMOUNT, conditions.getTaxRateAmount());
        }

        if (Objects.nonNull(conditions.getAmountWithOutRate())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.AMOUNT_WITH_OUT_RATE, conditions.getAmountWithOutRate());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillCalculateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillCalculateE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractSettlementsBillCalculateE.GMT_CREATE);
        }
        Page<ContractSettlementsBillCalculateE> page = contractSettlementsBillCalculateMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillCalculateV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillCalculateV> frontPage(PageF<SearchF<ContractSettlementsBillCalculateE>> request) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractSettlementsBillCalculateE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractSettlementsBillCalculateE> page = contractSettlementsBillCalculateMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillCalculateV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractSettlementsBillCalculateE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillCalculateE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractSettlementsBillCalculateE selectOneBy(Consumer<QueryWrapper<ContractSettlementsBillCalculateE>> consumer,String... fields) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillCalculateMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillCalculateE中id字段的值, select 指定字段
    *
    * @param fields ContractSettlementsBillCalculateE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillCalculateE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractSettlementsBillCalculateMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractSettlementsBillCalculateE>> consumer) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractSettlementsBillCalculateMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractSettlementsBillCalculateE中仅包含a字段的值
     *
     * @param fields ContractSettlementsBillCalculateE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractSettlementsBillCalculateE> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillCalculateE>> consumer,String... fields) {
         QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractSettlementsBillCalculateMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractSettlementsBillCalculateE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillCalculateE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractSettlementsBillCalculateE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractSettlementsBillCalculateE>> consumer, String... fields) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillCalculateMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillCalculateE中id字段的值, select 指定字段
     *
     * @param fields ContractSettlementsBillCalculateE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractSettlementsBillCalculateE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractSettlementsBillCalculateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractSettlementsBillCalculateE> page = Page.of(pageNum, pageSize, count);
        Page<ContractSettlementsBillCalculateE> queryPage = contractSettlementsBillCalculateMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
