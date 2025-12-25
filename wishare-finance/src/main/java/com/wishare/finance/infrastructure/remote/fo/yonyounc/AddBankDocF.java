package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 银行信息新增类
 * @author: pgq
 * @since: 2022/12/9 15:23
 * @version: 1.0.0
 */
@ApiModel("银行信息新增类")
@Valid
@Getter
@Setter
public class AddBankDocF {

    /**
     * 开户行编码
     */
    @ApiModelProperty("开户行编码")
    @JSONField(name = "code")
    @NotBlank(message = "开户行编码不能为空")
    @Size(max = 40, message = "开户行编码最大长度为40")
    private String code;

    /**
     * 开户行名称
     */
    @ApiModelProperty("开户行名称")
    @JSONField(name = "name")
    @NotBlank(message = "开户行名称不能为空")
    @Size(max = 200, message = "开户行名称最大长度为200")
    private String name;

    /**
     * 开户行简称
     */
    @ApiModelProperty("开户行简称")
    @JSONField(name = "shortname")
    @NotBlank(message = "开户行简称不能为空")
    @Size(max = 200, message = "开户行简称最大长度为200")
    private String shortname;

    /**
     * 银行类别编码
     */
    @ApiModelProperty("银行类别编码")
    @JSONField(name = "banktypecode")
    @NotBlank(message = "银行类别编码不能为空")
    @Size(max = 40, message = "银行类别编码最大长度为40")
    private String bankTypeCode;

    /**
     * 国家
     */
    @ApiModelProperty("国家")
    @JSONField(name = "pk_country")
    @NotBlank(message = "国家不能为空")
    @Size(max = 20, message = "国家最大长度为20")
    private String pkCountry;
}
