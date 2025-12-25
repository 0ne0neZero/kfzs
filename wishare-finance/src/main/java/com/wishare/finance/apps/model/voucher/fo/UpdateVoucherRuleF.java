package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.entity.VoucherBook;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherScheduleRuleOBV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 凭证规则
 * @author dxclay
 * @since  2023/3/28
 * @version 1.0
 */
@Getter
@Setter
public class UpdateVoucherRuleF {

    @ApiModelProperty(value = "凭证规则id")
    @NotNull(message = "凭证规则id不能为空")
    private Long id;

    @ApiModelProperty(value = "触发事件类型：1应收计提，2收款结算，3预收应收核销，4账单调整，5账单开票，6冲销作废，7未认领暂收款，" +
            "8应付计提，9付款结算，10收票结算，11手动生成，12银行到账，13收入退款")
    private Integer eventType;

    @ApiModelProperty(value = "方案名称")
    private String ruleName;

    @ApiModelProperty(value = "推凭账簿模式：1指定账簿，2映射账簿")
    private Integer bookMode;

    @ApiModelProperty(value = "指定账簿编码")
    @Valid
    private VoucherBook book;

    @ApiModelProperty(value = "凭证模板id")
    private Long voucherTemplateId;

    @ApiModelProperty(value = "过滤条件")
    private List<VoucherRuleConditionOBV> conditions;

    @ApiModelProperty(value = "定时规则信息")
    private VoucherScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "推凭模式： 1定时推送  2即时推送， 3手动推凭")
    private Integer pushMode;

    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;

}
