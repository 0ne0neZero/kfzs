package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.GatherBill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Setter
@Getter
@Accessors(chain = true)
@ApiModel("未开票收款单信息")
public class UnInvoiceGatherBillDto extends GatherBill {

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime payTime;

    @ApiModelProperty("单据编号")
    private String billNo;

    @ApiModelProperty("单据ID")
    private String billId;

    @ApiModelProperty("单据来源")
    private Integer sysSource;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "房号")
    private String roomName;

    @ApiModelProperty(value = "房号Id")
    private String roomId;

    @ApiModelProperty(value = "收费对象ID")
    private String payerId;

    @ApiModelProperty(value = "收费对象名称")
    private String payerName;

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;
    public String getPayChannelName(){
        if (StringUtils.isNotBlank(this.payChannel)){
            String[] paychannels = StringUtils.split(this.payChannel, ",");
            return Arrays.stream(paychannels)
                    .map(channel -> SettleChannelEnum.valueNameOfByCode(payChannel))
                    .collect(Collectors.joining(","));
        }
        return "";
    }

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty(value = "结算金额")
    private Long settleAmount;

    public Long getSettleAmount() {
        this.settleAmount = getActualSettleAmount();
        return settleAmount;
    }

}
