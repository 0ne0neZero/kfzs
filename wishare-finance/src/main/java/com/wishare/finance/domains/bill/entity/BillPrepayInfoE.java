package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoE
 * @date 2023.11.08  10:00
 * @description 账单预支付信息 主表
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_PREPAY_INFO, autoResultMap = true)
public class BillPrepayInfoE {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 账单类型：1.应收账单 2.预收账单 3.临时缴费账单
     */
    private Integer billType;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 账单状态（支付状态：0未支付，1支付中，2支付成功，3支付取消，4支付失败,5支付超时）
     */
    private Integer payState;
    /**
     * 支付生效时间
     */
    private LocalDateTime startTime;
    /**
     * 支付过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 支付失败原因
     */
    private String reason;
    /**
     * 关联二维码url
     */
    private String qrCodeUrl;
    /**
     * 商户订单号
     */
    private String mchOrderNo;
    /**
     * 支付来源
     * 支付来源: 0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序
     * 10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，
     * 14-亿管家智能POS机，15-亿家生活公众号物管端
     */
    private Integer paySource;
    /**
     * 上级收费单元id
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String supCpUnitId;
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
