package com.wishare.contract.apps.fo.contractset;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量付款参数
 * @author ljx
 */
@ApiModel("批量付款参数")
@Data
public class ContractPaymentF {

    @ApiModelProperty("合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty("付款人id")
    @NotNull
    private String userId;

    @ApiModelProperty("付款人姓名")
    @NotNull
    private String userName;

    @ApiModelProperty("付款类型  0有票付款  1无票付款")
    @NotNull
    private Integer paymentType;

    @ApiModelProperty("付款方式  0现金  1银行转帐  2汇款  3支票")
    @NotNull
    private Integer paymentMethod;

    @ApiModelProperty("请款说明")
    private String remark;

    @ApiModelProperty("申请付款时间")
    private LocalDateTime applyPaymentTime;

    @ApiModelProperty("发票文件集")
    private List<FileVo> invoiceFiles;

    @ApiModelProperty("票据号码集")
    private String invoiceCode;

    @ApiModelProperty("收款计划id及金额")
    private List<CollectionAmountF> collectionAmountFList;
}
