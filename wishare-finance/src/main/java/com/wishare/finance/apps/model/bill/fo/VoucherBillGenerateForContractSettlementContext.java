package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
 @Setter
 @ApiModel("报账单-实签-中交")
public class VoucherBillGenerateForContractSettlementContext {

    @ApiModelProperty("所属项目ID")
    private List<String> communityIdList;

    @ApiModelProperty("账单id")
    private List<String> billIdList;

    @ApiModelProperty(value = "触发事件类型：6:收入确认-实签")
    private Integer eventType;

    @ApiModelProperty(value = "结算单id-对应结算审批/确收审批")
    private String settlementId;

    @ApiModelProperty("结算单id")
    private List<String> settlementIdList;

    @ApiModelProperty(value = "是否保存报账单")
    private Boolean saveVoucher = false;

    @ApiModelProperty("业务id")
    private String bizId;

    @ApiModelProperty("推送部门code")
    private String externalDepartmentCode;


    //------- 业务支付申请单，生成合同报账单使用---------------
    @ApiModelProperty("业务事由")
    private String receiptRemark;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("附件张数")
    private Integer uploadNum;
    @ApiModelProperty(value = "流程id")
    private String processId;
}
