package com.wishare.finance.domains.imports;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.domains.imports.entity.ExcelSheet;
import com.wishare.finance.domains.imports.vo.ExcelCheckData;
import com.wishare.finance.infrastructure.beans.ExcelImporReturn;
import com.wishare.finance.infrastructure.beans.ExcelImportResult;
import com.wishare.finance.infrastructure.enums.WishareBeanMapUtil;
import com.wishare.finance.infrastructure.interfaces.ExcelCheckManager;
import com.wishare.finance.infrastructure.support.excel.AutoHeadColumnWidthStyleStrategy;
import com.wishare.finance.infrastructure.support.excel.ExcelSelectorDataWriteHandler;
import com.wishare.finance.infrastructure.utils.EasyExcelListener;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
public class ExcelCheckDataService<T> implements ExcelCheckManager<T>, ApplicationContextAware {

    public ApplicationContext applicationContext;

    @Override
    public ExcelImportResult checkImportExcel(List<T> objects, ExcelSheet excelSheet) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //成功结果集
        List<T> successList = new ArrayList<>();
        //错误数组
        List<T> errList = new ArrayList<>();

        ExcelCheckData<T> selfCheckData = validSelf(objects);
        for (T obj : selfCheckData.getSuccessList()) {
            //错误信息
            String errMsg = null;
            if (excelSheet.getCurrentExcelSheetGet()) {
                errMsg = validChoiceInfo(obj, excelSheet);
            } else {
                errMsg = validChoiceInfo(obj);
            }
            //根据自己的业务去做判断
            if (StringUtils.isEmpty(errMsg)) {
                //这里有两个选择，1、一个返回成功的对象信息，2、进行持久化操作
                successList.add(obj);
            } else {//添加错误信息
                Method method = obj.getClass().getMethod("setErrMsg", String.class);
                method.invoke(obj, errMsg);
                errList.add(obj);
            }
        }

