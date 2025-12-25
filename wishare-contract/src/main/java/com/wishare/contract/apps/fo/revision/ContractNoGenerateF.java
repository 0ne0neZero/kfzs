package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "合同编号自动生成请求信息")
public class ContractNoGenerateF {

    @ApiModelProperty(value = "所属区域简称编码 对应合同所属区域选项对应的简称,eg.HBQY")
    private String regionAbbrCode;

    @ApiModelProperty("所属项目ID")
    private String communityId;

    @ApiModelProperty("所属项目名称")
    @NotBlank(message = "所属项目名称不能为空")
    private String communityName;

    @ApiModelProperty("项目简称编码")
    private String projectNameNumber;



    @ApiModelProperty(value = "收入类型 收入合同 1 支出合同 0")
    @NotNull(message = "收入类型不能为空")
    private Integer incomeType;

    @ApiModelProperty("无支付义务类 是 1  否 0")
    @NotNull(message = "无支付义务类 不能为空")
    private Integer noPaySign;

    @ApiModelProperty(value = "省份代码简称")
    private String provinceAbbrCode;

    @ApiModelProperty(value = "部门代码简称")
    private String departAbbrCode;

    @ApiModelProperty(value = "合同类别简称")
    @NotBlank(message = "请维护 合同类别简称")
    private String categoryAbbrCode;

    /**
     * 签约方式 0 新签 1 补充协议 2 续签
     */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;

}
