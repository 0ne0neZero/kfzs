package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "收款记录同步空间资源实体类")
public class ReceiptRecordF {

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同 ID")
    private String agreementId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同编码")
    private String agreementNo;

    /**
     * 必填
     */
    @ApiModelProperty(value = "业务账单 ID")
    private String agreementBillId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "实收日期")
    private LocalDate datetime;

    /**
     * 必填
     */
    @ApiModelProperty(value = "收款凭证图片地址")
    private String certUrl;

    /**
     * 必填
     */
    @ApiModelProperty(value = "发票类型：0、未开票，1、已开票，2、不需要开票")
    private Integer invoiceType;

    /**
     * 必填
     */
    @ApiModelProperty(value = "收款金额, 以分计算")
    private BigDecimal payAmount;

    /**
     * 必填
     */
    @ApiModelProperty(value = "收款方式, 可以写 1:刷卡 2: 微信 3:支付宝 4:支票 5:转账 6:银行托收")
    private Integer payType;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
