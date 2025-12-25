package com.wishare.contract.apps.service.revision.attachment;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Lists;
import com.wishare.contract.apps.fo.revision.attachment.*;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.fo.image.ImageFileF;
import com.wishare.contract.apps.remote.fo.image.ImageParams;
import com.wishare.contract.apps.remote.fo.image.SafetyF;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.apps.remote.vo.imagefile.ImageFileV;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentListV;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 关联附件管理表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@Service
@Slf4j
public class AttachmentAppService {

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private TransactionTemplate transactionTemplate;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentMapper attachmentMapper;

    @Setter(onMethod_ = {@Autowired})
    private FileStorage fileStorage;

    public AttachmentV get(AttachmentF attachmentF) {
        return attachmentService.get(attachmentF).orElse(null);
    }

    public AttachmentListV list(AttachmentListF attachmentListF) {
        return attachmentService.list(attachmentListF);
    }

    public String save(AttachmentSaveF attachmentF) {
        return attachmentService.save(attachmentF);
    }

    public String saveInfo(FileVo fileVo, String fileId) {
        return attachmentService.saveInfo(fileVo, fileId);
    }

    public String saveInfoSmj(FileVo fileVo, String fileId, String contractId) {
        return attachmentService.saveInfoSmj(fileVo, fileId, contractId);
    }

    public String saveInfoSmjRepalce(FileVo fileVo, String fileId, String contractId) {
        return attachmentService.saveInfoSmjRepalce(fileVo, fileId, contractId);
    }

    public void update(AttachmentUpdateF attachmentF) {
        attachmentService.update(attachmentF);
    }

    public boolean removeById(String id) {
        return attachmentService.removeById(id);
    }

    public PageV<AttachmentV> page(PageF<AttachmentPageF> request) {
        return attachmentService.page(request);
    }

    public List<AttachmentV> pageForContract(AttachmentPageF request) {
        return attachmentService.pageForContract(request);
    }

    public PageV<AttachmentV> frontPage(PageF<SearchF<AttachmentE>> request) {
        return attachmentService.frontPage(request);
    }

    public PageV<AttachmentV> frontPageForAllContract(PageF<SearchF<AttachmentE>> request) {
        return attachmentService.frontPageForAllContract(request);
    }

