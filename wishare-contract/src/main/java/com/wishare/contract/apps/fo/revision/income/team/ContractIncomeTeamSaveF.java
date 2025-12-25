package com.wishare.contract.apps.fo.revision.income.team;


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
* 收入合同-团队表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同-团队表新增请求参数", description = "收入合同-团队表新增请求参数")
public class ContractIncomeTeamSaveF {

    /**
    * 关联合同ID 不可为空
    */
    @ApiModelProperty(value = "关联合同ID",required = true)
    @NotBlank(message = "关联合同ID不可为空")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * 姓名
    */
    @ApiModelProperty("姓名")
    @Length(message = "姓名不可超过 50 个字符",max = 50)
    private String name;
    /**
    * 所属公司ID
    */
    @ApiModelProperty("所属公司ID")
    @Length(message = "所属公司ID不可超过 50 个字符",max = 50)
    private String orgId;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 50 个字符",max = 50)
    private String departId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String departName;
    /**
    * 所属公司名称
    */
    @ApiModelProperty("所属公司名称")
    @Length(message = "所属公司名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
     * 团队角色ID
     */
    @ApiModelProperty("团队角色ID")
    @Length(message = "团队角色ID不可超过 40 个字符",max = 40)
    private String roleId;
    /**
    * 截止有效期
    */
    @ApiModelProperty("截止有效期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
