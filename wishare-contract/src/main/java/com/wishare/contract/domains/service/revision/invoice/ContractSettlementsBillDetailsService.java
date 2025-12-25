package com.wishare.contract.domains.service.revision.invoice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillDetailsE;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillDetailsMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsV;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsListV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsPageF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsUpdateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsListF;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 收票明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-10
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractSettlementsBillDetailsService extends ServiceImpl<ContractSettlementsBillDetailsMapper, ContractSettlementsBillDetailsE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillDetailsMapper contractSettlementsBillDetailsMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractSettlementsBillDetailsV> get(ContractSettlementsBillDetailsF conditions){
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNum())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_NUM, conditions.getBillNum());
        }

        if (StringUtils.isNotBlank(conditions.getBillCode())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_CODE, conditions.getBillCode());
        }

        if (Objects.nonNull(conditions.getBillType())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_TYPE, conditions.getBillType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getBillDate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.BILL_DATE, conditions.getBillDate());
        }

        if (StringUtils.isNotBlank(conditions.getExtend1())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND1, conditions.getExtend1());
        }

        if (StringUtils.isNotBlank(conditions.getExtend2())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND2, conditions.getExtend2());
        }

        if (StringUtils.isNotBlank(conditions.getExtend3())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND3, conditions.getExtend3());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.TENANT_ID, conditions.getTenantId());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR_NAME, conditions.getOperatorName());
        }
        ContractSettlementsBillDetailsE contractSettlementsBillDetailsE = contractSettlementsBillDetailsMapper.selectOne(queryWrapper);
        if (contractSettlementsBillDetailsE != null) {
            return Optional.of(Global.mapperFacade.map(contractSettlementsBillDetailsE, ContractSettlementsBillDetailsV.class));
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
    public ContractSettlementsBillDetailsListV list(ContractSettlementsBillDetailsListF conditions){
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNum())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_NUM, conditions.getBillNum());
        }

        if (StringUtils.isNotBlank(conditions.getBillCode())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_CODE, conditions.getBillCode());
        }

        if (Objects.nonNull(conditions.getBillType())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_TYPE, conditions.getBillType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getBillDate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.BILL_DATE, conditions.getBillDate());
        }

        if (StringUtils.isNotBlank(conditions.getExtend1())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND1, conditions.getExtend1());
        }

        if (StringUtils.isNotBlank(conditions.getExtend2())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND2, conditions.getExtend2());
        }

        if (StringUtils.isNotBlank(conditions.getExtend3())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND3, conditions.getExtend3());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.TENANT_ID, conditions.getTenantId());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR_NAME, conditions.getOperatorName());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractSettlementsBillDetailsE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractSettlementsBillDetailsE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractSettlementsBillDetailsV> retVList = Global.mapperFacade.mapAsList(contractSettlementsBillDetailsMapper.selectList(queryWrapper),ContractSettlementsBillDetailsV.class);
        ContractSettlementsBillDetailsListV retV = new ContractSettlementsBillDetailsListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractSettlementsBillDetailsSaveF contractSettlementsBillDetailsF){
        ContractSettlementsBillDetailsE map = Global.mapperFacade.map(contractSettlementsBillDetailsF, ContractSettlementsBillDetailsE.class);
        map.setTenantId(tenantId());
        map.setGmtCreate(LocalDateTime.now());
        map.setGmtModify(LocalDateTime.now());
        contractSettlementsBillDetailsMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractSettlementsBillDetailsF 根据Id更新
    */
    public void update(ContractSettlementsBillDetailsUpdateF contractSettlementsBillDetailsF){
        if (contractSettlementsBillDetailsF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractSettlementsBillDetailsE map = Global.mapperFacade.map(contractSettlementsBillDetailsF, ContractSettlementsBillDetailsE.class);
        contractSettlementsBillDetailsMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractSettlementsBillDetailsMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillDetailsV> page(PageF<ContractSettlementsBillDetailsPageF> request) {
        ContractSettlementsBillDetailsPageF conditions = request.getConditions();
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBillId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNum())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_NUM, conditions.getBillNum());
        }

        if (StringUtils.isNotBlank(conditions.getBillCode())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_CODE, conditions.getBillCode());
        }

        if (Objects.nonNull(conditions.getBillType())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.BILL_TYPE, conditions.getBillType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getBillDate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.BILL_DATE, conditions.getBillDate());
        }

        if (StringUtils.isNotBlank(conditions.getExtend1())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND1, conditions.getExtend1());
        }

        if (StringUtils.isNotBlank(conditions.getExtend2())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND2, conditions.getExtend2());
        }

        if (StringUtils.isNotBlank(conditions.getExtend3())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.EXTEND3, conditions.getExtend3());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.TENANT_ID, conditions.getTenantId());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractSettlementsBillDetailsE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractSettlementsBillDetailsE.OPERATOR_NAME, conditions.getOperatorName());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractSettlementsBillDetailsE.GMT_CREATE);
        }
        Page<ContractSettlementsBillDetailsE> page = contractSettlementsBillDetailsMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillDetailsV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractSettlementsBillDetailsV> frontPage(PageF<SearchF<ContractSettlementsBillDetailsE>> request) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractSettlementsBillDetailsE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractSettlementsBillDetailsE> page = contractSettlementsBillDetailsMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractSettlementsBillDetailsV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractSettlementsBillDetailsE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillDetailsE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractSettlementsBillDetailsE selectOneBy(Consumer<QueryWrapper<ContractSettlementsBillDetailsE>> consumer,String... fields) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillDetailsMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillDetailsE中id字段的值, select 指定字段
    *
    * @param fields ContractSettlementsBillDetailsE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillDetailsE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractSettlementsBillDetailsMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractSettlementsBillDetailsE>> consumer) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractSettlementsBillDetailsMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractSettlementsBillDetailsE中仅包含a字段的值
     *
     * @param fields ContractSettlementsBillDetailsE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractSettlementsBillDetailsE> selectListBy(Consumer<QueryWrapper<ContractSettlementsBillDetailsE>> consumer,String... fields) {
         QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractSettlementsBillDetailsMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractSettlementsBillDetailsE中仅包含a字段的值
    *
    * @param fields ContractSettlementsBillDetailsE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractSettlementsBillDetailsE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractSettlementsBillDetailsE>> consumer, String... fields) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractSettlementsBillDetailsMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractSettlementsBillDetailsE中id字段的值, select 指定字段
     *
     * @param fields ContractSettlementsBillDetailsE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractSettlementsBillDetailsE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractSettlementsBillDetailsE> page = Page.of(pageNum, pageSize, count);
        Page<ContractSettlementsBillDetailsE> queryPage = contractSettlementsBillDetailsMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


    @Nullable
    public List<ContractSettlementsBillDetailsV> getIsEeistBillNo(String billNum){
        LambdaQueryWrapper<ContractSettlementsBillDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractSettlementsBillDetailsE::getDeleted, 0)
                .eq(ContractSettlementsBillDetailsE::getBillNum, billNum);
        List<ContractSettlementsBillDetailsE> contractPaySettlementConcludeEList = contractSettlementsBillDetailsMapper.selectList(queryWrapper);
        List<ContractSettlementsBillDetailsV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractPaySettlementConcludeEList, ContractSettlementsBillDetailsV.class);
        return contractPaySettDetailsVList;
    }

}
