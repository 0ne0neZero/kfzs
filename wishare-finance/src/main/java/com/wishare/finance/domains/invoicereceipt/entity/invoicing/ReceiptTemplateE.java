package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: Linitly
 * @date: 2023/8/7 17:01
 * @descrption:
 *
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
 *
 * {@link com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV}
 */
@Getter
@Setter
@TableName(value = "receipt_template", autoResultMap = true)
public class ReceiptTemplateE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板类型：6:电子收据;
     */
    private Integer templateType;
    /**
     * 模板样式
     */
    private Integer templateStyle;
    /**
     * 启用电子签章：0:不启用;1:启用;
     */
    private Integer enableElectSign;
    /**
     * 电子签章类型：1:系统默认;2:手动上传;
     */
    private Integer electSignType;
    /**
     * 签章图片地址
     */
    @TableField(typeHandler = JSONTypeHandler.class)
    private FileVo signPictureUrl;
    /**
     * 是否启用：0启用，1禁用
     */
    private Integer disabled;
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
