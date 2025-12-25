package com.wishare.finance.infrastructure.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.FinanceSearchF;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 账单查询根据Field进行排序，提高查询效率
 * @author: huishen
 * @date: 2023/12/12 19:46
 **/

public class SearchFieldSortUtils {
    private static final Logger log = LoggerFactory.getLogger(SearchFieldSortUtils.class);

    public static QueryWrapper<?>  sortField(PageF<SearchF<?>> query) {
        List<Field> fieldList = query.getConditions().getFields();
        List<Field> fieldNewList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(fieldList)){
            List<Field> collect1 = Lists.newArrayList();
            List<Field> collect2 = Lists.newArrayList();
            List<Field> collect3 = Lists.newArrayList();
            List<Field> collect4 = Lists.newArrayList();
            List<Field> collect5 = Lists.newArrayList();
            List<Field> collect6 = Lists.newArrayList();
            List<Field> collect7 = Lists.newArrayList();
            List<Field> collect8 = Lists.newArrayList();
            List<Field> collect9 = Lists.newArrayList();
            List<Field> collect10 = Lists.newArrayList();
            List<Field> collect11 = Lists.newArrayList();
            List<Field> collect12 = Lists.newArrayList();
            List<Field> collect13 = Lists.newArrayList();
            List<Field> collect14 = Lists.newArrayList();
            List<Field> collect15 = Lists.newArrayList();
            for(Field field : fieldList){
                String name = field.getName();
                // 防止charge服务会传b.deleted 或者 rb.deleted 或者deleted
                String[] fieldArr = name.split("\\.");
                String fieldName = "";
                if(fieldArr.length == 1){
                    fieldName = fieldArr[0];
                }else if(fieldArr.length == 2){
                    fieldName = fieldArr[1];
                }
                if("deleted".equals(fieldName)){
                    collect1.add(field);
                }else if("approved_state".equals(fieldName)){
                    collect2.add(field);
                }else if("bill_type".equals(fieldName)){
                    collect3.add(field);
                }else if("state".equals(fieldName)){
                    collect4.add(field);
                }else if("reversed".equals(fieldName)){
                    collect5.add(field);
                }else if("carried_state".equals(fieldName)){
                    collect6.add(field);
                }else if("refund_state".equals(fieldName)){
                    collect7.add(field);
                }else if("sup_cp_unit_id".equals(fieldName)){
                    collect8.add(field);
                }else if("bill_label".equals(fieldName)){
                    collect9.add(field);
                }else if("overdue".equals(fieldName)){
                    collect10.add(field);
                }else if("charge_item_name".equals(fieldName)){
                    collect11.add(field);
                }else if("settle_state".equals(fieldName)){
                    collect12.add(field);
                }else if("room_name".equals(fieldName)){
                    collect13.add(field);
                }else if("room_id".equals(fieldName)){
                    collect14.add(field);
                }

            }
            if(!CollectionUtils.isEmpty(collect8)){
                fieldNewList.addAll(collect8);
            }
            if(!CollectionUtils.isEmpty(collect14)){
                fieldNewList.addAll(collect14);
            }
            if(!CollectionUtils.isEmpty(collect2)){
                fieldNewList.addAll(collect2);
            }
            if(!CollectionUtils.isEmpty(collect4)){
                fieldNewList.addAll(collect4);
            }
            if(!CollectionUtils.isEmpty(collect3)){
                fieldNewList.addAll(collect3);
            }
            if(!CollectionUtils.isEmpty(collect1)){
                fieldNewList.addAll(collect1);
            }
            if(!CollectionUtils.isEmpty(collect5)){
                fieldNewList.addAll(collect5);
            }
            if(!CollectionUtils.isEmpty(collect6)){
                fieldNewList.addAll(collect6);
            }
            if(!CollectionUtils.isEmpty(collect7)){
                fieldNewList.addAll(collect7);
            }
            if(!CollectionUtils.isEmpty(collect9)){
                fieldNewList.addAll(collect9);
            }
            if(!CollectionUtils.isEmpty(collect10)){
                fieldNewList.addAll(collect10);
            }
            if(!CollectionUtils.isEmpty(collect12)){
                fieldNewList.addAll(collect12);
            }
            if(!CollectionUtils.isEmpty(collect11)){
                fieldNewList.addAll(collect11);
            }
            if(!CollectionUtils.isEmpty(collect13)){
                fieldNewList.addAll(collect13);
            }
            List<Field> finalFieldList = getFinalFieldList(fieldNewList,fieldList);
            query.getConditions().getFields().removeAll(fieldList);
            query.getConditions().setFields(finalFieldList);
        }

