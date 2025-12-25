package com.wishare.contract.apps.fo.contractset;


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
*  分页请求参数
* </p>
*
* @author jinhui
* @since 2023-02-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bpm_process_record", description = "")
public class ContractBpmProcessRecordPageF {

    @ApiModelProperty("主键")
    @NotNull(message = "主键不可为空")
    private Long id;
    @ApiModelProperty("流程id")
    private String processId;
    @ApiModelProperty("Bo对象中的id")
    private String bpmBoUuid;
    @ApiModelProperty("类型（1合同订立支出2合同订立收入）")
    private Boolean type;
    @ApiModelProperty("租户ID")
    @NotBlank(message = "租户ID不可为空")
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
    private Boolean reviewStatus;
    @ApiModelProperty("驳回原因")
    private String rejectReason;
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"processId\",\"bpmBoUuid\",\"type\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"deleted\",\"gmtModify\",\"reviewStatus\",\"rejectReason\"]"
        + "id 主键"
        + "processId 流程id"
        + "bpmBoUuid Bo对象中的id"
        + "type 类型（1合同订立支出2合同订立收入）"
        + "tenantId 租户ID"
        + "creator 创建人id"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 修改人id"
        + "operatorName 修改人名称"
        + "deleted 是否删除（0否1是）"
        + "gmtModify 修改时间"
        + "reviewStatus 审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批"
        + "rejectReason 驳回原因")
    private List<String> fields;


}
