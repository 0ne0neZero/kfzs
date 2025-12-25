package com.wishare.finance.infrastructure.remote.vo.payment;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 支付渠道配置表
 *
 * @author dxclay
 * @since 2023-08-04
 */
@Getter
@Setter
@ApiModel(value="支付渠道配置信息")
public class ChannelConfigV {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "商户id （merchant.id）")
    private Long merchantId;

    @ApiModelProperty(value = "支付渠道唯一编码")
    private Long channelCode;
    @ApiModelProperty(value = "支付渠道唯一名称")
    private String channelName;

    @ApiModelProperty(value = "渠道配置参数JSON，根据不同渠道，配置的参数不同")
    private JSONObject channelParams;

    @ApiModelProperty(value = "是否启用： 0启用：1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    public void setChannelName(String channelCode) {
        switch (channelCode) {
            case "1":
                this.channelName = "微信";
                break;
            case "2":
                this.channelName = "支付宝";
                break;
            case "3":
                this.channelName = "银联";
                break;
            case "8":
                this.channelName = "郑州银行";
                break;
            case "9":
                this.channelName = "通联支付";
                break;
            default:
                this.channelName = "";
        }
    }

}
