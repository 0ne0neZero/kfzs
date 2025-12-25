package com.wishare.finance.domains.configure.cashflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class CashFlowDto {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父编码")
    private String fCode;

    @ApiModelProperty(value = "父名称")
    private String fName;

    @ApiModelProperty(value = "全路径id列表")
    private String path;

    @ApiModelProperty(value = "全路径名称")
    private String pathName;

    @ApiModelProperty(value = "现金流量项目类型 1现金流入，2=现金流出，3=流入流出")
    private String itemType;

}
