package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 应收、 临时、收款单 勾选标记推送入参
 */
@Setter
@Getter
@ApiModel("应收、 临时、收款单 勾选标记推送入参")
public class BillFlagF {

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "账单、收款单id")
    @NotNull(message = "未选择数据")
    private List<Long> billIdList;

    @ApiModelProperty(value = "0 取消标记, 1 添加标记")
    @NotNull(message = "标记类型不能为空")
    private Integer flag;

}
