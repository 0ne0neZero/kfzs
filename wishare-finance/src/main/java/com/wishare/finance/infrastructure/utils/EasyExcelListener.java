package com.wishare.finance.infrastructure.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.wishare.finance.domains.imports.entity.ExcelSheet;
import com.wishare.finance.infrastructure.aspect.ExcelErrMsg;
import com.wishare.finance.infrastructure.beans.ExcelImportResult;
import com.wishare.finance.infrastructure.interfaces.ExcelCheckManager;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: zhengHui
 * @CreateTime: 2022-06-29  16:02
 * @Description:
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
@Data
public class EasyExcelListener<T> implements ReadListener<T> {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 成功结果集
     */
    private List<T> successList = new ArrayList<>();

    /**
     * 失败结果集
     */
    private List<T> errList = new ArrayList<>();

    /**
     * 记录成功的条数
     */
    private Long totalSuccessNum = 0L;

    /**
     * 是否记录成功的标志
     */
    private Boolean recordSuccessData;

    /**
     * 处理逻辑service
     */
    private ExcelCheckManager excelCheckManager;

    /**
     * 需要跳过表头校验的行数
     */
    private Integer headSkipRow;

    private List<T> list = new ArrayList<>();

    /**
     * excel对象的反射类
     */
    private Class<T> clazz;

    /**
     * 收集表头和错误数据，利用easyExcel将错误数据返回给用户
     */
    private List<List<String>> head = new ArrayList<>();
    /**
     * 数据转换异常捕获
     */
    public List<Map<String, Object>> convertErrData = new ArrayList<>();
    /**
     * filedName
     */
    String[] dataStrMap = {};

    String[] headTitle = {};

    /**
     * excelSheet.getConvertDataExceptionSave()配置为false时候存储的对象
     * List<Map<Object, Object>>
     */
    List<Map<Object, Object>> errListFail = new ArrayList<>();
    /**
     * 当前登录人的租户id
     */
    private ExcelSheet excelSheet;


