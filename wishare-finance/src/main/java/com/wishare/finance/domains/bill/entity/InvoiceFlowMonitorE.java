package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.starter.beans.BaseFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 开票流程监控表
 * </p>
 *
 * @author dxclay
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
@TableName(value = TableNames.INVOICE_FLOW_MONITOR, autoResultMap = true)
@ApiModel(value = "开票流程监控表")
public class InvoiceFlowMonitorE extends BaseFields {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "收据id")
    private Long receiptId;

    @ApiModelProperty(value = "开票id")
    private Long invoiceId;

    @ApiModelProperty(value = "账单ids")
    private String billIds;

    @ApiModelProperty(value = "发票明细详情")
    private String invoiceReceiptDetailEList;


    @ApiModelProperty(value = "步骤类型")
    private Integer stepType;

    @ApiModelProperty(value = "步骤名称")
    private String stepDescription;

    @ApiModelProperty(value = "开收据入参")
    private String receiptParameters;

    @ApiModelProperty(value = "开发票入参")
    private String invoiceParameters;

    @ApiModelProperty(value = "错误信息")
    private String remark;


}
