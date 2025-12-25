package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("回调VO")
public class CallBackV {

    @ApiModelProperty("处理是否成功")
    private Boolean result;
}
