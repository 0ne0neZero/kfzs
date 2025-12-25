package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@TableName("voucher_bill_ZJ")
public class VoucherBillZJE {

    @ApiModelProperty(value = "汇总单据id")
    private Long id;
    @ApiModelProperty(value = "规则id")
    private Long rule_id;
    @ApiModelProperty(value = "推送状态  1待推送 2已推送 3 推送失败")
    private Integer push_state;
    @ApiModelProperty(value = "报账单号")
    private String voucher_bill_no;
    @ApiModelProperty(value = "财务单号")
    private String finance_no;
    @ApiModelProperty(value = "业务单元名称")
    private String business_unit_name;
    @ApiModelProperty(value = "成本中心名称")
    private String cost_center_name;

    @ApiModelProperty(value = "报账总金额")
    private Long total_amount;
    @ApiModelProperty(value = "推送方式枚举(1手动推送、2按日推送)")
    private Integer push_method;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    @ApiModelProperty(value = "单据备注")
    private String receiptRemark;

    @ApiModelProperty(value = "到期日期")
    private LocalDateTime gmtExpire;
}
