package com.wishare.contract.domains.vo.revision.invoice;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 收票款项明细表视图对象
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票款项明细表视图对象", description = "收票款项明细表视图对象")
public class ContractSettlementsBillItemV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联票据ID
    */
    @ApiModelProperty("关联票据ID")
    private String billId;
    /**
    * 业务类型ID
    */
    @ApiModelProperty("业务类型ID")
    private String bussTypeId;
    /**
    * 业务类型名称
    */
    @ApiModelProperty("业务类型名称")
    private String bussTypeName;
    /**
    * 变动ID
    */
    @ApiModelProperty("变动ID")
    private String changeId;
    /**
    * 变动名称
    */
    @ApiModelProperty("变动名称")
    private String changeName;
    /**
    * 款项
    */
    @ApiModelProperty("款项")
    private String itemId;
    /**
    * 款项名称
    */
    @ApiModelProperty("款项名称")
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
    private String tenantId;
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
