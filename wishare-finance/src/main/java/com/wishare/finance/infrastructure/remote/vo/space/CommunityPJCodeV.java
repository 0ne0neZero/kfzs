package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPJCodeV {

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("项目PJ码(主数据)")
    private String communityCode;

}
