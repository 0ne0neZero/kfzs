package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 凭证规则分录辅助核算信息
 */
@Getter
@Setter
public class VoucherTemplateEntryAssiste {

    @ApiModelProperty(value = "辅助核算编码")
    private String code;

    @ApiModelProperty(value = "辅助核算名称")
    private String name;

    @ApiModelProperty(value = "辅助核算名称对应编码")
    private String nameCode;

    @ApiModelProperty(value = "辅助核算项编码")
    private String ascCode;

    @ApiModelProperty(value = "辅助核算项名称")
    private String ascName;

}
