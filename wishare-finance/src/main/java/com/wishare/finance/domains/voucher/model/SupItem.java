package com.wishare.finance.domains.voucher.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 辅助核算信息
 * @author: pgq
 * @since: 2023/2/16 16:55
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("辅助核算项")
public class SupItem {

    /**
     * 辅助核算code
     */
    @ApiModelProperty("辅助核算code")
    private String code;

    /**
     * 辅助核算名称
     */
    @ApiModelProperty("辅助核算名称")
    private String name;

    /**
     * 辅助核算类型
     */
    @ApiModelProperty("辅助核算类型")
    private String type;

    /**
     * 辅助核算具体值
     */
    @ApiModelProperty("辅助核算具体值")
    private String value;

    /**
     * 辅助核算具体值
     */
    @ApiModelProperty("辅助核算具体值")
    private String valueName;

}
