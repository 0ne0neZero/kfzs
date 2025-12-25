package com.wishare.contract.domains.service.revision.income.settdetails;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionListF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionPageF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeConcludeSettdeductionE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import com.wishare.contract.domains.mapper.revision.income.settdetails.ContractIncomeConcludeSettdeductionMapper;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionListV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class ContractIncomeConcludeSettdeductionService extends ServiceImpl<ContractIncomeConcludeSettdeductionMapper, ContractIncomeConcludeSettdeductionE>  {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeSettdeductionMapper contractIncomeConcludeSettdeductionMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeConcludeSettdeductionV> get(ContractPayConcludeSettdeductionF conditions){
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        
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

        ContractIncomeConcludeSettdeductionE contractPayConcludeSettdeductionE = contractIncomeConcludeSettdeductionMapper.selectOne(queryWrapper);
        if (contractPayConcludeSettdeductionE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayConcludeSettdeductionE, ContractIncomeConcludeSettdeductionV.class));
        }else {
            return Optional.empty();
        }
    }


    public String save(ContractIncomeConcludeSettdeductionSaveF contractIncomeConcludeSettdeductionF){
        ContractIncomeConcludeSettdeductionE map = Global.mapperFacade.map(contractIncomeConcludeSettdeductionF, ContractIncomeConcludeSettdeductionE.class);
        contractIncomeConcludeSettdeductionMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractIncomeConcludeSettdeductionF 根据Id更新
    */
    public void update(ContractIncomeConcludeSettdeductionUpdateF contractIncomeConcludeSettdeductionF){
        if (contractIncomeConcludeSettdeductionF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeConcludeSettdeductionE map = Global.mapperFacade.map(contractIncomeConcludeSettdeductionF, ContractIncomeConcludeSettdeductionE.class);
        contractIncomeConcludeSettdeductionMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractIncomeConcludeSettdeductionMapper.deleteById(id);
        return true;
    }




   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeConcludeSettdeductionE selectOneBy(Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeSettdeductionMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeSettdeductionE中id字段的值, select 指定字段
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeConcludeSettdeductionMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>> consumer) {
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeConcludeSettdeductionMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
     *
     * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeConcludeSettdeductionE> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeConcludeSettdeductionMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayConcludeSettdeductionE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeConcludeSettdeductionE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeSettdeductionMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeSettdeductionE中id字段的值, select 指定字段
     *
     * @param fields ContractPayConcludeSettdeductionE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeConcludeSettdeductionE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeConcludeSettdeductionE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeConcludeSettdeductionE> queryPage = contractIncomeConcludeSettdeductionMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    public List<ContractIncomeConcludeSettdeductionV> getDetailsBySettlementId(String id){
        LambdaQueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeSettdeductionE::getSettlementId, id);
        List<ContractIncomeConcludeSettdeductionE> contractIncomeSettlementConcludeEList = contractIncomeConcludeSettdeductionMapper.selectList(queryWrapper);
        List<ContractIncomeConcludeSettdeductionV> contractIncomeSettDetailsVList = Global.mapperFacade.mapAsList(contractIncomeSettlementConcludeEList, ContractIncomeConcludeSettdeductionV.class);
        return contractIncomeSettDetailsVList;
    }

}
