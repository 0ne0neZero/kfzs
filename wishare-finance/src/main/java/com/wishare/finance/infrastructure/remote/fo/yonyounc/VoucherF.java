package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("保存凭证入参")
public class VoucherF {

    @ApiModelProperty("业务唯一id")
    private String id;

    @ApiModelProperty("保存凭证头")
    @Valid
    private VoucherHeadF voucher_head;
}
