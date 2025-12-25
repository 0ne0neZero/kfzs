package com.wishare.contract.apps.fo.contractset;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("保证金计划收款入参")
public class ContractBondPlanCollectionF {

    @ApiModelProperty(value = "合同id",required = true)
    @NotNull(message = "合同id不能为空")
    private Long contractId;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("保证金类型 0 收取类 1缴纳类")
    private Integer bondType;

    @ApiModelProperty("保证金计划id、金额")
    private List<BondPlanAmountF> bondPlanAmountFList;

    @ApiModelProperty("收款方式  0现金  1网上转帐  2支付宝  3微信")
    private Integer collectionMethod;

    @ApiModelProperty("收款流水号")
    private String collectionNumber;

    @ApiModelProperty("收款凭证文件集")
    private List<FileVo> receiptVoucherFileVos;

    @ApiModelProperty("收款时间")
    private LocalDateTime collectionTime;

    @ApiModelProperty("备注")
    private String remark;
}
