package com.wishare.contract.apps.fo.revision.relation;


import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

/**
* <p>
*  更新请求参数
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "下拉列表请求参数", description = "")
public class ContractRelationRecordListF {

    /**
    * type
    */
    @ApiModelProperty("操作类型  1变更  2续签")
    private Integer type;
    /**
    * addId
    */
    @ApiModelProperty("新增合同数据的主键ID")
    @Length(message = "新增合同数据的主键ID不可超过 40 个字符",max = 40)
    private String addId;
    /**
    * oldId
    */
    @ApiModelProperty("原先合同数据的主键ID")
    @Length(message = "原先合同数据的主键ID不可超过 40 个字符",max = 40)
    private String oldId;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人")
    @Length(message = "创建人不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人名称")
    @Length(message = "创建人名称不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * operator
    */
    @ApiModelProperty("操作人")
    @Length(message = "操作人不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人名称")
    @Length(message = "操作人名称不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private String indexId;
}
