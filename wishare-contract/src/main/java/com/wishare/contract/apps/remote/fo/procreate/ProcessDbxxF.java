package com.wishare.contract.apps.remote.fo.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/11:16
 */
@Data
@ApiModel(value = "业务数据实体类")
public class ProcessDbxxF {

    @ApiModelProperty(value = "担保类别")
    private String GUARANTYCLASSIFY;

    @ApiModelProperty(value = "担保形式")
    private String GUARANTYFORM;

    @ApiModelProperty(value = "担保比例")
    private String GUARANTYPROPORT;

    @ApiModelProperty(value = "退还条件")
    private String BACKRULE;


}
