package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dongpeng
 * @date 2023/7/26 14:36
 */
@Data
public class ShareChargeV {
    @ApiModelProperty(value = "分成费项编码id")
    private String shareChargeId;

    @ApiModelProperty(value = "分成费项编码名称")
    private String shareChargeName;
}
