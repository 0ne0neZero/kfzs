package com.wishare.finance.apps.model.bill.fo;

import com.wishare.tools.starter.fo.search.Field;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/25 20:37
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("批量查询账单推凭")
public class ListBillInferenceF {

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("事件类型")
    private Integer eventType;

    @ApiModelProperty("条件")
    private List<Field> fieldList;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("当前页")
    private long pageNum = 1;

    @ApiModelProperty("页大小")
    private long pageSize = 1000;
}
