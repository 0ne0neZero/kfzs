package com.wishare.contract.apps.fo.revision.bond;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* 收取保证金改版关联单据明细表
* </p>
*
* @author chenglong
* @since 2023-07-27
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收取保证金改版关联单据明细表请求参数BillF", description = "收取保证金改版关联单据明细表BillF")
public class CollectBondBillF {

    /**
    * bondId
    */
    @ApiModelProperty(value = "保证金计划ID", required = true)
    @Length(message = "保证金计划ID不可超过 40 个字符",max = 40)
    private String bondId;
    /**
    * amount
    */
    @ApiModelProperty(value = "金额（收款，收据，结转，退款）", required = true)
    @Digits(integer = 18,fraction =2,message = "金额（收款，收据，结转，退款）不正确")
    private BigDecimal amount;
    /**
    * dealWayCode
    */
    @ApiModelProperty("交易方式编码（现金，微信，支付宝，网上转账等）")
    @Length(message = "交易方式编码（现金，微信，支付宝，网上转账等）不可超过 40 个字符",max = 40)
    private String dealWayCode;
    /**
    * chargeItemId
    */
    @ApiModelProperty("业务费项ID")
    @Length(message = "业务费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;
    /**
    * dealDate
    */
    @ApiModelProperty("（收款，收据，结转，退款）交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealDate;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
    * reason
    */
    @ApiModelProperty("原因")
    @Length(message = "原因不可超过 255 个字符",max = 255)
    private String reason;
    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private FileVo fileVo;
    /**
     * bankAccount
     */
    @ApiModelProperty("银行账户")
    @Length(message = "银行账户不可超过 40 个字符",max = 50)
    private String bankAccount;

}
