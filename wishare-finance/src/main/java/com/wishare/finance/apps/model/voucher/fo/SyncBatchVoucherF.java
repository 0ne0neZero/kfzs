package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.consts.enums.VoucherSystemEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量同步请求参数
 * @author dxclay
 * @since  2023/3/10
 * @version 1.0
 */
@Setter
@Getter
@ApiModel("批量同步请求参数")
public class SyncBatchVoucherF {

    @ApiModelProperty(value = "凭证id列表")
    @Size(min = 1, max = 10, message = "凭证同步条数仅允许1~10条")
    private List<Long> voucherIds;

    @ApiModelProperty(value = "同步系统")
    private Integer voucherSystem = VoucherSystemEnum.用友NCC.getCode();

}
