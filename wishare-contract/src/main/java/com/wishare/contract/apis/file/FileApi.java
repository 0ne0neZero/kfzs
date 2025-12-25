package com.wishare.contract.apis.file;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.fo.file.DeleteFileF;
import com.wishare.contract.apps.fo.revision.attachment.AttachmentF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceDetailF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.fo.InvoiceBaseInfoF;
import com.wishare.contract.apps.remote.fo.image.*;
import com.wishare.contract.apps.remote.vo.EsbRetrunInfoV;
import com.wishare.contract.apps.remote.vo.imagefile.ImageFileDownV;
import com.wishare.contract.apps.remote.vo.imagefile.ImageFileV;
import com.wishare.contract.apps.service.revision.attachment.AttachmentAppService;
import com.wishare.contract.domains.enums.revision.FileCheckResultEnum;
import com.wishare.contract.domains.enums.revision.VerifyStatusEnum;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangrui
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"上传接口"})
public class FileApi implements ApiBase {

    private final FileStorage fileStorage;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ExternalFeignClient externalFeignClient;

    private final AttachmentAppService attachmentAppService;

    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    @ApiOperation(value = "上传接口", notes = "上传接口", response = FileVo.class)
    @PostMapping("/upload")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public FileVo uploadIcon(@RequestParam("file") MultipartFile file) {
        return fileStorage.tmpSave(file, getTenantId().get());
    }

    @ApiOperation(value = "上传接口并上传中交影像", notes = "上传接口并上传中交影像", response = FileVo.class)
    @PostMapping("/zjUpload")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public ZJFileVo zjUpload(@RequestParam("file") MultipartFile file, @RequestParam(value = "billNo", required = false) String billNo) {
        ZJFileVo zjFileVo = new ZJFileVo();
        FileVo fileVo = fileStorage.tmpSave(file, getTenantId().get());
        this.convertFileExtensionToLowerCase(fileVo);
        BeanUtils.copyProperties(fileVo, zjFileVo);
        if(null != fileVo && devFlag != 1) {
            String fileId = getZjFileId(file, false);
//            String fileId = "ee93ce7ce8074a8a99365fad033800b7";

            if(StringUtils.isNotEmpty(fileId)) {
                zjFileVo.setFileId(fileId);
                zjFileVo.setBillNo(billNo);
                attachmentAppService.saveInfo(fileVo, fileId);
            }
        }
        return zjFileVo;
    }

    @ApiOperation(value = "下载中交影像附件", notes = "下载中交影像附件", response = FileVo.class)
    @PostMapping("/downZjFileId")
    public ImageFileDownV downZjFileId(@RequestParam(value = "fileId") String fileId) {
        return downZjFileIdInfo(fileId);
    }

    @ApiOperation(value = "上传接口并上传扫描件到中交影像", notes = "上传接口并上传扫描件到中交影像", response = FileVo.class)
    @PostMapping("/zjUploadSmj")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public ZJFileVo zjUploadSmj(@RequestParam("file") MultipartFile file, @RequestParam(value = "contractId", required = false) String contractId) {
        AttachmentF attachmentF = new AttachmentF();
        attachmentF.setBusinessId(contractId);
        AttachmentV attachmentV = attachmentAppService.get(attachmentF);
        if (ObjectUtils.isNotEmpty(attachmentV)) {
            throw new OwlBizException("只能上传一份合同扫描件");
        }
        ZJFileVo zjFileVo = new ZJFileVo();

        if (devFlag == 1) {
            FileVo fileVo = new FileVo();
            fileVo.setFileKey("前期物业管理服务协议.pdf");
            fileVo.setName("前期物业管理服务协议.pdf");
            fileVo.setType(0);
            fileVo.setSize(0L);
            attachmentAppService.saveInfoSmj(fileVo, LocalDateTime.now().toString(), contractId);
            BeanUtils.copyProperties(fileVo,zjFileVo);
            return zjFileVo;
        }
        FileVo fileVo = fileStorage.tmpSave(file, getTenantId().get());
        this.convertFileExtensionToLowerCase(fileVo);
        if(null != fileVo) {
            String fileId = getZjFileId(file, false);
//            String fileId = "ee93ce7ce8074a8a99365fad033800b7";

            if(StringUtils.isNotEmpty(fileId)) {
                BeanUtils.copyProperties(fileVo,zjFileVo);
                zjFileVo.setFileId(fileId);
                attachmentAppService.saveInfoSmj(fileVo, fileId, contractId);
            }
        }
        return zjFileVo;
    }

