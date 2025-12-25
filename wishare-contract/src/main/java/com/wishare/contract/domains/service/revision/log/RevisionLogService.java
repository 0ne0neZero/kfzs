package com.wishare.contract.domains.service.revision.log;

import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.log.RevisionLogE;
import com.wishare.contract.domains.mapper.revision.log.RevisionLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.log.RevisionLogV;
import com.wishare.contract.domains.vo.revision.log.RevisionLogListV;
import com.wishare.contract.apps.fo.revision.log.RevisionLogF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogPageF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogSaveF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogUpdateF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogListF;
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
 * 合同改版动态记录表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-12
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class RevisionLogService extends ServiceImpl<RevisionLogMapper, RevisionLogE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionLogMapper revisionLogMapper;


    public String insertOneLog(String bizId,
                               String bizName,
                               String code) {
        RevisionLogE map = new RevisionLogE();

        map.setActionCode(code)
                .setAction(LogActionTypeEnum.parseName(code))
                .setBizId(bizId)
                .setTenantId(tenantId())
                .setGmtCreate(LocalDateTime.now())
                .setContent(bizName);

        map.setTitle(userName() + "" + map.getAction() + (Objects.isNull(bizName) ? "合同" : bizName));

        revisionLogMapper.insert(map);

        return map.getId();
    }


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<RevisionLogV> get(RevisionLogF conditions){
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(RevisionLogE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getBizId())) {
            queryWrapper.eq(RevisionLogE.BIZ_ID, conditions.getBizId());
        }

        if (StringUtils.isNotBlank(conditions.getTitle())) {
            queryWrapper.eq(RevisionLogE.TITLE, conditions.getTitle());
        }

        if (StringUtils.isNotBlank(conditions.getActionCode())) {
            queryWrapper.eq(RevisionLogE.ACTION_CODE, conditions.getActionCode());
        }

        if (StringUtils.isNotBlank(conditions.getAction())) {
            queryWrapper.eq(RevisionLogE.ACTION, conditions.getAction());
        }

        if (StringUtils.isNotBlank(conditions.getContent())) {
            queryWrapper.eq(RevisionLogE.CONTENT, conditions.getContent());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionLogE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionLogE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionLogE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionLogE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionLogE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionLogE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionLogE.GMT_MODIFY, conditions.getGmtModify());
        }

        RevisionLogE revisionLogE = revisionLogMapper.selectOne(queryWrapper);
        if (revisionLogE != null) {
            return Optional.of(Global.mapperFacade.map(revisionLogE, RevisionLogV.class));
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
    public RevisionLogListV list(RevisionLogListF conditions){
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBizId())) {
            queryWrapper.eq(RevisionLogE.BIZ_ID, conditions.getBizId());
        }

        if (StringUtils.isNotBlank(conditions.getTitle())) {
            queryWrapper.eq(RevisionLogE.TITLE, conditions.getTitle());
        }

        if (StringUtils.isNotBlank(conditions.getActionCode())) {
            queryWrapper.eq(RevisionLogE.ACTION_CODE, conditions.getActionCode());
        }

        if (StringUtils.isNotBlank(conditions.getAction())) {
            queryWrapper.eq(RevisionLogE.ACTION, conditions.getAction());
        }

        if (StringUtils.isNotBlank(conditions.getContent())) {
            queryWrapper.eq(RevisionLogE.CONTENT, conditions.getContent());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionLogE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionLogE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionLogE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionLogE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionLogE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionLogE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionLogE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(RevisionLogE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(RevisionLogE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<RevisionLogV> retVList = Global.mapperFacade.mapAsList(revisionLogMapper.selectList(queryWrapper),RevisionLogV.class);
        RevisionLogListV retV = new RevisionLogListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(RevisionLogSaveF revisionLogF){
        RevisionLogE map = Global.mapperFacade.map(revisionLogF, RevisionLogE.class);
        revisionLogMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param revisionLogF 根据Id更新
    */
    public void update(RevisionLogUpdateF revisionLogF){
        if (revisionLogF.getId() == null) {
            throw new IllegalArgumentException();
        }
        RevisionLogE map = Global.mapperFacade.map(revisionLogF, RevisionLogE.class);
        revisionLogMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        revisionLogMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<RevisionLogV> page(PageF<RevisionLogPageF> request) {
        RevisionLogPageF conditions = request.getConditions();
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getBizId())) {
            queryWrapper.eq(RevisionLogE.BIZ_ID, conditions.getBizId());
        }

        if (StringUtils.isNotBlank(conditions.getTitle())) {
            queryWrapper.eq(RevisionLogE.TITLE, conditions.getTitle());
        }

        if (StringUtils.isNotBlank(conditions.getActionCode())) {
            queryWrapper.eq(RevisionLogE.ACTION_CODE, conditions.getActionCode());
        }

        if (StringUtils.isNotBlank(conditions.getAction())) {
            queryWrapper.eq(RevisionLogE.ACTION, conditions.getAction());
        }

        if (StringUtils.isNotBlank(conditions.getContent())) {
            queryWrapper.eq(RevisionLogE.CONTENT, conditions.getContent());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionLogE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionLogE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionLogE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionLogE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionLogE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionLogE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionLogE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(RevisionLogE.GMT_CREATE);
        }
        Page<RevisionLogE> page = revisionLogMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), RevisionLogV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<RevisionLogV> frontPage(PageF<SearchF<RevisionLogE>> request) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        SearchF<RevisionLogE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<RevisionLogE> page = revisionLogMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), RevisionLogV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的RevisionLogE中仅包含a字段的值
    *
    * @param fields RevisionLogE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public RevisionLogE selectOneBy(Consumer<QueryWrapper<RevisionLogE>> consumer,String... fields) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionLogMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionLogE中id字段的值, select 指定字段
    *
    * @param fields RevisionLogE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<RevisionLogE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(revisionLogMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<RevisionLogE>> consumer) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        revisionLogMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的RevisionLogE中仅包含a字段的值
     *
     * @param fields RevisionLogE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<RevisionLogE> selectListBy(Consumer<QueryWrapper<RevisionLogE>> consumer,String... fields) {
         QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return revisionLogMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的RevisionLogE中仅包含a字段的值
    *
    * @param fields RevisionLogE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<RevisionLogE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<RevisionLogE>> consumer, String... fields) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionLogMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionLogE中id字段的值, select 指定字段
     *
     * @param fields RevisionLogE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<RevisionLogE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<RevisionLogE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<RevisionLogE> page = Page.of(pageNum, pageSize, count);
        Page<RevisionLogE> queryPage = revisionLogMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
