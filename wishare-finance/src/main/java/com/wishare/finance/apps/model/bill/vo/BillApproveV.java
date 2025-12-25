package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单审核记录")
public class BillApproveV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账单ID")
    private Long billId;

    @ApiModelProperty("外部审批标识")
    private String outApproveId;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("审核原因")
    private String reason;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer operateType;

    @ApiModelProperty("账单类型（1:应收账单，2:预收账单，3:临时收费账单）")
    private Integer billType;

    @ApiModelProperty("审核状态（1审核中，2已审核，3未通过）")
    private Integer approvedState;

    @ApiModelProperty("审核结果说明")
    private String approvedRemark;

    @ApiModelProperty("上次审批的状态")
    private Integer lastApproveState;

    @ApiModelProperty("审核类型： 0内部审核，1外部审核")
    private Integer approveType;

    @ApiModelProperty("扩展字段1(根据业务需要自行设置)")
    private String extField1;

    /**
     * 备注
     */
    private String remark;
}
