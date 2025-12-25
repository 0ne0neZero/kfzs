package com.wishare.finance.infrastructure.remote.vo.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("账单结转信息")
public class BillCarryoverRV {

    @ApiModelProperty(value = "主键id", required = true)
    private Long id;

    @ApiModelProperty(value = "结转账单id", required = true)
    private Long carriedBillId;

    @ApiModelProperty(value = "结转账单编号", required = true)
    private String carriedBillNo;

    @ApiModelProperty(value = "被结转账单id", required = true)
    private List<Long> targetBillIds;

    @ApiModelProperty(value = "被结转账单编号", required = true)
    private List<String> targetBillNos;

    @ApiModelProperty(value = "结转金额（单位：分）", required = true)
    private Long carryoverAmount;

    @ApiModelProperty(value = "结转方式：1抵扣，2结转预收", required = true)
    private Integer carryoverType;

    @ApiModelProperty(value = "审核记录id", required = true)
    private Long billApproveId;

    @ApiModelProperty(value = "申请结转时间", required = true)
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "结转时间")
    private LocalDateTime carryoverTime;

    @ApiModelProperty(value = "是否结转预收： 0不结转，1结转")
    private Integer advanceCarried;

    @ApiModelProperty(value = "结转的预收账单id")
    private Long advanceBillId;

    @ApiModelProperty(value = "结转的预收账单编号")
    private String advanceBillNo;

    @ApiModelProperty(value = "结转附件文件路径")
    private List<String> fileUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "结转明细列表")
    private List<CarryoverDetailRV> carryoverDetail;

    @ApiModelProperty(value = "结转状态：0待审核，1审核中，2已生效，3未生效", required = true)
    private Integer state;

    @ApiModelProperty(value = "创建人ID", required = true)
    private String creator;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private String creatorName;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID", required = true)
    private String operator;

    @ApiModelProperty(value = "修改人姓名", required = true)
    private String operatorName;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime gmtModify;

}
