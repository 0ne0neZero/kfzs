package com.wishare.contract.domains.vo.revision.pay.team;

import java.time.LocalDate;
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
* 支出合同-团队表视图对象
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同-团队表视图对象", description = "支出合同-团队表视图对象")
public class ContractPayTeamV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 姓名
    */
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("关联合同ID")
    private String contractId;
    /**
    * 所属公司ID
    */
    @ApiModelProperty("所属公司ID")
    private String orgId;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    private String departId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    private String departName;
    /**
    * 所属公司名称
    */
    @ApiModelProperty("所属公司名称")
    private String orgName;
    @ApiModelProperty("团队角色ID")
    private String roleId;
    /**
    * 团队角色
    */
    @ApiModelProperty("团队角色")
    private String role;
    /**
    * 截止有效期
    */
    @ApiModelProperty("截止有效期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
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
