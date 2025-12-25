package com.wishare.finance.apps.model.bill.fo;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.tools.starter.enums.WithinDateTimeEnum;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class FinanceSearchF<T> extends SearchF {

    private static final String START = "start";
    private static final String END = "end";

    @Override
    public QueryWrapper<T> getQueryModel() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        for (Object field : super.getFields()) {
           createQueryWrapperF((Field) field, queryWrapper);
        }
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> createQueryWrapperF(Field field, QueryWrapper<T> queryWrapper){
        Map<String, Object> map = field.getMap();
        switch (field.getMethod()) {
            case 1:
                /*
                 * 等于
                 */
                queryWrapper.eq(field.getName(), field.getValue());
                break;
            case 2:
                /*
                 * 不等于
                 */
                queryWrapper.ne(field.getName(), field.getValue());
                break;
            case 3:
                /*
                 * 大于
                 */
                queryWrapper.gt(field.getName(), field.getValue());
                break;
            case 4:
                /*
                 * 大于等于
                 */
                queryWrapper.ge(field.getName(), field.getValue());
                break;
            case 5:
                /*
                 * 小于
                 */
                queryWrapper.lt(field.getName(), field.getValue());
                break;
            case 6:
                /*
                 * 小于等于
                 */
                queryWrapper.le(field.getName(), field.getValue());
                break;
            case 7:
                /*
                 * 包含(模糊查询)
                 */
                //如果map为空代表是单字段的模糊查询
                if (map.isEmpty()) {
                    queryWrapper.like(field.getName(), field.getValue());
                } else {
                    /*
                     * 非空代表是多字段对同一个值进行模糊查询，使用or连接
                     * 由于优先级问题，需要使用()包裹or连接的条件
                     */
                    queryWrapper.and(wrapper -> map.keySet().forEach(key -> wrapper.or().like(key, map.get(key))));
                }
                break;
            case 8:
                /*
                 * 开始于(模糊查询)
                 */
                queryWrapper.likeRight(field.getName(), field.getValue());
                break;
            case 9:
                /*
                 * 结束于(模糊查询)
                 */
                queryWrapper.likeLeft(field.getName(), field.getValue());
                break;
            case 10:
                /*
                 * 不包含(模糊查询)
                 */
                /*Object value = field.getValue();
                if(value instanceof List){

                }
                queryWrapper.notIn(field.getName(), (List) field.getValue());*/
                queryWrapper.notLike(field.getName(), field.getValue());
                break;
            case 11:
                /*
                 * 空
                 */
                queryWrapper.isNull(field.getName());
                break;
            case 12:
                /*
                 * 非空
                 */
                queryWrapper.isNotNull(field.getName());
                break;
            case 13:
                /*
                 * 执行between方法时，字段的值有两个，分2种情况：
                 * 1、在value中指定今日/昨日/明日/本周/上周/下周/本月/上月/下月/今年/去年/明年常量时，动态计算
                 * 2、使用map接收并获取
                 */
                if (null != field.getValue()) {
                    LocalDateTime[] startAndEnd = WithinDateTimeEnum.valueOf(field.getValue().toString()).startAndEnd();
                    queryWrapper.between(field.getName(), startAndEnd[0], startAndEnd[1]);
                } else if (map != null && !map.isEmpty()) {
                    queryWrapper.between(field.getName(), map.get(START), map.get(END));
                }
                break;
            case 14:
                /*
                 * 执行地图定位方法时，字段的值有多个，使用map接收并获取
                 * 遍历map，将查询条件添加到构造器
                 */
                for (String key : map.keySet()) {
                    queryWrapper.eq(key, map.get(key));
                }
                break;
            case 15:
                /*
                 * in
                 * 单字段多值匹配
                 */
                queryWrapper.in(field.getName(), (List) field.getValue());
                break;
            case 16:
                /*
                 * not in
                 * 单字段多值匹配
                 */
                queryWrapper.notIn(field.getName(), (List) field.getValue());
                break;
            case 101:
                /*
                 * 等于或为空
                 */
                queryWrapper.and(wrapper -> wrapper.eq(field.getName(), field.getValue()).or()
                        .isNull(field.getName()));
                break;
            default:
                break;
        }
        return queryWrapper;
    }

    public void fieldNotLikeToNotIn(List<Field> fields) {
        if (!CollectionUtils.isEmpty(fields)){
            for (Field field : fields) {
                if (ObjectUtil.isNotNull(field) && field.getMethod() != null && field.getMethod() == 10){
//                    if (field.getValue() instanceof Collection)
                    if (field.getValue() != null &&  field.getValue().toString().contains("[")){
                        field.setMethod(16);
                    }
                }
            }
        }
        setFields(fields);
    }
}
