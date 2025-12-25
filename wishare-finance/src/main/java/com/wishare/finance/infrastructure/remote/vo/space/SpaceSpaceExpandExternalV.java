package com.wishare.finance.infrastructure.remote.vo.space;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返回值
 * </p>
 *
 * @author zhenghui
 * @since 2023-04-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "空间扩展表信息返回值", description = "空间扩展表返回值")
public class SpaceSpaceExpandExternalV {

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("小区id")
    private String communityId;


    @ApiModelProperty("空间id")
    private Long spaceId;


    @ApiModelProperty("管理费标准")
    private String manageFeeStandard;


    @ApiModelProperty("产权人名称")
    private String propertyOwner;
}
