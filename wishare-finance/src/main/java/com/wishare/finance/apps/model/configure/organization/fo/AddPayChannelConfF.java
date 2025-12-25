package com.wishare.finance.apps.model.configure.organization.fo;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.infrastructure.remote.enums.ChannelTypeEnum;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/8/15
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增支付设置入参")
public class AddPayChannelConfF {

    @ApiModelProperty(value = "法定单位id", required = true)
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称", required = true)
    @NotNull(message = "法定单位名称不能为空")
    private String statutoryBodyName;
    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行", required = true)
    @NotNull(message = "渠道商类型不能为空")
    private Integer channelType;

    @ApiModelProperty("支付配置Json对象")
    private JSONObject channelParams;

    @ApiModelProperty("是否启用： 0启用：1禁用")
    private Integer disabled;

    /**
     * 校检参数
     */
    public void check() {
        checkChannelType();
    }

    /**
     * 校检渠道商类型
     */
    private void checkChannelType() {
        ChannelTypeEnum channelTypeEnum = ChannelTypeEnum.valueOfByCode(this.channelType);
        if (null == channelTypeEnum) {
            throw BizException.throw400("渠道商类型暂不存在");
        }
    }
}
