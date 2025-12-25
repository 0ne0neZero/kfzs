package com.wishare.contract.domains.service.revision.template;

import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import com.wishare.contract.domains.mapper.revision.template.ContractRecordInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoListV;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoPageF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoUpdateF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoListF;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static java.time.LocalDateTime.now;

/**
 * <p>
 * 合同修改记录表
 * </p>
 *
 * @author zhangfuyu
 * @since 2023-07-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractRecordInfoService extends ServiceImpl<ContractRecordInfoMapper, ContractRecordInfoE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRecordInfoMapper contractRecordInfoMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractRecordInfoV> get(ContractRecordInfoF conditions){
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractRecordInfoE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateId())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_ID, conditions.getTemplateId());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateName())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_NAME, conditions.getTemplateName());
        }

        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractRecordInfoE.VERSION, conditions.getVersion());
        }

        if (StringUtils.isNotBlank(conditions.getFieldRecord())) {
            queryWrapper.eq(ContractRecordInfoE.FIELD_RECORD, conditions.getFieldRecord());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRecordInfoE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_MODIFY, conditions.getGmtModify());
        }
        ContractRecordInfoE contractRecordInfoE = contractRecordInfoMapper.selectOne(queryWrapper);
        if (contractRecordInfoE != null) {
            return Optional.of(Global.mapperFacade.map(contractRecordInfoE, ContractRecordInfoV.class));
        }else {
            return Optional.empty();
        }
    }

    /**
     * 列表接口，一般用于下拉列表
     *
     * @param contractId 根据Id更新
     * @return 下拉列表
     */
    public ContractRecordInfoE insertOneRecord(String contractId,
                               String contractName,
                               String templateId,
                               String templateName,
                               String fieldRecord,
                               String fileName,
                               Integer fileSize){
        ContractRecordInfoE contractRecordInfoE = new ContractRecordInfoE();
        contractRecordInfoE.setContractId(contractId);
        contractRecordInfoE.setFileName(fileName);
        contractRecordInfoE.setFileSize(fileSize);
        contractRecordInfoE.setContractName(contractName);
        contractRecordInfoE.setTemplateId(templateId);
        contractRecordInfoE.setTemplateName(templateName);
        contractRecordInfoE.setFieldRecord(fieldRecord);
        contractRecordInfoE.setTenantId(tenantId());
        contractRecordInfoE.setGmtCreate(now());
        contractRecordInfoE.setGmtModify(now());
        contractRecordInfoMapper.insert(contractRecordInfoE);
        return contractRecordInfoE;
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public ContractRecordInfoListV list(ContractRecordInfoListF conditions){
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateId())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_ID, conditions.getTemplateId());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateName())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_NAME, conditions.getTemplateName());
        }

        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractRecordInfoE.VERSION, conditions.getVersion());
        }

        if (StringUtils.isNotBlank(conditions.getFieldRecord())) {
            queryWrapper.eq(ContractRecordInfoE.FIELD_RECORD, conditions.getFieldRecord());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRecordInfoE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractRecordInfoE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractRecordInfoE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractRecordInfoV> retVList = Global.mapperFacade.mapAsList(contractRecordInfoMapper.selectList(queryWrapper),ContractRecordInfoV.class);
        ContractRecordInfoListV retV = new ContractRecordInfoListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractRecordInfoSaveF contractRecordInfoF){
        ContractRecordInfoE map = Global.mapperFacade.map(contractRecordInfoF, ContractRecordInfoE.class);
        contractRecordInfoMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractRecordInfoF 根据Id更新
    */
    public void update(ContractRecordInfoUpdateF contractRecordInfoF){
        if (contractRecordInfoF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractRecordInfoE map = Global.mapperFacade.map(contractRecordInfoF, ContractRecordInfoE.class);
        contractRecordInfoMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractRecordInfoMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractRecordInfoV> page(PageF<ContractRecordInfoPageF> request) {
        ContractRecordInfoPageF conditions = request.getConditions();
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(ContractRecordInfoE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateId())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_ID, conditions.getTemplateId());
        }

        if (StringUtils.isNotBlank(conditions.getTemplateName())) {
            queryWrapper.eq(ContractRecordInfoE.TEMPLATE_NAME, conditions.getTemplateName());
        }

        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractRecordInfoE.VERSION, conditions.getVersion());
        }

        if (StringUtils.isNotBlank(conditions.getFieldRecord())) {
            queryWrapper.eq(ContractRecordInfoE.FIELD_RECORD, conditions.getFieldRecord());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractRecordInfoE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractRecordInfoE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractRecordInfoE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractRecordInfoE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractRecordInfoE.GMT_CREATE);
        }
        Page<ContractRecordInfoE> page = contractRecordInfoMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractRecordInfoV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractRecordInfoV> frontPage(PageF<SearchF<ContractRecordInfoE>> request) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractRecordInfoE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        queryWrapper.eq(ContractRecordInfoE.DELETED, false);
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractRecordInfoE> page = contractRecordInfoMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractRecordInfoV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractRecordInfoE中仅包含a字段的值
    *
    * @param fields ContractRecordInfoE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractRecordInfoE selectOneBy(Consumer<QueryWrapper<ContractRecordInfoE>> consumer,String... fields) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractRecordInfoMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractRecordInfoE中id字段的值, select 指定字段
    *
    * @param fields ContractRecordInfoE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractRecordInfoE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractRecordInfoMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractRecordInfoE>> consumer) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractRecordInfoMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractRecordInfoE中仅包含a字段的值
     *
     * @param fields ContractRecordInfoE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractRecordInfoE> selectListBy(Consumer<QueryWrapper<ContractRecordInfoE>> consumer,String... fields) {
         QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractRecordInfoMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractRecordInfoE中仅包含a字段的值
    *
    * @param fields ContractRecordInfoE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractRecordInfoE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractRecordInfoE>> consumer, String... fields) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractRecordInfoMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractRecordInfoE中id字段的值, select 指定字段
     *
     * @param fields ContractRecordInfoE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractRecordInfoE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractRecordInfoE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractRecordInfoE> page = Page.of(pageNum, pageSize, count);
        Page<ContractRecordInfoE> queryPage = contractRecordInfoMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
