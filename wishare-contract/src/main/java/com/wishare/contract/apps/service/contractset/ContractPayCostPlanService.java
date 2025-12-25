package com.wishare.contract.apps.service.contractset;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.PayCostPlanE;
import com.wishare.contract.domains.mapper.revision.income.PayCostPlanMapper;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanPageV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class ContractPayCostPlanService extends ServiceImpl<PayCostPlanMapper, PayCostPlanE> implements IOwlApiBase {

    public PageV<PayCostPlanPageV> pageFront(PageF<SearchF<?>> request) {
        Page<?> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        String belongMonth = null;
        // 这里替换为stream 获取并删除，避免报错
        Field belongMonthField = request.getConditions().getFields().stream().filter(field -> "p.costStartTime".equals(field.getName())).findFirst().orElse(null);
        request.getConditions().getFields().removeIf(field -> "p.costStartTime".equals(field.getName()));
        if (Objects.nonNull(belongMonthField)){
            belongMonth = belongMonthField.getValue().toString();
        }
        QueryWrapper<?> queryModel = request.getConditions().getQueryModel();
        if (StringUtils.isNotBlank(belongMonth)){
            queryModel.apply("date_format(p.costStartTime,'%Y-%m') = {0}",  belongMonth);
        }
        queryModel.orderByDesc("p.gmtModify");
        queryModel.orderByAsc("p.costPlanCode");
        IPage<PayCostPlanPageV> pageList = this.getBaseMapper().pageInfo(pageF, queryModel);
        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    /**
     * 获取数据库中编号以指定前缀开头的所有编号
     * 并获取最大的结尾数字,不足两位补全两位
     **/
    public Integer getMaxNumByPrefix(String prefix){
        if (StringUtils.isBlank(prefix)){
            throw new IllegalArgumentException("前缀不能为空");
        }
        List<String> certainNoList = this.getBaseMapper().getNoListByPrefix(prefix);
        if (CollectionUtils.isEmpty(certainNoList)){
            return 1;
        }
        int max = 0;
        for (String certainNo : certainNoList) {
            String numStr = certainNo.substring(prefix.length() + 1);
            try {
                int num = Integer.parseInt(numStr);
                if (num > max){
                    max = num;
                }
            }catch (NumberFormatException e){
                log.error("数字格式化异常");
            }
        }
        return max+1;
    }

    /**
     * 根据入参的数字,不足两位补全两位,否则直接返回
     **/
    public String getNumStr(Integer num){
        if (num < 10){
            return "0" + num;
        }
        return String.valueOf(num);
    }

    public List<PayCostPlanV> queryPlanVListByPlanIdList(String contractId, String funId, Date startDate, Date endDate) {
        return this.getBaseMapper().queryPlanVListByPlanIdList(contractId, funId,startDate, endDate);
    }

    /*根据结算计划ID及结算周期查询范围内的成本计划*/
    public List<PayCostPlanE> getCostListByPlanAndCostTime(String contractId, String funId, Date startDate, Date endDate) {
        return this.baseMapper.getCostListByPlanAndCostTime(contractId, funId,startDate, endDate);
    }
}
