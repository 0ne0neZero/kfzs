package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.entity.DiscountOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 应收账单支付下单数据
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("应收账单支付下单信息")
public class ReceivableBillPreTransactionCommand {

    @ApiModelProperty(value = "账单id列表", required = true)
    @Size(max = 200, min = 1, message = "账单id列表大小不正确，大小区间为[1,500]")
    private List<String> billIds;

    @ApiModelProperty(value = "支付金额（单位：分）", required = true)
    @NotNull(message = "支付金额不能为空")
    private Long totalAmount;

    @ApiModelProperty(value = "减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty(value = "减免说明列表")
    private List<DiscountOBV> discounts;

    @ApiModelProperty(value = "收款人ID")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款人ID")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "付款人手机号码")
    private String payerPhone;

    @ApiModelProperty(value = "请求超时时间 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

}
