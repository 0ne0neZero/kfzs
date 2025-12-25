package com.wishare.finance.apps.model.bill.fo;

import com.wishare.starter.beans.OrderBy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 中台自定义分页入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("分页参数")
public class BasePageF {

    @ApiModelProperty("页码")
    @NotNull(message = "页码不可为空")
    @Min(value = 1, message = "页码不能小于1")
    public long pageNum;

    @ApiModelProperty("每页条数")
    @NotNull(message = "每页条数不可为空")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 1000, message = "每页条数不能大于1000")
    public long pageSize;

    @ApiModelProperty("排序")
    public List<OrderBy> orderBy;

}