    /**
     * 上传附件服务
     * <p>
     * 使用Base64编码上传文件的一些原因：<br>
     * 1、通用性：Base64编码的结果是ASCII字符串，这是一种通用的字符集，可以在几乎所有系统中被解析。<br>
     * 2、兼容性：HTTP协议是文本协议，一些非ASCII字符在传输过程中可能出现问题。Base64编码将二进制数据转换成纯文本，避免了这个问题。<br>
     * 3、数据封装：有时候，我们可能需要将文件和其他数据（如文本、JSON等）一起发送。Base64编码可以让我们把文件编码成字符串，并和其他数据一起发送。<p>
     * 然而，使用Base64编码也有缺点：<br>
     * 1、体积增大：Base64编码会使数据体积增大约33%，因为每3个字节的数据需要用4个字节的Base64字符来表示。<br>
     * 2、CPU使用率：编码和解码Base64需要CPU资源，大量的Base64编码/解码可能会影响性能。<p>
     * <a href="https://juejin.cn/post/7251131990438264889">参考</a>
     *
     * @param file 附件 文档中并没有要求文件类型校验、文件大小限制、文件内容安全性校验、病毒扫描，暂不做
     * @param id   合同id
     * @return 是否上传成功
     */
    public Boolean uploadAttachFile(MultipartFile file, String id) {
        checkFile(file);
        LambdaQueryWrapperX<AttachmentE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(AttachmentE::getId, id).eq(AttachmentE::getDeleted, 0);
        AttachmentE attachmentE = attachmentMapper.selectOne(queryWrapper);
        // 根据合同ID查询,对应的数据必然存在,不然前端页面就已经出错了
        if (ObjectUtil.isNull(attachmentE)) {
            throw new OwlBizException("需上传的附件没有对应的合同ID,无法绑定对应的合同");
        }

        LocalDateTime time = LocalDateTime.now();
        // FIXME 暂时没有
        String clientcode = "应该是一个固定值,待影像系统和我们商务洽谈 --2023年8月14日";
        String barcode = "";
        String useraccount = attachmentE.getOperator();
        String ticket = SecureUtil.md5(time + clientcode + barcode + useraccount);

        // 构造数据
        SafetyF safetyF = Builder.of(SafetyF::new)
                .with(SafetyF::setTime, System.currentTimeMillis()+"") // *创建时间
                .with(SafetyF::setTicket, ticket) // *安全校验位
                .with(SafetyF::setClientcode, clientcode) // *分配给业务系统的账号
                .with(SafetyF::setBarcode, barcode) // 操作单号
                .with(SafetyF::setUseraccount, useraccount) // 当前操作用户
                .build();
        ImageParams imageParams;
        try {
            imageParams = Builder.of(ImageParams::new)
                    .with(ImageParams::setOperatoruser, attachmentE.getOperator()) // *操作人帐号
                    .with(ImageParams::setOperatorname, attachmentE.getOperatorName()) // *操作人名称
                    .with(ImageParams::setFilecontent, Base64.encode(file.getBytes())) // *文件base64内容
                    .with(ImageParams::setFilename, file.getOriginalFilename()) // *文件名称，带后缀
                    .with(ImageParams::setIsocr, 0) // 是否需要OCR:不需要
//                    .with(ImageParams::setInvoiceinfo, Lists.newArrayList()) // 发票信息列表数组
                    .build();
        } catch (IOException ex) {
            log.error("获取文件二进制数据出错,请重试");
            throw new OwlBizException("获取文件二进制数据出错,请重试");
        }
        ImageFileF imageFileF = Builder.of(ImageFileF::new)
                .with(ImageFileF::setSafety, safetyF)
                .with(ImageFileF::setParams, imageParams)
                .build();
        String fileuuid = externalFeignClient.zjUpload(imageFileF);


        // 将 fileuuid 入 [Attachment] 库
        attachmentE.setFileuuid(fileuuid);

        transactionTemplate.execute(status -> {
            try {
                attachmentMapper.updateById(attachmentE);
                return Boolean.TRUE;
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.error("返回文件唯一ID数据更新失败,已回滚,原因：{}", ex.getMessage());
                throw new OwlBizException("返回文件唯一ID数据更新失败,已回滚");
            }
        });
        // 走不到这里,要么正常返回,要么抛异常
        return Boolean.FALSE;
    }

    /**
     * 防止文件类型欺骗：恶意用户可能会尝试更改文件扩展名或MIME类型来欺骗系统，以允许上传危险的文件。通过校验文件内容和扩展名等方式，可以避免这种情况的发生。
     * 防止文件大小超限：限制文件大小是非常重要的，以防止用户上传过大的文件影响服务器性能或导致拒绝服务攻击。
     * 防止文件包含恶意代码：检查上传的文件是否存在恶意代码（例如病毒、木马等），以防止恶意文件传播和安全风险。
     * 防止文件参数篡改：对文件参数（名称、路径、大小等）进行校验以确保上传的文件参数与预期的相同，防止黑客使用文件上传功能来修改参数，攻击系统。
     */
    private void checkFile(MultipartFile file) {
        // TODO 文件类型校验、文件大小限制、文件内容安全性校验、病毒扫描，暂不做
    }

    public Integer checkIsSubmit(String id) {
        log.info("查询合同checkIsSubmit:{}", id);
        Integer isFlag = 0;
        List<AttachmentE> attachmentEList = attachmentService.listById(id);
        ContractPayConcludeE payConcludeE = contractPayConcludeService.getById(id);
        ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeService.getById(id);

        if(!ObjectUtils.isNotEmpty(attachmentEList)){
            return isFlag;
        }

        if(ObjectUtils.isNotEmpty(payConcludeE) && ObjectUtils.isNotEmpty(payConcludeE.getSignDate())){
            isFlag = 1;
        }else if(ObjectUtils.isNotEmpty(incomeConcludeE) && ObjectUtils.isNotEmpty(incomeConcludeE.getSignDate())){
            isFlag = 1;
        }
        log.info("合同{}checkIsSubmit结果:{}", id, isFlag);
        return isFlag;
    }

    /**
     * 删除合同扫描件
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSmj(String id) {
        AttachmentF attachmentF = new AttachmentF();
        attachmentF.setId(id);
        AttachmentV attachmentV = this.get(attachmentF);
        if (ObjectUtils.isEmpty(attachmentV)) {
            return true;
        }
        this.removeById(id);
        return fileStorage.delete(attachmentV.getFileKey());
    }
}
