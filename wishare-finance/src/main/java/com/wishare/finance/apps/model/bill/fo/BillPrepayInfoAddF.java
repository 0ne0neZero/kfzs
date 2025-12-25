package com.wishare.finance.apps.model.bill.fo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-charge
 * @title BillPrepayInfoAddF
 * @date 2023.11.08  14:26
 * @description 账单预支付信息添加
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("账单预支付信息添加入参")
public class BillPrepayInfoAddF extends BillPrepayInfoF{

    @ApiModelProperty("支付生效时间不能为空")
    @NotNull(message = "支付生效时间不能为空")
    @NotNull(message = "支付生效时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("支付过期时间不能为空")
    @NotNull(message = "支付过期时间不能为空")
    @NotNull(message = "支付生效时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty("支支付状态不能为空")
    @NotNull(message = "支付状态不能为空")
    private Integer payState;

    @ApiModelProperty("关联二维码url")
    private String qrCodeUrl;

    @ApiModelProperty(value = "商户订单号")
    private String mchOrderNo;

    @ApiModelProperty("支付来源")
    private Integer paySource;
}
