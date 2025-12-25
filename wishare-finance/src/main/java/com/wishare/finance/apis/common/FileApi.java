package com.wishare.finance.apis.common;

import com.wishare.finance.apps.model.common.DeleteFileF;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


/**
 * @author yancao
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"上传接口"})
public class FileApi implements ApiBase {

    private final FileStorage fileStorage;

    @ApiOperation(value = "上传接口", notes = "上传接口", response = FileVo.class)
    @PostMapping("/upload")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "__File", allowMultiple = true, paramType = "query",
            dataTypeClass = MultipartFile.class, required = true)
    public FileVo uploadIcon(@RequestParam("file") MultipartFile file) {
        return fileStorage.tmpSave(file, getTenantId().get());
    }

    @ApiOperation(value = "根据fileKey删除正式文件", notes = "根据fileKey删除正式文件")
    @PostMapping("/delete/formal")
    public boolean deleteFormal(@RequestBody DeleteFileF deleteFileF) {
        return fileStorage.delete(deleteFileF.getFileKey());
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

}
