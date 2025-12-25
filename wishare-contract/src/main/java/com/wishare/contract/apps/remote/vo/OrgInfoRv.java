package com.wishare.contract.apps.remote.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-04-14
 */
@Data
@Accessors(chain = true)
public class OrgInfoRv {

    @ApiModelProperty("组织ID 主键")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("组织标准名称")
    private String standardOrgName;
    @ApiModelProperty("标准组织路径")
    private String standardOrgPath;
    @ApiModelProperty("父级ID")
    private Long pid;
    @ApiModelProperty("4A主数据id")
    private String oid;
    @ApiModelProperty("4A主数据名字")
    private String oidName;
    @ApiModelProperty("简称编码")
    private String abbrCode;


}
