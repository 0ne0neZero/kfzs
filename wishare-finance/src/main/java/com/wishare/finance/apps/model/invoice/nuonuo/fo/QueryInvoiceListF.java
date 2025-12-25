package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票列表查询入参")
public class QueryInvoiceListF {

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "请求类型，默认是0；0为按照订单添加时间查询，1为按照开票时间查询", example = "0")
    @NotBlank(message = "请求类型不能为空")
    private String requestType;

    @ApiModelProperty(value = "若requestType=0，表示开票订单请求时间起始值 若requestType=1，表示开票时间起始值 格式：yyyy-MM-dd HH:mm:ss", example = "2022-08-08 00:00:00")
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty(value = "若requestType=0，表示开票订单请求时间结束值 若requestType=1，表示开票时间结束值 起始时间和结束时间间隔不能大于10天 格式：yyyy-MM-dd HH:mm:ss 时间区间前闭后开", example = "2022-08-10 00:00:00")
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "当前页", example = "1",required = true)
    @NotBlank(message = "当前页不能为空")
    private String pageNo;

    @ApiModelProperty(value = "每页条数", example = "10",required = true)
    @NotBlank(message = "每页条数不能为空")
    private String pageSize;
}
