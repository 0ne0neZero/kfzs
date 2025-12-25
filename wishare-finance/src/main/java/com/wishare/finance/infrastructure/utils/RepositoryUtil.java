package com.wishare.finance.infrastructure.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源库工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
public class RepositoryUtil {


    /**
     * 添加逻辑删除
     * @param queryWrapper  查询构造器
     * @param alias 别名
     * @return  查询构造器
     * @param <T> 数据类型
     */
    public static <T>  QueryWrapper<T> putLogicDeleted(QueryWrapper<T> queryWrapper, String ...alias){
        int code = DataDeletedEnum.NORMAL.getCode();
        if (alias == null || alias.length == 0){
            return queryWrapper.eq("deleted", code);
        }
        for (String alia : alias) {
            queryWrapper.eq(alia + ".deleted", code);
        }
        return queryWrapper;
    }

    /**
     * 分页接口数据转换
     * @param pageData mybatis-plus 分页数据
     * @param tClass 目标类
     * @return  目标类分页接口数据
     * @param <E> 初始数据类型
     * @param <T> 目标数据类型
     */
    public static <E, T> PageV<T> convertPage(Page<E> pageData, Class<T> tClass){
        return PageV.of(pageData.getCurrent(), pageData.getSize(), pageData.getTotal(), Global.mapperFacade.mapAsList(pageData.getRecords(), tClass));
    }

    /**
     * 分页接口数据转换(分转元)
     * @param pageData mybatis-plus 分页数据
     * @param tClass 目标类
     * @return  目标类分页接口数据
     * @param <E> 初始数据类型
     * @param <T> 目标数据类型
     */
    public static <E, T> PageV<T> convertMoneyPage(Page<E> pageData, Class<T> tClass){
        return PageV.of(pageData.getCurrent(), pageData.getSize(), pageData.getTotal(),
                MapperFacadeUtil.getMoneyMapperFacade().mapAsList(pageData.getRecords(), tClass));
    }

    /**
     * 分页接口数据转换
     * @param pageData 分页插件
     * @param tClass 目标类
     * @return 目标类分页接口数据
     * @param <E> 初始数据类型
     * @param <T> 目标数据类型
     */
    public static <E, T> PageV<T> convertPage(PageV<E> pageData, Class<T> tClass){
        return PageV.of(pageData.getPageNum(), pageData.getPageSize(), pageData.getTotal(), Global.mapperFacade.mapAsList(pageData.getRecords(), tClass));
    }

    /**
     * 转换为mybatis-plus 分页对象
     * @param pageData 分页入参
     * @return mybatis-plus 分页对象
     * @param <T> 目标数据类型
     */
    public static <T> Page<T> convertMPPage(PageF<T> pageData){
        return Page.of(pageData.getPageNum(), pageData.getPageSize());
    }

    /**
     * 金额查询问题转换
     * @param conditions
     * @param fieldName 字段名称
     */
    public static void convertSearch(SearchF<?> conditions, String fieldName){
        for (Field field : conditions.getFields()) {
            if (fieldName.equals(field.getName())){
                try {
                    field.setValue(AmountUtils.toLongByScaleLength(field.getValue().toString()));
                }catch (Exception e){
                    throw BizException.throw403(ErrMsgEnum.AMOUNT_FAIL.getErrMsg());
                }
                break;
            }
        }
    }

