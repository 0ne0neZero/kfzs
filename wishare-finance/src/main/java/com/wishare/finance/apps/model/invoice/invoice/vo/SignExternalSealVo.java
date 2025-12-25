package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignExternalSealVo {

    @ApiModelProperty(value = "pdf文件")
    FileVo fileVo;

    @ApiModelProperty(value = "是否使用批量 true 是 false 否")
    boolean batchFlag;

    @ApiModelProperty(value = "pdf文件")
    List<FileVo> fileVos;

    @ApiModelProperty("签章状态0：关闭 1：开启")
    Integer signStatus;

    @ApiModelProperty("发票主表信息")
    InvoiceReceiptE invoiceReceiptE;

    @ApiModelProperty("地址前缀")
    String fileHost;

    @ApiModelProperty("收据表信息")
    ReceiptE receiptE;

    @ApiModelProperty("部分参数")
    Map<String, Object> map;

}
