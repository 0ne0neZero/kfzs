package com.wishare.finance.apps.model.configure.arrearsCategory.vo;

import com.wishare.finance.apps.model.bill.vo.ReceivableBillPageV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@ApiModel("含欠费原因的账单返回体")
public class ArrearsReasonBillV extends ReceivableBillPageV {

    @ApiModelProperty(value = "欠费原因")
    private String arrearsReason;

    @ApiModelProperty("欠缴金额")
    private Long amountDueArrears;

    @ApiModelProperty(value = "最近欠费类别")
    private String arrearsCategoryName;

    @ApiModelProperty(value = "最近欠费类别id")
    private Long arrearsCategoryId;

    @ApiModelProperty("最新催缴日期")
    private LocalDateTime callDate;


    /**
     * 实际应缴金额
     */
    private Long actualUnpayAmount;

    public Long getAmountDueArrears() {
        return getActualUnpayAmount() == null ? 0 : getActualUnpayAmount();
    }


    public Long getActualUnpayAmount() {
        return getReceivableAmount() + getOverdueAmount() - getDiscountAmount() + getRefundAmount() + getCarriedAmount() - getSettleAmount();
    }
}
