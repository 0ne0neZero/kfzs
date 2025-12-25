package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
* 合同订立信息表
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractConcludeSumV {

    @ApiModelProperty("合同收/付总金额")
    private BigDecimal amountSum;
    @ApiModelProperty("合同已收/已付总金额")
    private BigDecimal paymentAmountSum;
    @ApiModelProperty("合同未收/付总金额")
    private BigDecimal uncollectedSum;
    @ApiModelProperty("累计已到期金额")
    private BigDecimal dueSumAmount;

}
