package com.wishare.finance.apps.model.configure.businessunit.fo;

import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建业务单元入参
 *
 * @author
 */
@Getter
@Setter
@ApiModel("查询业务单元数据")
public class QueryBusinessUnitF {

    @ApiModelProperty(value = "业务单元名称")
    private String name;

    @ApiModelProperty(value = "业务单元编码")
    private String code;

    @ApiModelProperty(value = "业务单元编码集合")
    private List<String> codes;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("业务单元id")
    private Long id;

    @ApiModelProperty("业务单元ids")
    private List<Long> ids;

    @ApiModelProperty("是否包含关联明细")
    private Boolean containDetail;

    @ApiModelProperty("租户id")
    private String tenantId;
}
