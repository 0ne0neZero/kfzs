package com.wishare.contract.domains.consts;

/**
 * 合同设置常量
 *
 * @author yancao
 */
public class ContractSetConst {

    /**
     * 合同分类最大层级
     */
    public static final Integer CONTRACT_CATEGORY_MAX_LEVEL = 5;

    /**
     * 文件上传用  服务名
     */
    public static final String serviceName = "contract";

    /**
     * 合同中心名称
     */
    public static final String CONTRACT = "contract";

    /**
     * 损益计划收款方式 按合同期限
     */
    public static final Integer CONTRACT_PERIOD = 1;
    /**
     * 损益计划收款方式 按服务期限
     */
    public static final Integer SERVICE_PERIOD = 2;
    /**
     * 损益计划收款方式 按事点期限
     */
    public static final Integer ON_TIME = 3;
    /**
     * 合同类型-补充协议
     */
    public static final Integer SUPPLEMENT = 1;
    /**
     * 合同类型-子合同
     */
    public static final Integer SUB_CONTRACT = 5;
    /**
     * 合同类型-强制终止
     */
    public static final Integer KILL_CONTRACT = 6;
    /**
     * 合同类型-续签协议
     */
    public static final Integer RENEWAL = 2;
    /**
     * 合同类型-新签
     */
    public static final Integer NEW_SIGNATURE = 0;
    /**
     * 合同类型-终止协议
     */
    public static final Integer STOP = 3;

    /**
     * 合同性质-收入类
     */
    public static final Integer INCOME = 1;

    /**
     * 合同性质-支出类
     */
    public static final Integer PAY = 2;

    /**
     * 合同状态-履行中
     */
    public static final Integer PERFORM = 1;
    /**
     * 合同状态-已到期
     */
    public static final Integer BECOME_DUE = 2;
    /**
     * 合同状态-已终止
     */
    public static final Integer BECOME_STOPED = 3;
    /**
     * 合同状态-终止中
     */
    public static final Integer STOP_CONTRACT = 4;

    /**
     * 合同状态-未履行
     */
    public static final Integer NOT_PERFORM = 0;

    /**
     * 审核状态-已通过
     */
    public static final Integer POSS = 2;

    /**
     * 审核状态-未审核
     */
    public static final Integer NOT_POSS = 0;

    /**
     * 审核状态-审批中
     */
    public static final Integer POSSING = 1;

    /**
     * 审核状态-已拒绝
     */
    public static final Integer REJECT = 3;

    /**
     * 合同状态-已补充
     */
    public static final Integer CHANGE = 5;

    /**
     * 财务-计费方式-固定金额
     */
    public static final Integer BILL_METHOD_GDJE = 8;

    /**
     * 合同收款/付款计划-收款/付款状态 0未收/付  1部分收/付  2已收/付
     */
    public static final Integer NOT_COLLECTED = 0;

    public static final Integer PART_COLLECTED = 1;

    public static final Integer ALL_COLLECTED = 2;

    /**
     * 合同收款/付款计划-开票/收票状态 0未开/收  1部分开/收  2已开/收
     */
    public static final Integer NOT_INVOICED = 0;

    public static final Integer PART_INVOICED = 1;

    public static final Integer ALL_INVOICED = 2;
    // 收款计划 开票进度
    public static final Integer INVOICE_SUCCESS = 0;
    public static final Integer INVOICE_ING = 1;
    public static final Integer INVOICE_FAIL = 2;

    /**
     * 操作类型收款、付款、开票
     */
    public static final Integer COLLECTION = 1;
    public static final Integer PAYMENT = 2;
    public static final Integer INVOICE = 3;

    /**
     * 付款明细表 审核状态  0通过  1审核中  2未通过
     */
    public static final Integer PAYMENT_POSS = 0;
    public static final Integer PAYMENT_POSSING = 1;
    public static final Integer PAYMENT_REJECT = 2;

    /**
     * 成本中心形式 1 项目 2行政组织
     */
    public static final Integer COSTFORM_XM = 1;


    /**
     * 财务中台账单合同业务id
     */
    public static final String CONTRACTAPPID = "85e822bdf5b54d27a8c49ed1d5ec234e";
    public static final String CONTRACTAPPNAME = "合同系统";

    public static final String CONTRACT_APP_BOND_PLAN_NAME = "合同系统-保证金计划";

    /**
     * 保证金收/付款状态  0未收/付  1部分收/付  2已收/付
     */
    public static final Integer NOT_PAYMENT = 0;

    public static final Integer PART_PAYMENT = 1;

    public static final Integer ALL_PAYMENT = 2;

    /**
     * 保证金收/退款状态   0未收/退  1部分收/退  2已收/退
     */
    public static final Integer NOT_REFUND = 0;

    public static final Integer PART_REFUND = 1;

    public static final Integer ALL_REFUND = 2;

    /**
     * 付/退渠道  0现金  1银行转帐  2汇款  3支票
     */
    public static final Integer PAYMENT_METHOD_CASH = 0;
    public static final Integer PAYMENT_METHOD_BANK = 1;
    public static final Integer PAYMENT_METHOD_REMITTANCE = 2;
    public static final Integer PAYMENT_METHOD_CHEQUE = 3;

    /**
     * 收款渠道  0现金  1网上转帐  2支付宝  3微信
     */
    public static final Integer COLLECTION_METHOD_CASH = 0;
    public static final Integer COLLECTION_METHOD_TRANSFER = 1;
    public static final Integer COLLECTION_METHOD_ALIPAY = 2;
    public static final Integer COLLECTION_METHOD_WECHAT = 3;
}
