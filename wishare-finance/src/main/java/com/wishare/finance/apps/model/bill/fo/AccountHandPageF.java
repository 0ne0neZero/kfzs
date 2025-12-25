package com.wishare.finance.apps.model.bill.fo;

import com.wishare.starter.beans.PageF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pgq
 * @since  2022-09-26
 * @Description:
 */
@Getter
@Setter
@ApiModel("分页查询交账列表")
public class AccountHandPageF extends PageF {

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    private Integer billType;
}
