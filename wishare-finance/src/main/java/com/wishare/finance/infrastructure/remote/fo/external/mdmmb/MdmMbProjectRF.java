package com.wishare.finance.infrastructure.remote.fo.external.mdmmb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@ApiModel(value = "实体项目信息映射表请求参数", description = "实体项目信息映射表")
public class MdmMbProjectRF {

    @ApiModelProperty("主数据自增ID")
    private Long mdmId;
    @ApiModelProperty("主数据编码")
    private String mdmCode;
    @ApiModelProperty("远程系统业务数据ID")
    private String businessId;
    @ApiModelProperty("远程系统ID")
    private Long remoteSystemId;
    @ApiModelProperty("业务系统值")
    private String businessValue;
    @ApiModelProperty("查询表名")
    @NotBlank(message = "表名不可为空")
    private String tableName;

}
