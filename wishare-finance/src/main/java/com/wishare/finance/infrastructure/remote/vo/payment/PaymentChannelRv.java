package com.wishare.finance.infrastructure.remote.vo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("支付渠道配置")
public class PaymentChannelRv {

    @ApiModelProperty(value = "渠道商配置id")
    private Long id;

    @ApiModelProperty(value = "渠道配置名称")
    private String name;

    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行")
    private Integer channelType;

    @ApiModelProperty(value = "渠道配置参数JSON，根据不同渠道，配置的参数不同")
    private String channelParams;

    @ApiModelProperty("是否启用： 0启用：1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "应用id")
    private Long applicationId;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}
