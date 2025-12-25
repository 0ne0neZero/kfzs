package com.wishare.finance.domains.bill.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title ChargeOverdueDto
 * @date 2023.11.28  16:42
 * @description
 */
@Getter
@Setter
@ApiModel("违约金明细")
public class ChargeOverdueDto {

    @ApiModelProperty("")
    private Long id;
    /**
     * 违约金编号
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("违约金编号")
    @Length(max= 40,message="编码长度不能超过40")
    private String overdueNo;
    /**
     * 违约金账单id
     */
    @ApiModelProperty("违约金账单id")
    private Long billId;
    /**
     * 违约金账单编号
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("违约金账单编号")
    @Length(max= 40,message="编码长度不能超过40")
    private String billNo;
    /**
     * 违约金账单生成状态
     */
    @ApiModelProperty("违约金账单生成状态")
    private Integer billCreateState;
    /**
     * 违约金账单结算状态
     */
    @ApiModelProperty("违约金账单结算状态")
    private Integer billSettleState;
    /**
     * 关联账单id
     */
    @ApiModelProperty("关联账单id")
    private Long refBillId;
    /**
     * 关联账单编号
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("关联账单编号")
    @Length(max= 40,message="编码长度不能超过40")
    private String refBillNo;
    /**
     * 项目ID
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("项目ID")
    @Length(max= 40,message="编码长度不能超过40")
    private String communityId;
    /**
     * 项目名称
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("项目名称")
    @Length(max= 100,message="编码长度不能超过100")
    private String communityName;
    /**
     * 房号ID
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("房号ID")
    @Length(max= 40,message="编码长度不能超过40")
    private String roomId;
    /**
     * 房号名称
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("房号名称")
    @Length(max= 100,message="编码长度不能超过100")
    private String roomName;
    /**
     * 费项id
     */
    @ApiModelProperty("费项id")
    private Long chargeItemId;
    /**
     * 费项名称
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("费项名称")
    @Length(max= 100,message="编码长度不能超过100")
    private String chargeItemName;
    /**
     * 收费对象ID
     */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("收费对象ID")
    @Length(max= 40,message="编码长度不能超过40")
    private String customerId;
    /**
     * 收费对象名称
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("收费对象名称")
    @Length(max= 100,message="编码长度不能超过100")
    private String customerName;
    /**
     * 违约金金额
     */
    @ApiModelProperty("违约金金额")
    private Long overdueAmount;
    /**
     * 违约金比率
     */
    @ApiModelProperty("违约金比率")
    private BigDecimal overdueRate;
    /**
     * 违约金起算日期
     */
    @ApiModelProperty("违约金起算日期")
    private LocalDateTime overdueBeginDate;

    @ApiModelProperty("违约金统计截止日期")
    private LocalDateTime overdueEndDate;

    @ApiModelProperty("删除状态：0 未删除，1 已删除")
    protected Byte deleted;

    @ApiModelProperty("租户id")
    @TableField(fill = FieldFill.INSERT)
    protected String tenantId;

    @ApiModelProperty("创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    protected LocalDateTime gmtCreate;
    @ApiModelProperty("最后修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    protected LocalDateTime gmtModify;

    @ApiModelProperty("操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String operator;


    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    protected String creator;

    @ApiModelProperty("违约金账单状态")
    private Integer state;

    @ApiModelProperty("违约金账单审核状态（1审核中，2已审核，3未通过）")
    private Integer approvedState;

    @ApiModelProperty("违约金账单核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty("违约金退款状态")
    private Integer refundState;

    @ApiModelProperty("违约金结转状态")
    private Integer carriedState;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("违约金账单是否冲销状态：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty(value = "违约金账单计费方式")
    private Integer billMethod;

    @ApiModelProperty("违约金账单付款方名称")
    private String payerName;

    @ApiModelProperty("违约金账单收费对象类型（0:业主，1开发商，2租客, 98临时客商）")
    private Integer payerType;


}
