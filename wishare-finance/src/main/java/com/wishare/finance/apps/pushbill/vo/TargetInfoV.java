package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TargetInfoV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "目标id")
    private String targetId;

    @ApiModelProperty(value = "目标名")
    private String targetName;

}
