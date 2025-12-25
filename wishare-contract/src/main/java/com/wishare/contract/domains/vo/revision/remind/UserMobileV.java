package com.wishare.contract.domains.vo.revision.remind;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户手机信息")
public class UserMobileV implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户手机号")
    private String mobileNum;

}
