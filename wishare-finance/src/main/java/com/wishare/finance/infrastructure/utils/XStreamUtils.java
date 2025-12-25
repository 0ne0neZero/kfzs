package com.wishare.finance.infrastructure.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
public class XStreamUtils {

    /**
     * @param obj 实体类
     * @return 实体类转XML字符串
     */
    public static String toXml(Object obj) {
        XStream xStream = new XStream(new DomDriver());
        // 扫描@XStream注解
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj).replaceAll("\\_+", "_");//正则过滤双下划线转为单下划线
    }

    /**
     * xml转bean
     *
     * @param xml
     * @param clazz
     * @return
     */

    public static Object xmltoBean(String xml, Class<?> clazz) {
        // DomDriver
        XStream xstream = new XStream(new DomDriver());
        // xstream 的安全框架没有初始化，xstream 容易受攻击,此处设置默认安全防护,同时设置允许的类
        XStream.setupDefaultSecurity(xstream);
        //		尽量限制所需的最低权限 这条语句解决该问题
        xstream.addPermission(AnyTypePermission.ANY);
        //应用传过来的类的注解
        xstream.processAnnotations(clazz);
        //自动检测注解
        xstream.autodetectAnnotations(true);
        //使用了默认安全框架,此处为必须项
        //xstream.allowTypeHierarchy(Root.class);
        // 两个类的相等性取决于类名和加载器,XStream必须使用不同的类加载器,防止ClassCastException的快速解决方案是设置当前线程使用的相同类加载器
        xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
        // 未定义字段不接收
        xstream.ignoreUnknownElements();
        Object xmlObject = xstream.fromXML(xml);
        return xmlObject;

    }
}
