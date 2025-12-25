package com.wishare.contract.apps.remote.fo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 安全校验节点
 *
 * @author 龙江锋
 * @date 2023/8/8 13:39
 */
@Data
@ApiModel("安全校验节点")
public class SafetyF {
    /**
     * 操作时间,
     * 格式：20190420140822
     */
    @ApiModelProperty("创建时间")
    @NotBlank(message = "操作时间不能为空")
    private String time;

    /**
     * 安全校验位，Time的值+clientcode的值+barcode的值+useraccount的值+密码, 整体生成MD5
     */
    @ApiModelProperty("安全校验位，Time的值+clientcode的值+barcode的值+useraccount的值+密码, 整体生成MD5")
    @Length(message = "安全校验位为固定的32位")
    private String ticket;

    /**
     * 分配给业务系统的账号
     */
    @ApiModelProperty("分配给业务系统的账号")
    @Length(message = "业务系统的账号不能超过50位", max = 50)
    private String clientcode;

    /**
     * 操作单号
     */
    @ApiModelProperty("操作单号")
    @Length(message = "操作单号不能超过50位", max = 50)
    private String barcode;

    /**
     * 当前操作用户
     */
    @ApiModelProperty("当前操作用户")
    @Length(message = "当前操作用户不能超过50位", max = 50)
    private String useraccount;
}
