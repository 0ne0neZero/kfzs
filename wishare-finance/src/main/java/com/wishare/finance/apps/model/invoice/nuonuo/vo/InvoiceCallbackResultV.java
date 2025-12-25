package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/9/14 20:39
 */
@Getter
@Setter
@ApiModel("接口回调返回参数")
public class InvoiceCallbackResultV {

    @ApiModelProperty("状态编号")
    private String status;

    @ApiModelProperty("描述信息")
    private String message;

    public InvoiceCallbackResultV(String status, String message){
        this.status = status;
        this.message = message;
    }
}
