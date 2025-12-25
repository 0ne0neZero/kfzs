package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 收票信息
 * @author dxclay
 * @since  2023/2/21
 * @version 1.0
 */
@Getter
@Setter
@ApiModel("收票信息")
public class InvoiceCollectionF {

    @ApiModelProperty(value = "行政组织编码")
    private String orgCode;

    @ApiModelProperty(value = "行政组织名称")
    private String orgName;

    @ApiModelProperty(value = "法定单位id, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "同步系统： 101灵税通")
    private Integer syncSystem;

    @ApiModelProperty(value = "发票信息", required = true)
    @NotNull(message = "发票信息不能为空")
    @Valid
    private List<InvoiceInfoF> invoiceInfos;

}
