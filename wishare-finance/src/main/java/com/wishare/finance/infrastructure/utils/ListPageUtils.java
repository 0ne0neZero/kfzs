package com.wishare.finance.infrastructure.utils;

import java.util.ArrayList;
import java.util.List;

public class ListPageUtils {

    /**
     * @param list  要分页的集合
     * @param pageNo    第几页
     * @param pageSize  每页条数
     * @return      分页集合对象
     */
    public static List page(List list, int pageNo, int pageSize){

        List result = new ArrayList();
        if (list != null && list.size() > 0) {
            int allCount = list.size();
            int pageCount = (allCount + pageSize - 1) / pageSize;
            int start = (pageNo - 1) * pageSize;
            int end = pageNo * pageSize;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(list.get(i));
            }
        }
        return (result.size() > 0) ? result : null;
    }

}



