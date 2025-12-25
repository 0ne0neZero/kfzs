package com.wishare.contract.domains.service.revision.income;

import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeExpandMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandListV;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandPageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandSaveF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandUpdateF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandListF;
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
 * 收入合同订立信息拓展表
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeConcludeExpandService extends ServiceImpl<ContractIncomeConcludeExpandMapper, ContractIncomeConcludeExpandE>  {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeExpandMapper contractIncomeConcludeExpandMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeConcludeExpandV> get(ContractIncomeConcludeExpandF conditions){
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        
        if (Objects.nonNull(conditions.getId())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractIncomeConcludeExpandE contractIncomeConcludeExpandE = contractIncomeConcludeExpandMapper.selectOne(queryWrapper);
        if (contractIncomeConcludeExpandE != null) {
            return Optional.of(Global.mapperFacade.map(contractIncomeConcludeExpandE, ContractIncomeConcludeExpandV.class));
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
    public ContractIncomeConcludeExpandListV list(ContractIncomeConcludeExpandListF conditions){
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractIncomeConcludeExpandE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractIncomeConcludeExpandE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractIncomeConcludeExpandV> retVList = Global.mapperFacade.mapAsList(contractIncomeConcludeExpandMapper.selectList(queryWrapper),ContractIncomeConcludeExpandV.class);
        ContractIncomeConcludeExpandListV retV = new ContractIncomeConcludeExpandListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public Long save(ContractIncomeConcludeExpandSaveF contractIncomeConcludeExpandF){
        ContractIncomeConcludeExpandE map = Global.mapperFacade.map(contractIncomeConcludeExpandF, ContractIncomeConcludeExpandE.class);
        contractIncomeConcludeExpandMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractIncomeConcludeExpandF 根据Id更新
    */
    public void update(ContractIncomeConcludeExpandUpdateF contractIncomeConcludeExpandF){
        if (contractIncomeConcludeExpandF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeConcludeExpandE map = Global.mapperFacade.map(contractIncomeConcludeExpandF, ContractIncomeConcludeExpandE.class);
        contractIncomeConcludeExpandMapper.updateById(map);
    }

    /**
     * 根据ContractId更新
     *
     * @param contractIncomeConcludeExpandF 根据Id更新
     */
    public void updateByContractId(ContractIncomeConcludeExpandUpdateF contractIncomeConcludeExpandF){
        if (contractIncomeConcludeExpandF.getContractId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeConcludeExpandE map = Global.mapperFacade.map(contractIncomeConcludeExpandF, ContractIncomeConcludeExpandE.class);
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeExpandE.CONTRACTID, contractIncomeConcludeExpandF.getContractId());
        contractIncomeConcludeExpandMapper.update(map,queryWrapper);
    }


    /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(Long id){
        contractIncomeConcludeExpandMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeConcludeExpandV> page(PageF<ContractIncomeConcludeExpandPageF> request) {
        ContractIncomeConcludeExpandPageF conditions = request.getConditions();
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONTRACTID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getConlanguage())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONLANGUAGE, conditions.getConlanguage());
        }

        if (StringUtils.isNotBlank(conditions.getConname())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONNAME, conditions.getConname());
        }

        if (StringUtils.isNotBlank(conditions.getIssupplycontract())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.ISSUPPLYCONTRACT, conditions.getIssupplycontract());
        }

        if (StringUtils.isNotBlank(conditions.getOppositeconmaincode())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPPOSITECONMAINCODE, conditions.getOppositeconmaincode());
        }

        if (StringUtils.isNotBlank(conditions.getIncomeexpendtype())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.INCOMEEXPENDTYPE, conditions.getIncomeexpendtype());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcountry())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCOUNTRY, conditions.getConperformcountry());
        }

        if (StringUtils.isNotBlank(conditions.getConperformprovinces())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMPROVINCES, conditions.getConperformprovinces());
        }

        if (StringUtils.isNotBlank(conditions.getConperformcity())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMCITY, conditions.getConperformcity());
        }

        if (StringUtils.isNotBlank(conditions.getConperformxian())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONPERFORMXIAN, conditions.getConperformxian());
        }

        if (StringUtils.isNotBlank(conditions.getDisputesolutionway())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.DISPUTESOLUTIONWAY, conditions.getDisputesolutionway());
        }

        if (StringUtils.isNotBlank(conditions.getConmodelusecondition())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONMODELUSECONDITION, conditions.getConmodelusecondition());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakerid())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERID, conditions.getConundertakerid());
        }

        if (StringUtils.isNotBlank(conditions.getConundertaker())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKER, conditions.getConundertaker());
        }

        if (StringUtils.isNotBlank(conditions.getConundertakercontact())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CONUNDERTAKERCONTACT, conditions.getConundertakercontact());
        }

        if (Objects.nonNull(conditions.getTaxrate())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXRATE, conditions.getTaxrate());
        }

        if (Objects.nonNull(conditions.getTaxamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TAXAMT, conditions.getTaxamt());
        }

        if (Objects.nonNull(conditions.getHsbgamt())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.HSBGAMT, conditions.getHsbgamt());
        }

        if (StringUtils.isNotBlank(conditions.getTzxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.TZXX, conditions.getTzxx());
        }

        if (StringUtils.isNotBlank(conditions.getFkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FKDWXX, conditions.getFkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getSkdwxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SKDWXX, conditions.getSkdwxx());
        }

        if (StringUtils.isNotBlank(conditions.getBzjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.BZJXX, conditions.getBzjxx());
        }

        if (StringUtils.isNotBlank(conditions.getSpxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.SPXX, conditions.getSpxx());
        }

        if (StringUtils.isNotBlank(conditions.getFjxx())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.FJXX, conditions.getFjxx());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeExpandE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeExpandE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractIncomeConcludeExpandE.GMT_CREATE);
        }
        Page<ContractIncomeConcludeExpandE> page = contractIncomeConcludeExpandMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeExpandV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeConcludeExpandV> frontPage(PageF<SearchF<ContractIncomeConcludeExpandE>> request) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractIncomeConcludeExpandE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractIncomeConcludeExpandE> page = contractIncomeConcludeExpandMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeExpandV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractIncomeConcludeExpandE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeExpandE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeConcludeExpandE selectOneBy(Consumer<QueryWrapper<ContractIncomeConcludeExpandE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeExpandMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeExpandE中id字段的值, select 指定字段
    *
    * @param fields ContractIncomeConcludeExpandE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeExpandE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeConcludeExpandMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeConcludeExpandE>> consumer) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeConcludeExpandMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractIncomeConcludeExpandE中仅包含a字段的值
     *
     * @param fields ContractIncomeConcludeExpandE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeConcludeExpandE> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeExpandE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeConcludeExpandMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractIncomeConcludeExpandE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeExpandE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeConcludeExpandE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeConcludeExpandE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeExpandMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeExpandE中id字段的值, select 指定字段
     *
     * @param fields ContractIncomeConcludeExpandE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeConcludeExpandE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeConcludeExpandE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeConcludeExpandE> queryPage = contractIncomeConcludeExpandMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
