package com.wishare.contract.apps.remote.fo.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 发送短信
 * @author syy
 */
@Getter
@Setter
public class SmsSendF {

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号码不能为空")
    private String mobileNum;

    @ApiModelProperty(value = "短信模板ID", required = true)
    @NotBlank(message = "模板ID不能为空")
    private String templateId;

    @ApiModelProperty(value = "模板变量。格式：[\"param1\",\"param2\", ...]", required = false)
    private String[] params;
}
