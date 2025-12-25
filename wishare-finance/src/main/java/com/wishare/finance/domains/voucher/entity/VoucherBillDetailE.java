package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 账单推凭数据
 * @author: pgq
 * @since: 2022/10/24 20:46
 * @version: 1.0.0
 */
@Setter
@Getter
@TableName("voucher_bill_detail")
public class VoucherBillDetailE {

    /**
     * 账单id
     */
    @TableId
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 凭证id
     */
    private Long voucherId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer type;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称中文
     */
    private String costCenterName;

    /**
     * 项目id
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 房号ID
     */
    private Long roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号
     */
    private String outBusNo;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 账单金额
     */
    private Long totalAmount;

    /**
     * 结算金额
     */
    private Long receivableAmount;

    /**
     * 违约金金额
     */
    private Long overdueAmount;

    /**
     * 实收减免金额
     */
    private Long discountAmount;

    /**
     * 结算金额
     */
    private Long settleAmount;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount;

    /**
     * 转结金额
     */
    private Long carriedAmount;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 退款金额
     */
    private Long invoiceAmount;

    /**
     * 费项类型 1收入 2支出
     */
    private String payeeId;

    /**
     * 费项类型 1收入 2支出
     */
    private String payeeName;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 增值税普通发票\r\n1: 增值税普通发票\r\n2: 增值税专用发票\r\n3: 增值税电子发票\r\n4: 增值税电子专票\r\n5: 收据\r\n6：电子收据\r\n7:纸质收据
     */
    private String invoiceType;

    /**
     * 收费对象属性（1个人，2企业）
     */
    private String payerLabel;

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

}
