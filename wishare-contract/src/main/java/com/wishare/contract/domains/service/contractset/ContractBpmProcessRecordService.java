package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import com.wishare.contract.domains.mapper.contractset.ContractBpmProcessRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractBpmProcessRecordV;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordPageF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordUpdateF;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractBpmProcessRecordFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 
 * </p>
 *
 * @author jinhui
 * @since 2023-02-24
 */
@Service
@Slf4j
public class ContractBpmProcessRecordService extends ServiceImpl<ContractBpmProcessRecordMapper, ContractBpmProcessRecordE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractBpmProcessRecordMapper contractBpmProcessRecordMapper;


    /**
    * 根据请求参数获取指定对象
    *
    * @param contractBpmProcessRecordF 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractBpmProcessRecordV> get(ContractBpmProcessRecordF contractBpmProcessRecordF){
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(contractBpmProcessRecordF.getId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.ID, contractBpmProcessRecordF.getId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getProcessId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.PROCESS_ID, contractBpmProcessRecordF.getProcessId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getBpmBoUuid())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.BPM_BO_UUID, contractBpmProcessRecordF.getBpmBoUuid());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getType())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TYPE, contractBpmProcessRecordF.getType());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getTenantId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TENANT_ID, contractBpmProcessRecordF.getTenantId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getCreator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR, contractBpmProcessRecordF.getCreator());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getCreatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR_NAME, contractBpmProcessRecordF.getCreatorName());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getGmtCreate())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_CREATE, contractBpmProcessRecordF.getGmtCreate());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getOperator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR, contractBpmProcessRecordF.getOperator());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getOperatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR_NAME, contractBpmProcessRecordF.getOperatorName());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getDeleted())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.DELETED, contractBpmProcessRecordF.getDeleted());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getGmtModify())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_MODIFY, contractBpmProcessRecordF.getGmtModify());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getReviewStatus())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REVIEW_STATUS, contractBpmProcessRecordF.getReviewStatus());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getRejectReason())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REJECT_REASON, contractBpmProcessRecordF.getRejectReason());
        }
        ContractBpmProcessRecordE contractBpmProcessRecordE = contractBpmProcessRecordMapper.selectOne(queryWrapper);
        if (contractBpmProcessRecordE != null) {
            return Optional.of(Global.mapperFacade.map(contractBpmProcessRecordE, ContractBpmProcessRecordV.class));
        }else {
            return Optional.empty();
        }
    }

    /**
    * 列表接口，一般用于下拉列表
    *
    * @param contractBpmProcessRecordF 根据Id更新
    * @param limit 限制查询数量，null时查询20
    * @return 下拉列表
    */
    @Nonnull
    public List<ContractBpmProcessRecordV> list(ContractBpmProcessRecordF contractBpmProcessRecordF,Integer limit){
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(contractBpmProcessRecordF.getId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.ID, contractBpmProcessRecordF.getId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getProcessId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.PROCESS_ID, contractBpmProcessRecordF.getProcessId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getBpmBoUuid())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.BPM_BO_UUID, contractBpmProcessRecordF.getBpmBoUuid());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getType())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TYPE, contractBpmProcessRecordF.getType());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getTenantId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TENANT_ID, contractBpmProcessRecordF.getTenantId());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getCreator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR, contractBpmProcessRecordF.getCreator());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getCreatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR_NAME, contractBpmProcessRecordF.getCreatorName());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getGmtCreate())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_CREATE, contractBpmProcessRecordF.getGmtCreate());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getOperator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR, contractBpmProcessRecordF.getOperator());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getOperatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR_NAME, contractBpmProcessRecordF.getOperatorName());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getDeleted())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.DELETED, contractBpmProcessRecordF.getDeleted());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getGmtModify())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_MODIFY, contractBpmProcessRecordF.getGmtModify());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getReviewStatus())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REVIEW_STATUS, contractBpmProcessRecordF.getReviewStatus());
        }
        if (Objects.nonNull(contractBpmProcessRecordF.getRejectReason())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REJECT_REASON, contractBpmProcessRecordF.getRejectReason());
        }
        queryWrapper.last("limit " + Optional.ofNullable(limit).orElse(20));
        return Global.mapperFacade.mapAsList(contractBpmProcessRecordMapper.selectList(queryWrapper),ContractBpmProcessRecordV.class);
    }

    public Long save(ContractBpmProcessRecordSaveF contractBpmProcessRecordF){
        ContractBpmProcessRecordE map = Global.mapperFacade.map(contractBpmProcessRecordF, ContractBpmProcessRecordE.class);
        contractBpmProcessRecordMapper.insert(map);
        return map.getId();
    }


    /**
    * 根据Id更新
    *
    * @param contractBpmProcessRecordF 根据Id更新
    */
    public void update(ContractBpmProcessRecordUpdateF contractBpmProcessRecordF){
        if (contractBpmProcessRecordF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractBpmProcessRecordE map = Global.mapperFacade.map(contractBpmProcessRecordF, ContractBpmProcessRecordE.class);
        contractBpmProcessRecordMapper.updateById(map);
    }

    /**
    *
    * @param {table.convertTableName?uncap_first}F 根据Id删除
    * @return 删除结果
    */
    public boolean remove(ContractBpmProcessRecordF contractBpmProcessRecordF){
        if (contractBpmProcessRecordF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractBpmProcessRecordE map = Global.mapperFacade.map(contractBpmProcessRecordF, ContractBpmProcessRecordE.class);
        contractBpmProcessRecordMapper.deleteById(map);
        return true;
    }

    /**
    * 该接口供给后端
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractBpmProcessRecordV> page(PageF<ContractBpmProcessRecordPageF> request) {
        ContractBpmProcessRecordPageF conditions = request.getConditions();
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(conditions.getId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.ID, conditions.getId());
        }
        if (StringUtils.isNotBlank(conditions.getProcessId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.PROCESS_ID, conditions.getProcessId());
        }
        if (StringUtils.isNotBlank(conditions.getBpmBoUuid())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.BPM_BO_UUID, conditions.getBpmBoUuid());
        }
        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TYPE, conditions.getType());
        }
        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.TENANT_ID, conditions.getTenantId());
        }
        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR, conditions.getCreator());
        }
        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.CREATOR_NAME, conditions.getCreatorName());
        }
        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_CREATE, conditions.getGmtCreate());
        }
        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR, conditions.getOperator());
        }
        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.OPERATOR_NAME, conditions.getOperatorName());
        }
        if (Objects.nonNull(conditions.getDeleted())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.DELETED, conditions.getDeleted());
        }
        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REVIEW_STATUS, conditions.getReviewStatus());
        }
        if (StringUtils.isNotBlank(conditions.getRejectReason())) {
            queryWrapper.eq(ContractBpmProcessRecordFieldConst.REJECT_REASON, conditions.getRejectReason());
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractBpmProcessRecordE> page = contractBpmProcessRecordMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractBpmProcessRecordV.class));
    }

    /**
    * 该接口供给给前端
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractBpmProcessRecordV> frontPage(PageF<SearchF<ContractBpmProcessRecordE>> request) {
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper;
        if (request.getConditions() != null) {
            queryWrapper = request.getConditions().getQueryModel();
        }else {
            queryWrapper = new QueryWrapper<>();
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractBpmProcessRecordE> page = contractBpmProcessRecordMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractBpmProcessRecordV.class));
    }

    /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractBpmProcessRecordE中仅包含a字段的值
    * @param fields ContractBpmProcessRecordFieldConst 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractBpmProcessRecordE selectOneBy(Consumer<QueryWrapper<ContractBpmProcessRecordE>> consumer,String... fields) {
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractBpmProcessRecordMapper.selectOne(queryWrapper);
    }

    /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractBpmProcessRecordE中id字段的值, select 指定字段
    * @param fields ContractBpmProcessRecordFieldConst 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractBpmProcessRecordE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractBpmProcessRecordMapper.selectList(queryWrapper), retClazz);
    }

     /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractBpmProcessRecordE中仅包含a字段的值
     * @param fields ContractBpmProcessRecordFieldConst 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractBpmProcessRecordE> selectListBy(Consumer<QueryWrapper<ContractBpmProcessRecordE>> consumer,String... fields) {
         QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractBpmProcessRecordMapper.selectList(queryWrapper);
    }

    /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractBpmProcessRecordE中仅包含a字段的值
    * @param fields ContractBpmProcessRecordFieldConst 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractBpmProcessRecordE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractBpmProcessRecordE>> consumer, String... fields) {
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractBpmProcessRecordMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

     /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractBpmProcessRecordE中id字段的值, select 指定字段
     * @param fields ContractBpmProcessRecordFieldConst 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractBpmProcessRecordE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractBpmProcessRecordE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractBpmProcessRecordE> page = Page.of(pageNum, pageSize, count);
        Page<ContractBpmProcessRecordE> queryPage = contractBpmProcessRecordMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }
}
