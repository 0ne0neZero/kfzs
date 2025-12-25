package com.wishare.contract.domains.dto.revision.invoice;

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
* 收票款项明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_settlements_bill_item请求对象", description = "收票款项明细表")
public class ContractSettlementsBillItemD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("关联票据ID")
    private String billId;
    @ApiModelProperty("业务类型ID")
    private String bussTypeId;
    @ApiModelProperty("业务类型名称")
    private String bussTypeName;
    @ApiModelProperty("变动ID")
    private String changeId;
    @ApiModelProperty("变动名称")
    private String changeName;
    @ApiModelProperty("款项")
    private String itemId;
    @ApiModelProperty("款项名称")
    private String itemName;
    @ApiModelProperty("核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}")
    private String writeOffInfo;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
