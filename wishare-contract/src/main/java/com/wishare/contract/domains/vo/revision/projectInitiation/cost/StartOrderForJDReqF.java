package com.wishare.contract.domains.vo.revision.projectInitiation.cost;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 京东慧采下单请求参数
 */
@Data
@Accessors(chain = true)
public class StartOrderForJDReqF {

    @ApiModelProperty("立项ID")
    @NotNull(message = "立项ID不能为空")
    private String id;

    @ApiModelProperty("订单ID")
    private String orderId;

    @ApiModelProperty("京东订单ID")
    private String jdOrderId;

    @ApiModelProperty("慧采账号")
    @NotNull(message = "慧采账号不能为空")
    private String userName;

    @ApiModelProperty("慧采密码")
    private String pwdMd5;

    @ApiModelProperty("慧采密码")
    @NotNull(message = "慧采密码不能为空")
    private String password;

    public String getPwdMd5() {
        return null != password ? Md5DigestUtils.getMD5Str(password).toLowerCase() : pwdMd5;
    }
}
