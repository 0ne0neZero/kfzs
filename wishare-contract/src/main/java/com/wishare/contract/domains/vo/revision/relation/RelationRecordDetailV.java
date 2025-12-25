package com.wishare.contract.domains.vo.revision.relation;

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

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/8/10  10:18
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "RelationRecordDetailV", description = "RelationRecordDetailV")
public class RelationRecordDetailV {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 操作类型  1变更  2续签
     */
    @ApiModelProperty("操作类型  1变更  2续签")
    private Integer type;
    /**
     * 新增合同数据的主键ID
     */
    @ApiModelProperty("新增合同数据的主键ID")
    private String addId;
    /**
     * 新增合同数据的合同编号
     */
    @ApiModelProperty("新增合同数据的合同编号")
    private String addContractNo;
    /**
     * 新增合同数据的名称
     */
    @ApiModelProperty("新增合同数据的名称")
    private String addName;
    /**
     * 原先合同数据的主键ID
     */
    @ApiModelProperty("原先合同数据的主键ID")
    private String oldId;
    /**
     * 原先合同数据的合同编号
     */
    @ApiModelProperty("原先合同数据的合同编号")
    private String oldContractNo;
    /**
     * 原先合同数据的名称
     */
    @ApiModelProperty("原先合同数据的名称")
    private String oldName;
    /**
     * 是否已执行  1已执行  0未执行
     */
    @ApiModelProperty("是否已执行  1已执行  0未执行")
    private Integer isDone;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creator;
    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operator;
    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
