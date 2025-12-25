package com.wishare.finance.domains.imports.entity;

import com.wishare.finance.infrastructure.enums.ExcelSheetEnum;
import com.wishare.starter.interfaces.ApiBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Accessors(chain = true)
public class ExcelSheet implements ApiBase {

    @ApiModelProperty("导入的账单类型: 异步导入的时候查询导出记录的时候需要的")
    private Integer importType;

    @ApiModelProperty("多sheet页name")
    private List<String> sheetNames;

    @ApiModelProperty("需要跳过几行才能读取到对象头")
    private Integer headSkipRow;

    @ApiModelProperty("租户id")
    private String tenantId = getTenantId().get();

    @ApiModelProperty("excel限制大小")
    private Integer excelLimitSize = 10 * 1024 * 1024;

    @ApiModelProperty("超过限制大小的时候需要的提示")
    private String excelLimitM;

    @ApiModelProperty("是否记录成功的数据")
    private Boolean recordSuccessData = false;

    @ApiModelProperty("是否返回失败的数据前台")
    private Boolean returnFailData = false;

    @ApiModelProperty("用户id")
    private String userId = getUserId().orElse(StringUtils.EMPTY);

    @ApiModelProperty("用户名称")
    private String userName = getUserName().orElse(StringUtils.EMPTY);

    @ApiModelProperty("解析成功失败的或者上传模板文件的时候需要传递的业务类型")
    private Integer businessType;

    @ApiModelProperty("模板文件上传时候需要传递的实体类所在的class")
    private String templateClassName;

    @ApiModelProperty("当前对象是否在校验的时候能拿到，默认false")
    private boolean currentExcelSheetGet = false;

    @ApiModelProperty("远程方法调用的时候是否可以拿到excelSheet对象，默认false")
    private boolean remoteExcelSheetGet = false;

    @ApiModelProperty("转换异常的类是否需要分sheet存放，true，单独sheet存放，false一个sheet存放")
    private boolean convertDataExceptionSave = true;

    @ApiModelProperty("数据在导出的时候携带额外的数据，需要自己解析")
    private Object transmissionData;

    @ApiModelProperty("上传文件模板的时候是否保存原本对的上传文件名称:1: 保存原本文件名 2: 随机uuid作为文件的名称")
    private Integer isSaveFileOriginName = 1;

    @ApiModelProperty("errMsgIsHead: true 在头部，false：尾部")
    private boolean errMsgIsHead = true;

    public void setSheetNames(List<String> sheetNames) {
        this.sheetNames = sheetNames;
    }

    public void setHeadSkipRow(Integer headSkipRow) {
        this.headSkipRow = headSkipRow;
    }

    public void setExcelLimitSize(Integer excelLimitSize) {
        this.excelLimitSize = excelLimitSize * 1024 * 1024;
        excelLimitM = excelLimitSize + "M";
    }

    public Integer getIsSaveFileOriginName() {
        return isSaveFileOriginName;
    }

    public boolean getRemoteExcelSheetGet() {
        return remoteExcelSheetGet;
    }

    public void setRecordSuccessData(Boolean recordSuccessData) {
        this.recordSuccessData = recordSuccessData;
    }

    public void setReturnFailData(Boolean returnFailData) {
        this.returnFailData = returnFailData;
    }

    public boolean getCurrentExcelSheetGet() {
        return currentExcelSheetGet;
    }

    public boolean getConvertDataExceptionSave() {
        return convertDataExceptionSave;
    }

    public void setConvertDataExceptionSave(boolean convertDataExceptionSave) {
        this.convertDataExceptionSave = convertDataExceptionSave;
    }

    public void setUserIdAlias(String userId) {
        this.userId = userId;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public void setTenantIdAlias(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getSheetNames() {
        return sheetNames;
    }

    public Integer getHeadSkipRow() {
        return headSkipRow;
    }

    public Integer getExcelLimitSize() {
        return excelLimitSize;
    }

    public String getExcelLimitM() {
        return excelLimitM;
    }

    public Boolean getRecordSuccessData() {
        return recordSuccessData;
    }

    public Boolean getReturnFailData() {
        return returnFailData;
    }


    public String getTenantIdAlias() {
        return tenantId;
    }

    public String getUserIdAlias() {
        return userId;
    }

    public String getUserNameAlias() {
        return userName;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public Object getTransmissionData() {
        return transmissionData;
    }

    public ExcelSheet() {
    }

    public void setIsSaveFileOriginName(Integer isSaveFileOriginName) {
        this.isSaveFileOriginName = isSaveFileOriginName;
    }

    public void setTransmissionData(Object transmissionData) {
        this.transmissionData = transmissionData;
    }

    public Integer getImportType() {
        return importType;
    }

    public void setImportType(Integer importType) {
        this.importType = importType;
    }

    public ExcelSheet(List<String> sheetNames, Integer headSkipRow, ExcelSheetEnum excelSheetEnum) {
        this.sheetNames = sheetNames;
        this.headSkipRow = headSkipRow;
        if (excelSheetEnum == ExcelSheetEnum.VALID_CHOICE_INFO) {
            validChoiceInfoGetExcelSheet();
        } else if (excelSheetEnum == ExcelSheetEnum.REMOTE) {
            remoteGetExcelSheet();
        } else if (excelSheetEnum == ExcelSheetEnum.ALL) {
            allExcelSheet();
        } else if (excelSheetEnum == ExcelSheetEnum.NO) {
            noExcelSheet();
        }
        // 暂时不考虑会存在多个sheet，默认获取第一个sheetName
        //String sheetName = sheetNames.get(0);
        //importType = ChargeFileRecordBusinessTypeEnum.getCode(sheetName);
    }

    public ExcelSheet(List<String> sheetNames, Integer headSkipRow, ExcelSheetEnum excelSheetEnum, Integer importType) {
        this(sheetNames, headSkipRow, excelSheetEnum);
        this.importType = importType;
    }

    public boolean isErrMsgIsHead() {
        return errMsgIsHead;
    }

    public void setErrMsgIsHead(boolean errMsgIsHead) {
        this.errMsgIsHead = errMsgIsHead;
    }

    /**
     * validChoiceInfo调用的时候需要获取excelSheet对象
     * remote调用的时候获取excelSheet
     */
    public void allExcelSheet() {
        this.currentExcelSheetGet = true;
        this.remoteExcelSheetGet = true;
        commonExcelSheet();
    }

    /**
     * validChoiceInfo调用的时候不获取excelSheet对象
     * remote调用的时候不获取excelSheet
     */
    public void noExcelSheet() {
        this.currentExcelSheetGet = false;
        this.remoteExcelSheetGet = false;
        commonExcelSheet();
    }

    /**
     * validChoiceInfo调用的时候需要获取excelSheet对象
     * remote调用的时候获取不到到excelSheet
     */
    public void validChoiceInfoGetExcelSheet() {
        this.currentExcelSheetGet = true;
        this.remoteExcelSheetGet = false;
        commonExcelSheet();
    }

    /**
     * validChoiceInfo调用的时候获取不到excelSheet对象
     * remote调用的时候可以获取到excelSheet
     */
    public void remoteGetExcelSheet() {
        this.currentExcelSheetGet = false;
        this.remoteExcelSheetGet = true;
        commonExcelSheet();
    }

    public void commonExcelSheet() {
        this.tenantId = getTenantId().get();
        this.userId = getUserId().get();
        this.userName = getUserName().get();
        this.setRecordSuccessData(false);
        this.setReturnFailData(false);
        this.convertDataExceptionSave = false;
    }
}
