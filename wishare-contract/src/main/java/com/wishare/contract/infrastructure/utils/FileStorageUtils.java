package com.wishare.contract.infrastructure.utils;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.starter.consts.Const;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.utils.FileStorageUtil;
import com.wishare.tools.starter.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 *
 * @author: PengAn
 * @create: 2022-07-13
 * @description:
 **/
@Component
@Slf4j
public class FileStorageUtils {

    private final FileStorage fileStorage;

    /**
     * 逗号,用于分隔图片
     */
    private static final String COMMA = ",";

    public FileStorageUtils(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    /**
     * 处理文件上传对象，返回fileKey
     *
     * @param oldFileKey 旧的fileKey，保存在数据的url
     * @param fileVo     前端提交过来的fileVo对象
     * @param tenantId
     * @param businessId
     * @param clazz
     * @return 删除图片返回空字符串，更新图片返回新的fileKey，没有更新图片返回旧的fileKey
     */
    public String handleFileOV(String oldFileKey, FileVo fileVo, String tenantId, String businessId, Class<?> clazz) {
        if (ObjectUtils.isEmpty(fileVo)) {
            if (StringUtils.isNotBlank(oldFileKey)) {
                fileStorage.delete(oldFileKey);
            }
            return StringUtils.EMPTY;
        }
        if (fileVo.getType() == Const.State._1) {
            return oldFileKey;
        }
        //先删除旧的
        if (StringUtils.isNotBlank(oldFileKey)) {
            fileStorage.delete(oldFileKey);
        }
        //保存正式的
        FileVo submit = null;
        // 因有时上传临时文件后取不到地址, 故先抓取报错 继续下面逻辑
        try {
            submit = fileStorage.submit(fileVo, FormalF.builder().tenantId(tenantId)
                        .businessId(businessId + Const.POINT + UUID.randomUUID()).clazz(clazz).serverName(ContractSetConst.serviceName).build());
        } catch (Exception e) {
            log.error("临时文件转正式文件失败, 临时文件数据: {}, 错误: {}", JSON.toJSONString(fileVo), e);
        }
        return Objects.nonNull(submit) ? submit.getFileKey() : null;
    }

    /**
     * 新增上传文件
     *
     * @param url 文件对象
     */
    public String submitFile(FileVo url, Class<?> clazz, String tenantId) {
        return fileStorage.submit(url, FormalF.builder().tenantId(tenantId).businessId(UidHelper.nextIdStr(ContractSetConst.CONTRACT)).
                clazz(clazz).serverName(ContractSetConst.CONTRACT).build()).getFileKey();
    }

    /**
     * 批量上传文件
     *
     * @param url 文件对象
     */
    public String batchSubmitFile(List<FileVo> fileVoList, String url,Long id, String tenantId) {
        if (fileVoList == null || fileVoList.size() == 0)
            return null;
        List<FormalF> formalFs = generateFormalFs(fileVoList, id, tenantId);
        List<String> fileKeys= Collections.emptyList();
        if(org.springframework.util.StringUtils.hasText(url)){
            fileKeys = Arrays.asList(url.split(COMMA));
        }
        List<String> certificatesList = fileStorage.compareThenSubmitOrDelete(fileVoList,fileKeys, formalFs);
        return String.join(COMMA, certificatesList);
    }

    /**
     * 批量上传文件名称
     *
     */
    public String batchSubmitName(List<FileVo> fileVoList) {
        if (fileVoList == null || fileVoList.size() == 0)
            return null;
        List<String> certificatesList = new ArrayList<>();
        for (FileVo fileVo : fileVoList) {
            certificatesList.add(fileVo.getName());
        }
        return String.join(COMMA, certificatesList);
    }

    /**
     * 生成FormalF列表方法
     */
    private List<FormalF> generateFormalFs(List<FileVo> fileVoList, Long id, String tenantId) {
        int l;
        if (fileVoList == null || (l = fileVoList.size()) == 0)
            return Collections.emptyList();
        List<FormalF> formalFs = new ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            FormalF formalF = FormalF.builder()
                    .serverName(ContractSetConst.CONTRACT)
                    .clazz(ContractConcludeE.class)
                    .businessId(String.valueOf(id + i))
                    .tenantId(tenantId)
                    .build();
            formalFs.add(formalF);
        }
        return formalFs;
    }

    /**
     * 查询文件
     *
     * @param url 文件对象
     */
    public FileVo getFile(String url) {
        if(!org.springframework.util.StringUtils.hasText(url)){
            return null;
        }
        return FileStorageUtil.createFormalFileVo(url);
    }

    /**
     * 查询批量文件
     *
     * @param url 文件对象
     */
    public List<FileVo> getFileList(String url) {
        if(!org.springframework.util.StringUtils.hasText(url)){
            return null;
        }
        List<FileVo> formalFileVo = Collections.emptyList();
        List<String> fileKeys = Arrays.asList(url.split(COMMA));
        formalFileVo = FileStorageUtil.createFormalFileVo(fileKeys);
        return formalFileVo;
    }

    public static FileVo createFormalFileVo(String fileKey,String fileName) {
        return new FileVo.Builder()
                .setFileKey(fileKey)
                .setName(fileName)
                .setType(1)
                .build();
    }

    /**
     * 查询批量文件+名称
     *
     * @param url 文件对象
     */
    public List<FileVo> getFileNameList(String url,String name) {
        if(!org.springframework.util.StringUtils.hasText(url)){
            return null;
        }
        List<FileVo> formalFileVo = Collections.emptyList();
        List<String> fileKeys = Arrays.asList(url.split(COMMA));
        List<String> fileNames = Arrays.asList(name.split(COMMA));
        formalFileVo = FileStorageUtil.createFormalFileVo(fileKeys);
//        for (FileVo fileVo : formalFileVo) {
//            int i1;
//            for (i1 = 0; i1 < formalFileVo.size(); i1++) {
//                if(null != fileNames.get(0)){
//                    fileVo.setName(fileNames.get(i1));
//                    break;
//                }
//            }
//        }
        for (int i = 0; i < formalFileVo.size(); i++) {
            if (StringUtils.isNotBlank(fileNames.get(i))) {
                formalFileVo.get(i).setName(fileNames.get(i));
            }
        }
        return formalFileVo;
    }

    /**
     * 删除文件
     *
     * @param url 文件对象
     */
    public void delete(String url) {
        if(org.springframework.util.StringUtils.hasText(url)){
            fileStorage.delete(url);
        }
    }

    /**
     * 批量删除文件
     *
     * @param url 文件对象
     */
    public void batchDelete(String url) {
        if(org.springframework.util.StringUtils.hasText(url)){
            List<String> fileKeys = Arrays.asList(url.split(COMMA));
            fileStorage.batchDelete(fileKeys);
        }
    }
}
