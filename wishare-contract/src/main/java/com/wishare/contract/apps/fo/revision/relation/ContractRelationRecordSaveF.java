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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
* <p>
*  新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新增请求参数", description = "新增请求参数")
public class ContractRelationRecordSaveF {

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

}
