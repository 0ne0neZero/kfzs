package com.wishare.contract.apps.fo.revision.invoice;


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
* 收票款项明细表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票款项明细表原始请求参数", description = "收票款项明细表原始请求参数，会跟着表重新生成")
public class ContractSettlementsBillItemRawF {

    /**
    * 关联票据ID 不可为空
    */
    @ApiModelProperty(value = "关联票据ID",required = true)
    @NotBlank(message = "关联票据ID不可为空")
    @Length(message = "关联票据ID不可超过 50 个字符",max = 50)
    private String billId;
    /**
    * 业务类型ID
    */
    @ApiModelProperty("业务类型ID")
    @Length(message = "业务类型ID不可超过 40 个字符",max = 40)
    private String bussTypeId;
    /**
    * 业务类型名称
    */
    @ApiModelProperty("业务类型名称")
    @Length(message = "业务类型名称不可超过 50 个字符",max = 50)
    private String bussTypeName;
    /**
    * 变动ID
    */
    @ApiModelProperty("变动ID")
    @Length(message = "变动ID不可超过 40 个字符",max = 40)
    private String changeId;
    /**
    * 变动名称
    */
    @ApiModelProperty("变动名称")
    @Length(message = "变动名称不可超过 50 个字符",max = 50)
    private String changeName;
    /**
    * 款项
    */
    @ApiModelProperty("款项")
    @Length(message = "款项不可超过 40 个字符",max = 40)
    private String itemId;
    /**
    * 款项名称
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 40 个字符",max = 40)
    private String itemName;
    /**
    * 核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}
    */
    @ApiModelProperty("核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}")
    private String writeOffInfo;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
