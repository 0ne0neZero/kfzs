package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/1/4
 * @Description:
 */
@Getter
@Setter
@ApiModel("统计费项分组分页查询账单列表(用于业务信息)")
public class ChargeBillGroupStatisticsDto {

    @ApiModelProperty("账单数量")
    private Long billNum = 0L;
    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount = 0L;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount = 0L;
    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmount = 0L;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmount = 0L;
    @ApiModelProperty("结算金额 (单位： 分)")
    private Long settleAmount = 0L;
    @ApiModelProperty("结转金额")
    private Long carriedAmount = 0L;
    @ApiModelProperty("退款金额")
    private Long refundAmount = 0L;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount = 0L;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount = 0L;

    public Long getActualUnpayAmount() {
        return receivableAmount + overdueAmount - discountAmount + refundAmount + carriedAmount - settleAmount;
    }

    /**
     * 获取实际收款金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额
     */
    public Long getActualPayAmount() {
        return settleAmount - refundAmount - carriedAmount;
    }

}
