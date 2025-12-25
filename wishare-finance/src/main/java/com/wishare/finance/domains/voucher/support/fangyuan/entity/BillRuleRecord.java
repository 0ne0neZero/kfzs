package com.wishare.finance.domains.voucher.support.fangyuan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL_RULE_RECORD, autoResultMap = true)
public class BillRuleRecord extends BaseEntity{

    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "推单规则id")
    private Long ruleId;

    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认5预收结转")
    private Integer eventType;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    @ApiModelProperty(value = "凭证系统 1 方圆")
    private Integer voucherSystem;
    @ApiModelProperty(value = "执行状态：0待处理，1处理中，2处理完成，3处理失败")
    private Integer state;
    @ApiModelProperty(value = "运行说明")
    private String remark;
}
