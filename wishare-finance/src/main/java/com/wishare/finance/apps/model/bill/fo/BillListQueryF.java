package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单列表查询")
public class BillListQueryF {

    @ApiModelProperty(value = "房号id",required = true)
    @NotBlank(message = "房号id不能为空")
    private String roomId;

    @ApiModelProperty("查询日期数组")
    private List<QueryDateF> queryDateList;
}
