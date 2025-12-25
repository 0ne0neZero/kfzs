package com.wishare.finance.infrastructure.remote.vo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value="商户应用详细信息")
public class MerchantAppDetailV {

    @ApiModelProperty(value = "商户id", required = true)
    private Long id;

    @ApiModelProperty(value = "商户名称", required = true)
    private String name;

    @ApiModelProperty(value = "商户编号", required = true)
    private String mchNo;

    @ApiModelProperty(value = "外部商户编号")
    private String outMchNo;

    @ApiModelProperty(value = "商户类型：0普通商户，1特约商户", required = true)
    private Integer mchType;

    @ApiModelProperty(value = "服务商id")
    private Long spId;

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

    @ApiModelProperty(value = "应用列表")
    private List<PaymentApplicationV> applications;

}
