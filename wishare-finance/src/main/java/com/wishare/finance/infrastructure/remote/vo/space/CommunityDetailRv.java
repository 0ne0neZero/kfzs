package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/1/13
 * @Description:
 */
@Getter
@Setter
@ApiModel("空间中心通过项目id批量查询项目名称反参")
public class CommunityDetailRv {

    @ApiModelProperty("项目id")
    private String id;

    @ApiModelProperty("项目名称")
    private String name;
}
