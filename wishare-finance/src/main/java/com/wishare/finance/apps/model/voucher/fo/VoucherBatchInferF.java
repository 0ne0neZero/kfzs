package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/21 15:14
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("批量同步请求接口")
public class VoucherBatchInferF {

    private List<Long> voucherIds;
}
