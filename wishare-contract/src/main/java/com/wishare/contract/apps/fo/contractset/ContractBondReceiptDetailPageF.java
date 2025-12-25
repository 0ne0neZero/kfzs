package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 保证金收据明细表 分页请求参数
* </p>
*
* @author ljx
* @since 2022-12-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bond_receipt_detail", description = "保证金收据明细表")
public class ContractBondReceiptDetailPageF {

    @ApiModelProperty("id")
    @NotNull(message = "id不可为空")
    private Long id;
    @ApiModelProperty("合同id")
    @NotNull(message = "合同id不可为空")
    private Long contractId;
    @ApiModelProperty("保证金计划id")
    @NotNull(message = "保证金计划id")
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
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"collectionPlanId\",\"invoiceAmount\",\"invoiceType\",\"invocieStatus\",\"invoiceNumber\",\"invoiceTime\",\"remark\",\"deleted\",\"creator\",\"creatorName\",\"gmtCreate\"]"
        + "id id"
        + "contractId 合同id"
        + "collectionPlanId 付款计划id"
        + "invoiceAmount 收票金额"
        + "invoiceType 发票类型  6电子收据"
        + "invocieStatus 开票状态  0开票中  1成功 2失败"
        + "invoiceNumber 票据号码  收据编号"
        + "invoiceTime 申请开票时间"
        + "remark 备注"
        + "deleted 是否删除:0未删除，1已删除"
        + "creator 创建人ID"
        + "creatorName 创建人姓名"
        + "gmtCreate 创建时间")
    private List<String> fields;


}
