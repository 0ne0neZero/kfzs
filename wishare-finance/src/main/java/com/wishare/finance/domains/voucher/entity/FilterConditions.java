package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author szh
 * @date 2024/4/19 16:08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterConditions {

    @ApiModelProperty(value = "前端回显用")
    private String name;
    @ApiModelProperty(value = "BPM凭证模板过滤条件")
    private List<VoucherTemplateFilterItem> filterItems;
}
