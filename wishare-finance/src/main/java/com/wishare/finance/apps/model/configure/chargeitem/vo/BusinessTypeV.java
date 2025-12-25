package com.wishare.finance.apps.model.configure.chargeitem.vo;

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
@ApiModel("业务类型")
public class BusinessTypeV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("code")
    private String code;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("fcode")
    private String fcode;

    @ApiModelProperty("fname")
    private String fname;

    @ApiModelProperty("pk_group")
    private String pkGroup;

    @ApiModelProperty("pk_org")
    private String pkOrg;

    @ApiModelProperty("状态")
    private String state;




}
