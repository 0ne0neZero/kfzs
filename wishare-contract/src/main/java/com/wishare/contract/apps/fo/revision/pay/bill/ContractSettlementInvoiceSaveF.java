package com.wishare.contract.apps.fo.revision.pay.bill;

import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("收票明细")
public class ContractSettlementInvoiceSaveF implements Serializable {

    private static final long serialVersionUID = 1L;

    private String settlementId;

    @ApiModelProperty("ocr附件信息")
    private List<ZJFileVo> attachments;

    @ApiModelProperty("收票明细数据")
    private List<ContractSettlementInvoiceDetailF> invoiceDetails;

    @ApiModelProperty("其他附件数据")
    private ContractSettlementInvoiceOtherFileF otherFile;

}
