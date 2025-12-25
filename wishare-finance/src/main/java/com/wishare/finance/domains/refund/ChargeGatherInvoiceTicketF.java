package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author ℳ๓采韵
 * @project wishare-charge
 * @title ChargeGatherInvoiceTicketF
 * @date 2024.03.08  13:51
 * @description 收款单开票参数
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收款单开票参数")
public class ChargeGatherInvoiceTicketF {

    @ApiModelProperty("收款单ID")
    @NotNull(message = "收款单ID不能为空")
    private Long gatherBillId;

    @ApiModelProperty("账单id列表")
    private List<Long> billIds;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("房号列表")
    @NotEmpty(message = "请传入房号列表")
    private Set<String> roomIds;

    @ApiModelProperty("租户ID")
    @NotBlank(message = "租户ID不能为空")
    private String tenantId;

    @ApiModelProperty("项目ID")
    @NotBlank(message = "项目ID不能为空")
    private String communityId;

    @ApiModelProperty("付款人手机")
    private String payerPhone;

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名称不能为空")
    private String userName;
    @ApiModelProperty(value = "支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序  5-线上物管pos机" +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端"
            , hidden = true)
    private Integer paySource;
}
