package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.GatherAndPayDto;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.repository.mapper.GatherAndPayMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherAndPayRepository {

    @Autowired
    private GatherAndPayMapper gatherAndPayMapper;


    /**
     * 分页查询收付款记录
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        return gatherAndPayMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 分页查询收付款记录(收付款单维度)
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> billQueryPage(PageF<SearchF<?>> form, String gatherBillName,String gatherDetailName) {
        SearchF<?> conditions = form.getConditions();
        List<Field> fields = conditions.getFields();
        Optional<Field> supCpUnitIdField = fields.stream().filter(e -> "gb.sup_cp_unit_id".equals(e.getName())).findAny();
        supCpUnitIdField.ifPresent(a->a.setName("gd.sup_cp_unit_id"));
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        return gatherAndPayMapper.billQueryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel, gatherBillName,gatherDetailName);
    }

    /**
     * 分页查询收付款记录(收付款单维度无租户隔离)
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> billQueryPageIgnoreTenant(PageF<SearchF<?>> form, String gatherBillName, String gatherDetailName) {
        SearchF<?> conditions = form.getConditions();
        List<Field> collect = conditions.getFields().stream().filter(a -> "d.payee_id".equals(a.getName())).collect(Collectors.toList());
        //HXYUN-12755
        if (CollectionUtils.isEmpty(collect)) {
            QueryWrapper<?> queryModel = conditions.getQueryModel();
            conditions.getFields().removeIf(a -> "gb.sup_cp_unit_id".equals(a.getName()));
            QueryWrapper<?> queryPayModel = conditions.getQueryModel();
            return gatherAndPayMapper.billQueryPageIgnoreTenant(Page.of(form.getPageNum(), form.getPageSize()
                    , form.isCount()), queryModel, queryPayModel, gatherBillName, gatherDetailName);
        }
        Field field = collect.get(0);
        conditions.getFields().removeIf(a -> "d.payee_id".equals(a.getName()));
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        conditions.getFields().removeIf(a -> "d.payer_id".equals(a.getName()));
        conditions.getFields().removeIf(a -> "gb.sup_cp_unit_id".equals(a.getName()));
        conditions.getFields().add(field);
        QueryWrapper<?> queryPayModel = conditions.getQueryModel();
        return gatherAndPayMapper.billQueryPageIgnoreTenant(Page.of(form.getPageNum(), form.getPageSize()
                , form.isCount()), queryModel, queryPayModel, gatherBillName, gatherDetailName);
    }

    /**
     * 统计收付款记录
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statistics(SearchF<?> form) {
        QueryWrapper<?> queryModel = form.getQueryModel();
        Long gatherAmountSum = gatherAndPayMapper.statisticsGather(queryModel);
        Long payAmountSum = gatherAndPayMapper.statisticsPay(queryModel);
        return new GatherAndPayStatisticsDto(gatherAmountSum,payAmountSum);
    }

    /**
     * 统计收付款记录(无租户隔离)
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statisticsIgnoreTenant(SearchF<?> form,String gatherDetailName) {
        List<Field> collect = form.getFields().stream().filter(a -> "d.payee_id".equals(a.getName())).collect(Collectors.toList());
        form.getFields().removeIf(a-> "gb.sup_cp_unit_id".equals(a.getName()));
        //HXYUN-12755
        if(CollectionUtils.isEmpty(collect)){
            Long gatherAmountSum = gatherAndPayMapper.statisticsGatherIgnoreTenant(form.getQueryModel(),gatherDetailName);
            Long payAmountSum = gatherAndPayMapper.statisticsPayIgnoreTenant(form.getQueryModel());
            return new GatherAndPayStatisticsDto(gatherAmountSum,payAmountSum);
        }
        Field field = collect.get(0);
        form.getFields().removeIf(a-> "d.payee_id".equals(a.getName()));
        QueryWrapper<?> queryModel = form.getQueryModel();
        form.getFields().removeIf(a-> "d.payer_id".equals(a.getName()));
        form.getFields().add(field);
        QueryWrapper<?> queryPayModel = form.getQueryModel();
        Long gatherAmountSum = gatherAndPayMapper.statisticsGatherIgnoreTenant(queryModel,gatherDetailName);
        Long payAmountSum = gatherAndPayMapper.statisticsPayIgnoreTenant(queryPayModel);
        return new GatherAndPayStatisticsDto(gatherAmountSum,payAmountSum);
    }
}
