package com.wishare.contract.apps.remote.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 财务组织信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgFinanceRv {

    @ApiModelProperty("财务组织id")
    private Long id;
    @ApiModelProperty("组织中文名称")
    private String nameCn;
    @ApiModelProperty("组织英文名称")
    private String nameEn;
    @ApiModelProperty("营业地址")
    private String address;
    @ApiModelProperty("纳税人类别：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关")
    private Integer taxpayerType;

    @ApiModelProperty("4A法定单位id")
    private String oid;

    @ApiModelProperty("4A法定单位名称")
    private String oidName;
}
