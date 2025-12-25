package com.wishare.finance.domains.invoicereceipt.support.nuonuo;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.*;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.*;
import com.wishare.finance.domains.invoicereceipt.dto.NuonuoConfigDTO;
import com.wishare.finance.domains.invoicereceipt.entity.nuonuo.NuonuoTokenE;
import com.wishare.finance.domains.invoicereceipt.repository.ExternalConfigRepository;
import com.wishare.finance.domains.invoicereceipt.repository.NuonuoTokenRepository;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.*;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nuonuo.open.sdk.NNOpenSDK;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wishare.finance.domains.invoicereceipt.consts.NuonuoConsts.*;


/**
 * @author xujian
 * @date 2022/8/8
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RefreshScope
public class NuonuoSupport implements ApiBase {

    private final ExternalConfigRepository externalConfigRepository;

    private final NuonuoTokenRepository nuonuoTokenRepository;

    /**
     * 诺诺请求地址
     */

    @Value("${nuonuo.openUrl:demo}")
    private String openUrl;


    /**
     * 请求开具发票接口(2.0)
     *
     * @param form
     * @return
     */
    public BillingNewV billingNew(BillingNewF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), REQUESTBILLINGNEW_API, content, form.getTaxnum());
        return JSON.parseObject(result, BillingNewV.class);
    }



    /**
     * 获取批量打印编号
     *
     * @param form
     * @return
     */
    public InvoicePrintBatchV invoicePrintBatch(InvoicePrintBatchF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), INVOICE_PRINT_BATCH_API, content, form.getTaxnum());
        return JSON.parseObject(result, InvoicePrintBatchV.class);
    }


    /**
     * 开票结果查询接口
     *
     * @param form
     * @return
     */
    public List<QueryInvoiceResultV> queryInvoiceResult(QueryInvoiceResultF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), QUERYINVOICERESULT_API, content, form.getTaxnum());
        return JSON.parseArray(result, QueryInvoiceResultV.class);
    }

    /**
     * 开票重试接口
     *
     * @param form
     * @return
     */
    public String reInvoice(ReInvoiceF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), REINVOICE_API, content, form.getTaxnum());
        return result;
    }

    /**
     * 发票列表查询接口
     *
     * @param form
     * @return
     */
    public QueryInvoiceListV queryInvoiceList(QueryInvoiceListF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), QUERYINVOICELIST_API, content, form.getTaxnum());
        return JSON.parseObject(result, QueryInvoiceListV.class);
    }

    /**
     * 企业发票余量查询接口
     *
     * @param form
     * @return
     */
    public List<GetInvoiceStockV> getInvoiceStock(GetInvoiceStockF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), GETINVOICESTOCK_API, content, form.getTaxnum());
        return JSON.parseArray(result, GetInvoiceStockV.class);
    }

    /**
     * 发票作废接口
     *
     * @param form
     * @return
     */
    public InvoiceCancellationV invoiceCancellation(InvoiceCancellationF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), INVOICECANCELLATION_API, content, form.getTaxnum());
        return JSON.parseObject(result, InvoiceCancellationV.class);
    }

    /**
     * 红字专用发票信息表申请接口
     *
     * @param form
     * @return
     */
    public InvoiceRedApplyV InvoiceRedApply(InvoiceRedApplyF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), INVOICEREDAPPLY_API, content, form.getTaxnum());
        return JSON.parseObject(result, InvoiceRedApplyV.class);
    }

    /**
     * 红字专用发票信息表撤销接口
     *
     * @param form
     * @return
     */
    public CancelInvoiceRedApplyV cancelInvoiceRedApply(CancelInvoiceRedApplyF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), CANCELINVOICEREDAPPLY_API, content, form.getTaxnum());
        return JSON.parseObject(result, CancelInvoiceRedApplyV.class);
    }

    /**
     * 红字专用发票信息表查询接口
     *
     * @param form
     * @return
     */
    public List<InvoiceRedQueryV> invoiceRedQuery(InvoiceRedQueryF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), INVOICEREDQUERY_API, content, form.getTaxnum());
        return JSON.parseArray(result, InvoiceRedQueryV.class);
    }

    /**
     * 红字专用发票信息表下载接口
     *
     * @param form
     * @return
     */
    public DownloadInvoiceRedApplyV downloadInvoiceRedApply(DownloadInvoiceRedApplyF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(), DOWNLOADINVOICEREDAPPLY_API, content, form.getTaxnum());
        return JSON.parseObject(result, DownloadInvoiceRedApplyV.class);
    }

    /**
     * 请求开具全电发票接口(2.0)
     *
     * @param form
     * @return
     */
    public BillingNewV opeMplatformBillingNew(BillingNewF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(),OPEMPLATFORM_REQUESTBILLINGNEW_API, content, form.getTaxnum());
        return JSON.parseObject(result, BillingNewV.class);
    }

    /**
     * 诺税通saas发票详情查询接口
     *
     * @param form
     * @return
     */
    public List<QueryInvoiceResultV> opeMplatformQueryInvoiceResult(QueryInvoiceResultF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(),OPEMPLATFORM_QUERYINVOICERESULT, content, form.getTaxnum());
        return JSON.parseArray(result, QueryInvoiceResultV.class);
    }

    /**
     * 诺税通saas红字确认单申请
     * @param form
     * @return 红字确认单申请号
     */
    public String electronInvoiceRedApply(ElectronInvoiceRedApplyF form) {
        String content = JSON.toJSONString(form);
        return sendPost(form.getTenantId(),ELECTRON_INVOICE_RED_APPLY, content, form.getSellerTaxNo());
    }


    public String electronInvoiceRedQuery(ElectronInvoiceRedQueryF form) {
        String content = JSON.toJSONString(form);
        return sendPost(form.getTenantId(),ELECTRON_INVOICE_RED_QUERY, content, form.getTaxnum());
    }

    /**
     * 红票快捷冲红接口
     *
     * @param form
     * @return
     */
    public BillingNewV fastInvoiceRed(FastInvoiceRedF form) {
        String content = JSON.toJSONString(form);
        String result = sendPost(form.getTenantId(),ELECTRONINVOICE_FASTINVOICERED, content, form.getTaxnum());
        return JSON.parseObject(result, BillingNewV.class);
    }


    /**
     * 向诺诺发送post请求
     *
     * @return
     */
    private String sendPost(String tenantId, String method, String content, String taxnum) {
        //获取当前租户配置
        String filter = getFilter(method);
        String configJson = externalConfigRepository.getConfig(tenantId, NUONUO_PREFIX, filter);
        NuonuoConfigDTO nuonuoConfigDTO = JSON.parseObject(configJson, NuonuoConfigDTO.class);
        //获取token
        String token = getToken(tenantId, taxnum);
        String simpleUUID = IdUtil.simpleUUID();
        String openUrlHost = openUrl;
        if (StringUtils.isNotBlank(nuonuoConfigDTO.getTempHost())) {
            openUrlHost = nuonuoConfigDTO.getTempHost();
        }
        log.info("诺诺开票入参： 【senid】: {}, 【appKey】: {}, 【appSecret】: {}, 【token】: {}, 【taxnum】: {}, 【method】: {}, 【content】: {}，【urlHost】：{}", simpleUUID, nuonuoConfigDTO.getAppKey(), nuonuoConfigDTO.getAppSecret(), token, taxnum, method, content,openUrlHost);
        String result = NNOpenSDK.getIntance().sendPostSyncRequest(openUrlHost, simpleUUID, nuonuoConfigDTO.getAppKey(), nuonuoConfigDTO.getAppSecret(), token, taxnum, method, content);
        log.info("诺诺开票反参： {}", result);
        //   打印申请接口出参不一致 特殊处理
        if ("nuonuo.OpeMplatform.invoicePrintBatch".equals(method) ) {
            return result;
        } else {
            return handleResult(result);
        }
    }



    /**
     * 添加过滤自动，兼容研发环境一套key用于增值税电子发票，一套key用于全电发票的情况
     * @param method
     * @return
     */
    private String getFilter(String method) {
        // 测试环境用另一套appKey来测试
        if (OPEMPLATFORM_REQUESTBILLINGNEW_API.equals(method) || OPEMPLATFORM_QUERYINVOICERESULT.equals(method)
                || ELECTRON_INVOICE_RED_APPLY.equals(method) || ELECTRON_INVOICE_RED_QUERY.equals(method)
                || ELECTRONINVOICE_FASTINVOICERED.equals(method)) {
            return FULL_ELECTRONIC_INVOICE_FILTER;
        }
        return null;
    }

    /**
     * 获取诺诺token
     *
     * @param tenantId
     * @param taxnum
     * @return
     */
    private String getToken(String tenantId, String taxnum) {
        NuonuoTokenE nuonuoTokenE = nuonuoTokenRepository.getToken(tenantId, taxnum);
        if (null == nuonuoTokenE) {
            throw BizException.throw400("该税号:"+taxnum+"未配置相应token，请联系管理员");
        }
        return nuonuoTokenE.getToken();
    }

    private String handlePrintResult(String result){
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if (!code.equals(0000)) {
            String describe = jsonObject.getString("msg");
            throw BizException.throw400(describe);
        }
        String resultJson = jsonObject.getString("data");
        return resultJson;
    }

    /**
     * 处理诺诺反参
     *
     * @param result
     * @return
     */
    private String handleResult(String result) {
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if (code.equals("E9500")) {
            String describe = jsonObject.getString("describe");
            log.info(describe);
            return null;
        }
        if (!code.equals(SUCCESS_CODE)) {
            String describe = jsonObject.getString("describe");
            throw BizException.throw400(describe);
        }
        String resultJson = jsonObject.getString("result");
        if (StringUtils.isBlank(resultJson)) {
            return jsonObject.getString("total");
        } else {
            return resultJson;
        }
    }
}
