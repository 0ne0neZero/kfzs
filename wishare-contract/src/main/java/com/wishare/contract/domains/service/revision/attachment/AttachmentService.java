package com.wishare.contract.domains.service.revision.attachment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.contract.apps.fo.revision.attachment.*;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.ContractTypeEnum;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentListV;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 关联附件管理表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class AttachmentService extends ServiceImpl<AttachmentMapper, AttachmentE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentMapper attachmentMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<AttachmentV> get(AttachmentF conditions){
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(AttachmentE.ID, conditions.getId());
        }

        if (Objects.nonNull(conditions.getBusinessType())) {
            queryWrapper.eq(AttachmentE.BUSINESS_TYPE, conditions.getBusinessType());
        }

        if (StringUtils.isNotBlank(conditions.getBusinessId())) {
            queryWrapper.eq(AttachmentE.BUSINESS_ID, conditions.getBusinessId());
        }

        if (StringUtils.isNotBlank(conditions.getFileKey())) {
            queryWrapper.eq(AttachmentE.FILE_KEY, conditions.getFileKey());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(AttachmentE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getSuffix())) {
            queryWrapper.eq(AttachmentE.SUFFIX, conditions.getSuffix());
        }

        if (Objects.nonNull(conditions.getSize())) {
            queryWrapper.eq(AttachmentE.SIZE, conditions.getSize());
        }

        if (StringUtils.isNotBlank(conditions.getFileSizeStr())) {
            queryWrapper.eq(AttachmentE.FILE_SIZE_STR, conditions.getFileSizeStr());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(AttachmentE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(AttachmentE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(AttachmentE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(AttachmentE.OPERATOR, conditions.getOperator());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(AttachmentE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(AttachmentE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(AttachmentE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(AttachmentE.OPERATOR_NAME, conditions.getOperatorName());
        }
        AttachmentE attachmentE = attachmentMapper.selectOne(queryWrapper);
        if (attachmentE != null) {
            return Optional.of(Global.mapperFacade.map(attachmentE, AttachmentV.class));
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
    public AttachmentListV list(AttachmentListF conditions){
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        

        if (Objects.nonNull(conditions.getBusinessType())) {
            queryWrapper.eq(AttachmentE.BUSINESS_TYPE, conditions.getBusinessType());
        }

        if (StringUtils.isNotBlank(conditions.getBusinessId())) {
            queryWrapper.eq(AttachmentE.BUSINESS_ID, conditions.getBusinessId());
        }

        if (StringUtils.isNotBlank(conditions.getFileKey())) {
            queryWrapper.eq(AttachmentE.FILE_KEY, conditions.getFileKey());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(AttachmentE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getSuffix())) {
            queryWrapper.eq(AttachmentE.SUFFIX, conditions.getSuffix());
        }

        if (Objects.nonNull(conditions.getSize())) {
            queryWrapper.eq(AttachmentE.SIZE, conditions.getSize());
        }

        if (StringUtils.isNotBlank(conditions.getFileSizeStr())) {
            queryWrapper.eq(AttachmentE.FILE_SIZE_STR, conditions.getFileSizeStr());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(AttachmentE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(AttachmentE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(AttachmentE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(AttachmentE.OPERATOR, conditions.getOperator());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(AttachmentE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(AttachmentE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(AttachmentE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(AttachmentE.OPERATOR_NAME, conditions.getOperatorName());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(AttachmentE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(AttachmentE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<AttachmentV> retVList = Global.mapperFacade.mapAsList(attachmentMapper.selectList(queryWrapper),AttachmentV.class);
        AttachmentListV retV = new AttachmentListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(AttachmentSaveF attachmentF){
        AttachmentE map = Global.mapperFacade.map(attachmentF, AttachmentE.class);

        FileVo fileVo = attachmentF.getFileVo();
        map.setTenantId(tenantId())
                .setFileKey(fileVo.getFileKey())
                .setName(fileVo.getName())
                .setSuffix(fileVo.getSuffix())
                .setType(fileVo.getType())
                .setSize(fileVo.getSize());

        attachmentMapper.insert(map);
        return map.getId();
    }

    public String saveInfo(FileVo fileVo, String fileId) {
        AttachmentE map = new AttachmentE();
        map.setTenantId(tenantId())
                .setFileKey(fileVo.getFileKey())
                .setName(fileVo.getName())
                .setSuffix(fileVo.getSuffix())
                .setType(fileVo.getType())
                .setSize(fileVo.getSize())
                .setBusinessId(fileId)
                .setBusinessType(1001)
                .setFileuuid(fileId)
                .setId(fileId);
        attachmentMapper.insert(map);
        return map.getId();
    }

    public String saveInfoSmj(FileVo fileVo, String fileId,String contractId) {
        AttachmentE map = new AttachmentE();
        map.setTenantId(tenantId())
                .setFileKey(fileVo.getFileKey())
                .setName(fileVo.getName())
                .setSuffix(fileVo.getSuffix())
                .setType(fileVo.getType())
                .setSize(fileVo.getSize())
                .setBusinessId(contractId)
                .setBusinessType(1002)
                .setFileuuid(fileId);
        attachmentMapper.insert(map);
        return map.getId();
    }

    public String saveInfoSmjRepalce(FileVo fileVo, String fileId,String contractId) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AttachmentE.BUSINESS_ID, contractId)
                .eq(AttachmentE.DELETED,0);
        List<AttachmentE> list = list(queryWrapper);
        if(ObjectUtils.isNotEmpty(list)){
            for(AttachmentE s : list){
                attachmentMapper.deleteById(s.getId());
            }
        }
        AttachmentE map = new AttachmentE();
        map.setTenantId(tenantId())
                .setFileKey(fileVo.getFileKey())
                .setName(fileVo.getName())
                .setSuffix(fileVo.getSuffix())
                .setType(fileVo.getType())
                .setSize(fileVo.getSize())
                .setBusinessId(contractId)
                .setBusinessType(1002)
                .setFileuuid(fileId);
        attachmentMapper.insert(map);
        return map.getId();
    }

    public List<AttachmentE> listById(String bussinessId){
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AttachmentE.BUSINESS_ID, bussinessId)
                .eq(AttachmentE.DELETED,0);
        return list(queryWrapper);
    }

    public AttachmentE listOneById(String bussinessId){
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AttachmentE.BUSINESS_ID, bussinessId)
                .orderByDesc(AttachmentE.GMT_CREATE);
        List<AttachmentE> list = list(queryWrapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }


   /**
    * 根据Id更新
    *
    * @param attachmentF 根据Id更新
    */
    public void update(AttachmentUpdateF attachmentF){
        if (attachmentF.getId() == null) {
            throw new IllegalArgumentException();
        }
        AttachmentE map = Global.mapperFacade.map(attachmentF, AttachmentE.class);
        attachmentMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        attachmentMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<AttachmentV> page(PageF<AttachmentPageF> request) {
        AttachmentPageF conditions = request.getConditions();
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        

        if (Objects.nonNull(conditions.getBusinessType())) {
            queryWrapper.eq(AttachmentE.BUSINESS_TYPE, conditions.getBusinessType());
        }

        if (StringUtils.isNotBlank(conditions.getBusinessId())) {
            queryWrapper.eq(AttachmentE.BUSINESS_ID, conditions.getBusinessId());
        }

        if (StringUtils.isNotBlank(conditions.getFileKey())) {
            queryWrapper.eq(AttachmentE.FILE_KEY, conditions.getFileKey());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(AttachmentE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getSuffix())) {
            queryWrapper.eq(AttachmentE.SUFFIX, conditions.getSuffix());
        }

        if (Objects.nonNull(conditions.getSize())) {
            queryWrapper.eq(AttachmentE.SIZE, conditions.getSize());
        }

        if (StringUtils.isNotBlank(conditions.getFileSizeStr())) {
            queryWrapper.eq(AttachmentE.FILE_SIZE_STR, conditions.getFileSizeStr());
        }

        if (Objects.nonNull(conditions.getType())) {
            queryWrapper.eq(AttachmentE.TYPE, conditions.getType());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(AttachmentE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(AttachmentE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(AttachmentE.OPERATOR, conditions.getOperator());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(AttachmentE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(AttachmentE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(AttachmentE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(AttachmentE.OPERATOR_NAME, conditions.getOperatorName());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(AttachmentE.GMT_CREATE);
        }
        Page<AttachmentE> page = attachmentMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), AttachmentV.class));
    }
    /**
     * 该接口供给后端
     *
     * @param request 请求分页的参数
     * @return 查询出的分页列表
     */
    public List<AttachmentV> pageForContract(AttachmentPageF request) {

        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();


        if (Objects.nonNull(request.getBusinessType())) {
            queryWrapper.eq(AttachmentE.BUSINESS_TYPE, request.getBusinessType());
        }

        if (StringUtils.isNotBlank(request.getBusinessId())) {
            queryWrapper.eq(AttachmentE.BUSINESS_ID, request.getBusinessId());
        } else {
            throw new OwlBizException("业务主键ID不可为空");
        }

        // 默认排序
        queryWrapper.orderByDesc(AttachmentE.GMT_CREATE);
        queryWrapper.eq(AttachmentE.TENANT_ID, tenantId());

        return Global.mapperFacade.mapAsList(list(queryWrapper), AttachmentV.class);
    }

    public PageV<AttachmentV> frontPageForAllContract(PageF<SearchF<AttachmentE>> request) {
        for (Field field : request.getConditions().getFields()) {
            if (field.getName().equals("businessId")){
                List<String> allContractId = Lists.newArrayList();
                String parentContractId = field.getValue().toString();
                allContractId.add(parentContractId);

                LambdaQueryWrapper<ContractPayConcludeE> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ContractPayConcludeE::getPid,parentContractId);
                queryWrapper.eq(ContractPayConcludeE::getContractType, ContractTypeEnum.补充协议.getCode());
                queryWrapper.eq(ContractPayConcludeE::getDeleted,0);
                List<ContractPayConcludeE> childContractList = contractPayConcludeMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(childContractList)){
                    allContractId.addAll(childContractList.stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()));
                }

                LambdaQueryWrapper<ContractIncomeConcludeE> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(ContractIncomeConcludeE::getPid,parentContractId);
                queryWrapper2.eq(ContractIncomeConcludeE::getContractType, ContractTypeEnum.补充协议.getCode());
                queryWrapper2.eq(ContractIncomeConcludeE::getDeleted,0);
                List<ContractIncomeConcludeE> childContractList2 = contractIncomeConcludeMapper.selectList(queryWrapper2);
                if (CollectionUtils.isNotEmpty(childContractList2)){
                    allContractId.addAll(childContractList2.stream().map(ContractIncomeConcludeE::getId).collect(Collectors.toList()));
                }
                field.setValue(allContractId);
                field.setMethod(15);
            }
        }
        return frontPage(request);
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<AttachmentV> frontPage(PageF<SearchF<AttachmentE>> request) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        SearchF<AttachmentE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(AttachmentE.TENANT_ID, tenantId())
                .eq(AttachmentE.DELETED,false)
                .orderByDesc(AttachmentE.GMT_CREATE);

        List<OrderBy> orderBy = request.getOrderBy();
        Page<AttachmentE> page = attachmentMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        PageV<AttachmentV> attachmentVPageV = PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), AttachmentV.class));
        attachmentVPageV.getRecords().forEach(item -> {
            if(StringUtils.isNotEmpty(item.getBusinessId())){
                ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectById(item.getBusinessId());
                if(ObjectUtils.isNotEmpty(contractIncomeConcludeE)){
                    item.setContractType(contractIncomeConcludeE.getContractType());
                    item.setContractNature(contractIncomeConcludeE.getContractNature());
                }
                ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(item.getBusinessId());
                if(ObjectUtils.isNotEmpty(contractPayConcludeE)){
                    item.setContractType(contractPayConcludeE.getContractType());
                    item.setContractNature(contractPayConcludeE.getContractNature());
                }
            }
        });
        return attachmentVPageV;
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的AttachmentE中仅包含a字段的值
    *
    * @param fields AttachmentE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public AttachmentE selectOneBy(Consumer<QueryWrapper<AttachmentE>> consumer,String... fields) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return attachmentMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的AttachmentE中id字段的值, select 指定字段
    *
    * @param fields AttachmentE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<AttachmentE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(attachmentMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<AttachmentE>> consumer) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        attachmentMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的AttachmentE中仅包含a字段的值
     *
     * @param fields AttachmentE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<AttachmentE> selectListBy(Consumer<QueryWrapper<AttachmentE>> consumer,String... fields) {
         QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return attachmentMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的AttachmentE中仅包含a字段的值
    *
    * @param fields AttachmentE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<AttachmentE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<AttachmentE>> consumer, String... fields) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return attachmentMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的AttachmentE中id字段的值, select 指定字段
     *
     * @param fields AttachmentE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<AttachmentE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<AttachmentE> page = Page.of(pageNum, pageSize, count);
        Page<AttachmentE> queryPage = attachmentMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    public List<AttachmentE> getAttachmentByBusinessId(String businessId, Integer type) {
        LambdaQueryWrapper<AttachmentE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttachmentE::getBusinessId, businessId)
                .eq(AttachmentE::getDeleted, 0)
                .eq(AttachmentE::getType, type);
        return attachmentMapper.selectList(queryWrapper);
    }

    //根据现有数据删除其余无效数据
    public void deleteInvalidFileById(Integer businessType, String businessId, List<String> idList){
        //根据现有数据删除其余无效数据
        attachmentMapper.deleteInvalidFileById(businessType, businessId, idList);
    }
}
