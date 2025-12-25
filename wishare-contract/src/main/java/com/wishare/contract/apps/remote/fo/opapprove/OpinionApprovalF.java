package com.wishare.contract.apps.remote.fo.opapprove;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:46
 */
@Data
@ApiModel(value = "流程更新请求实体")
public class OpinionApprovalF {


    @ApiModelProperty(value = "请求头部信息")
    private OpinionApprovalHeadF IS_REQ_HEAD;


    @ApiModelProperty(value = "业务数据")
    private OpinionApprovalDataF IT_DATA;

}
