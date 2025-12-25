package com.wishare.contract.apps.fo.revision.pay.settdetails;


import java.math.BigDecimal;
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
* 结算单扣款明细表信息 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfy
* @since 2024-05-20
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单扣款明细表信息原始请求参数", description = "结算单扣款明细表信息原始请求参数，会跟着表重新生成")
public class ContractPayConcludeSettdeductionRawF {

    /**
    * 关联结算单ID 不可为空
    */
    @ApiModelProperty(value = "关联结算单ID",required = true)
    @NotBlank(message = "关联结算单ID不可为空")
    @Length(message = "关联结算单ID不可超过 50 个字符",max = 50)
    private String settlementId;
    /**
    * 款项类型ID
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * 款项类型
    */
    @ApiModelProperty("款项类型")
    @Length(message = "款项类型不可超过 50 个字符",max = 50)
    private String type;
    /**
    * 扣款金额
    */
    @ApiModelProperty("扣款金额")
    @Digits(integer = 10,fraction =2,message = "扣款金额不正确")
    private BigDecimal amount;
    /**
    * 费项ID
    */
    @ApiModelProperty("费项ID")
    private Long chargeItemId;
    /**
    * 费项
    */
    @ApiModelProperty("费项")
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
