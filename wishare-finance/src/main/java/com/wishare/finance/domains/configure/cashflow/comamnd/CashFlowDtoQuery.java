package com.wishare.finance.domains.configure.cashflow.comamnd;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 查询现金流量信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/19
 */
@Getter
@Setter
public class CashFlowDtoQuery {

    @ApiModelProperty(value = "现金流量编码")
    private String code;

    @ApiModelProperty(value = "现金流量名称")
    private String name;

    @ApiModelProperty(value = "现金流量项目类型列表 1现金流入，2=现金流出，3=流入流出")
    private List<Integer> types;
}
