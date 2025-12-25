package com.wishare.finance.domains.voucher.strategy.core;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherPayWayEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionMethodEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 凭证条件构建工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/10
 */
public class VoucherRuleConditionUtils {

    public static QueryWrapper<?> parseConditionToQuery(List<VoucherRuleConditionOBV> conditions){
        return parseConditionToQuery(conditions, null);
    }


    /**
     * 根据搜索条件构建查询条件
     * @param conditions 条件列表
     * @return 构造条件信息
     */
    public static QueryWrapper<?> parseConditionToQuery(List<VoucherRuleConditionOBV> conditions, BillTypeEnum billTypeEnum){
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        if (conditions == null || conditions.isEmpty()) {
            return queryWrapper;
        }

        for (VoucherRuleConditionOBV condition : conditions) {
            VoucherRuleConditionTypeEnum ruleConditionTypeEnum = VoucherRuleConditionTypeEnum.valueOfByCode(condition.getType());
            String fieldString = getFieldString(billTypeEnum, ruleConditionTypeEnum);;
            switch (ruleConditionTypeEnum) {
                case 费项:
                    putField(condition.getMethod(), queryWrapper, fieldString == null ? "b.charge_item_id" : fieldString,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 结算方式:
                    List<VoucherRuleConditionOptionOBV> values = condition.getValues();
//                    int lastIndex = values.size() - 1;
                    String finalFieldString = fieldString;
                    queryWrapper.and(wrapper -> {
                        for (int i = 0; i < values.size(); i++) {
                            VoucherRuleConditionOptionOBV value = values.get(i);
                            VoucherPayWayEnum voucherPayWay = VoucherPayWayEnum.valueOfByCode(value.getCode());
                            wrapper.or(w -> {
                                putField(condition.getMethod(), w, finalFieldString == null  ? "b.pay_way" : "gd.pay_way", List.of(voucherPayWay.getType()), false);
                                putField(condition.getMethod(), w, finalFieldString == null  ? "b.pay_channel": finalFieldString, List.of(voucherPayWay.getPayCode()), false);
                            });
//                            if (i < lastIndex){
//                                //添加或操作，达到 包含的效果
//                                wrapper.or();
//                            }
                        }
                    });
                    break;
                case 票据类型:
                    putField(condition.getMethod(), queryWrapper, "b.type",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 单据来源:
                    putField(condition.getMethod(), queryWrapper, "b.sys_source",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 税率:
                    putField(condition.getMethod(), queryWrapper, "b.tax_rate_id",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 客商:
                    putField(condition.getMethod(), queryWrapper, "b.payer_type", List.of(3), false);
                    break;
                case 银行流水:
                    break;
                case 收款银行账户:
                case 付款银行账户:
                    putField(condition.getMethod(), queryWrapper, "b.sb_account_id",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 支付渠道:
                    putField(condition.getMethod(), queryWrapper, fieldString == null ? "b.pay_channel" : fieldString,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 计费周期:
                    putField(condition.getMethod(), queryWrapper, "b.tax_rate_id",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), true);
                    break;
                case 结算时间:
                    putField(condition.getMethod(), queryWrapper, "b.pay_time",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), true);
                    break;
                case 归属月:
                    List<Object> dates = new ArrayList<>();
                    condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()).forEach(s ->{
                        dates.add(s +"-01");
                    });
                    fieldString = getFieldDateString(billTypeEnum);
                    putFieldByMonth(condition.getMethod(), queryWrapper, fieldString == null ? "b.account_date" : fieldString, dates);
                    break;
                case 业务场景:
                    putField(condition.getMethod(), queryWrapper, "m_business_type",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 调整方式:
                    putField(condition.getMethod(), queryWrapper, "ba.adjust_way",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 调整类型:
                    putField(condition.getMethod(), queryWrapper,"ba.adjust_type" ,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 应收类型:
                    putField(condition.getMethod(), queryWrapper, "b.bill_type",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 费项属性:
                    putField(condition.getMethod(), queryWrapper, "ci.attribute",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 减免形式:
                    putField(condition.getMethod(), queryWrapper, "ba.deduction_method",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 业务类型:
                    putField(condition.getMethod(), queryWrapper, "b.business_name",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getName).collect(Collectors.toList()), false);
                    break;
                case 业务单元:
                    String fieldUnitString = getFieldUnitString(billTypeEnum);
                    putField(condition.getMethod(), queryWrapper, StringUtils.isNotBlank(fieldUnitString) ? fieldUnitString : "b.business_unit_id",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 成本中心:
                    putField(condition.getMethod(), queryWrapper, fieldString == null ? "b.cost_center_id" : fieldString,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 法定单位:
                    putField(condition.getMethod(), queryWrapper, fieldString == null ? "b.statutory_body_id" : fieldString,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 结算状态:
                    putField(condition.getMethod(), queryWrapper, fieldString == null ? "b.settle_state" : fieldString,
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                    break;
                case 结转时间:
                    putField(condition.getMethod(), queryWrapper, "br.carryover_time",
                            condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), true);
                    break;
            }
        }
        return queryWrapper;
    }

    public static String getFieldUnitString(BillTypeEnum billTypeEnum) {
        if (billTypeEnum == null) {
            return null;
        }
        switch (billTypeEnum) {
            case 收款单:
                return "rb.business_unit_id";
        }
        return null;
    }


    public static String getFieldString(BillTypeEnum billTypeEnum, VoucherRuleConditionTypeEnum conditionTypeEnum) {
        if (billTypeEnum == null) {
            return null;
        }
        switch (billTypeEnum) {
            case 收款单:
                switch (conditionTypeEnum) {
                    case 费项:
                        return "gd.charge_item_id";
                    case 成本中心:
                        return "gd.cost_center_id";
                    case 支付渠道:
                    case 结算方式:
                        return "gd.pay_channel";
                }
                break;
            case 付款单:
                switch (conditionTypeEnum) {
                    case 费项:
                        return "pd.charge_item_id";
                    case 成本中心:
                        return "pd.cost_center_id";
                }
                break;
        }
        return null;
    }

    public static String getPayWayString(BillTypeEnum billTypeEnum) {
        if (BillTypeEnum.收款单.equalsByCode(billTypeEnum.getCode())) {
            return "gd.pay_way";
        }
        return "b.pay_way";
    }

    public static String getPayChannelString(BillTypeEnum billTypeEnum) {
        if (BillTypeEnum.收款单.equalsByCode(billTypeEnum.getCode())) {
            return "gd.pay_channel";
        }
        return "b.pay_channel";
    }



    public static String getFieldDateString(BillTypeEnum billTypeEnum) {
        if (billTypeEnum == null) {
            return null;
        }
        switch (billTypeEnum) {
            case 收款单:
                return "rb.account_date";
        }
        return null;
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
    private static QueryWrapper<?> putField(Integer method, QueryWrapper<?> wrapper, String field, List<Object> values, boolean isDate){
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


    public static QueryWrapper<?> putFieldByMonth(Integer method, QueryWrapper<?> wrapper, String field, List<Object> values) {
        switch (VoucherRuleConditionMethodEnum.valueOfByCode(method)) {
            case 包含:
                wrapper.ge(field, values.get(0)).le(field, values.get(1));
                break;
            case 不包含:
                wrapper.le(field, values.get(0)).ge(field, values.get(1));
                break;
            case 等于:
                String endMonth = DateUtil.endOfMonth(DateUtil.parseDate((String) values.get(0))).toDateStr();
                wrapper.ge(field, values.get(0)).le(field, endMonth);
                break;
            case 小于等于:
                wrapper.le(field, DateUtil.endOfMonth(DateUtil.parseDate((String) values.get(0))).toDateStr());
                break;
            case 大于等于:
                wrapper.ge(field, values.get(0));
                break;
        }
        return wrapper;
    }


}
