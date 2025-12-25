package com.wishare.contract.apps.fo.revision.invoice;


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
* 收票款项明细表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票款项明细表新增请求参数", description = "收票款项明细表新增请求参数")
public class ContractSettlementsBillItemSaveF {

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

}
