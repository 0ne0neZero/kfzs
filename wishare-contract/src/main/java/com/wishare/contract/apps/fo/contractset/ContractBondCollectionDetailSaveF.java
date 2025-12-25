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
* 保证金计划收款明细 保存请求参数
* </p>
*
* @author ljx
* @since 2022-10-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bond_collection_detail", description = "保证金计划收款明细")
public class ContractBondCollectionDetailSaveF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("保证金计划id")
    private Long bondPlanId;
    @ApiModelProperty("收款金额")
    private BigDecimal collectionAmount;
    @ApiModelProperty("收款方式  0现金  1银行转帐  2汇款  3支票")
    private Integer collectionMethod;
    @ApiModelProperty("收款流水号")
    private String collectionNumber;
    @ApiModelProperty("收款凭证文件集")
    private String receiptVoucher;
    @ApiModelProperty("收款凭证文件名称")
    private String receiptVoucherName;
    @ApiModelProperty("收款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectionTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否删除")
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
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("收款编码")
    private String collectionCode;
}
