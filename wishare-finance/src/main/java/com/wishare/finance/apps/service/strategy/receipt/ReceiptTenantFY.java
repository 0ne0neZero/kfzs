package com.wishare.finance.apps.service.strategy.receipt;


import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.ESignF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.service.acl.AclSignService;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.infrastructure.configs.EsignConfigProperties;
import com.wishare.finance.infrastructure.pdf.PDFConst;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.Global;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @see com.wishare.finance.infrastructure.conts.EnvConstEnum
 */
@Component
@Lazy
public class ReceiptTenantFY extends AbReceiptTenant {


    @Setter(onMethod_ = @Autowired)
    private EsignConfigProperties esignConfigProperties;

    /**
     * 获取模板路劲
     *
     * @param templateE
     * @return
     */
    @Override
    public String getReceiptTemplatePath(ReceiptTemplateE templateE) {
        final String receiptTemplatePath = super.getReceiptTemplatePath(templateE);
        //样式一为标准默认模板 方圆默认模板为样式二 如果拿到的模板是样式一则进行转换
        return receiptTemplatePath.equals(ReceiptTemplateStyleEnum.样式一.getTemplatePath()) ?
                ReceiptTemplateStyleEnum.样式二.getTemplatePath() : receiptTemplatePath;
    }


    /**
     * @param receiptTemplateE
     * @param signStatus       是否需要签章：0 - 是 1 - 否
     */
    @Override
    public void signOnPdf(ReceiptTemplateE receiptTemplateE, Integer signStatus,
            Map<String, Object> map) {
        //存在 存在模板&&需要盖章&&电子收据
        if (Objects.nonNull(receiptTemplateE) && ObjectUtils.isNotEmpty(signStatus) && signStatus.equals(0) && receiptTemplateE.getTemplateType() == 6) {
            //启用电子签章：0:不启用;1:启用;(关闭内部章，用外部章比如 e签宝)
            receiptTemplateE.setEnableElectSign(0);
        }
    }

    @Override
    public void pdfLogo(String path, HashMap<String, Object> templateData) {
        templateData.put("fangyuanLogo1", path + PDFConst.FANGYUAN_LOGO_PATH_01);
        templateData.put("fangyuanLogo2", path + PDFConst.FANGYUAN_LOGO_PATH_02);
    }

    /**
     * 如果开启e签宝签章 则不进行后续信息下发客户，定时任务处理
     * 对收据表的状态以及签章批次号做更新或者记录
     *
     * @param vo
     * @return
     */
    @Override
    public boolean signExternalSeal(SignExternalSealVo vo) {
        Integer signStatus = vo.getSignStatus();
        if (!(ObjectUtils.isNotEmpty(signStatus) && signStatus == 0)) {
            return true;
        }
        //开启签章,进行签章（定时任务跑获取签章状态信息）获取到签章之后再走推送逻辑
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(vo.getReceiptE().getId());
        //对于部分企业想用的章为配置上的章
        //获取签署请求编号
        ESignF build = ESignF.builder()
                .fileVo(vo.getFileVo())
                .orgIDCardNum(esignConfigProperties.getOrgFinanceId())
                .orgIDCardType("CRED_ORG_USCC")
                .sealType("COMMON")
                .keyword("熊仔王盖章点")
                .build();
        if (esignConfigProperties.getOrgFinanceId().equals(vo.getInvoiceReceiptE().getStatutoryBodyId().toString())) {
            //如果传入的纳税人识别号是默认的，则不采用外部授权印章
//            build.setKeyword(vo.getInvoiceReceiptE().getStatutoryBodyName());
        } else {
            //如果传入的纳税人识别号是默认的，则采用外部授权印章
//            build.setKeyword(esignConfigProperties.getDefaultOrgFinanceName());
            build.setAuthOrgIDCardNum(vo.getInvoiceReceiptE().getStatutoryBodyId().toString());
            build.setAuthOrgName(vo.getInvoiceReceiptE().getStatutoryBodyName());
        }
        //发起签署
        String stamp = Global.ac.getBean(AclSignService.class).stamp(build, vo.getInvoiceReceiptE().getId());
        //签署结果
        receiptE.setSignSealStatus(StringUtils.isEmpty(stamp)?8:1);
        receiptE.setSignApplyNo(stamp);
        //更新数据
        Global.ac.getBean(ReceiptRepository.class).updateById(receiptE);
        //不需要发短信等后续处理,定时任务处理
        return false;
    }

    /**
     * @param receiptVDto 数据对象集
     * @return
     */
    @Override
    public int receiptSend(ReceiptVDto receiptVDto) {
        //是否需要签章：0 - 是 1 - 否
        final Integer signStatus = receiptVDto.getSignStatus();
        //e签宝
        if (ObjectUtils.isNotEmpty(signStatus) && signStatus.equals(0)) {
            return 0;
        }
        return super.receiptSend(receiptVDto);
    }

    /**
     * 获取签署结果
     */
    @Override
    public EsignResult signResult(String signFlowId) {
        EsignResult esignResult = Global.ac.getBean(AclSignService.class).querySignResultFY(signFlowId);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(esignResult), "查询签署结果异常");
        return esignResult;
    }

    /**
     * 处理签署结果
     * @param receiptEId
     * @param esignResult
     * @return
     */
    @Override
    public ReceiptE signResultAfter(Long receiptEId, EsignResult esignResult) {
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(receiptEId);
        receiptE1.setSignReceiptUrl(esignResult.getReusltUrl());
        receiptE1.setSignFileVos(esignResult.getFileVos());
        receiptE1.setSignSealStatus(Integer.valueOf(esignResult.getStatus()));
        return receiptE1;
    }
}
