package com.wishare.finance.infrastructure.interfaces;

import com.wishare.finance.domains.imports.entity.ExcelSheet;
import com.wishare.finance.infrastructure.beans.ExcelImportResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ExcelCheckManager<T> {

    /**
     * @description:
     * @author: zhenghui
     * @date: 2022/6/30 15:36
     */
    ExcelImportResult checkImportExcel(List<T> objects, ExcelSheet excelSheet) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}