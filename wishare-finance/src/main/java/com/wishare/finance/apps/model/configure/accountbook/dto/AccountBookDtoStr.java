package com.wishare.finance.apps.model.configure.accountbook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
public class AccountBookDtoStr {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "凭证系统")
    private Integer voucherSys;

    @ApiModelProperty(value = "账簿编码")
    private String code;

    @ApiModelProperty(value = "账簿名称")
    private String name;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty(value = "账簿组合描述")
    private List<String> accountBookGroup;

    @ApiModelProperty(value = "是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty(value = "系统来源列表")
    private List<Integer> sysSource;

}
