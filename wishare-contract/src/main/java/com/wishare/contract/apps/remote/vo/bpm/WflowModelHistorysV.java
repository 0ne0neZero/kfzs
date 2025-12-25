package com.wishare.contract.apps.remote.vo.bpm;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/10/28 18:28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "wflow_model_historys视图对象", description = "历史表单流程模型表，每次保存/发布新增一条记录")
public class WflowModelHistorysV {

    private String id;
    @ApiModelProperty("流程定义的ID")
    private String processDefId;
    private String formId;
    private String formName;
    private Integer version;
    private String logo;
    private Integer groupId;
    private String remark;
    @ApiModelProperty("来源")
    private String source;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("创建用户")
    private String creator;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    /**
     * 基础设置
     */
    private String settings;

    /**
     * 模板表单
     */
    private String formItems;

    /**
     * 流程信息
     */
    private String process;

    @ApiModelProperty("模型类型")
    private String modelType;
    @ApiModelProperty("应用Code")
    private String appCode;
    @ApiModelProperty("分类ID")
    private Long categoryId;
    @ApiModelProperty("模型标签。用于模型分发时，确认同源关系。同一个模板复制到其他租户下，模型标签不变。也可用于业务系统匹配流程模型。租户下唯一。")
    private String modelTag;

    @ApiModelProperty("用户类型")
    private String modelUserType;

    @ApiModelProperty("自定义表单标准字段ID列表")
    private List<String> stdFieldIds;

}