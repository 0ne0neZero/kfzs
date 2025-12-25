package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("预收账单详情(包含各个操作信息)")
public class AdvanceBillAllDetailV extends BillAllDetailV {

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty(value = "计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积")
    private Integer billArea;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("业务类型")
    private String billType;

    @ApiModelProperty("收款单ID")
    private Long gatherBillId;

    public void billInvoiceAmount(){
        if (getSettleState() == BillSettleStateEnum.未结算.getCode()) {
            setCanInvoiceAmount(getReceivableAmount() -
                    getDiscountAmount() - getInvoiceAmount());
        } else {
            if (TenantUtil.bf24()) {
                // 方圆慧享云能开出结转出去的金额，结转进来的金额不能开(押金结转进来的金额能开)
                List<PayInfo> payInfos = getPayInfos();
                long carriedInAmount = 0L;
                if (CollectionUtils.isNotEmpty(payInfos)) {
                    carriedInAmount = payInfos.stream()
                            .filter(info -> StringUtils.equals(info.getPayChannel(), SettleChannelEnum.结转.getCode()))
                            .mapToLong(PayInfo::getAmount)
                            .sum();
                }
                // 方圆慧享云能开出结转出去的金额
                setCanInvoiceAmount(getActualPayAmount() - getInvoiceAmount() + getCarriedAmount() - carriedInAmount);
            } else {
                setCanInvoiceAmount(getActualPayAmount() - getInvoiceAmount());
            }
        }
    }

}
