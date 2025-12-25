package com.wishare.finance.infrastructure.remote.fo.sign;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 下载对账文件入参
 *
 * @Author zyj
 * @Date 2023/08/03
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("下载对账文件入参")
public class AccountCheckingFileDownloadF {
    @ApiModelProperty(value = "-项目id")
    @NotBlank(message = "项目id不可为空")
    private String communityId;

    @ApiModelProperty(value = "-商户号")
    @Length(min = 10, max = 20, message = "商户号格式不正确")
    @NotBlank(message = "商户号不可为空")
    private String merchantId;

    @ApiModelProperty(value = "-用户名")
    @Length(max = 20, message = "用户名格式不正确")
    @NotBlank(message = "用户名不可为空")
    private String userName;

    @ApiModelProperty(value = "-用户密码")
    @Length(max = 20, message = "用户密码格式不正确")
    private String userPass;

    @ApiModelProperty(value = "-请求流水号")
    @Length(max = 60, message = "请求流水号格式不正确")
    private String reqSn;

    @ApiModelProperty(value = "-签名信息")
    private String signedMsg;

    @ApiModelProperty(value = "开始日 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDay;

    @ApiModelProperty(value = "结束日 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDay;

    @ApiModelProperty(value = "-状态（交易状态条件, 0成功,1失败, 2全部）")
    private Integer status;

    @ApiModelProperty(value = "-是否包含手续费（0.不需手续费，1.包含手续费空则默认为0）")
    @Length(max = 2, message = "是否包含手续费格式不正确")
    private String contFee;

    @ApiModelProperty(value = "-查询类型（0.按完成日期1.按提交日期）")
    private Integer type;

    @ApiModelProperty(value = "-结算账号（可空）")
    @Length(max = 30, message = "结算账号格式不正确")
    private String settAcct;

}
