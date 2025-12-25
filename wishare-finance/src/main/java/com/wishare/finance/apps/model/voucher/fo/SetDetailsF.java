package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/5/31
 */
@Getter
@Setter
@ApiModel(value="设置凭证明细", description="设置凭证明细")
public class SetDetailsF {

    @NotNull
    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @NotEmpty
    @ApiModelProperty(value = "凭证明细")
    private List<VoucherDetailOBV> details;

}