        FinanceSearchF<?> financeSearchF = new FinanceSearchF<>();
        List<Field> fields = query.getConditions().getFields();
        financeSearchF.fieldNotLikeToNotIn(fields);

        return financeSearchF.getQueryModel();
    }


    /**
     * @description:把原始field去除，然后把fieldNewList加到list最上面
     * @author: huishen
     * @date: 2023/12/13 14:45
     * @param: [fieldNewList, fieldOriList]
     * @return: java.util.List<com.wishare.tools.starter.fo.search.Field>
     **/
    public static List<Field> getFinalFieldList(List<Field> fieldNewList,List<Field> fieldOriList){
        List<Field> fieldFinalList = Lists.newArrayList();
        Iterator<Field> iterator = fieldOriList.iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            String name = field.getName();
            // 防止charge服务会传b.deleted 或者 rb.deleted 或者deleted
            String[] fieldArr = name.split("\\.");
            String fieldName = "";
            if(fieldArr.length == 1){
                fieldName = fieldArr[0];
            }else if(fieldArr.length == 2){
                fieldName = fieldArr[1];
            }
            if("deleted".equals(fieldName)){
                iterator.remove();
            }else if("approved_state".equals(fieldName)){
                iterator.remove();
            }else if("bill_type".equals(fieldName)){
                iterator.remove();
            }else if("state".equals(fieldName)){
                iterator.remove();
            }else if("reversed".equals(fieldName)){
                iterator.remove();
            }else if("carried_state".equals(fieldName)){
                iterator.remove();
            }else if("refund_state".equals(fieldName)){
                iterator.remove();
            }else if("sup_cp_unit_id".equals(fieldName)){
                iterator.remove();
            }else if("bill_label".equals(fieldName)){
                iterator.remove();
            }else if("overdue".equals(fieldName)){
                iterator.remove();
            }else if("charge_item_name".equals(fieldName)){
                iterator.remove();
            }else if("settle_state".equals(fieldName)){
                iterator.remove();
            }else if("room_name".equals(fieldName)){
                iterator.remove();
            }else if("room_id".equals(fieldName)){
                iterator.remove();
            }else if("community_id".equals(fieldName)){
                iterator.remove();
            }
        }
        fieldFinalList.addAll(fieldNewList);
        fieldFinalList.addAll(fieldOriList);
        return fieldFinalList;

    }

    public static void main(String[] args) {
        String s = "rb.aa";
        String[] split = s.split("\\.");
        System.out.println(split.length);
        List<Field> fieldNewList = Lists.newArrayList();
        List<Field> fieldNewAll = Lists.newArrayList();
        Field field1 = new Field("11",11,1);
        Field field2 = new Field("deleted",11,1);
        Field field3 = new Field("approved_state",11,1);
        Field field4 = new Field("44",11,1);
        List<Field> list = Lists.newArrayList(field1,field2,field3,field4);


        List<Field> collect1 = Lists.newArrayList();
        List<Field> collect2 = Lists.newArrayList();
        for(Field field : list){
            String name = field.getName();
            // 防止charge服务会传b.deleted 或者 rb.deleted 或者deleted
            String[] fieldArr = name.split("\\.");
            String fieldName = "";
            if(fieldArr.length == 1){
                fieldName = fieldArr[0];
            }else if(fieldArr.length == 2){
                fieldName = fieldArr[1];
            }
            if("deleted".equals(fieldName)){
                collect1.add(field);
            }else if("approved_state".equals(fieldName)){
                collect2.add(field);
            }

        }
        fieldNewList.addAll(collect2);
        fieldNewList.addAll(collect1);

        Iterator<Field> iterator = list.iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            String name = field.getName();
            // 防止charge服务会传b.deleted 或者 rb.deleted 或者deleted
            String[] fieldArr = name.split("\\.");
            String fieldName = "";
            if(fieldArr.length == 1){
                fieldName = fieldArr[0];
            }else if(fieldArr.length == 2){
                fieldName = fieldArr[1];
            }
            if("deleted".equals(fieldName)){
                iterator.remove();
            }else if("approved_state".equals(fieldName)){
                iterator.remove();
            }
        }
        fieldNewAll.addAll(fieldNewList);
        fieldNewAll.addAll(list);
        System.out.println(11);
    }
}
