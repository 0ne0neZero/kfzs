package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("减免发起OA审批入参")
public class ReduceApprovalProcessF {
    // 所属区域
    private String areaName;
    private Long areaId;
    // 项目名称 xmmc
    private String communityName;
    // 减免原因 jmyy
    private String reason;
    // 减免金额 sqjmje
    private Long reductionTotalAmount;
    // 减免比例 jmzkbl
    private Double adjustRatio;
    private String mainDataId;
}
