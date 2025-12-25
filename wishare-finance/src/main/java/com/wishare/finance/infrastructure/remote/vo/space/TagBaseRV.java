package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author :lethe
 * @Date : 2022/11/17 16:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "标签", description = "标签")
public class TagBaseRV {

    @ApiModelProperty(value = "编号")
    private Integer code;

    @ApiModelProperty(value = "名称")
    private String name;

}
