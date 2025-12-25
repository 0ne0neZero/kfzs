package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("法定单位反参")
public class StatutoryBodySimpleV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("法定单位名称中文")
    private String nameCn;
}
