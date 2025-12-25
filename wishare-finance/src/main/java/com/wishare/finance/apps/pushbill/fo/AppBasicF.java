package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-21
 */
@Data
@ApiModel("支付申请-基本信息")
public class AppBasicF {

    @ApiModelProperty(" 项目id")
    private String communityId;


}
