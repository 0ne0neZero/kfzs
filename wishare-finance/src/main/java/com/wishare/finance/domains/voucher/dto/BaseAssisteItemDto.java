package com.wishare.finance.domains.voucher.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 辅助核算基础信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Setter
@Getter
@ApiModel("辅助核算基础信息")
public class BaseAssisteItemDto {

    @ApiModelProperty(value = "辅助核算编码")
    private String code;

    @ApiModelProperty(value = "辅助核算名称")
    private String name;

}
