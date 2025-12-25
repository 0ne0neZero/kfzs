package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 凭证
 * @author: pgq
 * @since: 2022/10/24 19:41
 * @version: 1.0.0
 */
@Getter
@Setter
@TableName("voucher")
public class VoucherE {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 账单列表
     */
    private String billList;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 账单类型
     */
    private Integer billType;

    /**
     * 报账凭证编号
     */
    private String voucherNo;


    /**
     * 账单推凭记录
     */
    private String billInferIds;

    /**
     * 制单方式
     */
    private String madeType;

    /**
     * 凭证类别
     */
    private String voucherType;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 推凭金额 单位：分
     */
    private Long amount;

    /**
     * 推凭状态：0待同步，1成功，2失败
     */
    private Integer inferenceState;

    /**
     * 分录详情
     */
    private String details;

    /**
     * 预制凭证信息
     */
    private String prefabricationDetails;

    /**
     * 触发事件
     */
    private Integer evenType;

    /**
     * 原因
     */
    private String reason;

    /**
     * 制单人
     */
    private String maker;

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
