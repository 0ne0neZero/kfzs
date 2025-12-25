package com.wishare.finance.apps.model.configure.arrearsCategory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("搜索单选框查询返回体")
public class SearchV {
    @ApiModelProperty("key")
    private String label;

    @ApiModelProperty("value")
    private String value;


}
