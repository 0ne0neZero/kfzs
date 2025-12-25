package com.wishare.finance.infrastructure.conts;

public interface TableNames {

    /**
     * 账单表名称
     */
    public static final String BILL = "bill";
    /**
     * 应收账单表名称
     */
    public static final String RECEIVABLE_BILL = "receivable_bill";
    /**
     * 临时收费账单表
     */
    public static final String TEMPORARY_CHARGE_BILL = "receivable_bill";
    /**
     *账单审核记录表
     */
    public static final String BILL_APPROVE = "bill_approve";
    /**
     * 账单调整明细表
     */
    public static final String BILL_ADJUST = "bill_adjust";
    /**
     * 账单预支付信息表
     */
    public static final String BILL_PREPAY_INFO = "bill_prepay_info";


    /**
     * 预收账单表名称
     */
    public static final String ADVANCE_BILL = "advance_bill";
    /**
     * 结算表名称
     */
    public static final String BILL_SETTLE = "bill_settle";
    /**
     * 违约金管理表
     */
    public static final String CHARGE_OVERDUE = "charge_overdue";
    /**
     * 账单结转记录表
     */
    public static final String BILL_CARRYOVER = "bill_carryover";
    /**
     * 账单结转详情信息表
     */
    public static final String BILL_CARRYOVER_DETAIL = "bill_carryover_detail";
    /**
     * 账单交账记录表
     */
    public static final String BILL_HAND = "bill_hand";
    /**
     * 账单关联合同记录
     */
    public static final String BILL_CONTRACT = "bill_contract";
    /**
     * 应付账单表名称
     */
    public static final String PAYABLE_BILL = "payable_bill";

    /**
     * 冲销记录表
     */
    public static final String BILL_REVERSE = "bill_reverse";

    /**
     * 扣款明细表
     */
    public static final String BILL_DEDUCTION = "bill_deduction";

    /**
     * 付款单
     */
    public static final String PAY_BILL = "pay_bill";
    /**
     * 付款明细表
     */
    public static final String PAY_DETAIL = "pay_detail";
    /**
     * 收款单表
     */
    public static final String GATHER_BILL = "gather_bill";
    /**
     * 收款明细表
     */
    public static final String GATHER_DETAIL = "gather_detail";

    /**
     * 收款明细表
     */
    public static final String BILL_SPLIT_LOG = "bill_split_log";
    /**
     * 交账信息表
     */
    public static final String BILL_ACCOUNT_HAND = "bill_account_hand";
    /**
     * 交账规则表
     */
    public static final String BILL_ACCOUNT_HAND_RULE = "bill_account_hand_rule";
    /**
     * 交易订单表
     */
    public static final String TRANSACTION_ORDER = "transaction_order";
    /**
     * 凭证信息表
     */
    public static final String VOUCHER_INFO = "voucher_info";
    /**
     * 凭证规则运行记录表
     */
    public static final String VOUCHER_RULE_RECORD = "voucher_rule_record";
    /**
     * 辅助核算（业务类型）
     */
    public static final String ASSISTE_BIZ_TYPE = "assiste_biz_type";
    /**
     * 辅助核算（收支项目）
     */
    public static final String ASSISTE_INOUTCLASS = "assiste_inoutclass";
    /**
     * 辅助核算（业务单元）
     */
    public static final String ASSISTE_ORG = "assiste_org";
    /**
     * 辅助核算（部门）
     */
    public static final String ASSISTE_ORG_DEPT = "assiste_org_dept";
    /**
     * 科目-现金流量关系表
     */
    public static final String SUBJECT_CASH_FLOW = "subject_cash_flow";
    /**
     * 科目表
     */
    public static final String SUBJECT = "subject";
    /**
     * 科目体系表
     */
    public static final String SUBJECT_SYSTEM = "subject_system";
    /**
     * 税率
     */
    public static final String TAX_RATE = "tax_rate";
    /**
     * 税种
     */
    public static final String TAX_CATEGORY = "tax_category";
    /**
     * 现金流量表
     */
    public static final String CASH_FLOW = "cash_flow";
    /**
     * 凭证规则
     */
    public static final String VOUCHER_RULE = "voucher_rule";
    /**
     * 凭证业务明细表
     */
    public static final String VOUCHER_BUSINESS_DETAIL = "voucher_business_detail";
    /**
     * 凭证核算方案表
     */
    public static final String VOUCHER_SCHEME = "voucher_scheme";
    /**
     * 凭证核算方案凭证规则关联表
     */
    public static final String VOUCHER_SCHEME_RULE = "voucher_scheme_rule";
    /**
     * 凭证核算方案财务组织关联表
     */
    public static final String VOUCHER_SCHEME_ORG = "voucher_scheme_org";
    /**
     * 凭证模板表
     */
    public static final String VOUCHER_TEMPLATE = "voucher_template";

