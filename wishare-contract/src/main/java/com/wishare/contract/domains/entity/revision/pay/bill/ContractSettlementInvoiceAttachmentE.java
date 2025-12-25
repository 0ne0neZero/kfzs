package com.wishare.contract.domains.entity.revision.pay.bill;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_settlement_invoice_attachment")
public class ContractSettlementInvoiceAttachmentE {

    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 结算单id
     */
    private String settlementId;

    /**
     * 影像返回的id
     */
    private String fileId;

    /**
     * 文件key
     */
    private String fileKey;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
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
     * 创建时间
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
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}
