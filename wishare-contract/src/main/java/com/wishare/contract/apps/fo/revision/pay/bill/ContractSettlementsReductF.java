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
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/17/14:36
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同收款计划减免信息表", description = "合同收款计划减免信息表")
public class ContractSettlementsReductF {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("结算单id")
    private String settlementId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("收票/开票类型 收款 0 付款 1 ")
    private String type;

    @ApiModelProperty("减免金额")
    private BigDecimal amount;

    @ApiModelProperty("附件(前端传数组)")
    private List<FileVo> attachments;

    @ApiModelProperty("备注")
    private String remark;
}
