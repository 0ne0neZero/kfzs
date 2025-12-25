package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-16
 */
@Data
public class PreGenerateDetailF implements Serializable {

    @ApiModelProperty(value = "结算id")
    @NotNull(message = "结算id不能为空")
    private List<String> settlementIdList;

    @ApiModelProperty(value = "项目id")
    @NotEmpty(message = "项目id不能为空")
    private String communityId;
}
