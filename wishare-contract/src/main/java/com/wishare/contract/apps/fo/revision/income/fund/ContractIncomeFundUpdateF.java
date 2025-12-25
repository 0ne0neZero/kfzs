package com.wishare.contract.apps.fo.revision.income.fund;


import java.math.BigDecimal;
import java.time.LocalDate;
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
* 收入合同-款项表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同-款项表更新请求参数", description = "收入合同-款项表")
public class ContractIncomeFundUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;
    /**
     * 关联合同ID 不可为空
     */
    @ApiModelProperty(value = "关联合同ID")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * name
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 50 个字符",max = 50)
    private String name;
    /**
    * typeId
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * amount
    */
    @ApiModelProperty("金额")
    @Digits(integer = 10,fraction =2,message = "金额不正确")
    private BigDecimal amount;
    /**
    * taxRateId
    */
    @ApiModelProperty("税率ID")
    @Length(message = "税率ID不可超过 40 个字符",max = 40)
    private String taxRateId;
    /**
    * payTypeId
    */
    @ApiModelProperty("付费类型ID")
    @Length(message = "付费类型ID不可超过 30 个字符",max = 30)
    private String payTypeId;
    /**
    * payWayId
    */
    @ApiModelProperty("付费方式ID")
    @Length(message = "付费方式ID不可超过 30 个字符",max = 30)
    private String payWayId;
    /**
    * standardId
    */
    @ApiModelProperty("收费标准ID")
    @Length(message = "收费标准ID不可超过 30 个字符",max = 30)
    private String standardId;
    /**
     * 收费标准金额
     */
    @ApiModelProperty("收费标准金额")
    @Digits(integer = 10,fraction =2,message = "收费标准金额不正确")
    private BigDecimal standAmount;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 255 个字符",max = 255)
    private String remark;

    @ApiModelProperty("税额")
    @Digits(integer = 10,fraction =6,message = "税额不正确")
    private BigDecimal taxRateAmount;
    @ApiModelProperty("不含税金额")
    @Digits(integer = 10,fraction =6,message = "不含税金额不正确")
    private BigDecimal amountWithOutRate;
    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("费项ID")
    private String chargeItemId;
    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("编辑操作编码 add 新增  edit 编辑  delete 删除")
    private String actionCode;
    @ApiModelProperty("数量")
    private BigDecimal amountNum;

    @ApiModelProperty("是否主合同清单（1.是）")
    private Integer isMain;
    //收费方式ID
    private String chargeMethodId;
    //收费方式
    private String chargeMethodName;
    //修正记录表示（1.新，0.历史）
    private Integer correctionTag;
    //对应数据ID
    private String mainId;
}
