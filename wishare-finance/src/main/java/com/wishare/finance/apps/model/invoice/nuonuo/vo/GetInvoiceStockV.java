package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("企业发票余量查询反参")
public class GetInvoiceStockV {

    @ApiModelProperty("分机号")
    private Integer extensionNumber;

    @ApiModelProperty("终端号")
    private String terminalNumber;

    @ApiModelProperty("发票种类：p:电子增值税普通发票 c:增值税普通发票(纸票) s:增值税专用发票 e:收购发票(电子) f :收购发票(纸质)r: 增值税普通发票(卷式)")
    private String invoiceLine;

    @ApiModelProperty("机器编号")
    private String machineCode;

    @ApiModelProperty("剩余数量")
    private Long remainNum;

    @ApiModelProperty("发票代码")
    private String typeCode;

    @ApiModelProperty("起始发票号码")
    private String invoiceNumStart;

    @ApiModelProperty("终止发票号码")
    private String invoiceNumEnd;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
