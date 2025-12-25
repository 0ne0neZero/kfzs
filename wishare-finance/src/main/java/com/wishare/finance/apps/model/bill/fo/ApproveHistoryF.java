package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取审核历史记录入参")
public class ApproveHistoryF {

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("外部审批标识id")
    private String outApproveId;

    @ApiModelProperty("账单id集合")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    // @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
