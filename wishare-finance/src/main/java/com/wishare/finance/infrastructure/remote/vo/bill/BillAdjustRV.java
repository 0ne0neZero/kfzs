package com.wishare.finance.infrastructure.remote.vo.bill;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单调整信息
 * @Author dxclay
 * @Date 2022/8/28
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("账单调整信息")
public class BillAdjustRV {

    @ApiModelProperty(value = "调整id", required = true)
    private Long id;

    @ApiModelProperty(value = "调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他", required = true)
    private Integer adjustWay;

    @ApiModelProperty(value = "调整内容", required = true)
    private String content;

    @ApiModelProperty(value = "调整原因", required = true)
    private Integer reason;

    @ApiModelProperty(value = "原账单金额 (单位： 分)", required = true)
    private Long billAmount;

    @ApiModelProperty(value = "调整金额(减免时为负数，调高时为正数) (单位： 分)", required = true)
    private Long adjustAmount;

    @ApiModelProperty(value = "调整时拆单的账单编号")
    private String separateBillNo;

    @ApiModelProperty(value = "调整类型： 1减免，2调高，3调低")
    private Integer adjustType;

    @ApiModelProperty(value = "减免凭证号")
    private String voucher;

    @ApiModelProperty(value = "调整比例，区间[0.01, 100]")
    private BigDecimal adjustRatio;

    @ApiModelProperty(value = "审核记录id", required = true)
    private Long billApproveId;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "调整状态：0待审核，1审核中,2已生效，3未生效", required = true)
    private Integer state;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    @ApiModelProperty(value = "付款方ID")
    private String payerId;

    @ApiModelProperty(value = "付款方名称")
    private String payerName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "申请调整时间", required = true)
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "调整时间")
    private LocalDateTime adjustTime;

    @ApiModelProperty("调整附件信息")
    private List<FileVo> fileVos;

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
