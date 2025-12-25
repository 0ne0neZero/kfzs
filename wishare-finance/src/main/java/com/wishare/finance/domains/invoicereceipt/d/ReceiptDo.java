package com.wishare.finance.domains.invoicereceipt.d;

import com.wishare.finance.domains.invoicereceipt.entity.base.FinanceBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
 *
 *
 *
 *
 * @see com.wishare.finance.domains.invoicereceipt.d.ReceiptDo;
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Builder
@Data
public class ReceiptDo extends FinanceBaseEntity {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "发票收据主表id")
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "缴费时间")
    private LocalDateTime paymentTime;

    @ApiModelProperty(value = "缴费方式")
    private String paymentType;

    @ApiModelProperty(value = "图章url")
    private String stampUrl;

    @ApiModelProperty(value = "收据pdf url地址")
    private String receiptUrl;

    @ApiModelProperty(value = "是否需要签章：0 - 是 1 - 否")
    private Integer signStatus;

    @ApiModelProperty(value = "0 - 申请签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署） 8 - 未知（调用服务异常等）")
    private Integer signSealStatus;

    @ApiModelProperty(value = "收据pdf url地址(盖章)")
    private String signReceiptUrl;

    @ApiModelProperty(value = "盖章申请编号(用来获取签章结果)")
    private String signApplyNo;

    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败,4:不需要")
    private Integer sendStatus;


    @ApiModelProperty(value = "最后推送时间")
    private LocalDateTime lastPushTime;

    /**
     * 优惠信息[
    {
        "goodsName":"",
        "price":"",
        "num":"",
        "totalPrice":"",
        "remark":""
    }
]
     */
    @ApiModelProperty(value = "优惠信息")
    private String discountInfo;

    @ApiModelProperty(value = "收据号")
    private Long receiptNo;




}

