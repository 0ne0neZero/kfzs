package com.wishare.finance.apps.model.voucher.fo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.entity.CashFlowOBV;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import com.wishare.finance.domains.voucher.support.ListVoucherDetailTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value="编辑凭证明细入参")
public class UpdateVoucherDetailF {

    @ApiModelProperty(value = "凭证id")
    @NotNull(message = "凭证id不可以为空")
    private Long id;

    @ApiModelProperty(value = "分录详情")
    @TableField(typeHandler = ListVoucherDetailTypeHandler.class)
    private List<VoucherDetailOBV> details;


}
