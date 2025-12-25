package com.wishare.finance.domains.invoicereceipt.consts;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
public interface NuonuoConsts {

    /**
     * 申请开票api方法名称
     */
    String REQUESTBILLINGNEW_API = "nuonuo.ElectronInvoice.requestBillingNew";


    /**
     * 获取批量打印编号
     */
    String INVOICE_PRINT_BATCH_API = "nuonuo.OpeMplatform.invoicePrintBatch";
    /**
     * 开票结果查询接口
     */
    String QUERYINVOICERESULT_API = "nuonuo.ElectronInvoice.queryInvoiceResult";

    /**
     * 开票重试接口
     */
    String REINVOICE_API = "nuonuo.ElectronInvoice.reInvoice";

    /**
     * 发票列表查询接口
     */
    String QUERYINVOICELIST_API = "nuonuo.ElectronInvoice.queryInvoiceList";

    /**
     * 企业发票余量查询接口
     */
    String GETINVOICESTOCK_API = "nuonuo.ElectronInvoice.getInvoiceStock";

    /**
     * 发票作废接口
     */
    String INVOICECANCELLATION_API = "nuonuo.electronInvoice.invoiceCancellation";

    /**
     * 红字专用发票信息表申请接口
     */
    String INVOICEREDAPPLY_API = "nuonuo.OpeMplatform.InvoiceRedApply";

    /**
     * 红字专用发票信息表撤销接口
     */
    String CANCELINVOICEREDAPPLY_API = "nuonuo.ElectronInvoice.cancelInvoiceRedApply";

    /**
     * 红字专用发票信息表查询接口
     */
    String INVOICEREDQUERY_API = "nuonuo.ElectronInvoice.InvoiceRedQuery";

    /**
     * 红字专用发票信息表下载接口
     */
    String DOWNLOADINVOICEREDAPPLY_API = "nuonuo.ElectronInvoice.downloadInvoiceRedApply";

    /**
     * 诺诺开票成功标识
     */
    String SUCCESS_CODE = "E0000";

    /**
     * 诺诺前缀
     */
    String NUONUO_PREFIX = "nuonuo";


    /**
     * 请求开具全电发票接口(2.0)
     */
    String OPEMPLATFORM_REQUESTBILLINGNEW_API = "nuonuo.OpeMplatform.requestBillingNew";

    /**
     * 诺税通saas发票详情查询接口
     */
    String OPEMPLATFORM_QUERYINVOICERESULT = "nuonuo.OpeMplatform.queryInvoiceResult";

    /**
     * 诺税通saas红字确认单申请接口
     */
    String ELECTRON_INVOICE_RED_APPLY = "nuonuo.OpeMplatform.saveInvoiceRedConfirm";

    /**
     * 诺税通saas红字确认单查询接口
     */
    String ELECTRON_INVOICE_RED_QUERY = "nuonuo.OpeMplatform.queryInvoiceRedConfirm";

    /**
     * 全电发票快捷冲红接口
     */
    String ELECTRONINVOICE_FASTINVOICERED = "nuonuo.OpeMplatform.fastInvoiceRed";



    /**
     * 全电发票过滤配置
     */
    String FULL_ELECTRONIC_INVOICE_FILTER = "full_electronic_invoice";

    /**
     * 发票已红冲返回提示
     */
    String ALREADY_RED_MSG = "已开具状态";
}
