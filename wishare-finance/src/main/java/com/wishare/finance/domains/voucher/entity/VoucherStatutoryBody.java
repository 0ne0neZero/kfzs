package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 凭证法定单位信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Getter
@Setter
public class VoucherStatutoryBody {

    @ApiModelProperty(value = "法定单位id")
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码")
    @NotBlank(message = "法定单位编码不能为空")
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称")
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    public VoucherStatutoryBody() {
    }

    public VoucherStatutoryBody(Long statutoryBodyId, String statutoryBodyCode, String statutoryBodyName) {
        this.statutoryBodyId = statutoryBodyId;
        this.statutoryBodyCode = statutoryBodyCode;
        this.statutoryBodyName = statutoryBodyName;
    }
}
