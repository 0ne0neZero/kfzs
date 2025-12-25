package com.wishare.finance.domains.voucher.support;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;


/**
 * 费项列表类型转换
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListUploadLinkZJHandler extends AbstractJsonTypeHandler<List<UploadLinkZJ>> {


    @Override
    protected List<UploadLinkZJ> parse(String json) {
        return JSONObject.parseArray(json, UploadLinkZJ.class);
    }

    @Override
    protected String toJson(List<UploadLinkZJ> obj) {
        return JSONObject.toJSONString(obj);
    }
}
