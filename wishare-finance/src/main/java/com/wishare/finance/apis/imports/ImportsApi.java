package com.wishare.finance.apis.imports;

import com.wishare.component.imports.ImportStandardV;
import com.wishare.finance.apps.enums.imports.ImportTaskEnum;
import com.wishare.finance.apps.service.imports.ImportService;
import com.wishare.starter.interfaces.ApiBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author luzhonghe
 */
@Api(tags = {"导入api"})
@RestController
@RequestMapping("/imports")
@RequiredArgsConstructor
@Validated
public class ImportsApi implements ApiBase {

    private final ImportService importService;


    @ApiOperation(value = "导入excel", notes = "导入excel")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public ImportStandardV importTask(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        return importService.importTask(file, ImportTaskEnum.valueOfByCode(type));
    }
}
