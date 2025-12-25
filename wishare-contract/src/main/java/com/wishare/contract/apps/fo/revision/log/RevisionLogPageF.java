package com.wishare.contract.apps.fo.revision.log;


import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
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
* 合同改版动态记录表 分页请求参数
* </p>
*
* @author chenglong
* @since 2023-07-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同改版动态记录表分页请求参数", description = "合同改版动态记录表")
public class RevisionLogPageF {

    /**
    * bizId
    */
    @ApiModelProperty("关联数据ID")
    @Length(message = "关联数据ID不可超过 40 个字符",max = 40)
    private String bizId;
    /**
    * title
    */
    @ApiModelProperty("标题")
    @Length(message = "标题不可超过 255 个字符",max = 255)
    private String title;
    /**
    * actionCode
    */
    @ApiModelProperty("操作类型code")
    @Length(message = "操作类型code不可超过 40 个字符",max = 40)
    private String actionCode;
    /**
    * action
    */
    @ApiModelProperty("操作类型")
    @Length(message = "操作类型不可超过 50 个字符",max = 50)
    private String action;
    /**
    * content
    */
    @ApiModelProperty("内容")
    @Length(message = "内容不可超过 255 个字符",max = 255)
    private String content;
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
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"bizId\",\"title\",\"actionCode\",\"action\",\"content\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 主键ID"
        + "bizId 关联数据ID"
        + "title 标题"
        + "actionCode 操作类型code"
        + "action 操作类型"
        + "content 内容"
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "deleted 是否删除  0 正常 1 删除")
    private List<String> fields;


}