    @ApiOperation(value = "替换扫描件到中交影像", notes = "替换扫描件到中交影像", response = FileVo.class)
    @PostMapping("/zjUploadSmjReplace")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public ZJFileVo zjUploadSmjReplace(@RequestParam("file") MultipartFile file, @RequestParam(value = "contractId") String contractId) {
        ZJFileVo zjFileVo = new ZJFileVo();
        FileVo fileVo = fileStorage.tmpSave(file, getTenantId().get());
        this.convertFileExtensionToLowerCase(fileVo);
        if(null != fileVo) {
            String fileId = getZjFileId(file, false);
//            String fileId = "ee93ce7ce8074a8a99365fad033800b7";

            if(StringUtils.isNotEmpty(fileId)) {
                BeanUtils.copyProperties(fileVo,zjFileVo);
                zjFileVo.setFileId(fileId);
                attachmentAppService.saveInfoSmjRepalce(fileVo, fileId, contractId);
            }
        }
        return zjFileVo;
    }

    @ApiOperation(value = "上传接口并上传中交影像进行OCR", notes = "上传接口并上传中交影像进行OCR", response = FileVo.class)
    @PostMapping("/zjUploadOcr")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public ZJFileVo zjUploadOcr(@RequestParam("file") MultipartFile file) {
        ZJFileVo zjFileVo = new ZJFileVo();
        FileVo fileVo = fileStorage.tmpSave(file, getTenantId().get());
        if(null != fileVo) {
            String fileId = getZjFileId(file,true);
//            String fileId = "ee93ce7ce8074a8a99365fad033800b7";

            if(StringUtils.isNotEmpty(fileId)) {
                BeanUtils.copyProperties(fileVo,zjFileVo);
                zjFileVo.setFileId(fileId);
            }
        }
        return zjFileVo;
    }

    @ApiOperation(value = "上传接口并上传中交影像进行OCR")
    @PostMapping("/v2/zjUploadOcr")
    public InvoiceOcrResultV zjUploadOcrV2(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        InvoiceOcrResultV resultV = new InvoiceOcrResultV();
        ZJFileVo zjFileVo = new ZJFileVo();
        FileVo fileVo = fileStorage.tmpSave(file, getTenantId().get());
        if (ObjectUtils.isEmpty(file)) {
            return resultV;
        }
        //记录结算单id日志，获取日志traceId
        log.info("开始发票orc识别,当前处理的结算单id:{}", id);
        ImageFileV imageFileV = getZjFileIdV2(file, true);
        log.info("ocr识别到的原始ImageFileV信息:{}", JSON.toJSONString(imageFileV));
        String fileId = imageFileV.getData().getFileuuid();
        if (StringUtils.isNotEmpty(fileId)) {
            BeanUtils.copyProperties(fileVo, zjFileVo);
            zjFileVo.setFileId(fileId);
        }
        resultV.setFileInfo(zjFileVo);
        if (CollectionUtils.isEmpty(imageFileV.getData().getOcrinfo())) {
            return resultV;
        }
        List<ContractSettlementInvoiceDetailF> ocrInfos = imageFileV.getData().getOcrinfo().stream().map(ocrInfo -> {
            ContractSettlementInvoiceDetailF detailF = new ContractSettlementInvoiceDetailF();
            //购方名称
            detailF.setInname(ocrInfo.getInname());
            //购方识别号
            detailF.setGfsbh(ocrInfo.getGfsbh());
            //销方名称
            detailF.setOutname(ocrInfo.getOutname());
            //销方识别号
            detailF.setXfsbh(ocrInfo.getXfsbh());

            detailF.setInvoiceType(ocrInfo.getInvoicetype());
            detailF.setInvoiceNum(ocrInfo.getInvoicenum());
            detailF.setInvoiceCode(ocrInfo.getInvoicecode());
            detailF.setInvoiceTaxAmount(new BigDecimal(ocrInfo.getTotalamount()).setScale(6, RoundingMode.HALF_UP));
            //获取到不含税金额
            BigDecimal jamount = new BigDecimal(ocrInfo.getJamount()).setScale(6, RoundingMode.HALF_UP);
            //计算税额
            detailF.setTaxAmount(detailF.getInvoiceTaxAmount().subtract(jamount));
            detailF.setDeductionAmount(detailF.getTaxAmount());
            LocalDate invoiceDate = parseInvoicedate(ocrInfo.getInvoicedate(), "yyyy年MM月dd日");
            //如果为空，按照yyyy-MM-dd继续解析
            if (ObjectUtils.isEmpty(invoiceDate)) {
                invoiceDate = parseInvoicedate(ocrInfo.getInvoicedate(), "yyyy-MM-dd");
            }
            //解析不到就是空，能解析到就正常返回
            detailF.setInvoiceDate(invoiceDate);
            if (StringUtils.isEmpty(ocrInfo.getVerifyresult())) {
                detailF.setVerifyStatus(VerifyStatusEnum.PASS.getCode());
            } else {
                Integer res = Integer.valueOf(ocrInfo.getVerifyresult());
                detailF.setVerifyStatus(VerifyStatusEnum.PASS.getCode().equals(res) ? VerifyStatusEnum.PASS.getCode() : VerifyStatusEnum.NOT_PASS.getCode());
            }
            return detailF;
        }).collect(Collectors.toList());
        resultV.setOcrInfos(ocrInfos);
        log.info("ocr接口识别返回的最终数据:{}", JSON.toJSONString(resultV));

        return resultV;
    }

