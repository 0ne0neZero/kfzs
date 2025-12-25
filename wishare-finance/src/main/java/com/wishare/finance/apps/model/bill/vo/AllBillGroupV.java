package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.dto.AllBillGroupDto;
import com.wishare.starter.beans.PageV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单分组信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("所有账单分组信息")
public class AllBillGroupV {

    @ApiModelProperty("账单数量")
    private Long billCount;

    @ApiModelProperty("账单分组信息")
    private PageV<AllBillGroupDto> billInfo;
}
