package com.wishare.contract.domains.dto.revision.template;

import java.math.BigDecimal;
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
* 合同修改记录表
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_record_info请求对象", description = "合同修改记录表")
public class ContractRecordInfoD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("模板id")
    private String templateId;
    @ApiModelProperty("模板名称")
    private String templateName;
    @ApiModelProperty("合同版本")
    private BigDecimal version;
    @ApiModelProperty("更新记录")
    private String fieldRecord;
    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Boolean deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人id")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
