package com.wishare.contract.domains.vo.contractset;

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
* 
* </p>
*
* @author jinhui
* @since 2023-02-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bpm_process_record视图对象", description = "")
public class ContractBpmProcessRecordV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("流程id")
    private String processId;
    @ApiModelProperty("Bo对象中的id")
    private String bpmBoUuid;
    @ApiModelProperty("类型（1合同订立支出2合同订立收入）")
    private Integer type;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("创建人id")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("修改人id")
    private String operator;
    @ApiModelProperty("修改人名称")
    private String operatorName;
    @ApiModelProperty("是否删除（0否1是）")
    private Boolean deleted;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批")
    private Integer reviewStatus;
    @ApiModelProperty("驳回原因")
    private String rejectReason;

}
