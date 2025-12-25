package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.invoicereceipt.entity.base.FinanceBaseEntity;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.*;

import java.util.List;

/**
 * 收据推送记录表
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper=false)
@TableName(value = TableNames.RECEIPT_SEND_LOG, autoResultMap = true)
public class ReceiptSendLogE extends FinanceBaseEntity {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;

    /**
     * 发送结果0：成功 1：失败
     */
    private Integer sendResult;

    /**
     * 日志信息
     */
    private String message;

    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<Integer> pushModes;

}

