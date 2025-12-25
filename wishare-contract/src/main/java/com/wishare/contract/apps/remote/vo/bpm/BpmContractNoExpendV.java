package com.wishare.contract.apps.remote.vo.bpm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@ApiModel("bpm申请参数")
public class BpmContractNoExpendV {

    @ApiModelProperty(value = "是否发起成功")
    private boolean successFlag;

    @ApiModelProperty(value = "本次审批流id")
    private String resId;

    @ApiModelProperty(value = "字表里的id，上传附件要用")
    private String uuid;

    @ApiModelProperty(value = "失败异常")
    private String errorMsg;

    @ApiModelProperty(value = "合同单id", required = true)
    private Long gatherBillId;

    @ApiModelProperty("bpm的Boid")
    private String bpmBoUuId;
//
//    @ApiModelProperty("损益计划bpmBoUuId集合")
//    private List<String> bpmLossPlanUuIdList;
//
//    @ApiModelProperty("收款计划bpmBoUuId集合")
//    private List<String> bpmCollectionPlanUuIdList;

}
