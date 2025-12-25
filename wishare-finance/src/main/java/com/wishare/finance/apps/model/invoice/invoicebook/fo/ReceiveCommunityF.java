package com.wishare.finance.apps.model.invoice.invoicebook.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description:
 */
@Getter
@Setter
@ApiModel("领用项目信息")
public class ReceiveCommunityF {

    @ApiModelProperty(value = "领用项目id", required = true)
    @NotBlank(message = "领用项目id不能为空")
    private String communityId;

    @ApiModelProperty(value = "领用项目名称", required = true)
    @NotBlank(message = "领用项目名称不能为空")
    private String communityName;
}
