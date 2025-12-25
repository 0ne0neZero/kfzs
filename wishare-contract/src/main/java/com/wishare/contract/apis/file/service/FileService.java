package com.wishare.contract.apis.file.service;

import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.fo.image.ImageFileF;
import com.wishare.contract.apps.remote.fo.image.ImageParams;
import com.wishare.contract.apps.remote.fo.image.SafetyF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@Slf4j
public class FileService {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ExternalFeignClient externalFeignClient;
    public String getZjFileId(MultipartFile file, boolean isOcr) {
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
        //return "ee93ce7ce8074a8a99365fad033800b7";
    }
}
