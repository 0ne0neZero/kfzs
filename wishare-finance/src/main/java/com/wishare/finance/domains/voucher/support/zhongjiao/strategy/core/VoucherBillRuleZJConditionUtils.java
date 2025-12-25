package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherPayWayEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionMethodEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.VoucherBillRuleConditionZJTypeEnum;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 凭证条件构建工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/10
 */
public class VoucherBillRuleZJConditionUtils {

    public static QueryWrapper<?> parseConditionToQuery(List<VoucherBillRuleConditionOBV> conditions){
        return parseConditionToQuery(conditions, null);
    }

    /**
     * 根据搜索条件构建查询条件
     * @param conditions 条件列表
     * @return 构造条件信息
     */
    public static QueryWrapper<?> parseConditionToQuery(List<VoucherBillRuleConditionOBV> conditions, BillTypeEnum billTypeEnum){
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        if (conditions == null || conditions.isEmpty()) {
            return queryWrapper;
        }
        for (VoucherBillRuleConditionOBV condition : conditions) {
            VoucherBillRuleConditionZJTypeEnum ruleConditionTypeEnum = VoucherBillRuleConditionZJTypeEnum.valueOfByCode(condition.getType());
            switch (ruleConditionTypeEnum) {
                case 费项:
                    putField(condition.getMethod(), queryWrapper,  "b.charge_item_id" ,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 单据来源:
                    putField(condition.getMethod(), queryWrapper, "b.sys_source",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 账单类型:
                    putField(condition.getMethod(), queryWrapper, "b.bill_type",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 违约金标识:
                    putField(condition.getMethod(), queryWrapper, "b.overdue",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 结算时间:
                    putField(condition.getMethod(), queryWrapper, "b.pay_time",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), true);
                    break;
                case 归属月:
                    List<Object> dates = new ArrayList<>();
                    condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()).forEach(s ->{
                        dates.add(s);
                    });
                    putField(condition.getMethod(), queryWrapper,  "DATE_FORMAT(b.account_date, '%Y-%m')",
                            dates, true);
                    break;
                case 结算方式:
                    List<VoucherRuleConditionOptionOBV> values = condition.getValues();
                    queryWrapper.and(wrapper -> {
                        for (int i = 0; i < values.size(); i++) {
                            VoucherRuleConditionOptionOBV value = values.get(i);
                            VoucherPayWayEnum voucherPayWay = VoucherPayWayEnum.valueOfByCode(value.getCode());
                            wrapper.or(w -> {
                                putField(condition.getMethod(), w,"b.pay_way", List.of(voucherPayWay.getType()), false);
                                putField(condition.getMethod(), w, "b.pay_channel", List.of(voucherPayWay.getPayCode()), false);
                            });
                        }
                    });
                    break;
                case 收费对象类型:
                    putField(condition.getMethod(), queryWrapper,  "b.customer_type" ,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
            }
        }
        return queryWrapper;
    }


    /**
     * 添加条件
     * @param method 匹配方式
     * @param wrapper 条件构造器
     * @param field 参数
     * @param values 值
     * @param isDate 是否为日期
     * @return
     */
    public static QueryWrapper<?> putField(Integer method, QueryWrapper<?> wrapper, String field, List<Object> values, boolean isDate){
        switch (VoucherRuleConditionMethodEnum.valueOfByCode(method)){
            case 包含:
                if (isDate){
                    wrapper.ge(field, values.get(0)).le(field, values.get(1));
                }else if (values.size() == 1){
                    wrapper.eq(field, values.get(0));
                }else {
                    wrapper.in(field, values);
                }
                break;
            case 不包含:
                if (isDate){
                    wrapper.le(field, values.get(0)).ge(field, values.get(1));
                }else if (values.size() == 1){
                    wrapper.ne(field, values.get(0));
                }else {
                    wrapper.notIn(field, values);
                }
                break;
            case 等于:
                wrapper.eq(field, values.get(0));
                break;
            case 小于等于:
                wrapper.le(field, values.get(0));
                break;
            case 大于等于:
                wrapper.ge(field, values.get(0));
                break;
        }
        return wrapper;
    }


}
