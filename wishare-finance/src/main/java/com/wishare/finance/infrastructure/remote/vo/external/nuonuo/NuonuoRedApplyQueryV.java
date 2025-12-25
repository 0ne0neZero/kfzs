package com.wishare.finance.infrastructure.remote.vo.external.nuonuo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/15
 */
@Getter
@Setter
public class NuonuoRedApplyQueryV {

    /**
     * 红字确认单编号
     */
    private String billNo;
    /**
     * 红字确认单uuid
     */
    private String billUuid;
    /**
     * 红字确认单状态： 01 无需确认 02 销方录入待
     * 购方确认 03 购方录入待销方确认 04 购销双方
     * 已确认 05 作废（销方录入购方否认） 06 作废
     * （购方录入销方否认） 07 作废（超72小时未确
     * 认） 08 作废（发起方已撤销） 09 作废（确认
     * 后撤销） 15 申请中 16 申请失败
     */
    private String billStatus;
    /**
     * 操作状态：（根据操作方返回对应状态，可能为
     * 空） 01 撤销中 02撤销失败 03 确认中 04 确认
     * 失败
     */
    private String requestStatus;
    /**
     * 已开具红字发票标记： 1：已开具 0：未开具
     */
    private String openStatus;
    /**
     * 红字确认单明细信息列表
     */
    private String detail;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 已有红冲单，针对百望开票使用，不使用上面的openStatus字段，避免处理openStatus字段影响其他的对接流程
     */
    private Boolean hasRedInvoice = Boolean.FALSE;

    /**
     * 诺诺：开票信息
     */
    private String billMessage;

    /**
     * 错误提示
     */
    private String errMsg;
}
