package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 凭证分录辅助核算信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/7
 */
@Getter
@Setter
public class VoucherAssisteItem {

    @ApiModelProperty(value = "辅助核算编码", required = true)
    @NotBlank(message = "辅助核算编码不能为空")
    private String code;

    @ApiModelProperty(value = "辅助核算名称", required = true)
    @NotBlank(message = "辅助核算名称不能为空")
    private String name;

}
