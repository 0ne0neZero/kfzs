package com.wishare.finance.domains.invoicereceipt.command.invociebook;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiveDetailedStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveDetailedE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description: 新增发票领用command
 */
@Getter
@Setter
public class AddInvoiceReceiveCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 领用票本id
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
     * 领用时间
     */
    private LocalDateTime receiveTime;
    /**
     * 发票起始号码
     */
    private Long invoiceStartNumber;
    /**
     * 发票起始号码
     */
    private Long invoiceEndNumber;
    /**
     * 领用数量
     */
    private Long receiveNumber;
    /**
     * 项目信息
     */
    private String community;
    /**
     * 租户id
     */
    private String tenantId;
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


    /**
     * 构建发票领用实体
     *
     * @return
     */
    public InvoiceReceiveE getInvoiceReceiveE() {
        InvoiceReceiveE invoiceReceiveE = Global.mapperFacade.map(this, InvoiceReceiveE.class);
        invoiceReceiveE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receive_id"));
        return invoiceReceiveE;
    }

    /**
     * 构建发票领用明细实体
     *
     * @param invoiceReceiveE
     * @param command
     * @param invoiceBookE
     * @return
     */
    public List<InvoiceReceiveDetailedE> getInvoiceReceiveDetailedE(InvoiceReceiveE invoiceReceiveE, AddInvoiceReceiveCommand command, InvoiceBookE invoiceBookE) {
        List<InvoiceReceiveDetailedE> detailedES = Lists.newArrayList();
        for (int i = 0; i < this.receiveNumber; i++) {
            InvoiceReceiveDetailedE invoiceReceiveDetailedE = new InvoiceReceiveDetailedE();
            invoiceReceiveDetailedE.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receive_detailed_id"));
            invoiceReceiveDetailedE.setInvoiceReceiveId(invoiceReceiveE.getId());
            invoiceReceiveDetailedE.setInvoiceNum(invoiceStartNumber++);
            invoiceReceiveDetailedE.setState(InvoiceReceiveDetailedStateEnum.待使用.getCode());
            invoiceReceiveDetailedE.setInvoiceType(invoiceBookE.getType());
            detailedES.add(invoiceReceiveDetailedE);
        }
        return detailedES;
    }


}
