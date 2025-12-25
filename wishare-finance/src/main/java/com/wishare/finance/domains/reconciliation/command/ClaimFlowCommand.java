package com.wishare.finance.domains.reconciliation.command;

import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 认领流水命令
 *
 * @author yancao
 */
@Getter
@Setter
public class ClaimFlowCommand {

    @Deprecated
    @ApiModelProperty("系统来源：1收费 2合同")
    private Integer sysSource;

    @ApiModelProperty("认领类型(1:发票认领;2:账单认领;)")
    private Integer claimType;

    @ApiModelProperty("认领记录ID(修改时传入)")
    private Long getFlowClaimRecordId;

    @ApiModelProperty("领用金额")
    private Long claimAmount;

    @ApiModelProperty("实收金额")
    private Long settleAmount;

    @ApiModelProperty("发票id集合")
    private List<Long> invoiceIdList;

    @ApiModelProperty("流水id集合")
    private List<Long> flowIdList;

    @ApiModelProperty("票据总金额")
    private Long invoiceAmount;

    @ApiModelProperty("收入流水总金额")
    private Long flowIncomeFlowAmount = 0L;

    @ApiModelProperty("退款流水总金额")
    private Long flowRefundFlowAmount = 0L;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("发票信息")
    private List<InvoiceReceiptE> invoiceReceiptList;

    @ApiModelProperty("账单信息")
    private List<ContractFlowBillV> flowBillList;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("项目名称")
    private String supCpUnitName;

    @ApiModelProperty("日报表附件集合")
    private List<FileVo> reportFileVos;

    @ApiModelProperty("银行回单附件集合")
    private List<FileVo> flowFileVos;

    @ApiModelProperty("是否差额认领 0 差额认领 1或者其他 全额认领 若是差额认领必须传0")
    private Integer differenceFlag;

    @ApiModelProperty("差额认领原因 差额认领必传")
    private String differenceReason;

    @ApiModelProperty("差额认领备注")
    private String differenceRemark;
}
