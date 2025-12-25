package com.wishare.finance.apps.model.configure.organization.vo;

import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.remote.enums.ChannelTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/15
 * @Description:
 */
@Getter
@Setter
@ApiModel("支付渠道反参")
public class PayChannelConfV {

    @ApiModelProperty(value = "渠道商配置id")
    private Long id;

    @ApiModelProperty(value = "渠道配置名称")
    private String name;

    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行")
    private Integer channelType;
    @ApiModelProperty("渠道商类型描述")
    private String channelTypeStr;

    @ApiModelProperty(value = "渠道配置参数JSON，根据不同渠道，配置的参数不同")
    private String channelParams;

    @ApiModelProperty("是否启用： 0启用：1禁用")
    private Integer disabled;
    @ApiModelProperty("是否启用描述")
    private String disabledStr;

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

    public String getDisabledStr() {
        if (null != this.getDisabled()) {
            return DataDisabledEnum.valueOfByCode(this.getDisabled()).getDes();
        }
        return null;
    }

    public String getChannelTypeStr() {
        if (null != this.getChannelType()) {
            return ChannelTypeEnum.valueOfByCode(this.getChannelType()).getDes();
        }
        return null;
    }
}