    public EasyExcelListener(ExcelCheckManager excelCheckManager, Class<T> clazz, Integer headSkipRow, Boolean recordSuccessData, ExcelSheet excelSheet) {
        this.excelCheckManager = excelCheckManager;
        this.clazz = clazz;
        this.headSkipRow = headSkipRow;
        this.recordSuccessData = recordSuccessData;
        this.excelSheet = excelSheet;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param context one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        Class<?> superclass = null;
        String errMsg;
        try {
            /**
             * 根据excel数据实体中的javax.validation + 正则表达式来校验excel数据
             */
            errMsg = EasyExcelValiUtil.validateEntity(t);
            //手动设置行号 唯一性校验使用
            Integer rowIndex = context.readRowHolder().getRowIndex() + 1;
            superclass = t.getClass().getSuperclass();
            Field rowNumber = superclass.getDeclaredField("rowNumber");
            rowNumber.setAccessible(true);
            rowNumber.set(t, Long.valueOf(rowIndex));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            errMsg = "解析数据出错";
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(errMsg)) {
            try {
                Field errMsg1 = superclass.getField("errMsg");
                errMsg1.setAccessible(true);
                errMsg1.set(t, errMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //ExcelImportErrResult excelImportErrResult = new ExcelImportErrResult(t, errMsg);
            errList.add(t);
        } else {
            list.add(t);
        }
        //每1000条处理一次  此处解析完毕完成 还是会调用 doAfterAllAnalysed 最后重新解析
        if (list.size() >= 1000) {
            //校验
            ExcelImportResult result = null;
            try {
                result = excelCheckManager.checkImportExcel(list, excelSheet);
                List successList = result.getSuccessList();
                if (recordSuccessData) {
                    this.successList.addAll(successList);
                }
                totalSuccessNum = totalSuccessNum + successList.size();
                if (excelSheet.getConvertDataExceptionSave()) {
                    errList.addAll(result.getErrList());
                } else {
                    List<Map<Object, Object>> tmpErrList = entityConvertMap(result.getErrList());
                    if (!CollectionUtils.isEmpty(tmpErrList) && tmpErrList.size() > 0) {
                        errListFail.addAll(tmpErrList);
                    }
                }
                list.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    //所有数据解析完成了 都会来调用
    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (!CollectionUtils.isEmpty(list)) {
            ExcelImportResult result = excelCheckManager.checkImportExcel(list, excelSheet);
            if (!CollectionUtils.isEmpty(result.getErrList())) {
                if (excelSheet.getConvertDataExceptionSave()) {
                    errList.addAll(result.getErrList());
                } else {
                    List<Map<Object, Object>> tmpErrList = entityConvertMap(result.getErrList());
                    if (!CollectionUtils.isEmpty(tmpErrList) && tmpErrList.size() > 0) {
                        errListFail.addAll(tmpErrList);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(result.getSuccessList())) {
                successList.addAll(result.getSuccessList());
            }
            list.clear();
        }
    }

    /**
     * @param headMap 传入excel的头部（第一行数据）数据的index,name
     * @param context
     * @return void
     * @throws
     * @description: 校验excel头部格式，必须完全匹配
     * @author zhy
     * @date 2019/12/24 19:27
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (clazz != null) {
            try {
                Integer currentRowNum = context.readSheetHolder().getRowIndex();
                if (currentRowNum <= headSkipRow - 1) {
                    return;
                }
                Map<Integer, String> indexNameMap = getIndexNameMap(clazz);
                Set<Integer> keySet = indexNameMap.keySet();
                if (dataStrMap == null || dataStrMap.length == 0) {
                    dataStrMap = new String[indexNameMap.size() + 1];
                    if (excelSheet.isErrMsgIsHead()) {
                        getHeadErrMsgFiledNameMap(clazz);
                    } else {
                        getFiledNameMap(clazz);
                    }
                }
                if (headTitle == null || headTitle.length == 0) {
                    headTitle = new String[indexNameMap.size() + 1];
                    if (excelSheet.isErrMsgIsHead()) {
                        getHeadErrMsgNameMap(clazz);
                    } else {
                        getNameMap(clazz);
                    }

                }
                for (Integer key : keySet) {
                    ReadCellData<?> readCellData = headMap.get(key);
                    String stringValue = readCellData.getStringValue();
                    if (StringUtils.isBlank(stringValue)) {
                        throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                    }
                    if (!stringValue.equals(indexNameMap.get(key))) {
                        throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param clazz
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     * @throws
     * @description: 获取注解里ExcelProperty的value，用作校验excel
     * @author zhy
     * @date 2019/12/24 19:21
     */
    public Map<Integer, String> getIndexNameMap(Class clazz) throws NoSuchFieldException {
        Map<Integer, String> result = new HashMap<>();
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                int index = excelProperty.index();
                String[] values = excelProperty.value();
                StringBuilder value = new StringBuilder();
                for (String v : values) {
                    value.append(v);
                }
                result.put(index, value.toString());
            }
        }
        return result;
    }


    public void getFiledNameMap(Class clazz) throws NoSuchFieldException {
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                dataStrMap[i] = field.getName();
            }
        }
        dataStrMap[dataStrMap.length - 1] = "errMsg";
    }

    public void getHeadErrMsgFiledNameMap(Class clazz) throws NoSuchFieldException {
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            boolean headErrMsgF = field.isAnnotationPresent(ExcelErrMsg.class);
            if (excelProperty != null) {
                dataStrMap[i + 1] = field.getName();
            }
        }
        dataStrMap[0] = "errMsg";
    }

    public void getNameMap(Class clazz) throws NoSuchFieldException {
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                String value = excelProperty.value()[0];
                headTitle[i] = value;
            }
        }
        headTitle[headTitle.length - 1] = "导入错误原因";
    }

    public void getHeadErrMsgNameMap(Class clazz) throws NoSuchFieldException {
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                String value = excelProperty.value()[0];
                headTitle[i + 1] = value;
            }
        }
        headTitle[0] = "导入错误原因";
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ArrayIndexOutOfBoundsException) {
            return;
        }
        // 调用以下封装的错误数据收集工具
        EasyExcelErrorFixUtil.setErrorData(exception, context, convertErrData, dataStrMap);
    }


    public List<Map<Object, Object>> entityConvertMap(List<T> list) {
        List<Map<Object, Object>> l = new LinkedList<>();
        try {
            for (T t : list) {
                Map<Object, Object> map = new HashMap<>();
                Field fieldMsg = t.getClass().getSuperclass().getDeclaredField("errMsg");
                fieldMsg.setAccessible(true);
                String name = fieldMsg.getName();
                if (name != null) {
                    if (name.equals("errMsg")) {
                        map.put(fieldMsg.getName(), fieldMsg.get(t));
                    }
                }

                Field[] fields = t.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = clazz.getDeclaredField(fields[i].getName());
                    field.setAccessible(true);
                    boolean rowNumber = field.getName().equals("rowNumber");
                    if (rowNumber) {
                        continue;
                    }
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    if (excelProperty != null) {
                        map.put(field.getName(), field.get(t));
                    }
                }

                l.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

}
