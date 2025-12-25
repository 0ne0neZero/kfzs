package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @author fxl
 * @describe
 * @date 2023/11/11
 */
@Setter
@Getter
@ApiModel("账单类型审核合计信息")
public class BillApproveTotalNewDto {

    @ApiModelProperty(value = "审核总数")
    private Long total;

    @ApiModelProperty(value = "初始审核")
    private Boolean isInit;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单")
    private Integer billType;

    public Long getTotal() {
        return Optional.ofNullable(this.total).orElse(0L);
    }
}
