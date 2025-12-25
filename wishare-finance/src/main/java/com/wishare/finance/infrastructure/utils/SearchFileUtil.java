package com.wishare.finance.infrastructure.utils;

import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author xujian
 * @date 2023/1/12
 * @Description:
 */
public class SearchFileUtil {

    //项目Id
    public static String communityId = "community_id";

    //项目名称
    public static String communityName = "community_name";

    //项目名称
    public static String roomName = "room_name";

    //费项的名称
    public static String chargeItemName = "charge_item_name";

    //开始日期
    public static String startDate = "start_date";

    //结束日期
    public static String endDate = "end_date";

    /**
     * 远洋默认物业管理费
     * <p>
     * 费项名称（必填，查询输入框，远洋默认为物业管理费）；
     */
    private static String chargeItemName_wuyeguanlifei = "物业管理费";


    /**
     * 获取费项
     *
     * @param form
     */
    public static String getChargeItemNameParam(PageF<SearchF<?>> form) {
        Field field = getConditions(form, chargeItemName);
        if (null == field) {
            return chargeItemName_wuyeguanlifei;
        }
        return (String) field.getValue();
    }

    /**
     * 获取项目Id
     *
     * @param form
     * @return
     */
    public static String getCommunityId(PageF<SearchF<?>> form) {
        Field field = getConditions(form, communityName);
        if (null != field ) {
            return (String) field.getValue();
        }
        return null;
    }

    /**
     * 获取开始日期
     *
     * @param form
     * @return
     */
    public static LocalDate getStartDate(PageF<SearchF<?>> form) {
        Field field = getConditions(form, startDate);
        if (null != field) {
            return (LocalDate) field.getValue();
        }
        return null;
    }



    /**
     * 获取结束日期
     *
     * @param form
     * @return
     */
    public static LocalDate getEndDate(PageF<SearchF<?>> form) {
        Field field = getConditions(form, endDate);
        if (null != field) {
            return (LocalDate) field.getValue();
        }
        return null;
    }

    /**
     * 获取项目名称
     *
     * @param form
     * @return
     */
    public static String getCommunityNameParam(PageF<SearchF<?>> form) {
        Field field = getConditions(form, communityName);
        if (null != field ) {
            return (String) field.getValue();
        }
        return null;
    }

    /**
     * 获取房屋名称
     *
     * @param form
     * @return
     */
    public static String getRoomNameParam(PageF<SearchF<?>> form) {
        Field field = getConditions(form, roomName);
        if (null != field ) {
            return (String) field.getValue();
        }
        return null;
    }


    /**
     * 获取里面的入参
     *
     * @return
     */
    public static  Field getConditions(PageF<SearchF<?>> form,String param){
        if (form.getConditions() != null) {
            Field field = getField(form, param);
            return field;
        }
        return null;
    }

    /**
     * 获取指定入参
     *
     * @param param
     * @param queryName
     * @return
     */
    public static Field getField(Object param, String queryName) {
        PageF<SearchF<?>> f = (PageF<SearchF<?>>) param;
        List<Field> fields = f.getConditions().getFields();
        Optional<Field> optionalField = fields.stream().filter(e -> queryName.equals(e.getName())).findAny();
        boolean empty = optionalField.isEmpty();
        if (empty) {
            return null;
        }
        return optionalField.get();
    }


}
