package com.wishare.finance.apps.model.signature;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
* <p>
* 中交核算机构映射表
* </p>
*
* @author zhangfy
* @since 2023-12-11
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "中交核算机构映射表下拉列表视图对象", description = "中交核算机构映射表")
public class ExternalMaindataCalmappingListV {
    @ApiModelProperty("中交核算机构映射表下拉列表视图列表")
    private List<ExternalMaindataCalmappingV> infoList;
    @ApiModelProperty("偏移坐标，当下滑加载更多时，传入接口返回的该值作为请求参数之一")
    private Long indexId;
}
