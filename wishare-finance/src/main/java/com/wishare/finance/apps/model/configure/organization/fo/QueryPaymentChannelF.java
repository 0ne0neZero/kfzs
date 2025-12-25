package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 支付渠道查询信息
 *
 * @Author dxclay
 * @Date 2022/11/22
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("支付渠道商查询信息")
public class QueryPaymentChannelF {

    @ApiModelProperty(value = "服务商id")
    private Long spId;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;


    @ApiModelProperty(value = "外部商户编号 特殊条件：商户id和外部商户编号同时存在则以商户id为准")
    @NotEmpty(message = "外部商户编号不能为空")
    private String outMchNo;

    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行")
    private List<Integer> channelTypes;

}
