package com.wishare.contract.apps.remote.fo.image;

import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceDetailF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("发票orc识别结果V")
public class InvoiceOcrResultV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发票文件信息")
    private ZJFileVo fileInfo;

    @ApiModelProperty("发票ocr识别结果")
    private List<ContractSettlementInvoiceDetailF> ocrInfos;

}
