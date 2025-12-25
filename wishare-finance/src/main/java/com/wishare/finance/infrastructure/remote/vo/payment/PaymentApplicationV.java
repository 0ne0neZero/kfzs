package com.wishare.finance.infrastructure.remote.vo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 支付应用表
 * </p>
 *
 * @author dxclay
 * @since 2022-11-21
 */
@Getter
@Setter
@ApiModel
public class PaymentApplicationV {

    @ApiModelProperty(value = "应用主id")
    private Long id;

    @ApiModelProperty(value = "应用id")
    private String appId;

    @ApiModelProperty(value = "应用名称")
    private String name;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "服务商id")
    private Long spId;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "支付公钥", required = true)
    private String publicKey;

    @ApiModelProperty(value = "支付秘钥", required = true)
    private String privateKey;

    @ApiModelProperty(value = "应用类型：0商户应用，1服务商应用", required = true)
    private Integer appType;

    @ApiModelProperty(value = "商户状态：0正常，1冻结，注销", required = true)
    private Integer state;

    @ApiModelProperty(value = "创建人ID", required = true)
    private String creator;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private String creatorName;

    @ApiModelProperty(value = "操作人ID", required = true)
    private String operator;

    @ApiModelProperty(value = "修改人姓名", required = true)
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;


}
