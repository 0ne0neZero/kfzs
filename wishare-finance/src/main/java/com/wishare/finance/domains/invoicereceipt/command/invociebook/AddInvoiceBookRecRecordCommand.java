package com.wishare.finance.domains.invoicereceipt.command.invociebook;

import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description: 新增票本领用记录command
 */
@Getter
@Setter
public class AddInvoiceBookRecRecordCommand {

    /**
     * 票本id
     */
    private Long receiveInvoiceBookId;

    /**
     * 领用组织id
     */
    private Long receiveOrgId;

    /**
     * 领用组织名称
     */
    private String receiveOrgName;

    /**
     * 领用人id
     */
    private String receiveUserId;

    /**
     * 领用人名称
     */
    private String receiveUserName;

    /**
     * 领用时间
     */
    private LocalDateTime receiveTime;
    /**
     * 领用数量
     */
    private Long receiveNumber;
    /**
     * 购买总数量
     */
    private Long buyQuantity;

    /**
     * 构建新增票本领用实体
     * buyQuantity 领用数量
     *
     * @return
     */
    public InvoiceReceiveE getInvoiceBookReceiveRecordE() {
        InvoiceReceiveE invoiceReceiveE = Global.mapperFacade.map(this, InvoiceReceiveE.class);
        invoiceReceiveE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_book_receive_record_id"));
        return invoiceReceiveE;
    }


}
