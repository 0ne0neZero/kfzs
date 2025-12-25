package com.wishare.contract.domains.vo.revision.bond.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/8/21  11:15
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "转履约记录V", description = "转履约记录V")
public class VolumeUpV {


    /**
     * 保证金ID
     */
    @ApiModelProperty("保证金ID")
    private String id;
    /**
     * 保证金编码CODE
     */
    @ApiModelProperty("保证金编码CODE")
    private String code;
    /**
     * 转入保证金ID
     */
    @ApiModelProperty("转入保证金ID")
    private String transferId;
    /**
     * 转入保证金编码CODE
     */
    @ApiModelProperty("转入保证金编码CODE")
    private String transferCode;
    /**
     * 转入保证金金额
     */
    @ApiModelProperty("转入保证金金额")
    private BigDecimal amount;
    /**
     * 转履约日期
     */
    @ApiModelProperty("转履约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creator;
    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

}
