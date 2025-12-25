package com.wishare.contract.domains.dto.contractset;

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
* 保证金收据明细表
* </p>
*
* @author ljx
* @since 2022-12-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bond_receipt_detail请求对象", description = "保证金收据明细表")
public class ContractBondReceiptDetailD {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("保证金计划id")
    private Long bondPlanId;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("发票类型  6电子收据")
    private Integer invoiceType;
    @ApiModelProperty("开票状态  0开票中  1成功 2失败")
    private Integer invocieStatus;
    @ApiModelProperty("票据号码  收据编号")
    private String invoiceNumber;
    @ApiModelProperty("申请开票时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("收据id")
    private Long receiptId;
}
