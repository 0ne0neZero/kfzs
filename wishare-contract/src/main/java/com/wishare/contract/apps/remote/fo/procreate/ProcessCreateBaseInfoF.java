package com.wishare.contract.apps.remote.fo.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author long
 * @date 2023年7月19日19:06:42
 */
@Data
@ApiModel(value = "流程创建请求基础数据实体")
public class ProcessCreateBaseInfoF {

    @ApiModelProperty(value = "流程创建人4A编码")
    private String USERID;

    @ApiModelProperty(value = "流程workflowid")
    private String WORKFLOWID;

    @ApiModelProperty(value = "流程名称")
    private String REQUESTNAME;

    @ApiModelProperty(value = "是否默认提交到第二节点")
    private String ISNEXTFLOW;
}
