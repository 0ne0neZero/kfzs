package com.wishare.contract.apps.fo.revision.pay.settdetails;


import java.math.BigDecimal;
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
* 结算单扣款明细表信息 分页请求参数
* </p>
*
* @author zhangfy
* @since 2024-05-20
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单扣款明细表信息分页请求参数", description = "结算单扣款明细表信息")
public class ContractPayConcludeSettdeductionPageF {

    /**
    * settlementId
    */
    @ApiModelProperty("关联结算单ID")
    @Length(message = "关联结算单ID不可超过 50 个字符",max = 50)
    private String settlementId;
    /**
    * typeId
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * type
    */
    @ApiModelProperty("款项类型")
    @Length(message = "款项类型不可超过 50 个字符",max = 50)
    private String type;
    /**
    * amount
    */
    @ApiModelProperty("扣款金额")
    @Digits(integer = 10,fraction =2,message = "扣款金额不正确")
    private BigDecimal amount;
    /**
    * chargeItemId
    */
    @ApiModelProperty("费项ID")
    private Long chargeItemId;
    /**
    * chargeItem
    */
    @ApiModelProperty("费项")
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;
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
        + "[\"id\",\"settlementId\",\"typeId\",\"type\",\"amount\",\"chargeItemId\",\"chargeItem\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 主键ID"
        + "settlementId 关联结算单ID"
        + "typeId 款项类型ID"
        + "type 款项类型"
        + "amount 扣款金额"
        + "chargeItemId 费项ID"
        + "chargeItem 费项"
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
