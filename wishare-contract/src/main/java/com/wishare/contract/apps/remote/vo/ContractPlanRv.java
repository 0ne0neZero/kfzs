package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPlanRv {

    @ApiModelProperty("返回信息")
    private String msg;
    @ApiModelProperty("状态标识")
    private Integer code;
    @ApiModelProperty("返回信息")
    private List<ContractPlanDateRv> date;
}
