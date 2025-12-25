package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/6
 * @Description:
 */
@Getter
@Setter
@ApiModel("Ncc业务类型反参")
public class BusinessTypeRv {


    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String fcode;

    @ApiModelProperty("父名称")
    private String fname;

    @ApiModelProperty("所属集团")
    private String pk_group;

    @ApiModelProperty("所属业务单元")
    private String pk_org;

    @ApiModelProperty("启用状态")
    private String state;
}
