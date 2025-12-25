package com.wishare.finance.domains.voucher.support.fangyuan.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL, autoResultMap = true)
public class VoucherBill extends BaseEntity {

    @ApiModelProperty(value = "汇总单据id")
    private Long id;
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    @ApiModelProperty(value = "报账类型")
    private String ruleName;
    @ApiModelProperty(value = "推送状态  1待推送 2已推送 3 推送失败 4 推送中")
    private Integer pushState;
    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;
    @ApiModelProperty(value = "是报账单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "财务单号")
    private String financeNo;
    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "报账总金额")
    private Long totalAmount;
    @ApiModelProperty(value = "推送方式枚举(1手动推送、2按日推送)")
    private Integer pushMethod;

    @ApiModelProperty(value = "备注")
    @TableField(typeHandler = JSONTypeHandler.class)
    private String remark;
    @ApiModelProperty(value = "财务单号")
    private String ncFinanceNo;
    @ApiModelProperty(value = "对账id")
    private Long reconciliationId;
    
    public void init(BillRule rule, Long costCenterId, String costCenterName) {
        setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        setRuleId(rule.getId());
        setRuleName(TriggerEventBillTypeEnum.valueOfByCode(rule.getEventType()).name());
        setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbill", "hxbz", 20));
        setFinanceNo(IdentifierFactory.getInstance().serialNumber("pushbill", "YSWY", 20));
        setPushState(PushBillTypeEnum.待推送.getCode());
        setInferenceState(InferenceStateEnum.已推凭.getCode());
        setPushMethod(rule.getPushMode());
        setCostCenterId(costCenterId);
        setCostCenterName(costCenterName);
    }
}
