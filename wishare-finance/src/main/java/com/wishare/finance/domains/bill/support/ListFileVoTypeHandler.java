package com.wishare.finance.domains.bill.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.tools.starter.vo.FileVo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 结转明细列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListFileVoTypeHandler extends AbstractJsonTypeHandler<List<FileVo>> {

    @Override
    protected List<FileVo> parse(String json) {
        return JSONObject.parseArray(json, FileVo.class);
    }

    @Override
    protected String toJson(List<FileVo> obj) {
        return JSONObject.toJSONString(obj);
    }
}
