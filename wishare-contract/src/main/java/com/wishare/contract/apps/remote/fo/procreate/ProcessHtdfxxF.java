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
public class ProcessHtdfxxF {

    @ApiModelProperty(value = "单位类型")
    private String ISOTHEROWN;

    @ApiModelProperty(value = "签约方名称")
    private String UNITNAME;

    @ApiModelProperty(value = "是否有授权人")
    private String ISSQR;

    @ApiModelProperty(value = "授权人姓名")
    private String AUTHORIZEDNAME;

    @ApiModelProperty(value = "授权人身份证号")
    private String idCard;

}
