package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("跳收记录分页列表")
public class JumpRecordPageV {

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("房号名称集合")
    private String roomNames;

    @ApiModelProperty("费项名称集合")
    private String itemNames;

    @ApiModelProperty("跳收金额")
    private Long jumpAccount;

    @ApiModelProperty("任务状态：0-审核中,1-已审核,2-拒绝")
    private Integer state;

    @ApiModelProperty("成本中心id")
    private String reason;

    @ApiModelProperty("附件")
    private String file;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("跳收发起时间")
    private String createDate;

    @ApiModelProperty("跳收账单详情")
    private List<ReceivableBillDetailV> billDetailVList;

}
