package com.wishare.finance.infrastructure.aspect;

import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeadExcelColor {
    /**
     * 字体颜色
     * 默认IndexedColors.RED
     */
    IndexedColors backgroundColor();

    /**
     * 默认必填 颜色为红色
     *
     * @return
     */
    boolean required() default true;

}
