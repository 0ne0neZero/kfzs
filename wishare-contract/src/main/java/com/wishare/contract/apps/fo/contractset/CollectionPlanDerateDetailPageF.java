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
* 收款计划减免明细 分页请求参数
* </p>
*
* @author ljx
* @since 2022-11-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "collection_plan_derate_detail", description = "收款计划减免明细")
public class CollectionPlanDerateDetailPageF {

    @ApiModelProperty("id")
    @NotNull(message = "id不可为空")
    private Long id;
    @ApiModelProperty("合同Id")
    @NotNull(message = "合同Id不可为空")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    @NotNull(message = "收款计划id不可为空")
    private Long collectionPlanId;
    @ApiModelProperty("减免金额（元）")
    private BigDecimal derateAmount;
    @ApiModelProperty("减免原因")
    private String derateReason;
    @ApiModelProperty("审核状态  0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("审批编号")
    private String auditCode;
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
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"collectionPlanId\",\"derateAmount\",\"derateReason\",\"auditStatus\",\"auditCode\",\"deleted\",\"creator\",\"creatorName\",\"gmtCreate\"]"
        + "id id"
        + "contractId 合同Id"
        + "collectionPlanId 收款计划id"
        + "derateAmount 减免金额（元）"
        + "derateReason 减免原因"
        + "auditStatus 审核状态  0通过  1审核中  2未通过"
        + "auditCode 审批编号"
        + "deleted 是否删除:0未删除，1已删除"
        + "creator 创建人ID"
        + "creatorName 创建人姓名"
        + "gmtCreate 创建时间")
    private List<String> fields;


}
