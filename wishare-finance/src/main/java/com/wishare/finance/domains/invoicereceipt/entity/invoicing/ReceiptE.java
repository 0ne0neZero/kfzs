package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.support.ListFileVoTypeHandler;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收据表(Receipt)实体类
 * 收据表(receipt)
 *
 * @author makejava
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
 * <p>
 * <p>
 * {@link com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto}
 * @since 2022-09-23 11:28:49
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "receipt", autoResultMap = true)
public class ReceiptE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 缴费时间
     */
    private LocalDateTime paymentTime;
    /**
     * 缴费方式
     */
    private String paymentType;
    /**
     * 图章url
     */
    private String stampUrl;
    /**
     * 收据pdf url地址
     */
    private String receiptUrl;


    @ApiModelProperty(value = "是否需要签章：0 - 是 1 - 否")
    private Integer signStatus;

    @ApiModelProperty(value = "0 - 申请签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署） 8 - 未知（调用服务异常等）")
    private Integer signSealStatus;

    @ApiModelProperty(value = "0 - 申请签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署） 8 - 未知（调用服务异常等）")
    private Integer signVoidStatus;


    @ApiModelProperty(value = "收据pdf url地址(盖章)")
    private String signReceiptUrl;

    @ApiModelProperty(value = "盖章申请编号(用来获取签章结果)")
    private String signApplyNo;

    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败,4:不需要")
    private Integer sendStatus;


    @ApiModelProperty(value = "最后推送时间")
    private LocalDateTime lastPushTime;

    @ApiModelProperty(value = "作废收据pdf url地址")
    private String voidPdf;

    @ApiModelProperty(value = "作废申请编号")
    private String voidSignApplyNo;

    @ApiModelProperty(value = "原文件集合")
    @TableField(value = "script_file_vos",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> scriptFileVos;

    @ApiModelProperty(value = "签章文件集合")
    @TableField(value = "sign_file_vos",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> signFileVos;

    @ApiModelProperty(value = "作废文件集合")
    @TableField(value = "void_file_vos",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> voidFileVos;

    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败 4:不需要")
    private Integer voidSendStatus;

    /**
     * 优惠信息[
     * {
     * "goodsName":"",
     * "price":"",
     * "num":"",
     * "totalPrice":"",
     * "remark":""
     * }
     * ]
     */
    private String discountInfo;
    /**
     * 收据号
     */
    private Long receiptNo;

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

