package com.wishare.finance.domains.voucher.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.apps.model.bill.fo.SceneF;
import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessDetail;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankInfoF;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 *
 * 业务单据表
 * @author luzhonghe
* @TableName business_bill
*/
@Getter
@Setter
@ApiModel(value = "业务单据表")
@TableName(value = TableNames.BUSINESS_BILL, autoResultMap = true)
public class BusinessBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 单据id
    */
    @ApiModelProperty("单据id")
    @TableId
    private Long billId;
    /**
    * 单据标题
    */
    @ApiModelProperty("单据标题")
    private String title;
    /**
    * 业务单据id
    */
    @ApiModelProperty("业务单据id")
    private String businessId;
    /**
    * 业务单据编号（用与前端页面展示）
    */
    @ApiModelProperty("业务单据编号（用与前端页面展示）")
    private String businessNo;
    /**
    * 单据类型
    */
    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty(value = "单据类型编码")
    private String billTypeCode;
    /**
    * 实际付款时间
    */
    @ApiModelProperty("实际付款时间（通用表单）")
    private LocalDate payTime;
    /**
    * 归属月
    */
    @ApiModelProperty("归属月")
    private String billMonth;
    /**
    * 归属年
    */
    @ApiModelProperty("归属年")
    private Year billYear;
    /**
    * 单据开始时间
    */
    @ApiModelProperty("单据开始时间")
    private LocalDateTime startTime;
    /**
    * 单据结束时间
    */
    @ApiModelProperty("单据结束时间")
    private LocalDateTime endTime;
    /**
    * 支付状态：0待支付，1支付中，2已支付，3支付失败
    */
    @ApiModelProperty("支付状态：0待支付，1支付中，2已支付，3支付失败")
    private Integer payState;
    /**
    * 来源系统：101BPM系统
    */
    @ApiModelProperty("来源系统：102BPM系统")
    private Integer sysSource;
    /**
    * 来源说明
    */
    @ApiModelProperty("来源说明")
    private String source;
    /**
    * 单据描述
    */
    @ApiModelProperty("单据描述")
    private String description;
    /**
    * 制单人编码
    */
    @ApiModelProperty("制单人编码")
    private String makerCode;
    /**
    * 制单人名称
    */
    @ApiModelProperty("制单人名称")
    private String makerName;

    @ApiModelProperty(value = "凭证id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> voucherIds;
    /**
    * 推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败
    */
    @ApiModelProperty("推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败")
    private Integer voucherState;

    @ApiModelProperty("记账会计")
    private String accountant;

    @ApiModelProperty("财务出纳")
    private String casher;

    @ApiModelProperty("申请人")
    private String applicant;

    @ApiModelProperty("申请日期")
    private LocalDate applyTime;

    @ApiModelProperty("付款时间(与payment交互)")
    private LocalDateTime actualPayTime;

    @ApiModelProperty("支付信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private ProcessBankInfoF bankInfo;

    @ApiModelProperty("支付场景信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private SceneF scene;

    @ApiModelProperty("消息通知地址")
    private String notifyUrl;

    @ApiModelProperty("扩展参数")
    private String attachParam;

    @ApiModelProperty("推凭标识：0不推凭，1推凭")
    private String voucherFlag;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    public void init() {
        if (Objects.isNull(billId)) {
            billId = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BUSINESS_BILL);
        }
    }
}