    /**
     * 按照两种格式解析发票开票时间
     *
     * @param invoicedate
     * @param format
     * @return
     */
    private LocalDate parseInvoicedate(String invoicedate, String format) {
        if (StringUtils.isBlank(invoicedate)) {
            return null;
        }
        try {
            return DateUtil.parse(invoicedate, format).toLocalDateTime().toLocalDate();
        } catch (Exception e) {
            log.error("按照{}格式解析开票时间失败", format);
        }
        return null;
    }

    private boolean checkInvoiceInfo(ImageFileV imageFileV){
        if(ObjectUtils.isEmpty(imageFileV.getData().getOcrinfo())){
            throw new OwlBizException("发票验真返回信息为空，请检查！");
        }
        if(imageFileV.getData().getOcrinfo().get(0).getCode().equals("1")){
            Integer s = Integer.parseInt(imageFileV.getData().getOcrinfo().get(0).getCode());
            throw new OwlBizException("发票验真失败，原因:" + FileCheckResultEnum.parseName(s));
        }
        InvoiceBaseInfoF invoiceBaseInfoF = new InvoiceBaseInfoF();
        invoiceBaseInfoF.setInvoicecode(imageFileV.getData().getOcrinfo().get(0).getInvoicecode());
        invoiceBaseInfoF.setInvoicenumber(imageFileV.getData().getOcrinfo().get(0).getInvoicenum());
        EsbRetrunInfoV s = externalFeignClient.checkInvoice(invoiceBaseInfoF);
        if(ObjectUtils.isEmpty(s)){
            throw new OwlBizException("发票验重接口有误，请检查！");
        }
        if(!s.getCode().equals(1)){
            throw new OwlBizException(s.getMessage());
        }
        return false;
    }

