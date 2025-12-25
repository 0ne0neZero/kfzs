package com.wishare.contract.apps.remote.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
* <p>
* 
* </p>
*
* @author chenglong1
* @since 2023-07-031
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "下拉列表视图对象", description = "")
public class CustomerListV {
    @ApiModelProperty("下拉列表视图列表")
    private List<CustomerV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private Long indexId;
}
