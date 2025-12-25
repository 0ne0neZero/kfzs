package com.wishare.finance.apps.pushbill.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value="汇总单据信息")
public class VoucherBillV {
    @ApiModelProperty(value = "汇总单据id")
    private Long id;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    @ApiModelProperty(value = "报账类型")
    private String ruleName;
    @ApiModelProperty(value = "推送状态  1待推送 2已推送 3 推送失败")
    private Integer pushState;
    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;
    @ApiModelProperty(value = "是报账单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "财务单号")
    private String financeNo;
    @ApiModelProperty(value = "业务单元code")
    private String businessUnitCode;
    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;
    @ApiModelProperty(value = "错误信息")
    private String remark;
    @ApiModelProperty(value = "报账总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "推送方式枚举(1手动推送、2按日推送)")
    private Integer pushMethod;
    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic
    private Integer deleted;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
    @ApiModelProperty(value = "推单明细")
    private List<VoucherBillDetailV> voucherBillDetailVS;
}
