package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.financialnumber;

import lombok.Data;

/**
 * @author longhuadmin
 */
@Data
public class FinancialDocumentStatusResultV {
    private String SPSJ;
    private String LYDJNM;
    private String SPR;
    private String DJNM;
    private String DQHJMC;
    private String XYQT;
    private String SPYJ;
    private String DJBH;

    /**
     * 制单	0	　
     * 财务初审	13	　
     * 审批中	1	　
     * 财务审核	2	　
     * 结算办理	10	付款类单据有此状态
     * 凭证生成	30	付款单到这个环节代表付款完成
     * 完成	6	　
     * 驳回	-1	　
     * 开出保函经办	4	仅保函使用
     * 开出资信证明经办	5	仅资信证明使用
     * 资信岗	8	仅保函、资信证明使用
     * 代付办理	9	仅供薪酬单据使用
     */
    private String DQHJ;
}