    String INVOICE_RED_APPLY = "invoice_red_apply";

    /**
     * 推单规则规则
     */
    public static final String VOUCHER_BILL_RULE = "voucher_bill_rule";

    /**
     * 汇总单据表
     */
    public static final String VOUCHER_BILL = "voucher_bill";
    public static final String VOUCHER_BILL_ZJ = "voucher_bill_zj";
    public static final String VOUCHER_BILL_DX_ZJ = "voucher_bill_dx_zj";

    public static final String PAYMENT_APPLICATION_FORM_ZJ = "payment_application_form";

    public static final String FINANCE_PROCESS_RECORD_ZJ = "finance_process_record";

    public static final String PAYMENT_APPLICATION_FORM_KXMX = "payment_application_form_kxmx";

    public static final String PAYMENT_APPLICATION_FORM_PAY_MX = "payment_application_form_pay_mx";




    /**
     * 汇总明细表
     */
    public static final String VOUCHER_BILL_DETAIL = "voucher_bill_detail";
    public static final String VOUCHER_BILL_DETAIL_ZJ = "voucher_bill_detail_zj";
    public static final String VOUCHER_BILL_DETAIL_DX_ZJ = "voucher_bill_detail_dx_zj";
    public static final String VOUCHER_CONTRACT_INVOICE_ZJ = "voucher_contract_zj";

    public static final String VOUCHER_INVOICE_ZJ = "voucher_invoice_ZJ";
    /**
     * 中交合同发票计量明细表
     */
    public static final String VOUCHER_CONTRACT_MEASURE_DETAIL_ZJ = "voucher_contract_measurement_detail_ZJ";
    /**
     * 凭证规则运行记录表
     */
    public static final String VOUCHER_BILL_RULE_RECORD = "voucher_bill_rule_record";
    /**
     *  费项客商关联表
     */
    public static final String CHARGE_CUSTOMER = "charge_customer";

    /**
     * 账单跳收明细表
     */
    public static final String BILL_JUMP = "bill_jump";

    /**
     * 账单冻结明细记录表
     */
    public static final String BILL_FREEZE = "bill_freeze";
    /**
     * 关账记录表
     */
    public static final String CLOSE_ACCOUNT = "close_account";

    String BUSINESS_BILL = "business_bill";
    String YY_NCC_CUSTOMER_REL = "yy_ncc_customer_rel";

    String YY_NCC_PERSON = "yy_ncc_person";

    String BUSINESS_BILL_DETAIL = "business_bill_detail";
    /** 票据推送记录表 */
    String RECEIPT_SEND_LOG="receipt_send_log";

    String PARK_RENEW_INFO="park_renew_info";
    /** 开票流程监控表 */
    String INVOICE_FLOW_MONITOR="invoice_flow_monitor";
    /**
     * 费用报账主表（拈花湾金蝶报账）
     */
    String EXPENSE_REPORT = "expense_report";
    /**
     * 费用报账明细表（拈花湾金蝶报账）
     */
    String EXPENSE_REPORT_DETAIL = "expense_report_detail";



}
