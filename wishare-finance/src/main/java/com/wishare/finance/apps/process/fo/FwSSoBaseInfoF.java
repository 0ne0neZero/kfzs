package com.wishare.finance.apps.process.fo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FwSSoBaseInfoF {

    @ApiModelProperty("单点页面标识 1支出 2收入 3支出结算")
    private String PAGETYPE;

    @ApiModelProperty("人员4A编码")
    private String USERCODE;

    @ApiModelProperty("单位编码")
    private String ORGCODE;

    @ApiModelProperty("部门编码")
    private String DEPTCODE;

    @ApiModelProperty("流程ID")
    private String requestId;


}
