package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 合同收款明细表
* </p>
*
* @author ljx
* @since 2022-09-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_collection_detail", description = "合同收款明细表")
public class ContractCollectionDetailF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("开票信息")
    private String invoice;
    @ApiModelProperty("收款编号")
    private String receiptNumber;
    @ApiModelProperty("收款流水号")
    private String serialNumber;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("本次收款金额")
    private BigDecimal receivedAmount;
    @ApiModelProperty("收款方式   0现金  1网上转账  2支付宝  3微信")
    private Integer collectionType;
    @ApiModelProperty("收款凭证文件集")
    private String receiptVoucher;
    @ApiModelProperty("收款凭证文件名集")
    private String receiptVoucherName;
    @ApiModelProperty("收款人id")
    private String userId;
    @ApiModelProperty("收款人姓名")
    private String userName;
    @ApiModelProperty("收款时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectionTime;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("创建时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
