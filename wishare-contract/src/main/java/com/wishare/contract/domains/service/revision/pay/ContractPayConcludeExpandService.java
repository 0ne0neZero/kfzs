package com.wishare.contract.domains.service.revision.pay;

import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeExpandE;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeExpandMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeExpandListV;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandPageF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandSaveF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandUpdateF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeExpandListF;
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
 * 支出合同订立信息拓展表
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPayConcludeExpandService extends ServiceImpl<ContractPayConcludeExpandMapper, ContractPayConcludeExpandE>  {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeExpandMapper contractPayConcludeExpandMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractPayConcludeExpandV> get(ContractPayConcludeExpandF conditions){
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        
        if (Objects.nonNull(conditions.getId())) {
            queryWrapper.eq(ContractPayConcludeExpandE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractPayConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractPayConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractPayConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractPayConcludeExpandE contractPayConcludeExpandE = contractPayConcludeExpandMapper.selectOne(queryWrapper);
        if (contractPayConcludeExpandE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayConcludeExpandE, ContractPayConcludeExpandV.class));
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
    public ContractPayConcludeExpandListV list(ContractPayConcludeExpandListF conditions){
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractPayConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractPayConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractPayConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractPayConcludeExpandE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractPayConcludeExpandE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractPayConcludeExpandV> retVList = Global.mapperFacade.mapAsList(contractPayConcludeExpandMapper.selectList(queryWrapper),ContractPayConcludeExpandV.class);
        ContractPayConcludeExpandListV retV = new ContractPayConcludeExpandListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public Long save(ContractPayConcludeExpandSaveF contractPayConcludeExpandF){
        ContractPayConcludeExpandE map = Global.mapperFacade.map(contractPayConcludeExpandF, ContractPayConcludeExpandE.class);
        contractPayConcludeExpandMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractPayConcludeExpandF 根据Id更新
    */
    public void update(ContractPayConcludeExpandUpdateF contractPayConcludeExpandF){
        if (contractPayConcludeExpandF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayConcludeExpandE map = Global.mapperFacade.map(contractPayConcludeExpandF, ContractPayConcludeExpandE.class);
        contractPayConcludeExpandMapper.updateById(map);
    }


    /**
     * 根据ContractId更新
     *
     * @param contractPayConcludeExpandF 根据Id更新
     */
    public void updateByContractId(ContractPayConcludeExpandUpdateF contractPayConcludeExpandF){
        if (contractPayConcludeExpandF.getContractId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayConcludeExpandE map = Global.mapperFacade.map(contractPayConcludeExpandF, ContractPayConcludeExpandE.class);
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeExpandE.CONTRACTID, contractPayConcludeExpandF.getContractId());
        contractPayConcludeExpandMapper.update(map, queryWrapper);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(Long id){
        contractPayConcludeExpandMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeExpandV> page(PageF<ContractPayConcludeExpandPageF> request) {
        ContractPayConcludeExpandPageF conditions = request.getConditions();
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractPayConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractPayConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractPayConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractPayConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractPayConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayConcludeExpandE.GMT_CREATE);
        }
        Page<ContractPayConcludeExpandE> page = contractPayConcludeExpandMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeExpandV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeExpandV> frontPage(PageF<SearchF<ContractPayConcludeExpandE>> request) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayConcludeExpandE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractPayConcludeExpandE> page = contractPayConcludeExpandMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeExpandV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayConcludeExpandE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeExpandE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayConcludeExpandE selectOneBy(Consumer<QueryWrapper<ContractPayConcludeExpandE>> consumer,String... fields) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeExpandMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeExpandE中id字段的值, select 指定字段
    *
    * @param fields ContractPayConcludeExpandE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayConcludeExpandE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayConcludeExpandMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayConcludeExpandE>> consumer) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayConcludeExpandMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayConcludeExpandE中仅包含a字段的值
     *
     * @param fields ContractPayConcludeExpandE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayConcludeExpandE> selectListBy(Consumer<QueryWrapper<ContractPayConcludeExpandE>> consumer,String... fields) {
         QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayConcludeExpandMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayConcludeExpandE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeExpandE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayConcludeExpandE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayConcludeExpandE>> consumer, String... fields) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeExpandMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeExpandE中id字段的值, select 指定字段
     *
     * @param fields ContractPayConcludeExpandE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayConcludeExpandE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayConcludeExpandE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayConcludeExpandE> queryPage = contractPayConcludeExpandMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
