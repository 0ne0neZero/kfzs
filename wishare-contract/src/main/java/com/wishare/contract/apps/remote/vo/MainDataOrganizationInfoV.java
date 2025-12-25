package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同4A单位信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class MainDataOrganizationInfoV {
    @ApiModelProperty(value = "ID")
    private String oid;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构编码")
    private String ocode;

}
