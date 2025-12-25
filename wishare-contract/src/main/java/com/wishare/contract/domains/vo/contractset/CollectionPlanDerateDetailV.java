package com.wishare.contract.domains.vo.contractset;

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
* 收款计划减免明细
* </p>
*
* @author ljx
* @since 2022-11-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "collection_plan_derate_detail视图对象", description = "收款计划减免明细")
public class CollectionPlanDerateDetailV {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("收款计划id")
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

}