    private String getZjFileId(MultipartFile file, boolean isOcr) {
        //上传中交影像 返回唯一id
        ImageFileF imageFileF = new ImageFileF();
        LocalDateTime localDateTime =  LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String str = localDateTime.format(formatter);
        SafetyF safetyF = new SafetyF();
        safetyF.setTime(str);
        safetyF.setUseraccount("zzz");
        safetyF.setBarcode("111");
        imageFileF.setSafety(safetyF);

        ImageParams imageParams = new ImageParams();
        if(isOcr){
            imageParams.setIsocr(1);
            imageParams.setVerifytype("1");
        }
        try {
            imageParams.setOperatoruser("zzz");
            imageParams.setOperatorname("zzz");
            imageParams.setFilecontent(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            log.info("文件转化base64出错" + e);
        }
        imageParams.setFilename(file.getOriginalFilename());
        imageFileF.setParams(imageParams);
        System.out.println("推送中交影像请求===========================" + JSONObject.toJSONString(imageFileF));
        return externalFeignClient.zjUpload(imageFileF);
        //todo 目前写死id返回前端
//        return "ee93ce7ce8074a8a99365fad033800b7";
    }


    private ImageFileV getZjFileIdV2(MultipartFile file, boolean isOcr) {
        //上传中交影像 返回唯一id
        ImageFileF imageFileF = new ImageFileF();
        LocalDateTime localDateTime =  LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String str = localDateTime.format(formatter);
        SafetyF safetyF = new SafetyF();
        safetyF.setTime(str);
        safetyF.setUseraccount("zzz");
        safetyF.setBarcode("111");
        imageFileF.setSafety(safetyF);

        ImageParams imageParams = new ImageParams();
        if(isOcr){
            imageParams.setIsocr(1);
            imageParams.setVerifytype("1");
        }
        try {
            imageParams.setOperatoruser("zzz");
            imageParams.setOperatorname("zzz");
            imageParams.setFilecontent(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            log.info("文件转化base64出错" + e);
        }
        imageParams.setFilename(file.getOriginalFilename());
        imageFileF.setParams(imageParams);
        log.info("推送中交影像请求==========================={}", JSON.toJSONString(imageFileF));
        return externalFeignClient.zjUploadV2(imageFileF);
    }

    private ImageFileDownV downZjFileIdInfo(String fileId) {
        //上传中交影像 返回唯一id
        ImageFileDeleteF imageFileF = new ImageFileDeleteF();
        LocalDateTime localDateTime =  LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String str = localDateTime.format(formatter);
        SafetyF safetyF = new SafetyF();
        safetyF.setTime(str);
        safetyF.setUseraccount("zzz");
        safetyF.setBarcode("111");
        imageFileF.setSafety(safetyF);

        ImageFileDeleteParamF imageFileDeleteParamF = new ImageFileDeleteParamF();
        imageFileDeleteParamF.setOperatoruser("zzz");
        imageFileDeleteParamF.setOperatorname("zzz");
        imageFileDeleteParamF.setFileuuid(fileId);

        imageFileF.setParams(imageFileDeleteParamF);
        log.info("下载中交影像请求===========================" + JSONObject.toJSONString(imageFileF));
        return externalFeignClient.downImageFile(imageFileF);
        //todo 目前写死id返回前端
//        return "ee93ce7ce8074a8a99365fad033800b7";
    }


    @ApiOperation(value = "根据fileKey删除正式文件", notes = "根据fileKey删除正式文件")
    @PostMapping("/delete/formal")
    public boolean deleteFormal(@RequestBody DeleteFileF deleteFileF) {
        return fileStorage.delete(deleteFileF.getFileKey());
    }

    @ApiOperation(value = "删除合同扫描件", notes = "删除合同扫描件")
    @PostMapping("/delete/smj")
    public boolean deleteSmj(@RequestParam("id") String id) {
        return attachmentAppService.deleteSmj(id);
    }

    @ApiOperation(value = "单文件上传正式文件", notes = "上传正式文件")
    @PostMapping("/upload/formalSave")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public FileVo formalSave(@RequestParam("file") MultipartFile file) {
        return fileStorage.formalSave(file, FormalF.builder().tenantId(curIdentityInfo().getTenantId())
                .serverName(this.getClass().getSimpleName())
                .clazz(this.getClass())
                .businessId(UUID.randomUUID().toString()).build());
    }

    /**
     * 将FileVo中的文件后缀修改为小写
     *
     * @param fileVo
     */
    private void convertFileExtensionToLowerCase(FileVo fileVo) {
        if (ObjectUtils.isEmpty(fileVo)) {
            return;
        }
        String fileName = fileVo.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return;
        }
        String fileBase = fileName.substring(0, dotIndex);
        String fileExtension = fileName.substring(dotIndex + 1);
        String lowerCaseExtension = fileExtension.toLowerCase();
        fileVo.setName(fileBase + "." + lowerCaseExtension);
    }



}
