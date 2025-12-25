package com.wishare.contract.apps.fo.contractset;


import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收款操作参数
 * @author ljx
 */
@ApiModel("批量收款参数")
@Data
public class ContractCollectionF {

    @ApiModelProperty("合同id")
    @NotNull(message = "合同id不能为空")
    private Long contractId;

    @ApiModelProperty("收款人id")
    @NotNull(message = "收款人id不能为空")
    private String userId;

    @ApiModelProperty("收款人姓名")
    @NotNull(message = "收款人姓名不能为空")
    private String userName;

    @ApiModelProperty("收款流水号")
    private String serialNumber;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("收款方式   0现金  1网上转账  2支付宝  3微信")
    @NotNull
    private Integer collectionType;

    @ApiModelProperty("收款时间")
    @NotNull(message = "收款时间不能为空")
    private LocalDateTime collectionTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("开票信息")
    private String invoice;

    @ApiModelProperty("收款凭证文件集")
    private List<FileVo> receiptVoucherFileVo;

    @ApiModelProperty("收款计划id及金额")
    @NotNull(message = "收款计划id及金额不能为空")
    private List<CollectionAmountF> collectionAmountFList;
}
