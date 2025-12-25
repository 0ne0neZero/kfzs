package com.wishare.finance.apps.model.voucher.vo;

import com.wishare.finance.domains.voucher.entity.VoucherAccountBook;
import com.wishare.finance.domains.voucher.entity.VoucherChargeItemOBV;
import com.wishare.finance.domains.voucher.entity.VoucherCostCenterOBV;
import com.wishare.finance.domains.voucher.entity.VoucherStatutoryBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 推凭记录信息
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Getter
@Setter
@ApiModel("推凭记录信息")
public class VoucherRuleRecordV {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "凭证规则id")
    private Long voucherRuleId;

    @ApiModelProperty(value = "凭证规则名称")
    private String voucherRuleName;

    @ApiModelProperty(value = "凭证系统： 1用友NCC")
    private Integer voucherSystem;

    @ApiModelProperty(value = "账簿信息")
    private List<VoucherAccountBook> accountBooks;

    @ApiModelProperty(value = "法定单位信息")
    private List<VoucherStatutoryBody> statutoryBodys;

    @ApiModelProperty(value = "成本中心	[{ \"costCenterId\": 成本中心id,\"costCenterName\": \"成本中心名称\" }]")
    private List<VoucherCostCenterOBV> costCenters;

    @ApiModelProperty(value = "费项信息列表")
    private List<VoucherChargeItemOBV> chargeItems;

    @ApiModelProperty(value = "触发事件类型：1应收计提，2收款结算，3预收应收核销，4账单调整，5账单开票，6冲销作废，7未认领暂收款，8应付计提，9付款结算，10收票结算，11手动生成")
    private Integer eventType;

    @ApiModelProperty(value = "借方金额")
    private Long debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private Long creditAmount;

    @ApiModelProperty(value = "运行说明")
    private String remark;

    @ApiModelProperty(value = "执行状态：0待处理，1处理中，2处理完成，3处理失败")
    private Integer state;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}
