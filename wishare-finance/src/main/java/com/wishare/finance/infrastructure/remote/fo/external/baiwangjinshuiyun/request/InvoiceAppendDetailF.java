package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 附加要素明细
 * @author dongpeng
 * @date 2023/10/25 19:46
 */
@Data
@ApiModel("附加要素明细")
public class InvoiceAppendDetailF {

    @ApiModelProperty(value = "数据类型",required = true)
    private String sjlx;

    @ApiModelProperty(value = "场景模板名称")
    private String cjmbmc;

    @ApiModelProperty(value = "场景模板uuid")
    private String cjmbuuid;

    @ApiModelProperty(value = "附加要素项目uuid",required = true)
    private String fjysxmuuid;

    @ApiModelProperty(value = "附加要素项目名称",required = true)
    private String fjysxmmc;

    @ApiModelProperty(value = "附加要素名称",required = true)
    private String fjysmc;

    @ApiModelProperty(value = "附加要素值",required = true)
    private Date fjysz;
}
