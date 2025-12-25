package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 15:24
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("根据结算方式和id获取账单id表单")
public class SettleChannelAndIdsF {

    /**
     * 账单id集合
     */
    @ApiModelProperty("账单id集合")
    private List<Long> billIds;


    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;
    /**
     * 其他参数条件
     */
    @ApiModelProperty("其他参数条件")
    private String params;

}
