package com.wishare.contract.apps.fo.revision.relation;


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
*  原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "原始请求参数", description = "原始请求参数，会跟着表重新生成")
public class ContractRelationRecordRawF {

    /**
    * 操作类型  1变更  2续签
    */
    @ApiModelProperty("操作类型  1变更  2续签")
    private Integer type;
    /**
    * 新增合同数据的主键ID
    */
    @ApiModelProperty("新增合同数据的主键ID")
    @Length(message = "新增合同数据的主键ID不可超过 40 个字符",max = 40)
    private String addId;
    /**
    * 原先合同数据的主键ID
    */
    @ApiModelProperty("原先合同数据的主键ID")
    @Length(message = "原先合同数据的主键ID不可超过 40 个字符",max = 40)
    private String oldId;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"type\",\"addId\",\"oldId\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 主键ID"
        + "type 操作类型  1变更  2续签"
        + "addId 新增合同数据的主键ID"
        + "oldId 原先合同数据的主键ID"
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
