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

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/8/19  14:00
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金转履约传参collect", description = "保证金转履约传参collect")
public class BondVolumUpF {

    /**
     * bondId
     */
    @ApiModelProperty(value = "保证金计划ID", required = true)
    @Length(message = "保证金计划ID不可超过 40 个字符",max = 40)
    private String bondId;
    /**
     * 合同ID
     */
    @ApiModelProperty("合同ID")
    private String contractId;
    /**
     * amount
     */
    @ApiModelProperty(value = "金额（收款，收据，结转，退款）", required = true)
    @Digits(integer = 18,fraction =2,message = "金额（收款，收据，结转，退款）不正确")
    private BigDecimal amount;
    /**
     * dealDate
     */
    @ApiModelProperty("结转日期")
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
     * chargeItemId
     */
    @ApiModelProperty("业务费项ID")
    @Length(message = "业务费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;

}
