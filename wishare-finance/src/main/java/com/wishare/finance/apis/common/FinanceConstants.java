package com.wishare.finance.apis.common;

/**
 * @author szh
 * @date 2024/4/19 9:12
 */
public class FinanceConstants {

    public static final String BPM_VOUCHER_LOG = "makeBPMVoucher做凭证辅助行";

    public static final String developer_pay_log = "开发代付收据日志:";
    public static final String customer_type_change = "收费对象调整,凭证";

    public static final String CHECK_SPECIAL_CONDITIONS = "特殊收款结算事件";
    public static final String CHECK_SPECIAL_CONDITIONS_MSG = "提交失败，当前规则包含的收款方式含现金流和非现金流的收款方式，不可并存，请修改。";

    public static final String developer_pay_error_log = "请先选择相同房号，同法定单位下、开发代付生产的账单打印开发代付凭据";

    public static final String auto_sync_ncc_push = "定时生成凭证下自动推";
}
