package com.wishare.contract.apps.fo.revision.pay.bill;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/17/14:45
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同结算单收付款信息表", description = "合同结算单收付款信息表")
public class ContractSettlementsFundF {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("结算单id")
    private String settlementId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("收付款方式")
    private Integer fundType;

    @ApiModelProperty("收付款金额")
    private BigDecimal amount;

    @ApiModelProperty("收付款类型 收款 0 付款 1")
    private Integer type;

    @ApiModelProperty("收付款日期")
    private LocalDate collectTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("附件(前端传数组)")
    private List<FileVo> attachments;
}
