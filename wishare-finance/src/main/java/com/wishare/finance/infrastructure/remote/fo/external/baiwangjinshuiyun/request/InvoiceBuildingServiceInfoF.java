package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 建筑服务信息  特定约束类型代码为03时必填
 * @author dongpeng
 * @date 2023/10/25 20:05
 */
@Data
@ApiModel("建筑服务信息")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceBuildingServiceInfoF {

    @ApiModelProperty(value = "土地增值税项目编号",required = true)
    private String tdzzsxmbh;

    @ApiModelProperty(value = "建筑服务发生地(省市区)",required = true)
    private String jzfwfsd;

    @ApiModelProperty(value = "详细地址",required = true)
    private String fullAddress;

    @ApiModelProperty(value = "建筑项目名称",required = true)
    private String jzxmmc;

    @ApiModelProperty(value = "跨地市标识(N 否Y是)",required = true)
    private String kdsbz;

    @ApiModelProperty(value = "前端回显用",required = true)
    private List<String> provinceCityCode;
}
