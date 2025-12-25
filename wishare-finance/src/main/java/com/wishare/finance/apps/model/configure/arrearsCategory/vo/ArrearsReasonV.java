package com.wishare.finance.apps.model.configure.arrearsCategory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@ApiModel("欠费原因返回信息")
public class ArrearsReasonV {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value ="欠费类别id")
    private Long arrearsCategoryId;

    @ApiModelProperty(value = "欠费类别名称")
    private String arrearsCategoryName;

    @ApiModelProperty("欠费原因")
    private String arrearsReason;

    @ApiModelProperty("更新人id")
    private String operator;

    @ApiModelProperty("更新人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

}
