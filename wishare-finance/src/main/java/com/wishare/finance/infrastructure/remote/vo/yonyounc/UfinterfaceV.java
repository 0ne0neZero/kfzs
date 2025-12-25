package com.wishare.finance.infrastructure.remote.vo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("ufinterface反参")
public class UfinterfaceV {

    private String billtype;


    private String filename;

    private String isexchange;

    private String replace;

    private String roottag;

    private String successful;

    @ApiModelProperty("业务反参")
    private SendresultV sendresult;
}