    public static void convertSearchType(SearchF<?> conditions){
        List<Field> fields = conditions.getFields();

        List<Field> payTimeType = fields.stream().filter(s -> "fd.pay_time".equals(s.getName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(payTimeType)&&payTimeType.get(0).getValue()!=null){
            if ("1".equals(String.valueOf(payTimeType.get(0).getValue()))){
                fields.removeAll(payTimeType);
                conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(1),5));
                conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(0),4));
            }
            if ("2".equals(String.valueOf(payTimeType.get(0).getValue()))){
                fields.removeAll(payTimeType);
                conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(1),5));
                conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(-6),4));
            }
        }
        convertSettleAmount(fields, conditions);
    }

    private static void convertSettleAmount(List<Field> fields, SearchF<?> conditions) {
        List<Field> settleAmountMin = fields.stream().filter(s -> "settleAmountMin".equals(s.getName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(settleAmountMin) && settleAmountMin.get(0).getValue()!=null){
            fields.removeAll(settleAmountMin);
            long l = new BigDecimal(String.valueOf(settleAmountMin.get(0).getValue())).multiply(new BigDecimal("100")).longValue();
            conditions.getFields().add(new Field("fd.settle_amount",l,4));
        }
        List<Field> settleAmountMax = fields.stream().filter(s -> "settleAmountMax".equals(s.getName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(settleAmountMax) && settleAmountMax.get(0).getValue()!=null){
            fields.removeAll(settleAmountMax);
            long l = new BigDecimal(String.valueOf(settleAmountMax.get(0).getValue())).multiply(new BigDecimal("100")).longValue();
            conditions.getFields().add(new Field("fd.settle_amount",l,6));
        }
    }

    /**
     * 汇款认领查询时，准换条件
     * @param conditions
     */
    public static void remitConvertSearchType(SearchF<?> conditions){
        List<Field> fields = conditions.getFields();

        List<Field> payTimeType = fields.stream().filter(s -> "fd.pay_time".equals(s.getName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(payTimeType)) {
            // 默认展示最近七天
            conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(1),5));
            conditions.getFields().add(new Field("fd.pay_time",getTimeUtils(-6),4));
        }
        convertSettleAmount(fields, conditions);
    }

    /**
     * 发票认领页面列表 条件转换
     * @param conditions
     */
    public static void invoiceClaimPageConvertSearchType(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            if ("sys_source".equals(field.getName())) {
                field.setName("ir.sys_source");
            }
            if ("pay_channel".equals(field.getName())) {
                iterator.remove();
            }
            if ("bill_no".equals(field.getName())) {
                field.setName("ir.invoice_receipt_no");
            }
            if ("start_date".equals(field.getName())) {
                field.setName("ir.billing_time");
            }
            if ("end_date".equals(field.getName())) {
                Date endOfDay = getEndOfDay(field.getValue().toString());
                if (Objects.isNull(endOfDay)) {
                    iterator.remove();
                    continue;
                }
                field.setName("ir.billing_time");
                field.setValue(endOfDay);
                field.setMethod(6);
            }
            if ("customer_name".equals(field.getName())) {
                field.setName("ir.customer_name");
            }
            if ("cost_center_name".equals(field.getName())) {
                field.setName("ir.cost_center_name");
            }
            if ("room_name".equals(field.getName())) {
                field.setName("ird.room_name");
            }
            if ("settleAmountMin".equals(field.getName())) {
                long l = new BigDecimal(String.valueOf(field.getValue())).multiply(new BigDecimal("100")).longValue();
                field.setName("ir.price_tax_amount");
                field.setValue(l);
                field.setMethod(4);
            }
            if ("settleAmountMax".equals(field.getName())) {
                long l = new BigDecimal(String.valueOf(field.getValue())).multiply(new BigDecimal("100")).longValue();
                field.setName("ir.price_tax_amount");
                field.setValue(l);
                field.setMethod(6);
            }
        }
    }

    /**
     * 收入/退款单页面列表 条件转换
     * @param conditions
     */
    public static void billClaimPageConvertSearchType(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;

        while (iterator.hasNext()) {
            field = iterator.next();
            if ("sys_source".equals(field.getName())) {
                field.setName("b.sys_source");
            }
            if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
                if ("pay_channel".equals(field.getName())) {
                    field.setName("pd.pay_channel");
                }
            }else {
                if ("pay_channel".equals(field.getName())) {
                    field.setName("b.pay_channel");
                }
            }
            if ("bill_no".equals(field.getName())) {
                field.setName("b.bill_no");
            }
            if ("start_date".equals(field.getName())) {
                field.setName("b.pay_time");
                field.setMethod(4);
            }
            if ("end_date".equals(field.getName())) {
                Date endOfDay = getEndOfDay(field.getValue().toString());
                if (Objects.isNull(endOfDay)) {
                    iterator.remove();
                    continue;
                }
                field.setName("b.pay_time");
                field.setValue(endOfDay);
                field.setMethod(6);
            }
            if ("pay_time".equals(field.getName())) {
                field.setName("b.pay_time");
                if (field.getMethod() == 6){
                    Date endOfDay = getEndOfDay(field.getValue().toString());
                    if (Objects.isNull(endOfDay)) {
                        iterator.remove();
                        continue;
                    }
                    field.setName("b.gmt_create");
                    field.setValue(endOfDay);
                    field.setMethod(6);
                }
            }
            if ("customer_name".equals(field.getName())) {
                field.setName("b.payer_name");
            }
            if ("cost_center_name".equals(field.getName())) {
                field.setName("pd.cost_center_name");
            }
            if ("room_name".equals(field.getName())) {
                field.setName("pd.cp_unit_name");
            }
            if ("settleAmountMin".equals(field.getName())) {
                long l = new BigDecimal(String.valueOf(field.getValue())).multiply(new BigDecimal("100")).longValue();
                field.setName("b.total_amount");
                field.setValue(l);
                field.setMethod(4);
            }
            if ("settleAmountMax".equals(field.getName())) {
                long l = new BigDecimal(String.valueOf(field.getValue())).multiply(new BigDecimal("100")).longValue();
                field.setName("b.total_amount");
                field.setValue(l);
                field.setMethod(6);
            }
        }
    }

    private static Date getEndOfDay(String endDate) {
        if (StringUtils.isBlank(endDate)) {
            return null;
        }
        DateTime tempDate = DateUtil.parse(endDate, "yyyy-MM-dd");
        return DateUtil.endOfDay(tempDate);
    }

    public static String getTimeUtils(Integer day){
        Calendar calendar1 = Calendar.getInstance();
        // 这里查询应该使用yyyy-MM-dd格式
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar1.add(Calendar.DATE, day);
        String three_days_ago = sdf1.format(calendar1.getTime());
        return three_days_ago;
    }

    public static void channelFlowClaimConvertSearchType(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            if ("business_id".equals(field.getName())){
                iterator.remove();
            }
            if ("id".equals(field.getName())){
                iterator.remove();
            }
            if ("trade_no".equals(field.getName())) {
                field.setName("ry.seq_id");
            }
            if("channel_trade_amount".equals(field.getName())){
                field.setName("ry.trade_amount");
            }
            if ("commission".equals(field.getName())){
                field.setName("ry.commission");
            }
            if ("community_name".equals(field.getName())){
                field.setName("gb.sup_cp_unit_name");
            }
            if ("flow_state".equals(field.getName())){
                field.setName("gb.mc_reconcile_state");
            }

            if ("statutory_body_name".equals(field.getName())){
                field.setName("gb.statutory_body_name");
            }
        }
    }


    public static void channelFlowClaimConvertSearchTypeForYY(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            if ("business_id".equals(field.getName())){
                iterator.remove();
            }
            if ("id".equals(field.getName())){
                iterator.remove();
            }
            if ("flag".equals(field.getName())){
                iterator.remove();
            }
            if ("mid".equals(field.getName())) {
                field.setName("ry.mid");
            }
            if ("trade_no".equals(field.getName())) {
                field.setName("ry.search_reference_no");
            }

            if("channel_trade_amount".equals(field.getName())){
                field.setName("ry.trade_amount");

            }
            if ("commission".equals(field.getName())){
                field.setName("ry.commission");
            }
            if ("community_name".equals(field.getName())){
                field.setName("gb.sup_cp_unit_name");
            }
            if ("flow_state".equals(field.getName())){
                field.setName("gb.mc_reconcile_state");
            }
            if ("statutory_body_name".equals(field.getName())){
                field.setName("gb.statutory_body_name");
            }
            if ("bill_no".equals(field.getName())){
                field.setName("gb.bill_no");
            }
            if ("pay_time".equals(field.getName())){
                field.setName("gb.pay_time");
            }

        }
    }

}
