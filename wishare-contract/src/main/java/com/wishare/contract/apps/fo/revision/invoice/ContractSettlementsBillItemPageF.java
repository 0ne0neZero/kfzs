package com.wishare.contract.apps.fo.revision.invoice;


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
* 收票款项明细表 分页请求参数
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票款项明细表分页请求参数", description = "收票款项明细表")
public class ContractSettlementsBillItemPageF {

    /**
    * billId
    */
    @ApiModelProperty("关联票据ID")
    @Length(message = "关联票据ID不可超过 50 个字符",max = 50)
    private String billId;
    /**
    * bussTypeId
    */
    @ApiModelProperty("业务类型ID")
    @Length(message = "业务类型ID不可超过 40 个字符",max = 40)
    private String bussTypeId;
    /**
    * bussTypeName
    */
    @ApiModelProperty("业务类型名称")
    @Length(message = "业务类型名称不可超过 50 个字符",max = 50)
    private String bussTypeName;
    /**
    * changeId
    */
    @ApiModelProperty("变动ID")
    @Length(message = "变动ID不可超过 40 个字符",max = 40)
    private String changeId;
    /**
    * changeName
    */
    @ApiModelProperty("变动名称")
    @Length(message = "变动名称不可超过 50 个字符",max = 50)
    private String changeName;
    /**
    * itemId
    */
    @ApiModelProperty("款项")
    @Length(message = "款项不可超过 40 个字符",max = 40)
    private String itemId;
    /**
    * itemName
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 40 个字符",max = 40)
    private String itemName;
    /**
    * writeOffInfo
    */
    @ApiModelProperty("核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}")
    private String writeOffInfo;
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
        + "[\"id\",\"billId\",\"bussTypeId\",\"bussTypeName\",\"changeId\",\"changeName\",\"itemId\",\"itemName\",\"writeOffInfo\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 主键ID"
        + "billId 关联票据ID"
        + "bussTypeId 业务类型ID"
        + "bussTypeName 业务类型名称"
        + "changeId 变动ID"
        + "changeName 变动名称"
        + "itemId 款项"
        + "itemName 款项名称"
        + "writeOffInfo 核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}"
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
