package com.wishare.contract.apps.fo.revision.income.settdetails;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

/**
* <p>
* 结算单扣款明细表信息 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfy
* @since 2024-05-20
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单扣款明细表信息新增请求参数", description = "结算单扣款明细表信息新增请求参数")
public class ContractIncomeConcludeSettdeductionSaveF {

    /**
    * 关联结算单ID 不可为空
    */
    @ApiModelProperty(value = "关联结算单ID")
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

}
