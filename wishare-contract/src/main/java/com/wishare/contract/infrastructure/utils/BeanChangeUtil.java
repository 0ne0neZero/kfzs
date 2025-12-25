package com.wishare.contract.infrastructure.utils;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/21/11:21
 */
public class BeanChangeUtil<T> {

    public String contrastObj(Object oldBean, Object newBean) {
        // 创建字符串拼接对象
        StringBuilder str = new StringBuilder();
        // 转换为传入的泛型T
        T pojo1 = (T) oldBean;
        T pojo2 = (T) newBean;
        // 通过反射获取类的Class对象
        Class clazz = pojo1.getClass();
        // 获取类型及字段属性
        Field[] fields = clazz.getDeclaredFields();
        return jdk8Before(fields, pojo1, pojo2, str,clazz);
//        return jdk8OrAfter(fields, pojo1, pojo2, str,clazz);
    }

    // jdk8 普通循环方式
    public String jdk8Before(Field[] fields,T pojo1,T pojo2,StringBuilder str,Class clazz){
        int i = 1;
        try {
            for (Field field : fields) {
                if(field.isAnnotationPresent(PropertyMsg.class)){
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    // 获取对应属性值
                    Method getMethod = pd.getReadMethod();
                    Object o1 = getMethod.invoke(pojo1);
                    Object o2 = getMethod.invoke(pojo2);
                    if (o1 == null || o2 == null) {
                        continue;
                    }
                    if (!o1.toString().equals(o2.toString())) {
                        str.append(i + "、" + field.getAnnotation(PropertyMsg.class).value() + ":" + "修改前=>" + o1 + ",修改后=>" + o2 + "\n");
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public  Map<String, Object> EntityToMapUtil(Object obj, String type){
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        //获取实体类的全部字段，包括继承的父类的字段
        Field[] Fields= getAllFields(clazz);
        for(Field field : Fields){
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = null;
            try {
                object = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("**********************");
                System.out.println("实体类转换Map出错");
                System.out.println("**********************");
            }
            map.put(fieldName, object);
        }
        return map;
    }

    public Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }
}