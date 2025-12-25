package com.wishare.finance.apps.model.voucher.fo;

import java.time.YearMonth;
import java.util.List;

import com.wishare.finance.domains.voucher.entity.CloseAccountOBV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批量关账入参
 */
@Getter
@Setter
public class CloseAccountsF {

    @ApiModelProperty(value = "账簿信息")
    private List<CloseAccountOBV> closeAccounts;

    @ApiModelProperty(value = "关账月份区间")
    private List<YearMonth> period;
}
