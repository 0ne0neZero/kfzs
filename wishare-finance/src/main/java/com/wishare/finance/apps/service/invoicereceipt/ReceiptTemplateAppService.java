package com.wishare.finance.apps.service.invoicereceipt;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.receipttemplate.fo.TemplateF;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateStyleV;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateTypeV;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptTemplateDomainService;
import com.wishare.finance.domains.report.enums.InvoiceLineEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: Linitly
 * @date: 2023/8/7 19:54
 * @descrption:
 */
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiptTemplateAppService {

    private final ReceiptTemplateDomainService receiptTemplateDomainService;

    @Value("${wishare.file.host:}")
    private String fileHost;
    @Value("#{${receipt.demo-file-key:{}}}")
    private Map<String, String> receiptDemoFileKey;


    /**
     * 票据模板类型
     * @return
     */
    public List<TemplateTypeV> typeSelect() {
        return Stream.of(InvoiceLineEnum.电子收据).map(e -> new TemplateTypeV(
                e.getCode(), e.getDes()
        )).collect(Collectors.toList());
    }

    /**
     * 票据模板样式
     * @return
     */
    public List<TemplateStyleV> styleSelect() {
        // 中交的只展示中交定制模板
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            return Stream.of(ReceiptTemplateStyleEnum.中交电子收据模板样式).map(e -> new TemplateStyleV(
                    e.getCode(), e.getDesc()
            )).collect(Collectors.toList());
        }
        // 方圆的展示三个定制模板以及一个默认模板
        if (EnvConst.FANGYUAN.equals(EnvData.config)) {
            return Stream.of(
                    ReceiptTemplateStyleEnum.样式二,
                    ReceiptTemplateStyleEnum.方圆电子收据模板样式01
//                    ReceiptTemplateStyleEnum.方圆电子收据模板样式02,
//                    ReceiptTemplateStyleEnum.方圆电子收据模板样式03
            ).map(e -> new TemplateStyleV(
                    e.getCode(), "样式一".equals(e.getDesc()) ? "默认电子收据" : e.getDesc()
            )).collect(Collectors.toList());
        }
        //拈花湾
        if(TenantUtil.bf64()){
            return Stream.of(ReceiptTemplateStyleEnum.样式三).map(e -> new TemplateStyleV(
                    e.getCode(), e.getDesc()
            )).collect(Collectors.toList());
        }
        // 其他环境暂时只用原样式一
        return Stream.of(
                ReceiptTemplateStyleEnum.样式一,
                ReceiptTemplateStyleEnum.样式四).map(e -> new TemplateStyleV(
                e.getCode(), e.getDesc()
        )).collect(Collectors.toList());
    }

    /**
     * 添加票据模板
     * @param templateF
     * @return
     */
    public Long add(TemplateF templateF) {
//        this.fullSignPictureUrl(templateF);
        ReceiptTemplateE templateE = Global.mapperFacade.map(templateF, ReceiptTemplateE.class);
        return receiptTemplateDomainService.add(templateE);
    }

    /**
     * 更新票据模板
     * @param templateF
     * @return
     */
    public Boolean update(TemplateF templateF) {
//        this.fullSignPictureUrl(templateF);
        ReceiptTemplateE templateE = Global.mapperFacade.map(templateF, ReceiptTemplateE.class);
        return receiptTemplateDomainService.update(templateE);
    }

    public PageV<TemplateV> pageList(PageF<SearchF<?>> form) {
        Page<ReceiptTemplateE> receiptTemplateEPage = receiptTemplateDomainService.pageList(form);
        return RepositoryUtil.convertPage(receiptTemplateEPage, TemplateV.class);
    }

    public TemplateV get(Long id) {
        ReceiptTemplateE receiptTemplateE = receiptTemplateDomainService.get(id);
        return Global.mapperFacade.map(receiptTemplateE, TemplateV.class);
    }

    public List<TemplateV> list() {
        List<ReceiptTemplateE> list = receiptTemplateDomainService.list();
        return Global.mapperFacade.mapAsList(list, TemplateV.class);
    }

    public boolean delete(Long id) {
        return receiptTemplateDomainService.delete(id);
    }

    public String demoUrl(String code) {
        String fileKey = Objects.isNull(receiptDemoFileKey) ? "" : receiptDemoFileKey.get(code);
        return fileHost + fileKey;
    }

//    private void fullSignPictureUrl(TemplateF templateF) {
//        if (StringUtils.isBlank(templateF.getSignPictureUrl())) {
//            return;
//        }
//        templateF.setSignPictureUrl(fileHost + templateF.getSignPictureUrl());
//    }
}
