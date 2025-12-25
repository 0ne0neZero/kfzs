package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 凭证账簿信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/7
 */
@Getter
@Setter
@ApiModel("凭证账簿信息")
public class VoucherBook {

    @ApiModelProperty(value = "账簿id")
    @NotNull(message = "账簿id不能为空")
    private Long bookId;

    @ApiModelProperty(value = "账簿编码")
    @NotBlank(message = "账簿编码不能为空")
    private String bookCode;

    @ApiModelProperty(value = "账簿名称")
    @NotBlank(message = "账簿名称不能为空")
    private String bookName;

}
