package com.wishare.finance.apis.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author szh
 * @date 2024/5/10 16:14
 */

@Slf4j
public class DeveloperPayFilterCondition {

    public static void handleDeveloperPay(PageF<SearchF<?>> queryF){
        if (ObjectUtil.isNull(queryF)){
            return;
        }
        if (ObjectUtil.isNull(queryF.getConditions())){
            return;
        }

        List<Field> list = queryF.getConditions().getFields();
        if (CollUtil.isEmpty(list)){
            return;
        }
        List<Field> collect = list.stream().filter(field -> !field.getName().equals("developerPay")).collect(Collectors.toList());
        for (Field field : list) {
            if (field.getName().equals("developerPay")){
                ArrayList<String> value = (ArrayList<String>)field.getValue();
                if ( value.get(0).equals("99")){
                    Field f = new Field();f.setName("b.payer_type");f.setMethod(1);f.setValue(99);
                    collect.add(f);

                    Field a = new Field();a.setName("b.description");a.setMethod(1);a.setValue("开发代付");
                    collect.add(a);
                }
                queryF.getConditions().setFields(collect);
                log.error("开发商代付过滤条件：{}", JSONObject.toJSONString(collect));
            }
        }


    }
}
