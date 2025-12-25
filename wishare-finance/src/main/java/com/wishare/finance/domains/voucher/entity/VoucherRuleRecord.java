package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.voucher.support.ListVoucherAccountBookHandler;
import com.wishare.finance.domains.voucher.support.ListVoucherChargeItemHandler;
import com.wishare.finance.domains.voucher.support.ListVoucherCostCenterTypeHandler;
import com.wishare.finance.domains.voucher.support.ListVoucherStatutoryBodyHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 凭证规则运行记录表
 * </p>
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Getter
@Setter
@TableName(value = TableNames.VOUCHER_RULE_RECORD, autoResultMap = true)
@ApiModel(value="VoucherPushRecord对象", description="推凭记录表")
public class VoucherRuleRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "凭证规则id")
    private Long voucherRuleId;

    @ApiModelProperty(value = "凭证规则名称")
    private String voucherRuleName;

    @ApiModelProperty(value = "凭证系统： 1用友NCC")
    private Integer voucherSystem;

    @ApiModelProperty(value = "账簿信息")
    @TableField(typeHandler = ListVoucherAccountBookHandler.class)
    private List<VoucherAccountBook> accountBooks;

    @ApiModelProperty(value = "法定单位信息")
    @TableField(typeHandler = ListVoucherStatutoryBodyHandler.class)
    private List<VoucherStatutoryBody> statutoryBodys;

    @ApiModelProperty(value = "成本中心	[{ \"costCenterId\": 成本中心id,\"costCenterName\": \"成本中心名称\" }]")
    @TableField(typeHandler = ListVoucherCostCenterTypeHandler.class)
    private List<VoucherCostCenterOBV> costCenters;

    @ApiModelProperty(value = "费项信息列表")
    @TableField(typeHandler = ListVoucherChargeItemHandler.class)
    private List<VoucherChargeItemOBV> chargeItems;

    @ApiModelProperty(value = "触发事件类型：1应收计提，2收款结算，3预收应收核销，4账单调整，5账单开票，6冲销作废，7未认领暂收款，8应付计提，9付款结算，10收票结算，11手动生成")
    private Integer eventType;

    @ApiModelProperty(value = "借方金额")
    private Long debitAmount =0L;

    @ApiModelProperty(value = "贷方金额")
    private Long creditAmount=0L;

    @ApiModelProperty(value = "运行说明")
    private String remark;

    @ApiModelProperty(value = "执行状态：0待处理，1处理中，2处理完成，3处理失败")
    private Integer state;

    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    public VoucherRuleRecord() {
        if (Objects.isNull(id)) {
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_RULE_RECORD));
        }
    }

}
