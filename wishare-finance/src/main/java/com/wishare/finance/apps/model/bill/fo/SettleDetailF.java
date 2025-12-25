package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("根据账单ids获取应收账单结算详情")
public class SettleDetailF {

    @ApiModelProperty(value = "应收账单ids", required = true)
    @NotNull(message = "应收账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
