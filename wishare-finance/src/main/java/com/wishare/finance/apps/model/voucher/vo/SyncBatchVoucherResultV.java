package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量同步结果
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Getter
@Setter
@ApiModel("批量同步结果")
public class SyncBatchVoucherResultV {

    @ApiModelProperty(value = "oa-url链接")
    private String oaUrl;

    @ApiModelProperty(value = "消息等级")
    private String level;

    @ApiModelProperty(value = "成功条数")
    private int successTotal;

    @ApiModelProperty(value = "错误条数")
    private int errorTotal;

    @ApiModelProperty(value = "错误凭证id列表")
    private List<Long> errorList;

}
