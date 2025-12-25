package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
* <p>
* 合同订立信息拓展表市拓信息实体
* </p>
*
* @author zhangfy
* @since 2023-10-19
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "市拓合同项目信息表新增请求参数", description = "市拓合同项目信息表新增请求参数")
public class ContractCttmsjF {

    /**
     * 参数id 不可为空
     */
    @ApiModelProperty(value = "参数id")
    private String id;

    /**
    * 业务id 不可为空
    */
    @ApiModelProperty(value = "业务id")
    private String businessId;
    /**
    * 合同类型 不可为空
    */
    @ApiModelProperty(value = "合同类型")
    private String contractType;
    /**
    * 主数据编码 不可为空
    */
    @ApiModelProperty(value = "主数据编码")
    private String masterCode;
    /**
    * 4A员工编码
    */
    @ApiModelProperty(value = "4A员工编码")
    private String empCode;
    /**
    * 业态分类 不可为空
    */
    @ApiModelProperty(value = "业态分类")
    private String businessType;
    /**
    * 测算方式
    */
    @ApiModelProperty("测算方式")
    @Length(message = "测算方式不可超过 50 个字符",max = 50)
    private String mearsureWaye;
    /**
    * 物业费单价
    */
    @ApiModelProperty("物业费单价")
    @Length(message = "物业费单价不可超过 50 个字符",max = 50)
    private String properyFee;
    /**
    * 收费面积
    */
    @ApiModelProperty("收费面积")
    @Length(message = "收费面积不可超过 50 个字符",max = 50)
    private String chargeArea;
    /**
    * 车位数量
    */
    @ApiModelProperty("车位数量")
    @Length(message = "车位数量不可超过 50 个字符",max = 50)
    private String carAmount;
    /**
    * 车位单价
    */
    @ApiModelProperty("车位单价")
    @Length(message = "车位单价不可超过 50 个字符",max = 50)
    private String carPrice;
    /**
    * 签约年限
    */
    @ApiModelProperty("签约年限")
    @Length(message = "签约年限不可超过 50 个字符",max = 50)
    private String yearLimit;
    /**
    * 基础物管收入
    */
    @ApiModelProperty("基础物管收入")
    @Length(message = "基础物管收入不可超过 50 个字符",max = 50)
    private String baseIncome;
    /**
    * 基础物管收入含税
    */
    @ApiModelProperty("基础物管收入含税")
    @Length(message = "基础物管收入含税不可超过 50 个字符",max = 50)
    private String baseIncomeRate;

    @ApiModelProperty("市拓子项目code")
    private String projectCode;

    @ApiModelProperty("市拓项目投模上会版本号")
    private String versionNo;
}
