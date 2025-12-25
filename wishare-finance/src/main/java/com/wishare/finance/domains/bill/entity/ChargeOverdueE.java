package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* 
* @TableName charge_overdue
*/
@TableName(value = TableNames.CHARGE_OVERDUE)
public class ChargeOverdueE implements Serializable {

    /**
    * 
    */

    @TableId
    @NotNull(message="[]不能为空")
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

    @ApiModelProperty("是否违约即生成账单（true 是 false 否）")
    @TableField(exist = false)
    private boolean generateLateFeeBillDefaultFlag;

    public boolean isGenerateLateFeeBillDefaultFlag() {
        return generateLateFeeBillDefaultFlag;
    }

    public void setGenerateLateFeeBillDefaultFlag(boolean generateLateFeeBillDefaultFlag) {
        this.generateLateFeeBillDefaultFlag = generateLateFeeBillDefaultFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOverdueNo() {
        return overdueNo;
    }

    public void setOverdueNo(String overdueNo) {
        this.overdueNo = overdueNo;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getBillCreateState() {
        return billCreateState;
    }

    public void setBillCreateState(Integer billCreateState) {
        this.billCreateState = billCreateState;
    }

    public Integer getBillSettleState() {
        return billSettleState;
    }

    public void setBillSettleState(Integer billSettleState) {
        this.billSettleState = billSettleState;
    }

    public Long getRefBillId() {
        return refBillId;
    }

    public void setRefBillId(Long refBillId) {
        this.refBillId = refBillId;
    }

    public String getRefBillNo() {
        return refBillNo;
    }

    public void setRefBillNo(String refBillNo) {
        this.refBillNo = refBillNo;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getChargeItemId() {
        return chargeItemId;
    }

    public void setChargeItemId(Long chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    public String getChargeItemName() {
        return chargeItemName;
    }

    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Long overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public LocalDateTime getOverdueBeginDate() {
        return overdueBeginDate;
    }

    public void setOverdueBeginDate(LocalDateTime overdueBeginDate) {
        this.overdueBeginDate = overdueBeginDate;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(LocalDateTime gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getOverdueEndDate() {
        return overdueEndDate;
    }

    public void setOverdueEndDate(LocalDateTime overdueEndDate) {
        this.overdueEndDate = overdueEndDate;
    }
}
