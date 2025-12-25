package com.wishare.contract.apis.revision.attachment;

import com.wishare.contract.apps.fo.revision.attachment.*;
import com.wishare.contract.apps.service.revision.attachment.AttachmentAppService;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentListV;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 关联附件管理表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"关联附件管理表"})
@RequestMapping("/attachment")
public class AttachmentApi {

    private final AttachmentAppService attachmentAppService;

    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = AttachmentV.class)
    @GetMapping
    public AttachmentV get(@Validated AttachmentF attachmentF) {
        return attachmentAppService.get(attachmentF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = AttachmentV.class)
    @PostMapping("/list")
    public AttachmentListV list(@Validated @RequestBody AttachmentListF attachmentListF) {
        return attachmentAppService.list(attachmentListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody AttachmentSaveF attachmentF) {
        return attachmentAppService.save(attachmentF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody AttachmentUpdateF attachmentF) {
        attachmentAppService.update(attachmentF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id) {
        return attachmentAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = AttachmentV.class)
    @PostMapping("/page")
    public PageV<AttachmentV> page(@RequestBody PageF<AttachmentPageF> request) {
        return attachmentAppService.page(request);
    }

    @ApiOperation(value = "合同详情页查询附件数据列表", response = AttachmentV.class)
    @PostMapping("/getListForContract")
    public List<AttachmentV> pageForContract(@RequestBody AttachmentPageF request) {
        return attachmentAppService.pageForContract(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = AttachmentV.class)
    @PostMapping("/pageFront")
    public PageV<AttachmentV> frontPage(@RequestBody PageF<SearchF<AttachmentE>> request) {
        return attachmentAppService.frontPage(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用-全量合同(传父合同)", response = AttachmentV.class)
    @PostMapping("/allContract/pageFront")
    public PageV<AttachmentV> frontPageForAllContract(@RequestBody PageF<SearchF<AttachmentE>> request) {
        return attachmentAppService.frontPageForAllContract(request);
    }

    /**
     * 影像系统附件上传接口
     *
     * @param file 附件
     * @param id   合同ID
     * @return 是否上传成功
     */
    @ApiOperation(value = "影像系统附件上传接口", notes = "影像系统附件上传接口")
    @PostMapping("/uploadAttach")
    public Boolean uploadAttachFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        return attachmentAppService.uploadAttachFile(file, id);
    }

    @ApiOperation(value = "泛微校验是否提交")
    @GetMapping("/checkIsSubmit")
    public Integer checkIsSubmit(@RequestParam("businessId") String businessId) {
        return attachmentAppService.checkIsSubmit(businessId);
    }
}
