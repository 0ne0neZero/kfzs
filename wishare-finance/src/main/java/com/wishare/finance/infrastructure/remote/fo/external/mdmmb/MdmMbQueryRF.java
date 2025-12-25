package com.wishare.finance.infrastructure.remote.fo.external.mdmmb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@ApiModel(value = "实体项目信息映射查询", description = "实体项目信息映射查询")
public class MdmMbQueryRF {

    @ApiModelProperty(value = "主数据编码", notes = "主数据编码和业务数据ID和业务系统值不可同时为空")
    private String mdmCode;
    @ApiModelProperty(value = "远程系统业务数据ID", notes = "主数据编码和业务数据ID和业务系统值不可同时为空")
    private String businessId;
    @ApiModelProperty(value = "远程系统ID", required = true)
    @NotNull(message = "远程系统ID不可为空")
    private Long remoteSystemId;
    @ApiModelProperty(value = "业务系统值", notes = "主数据编码和业务数据ID和业务系统值不可同时为空")
    private String businessValue;
    @ApiModelProperty(value = "查询表名", required = true)
    @NotBlank(message = "表名不可为空")
    private String tableName;
    @ApiModelProperty(value = "业务系统值",notes = "业务系统值")
    private String mdmId;
}
