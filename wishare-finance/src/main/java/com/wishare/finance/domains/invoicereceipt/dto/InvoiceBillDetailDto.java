package com.wishare.finance.domains.invoicereceipt.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 发票账单相关信息
 *
 * @Author dxclay
 * @Date 2022/10/28
 * @Version 1.0
 */
@Getter
@Setter
public class InvoiceBillDetailDto {

    /**
     * 发票id
     */
    private Long id;
    /**
     * 发票类型
     */
    private Integer type;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 开票金额
     */
    private Long invoiceAmount;

    /**
     * 票据金额
     */
    private Long receiptAmount;

    /**
     * 账单id
     */
    private Long billId;

    @ApiModelProperty("收款单id")
    private Long gatherDetailId;

    /**
     * 票据类型\n" +
     * "     1: 增值税普通发票\n" +
     * "     2: 增值税专用发票\n" +
     * "     3: 增值税电子发票\n" +
     * "     4: 增值税电子专票\n" +
     * "     5: 收据\n" +
     * "     6：电子收据\n" +
     * "     7：纸质收据
     */
    private Integer invoiceType;

    /**
     * 开票时间 格式：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime invoiceTime;

    /**
     * 开票人ID
     */
    private String creator;

    /**
     * 开票人名称
     */
    private String creatorName;


}
