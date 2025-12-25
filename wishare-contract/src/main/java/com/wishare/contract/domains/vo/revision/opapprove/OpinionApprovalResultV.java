package com.wishare.contract.domains.vo.revision.opapprove;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/15:41
 */
@Data
@ApiModel(value = "流程创建实体类")
public class OpinionApprovalResultV {

    @ApiModelProperty(value = "审批时间 yy-mm-dd hh:mm:ss格式")
    private String EXAMDATE;

    @ApiModelProperty(value = "所属部门")
    private String OPERATORDEPTNAME;

    @ApiModelProperty(value = "节点名称")
    private String EXAMROLE;

    @ApiModelProperty(value = "审批结论")
    private String EXAMRESULT;

    @ApiModelProperty(value = "所属部门ID")
    private String OPERATORDEPTID;

    @ApiModelProperty(value = "经办人姓名")
    private String OPERATOR;

    @ApiModelProperty(value = "经办人ID")
    private String OPERATORID;

    @ApiModelProperty(value = "审批意见")
    private String EXAMOPINION;

    @ApiModelProperty(value = "类型")
    private String LOGTYPE;

    @ApiModelProperty(value = "所属单位 ID")
    private String OPERATORUNITID;

    @ApiModelProperty(value = "所属单位")
    private String OPERATORUNITNAME;
}
