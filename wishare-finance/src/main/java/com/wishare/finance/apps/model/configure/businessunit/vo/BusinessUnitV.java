package com.wishare.finance.apps.model.configure.businessunit.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 业务单元返回信息
 *
 * @author
 */
@Getter
@Setter
@ApiModel("业务单元返回信息")
public class BusinessUnitV extends Tree<BusinessUnitV, Long> {

    @ApiModelProperty(value = "业务单元id")
    private Long id;

    @ApiModelProperty(value = "父业务单元id")
    private Long parentId;

    @ApiModelProperty("业务单元编码")
    private String code;

    @ApiModelProperty("业务单元名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新人id")
    private String operator;

    @ApiModelProperty("更新人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;


}
