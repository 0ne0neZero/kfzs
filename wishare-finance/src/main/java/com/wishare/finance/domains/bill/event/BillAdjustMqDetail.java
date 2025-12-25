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
 * @since 2023/4/19
 */
@Getter
@Setter
@ApiModel("调整mq发送详情")
public class BillAdjustMqDetail {

    @ApiModelProperty("调整金额，单位分，可为负")
    private Long adjustAmount;

    @ApiModelProperty("调整类型： 1减免，2调高")
    private Integer adjustType;

    @ApiModelProperty("实收金额，单位分")
    private Long actualPayAmount;

    @ApiModelProperty("开票金额，单位分")
    private Long invoiceAmount;

    @ApiModelProperty("（调整后）应收金额，单位分")
    private Long receivableAmount;

    @ApiModelProperty("账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    public BillAdjustMqDetail() {

    }

    public <T extends Bill> BillAdjustMqDetail(BillAdjustA<T> billAdjustA) {
        adjustAmount = billAdjustA.getAdjustAmount();
        adjustType = billAdjustA.getAdjustType();
        actualPayAmount = billAdjustA.getBill().getActualPayAmount();
        invoiceAmount = billAdjustA.getBill().getInvoiceAmount();
        billType = billAdjustA.getBillType();
        receivableAmount = billAdjustA.getBill().getReceivableAmount();
    }

    /**
     * 获取应红冲金额，对应金额发票红冲
     * @return
     */
    public Long getRedAmount() {
        long refundAmount = this.receivableAmount - invoiceAmount;
        if (refundAmount < 0) {
            return Math.abs(refundAmount);
        } else {
            return 0L;
        }
    }

}
