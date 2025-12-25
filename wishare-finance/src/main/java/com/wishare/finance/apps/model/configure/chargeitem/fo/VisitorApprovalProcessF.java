package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("访客邀约发起OA审批入参")
public class VisitorApprovalProcessF {
    private String communityName;
    private String mainDataId;
    private Integer reviewStatus;
    /** 邀约人 */
    private String sqr;
    /** 邀约人名称 */
    private String sqrName;

    @ApiModelProperty("所属部门")
    private String ssbm;
    /** 申请日期 */
    private String sqrq;
    /** 到访日期 */
    private String  rzrq;

    @ApiModelProperty("附件")
    private String fj;

    @ApiModelProperty("备注")
    private String bz;

    @ApiModelProperty("所属分部")
    private String sqrfb;

    @ApiModelProperty("到访单位")
    private String dfdw;

    @ApiModelProperty("离开日期")
    private String lkrq;

    @ApiModelProperty("被邀约人")
    private String byyr;

    /** 是否驾车 0是 1否 */
    private Integer sfjc;
    /** 是否就餐 0是 1否 */
    private Integer sfjc1;
}
