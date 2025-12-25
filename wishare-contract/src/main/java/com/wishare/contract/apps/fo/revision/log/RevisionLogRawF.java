package com.wishare.contract.apps.fo.revision.log;


import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 合同改版动态记录表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-07-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同改版动态记录表原始请求参数", description = "合同改版动态记录表原始请求参数，会跟着表重新生成")
public class RevisionLogRawF {

    /**
    * 关联数据ID 不可为空
    */
    @ApiModelProperty(value = "关联数据ID",required = true)
    @NotBlank(message = "关联数据ID不可为空")
    @Length(message = "关联数据ID不可超过 40 个字符",max = 40)
    private String bizId;
    /**
    * 标题
    */
    @ApiModelProperty("标题")
    @Length(message = "标题不可超过 255 个字符",max = 255)
    private String title;
    /**
    * 操作类型code 不可为空
    */
    @ApiModelProperty(value = "操作类型code",required = true)
    @NotBlank(message = "操作类型code不可为空")
    @Length(message = "操作类型code不可超过 40 个字符",max = 40)
    private String actionCode;
    /**
    * 操作类型
    */
    @ApiModelProperty("操作类型")
    @Length(message = "操作类型不可超过 50 个字符",max = 50)
    private String action;
    /**
    * 内容
    */
    @ApiModelProperty("内容")
    @Length(message = "内容不可超过 255 个字符",max = 255)
    private String content;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
