package com.wishare.contract.apps.remote.fo.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/11:09
 */
@Data
@ApiModel(value = "业务数据实体类")
public class ProcessFkdwxxF {

    @ApiModelProperty(value = "支出方名称")
    private String DRAWEE;

    @ApiModelProperty(value = "应付合同金额")
    private String DRAWEEPAYAM;

    @ApiModelProperty(value = "实际付款人")
    private String TRUEDRAWEE;

}
