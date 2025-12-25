package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/9/28
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取可以领用发票号入参")
public class GetInvoiceReceiptNoF {

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "项目id不能为空")
    private String communityId;

    @ApiModelProperty(value = "票据类型 \n" +
            "1: 增值税普通发票\n" +
            "2: 增值税专用发票\n" +
            "3: 增值税电子发票\n" +
            "4: 增值税电子专票\n" +
            "5: 收据\n" +
            "6：电子收据\n" +
            "7:纸质收据" ,required = true)
    @NotNull(message = "票据类型不能为空")
    private Integer type;

    @ApiModelProperty("查询条数")
    private Long pageSize;


}