        if (!CollectionUtils.isEmpty(selfCheckData.getErrList())) {
            errList.addAll(selfCheckData.getErrList());
        }
        if (!CollectionUtils.isEmpty(successList)) {
            List<T> successListCp = (List<T>) WishareBeanMapUtil.INSTANCE.mapAsList(successList, getActualTypeArgument(getClass()));
            Map<String, List<T>> returnMap = null;
            List<T> failList = null;
            List<T> remoteCallList = null;
            Object data = null;
            //接收错误的数据

            if (excelSheet.getRemoteExcelSheetGet()) {
                data = remoteCall(successListCp, excelSheet);
            } else {
                data = remoteCall(successListCp);
            }
            if (data != null) {
                if (data instanceof Boolean) {
                    if ((boolean) data == false) {
                        remoteCallList = successListCp;
                    }
                } else if (data instanceof List) {
                    List<?> remoteData = (List<?>) data;
                    remoteCallList = (List<T>) WishareBeanMapUtil.INSTANCE.mapAsList(remoteData, getActualTypeArgument(getClass()));
                }
                returnMap = reshuffle(remoteCallList, successList, new ArrayList<>());
                successList = returnMap.get("success");
                failList = returnMap.get("fail");
                if (!CollectionUtils.isEmpty(failList)) {
                    errList.addAll(failList);
                }
            }
            // 防止远程的数据全部都是失败的
            if (!CollectionUtils.isEmpty(successList)) {
                List<T> tmp = (List<T>) WishareBeanMapUtil.INSTANCE.mapAsList(successList, getActualTypeArgument(getClass()));
                //保存接口
                List<T> dbSaveFailList = this.saveSuccessData(successList);
                returnMap = reshuffle(dbSaveFailList, tmp, new ArrayList<>());
                successList = returnMap.get("success");
                failList = returnMap.get("fail");
                if (!CollectionUtils.isEmpty(failList)) {
                    errList.addAll(failList);
                }
            }
        }
        Collections.sort(errList, (o1, o2) -> {
            try {
                Method method = o1.getClass().getMethod("getRowNumber");
                return Long.compare((Long) method.invoke(o1), (Long) method.invoke(o2));
            } catch (Exception e) {
                log.error("excel排序异常");
                throw new RuntimeException(e);
            }
        });
        return new ExcelImportResult(successList, errList);
    }

    /**
     * 校验文件本身的数据
     *
     * @param objects
     * @param
     * @return
     */
    public ExcelCheckData<T> validSelf(List<T> objects) {
        ExcelCheckData<T> excelCheckData = new ExcelCheckData<T>();
        excelCheckData.setSuccessList(objects);
        return excelCheckData;
    }

    //此接口为将调用的失败的数据从成功的数据当中去除，填入失败的集合当中
    private Map<String, List<T>> reshuffle(List<T> remoteCallList, List<T> successList, List<T> errList) {
        List<T> successTmpList = successList;
        Map<String, List<T>> returnMap = new HashMap<>();
        if (remoteCallList != null && remoteCallList.size() > 0) {
            List<Long> errRowIndex = reflectRowIndexs(remoteCallList);
            errList.addAll(remoteCallList);
            if (successList != null) {
                successList = successList.stream().filter(e -> !errRowIndex.contains(reflectRowIndex(e, "rowNumber"))).collect(Collectors.toList());
            }
        }
        List<T> failList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(errList)) {
            errList.forEach(e -> {
                Long rowIndex = reflectRowIndex(e, "rowNumber");
                String errMsg = reflectErrMsg(e, "errMsg");
                for (T w : successTmpList) {
                    try {
                        Long rowNumber = reflectRowIndex(w, "rowNumber");
                        if (rowNumber.equals(rowIndex)) {
                            Method method = w.getClass().getMethod("setErrMsg", String.class);
                            method.invoke(w, errMsg);
                            failList.add(w);
                            break;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            });
        }
        returnMap.put("success", successList);
        returnMap.put("fail", failList);
        return returnMap;
    }

    //反射获取rowIndex
    private List<Long> reflectRowIndexs(List<T> remoteCall) {
        return remoteCall.stream().map(e -> {
            try {
                Class<?> superclass = e.getClass().getSuperclass();
                Field rowNumberFiled = superclass.getDeclaredField("rowNumber");
                rowNumberFiled.setAccessible(true);
                Object value = ReflectionUtils.getField(rowNumberFiled, e);
                Long rowIndex = Long.parseLong(String.valueOf(value));
                return rowIndex;
            } catch (NoSuchFieldException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    private Long reflectRowIndex(T remoteCall, String declareFieldName) {
        Class<?> superclass = remoteCall.getClass().getSuperclass();
        try {
//            Field rowNumberFiled = superclass.getDeclaredField("rowNumber");
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

    private String reflectErrMsg(T remoteCall, String declareFieldName) {
        Class<?> superclass = remoteCall.getClass().getSuperclass();
        try {
//            Field rowNumberFiled = superclass.getDeclaredField("rowNumber");
            Field rowNumberFiled = superclass.getDeclaredField(declareFieldName);
            rowNumberFiled.setAccessible(true);
            Object value = ReflectionUtils.getField(rowNumberFiled, remoteCall);
            return String.valueOf(value);
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return null;
    }

    //远程调用第三方的方法
    public Object remoteCall(List<T> successList) {
        return true;
    }

    public Object remoteCall(List<T> successList, ExcelSheet excelSheet) {
        return true;
    }

    /**
     * 数据校验
     *
     * @param obj
     * @return
     */
    public String validChoiceInfo(T obj) {
        return null;
    }

    /**
     * 数据校验
     *
     * @param obj
     * @return
     */
    public String validChoiceInfo(T obj, ExcelSheet excelSheet) {
        return null;
    }

    /**
     * 数据校验成功数据的通用保存接口 如需要库中保存，请重写此接口，并返回插入失败的数据和告知原因
     *
     * @param objects
     * @return 返回插入成功的条数
     */
    public List<T> saveSuccessData(List<T> objects) {
        return null;
    }

    public ExcelImporReturn importExcel(MultipartFile file, ExcelSheet excelSheet) throws IOException {
        // 标记是否存在失败的数据 存在则代表导出excel
        boolean exportExcel = false;
        //数据大小限制
        log.info("file size : {}", file.getSize());
        if (file.getSize() > excelSheet.getExcelLimitSize()) {
            throw BizException.throw402("excel数据大小超过指定的限制，请控制在" + excelSheet.getExcelLimitM() + "以内");
        }

        ExcelImporReturn excelImporReturn = new ExcelImporReturn();
        Integer headSkipRow = excelSheet.getHeadSkipRow();
        List<String> sheetNames = excelSheet.getSheetNames();
        Boolean recordSuccessData = excelSheet.getRecordSuccessData();
        Boolean returnFailData = excelSheet.getReturnFailData();
        if (sheetNames.size() <= 0) {
            return excelImporReturn;
        }
        InputStream inputStream = null;
        if (file != null) {
            inputStream = new BufferedInputStream(file.getInputStream());
        }
        Class<?> actualTypeArgument = getActualTypeArgument(getClass());
        //Class<? extends ExcelCheckDataServcie> aClass = getClass();

        final Map<String, List<?>> dataMap = new HashMap<>();
        final Map<String, List<?>> successData = new HashMap<>();
        Map<String, List<Map<String, Object>>> convertMap = new HashMap();

        ExcelReader excelReader = EasyExcel.read(inputStream).build();
        String[] headTitle = null;
        String[] dataStrMap = null;
        for (String sheetName : sheetNames) {
            EasyExcelListener easyExcelListener = null;
            if (headSkipRow >= 0) {
                easyExcelListener = new EasyExcelListener((ExcelCheckManager) this, actualTypeArgument, headSkipRow, recordSuccessData, excelSheet);
            } else {
                throw BizException.throw400("请输入开始读取的行数");
            }
            try {
                // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
                ReadSheet readSheet =
                        EasyExcel.readSheet(sheetName).head(actualTypeArgument).headRowNumber(headSkipRow + 1).registerReadListener(easyExcelListener).build();
                excelReader.read(readSheet);
                List<Map<String, Object>> convertDataErr = easyExcelListener.convertErrData;
                List<?> data = null;
                if (excelSheet.getConvertDataExceptionSave()) {
                    data = easyExcelListener.getErrList();
                    convertMap.put(sheetName, convertDataErr);
                    dataMap.put(sheetName, data);
                } else {
                    data = easyExcelListener.getErrListFail();
                    convertDataErr.addAll((List<Map<String, Object>>) data);
                    dataMap.put(sheetName, convertDataErr);
                }
                if (data.size() > 0 || convertDataErr.size() > 0) {
                    exportExcel = true;
                }
                //数据转换异常的错误
                successData.put(sheetName, easyExcelListener.getSuccessList());
                headTitle = easyExcelListener.getHeadTitle();
                dataStrMap = easyExcelListener.getDataStrMap();
            } catch (Throwable e) {
                e.printStackTrace();
                //throw new ExcelAnalysisException("模板不合法");
            }
        }

        if (excelReader != null) {
            excelReader.close();
        }
        //excelReader.finish();
        //返回插入未成功的数据
       /* Map<String, List<?>> remoteFailDB = remoteCall(successData);
        if (!CollectionUtils.isEmpty(remoteFailDB)) {
            sheetNames.stream().forEach(e -> {
                List<Object> sheetErrData = (List<Object>) remoteFailDB.get(e);
                if (CollectionUtils.isEmpty(sheetErrData)) {
                    List<Object> originData = (List<Object>) dataMap.get(e);
                    if (CollectionUtils.isEmpty(originData)) {
                        dataMap.put(e, originData);
                    } else {
                        dataMap.put(e, Collections.singletonList(originData.addAll(sheetErrData)));
                    }
                }
            });
        }*/
        FileVo fileVo = null;
        // 优化 针对没有错误信息的数据 不调用打印excel
        if (exportExcel) {
            fileVo = writeSheets(getResponse(), file, sheetNames, actualTypeArgument, dataMap, excelSheet, convertMap, headTitle, dataStrMap);
        }
        if (returnFailData && fileVo != null) {
            excelImporReturn.setFailData(dataMap);
            excelImporReturn.setConvertFailData(convertMap);
        } else {
            excelImporReturn.setFailTotal(dataMap);
            excelImporReturn.setConvertFailTotal(convertMap);
        }
        if (recordSuccessData) {
            excelImporReturn.setSuccessData(successData);
        } else {
            excelImporReturn.setSuccessTotal(successData);
        }
        if (file != null && fileVo != null) {
            excelImporReturn.setExcelLinkUrl(fileVo.getFileKey());
        }

        Integer failTotal = excelImporReturn.getFailTotal();
        Integer successTotal = excelImporReturn.getSuccessTotal();
        if (failTotal == 0 && successTotal == 0) {
            throw BizException.throw402("文件导入失败，请不要输入空数据文件");
        }
        // 记录导出失败信息
        //importRecordBuild(excelImporReturn, excelSheet);
        return excelImporReturn;
    }


    public FileVo writeSheets(HttpServletResponse response, MultipartFile file, List<String> sheetNames, Class<?> actualTypeArgument, Map<String, List<?>> errData, ExcelSheet excelSheet, Map<String, List<Map<String, Object>>> convertMap, String[] headTitle, String[] dataStrMap) {
        if (errData == null || errData.size() <= 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out).build();
        ExcelSelectorDataWriteHandler writeHandler = new ExcelSelectorDataWriteHandler(ExcelSelectorDataWriteHandler.getRequiredMap(actualTypeArgument, excelSheet.isErrMsgIsHead()));
        try {
            //新建ExcelWriter
            for (String sheetName : sheetNames) {
                WriteSheet writeSheet = null;
                List<?> objects = errData.get(sheetName);
                if (excelSheet.getConvertDataExceptionSave()) {
                    writeSheet = EasyExcel.writerSheet(sheetName).registerWriteHandler(writeHandler)
                            .registerWriteHandler(new AutoHeadColumnWidthStyleStrategy())
                            .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 20, (short) 20))
                            .head(actualTypeArgument).build();
//                    writeSheet = EasyExcel.writerSheet(sheetName).head(actualTypeArgument).build();
                    excelWriter.write(objects, writeSheet);
                } else {
                    writeSheet = EasyExcel.writerSheet(sheetName).registerWriteHandler(writeHandler)
                            .registerWriteHandler(new AutoHeadColumnWidthStyleStrategy())
                            .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 20, (short) 20)).head(head(headTitle)).build();
//                    writeSheet = EasyExcel.writerSheet(sheetName).head(head(headTitle)).build();
                    excelWriter.write(dataList((List<Map<String, Object>>) objects, dataStrMap), writeSheet);
                }
            }
            if (convertMap != null && convertMap.size() > 0) {
                appendExcel(convertMap, sheetNames, headTitle, dataStrMap, excelWriter);
            } else {
                excelWriter.finish();
            }
            byte[] bytes = out.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            MultipartFile fileTo = new MockMultipartFile(file.getName(), file.getOriginalFilename(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            FileStorage fileStorage = applicationContext.getBean(FileStorage.class);
            FileVo fileVo = null;
            try {
                fileVo = fileStorage.tmpSave(fileTo, excelSheet.getTenantIdAlias());
                if (fileVo != null) {
                    //build(fileVo, file, excelSheet, 1, 0, 0);
                }
            } catch (RuntimeException runtimeException) {
                //build(null, file, excelSheet, 0, 0, 0);
            }
            System.out.println(fileVo);
            return fileVo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            excelWriter.finish();
        }
        return null;
    }

    void appendExcel(Map<String, List<Map<String, Object>>> convertMap, List<String> sheetNames, String[] headTitle, String[] dataStrMap, ExcelWriter excelWriter) {
        for (int i = 0; i < sheetNames.size(); i++) {
            List<Map<String, Object>> list = convertMap.get(sheetNames.get(i));
            if (!CollectionUtils.isEmpty(list)) {
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNames.get(i) + "转换异常").head(head(headTitle)).build();
                excelWriter.write(dataList(list, dataStrMap), writeSheet);
            }
        }
        excelWriter.finish();
    }

    void appendCurrentSheetExcel(Map<String, List<Map<String, Object>>> convertMap, List<String> sheetNames, String[] headTitle, String[] dataStrMap, ExcelWriter excelWriter) {
        for (int i = 0; i < sheetNames.size(); i++) {
            List<Map<String, Object>> list = convertMap.get(sheetNames.get(i));
            if (!CollectionUtils.isEmpty(list)) {
                //excelWriter.writeContext().writeSheetHolder().getWriteSheet()
                //EasyExcel.writerSheet(sheetNames.get(i)).head(head(headTitle));do
                //EasyExcel.readSheet()
                //   excelWriter.write(dataList(list, dataStrMap), writeSheet);
            }
        }
        excelWriter.finish();
       /* if (!CollectionUtils.isEmpty(convertList)) {
            WriteSheet writeSheet = excelWriter.writeContext().writeSheetHolder().getWriteSheet();
            writeSheet.setHead(head(headTitle));
            excelWriter.write(dataList(convertList, dataStrMap), writeSheet);

        }*/
    }

    /*public Map<String, List<?>> remoteCall(Map<String, List<?>> successData) {
        return null;
    }*/
    /*public void writeSheets(HttpServletResponse response, String fileName, List<String> sheetNames, Class<?> actualTypeArgument, Map<String, Object> errData, String tenantId) {
        if (errData == null || errData.size() <= 0) {
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out).build();

        final CountDownLatch latch = new CountDownLatch(sheetNames.size());
        //新建ExcelWriter
        for (String sheetName : sheetNames) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(sheetName);
                        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(actualTypeArgument).build();
                        List<?> objects = (List<?>) errData.get(sheetName);
                        excelWriter.write(objects, writeSheet);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        // 很关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
                        latch.countDown();
                    }
                }
            }).start();
        }
        if (excelWriter != null) {
            excelWriter.close();
        }
        try {
            // 线程countDown()都执行之后才会释放当前线程,程序才能继续往后执行
            latch.await();
            byte[] bytes = out.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            MultipartFile file = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            fileStorage.tmpSave(file, tenantId);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }*/


    public void writeSheets1(HttpServletResponse response, MultipartFile file, List<String> sheetNames, Class<?> actualTypeArgument, Map<String, Object> errData) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        final CountDownLatch latch = new CountDownLatch(sheetNames.size());
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            //fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + file.getName() + ".xlsx");
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            for (String sheetName : sheetNames) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(actualTypeArgument).build();
                            List<?> objects = (List<?>) errData.get(sheetName);
                            excelWriter.write(objects, writeSheet);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        } finally {
                            // 很关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
                            latch.countDown();
                        }
                    }
                }).start();
            }
            //关闭流
            excelWriter.finish();

        } catch (IOException e) {
            //log.error("导出异常{}", e.getMessage());
        }
    }

    public void writeUpload(HttpServletResponse response, String fileName, List<String> sheetNames, Class<?> actualTypeArgument, Map<String, List<?>> errData) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //新建ExcelWriter
        ExcelWriter excelWriter = EasyExcel.write(out).build();
        for (String sheetName : sheetNames) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(actualTypeArgument).build();
            List<?> objects = errData.get(sheetName);
            excelWriter.write(objects, writeSheet);
        }
        //关闭流
        excelWriter.finish();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }


    public static Class<?> getActualTypeArgument(Class<?> clazz) {
        Class<?> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<?>) actualTypeArguments[0];
            }
        }
        return entitiClass;
    }


    // 转换file对象可供下载
    public File transferToFile(MultipartFile multipartFile) {
        //选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            // 获取文件名
            String fileName = multipartFile.getOriginalFilename();
            // 获取文件后缀
            //String prefix = fileName.substring(fileName.lastIndexOf("."));
            // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
            file = File.createTempFile(fileName, null);
            multipartFile.transferTo(file);
            String fileAbsolutePath = file.getAbsolutePath();
            String substring = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("\\/"));
            File file1 = new File(file.getAbsolutePath() + fileName);
            file.renameTo(file1);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    //设置表头
    private static List<List<String>> head(String[] headMap) {
        List<List<String>> list = new ArrayList<List<String>>();
        for (String head : headMap) {
            List<String> headList = new ArrayList<String>();
            headList.add(head);
            list.add(headList);
        }
        return list;
    }

    //设置导出的数据内容
    private static List<List<String>> dataList(List<Map<String, Object>> dataList, String[] dataStrMap) {
        //这里可用Object 单时间会出错，建议采用String类型
        List<List<String>> list = new ArrayList<List<String>>();
        for (Map<String, Object> map : dataList) {
            List<String> data = new ArrayList<String>();
            for (int i = 0; i < dataStrMap.length; i++) {
                Object o = map.get(dataStrMap[i]);
                if (o == null) {
                    data.add("");
                } else {
                    data.add(o.toString());
                }
            }
            list.add(data);
        }
        return list;
    }

}