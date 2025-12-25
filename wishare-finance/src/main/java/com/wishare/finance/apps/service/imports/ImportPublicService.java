package com.wishare.finance.apps.service.imports;

import com.alibaba.fastjson.JSON;
import com.wishare.component.imports.IPersistence;
import com.wishare.component.imports.IValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ImportPublicService {

    @SneakyThrows
    public List<?> rowNumberSetDefaultValue(List<?> obj) {
        AtomicInteger rowNumberAddFirst = new AtomicInteger(0);
        for (Object t : obj) {
            Class superclass = t.getClass().getSuperclass();
            Field rowNumber = superclass.getDeclaredField("rowNumber");
            rowNumber.setAccessible(true);
            rowNumber.set(t, Long.valueOf(rowNumberAddFirst.addAndGet(1)));
        }
        return obj;
    }


    @SneakyThrows
    public static List<?> rowNumberSetDefaultValues(List<?> obj,List<?> objSource) {
        AtomicInteger rowNumberAddFirst = new AtomicInteger(0);
        AtomicInteger rowNumberAddFirstSource = new AtomicInteger(0);
        for(int i = 0; i < obj.size(); i++){
            Class superclass = obj.get(i).getClass().getSuperclass();
            Field rowNumber = superclass.getDeclaredField("rowNumber");
            rowNumber.setAccessible(true);
            rowNumber.set(obj.get(i), Long.valueOf(rowNumberAddFirst.addAndGet(1)));

            Class superclassSource = objSource.get(i).getClass().getSuperclass();
            Field rowNumberSource = superclassSource.getDeclaredField("rowNumber");
            rowNumberSource.setAccessible(true);
            rowNumberSource.set(objSource.get(i), Long.valueOf(rowNumberAddFirstSource.addAndGet(1)));
        }
        /*for (Object t : obj) {
            Class superclass = t.getClass().getSuperclass();
            Field rowNumber = superclass.getDeclaredField("rowNumber");
            rowNumber.setAccessible(true);
            rowNumber.set(t, Long.valueOf(rowNumberAddFirst.addAndGet(1)));
        }*/
        return obj;
    }


    public <T> List<IPersistence.ErrorRow<T>> saveAfterHandler(List<T> dataList, Object obj) {
        List<IPersistence.ErrorRow<T>> errorRowList = new ArrayList<>();
        if (obj instanceof Boolean) {
            if ((boolean) obj == false) {
                for (Object o : dataList) {
                    IPersistence.ErrorRow<T> errorRow = new IPersistence.ErrorRow<T>((T) o, List.of(new IValidator.ErrorColumn("没有返回对应的具体错误信息")));
                    errorRowList.add(errorRow);
                }
            }
        } else if (obj instanceof List) {
            List<T> errList = (List) obj;
            errList.forEach(e -> {
                Long rowIndex = reflectRowIndex(e, "rowNumber");
                String errMsg = reflectErrMsg(e, "errMsg");
                for (Object w : dataList) {
                    try {
                        Long rowNumber = reflectRowIndex(w, "rowNumber");
                        if (rowNumber.equals(rowIndex)) {
                            errorRowList.add(new IPersistence.ErrorRow<T>((T)w, List.of(new IValidator.ErrorColumn(errMsg))));
                            break;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            });
        }
        return errorRowList;
    }

    private Long reflectRowIndex(Object remoteCall, String declareFieldName) {
        Class<?> superclass = remoteCall.getClass().getSuperclass();
        try {
            Field rowNumberFiled = superclass.getDeclaredField(declareFieldName);
            rowNumberFiled.setAccessible(true);
            Object value = ReflectionUtils.getField(rowNumberFiled, remoteCall);
            Long rowIndex = Long.parseLong(String.valueOf(value));
            return rowIndex;
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return null;
    }

    private String reflectErrMsg(Object remoteCall, String errMsgFieldName) {
        Class<?> superclass = remoteCall.getClass().getSuperclass();
        try {
            Field rowNumberFiled = superclass.getDeclaredField(errMsgFieldName);
            rowNumberFiled.setAccessible(true);
            Object value = ReflectionUtils.getField(rowNumberFiled, remoteCall);
            return String.valueOf(value);
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return null;
    }


    public static <T> List copyList(List<T> list,Class tClass) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JSON.parseArray(JSON.toJSONString(list), tClass);
    }

}
