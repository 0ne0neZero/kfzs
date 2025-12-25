package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 远洋主数据与ncc客商对照表
 */
@Getter
@Setter
@ApiModel(value = "远洋主数据与ncc客商对照表-更新")
public class YyNccCustomerRelUpdateF implements Serializable {
    @ApiModelProperty("主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty("客商（NCC原名)")
    @Length(max = 100, message = "NCC客商名称最大长度100位")
    @NotBlank(message = "客商（NCC原名)不能为空")
    private String customerName;

    @ApiModelProperty("社会信用代码")
    @NotBlank(message = "社会信用代码不能为空")
    private String creditCode;

    @ApiModelProperty("NCC客户编码")
    @Length(max = 100, message = "NCC客户编码最大长度100位")
    @NotBlank(message = "NCC客户编码不能为空")
    private String nccCustomerCode;
}
