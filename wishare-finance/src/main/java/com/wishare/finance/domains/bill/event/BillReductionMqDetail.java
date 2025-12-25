package com.wishare.finance.domains.bill.event;

import com.wishare.finance.domains.bill.aggregate.BillAdjustA;
import com.wishare.finance.domains.bill.entity.Bill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/20
 */
@Getter
@Setter
@ApiModel("调整mq发送详情")
public class BillReductionMqDetail {

    @ApiModelProperty("调整金额，单位分")
    private Long adjustAmount;

    @ApiModelProperty("调整类型： 1减免，2调高")
    private Integer adjustType;

    @ApiModelProperty("实收金额，单位分")
    private Long actualPayAmount;

    @ApiModelProperty("开票金额，单位分")
    private Long invoiceAmount;

    public BillReductionMqDetail() {

    }

    public <T extends Bill> BillReductionMqDetail(BillAdjustA<T> billAdjustA) {
        adjustAmount = billAdjustA.getAdjustAmount();
        adjustType = billAdjustA.getAdjustType();
        actualPayAmount = billAdjustA.getBill().getActualPayAmount();
        invoiceAmount = billAdjustA.getBill().getInvoiceAmount();
    }

    /**
     * 获取应退款金额，对应金额发票红冲
     * @return
     */
    public Long getRefundAmount() {
        return Math.abs(actualPayAmount - invoiceAmount + adjustAmount);
    }
}
