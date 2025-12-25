package com.wishare.finance.domains.invoicereceipt.command.invociebook;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description: 新增票本command
 */
@Getter
@Setter
public class AddInvoiceBookCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 票本编号
     */
    private String invoiceBookNumber;
    /**
     * 票据类型:1增值税普通发票，2增值税专用发票，3纸质收据
     */
    private Integer type;
    /**
     * 购入组织id
     */
    private Long buyOrgId;
    /**
     * 购入组织名称
     */
    private String buyOrgName;
    /**
     * 购入数量
     */
    private Long buyQuantity;
    /**
     * 购入日期
     */
    private LocalDate buyDate;
    /**
     * 票面金额
     */
    private Long denomination;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 票本起始号码
     */
    private String invoiceStartNumber;
    /**
     * 票本结束号码
     */
    private String invoiceEndNumber;
    /**
     * 票本状态：1.未领用 2.部分领用 3.全部领用
     */
    private Integer state;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 创建人ID
     */
    private String creator;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;
}
