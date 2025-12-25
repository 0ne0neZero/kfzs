package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表审批信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractSpxxF {
    @ApiModelProperty("审批角色/岗位")
    private String examrole;

    @ApiModelProperty("经办人姓名")
    private String operator;

    @ApiModelProperty("审批时间")
    private String examdate;

    @ApiModelProperty("审批意见")
    private String examopinion;

    @ApiModelProperty("审批结论")
    private String examresult;

    @ApiModelProperty("审批类型")
    private String examtype;

    @ApiModelProperty("所属单位")
    private String operatorunitname;

    @ApiModelProperty("所属单位ID")
    private String operatorunitid;

    @ApiModelProperty("所属部门")
    private String operatordeptname;

    @ApiModelProperty("所属部门ID")
    private String operatordeptid;



}
